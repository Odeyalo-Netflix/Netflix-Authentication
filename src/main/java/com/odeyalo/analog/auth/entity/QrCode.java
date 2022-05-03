package com.odeyalo.analog.auth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String clientId;
    private String qrCodeValue;
    private boolean isActivated;
    private LocalDateTime expiryTime;

    public QrCode() {}

    public QrCode(String qrCodeValue) {
        this.qrCodeValue = qrCodeValue;
    }

    public QrCode(String qrCodeValue, LocalDateTime expiryTime) {
        this.qrCodeValue = qrCodeValue;
        this.expiryTime = expiryTime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public String getQrCodeValue() {
        return qrCodeValue;
    }

    public void setQrCodeValue(String qrCodeValue) {
        this.qrCodeValue = qrCodeValue;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public static QrCodeBuilder builder() {
        return new QrCodeBuilder();
    }


    public static final class QrCodeBuilder {
        private String clientId;
        private String qrCodeValue;
        private boolean isActivated;
        private LocalDateTime expiryTime;

        private QrCodeBuilder() {}

        public QrCodeBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public QrCodeBuilder qrCodeValue(String qrCodeValue) {
            this.qrCodeValue = qrCodeValue;
            return this;
        }

        public QrCodeBuilder isActivated(boolean isActivated) {
            this.isActivated = isActivated;
            return this;
        }

        public QrCodeBuilder expiryTime(LocalDateTime expiryTime) {
            this.expiryTime = expiryTime;
            return this;
        }

        public QrCode build() {
            QrCode qrCode = new QrCode();
            qrCode.setClientId(clientId);
            qrCode.setQrCodeValue(qrCodeValue);
            qrCode.setExpiryTime(expiryTime);
            qrCode.setActivated(isActivated);
            return qrCode;
        }
    }
}
