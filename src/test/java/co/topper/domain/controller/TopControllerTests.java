package co.topper.domain.controller;

import co.topper.AbstractionIntegrationTests;
import co.topper.domain.controller.service.TokenService;
import co.topper.domain.data.dto.TopDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.repository.AlbumRepository;
import co.topper.domain.data.repository.ArtistRepository;
import co.topper.domain.data.repository.TrackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class TopControllerTests extends AbstractionIntegrationTests {

    final String ROOT_PATH = "/topper/v1/top";

    // Test Service class to generate Tokens
    @Autowired
    TokenService tokenService;

    @Autowired
    TrackRepository trackRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    ObjectMapper mapper;

    //@Autowired
    MockMvc mockMvc;

    String token;

    List<TrackEntity> tracks = List.of(
            new TrackEntity("track-1", "track-name-1", Set.of("artist-1"),
                    "album-1", 1000L),
            new TrackEntity("track-2", "track-name-2", Set.of("artist-2", "artist-3"),
                    "album-2", 2000L),
            new TrackEntity("track-3", "track-name-3", Set.of("artist-3"),
                    "album-3", 3000L)
    );

    List<AlbumEntity> albums = List.of(
            new AlbumEntity("album-1", "album-name-1", Set.of("artist-1"), "2022-07-31"),
            new AlbumEntity("album-2", "album-name-2", Set.of("artist-2", "artist-3"), "2022-07-30"),
            new AlbumEntity("album-3", "album-name-3", Set.of("artist-3"), "2022-07-29")
    );

    List<ArtistEntity> artists = List.of(
            new ArtistEntity("artist-1", "artist-name-1", "https://img.com/1.png"),
            new ArtistEntity("artist-2", "artist-name-2", "https://img.com/2.png"),
            new ArtistEntity("artist-3", "artist-name-3", "https://img.com/3.png")
    );

    @BeforeEach
    void setup() {
        teardown();

        trackRepository.saveAll(tracks);
        albumRepository.saveAll(albums);
        artistRepository.saveAll(artists);

        tokenService.createUser();
        token = getToken();
    }

    @AfterEach
    void teardown() {
        trackRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();

        tokenService.deleteUser();
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER", "ADMIN"})
    void testGetTop() throws Exception {
        MvcResult result =
                this.mockMvc.perform(get(ROOT_PATH).header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk()).andReturn();

        String responseJson = result.getResponse().getContentAsString();

        List<TopDto> test = Stream.of(mapper.readValue(responseJson, TopDto[].class)).toList();

        Assertions.assertEquals(tracks.size(), test.size());
    }

    private String getToken() {
        String body = "{\"username\":\"test@mail.com\", \"password\":\"test-123\"}";

        MvcResult result = null;
        try {
            result = mockMvc.perform(MockMvcRequestBuilders.post("/v2/token")
                            .content(body))
                    .andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String response = null;
        try {
            response = result.getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response = response.replace("{\"access_token\": \"", "");
        String token = response.replace("\"}", "");

        return token;
    }
}
