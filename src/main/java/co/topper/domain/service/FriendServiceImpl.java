package co.topper.domain.service;

import co.topper.domain.data.converter.FriendConverter;
import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.FriendRequestNotFoundException;
import co.topper.domain.exception.NotFriendsConnectionException;
import co.topper.domain.exception.RequestAlreadySentException;
import co.topper.domain.exception.ResourceNotFoundException;
import co.topper.domain.exception.UserAlreadyFriendsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class FriendServiceImpl implements FriendService {

    private static final String KEY_FRIENDS = "friendsListIds" + ".";
    private static final String KEY_REQUEST_IDS = "requestsReceivedIds" + ".";

    private final DataManager dataManager;
    private final UserRepository userRepository;
    private final FriendConverter friendConverter;

    @Autowired
    public FriendServiceImpl(DataManager dataManager,
                             UserRepository userRepository,
                             FriendConverter friendConverter) {
        this.dataManager = dataManager;
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

        List<TrackEntity> friendTracks = dataManager.getTracks(extractTrackIds(friend));
        List<AlbumEntity> friendAlbums = dataManager.getAlbums(extractAlbums(friendTracks));
        List<ArtistEntity> friendArtists = dataManager.getArtists(extractArtists(friendTracks));

        return friendConverter.toDto(friend,
                friendTracks,
                friendAlbums,
                friendArtists);
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

    private List<String> extractTrackIds(UserEntity user) {
        return user.getTrackVotes().keySet().stream().toList();
    }

    private List<String> extractAlbums(List<TrackEntity> tracks) {
        return tracks.stream()
                .map(TrackEntity::getAlbumId)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<String> extractArtists(List<TrackEntity> tracks) {
        return tracks.stream()
                .map(TrackEntity::getArtistIds)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .toList();
    }

}
