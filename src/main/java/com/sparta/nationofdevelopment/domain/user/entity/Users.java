package com.sparta.nationofdevelopment.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true,length = 50)
    private String email;

    @Column(nullable = false,length = 20)
    private String username;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy.MM.dd")
    private Date birthday;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean isDeleted = false;

    private String password;

    public Users(String email, String username,String password, Date birthday, UserRole userRole) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.userRole = userRole;
    }
    public Users(Long id,String email, String username, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.userRole = userRole;
    }

    public void updateUserInfo(String username, Date birthday) {
        this.username = username;
        this.birthday = birthday;
    }

    public void updateUserInfo(String username) {
        this.username = username;
    }

    public void updateUserInfo(Date birthday) {
        this.birthday = birthday;
    }
    public void updatePassword(String password) {
        this.password = password;
    }

    public void deleteUser() {
        isDeleted = true;
    }
    //AuthUser로부터 Users 엔티티를 만드는 메서드입니다. 비밀번호제외
    public static Users fromAuthUser(AuthUser authUser) {
        return new Users(authUser.getId(), authUser.getEmail(), authUser.getUsername(),authUser.getUserRole());
    }
    public Users(UserRole userRole) {
        this.userRole = userRole;
    }

}
