package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.Objects;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class ArtistDto implements Serializable {

    private static final long serialVersionUID = 47567657L;
    private static final String PROPERTY_ARTIST_ID = "artistId";
    private static final String PROPERTY_ARTIST_NAME = "name";

    private final String artistId;
    private final String name;

    @JsonCreator
    public ArtistDto(@JsonProperty(PROPERTY_ARTIST_ID) String artistId,
                     @JsonProperty(PROPERTY_ARTIST_NAME) String name) {
        this.artistId = artistId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ArtistDto{" +
                "artistId='" + artistId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getArtistId() {
        return artistId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArtistDto artistDto = (ArtistDto) o;
        return Objects.equals(artistId, artistDto.artistId) && Objects.equals(name, artistDto.name);
    }

    @Override public int hashCode() {
        return Objects.hash(artistId, name);
    }
}
