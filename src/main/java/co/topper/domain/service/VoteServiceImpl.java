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

import static co.topper.domain.util.UserUtils.computeAvailableVotes;

@Service
public class VoteServiceImpl implements VoteService {

    private static final Logger LOG = LoggerFactory.getLogger(VoteServiceImpl.class.getName());

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
        Long availableVotes = computeAvailableVotes(user);

        if (availableVotes < vote.getVotes()) {
            throw new NotEnoughAvailableVotesException(vote.getVotes(), availableVotes);
        }

        // Create music entities, if non-existing
        dataManager.handleDataOf(vote);
        LOG.info("Data handled for vote for Track '{}'", vote.getId());

        String trackId = vote.getId();
        Long submittedVotes = vote.getVotes();

        Update postVotesUpdate = createUpdatePostVotes(trackId, submittedVotes, (availableVotes - submittedVotes));
        TrackEntity updatedTrack = trackRepository.vote(trackId, submittedVotes);
        UserEntity updatedUser = userRepository.updateUser(user.getEmailId(), postVotesUpdate);

        LOG.info("Track and User entities updated");

        return new SuccessVoteDto(updatedTrack.getId(), updatedTrack.getName(), updatedUser.getTrackVotes().get(trackId));
    }

    // Update for UserEntity's vote counts updates
    private Update createUpdatePostVotes(String trackId, Long submittedVotes, Long availableVotes) {
        return UpdateBuilder.create()
                .setAvailableVotes(availableVotes)
                .incrementTrackVotes(trackId, submittedVotes)
                .setLastVoteDate(Instant.now())
                .build()
                .orElseThrow(() -> new RuntimeException("Error creating User Update post voting"));
    }

    private UserEntity findUserFromToken(String authHeader) {
        String userEmail = tokenReader.getUserEmail(authHeader.split(" ")[1]);

        return userRepository.findById(userEmail)
                .orElseThrow(() -> new UserEmailNotFoundException(userEmail));
    }

}
