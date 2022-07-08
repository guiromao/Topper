package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.lang.Nullable;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

import static co.topper.configuration.constants.UserConstants.PROPERTY_ID;
import static co.topper.configuration.constants.UserConstants.PROPERTY_LAST_LOGIN;
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
    private final Map<String, BigInteger> trackVotes;
    private final Instant lastLogin;

    public UserDto(@Nullable @JsonProperty(PROPERTY_ID) String userId,
                   @JsonProperty(PROPERTY_USERNAME) String username,
                   @JsonProperty(PROPERTY_PASSWORD) String password,
                   @Nullable @JsonProperty(PROPERTY_TRACK_VOTES) Map<String, BigInteger> trackVotes,
                   @Nullable @JsonProperty(PROPERTY_LAST_LOGIN) Instant lastLogin) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.trackVotes = trackVotes;
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", trackVotes=" + trackVotes +
                ", lastLogin=" + lastLogin +
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

    @JsonProperty(PROPERTY_TRACK_VOTES)
    public Map<String, BigInteger> getTrackVotes() {
        return trackVotes;
    }

    @JsonProperty(PROPERTY_LAST_LOGIN)
    public Instant getLastLogin() {
        return lastLogin;
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
        return userId.equals(userDto.userId) && username.equals(userDto.username) && password.equals(userDto.password) &&
                trackVotes.equals(userDto.trackVotes) &&
                lastLogin.equals(userDto.lastLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, trackVotes, lastLogin);
    }

}