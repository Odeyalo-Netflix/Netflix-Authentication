package com.odeyalo.analog.auth.repository;

import com.odeyalo.analog.auth.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, Integer> {

    Optional<QrCode> findByQrCodeValue(String value);

}
