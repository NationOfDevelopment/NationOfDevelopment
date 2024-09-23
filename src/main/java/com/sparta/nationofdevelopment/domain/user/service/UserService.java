package com.sparta.nationofdevelopment.domain.user.service;

import com.sparta.nationofdevelopment.config.PasswordEncoder;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserGetRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.response.UserGetResponseDto;
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
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;


    public UserGetResponseDto getUserInfo(AuthUser authUser) {

        Users user =userRepository.findByEmail(authUser.getEmail()).orElseThrow(()->
                new InvalidRequestException("Invalid email")
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
        throw new InvalidRequestException("Invalid user role");
    }

}

