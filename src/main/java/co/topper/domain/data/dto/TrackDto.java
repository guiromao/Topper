package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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

    private final String id;
    private final String name;
    private final Set<String> artistIds;
    private final Set<String> artistsNames;

    public TrackDto(String id, String name, Set<String> artistIds, Set<String> artistsNames) {
        this.id = id;
        this.name = name;
        this.artistIds = artistIds;
        this.artistsNames = artistsNames;
    }

    @Override
    public String toString() {
        return "TrackDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artistIds=" + artistIds +
                ", artistsNames=" + artistsNames +
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

    public Set<String> getArtistsNames() {
        return artistsNames;
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
                artistIds.equals(trackDto.artistIds) && artistsNames.equals(trackDto.artistsNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artistIds, artistsNames);
    }

}
