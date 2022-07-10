package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class AlbumDto implements Serializable {

    private static final long serialVersionUID = 2143214324L;
    private static final String PROPERTY_ALBUM_ID = "id";
    private static final String PROPERTY_ALBUM_NAME = "name";
    private static final String PROPERTY_ARTIST_IDS = "artistIds";
    private static final String PROPERTY_RELEASE_DATE = "releaseDate";

    private final String id;
    private final String name;
    private final Set<String> artistIds;
    private final String releaseDate;

    @JsonCreator
    public AlbumDto(@JsonProperty(PROPERTY_ALBUM_ID) String id,
                    @JsonProperty(PROPERTY_ALBUM_NAME) String name,
                    @JsonProperty(PROPERTY_ARTIST_IDS) Set<String> artistIds,
                    @JsonProperty(PROPERTY_RELEASE_DATE) String releaseDate) {
        this.id = id;
        this.name = name;
        this.artistIds = artistIds;
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "AlbumDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artistIds=" + artistIds +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getArtistIds() {
        return artistIds;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlbumDto albumDto = (AlbumDto) o;
        return id.equals(albumDto.id) && name.equals(albumDto.name) && Objects.equals(artistIds, albumDto.artistIds) &&
                Objects.equals(releaseDate, albumDto.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artistIds, releaseDate);
    }
}
