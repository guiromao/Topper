package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

import static co.topper.configuration.constants.UserConstants.PROPERTY_EMAIL;
import static co.topper.configuration.constants.UserConstants.PROPERTY_ID;
import static co.topper.configuration.constants.UserConstants.PROPERTY_LAST_VOTE_ATTEMPT;
import static co.topper.configuration.constants.UserConstants.PROPERTY_PASSWORD;
import static co.topper.configuration.constants.UserConstants.PROPERTY_TRACK_VOTES;
import static co.topper.configuration.constants.UserConstants.PROPERTY_USERNAME;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class UserDto {

    private final String userId;
    private final String username;
    private final String password;
    private final String email;
    private final Map<String, Long> trackVotes;
    private final Instant lastVoteAttempt;

    @JsonCreator
    public UserDto(@Nullable @JsonProperty(PROPERTY_ID) String userId,
                   @JsonProperty(PROPERTY_USERNAME) String username,
                   @JsonProperty(PROPERTY_PASSWORD) String password,
                   @JsonProperty(PROPERTY_EMAIL) String email,
                   @Nullable @JsonProperty(PROPERTY_TRACK_VOTES) Map<String, Long> trackVotes,
                   @Nullable @JsonProperty(PROPERTY_LAST_VOTE_ATTEMPT) Instant lastVoteAttempt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.trackVotes = trackVotes;
        this.lastVoteAttempt = lastVoteAttempt;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", trackVotes=" + trackVotes +
                ", lastVoteAttempt=" + lastVoteAttempt +
                '}';
    }

    @JsonProperty(PROPERTY_ID)
    public String getUserId() {
        return userId;
    }

    @JsonProperty(PROPERTY_USERNAME)
    public String getUsername() {
        return username;
    }

    @JsonProperty(PROPERTY_PASSWORD)
    public String getPassword() {
        return password;
    }

    @JsonProperty(PROPERTY_EMAIL)
    public String getEmail() {
        return email;
    }

    @JsonProperty(PROPERTY_TRACK_VOTES)
    public Map<String, Long> getTrackVotes() {
        return trackVotes;
    }

    @JsonProperty(PROPERTY_LAST_VOTE_ATTEMPT)
    public Instant getLastVoteAttempt() {
        return lastVoteAttempt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDto userDto = (UserDto) o;
        return Objects.equals(userId, userDto.userId) && username.equals(userDto.username) && password.equals(userDto.password) &&
                email.equals(userDto.email) && Objects.equals(trackVotes, userDto.trackVotes) && Objects.equals(lastVoteAttempt, userDto.lastVoteAttempt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, email, trackVotes, lastVoteAttempt);
    }

}
