package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class TrackDto implements Serializable {

    private static final long serialVersionUID = 7156526077883281623L;

    private static final String PROPERTY_ID = "trackId";
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_ARTISTS = "artists";
    private static final String PROPERTY_ALBUM = "album";

    private final String id;
    private final String name;
    private final Set<ArtistDto> artists;
    private final AlbumDto album;

    @JsonCreator
    public TrackDto(@JsonProperty(PROPERTY_ID) String id,
                    @JsonProperty(PROPERTY_NAME) String name,
                    @Nullable @JsonProperty(PROPERTY_ARTISTS) Set<ArtistDto> artists,
                    @Nullable @JsonProperty(PROPERTY_ALBUM) AlbumDto album) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.album = album;
    }

    @Override
    public String toString() {
        return "TrackDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artists=" + artists +
                ", album=" + album +
                '}';
    }

    @JsonProperty(PROPERTY_ID)
    public String getId() {
        return id;
    }

    @JsonProperty(PROPERTY_NAME)
    public String getName() {
        return name;
    }

    @Nullable
    @JsonProperty(PROPERTY_ARTISTS)
    public Set<ArtistDto> getArtists() {
        return artists;
    }

    @Nullable
    @JsonProperty(PROPERTY_ALBUM)
    public AlbumDto getAlbum() {
        return album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TrackDto trackDto = (TrackDto) o;
        return id.equals(trackDto.id) && name.equals(trackDto.name) &&
                Objects.equals(artists, trackDto.artists) &&
                Objects.equals(album, trackDto.album);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artists, album);
    }

}
