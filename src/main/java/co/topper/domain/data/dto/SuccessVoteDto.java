package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class SuccessVoteDto {

    private static final String PROPERTY_TRACK_NAME = "trackName";
    private static final String PROPERTY_TRACK_VOTES = "trackVotes";

    private final String trackName;
    private final Long trackVotes;

    public SuccessVoteDto(String trackName, Long trackVotes) {
        this.trackName = trackName;
        this.trackVotes = trackVotes;
    }

    @Override
    public String toString() {
        return "SuccessVoteDto{" +
                "trackName='" + trackName + '\'' +
                ", trackVotes=" + trackVotes +
                '}';
    }

    @JsonProperty(PROPERTY_TRACK_NAME)
    public String getTrackName() {
        return trackName;
    }

    @JsonProperty(PROPERTY_TRACK_VOTES)
    public Long getTrackVotes() {
        return trackVotes;
    }

    @Override
    public boolean equals(Object o) { 
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuccessVoteDto that = (SuccessVoteDto) o;
        return trackName.equals(that.trackName) && trackVotes.equals(that.trackVotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackName, trackVotes);
    }
}
