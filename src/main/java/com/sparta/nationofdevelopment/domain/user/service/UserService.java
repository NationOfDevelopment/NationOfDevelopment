package com.sparta.nationofdevelopment.domain.user.service;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.config.PasswordEncoder;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserBirthdayUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserInfoUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.response.UserGetResponseDto;
import com.sparta.nationofdevelopment.domain.user.dto.response.UserNameUpdateResponseDto;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public UserGetResponseDto getUserInfo(AuthUser authUser) {

        Users user =userRepository.findByEmail(authUser.getEmail()).orElseThrow(()->
                new ApiException(ErrorStatus._NOT_FOUND_EMAIL)
        );
        if(authUser.getUserRole().equals(UserRole.USER)) {
            log.info("UserRole.USER");
            return new UserGetResponseDto(
                    user.getEmail(),
                    user.getUsername(),
                    user.getBirthday(),
                    user.getUserRole()
            );
        }
        if(authUser.getUserRole().equals(UserRole.OWNER)||authUser.getUserRole().equals(UserRole.ADMIN)) {
            Integer storeCount = storeRepository.countByUserAndStatus(user, StoreStatus.OPEN);
            List<Store> storeList = storeRepository.findAllByUserAndStatus(user, StoreStatus.OPEN);
            List<String> storeNames = storeList.stream().map(store -> store.getStoreName()).toList();
            log.info("UserRole.OWNER");
            return new UserGetResponseDto(
                    user.getEmail(),
                    user.getUsername(),
                    user.getBirthday(),
                    user.getUserRole(),
                    storeCount,
                    storeNames
            );
        }
        throw new IllegalArgumentException("Invalid user role");
    }
    @Transactional
    public void updateUserName(AuthUser authUser, UserInfoUpdateRequestDto requestDto) {
        Users user = getUsers(authUser);

        if(requestDto.getNewUserName()==null||requestDto.getNewUserName().trim().isEmpty()) {
            throw new ApiException(ErrorStatus._INVALID_USER_INFO);
        }
        user.updateUserInfo(requestDto.getNewUserName());

    }
    @Transactional
    public void updateBirthday(AuthUser authUser, UserBirthdayUpdateRequestDto requestDto) {
        Users user = getUsers(authUser);

        if (!(requestDto.isBirthdayValid())) {
            throw new ApiException(ErrorStatus._INVALID_BIRTHDAY);
        }
        user.updateUserInfo(requestDto.getBirthday());
    }

    public Users getUsers(AuthUser authUser) {
        Users user =userRepository.findByEmail(authUser.getEmail()).orElseThrow(()->
                new ApiException(ErrorStatus._NOT_FOUND_EMAIL));
        return user;
    }
    @Transactional
    public void updatePassword(AuthUser authUser, UserPasswordUpdateRequestDto requestDto) {
        Users user = getUsers(authUser);

        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus._PASSWORD_NOT_MATCHES);
        }
        if (passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus._PASSWORD_IS_DUPLICATED);
        }
        //변경하려는 비밀번호가 유효성검사를 통과하지못한다면.
        if (!passwordEncoder.isPasswordValid(requestDto.getNewPassword())) {
            throw new ApiException(ErrorStatus._INVALID_PASSWORD_FORM);
        }
        String password = passwordEncoder.encode(requestDto.getNewPassword());
        user.updatePassword(password);
    }
}

