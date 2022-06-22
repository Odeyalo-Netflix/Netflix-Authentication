package com.odeyalo.analog.auth.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String codeValue;
    private LocalDateTime expired;
    private boolean isActivated;

    @OneToOne(cascade = CascadeType.REMOVE)
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String code) {
        this.codeValue = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpired() {
        return expired;
    }

    public void setExpired(LocalDateTime expired) {
        this.expired = expired;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public static CodeBuilder builder() {
        return new CodeBuilder();
    }


    public static final class CodeBuilder {
        private Integer id;
        private String codeValue;
        private LocalDateTime expired;
        private boolean isActivated;
        private User user;

        private CodeBuilder() {
        }

        public CodeBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public CodeBuilder codeValue(String codeValue) {
            this.codeValue = codeValue;
            return this;
        }

        public CodeBuilder expired(LocalDateTime expired) {
            this.expired = expired;
            return this;
        }

        public CodeBuilder isActivated(boolean isActivated) {
            this.isActivated = isActivated;
            return this;
        }

        public CodeBuilder user(User user) {
            this.user = user;
            return this;
        }

        public VerificationCode build() {
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setId(id);
            verificationCode.setCodeValue(codeValue);
            verificationCode.setUser(user);
            verificationCode.expired = this.expired;
            verificationCode.isActivated = this.isActivated;
            return verificationCode;
        }
    }
}
