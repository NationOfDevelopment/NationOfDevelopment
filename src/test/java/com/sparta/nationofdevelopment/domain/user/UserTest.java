package com.sparta.nationofdevelopment.domain.user;

import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.dto.response.UserGetResponseDto;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import com.sparta.nationofdevelopment.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class UserTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;

    @Test
    public void getUserInfo_정상작동_User의경우() {
        Long userId = 1L;
        AuthUser authUser = new AuthUser(
                userId,
                "asd@gmail.com",
                "testname",
                UserRole.USER
        );
        Users user = Users.fromAuthUser(authUser);
        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(user));

        UserGetResponseDto responseDto = userService.getUserInfo(authUser);

        assertEquals(UserRole.USER,responseDto.getUserRole());
        assertEquals("asd@gmail.com",responseDto.getEmail());
        assertEquals("testname",responseDto.getUsername());
    }
    @Test
    public void getUserInfo_정상작동_Owner의경우() {
        Long userId = 1L;
        AuthUser authUser = new AuthUser(
                userId,
                "asd@gmail.com",
                "testname",
                UserRole.OWNER
        );
        Integer storeCount=3;
        Users user = Users.fromAuthUser(authUser);
        Store store1 = new Store("storeName1",15000, LocalTime.of(10,0,0)
                ,LocalTime.of(21,0,0), StoreStatus.OPEN,user);
        Store store2 = new Store("storeName2",20000, LocalTime.of(11,0,0)
                ,LocalTime.of(23,0,0), StoreStatus.OPEN,user);
        Store store3 = new Store("storeName3",30000, LocalTime.of(13,0,0)
                ,LocalTime.of(1,0,0), StoreStatus.OPEN,user);
        List<Store> stores = new ArrayList<>();

        stores.add(store1);
        stores.add(store2);
        stores.add(store3);

        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(user));
        given(storeRepository.countByUserAndStatus(user, StoreStatus.OPEN)).willReturn(storeCount);
        given(storeRepository.findAllByUserAndStatus(user, StoreStatus.OPEN)).willReturn(stores);

        UserGetResponseDto responseDto = userService.getUserInfo(authUser);

        assertEquals(UserRole.OWNER,responseDto.getUserRole());
        assertEquals("asd@gmail.com",responseDto.getEmail());
        assertEquals("testname",responseDto.getUsername());
        assertThat(responseDto.getStoreList()).contains("storeName1","storeName2","storeName3");
        assertThat(responseDto.getStoreCount()).isEqualTo(storeCount);
    }
    @Test
    public void AuthUser의_email로_user를_찾을_수_없을때() {
        Long userId = 1L;
        AuthUser authUser = new AuthUser(
                userId,
                "asd@gmail.com",
                "testname",
                UserRole.USER
        );

        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class,()-> {
                    userService.getUserInfo(authUser);
                });

        assertEquals("이메일을 찾을 수 없습니다.",exception.getErrorCode().getReasonHttpStatus().getMessage());
    }

}
