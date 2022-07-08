package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;
import java.util.Set;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class TrackDto {

    private final String id;
    private final String name;
    private final Set<String> artistIds;

    public TrackDto(String id, String name, Set<String> artistIds) {
        this.id = id;
        this.name = name;
        this.artistIds = artistIds;
    }

    @Override
    public String toString() {
        return "TrackDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artistIds='" + artistIds + '\'' +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TrackDto trackDto = (TrackDto) o;
        return id.equals(trackDto.id) && name.equals(trackDto.name) && artistIds.equals(trackDto.artistIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artistIds);
    }

}
