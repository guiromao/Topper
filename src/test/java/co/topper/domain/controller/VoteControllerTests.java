package co.topper.domain.controller;

import co.topper.AbstractionIntegrationTests;
import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.SuccessVoteDto;
import co.topper.domain.data.dto.VoteDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.AlbumRepository;
import co.topper.domain.data.repository.ArtistRepository;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

class VoteControllerTests extends AbstractionIntegrationTests {

    static final String ROOT_PATH = "/topper/v1/vote";

    @Autowired
    UserRepository userRepository;

    @Autowired
    TrackRepository trackRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ObjectMapper mapper;

    String password = "pass";

    UserEntity user = new UserEntity(
            "user-123@mail.com",
            "username",
            new BCryptPasswordEncoder().encode(password),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptyMap(),
            10000L, Instant.now(),
            Set.of(Role.USER)
    );

    VoteDto voteDto = new VoteDto(
            "track-1", "track-name",
            Set.of(new ArtistDto("artist-id-1", "artist-1")),
            new AlbumDto("album-id-1", "album-name", Set.of("artist-1"),
                    "2022-08-14"), 9000L
    );

    TrackEntity track = new TrackEntity("track-1", "track-name",
            Set.of("artist-id-1"), "album-id-1", 2000L);

    AlbumEntity album = new AlbumEntity("album-id-1", "album-name",
            Set.of("artist-id-1"), "2022-08-14");

    ArtistEntity artist = new ArtistEntity("artist-id-1", "artist-1", "https://image-com/1.png");

    String token;
    Headers headers;

    @BeforeEach
    void setup() throws Exception {
        teardown();

        userRepository.save(user);
        trackRepository.save(track);
        albumRepository.save(album);
        artistRepository.save(artist);

        token = getToken(user.getEmailId(), password);
        headers = new Headers(new Header("Authorization", "Bearer " + token));
    }

    @BeforeEach
    void teardown() {
        userRepository.deleteAll();
        trackRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    void testNotEnoughVotes() throws Exception {
        VoteDto tooManyVotes = new VoteDto(
                "track-1", "track-name",
                Set.of(new ArtistDto("artist-id-1", "artist-1")),
                new AlbumDto("album-id-1", "album-name", Set.of("artist-1"),
                        "2022-08-14"), 30000L
        );

        String largeVoteJson = mapper.writeValueAsString(tooManyVotes);

        String responseJson = post(ROOT_PATH, headers, largeVoteJson, withStatus(HttpStatus.BAD_REQUEST))
                .extract().asString();

        ErrorResponse errorResponse = mapper.readValue(responseJson, ErrorResponse.class);

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getCode());
    }

    @Test
    void testVote() throws Exception {
        String voteJson = mapper.writeValueAsString(voteDto);
        String responseJson = post(ROOT_PATH, headers, voteJson, responseOk())
                .extract().asString();

        SuccessVoteDto test = mapper.readValue(responseJson, SuccessVoteDto.class);

        Assertions.assertNotNull(test);
        Assertions.assertEquals(track.getId(), test.getTrackId());
        Assertions.assertEquals(track.getVotes() + voteDto.getVotes(),
                trackRepository.findById(voteDto.getId()).get().getVotes());
    }

}
