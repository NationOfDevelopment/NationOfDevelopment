package com.sparta.nationofdevelopment.domain.store.service;


import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.store.dto.request.StoreRequestDto;
import com.sparta.nationofdevelopment.domain.store.dto.response.StoreDetailResponseDto;
import com.sparta.nationofdevelopment.domain.store.dto.response.StoreResponseDto;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreResponseDto postStore(StoreRequestDto requestDto, AuthUser authUser) {

        // 인증된 사용자 가져오기
        Users users = getAuthUsers(authUser);

        // 권한 체크
        checkPermission(users);

        // 운영하는 가게 3개 초과 체크
        checkStoreLimit(users);

        Store store = new Store(requestDto, users);
        Store save = storeRepository.save(store);

        return new StoreResponseDto(save);

    }


    @Transactional
    public StoreResponseDto updateStore(AuthUser authUser, Long storeId,
                                        StoreRequestDto requestDto) {

        // 가게 조회
        Store store = getStoreId(storeId);

        // 인증된 사용자 가져오기
        Users users = getAuthUsers(authUser);

        // 권한 체크
        checkPermission(users);

        // 가게 정보 업데이트
        store.update(requestDto);
        storeRepository.save(store);
        return new StoreResponseDto(store);
    }


    // 가게 단건 조회
    public StoreDetailResponseDto getStore(Long storeId) {

        // 가게 조회
        Store store = getStoreId(storeId);

        // 폐업 상태 확인
        checkClosureStore(store);

        return new StoreDetailResponseDto(store);
    }


    // 가게 다건 조회
    public List<StoreResponseDto> getStores(String storeName) {
        List<Store> stores = storeRepository.findByStoreNameContaining(storeName);

        return stores.stream()
                .map(StoreResponseDto::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public StoreResponseDto deleteStore(Long storeId, AuthUser authUser) {

        Store store = getStoreId(storeId);
        Users users = getAuthUsers(authUser);

        // 권한 체크
        checkPermission(users);

        store.closeStore();
        storeRepository.save(store);

        return new StoreResponseDto(store);
    }


    private void checkStoreLimit(Users users) {
        if (storeRepository.countByUserAndStatus(users, StoreStatus.OPEN) >= 3) {
            throw new ApiException(ErrorStatus._NO_MORE_STORE);
        }
    }

    private Users getAuthUsers(AuthUser authUser) {
        return userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_USER));
    }

    private Store getStoreId(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_STORE));
    }

    private void checkPermission(Users users) {
        if (!(users.getUserRole().equals(UserRole.OWNER) || users.getUserRole().equals(UserRole.ADMIN))) {
            throw new ApiException(ErrorStatus._FORBIDDEN_ACCESS);
        }
    }

    private void checkClosureStore(Store store) {
        if (store.getStatus() == StoreStatus.CLOSED) {
            throw new ApiException(ErrorStatus._CLOSED_STORE);
        }
    }
}

