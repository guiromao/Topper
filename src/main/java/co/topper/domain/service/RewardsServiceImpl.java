package co.topper.domain.service;

import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class RewardsServiceImpl implements RewardsService {

    private final UserRepository userRepository;
    private final TokenReader tokenReader;

    @Autowired
    public RewardsServiceImpl(UserRepository userRepository,
                              TokenReader tokenReader) {
        this.userRepository = userRepository;
        this.tokenReader = tokenReader;
    }

    @Override
    public void incrementVotesOfUser(String authHeader, Long rewardedVotes) {
        String userEmail = tokenReader.getUserEmail(authHeader.split(" ")[1]);
        userRepository.updateUser(userEmail, rewardUpdateOfUser(rewardedVotes));
    }

    private Update rewardUpdateOfUser(Long incrementedVotes) {
        return UserEntity.UpdateBuilder.create()
                .incrementAvailableVotes(incrementedVotes)
                .build()
                .orElseThrow(() -> new RuntimeException("Unable to create update for incrementing user's votes"));
    }

}
