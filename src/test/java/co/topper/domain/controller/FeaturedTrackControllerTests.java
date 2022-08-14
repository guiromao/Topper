package co.topper.domain.controller;

import co.topper.AbstractionIntegrationTests;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.AlbumRepository;
import co.topper.domain.data.repository.ArtistRepository;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.data.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

class FeaturedTrackControllerTests extends AbstractionIntegrationTests {

    static final String ROOT_PATH = "/topper/v1/hour";

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

    TrackEntity featuredTrack = new TrackEntity("featured-track", "track-name",
            Set.of("artist-id-1"), "album-id-1", 2000L);

    AlbumEntity album = new AlbumEntity("album-id-1", "album-name",
            Set.of("artist-id-1"), "2022-08-14");

    ArtistEntity artist = new ArtistEntity("artist-id-1", "artist-1", "https://image-com/1.png");

    String password = "pass";
    UserEntity user = new UserEntity(
            "user-123@mail.com",
            "username",
            new BCryptPasswordEncoder().encode(password),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptyMap(),
            1000L, Instant.now(),
            Set.of(Role.USER)
    );

    String token;
    Headers headers;

    @BeforeEach
    void setup() throws Exception {
        teardown();

        userRepository.save(user);

        trackRepository.save(featuredTrack);
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
    void testGetFeaturedTrack() throws Exception {
        String responseJson = get(ROOT_PATH, headers, responseOk()).extract().asString();

        TrackDto test = mapper.readValue(responseJson, TrackDto.class);

        Assertions.assertNotNull(test);
        Assertions.assertEquals(featuredTrack.getId(), test.getId());
    }
}
