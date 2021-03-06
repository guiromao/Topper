package co.topper.domain.service;

import co.topper.domain.data.converter.FriendConverter;
import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.FriendRequestNotFoundException;
import co.topper.domain.exception.NonExistingFriendRequestException;
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
    private final TokenReader tokenReader;

    @Autowired
    public FriendServiceImpl(DataManager dataManager,
                             UserRepository userRepository,
                             FriendConverter friendConverter,
                             TokenReader tokenReader) {
        this.dataManager = dataManager;
        this.userRepository = userRepository;
        this.friendConverter = friendConverter;
        this.tokenReader = tokenReader;
    }


    @Override
    public void sendRequest(String authHeader, String friendId) {
        UserEntity user = fetchUserByToken(authHeader.split(" ")[1]);
        UserEntity possibleFriend = fetchUserById(friendId);

        if (possibleFriend.getRequestsReceivedIds().contains(user.getEmailId())) {
            throw new RequestAlreadySentException(friendId);
        }

        if (possibleFriend.getFriendsListIds().contains(user.getEmailId())) {
            throw new UserAlreadyFriendsException(friendId);
        }

        userRepository.updateUser(friendId, new Update().push(KEY_REQUEST_IDS, user.getEmailId()));
    }

    @Override
    public void acceptRequest(String authHeader, String friendId) {
        UserEntity user = fetchUserByToken(authHeader.split(" ")[1]);

        // If does not have friend request from user with ID 'friendId'
        if (!user.getRequestsReceivedIds().contains(friendId)) {
            throw new NonExistingFriendRequestException(friendId);
        }

        // Remove both emailId's from requests, and adding each other to list of friends
        userRepository.updateUser(user.getEmailId(),
                new Update().pull(KEY_REQUEST_IDS, friendId).push(KEY_FRIENDS, friendId));
        userRepository.updateUser(friendId,
                new Update().pull(KEY_REQUEST_IDS, user.getEmailId()).push(KEY_FRIENDS, user.getEmailId()));
    }

    @Override
    public void refuseRequest(String authHeader, String senderId) {
        UserEntity user = fetchUserByToken(authHeader.split(" ")[1]);

        if (!user.getRequestsReceivedIds().contains(senderId)) {
            throw new FriendRequestNotFoundException(senderId);
        }

        userRepository.updateUser(user.getEmailId(), new Update().pull(KEY_REQUEST_IDS, senderId));
    }

    @Override
    public FriendDto getFriend(String authHeader, String friendId) {
        UserEntity user = fetchUserByToken(authHeader.split(" ")[1]);
        UserEntity friend = fetchUserById(friendId);

        if (!friend.getFriendsListIds().contains(user.getEmailId())) {
            throw new NotFriendsConnectionException(friendId);
        }

        List<TrackEntity> friendTracks = dataManager.getTracks(extractTrackIds(friend));
        List<AlbumEntity> friendAlbums = dataManager.getAlbums(extractAlbums(friendTracks));
        List<ArtistEntity> friendArtists = dataManager.getArtists(extractArtists(friendTracks));

        return friendConverter.toDto(friend,
                friend.getTrackVotes(),
                friendTracks,
                friendAlbums,
                friendArtists);
    }

    @Override
    public void unfriend(String authHeader, String friendId) {
        UserEntity user = fetchUserByToken(authHeader.split(" ")[1]);
        UserEntity friend = fetchUserById(friendId);

        if (!user.getFriendsListIds().contains(friendId) ||
                !friend.getFriendsListIds().contains(user.getEmailId())) {
            throw new NotFriendsConnectionException(friendId);
        }

        userRepository.updateUser(user.getEmailId(), new Update().pull(KEY_FRIENDS, friendId));
        userRepository.updateUser(friendId, new Update().pull(KEY_FRIENDS, user.getEmailId()));
    }

    private UserEntity fetchUserByToken(String token) {
        String userEmail = tokenReader.getUserEmail(token);

        return fetchUserById(userEmail);
    }

    private UserEntity fetchUserById(String id) {
       return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, UserEntity.class));
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
