package co.topper.domain.service;

import co.topper.domain.data.converter.FriendConverter;
import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.entity.UserEntity.UpdateBuilder;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.FriendRequestNotFoundException;
import co.topper.domain.exception.NotFriendsConnectionException;
import co.topper.domain.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FriendServiceImpl implements FriendService {

    private static final String KEY_FRIENDS = "friendsListIds" + ".";
    private static final String KEY_REQUEST_IDS = "requestsReceivedIds" + ".";

    private final UserRepository userRepository;
    private final FriendConverter friendConverter;

    @Autowired
    public FriendServiceImpl(UserRepository userRepository,
                             FriendConverter friendConverter) {
        this.userRepository = userRepository;
        this.friendConverter = friendConverter;
    }


    @Override
    public void sendRequest(String userId, String friendId) {
        UserEntity possibleFriend = userRepository.findById(friendId)
                .orElseThrow(() -> new ResourceNotFoundException(friendId, UserEntity.class));

        if (!possibleFriend.getRequestsReceivedIds().contains(userId) &&
                !possibleFriend.getFriendsListIds().contains(userId)) {
            Set<String> requestIds = possibleFriend.getRequestsReceivedIds();
            requestIds.add(userId);

            Update update = UserEntity.UpdateBuilder.create()
                    .setRequestsReceivedIds(requestIds)
                    .build()
                    .orElseThrow(() -> new RuntimeException("Error creating update for adding friend"));

            userRepository.updateUser(friendId, update);
        }
    }

    @Override
    public void acceptRequest(String userId, String friendId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId, UserEntity.class));
        UserEntity friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ResourceNotFoundException(friendId, UserEntity.class));

        Set<String> userFriends = user.getFriendsListIds();
        Set<String> userReceivedRequests = user.getRequestsReceivedIds();
        Set<String> friendFriends = friend.getFriendsListIds();

        userReceivedRequests.remove(friendId);
        userFriends.add(friendId);
        friendFriends.add(userId);

        Update updateUser = UpdateBuilder.create()
                .setRequestsReceivedIds(userReceivedRequests)
                .setFriendsListIds(userFriends)
                .build()
                .orElseThrow(() -> new RuntimeException("Error creating update for User 1"));

        Update updateFriend = UpdateBuilder.create()
                .setFriendsListIds(friendFriends)
                .build()
                .orElseThrow(() -> new RuntimeException("Error creating update for User 2"));

        userRepository.updateUser(userId, updateUser);
        userRepository.updateUser(friendId, updateFriend);
    }

    @Override
    public void refuseRequest(String userId, String senderId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId, UserEntity.class));

        if (!user.getRequestsReceivedIds().contains(senderId)) {
            throw new FriendRequestNotFoundException(senderId);
        }

        userRepository.updateUser(userId, new Update().pull(KEY_REQUEST_IDS, senderId));
    }

    @Override
    public FriendDto getFriend(String userId, String friendId) {
        UserEntity friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ResourceNotFoundException(friendId, UserEntity.class));

        if (!friend.getFriendsListIds().contains(userId)) {
            throw new NotFriendsConnectionException(userId);
        }

        return friendConverter.toDto(friend);
    }

    @Override
    public void unfriend(String userId, String friendId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId, UserEntity.class));
        UserEntity friend = userRepository.findById(friendId)
                .orElseThrow(() -> new ResourceNotFoundException(friendId, UserEntity.class));

        if (!user.getFriendsListIds().contains(friendId) ||
                !friend.getFriendsListIds().contains(userId)) {
            throw new NotFriendsConnectionException(userId, friendId);
        }

        userRepository.updateUser(userId, new Update().pull(KEY_FRIENDS, friendId));
        userRepository.updateUser(friendId, new Update().pull(KEY_FRIENDS, userId));
    }

}
