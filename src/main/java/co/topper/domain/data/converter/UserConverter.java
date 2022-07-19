package co.topper.domain.data.converter;

import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static co.topper.configuration.constants.PlatformConstants.*;

@Component
public class UserConverter {

    public UserDto toDto(UserEntity user) {
        return new UserDto(
                user.getId(), user.getUsername(), null, user.getEmail(),
                user.getTrackVotes(), computeAvailableVotes(user), user.getLastVoteAttempt()
        );
    }

    private Long computeAvailableVotes(UserEntity user) {
        Long availableVotes = user.getAvailableVotes();
        Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant lastVoteDay = user.getLastVoteAttempt().truncatedTo(ChronoUnit.DAYS);

        if (today.isAfter(lastVoteDay)) {
            availableVotes += Math.abs(ChronoUnit.DAYS.between(today, lastVoteDay)) * DAILY_VOTES_VALUE;
        }

        return availableVotes;
    }

}
