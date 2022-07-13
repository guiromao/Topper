package co.topper.domain.service;

import co.topper.domain.data.converter.FriendConverter;
import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.entity.UserEntity.UpdateBuilder;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.FriendRequestNotFoundException;
import co.topper.domain.exception.NotFriendsConnectionException;
import co.topper.domain.exception.RequestAlreadySentException;
import co.topper.domain.exception.ResourceNotFoundException;
import co.topper.domain.exception.UserAlreadyFriendsException;
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
        UserEntity possibleFriend = fetchUser(friendId);

        if (possibleFriend.getRequestsReceivedIds().contains(userId)) {
            throw new RequestAlreadySentException(friendId);
        }

        if (possibleFriend.getFriendsListIds().contains(userId)) {
            throw new UserAlreadyFriendsException(friendId);
        }

        userRepository.updateUser(friendId, new Update().push(KEY_REQUEST_IDS, userId));
    }

    @Override
    public void acceptRequest(String userId, String friendId) {
        userRepository.updateUser(userId, new Update().pull(KEY_REQUEST_IDS, friendId));
        userRepository.updateUser(userId, new Update().push(KEY_FRIENDS, friendId));
        userRepository.updateUser(friendId, new Update().push(KEY_FRIENDS, userId));
    }

    @Override
    public void refuseRequest(String userId, String senderId) {
        UserEntity user = fetchUser(userId);

        if (!user.getRequestsReceivedIds().contains(senderId)) {
            throw new FriendRequestNotFoundException(senderId);
        }

        userRepository.updateUser(userId, new Update().pull(KEY_REQUEST_IDS, senderId));
    }

    @Override
    public FriendDto getFriend(String userId, String friendId) {
        UserEntity friend = fetchUser(friendId);

        if (!friend.getFriendsListIds().contains(userId)) {
            throw new NotFriendsConnectionException(userId);
        }

        return friendConverter.toDto(friend);
    }

    @Override
    public void unfriend(String userId, String friendId) {
        UserEntity user = fetchUser(userId);
        UserEntity friend = fetchUser(friendId);

        if (!user.getFriendsListIds().contains(friendId) ||
                !friend.getFriendsListIds().contains(userId)) {
            throw new NotFriendsConnectionException(userId, friendId);
        }

        userRepository.updateUser(userId, new Update().pull(KEY_FRIENDS, friendId));
        userRepository.updateUser(friendId, new Update().pull(KEY_FRIENDS, userId));
    }

    private UserEntity fetchUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId, UserEntity.class));
    }

}
