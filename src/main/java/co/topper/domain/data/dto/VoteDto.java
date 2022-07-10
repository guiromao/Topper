package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Set;

public class VoteDto extends TrackDto {

    private static final String PROPERTY_VOTES = "votes";

    private final Long votes;

    @JsonCreator
    public VoteDto(@JsonProperty("trackId") String trackId,
                   @JsonProperty("name") String name,
                   @JsonProperty("artists") Set<ArtistDto> artists,
                   @JsonProperty("album") AlbumDto album,
                   @JsonProperty("votes") Long votes) {
        super(trackId, name, artists, album);
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "VoteDto{" +
                "votes=" + votes +
                '}';
    }

    public Long getVotes() {
        return votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VoteDto voteDto = (VoteDto) o;
        return votes.equals(voteDto.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(votes);
    }
}
