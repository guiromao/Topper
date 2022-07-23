package co.topper.domain.data.converter;

import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.UserEntity;
import org.springframework.stereotype.Component;

import static co.topper.domain.util.UserUtils.computeAvailableVotes;

@Component
public class UserConverter {

    public UserDto toDto(UserEntity user) {
        return new UserDto(
                user.getEmailId(), user.getUsername(), null,
                user.getTrackVotes(), computeAvailableVotes(user), user.getLastVoteDate()
        );
    }

}
