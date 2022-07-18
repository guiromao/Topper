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

        TrackEntity updatedTrack = trackRepository.vote(trackId, votes);
        UserEntity updatedUser = userRepository.updateVotes(user.getId(), trackId, votes);

        LOG.info("Track and User entities updated");

        return new SuccessVoteDto(updatedTrack.getId(), updatedTrack.getName(), updatedUser.getTrackVotes().get(trackId));
    }

    private Long calculateAvailableVotesOf(UserEntity user) {
        Long userVotes = user.getAvailableVotes();

        Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);
        Instant lastVoteDay = user.getLastVoteAttempt().truncatedTo(ChronoUnit.DAYS);

        if (today.isAfter(lastVoteDay)) {
            userVotes += ChronoUnit.DAYS.between(today, lastVoteDay) * 1000;
            userRepository.updateUser(user.getId(), createAvailableVotesUpdate(userVotes));
        }

        return userVotes;
    }

    // Update User's available votes
    private Update createAvailableVotesUpdate(Long availableVotes) {
        return UpdateBuilder.create()
                //.setAvailableVotes(availableVotes)
                .setLastVoteAttempt(Instant.now().truncatedTo(ChronoUnit.DAYS))
                .build()
                .orElseThrow(() -> new RuntimeException("Error creating User Update for Available votes"));
    }

    private UserEntity findUserFromToken(String authHeader) {
        String userEmail = tokenReader.getUserEmail(authHeader.split(" ")[1]);

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserEmailNotFoundException(userEmail));
    }

}
