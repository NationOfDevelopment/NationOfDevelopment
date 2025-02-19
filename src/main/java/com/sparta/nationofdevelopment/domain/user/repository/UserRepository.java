package com.sparta.nationofdevelopment.domain.user.repository;

import com.sparta.nationofdevelopment.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByEmailAndIsDeleted(String email, Boolean isDeleted);
}
