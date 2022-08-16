package com.odeyalo.analog.auth.repository;

import com.odeyalo.analog.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmailAndPassword(String email, String password);

    Optional<User> findUserByNicknameAndPassword(String nickname, String password);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Optional<User> findUserByNickname(String nickname);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Optional<User> findUserByEmail(String email);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Optional<User> findUserByPhoneNumber(String phoneNumber);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE User u SET u.password = :password WHERE u=:user")
    void updateUserPassword(@Param("user") User user, @Param("password") String password);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE User user SET user.password = :password WHERE user.email=:email")
    void updateUserPassword(@Param("email") String email, @Param("password") String password);
}
