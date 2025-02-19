package com.sparta.nationofdevelopment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        if (url.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String bearerJwt = httpRequest.getHeader("Authorization");
            if (bearerJwt == null) {
                // 토큰이 없는 경우 400을 반환합니다.
                // httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
                jwtExceptionHandler(httpResponse, ErrorStatus._NOT_FOUND_TOKEN);
                return;
            }

            String jwt = jwtUtil.substringToken(bearerJwt);

            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                // httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                jwtExceptionHandler(httpResponse, ErrorStatus._BAD_REQUEST_ILLEGAL_TOKEN);
                return;
            }

            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

            httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
            httpRequest.setAttribute("email", claims.get("email"));
            httpRequest.setAttribute("userRole", claims.get("userRole"));
            httpRequest.setAttribute("userName", claims.get("userName"));

            if (url.startsWith("/admin")) {
                // 관리자 권한이 없는 경우 403을 반환합니다.
                if (!UserRole.ADMIN.equals(userRole)) {
                    // httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
                    jwtExceptionHandler(httpResponse, ErrorStatus._FORBIDDEN_TOKEN);
                    return;
                }
                chain.doFilter(request, response);
                return;
            }

            chain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._UNAUTHORIZED_INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._UNAUTHORIZED_EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._BAD_REQUEST_UNSUPPORTED_TOKEN);
        } catch (Exception e) {
            log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
            jwtExceptionHandler(httpResponse, ErrorStatus._BAD_REQUEST_ILLEGAL_TOKEN);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public void jwtExceptionHandler(HttpServletResponse response, ErrorStatus errorStatus) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(errorStatus.getStatusCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            ApiResponse<String> responseMessage = ApiResponse.onFailure(errorStatus);
            response.getWriter().write(mapper.writeValueAsString(responseMessage));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
