package co.topper.domain.service;

import co.topper.domain.data.dto.SuccessVoteDto;
import co.topper.domain.data.dto.VoteDto;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.UserEmailNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        dataManager.handleDataOf(vote);
        LOG.info("Data handled for vote for Track '{}'", vote.getId());

        String trackId = vote.getId();
        Long votes = vote.getVotes();

        String userId = findUserId(authHeader);

        TrackEntity updatedTrack = trackRepository.vote(trackId, votes);
        UserEntity updatedUser = userRepository.updateVotes(userId, trackId, votes);

        LOG.info("Track and User entities updated");

        return new SuccessVoteDto(updatedTrack.getId(), updatedTrack.getName(), updatedUser.getTrackVotes().get(trackId));
    }

    private String findUserId(String authHeader) {
        String userEmail = tokenReader.getUserEmail(authHeader);
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserEmailNotFoundException(userEmail));

        return user.getId();
    }

}
