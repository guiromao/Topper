package co.topper.domain.service;

import co.topper.domain.data.converter.FriendConverter;
import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.entity.UserEntity.UpdateBuilder;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.NotFriendsConnectionException;
import co.topper.domain.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final FriendConverter friendConverter;

    @Autowired
    public FriendServiceImpl(UserRepository userRepository,
                             FriendConverter friendConverter) {
        this.userRepository = userRepository;
        this.friendConverter = friendConverter;
    }


    @Override
    public void sendRequest(String userId, String targetId) {
        UserEntity possibleFriend = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException(targetId, UserEntity.class));

        if (!possibleFriend.getRequestsReceivedIds().contains(userId) &&
                !possibleFriend.getFriendsListIds().contains(userId)) {
            Set<String> requestIds = possibleFriend.getRequestsReceivedIds();
            requestIds.add(userId);

            Update update = UserEntity.UpdateBuilder.create()
                    .setRequestsReceivedIds(requestIds)
                    .build()
                    .orElseThrow(() -> new RuntimeException("Error creating update for adding friend"));

            userRepository.updateUser(targetId, update);
        }
    }

    @Override
    public void acceptRequest(String userId, String targetId) {
        UserEntity user1 = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId, UserEntity.class));
        UserEntity user2 = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException(targetId, UserEntity.class));

        Set<String> user1Friends = user1.getFriendsListIds();
        Set<String> user1ReceivedRequests = user1.getRequestsReceivedIds();
        Set<String> user2Friends = user2.getFriendsListIds();

        user1ReceivedRequests.remove(targetId);
        user1Friends.add(targetId);
        user2Friends.add(userId);

        Update updateUser1 = UpdateBuilder.create()
                .setRequestsReceivedIds(user1ReceivedRequests)
                .setFriendsListIds(user1Friends)
                .build()
                .orElseThrow(() -> new RuntimeException("Error creating update for User 1"));

        Update updateUser2 = UpdateBuilder.create()
                .setFriendsListIds(user2Friends)
                .build()
                .orElseThrow(() -> new RuntimeException("Error creating update for User 2"));

        userRepository.updateUser(userId, updateUser1);
        userRepository.updateUser(targetId, updateUser2);
    }

    @Override
    public FriendDto getFriend(String userId, String targetId) {
        UserEntity friend = userRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException(targetId, UserEntity.class));

        if (!friend.getFriendsListIds().contains(userId)) {
            throw new NotFriendsConnectionException(userId);
        }

        return friendConverter.toDto(friend);
    }

}
