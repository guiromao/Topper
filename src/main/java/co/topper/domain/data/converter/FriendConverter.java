package co.topper.domain.data.converter;

import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.data.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class FriendConverter {

    public FriendDto toDto(UserEntity user) {
        return new FriendDto(
            user.getId(),
            user.getUsername(),
            user.getTrackVotes()
        );
    }

}
