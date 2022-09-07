package com.odeyalo.analog.auth.service.events.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.events.Event;
import com.odeyalo.support.clients.common.Role;
import com.odeyalo.support.clients.common.UserInformationDTO;
import com.odeyalo.support.clients.common.UserRegisteredDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Registry user entity in user microservice using kafka
 */
@Service
public class UserMicroserviceRegistryUserRegisteredEventHandler extends AbstractUserRegisteredEventHandler {
    private final KafkaTemplate<String, UserRegisteredDTO> template;

    @Autowired
    public UserMicroserviceRegistryUserRegisteredEventHandler(KafkaTemplate<String, UserRegisteredDTO> template) {
        this.template = template;
    }

    @Override
    public void handleEvent(Event event) {
        boolean isEventCorrect = checkIncomingEvent(event);
        if (!isEventCorrect) {
            return;
        }
        UserRegisteredEvent userRegisteredEvent = (UserRegisteredEvent) event;
        User user = userRegisteredEvent.getUser();
        UserInformationDTO info = buildUserInformationDTO(user);
        this.template.send("USER_REGISTERED_TOPIC", new UserRegisteredDTO(info));
    }

    protected UserInformationDTO buildUserInformationDTO(User user) {
        Set<Role> roles = convertToDtoRoles(user.getRoles());
        return UserInformationDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .isUserBanned(user.isUserBanned())
                .isAccountActivated(user.isAccountActivated())
                .phoneNumber(user.getPhoneNumber())
                .roles(roles)
                .image(user.getImage())
                .build();
    }
    protected Set<Role> convertToDtoRoles (Set<com.odeyalo.analog.auth.entity.enums.Role> roles) {
        return roles.stream().map(x -> Role.valueOf(x.name())).collect(Collectors.toSet());
    }
}
