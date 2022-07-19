package co.topper.domain.service;

import co.topper.domain.data.dto.SuccessVoteDto;
import co.topper.domain.data.dto.VoteDto;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.entity.UserEntity.UpdateBuilder;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.NotEnoughAvailableVotesException;
import co.topper.domain.exception.UserEmailNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static co.topper.configuration.constants.PlatformConstants.*;

@Service
public class VoteServiceImpl implements VoteService {

    private static final Logger LOG = LoggerFactory.getLogger(VoteServiceImpl.class.getName());

    private static final String FIELD_TRACK_VOTES = "trackVotes";

    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final DataManager dataManager;
    private final TokenReader tokenReader;

    @Autowired
    public VoteServiceImpl(TrackRepository trackRepository,
                           UserRepository userRepository,
                           DataManager dataManager,
                           TokenReader tokenReader) {
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.dataManager = dataManager;
        this.tokenReader = tokenReader;
    }

    @Override
    public SuccessVoteDto vote(VoteDto vote, String authHeader) {
        UserEntity user = findUserFromToken(authHeader);
        Long availableVotes = calculateAvailableVotesOf(user);

        if (availableVotes < vote.getVotes()) {
            throw new NotEnoughAvailableVotesException(vote.getVotes(), availableVotes);
        }

        dataManager.handleDataOf(vote);
        LOG.info("Data handled for vote for Track '{}'", vote.getId());

        String trackId = vote.getId();
        Long votes = vote.getVotes();

        Update postVotesUpdate = createUpdatePostVotes(trackId, votes);
        TrackEntity updatedTrack = trackRepository.vote(trackId, votes);
        UserEntity updatedUser = userRepository.updateUser(user.getId(), postVotesUpdate);

        LOG.info("Track and User entities updated");

        return new SuccessVoteDto(updatedTrack.getId(), updatedTrack.getName(), updatedUser.getTrackVotes().get(trackId));
    }

    private Long calculateAvailableVotesOf(UserEntity user) {
        Long userVotes = user.getAvailableVotes();

        Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant lastVoteDay = user.getLastVoteAttempt().truncatedTo(ChronoUnit.DAYS);

        if (today.isAfter(lastVoteDay)) {
            userVotes += Math.abs(ChronoUnit.DAYS.between(lastVoteDay, today)) * DAILY_VOTES_VALUE;
            userRepository.updateUser(user.getId(), createAvailableVotesUpdate(userVotes));
        }

        return userVotes;
    }

    // Update User's available votes
    private Update createAvailableVotesUpdate(Long availableVotes) {
        return UpdateBuilder.create()
                .setAvailableVotes(availableVotes)
                .setLastVoteAttempt(Instant.now())
                .build()
                .orElseThrow(() -> new RuntimeException("Error creating User Update for Available votes"));
    }

    // Update for UserEntity's vote counts updates
    private Update createUpdatePostVotes(String trackId, Long submittedVotes) {
        return UpdateBuilder.create()
                .decrementAvailableVotes(submittedVotes)
                .incrementTrackVotes(trackId, submittedVotes)
                .build()
                .orElseThrow(() -> new RuntimeException("Error creating User Update post voting"));
    }

    private UserEntity findUserFromToken(String authHeader) {
        String userEmail = tokenReader.getUserEmail(authHeader.split(" ")[1]);

        return userRepository.findByEmailNoCache(userEmail)
                .orElseThrow(() -> new UserEmailNotFoundException(userEmail));
    }

}
