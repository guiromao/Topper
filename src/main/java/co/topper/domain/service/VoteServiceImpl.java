package co.topper.domain.service;

import co.topper.domain.data.dto.SuccessVoteDto;
import co.topper.domain.data.dto.VoteDto;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.data.repository.UserRepository;
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

    @Autowired
    public VoteServiceImpl(TrackRepository trackRepository,
                           UserRepository userRepository,
                           DataManager dataManager) {
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
        this.dataManager = dataManager;
    }

    @Override
    public SuccessVoteDto vote(VoteDto vote, String userId) {
        dataManager.handleDataOf(vote);
        LOG.info("Data handled for vote for Track '{}'", vote.getId());

        String trackId = vote.getId();
        Long votes = vote.getVotes();

        TrackEntity updatedTrack = trackRepository.vote(trackId, votes);
        UserEntity updatedUser = userRepository.updateVotes(userId, trackId, votes);

        LOG.info("Track and User entities updated");

        return new SuccessVoteDto(updatedTrack.getId(), updatedTrack.getName(), updatedUser.getTrackVotes().get(trackId));
    }

}
