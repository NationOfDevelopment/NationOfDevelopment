package com.sparta.nationofdevelopment.domain.user.entity;

import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false,length = 50)
    private String username;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date birthday;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean isDeleted = false;

    private String password;


}
