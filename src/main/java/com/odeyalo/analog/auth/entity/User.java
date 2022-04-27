package com.odeyalo.analog.auth.entity;

import com.odeyalo.analog.auth.entity.enums.AuthProvider;
import com.odeyalo.analog.auth.entity.enums.Role;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String nickname;
    @Column(length = 3000)
    private String password;
    private boolean isUserBanned;
    private boolean isAccountActivated;
    @Column(unique = true)
    private String phoneNumber;
    @Enumerated(value = EnumType.STRING)
    private AuthProvider authProvider;
    @Enumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Role.class)
    private Set<Role> roles;
    private String image;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUserBanned() {
        return isUserBanned;
    }

    public void setUserBanned(boolean userBanned) {
        isUserBanned = userBanned;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public boolean isAccountActivated() {
        return isAccountActivated;
    }

    public void setAccountActivated(boolean accountActivated) {
        isAccountActivated = accountActivated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isUserBanned == user.isUserBanned && isAccountActivated == user.isAccountActivated && id.equals(user.id) && email.equals(user.email) && nickname.equals(user.nickname) && password.equals(user.password) && authProvider == user.authProvider && roles.equals(user.roles) && image.equals(user.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, nickname, password, isUserBanned, isAccountActivated, authProvider, roles, image);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", isUserBanned=" + isUserBanned +
                ", isAccountActivated=" + isAccountActivated +
                ", authProvider=" + authProvider +
                ", roles=" + roles +
                ", image='" + image + '\'' +
                '}';
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static final class UserBuilder {
        private Integer id;
        private String email;
        private String nickname;
        private String password;
        private boolean banned;
        private AuthProvider authProvider;
        private final Set<Role> roles = new LinkedHashSet<>();
        private String image;
        private boolean activated;
        private String phoneNumber;

        private UserBuilder() {}


        public UserBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder banned(boolean banned) {
            this.banned = banned;
            return this;
        }
        public UserBuilder authProvider(AuthProvider authProvider) {
            this.authProvider = authProvider;
            return this;
        }

        public UserBuilder image(String image) {
            this.image = image;
            return this;
        }
        public UserBuilder activated(boolean activated) {
            this.activated = activated;
            return this;
        }
        public UserBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }
        public UserBuilder role(Role role) {
            this.roles.add(role);
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setEmail(email);
            user.setNickname(nickname);
            user.setPassword(password);
            user.setUserBanned(banned);
            user.setAuthProvider(authProvider);
            user.setImage(image);
            user.setRoles(roles);
            user.setPhoneNumber(phoneNumber);
            user.setAccountActivated(activated);
            return user;
        }
    }
}
