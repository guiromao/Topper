package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sun.istack.NotNull;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

import static co.topper.configuration.constants.UserConstants.PROPERTY_AVAILABLE_VOTES;
import static co.topper.configuration.constants.UserConstants.PROPERTY_ID;
import static co.topper.configuration.constants.UserConstants.PROPERTY_LAST_VOTE_DATE;
import static co.topper.configuration.constants.UserConstants.PROPERTY_PASSWORD;
import static co.topper.configuration.constants.UserConstants.PROPERTY_TRACK_VOTES;
import static co.topper.configuration.constants.UserConstants.PROPERTY_USERNAME;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class UserDto {

    private final String emailId;
    private final String username;
    private final String password;
    private final Map<String, Long> trackVotes;
    private final Long availableVotes;
    private final Instant lastVoteDate;

    @JsonCreator
    public UserDto(@NotNull @JsonProperty(PROPERTY_ID) String emailId,
                   @JsonProperty(PROPERTY_USERNAME) String username,
                   @JsonProperty(PROPERTY_PASSWORD) String password,
                   @Nullable @JsonProperty(PROPERTY_TRACK_VOTES) Map<String, Long> trackVotes,
                   @JsonProperty(PROPERTY_AVAILABLE_VOTES) Long availableVotes,
                   @Nullable @JsonProperty(PROPERTY_LAST_VOTE_DATE) Instant lastVoteDate) {
        this.emailId = emailId;
        this.username = username;
        this.password = password;
        this.trackVotes = trackVotes;
        this.availableVotes = availableVotes;
        this.lastVoteDate = lastVoteDate;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "emailId='" + emailId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", trackVotes=" + trackVotes +
                ", availableVotes=" + availableVotes +
                ", lastVoteDate=" + lastVoteDate +
                '}';
    }

    @JsonProperty(PROPERTY_ID)
    public String getEmailId() {
        return emailId;
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
    public Map<String, Long> getTrackVotes() {
        return trackVotes;
    }

    @JsonProperty(PROPERTY_AVAILABLE_VOTES)
    public Long getAvailableVotes() {
        return availableVotes;
    }

    @JsonProperty(PROPERTY_LAST_VOTE_DATE)
    public Instant getLastVoteDate() {
        return lastVoteDate;
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
        return Objects.equals(emailId, userDto.emailId) && username.equals(userDto.username) &&
                password.equals(userDto.password) &&
                Objects.equals(trackVotes, userDto.trackVotes) &&
                availableVotes.equals(userDto.availableVotes) &&
                Objects.equals(lastVoteDate, userDto.lastVoteDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailId, username, password,
                trackVotes, availableVotes, lastVoteDate);
    }

}
