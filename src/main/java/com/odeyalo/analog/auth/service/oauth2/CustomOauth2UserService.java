package com.odeyalo.analog.auth.service.oauth2;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.oauth2.support.OAuth2UserInfoFactory;
import com.odeyalo.analog.auth.service.oauth2.support.info.Oauth2UserInfo;
import com.odeyalo.analog.auth.service.register.Oauth2RegisterHandlerFactory;
import com.odeyalo.analog.auth.service.support.CustomUserDetails;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.Optional;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    protected final UserRepository userRepository;

    public CustomOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return this.processOauth2User(userRequest, oAuth2User);
        } catch (AuthException e) {
            throw new OAuth2AuthenticationException(e.getMessage());
        }
    }

    protected OAuth2User processOauth2User(OAuth2UserRequest request, OAuth2User oAuth2User) throws AuthException {
        Oauth2UserInfo oauth2UserInfo = OAuth2UserInfoFactory.getOauth2User(request.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oauth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationException("Email not provided");
        }
        Optional<User> userOptional = this.userRepository.findUserByEmail(oauth2UserInfo.getEmail());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getAuthProvider().equals(oauth2UserInfo.getAuthProvider())) {
                throw new OAuth2AuthenticationException("Use " + user.getAuthProvider() + " instead");
            }
            user = this.updateExistedUser(user, oauth2UserInfo);
        } else {
            user = this.registerNewUser(oauth2UserInfo);
        }
        return CustomUserDetails.create(user, oauth2UserInfo.getAuthorities());
    }

    private User updateExistedUser(User user, Oauth2UserInfo oauth2UserInfo) {
        user.setNickname(oauth2UserInfo.getFirstName());
        user.setImage(oauth2UserInfo.getImage());
        return this.userRepository.save(user);
    }

    private User registerNewUser(Oauth2UserInfo oauth2UserInfo) throws AuthException {
        return Oauth2RegisterHandlerFactory.getOauth2RegisterHandler(oauth2UserInfo).register(oauth2UserInfo);
    }
}
