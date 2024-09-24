package com.sparta.nationofdevelopment.domain.user;

import com.sparta.nationofdevelopment.config.PasswordEncoder;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserInfoUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.sparta.nationofdevelopment.domain.user.dto.response.UserGetResponseDto;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import com.sparta.nationofdevelopment.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

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
    @Mock
    private PasswordEncoder passwordEncoder;

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

        assertEquals(UserRole.USER, responseDto.getUserRole());
        assertEquals("asd@gmail.com", responseDto.getEmail());
        assertEquals("testname", responseDto.getUsername());
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
        Integer storeCount = 3;
        Users user = Users.fromAuthUser(authUser);
        Store store1 = new Store("storeName1", 15000, LocalTime.of(10, 0, 0)
                , LocalTime.of(21, 0, 0), StoreStatus.OPEN, user);
        Store store2 = new Store("storeName2", 20000, LocalTime.of(11, 0, 0)
                , LocalTime.of(23, 0, 0), StoreStatus.OPEN, user);
        Store store3 = new Store("storeName3", 30000, LocalTime.of(13, 0, 0)
                , LocalTime.of(1, 0, 0), StoreStatus.OPEN, user);
        List<Store> stores = new ArrayList<>();

        stores.add(store1);
        stores.add(store2);
        stores.add(store3);

        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(user));
        given(storeRepository.countByUserAndStatus(user, StoreStatus.OPEN)).willReturn(storeCount);
        given(storeRepository.findAllByUserAndStatus(user, StoreStatus.OPEN)).willReturn(stores);

        UserGetResponseDto responseDto = userService.getUserInfo(authUser);

        assertEquals(UserRole.OWNER, responseDto.getUserRole());
        assertEquals("asd@gmail.com", responseDto.getEmail());
        assertEquals("testname", responseDto.getUsername());
        assertThat(responseDto.getStoreList()).contains("storeName1", "storeName2", "storeName3");
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

        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.getUserInfo(authUser);
        });

        assertEquals("이메일을 찾을 수 없습니다.", exception.getErrorCode().getReasonHttpStatus().getMessage());
    }

    @Test
    public void nameCheck_정상작동() {
        Date today = new Date();
        UserInfoUpdateRequestDto requestDto = new UserInfoUpdateRequestDto("newName", today);
        boolean check = userService.userNameCheck(requestDto);

        assertTrue(check);
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void nameCheck_이름이_없는경우(String newName) {
        Date today = new Date();
        UserInfoUpdateRequestDto requestDto = new UserInfoUpdateRequestDto(newName, today);
        boolean check = userService.userNameCheck(requestDto);

        assertFalse(check);
    }

    @Test
    public void 생일이_잘못_된_경우() {
        Date today = getDate(2025, 8, 2);
        String newName = "newName";
        UserInfoUpdateRequestDto requestDto = new UserInfoUpdateRequestDto(newName, today);
        ApiException exception = assertThrows(ApiException.class, () -> {
            userService.userBirthdayCheck(requestDto);
        });
        assertEquals(exception.getErrorCode().getReasonHttpStatus().getMessage(), "잘못된 생일 값입니다");


    }

    //Calender클래스로 날짜를 지정한후 Date로 돌려주는 메서드
    public static Date getDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date);
        return new Date(cal.getTimeInMillis());
    }

    @Test
    public void updateUserInfo_정상작동() {
        Long userId = 1L;
        AuthUser authUser = new AuthUser(
                userId,
                "asd@gmail.com",
                "testname",
                UserRole.USER
        );
        Users user = Users.fromAuthUser(authUser);
        Date time = getDate(1999, 7, 20);
        Date time2 = getDate(1999, 7, 21);
        String newName = "newName";
        UserInfoUpdateRequestDto requestDto = new UserInfoUpdateRequestDto(newName, time);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        userService.updateUserInfo(authUser, requestDto);

        assertEquals(user.getUsername(), "newName");
        assertTrue(user.getBirthday().before(time2));
    }

    @Test
    public void updateUserInfo_변경하려는_생일이없는경우() {
        Long userId = 1L;
        AuthUser authUser = new AuthUser(
                userId,
                "asd@gmail.com",
                "testname",
                UserRole.USER
        );
        Users user = Users.fromAuthUser(authUser);
        String newName = "newName";
        UserInfoUpdateRequestDto requestDto = new UserInfoUpdateRequestDto(newName, null);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        userService.updateUserInfo(authUser, requestDto);

        assertEquals(user.getUsername(), "newName");
        assertNull(user.getBirthday());
    }

    @Test
    public void updateUserInfo_변경하려는_이름이없는경우() {
        Long userId = 1L;
        AuthUser authUser = new AuthUser(
                userId,
                "asd@gmail.com",
                "testname",
                UserRole.USER
        );
        Users user = Users.fromAuthUser(authUser);
        Date time = getDate(1999, 7, 20);
        Date now = new Date();
//        String newName = "newName";
        UserInfoUpdateRequestDto requestDto = new UserInfoUpdateRequestDto(null, time);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        userService.updateUserInfo(authUser, requestDto);

        assertEquals(user.getUsername(), "testname");
        assertTrue(user.getBirthday().before(now));
    }

    @Test
    public void updateUserInfo_UserUpdateRequestDto에_둘다_Null_인_경우() {
        Long userId = 1L;
        AuthUser authUser = new AuthUser(
                userId,
                "asd@gmail.com",
                "testname",
                UserRole.USER
        );
        Users user = Users.fromAuthUser(authUser);
        //        String newName = "newName";
        UserInfoUpdateRequestDto requestDto = new UserInfoUpdateRequestDto(null, null);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        ApiException exception = assertThrows(ApiException.class, () -> userService.updateUserInfo(authUser, requestDto));

        assertEquals(exception.getErrorCode().getReasonHttpStatus().getMessage(), "변경하려는 정보가 잘못되었습니다.");
    }

    @Test
    public void updatePassword_정상작동() {
    Long userId = 1L;
    AuthUser authUser = new AuthUser(
            userId,
            "asd@gmail.com",
            "testname",
            UserRole.USER
    );

    UserPasswordUpdateRequestDto requestDto = new UserPasswordUpdateRequestDto("Multiverse22@","Multiverse22!");
    Users user = Users.fromAuthUser(authUser);
    ReflectionTestUtils.setField(user, "password", "$2a$04$UWZFsXAJOiYydM7rMMjE4u7SfvgbKgnILlvmZdMRVsS/C9lys1s8m");
    String newpPassword="$2a$04$eBbek4kK25DJJBTBt69np.DH2qmv1G9YAPSrgEyo3u4kMkQ0.BOle";
    given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
    given(!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())).willReturn(true);
    given(passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())).willReturn(false);
    given(passwordEncoder.isPasswordValid(requestDto.getNewPassword())).willReturn(true);
    given(passwordEncoder.encode(requestDto.getNewPassword())).willReturn(newpPassword);

    userService.updatePassword(authUser, requestDto);


    }
    @Test
    public void deleteUser_정상작동() {
        Long userId = 1L;
        AuthUser authUser = new AuthUser(
                userId,
                "asd@gmail.com",
                "testname",
                UserRole.USER
        );
        Users user = Users.fromAuthUser(authUser);
        ReflectionTestUtils.setField(user, "password", "$2a$04$Oc0v5uwAPcSY/WtY9N3JlebgPOwIOpYDIzjS4QEz6yVVw7OcNOPou");
        String password="Multiverse22@";
        UserDeleteRequestDto requestDto = new UserDeleteRequestDto(password);

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(requestDto.getPassword(),user.getPassword())).willReturn(true);

        userService.deleteUser(authUser, requestDto);

    }
    @Test
    public void getUsersCheckDeleted_메서드_정상작동() {
        Long userId = 1L;
        AuthUser authUser = new AuthUser(
                userId,
                "asd@gmail.com",
                "testname",
                UserRole.USER
        );
        Users user = Users.fromAuthUser(authUser);
        user.deleteUser();

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        ApiException exception = assertThrows(ApiException.class, () -> userService.getUsersCheckDeleted(authUser));

        assertEquals(exception.getErrorCode().getReasonHttpStatus().getMessage(),"탈퇴한 계정입니다.");
    }
}
