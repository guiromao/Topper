package co.topper.domain.service;

import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.SuccessVoteDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.dto.VoteDto;
import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.NotEnoughAvailableVotesException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;
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
class VoteServiceTests {

    private static final String USER_EMAIL_ID = "user@mail.com";
    private static final Long DEFAULT_AVAILABLE_VOTES = 1000L;

    @Mock
    TrackRepository trackRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    DataManager dataManager;

    @Mock
    TokenReader tokenReader;

    VoteService voteService;

    @BeforeEach
    void setup() {
        voteService = new VoteServiceImpl(trackRepository, userRepository,
                dataManager, tokenReader);

        when(tokenReader.getUserEmail(anyString())).thenReturn(USER_EMAIL_ID);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userWithAvailableVotes(DEFAULT_AVAILABLE_VOTES)));
    }

    @Test
    void testInsufficientAvailableVotes() {
        final Long moreVotesThanAvailable = 3000L;
        VoteDto vote = voteWithValue(moreVotesThanAvailable);

        Assertions.assertThrows(
                NotEnoughAvailableVotesException.class,
                () -> voteService.vote(vote, "token header value")
        );

        verify(userRepository, times(0)).updateUser(anyString(), any(Update.class));
        verifyNoInteractions(dataManager);
        verifyNoInteractions(trackRepository);
    }

    @Test
    void testVote() {
        final Long validNumberOfVotes = 500L;
        final VoteDto vote = voteWithValue(validNumberOfVotes);
        final UserEntity user = userWithAvailableVotes(DEFAULT_AVAILABLE_VOTES - 500L);

        when(trackRepository.vote(anyString(), any(Long.class)))
                .thenReturn(trackExample());
        when(userRepository.updateUser(anyString(), any(Update.class)))
                .thenReturn(user);

        SuccessVoteDto test = voteService.vote(vote, "token header value");

        verify(dataManager, times(1)).handleDataOf(any(TrackDto.class));

        Assertions.assertEquals(trackExample().getId(), test.getTrackId());
        Assertions.assertEquals(trackExample().getName(), test.getTrackName());
        Assertions.assertEquals(
                user.getTrackVotes().get(trackExample().getId()),
                test.getTrackVotes()
        );
    }

    private VoteDto voteWithValue(Long numberVotes) {
        return new VoteDto(
            "track-1",
            "track-name",
            Set.of(new ArtistDto("artist-1", "artist-name")),
            new AlbumDto("album-id", "album-name", Set.of("artist-1"), "2022-07-24"),
            numberVotes
        );
    }

    private UserEntity userWithAvailableVotes(Long availableVotes) {
        return new UserEntity(
            USER_EMAIL_ID,
            "username",
            "encoded-pass",
            Set.of("user-2", "user-3"),
            Set.of("user-4"),
            Map.of("track-1", 3000L, "track-2", 7000L),
            availableVotes,
            Instant.now(),
            Set.of(Role.USER)
        );
    }

    private TrackEntity trackExample() {
        return new TrackEntity(
            "track-1",
            "track-name",
            Set.of("artist-1"),
            "album-id",
            1500L
        );
    }

}
