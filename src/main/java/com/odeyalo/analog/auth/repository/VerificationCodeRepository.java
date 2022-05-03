package com.odeyalo.analog.auth.repository;

import com.odeyalo.analog.auth.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Integer> {

    Optional<VerificationCode> findVerificationCodeByUser_Email(String email);

    Optional<VerificationCode> findCodeByCodeValue(String code);

    void deleteByCodeValue(String codeValue);
}
