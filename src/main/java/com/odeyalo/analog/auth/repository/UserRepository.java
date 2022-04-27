package com.odeyalo.analog.auth.repository;

import com.odeyalo.analog.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmailAndPassword(String email, String password);

    Optional<User> findUserByNicknameAndPassword(String nickname, String password);

    Optional<User> findUserByNickname(String nickname);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByPhoneNumber(String phoneNumber);
}
