package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Set;

public class TopDto extends TrackDto {

    private static final String PROPERTY_TOTAL_VOTES = "totalVotes";

    private final Long totalVotes;

    public TopDto(@JsonProperty("trackId") String trackId,
                  @JsonProperty("name") String name,
                  @JsonProperty("artists") Set<ArtistDto> artists,
                  @JsonProperty("album") AlbumDto album,
                  @JsonProperty("totalVotes") Long totalVotes) {
        super(trackId, name, artists, album);
        this.totalVotes = totalVotes;
    }

    @Override
    public String toString() {
        return "TopDto{" +
                "totalVotes=" + totalVotes +
                '}';
    }

    @JsonProperty(PROPERTY_TOTAL_VOTES)
    public Long getTotalVotes() {
        return totalVotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TopDto topDto = (TopDto) o;
        return totalVotes.equals(topDto.totalVotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalVotes);
    }

}
