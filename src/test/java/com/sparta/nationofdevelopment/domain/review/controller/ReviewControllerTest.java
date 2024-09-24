package com.sparta.nationofdevelopment.domain.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nationofdevelopment.config.AuthUserArgumentResolver;
import com.sparta.nationofdevelopment.config.GlobalExceptionHandler;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.review.dto.request.ReviewRequestDto;
import com.sparta.nationofdevelopment.domain.review.dto.response.ReviewResponseDto;
import com.sparta.nationofdevelopment.domain.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {
    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new AuthUserArgumentResolver())
                .build();
    }

    @Test
    void post_review_successfully() throws Exception {
        //given
        ReviewRequestDto requestDto = new ReviewRequestDto(3, "good");
        Long orderId = 1L;

        ReviewResponseDto responseDto = new ReviewResponseDto();
        ReflectionTestUtils.setField(responseDto, "reviewId", 1L);
        ReflectionTestUtils.setField(responseDto, "stars", 3);
        ReflectionTestUtils.setField(responseDto, "contents", "good");

        when(reviewService.saveReview(any(ReviewRequestDto.class), any(AuthUser.class), any(Long.class)))
                .thenReturn(responseDto);

        // when
        mockMvc.perform(post("/orders/{orderId}/reviews", orderId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto))
                    .requestAttr("userId", 1L)
                    .requestAttr("email", "test@example.com")
                    .requestAttr("userRole", "User"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['data']['reviewId']").value(1))
                .andDo(print());
    }

    @Test
    void get_reviews_successfully() throws Exception {
        //given
        Long storeId = 1L;

        ReviewResponseDto responseDto = new ReviewResponseDto();
        ReflectionTestUtils.setField(responseDto, "reviewId", 1L);
        ReflectionTestUtils.setField(responseDto, "stars", 3);
        ReflectionTestUtils.setField(responseDto, "contents", "good");

        List<ReviewResponseDto> responseDtos = new ArrayList<>();
        responseDtos.add(responseDto);

        when(reviewService.getReviews(any(Integer.class), any(Integer.class), any(Long.class)))
                .thenReturn(responseDtos);

        // when
        mockMvc.perform(get("/stores/{storeId}/reviews", storeId)
                        .param("min", "1")
                        .param("max", "5")
                        .requestAttr("userId", 1L)
                        .requestAttr("email", "test@example.com")
                        .requestAttr("userRole", "User"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['data'][0]['reviewId']").value(1))
                .andDo(print());
    }

}