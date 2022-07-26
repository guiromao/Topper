package co.topper.domain.service;

import co.topper.domain.data.converter.FriendConverter;
import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.FriendRequestNotFoundException;
import co.topper.domain.exception.NonExistingFriendRequestException;
import co.topper.domain.exception.NotFriendsConnectionException;
import co.topper.domain.exception.RequestAlreadySentException;
import co.topper.domain.exception.UserAlreadyFriendsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class FriendsServiceTests {

    static final String USER_EMAIL_ID = "user_id@mail.com";
    static final String FRIEND_EMAIL_ID = "friend_id@mail.com";
    // There needs to be at least one space in between two words, in the following String
    static final String TOKEN_FOR_TESTS = "auth token";

    final FriendDto FRIEND_DTO = friendDto();

    @Mock
    DataManager dataManager;

    @Mock
    UserRepository userRepository;

    @Mock
    FriendConverter friendConverter;

    @Mock
    TokenReader tokenReader;

    FriendService friendService;

    @BeforeEach
    void setup() {
        friendService = new FriendServiceImpl(dataManager, userRepository,
                friendConverter, tokenReader);

        when(tokenReader.getUserEmail(anyString())).thenReturn(USER_EMAIL_ID);
    }

    @Test
    void testSendRequestAlreadySent() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Collections.emptySet(), Collections.emptySet());
        UserEntity friend = userWithIdFriendsAndRequests(FRIEND_EMAIL_ID,
                Collections.emptySet(), Set.of(USER_EMAIL_ID));

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(FRIEND_EMAIL_ID)).thenReturn(Optional.of(friend));

        Assertions.assertThrows(
                RequestAlreadySentException.class,
                () -> friendService.sendRequest(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID)
        );

        verify(userRepository, times(0)).updateUser(anyString(), any(Update.class));
    }

    @Test
    void testSendRequestAlreadyFriends() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Collections.emptySet(), Collections.emptySet());
        UserEntity friend = userWithIdFriendsAndRequests(FRIEND_EMAIL_ID,
                Set.of(USER_EMAIL_ID), Collections.emptySet());

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(FRIEND_EMAIL_ID)).thenReturn(Optional.of(friend));

        Assertions.assertThrows(
                UserAlreadyFriendsException.class,
                () -> friendService.sendRequest(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID)
        );

        verify(userRepository, times(0)).updateUser(anyString(), any(Update.class));
    }

    @Test
    void testSendRequest() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Collections.emptySet(), Collections.emptySet());
        UserEntity friend = userWithIdFriendsAndRequests(FRIEND_EMAIL_ID,
                Collections.emptySet(), Collections.emptySet());

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(FRIEND_EMAIL_ID)).thenReturn(Optional.of(friend));

        friendService.sendRequest(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID);

        verify(userRepository, times(1)).updateUser(anyString(), any(Update.class));
    }

    @Test
    void testAcceptNonExistingRequest() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Collections.emptySet(), Collections.emptySet());

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));

        Assertions.assertThrows(
                NonExistingFriendRequestException.class,
                () -> friendService.acceptRequest(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID)
        );

        verify(userRepository, times(0)).updateUser(anyString(), any(Update.class));
    }

    @Test
    void testAcceptRequest() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Collections.emptySet(), Set.of(FRIEND_EMAIL_ID));

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));

        friendService.acceptRequest(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID);

        verify(userRepository, times(2)).updateUser(anyString(), any(Update.class));
    }

    @Test
    void testRefuseNonExistingRequest() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Collections.emptySet(), Collections.emptySet());

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));

        Assertions.assertThrows(
                FriendRequestNotFoundException.class,
                () -> friendService.refuseRequest(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID)
        );

        verify(userRepository, times(0)).updateUser(anyString(), any(Update.class));
    }

    @Test
    void testRefuseRequest() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Collections.emptySet(), Set.of(FRIEND_EMAIL_ID));

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));

        friendService.refuseRequest(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID);

        verify(userRepository, times(1)).updateUser(anyString(), any(Update.class));
    }

    @Test
    void testGetNotFriend() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Collections.emptySet(), Collections.emptySet());
        UserEntity friend = userWithIdFriendsAndRequests(FRIEND_EMAIL_ID,
                Collections.emptySet(), Collections.emptySet());

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(FRIEND_EMAIL_ID)).thenReturn(Optional.of(friend));

        Assertions.assertThrows(
                NotFriendsConnectionException.class,
                () -> friendService.getFriend(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID)
        );

        verifyNoInteractions(dataManager);
        verifyNoInteractions(friendConverter);
    }

    @Test
    void testGetFriend() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Set.of(FRIEND_EMAIL_ID), Collections.emptySet());
        UserEntity friend = userWithIdFriendsAndRequests(FRIEND_EMAIL_ID,
                Set.of(USER_EMAIL_ID), Collections.emptySet());

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(FRIEND_EMAIL_ID)).thenReturn(Optional.of(friend));
        when(dataManager.getTracks(any(List.class))).thenReturn(List.of());
        when(dataManager.getAlbums(any(List.class))).thenReturn(List.of());
        when(dataManager.getArtists(any(List.class))).thenReturn(List.of());
        when(friendConverter.toDto(any(UserEntity.class),
                any(Map.class), any(List.class), any(List.class), any(List.class)))
                        .thenReturn(FRIEND_DTO);

        FriendDto test = friendService.getFriend(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID);

        Assertions.assertEquals(FRIEND_DTO, test);
    }

    @Test
    void testUnfriendNotFriends() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Collections.emptySet(), Collections.emptySet());
        UserEntity friend = userWithIdFriendsAndRequests(FRIEND_EMAIL_ID,
                Collections.emptySet(), Collections.emptySet());

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(FRIEND_EMAIL_ID)).thenReturn(Optional.of(friend));

        Assertions.assertThrows(
                NotFriendsConnectionException.class,
                () -> friendService.unfriend(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID)
        );

        verify(userRepository, times(0)).updateUser(anyString(), any(Update.class));
    }

    @Test
    void testUnfriend() {
        UserEntity user = userWithIdFriendsAndRequests(USER_EMAIL_ID,
                Set.of(FRIEND_EMAIL_ID), Collections.emptySet());
        UserEntity friend = userWithIdFriendsAndRequests(FRIEND_EMAIL_ID,
                Set.of(USER_EMAIL_ID), Collections.emptySet());

        when(userRepository.findById(USER_EMAIL_ID)).thenReturn(Optional.of(user));
        when(userRepository.findById(FRIEND_EMAIL_ID)).thenReturn(Optional.of(friend));

        friendService.unfriend(TOKEN_FOR_TESTS, FRIEND_EMAIL_ID);

        verify(userRepository, times(2)).updateUser(anyString(), any(Update.class));
    }

    private UserEntity userWithIdFriendsAndRequests(String emailId,
                                                    Set<String> friendsIds,
                                                    Set<String> receivedRequestsIds) {
        return new UserEntity(
                emailId, "username", "password",
                friendsIds, receivedRequestsIds, Collections.emptyMap(),
                1000L, Instant.now(), Set.of(Role.USER)
        );
    }

    private FriendDto friendDto() {
        return new FriendDto(
                FRIEND_EMAIL_ID, "username", Collections.emptyList()
        );
    }

    private UserDto userDto() {
        return new UserDto(
                USER_EMAIL_ID, "username", "password",
                Collections.emptyMap(), 5000L,
                Instant.now()
        );
    }

}
