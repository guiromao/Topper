package co.topper.domain.util;

import co.topper.domain.data.entity.UserEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class UserUtils {

    public static Long computeAvailableVotes(UserEntity user) {
        Long availableVotes = user.getAvailableVotes();
        Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant lastVoteDate = user.getLastVoteDate().truncatedTo(ChronoUnit.DAYS);

        if (today.isAfter(lastVoteDate)) {
            availableVotes += Math.abs(ChronoUnit.DAYS.between(today, lastVoteDate)) * 1000;
        }

        return  availableVotes;
    }

}
