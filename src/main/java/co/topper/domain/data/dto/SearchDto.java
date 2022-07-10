package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class SearchDto {

    private final String trackName;

    @JsonCreator
    public SearchDto(@JsonProperty("trackName") String trackName) {
        this.trackName = trackName;
    }

    @Override
    public String toString() {
        return "SearchDto{" +
                "trackName='" + trackName + '\'' +
                '}';
    }

    public String getTrackName() {
        return trackName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchDto searchDto = (SearchDto) o;
        return trackName.equals(searchDto.trackName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackName);
    }

}
