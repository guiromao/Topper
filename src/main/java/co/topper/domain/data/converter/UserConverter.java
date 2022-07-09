package co.topper.domain.data.converter;

import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserDto toDto(UserEntity user) {
        return new UserDto(
                user.getId(), user.getUsername(), null, user.getEmail(),
                user.getTrackVotes(), user.getLastLogin()
        );
    }

}
