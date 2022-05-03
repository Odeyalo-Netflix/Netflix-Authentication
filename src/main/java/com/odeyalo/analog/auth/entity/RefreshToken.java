package com.odeyalo.analog.auth.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @Column(unique = true)
    private String refreshToken;
    @Column(nullable = false)
    private Instant expireDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public static RefreshTokenBuilder builder() {
        return new RefreshTokenBuilder();
    }

    public static final class RefreshTokenBuilder {
        private Integer id;
        private User user;
        private String refreshToken;
        private Instant expireDate;

        private RefreshTokenBuilder() {
        }



        public RefreshTokenBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public RefreshTokenBuilder user(User user) {
            this.user = user;
            return this;
        }

        public RefreshTokenBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshTokenBuilder expireDate(Instant expireDate) {
            this.expireDate = expireDate;
            return this;
        }

        public RefreshToken build() {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setId(this.id);
            refreshToken.setUser(this.user);
            refreshToken.setRefreshToken(this.refreshToken);
            refreshToken.setExpireDate(this.expireDate);
            return refreshToken;
        }
    }
}
