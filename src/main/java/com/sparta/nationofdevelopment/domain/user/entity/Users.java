package com.sparta.nationofdevelopment.domain.user.entity;

import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

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

    @Column(nullable = false,length = 50)
    private String username;

    @Temporal(TemporalType.DATE)
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
    public Users(String email, String username, UserRole userRole) {
        this.email = email;
        this.username = username;
        this.userRole = userRole;
    }
    //AuthUser로부터 Users 엔티티를 만드는 메서드입니다. 비밀번호제외
    public static Users fromAuthUser(AuthUser authUser) {
        return new Users(authUser.getEmail(), authUser.getUsername(),authUser.getUserRole());
    }


}
