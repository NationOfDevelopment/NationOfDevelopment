package com.sparta.nationofdevelopment.domain.store.service;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.store.dto.request.StoreRequestDto;
import com.sparta.nationofdevelopment.domain.store.dto.response.StoreResponseDto;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthUser authUser;

    @InjectMocks
    private StoreService storeService;

    @Test
    public void 가게_생성_성공() {
        // given
        StoreRequestDto requestDto = new StoreRequestDto();
        AuthUser authUser = new AuthUser(1L, "qhtjd5555@naver.com", "이보성5", UserRole.ADMIN);

        Users users = new Users("qhtjd5555@naver.com", "이보성5", "dlqhtjd@719",
                new Date(), UserRole.OWNER);

        Store store = new Store(requestDto, users);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(users));
        given(storeRepository.countByUserAndStatus(users, StoreStatus.OPEN)).
                willReturn(Math.toIntExact(Long.valueOf(2)));
        given(storeRepository.save(any())).willReturn(store);

        // when
        StoreResponseDto responseDto = storeService.postStore(requestDto, authUser);

        // then
        assertNotNull(responseDto);
    }

    @Test
    public void 가게_생성_실패_가게_개수_초과() {

        // given
        StoreRequestDto requestDto = new StoreRequestDto();
        Users users = new Users(UserRole.OWNER);

        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(users));
        given(storeRepository.countByUserAndStatus(users, StoreStatus.OPEN)).willReturn(3);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            storeService.postStore(requestDto, authUser);


        });
        assertEquals(ErrorStatus._NO_MORE_STORE, exception.getErrorCode());
    }

    @Test
    public void 가게_생성_실패_사용자_미존재() {

        // given
        StoreRequestDto requestDto = new StoreRequestDto();
        AuthUser authUser = new AuthUser(1L, "qhtjd5555@naver.com", "이보성5", UserRole.OWNER);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        ApiException exception = assertThrows(ApiException.class, () -> {
            storeService.postStore(requestDto, authUser);
        });

        // then
        assertEquals(null, exception.getMessage());
    }
}