package com.sparta.nationofdevelopment.domain.user.service;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.config.PasswordEncoder;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserBirthdayUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserInfoUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.response.UserGetResponseDto;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.Date;
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
        userRoleCheck(authUser);
        Users user = getUsersCheckDeleted(authUser);
        UserGetResponseDto userGetResponseDto=new UserGetResponseDto(
                    user.getEmail(),
                    user.getUsername(),
                    user.getBirthday(),
                    user.getUserRole()
            );
        if(authUser.getUserRole().equals(UserRole.USER)) {
            return userGetResponseDto;
        }
        Integer storeCount = storeRepository.countByUserAndStatus(user, StoreStatus.OPEN);
        List<Store> storeList = storeRepository.findAllByUserAndStatus(user, StoreStatus.OPEN);
        List<String> storeNames = storeList.stream().map(Store::getStoreName).toList();
        userGetResponseDto.addOwnerInfo(storeCount,storeNames);
        return userGetResponseDto;
    }
    @Transactional
    public void updateUserInfo(AuthUser authUser,UserInfoUpdateRequestDto requestDto) {
        Users user = getUsersCheckDeleted(authUser);
        //이름이 문제없으면 nameCheck는 true
        boolean nameCheck = userNameCheck(requestDto.getNewUserName());
        boolean birthdayCheck = userBirthdayCheck(requestDto);

        if(nameCheck&&birthdayCheck) {
            user.updateUserInfo(requestDto.getNewUserName(),requestDto.getNewUserBirthday());
        }
        //이름만 맞는경우
        if(nameCheck&&!birthdayCheck) {
            user.updateUserInfo(requestDto.getNewUserName());
        }
        //생일만 맞는경우
        if(!nameCheck&&birthdayCheck) {
            user.updateUserInfo(requestDto.getNewUserBirthday());
        }
        if(!nameCheck && !birthdayCheck) {
            throw new ApiException(ErrorStatus._INVALID_USER_INFO);
        }
    }

    public boolean userBirthdayCheck(UserInfoUpdateRequestDto requestDto) {
        Date now = new Date();
        if(requestDto.getNewUserBirthday()==null) {
            return false;
        }
        boolean Check=requestDto.getNewUserBirthday().before(now);
        if (!Check) {
            throw new ApiException(ErrorStatus._INVALID_BIRTHDAY);
        }
        return true;
    }

    @Transactional
    public void updatePassword(AuthUser authUser, UserPasswordUpdateRequestDto requestDto) {
        Users user = getUsersCheckDeleted(authUser);

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

    @Transactional
    public void deleteUser(AuthUser authUser, UserDeleteRequestDto requestDto) {
        Users user = getUsersCheckDeleted(authUser);

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus._PASSWORD_NOT_MATCHES);
        }
        user.deleteUser();
    }
    public boolean userNameCheck(String newName) {
        if (newName==null||newName.trim().isEmpty()) {
            return false;
        }
        String nameRegex="^[a-zA-Z0-9]{3,20}$";
        if (!newName.matches(nameRegex)) {
            throw new ApiException(ErrorStatus._INVALID_USER_NAME);
        }
        return true;
    }
    public Users getUsersCheckDeleted(AuthUser authUser) {
        Users user = userRepository.findByEmail(authUser.getEmail()).orElseThrow(()->
                new ApiException(ErrorStatus._NOT_FOUND_EMAIL));
        if(user.getIsDeleted()) {
            throw new ApiException(ErrorStatus._DELETED_USER);
        }
        return user;
    }
    public void userRoleCheck(AuthUser authUser) {
        if(!(authUser.getUserRole().equals(UserRole.USER)||authUser.getUserRole().equals(UserRole.OWNER)||authUser.getUserRole().equals(UserRole.ADMIN))) {
            throw new ApiException(ErrorStatus._INVALID_USER_ROLE);
        }
    }
}

