package co.topper.domain.service;

import co.topper.domain.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Update;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class RewardsServiceTests {

    @Mock
    UserRepository userRepository;

    @Mock
    TokenReader tokenReader;

    RewardsService rewardsService;

    @BeforeEach
    void setup() {
        rewardsService = new RewardsServiceImpl(userRepository, tokenReader);
    }

    @Test
    void testReward() {
        when(tokenReader.getUserEmail(anyString())).thenReturn("user@mail.com");

        rewardsService.incrementVotesOfUser("token header value", 50000L);

        verify(userRepository, times(1)).updateUser(anyString(), any(Update.class));
    }

}
