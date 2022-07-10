package co.topper.domain.service;

import co.topper.domain.data.dto.SuccessVoteDto;
import co.topper.domain.data.dto.VoteDto;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.data.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final DataManager dataManager;

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

        String trackId = vote.getId();
        Long votes = vote.getVotes();

        TrackEntity updatedTrack = trackRepository.vote(trackId, votes);
        UserEntity updatedUser = userRepository.updateVotes(userId, trackId, votes);

        return new SuccessVoteDto(updatedTrack.getName(), updatedUser.getTrackVotes().get(trackId));
    }

}
