package co.topper.domain.data.entity;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;
import java.util.Set;

@Document(collection = AlbumEntity.ALBUM_COLLECTION)
@TypeAlias(AlbumEntity.ALBUM_COLLECTION)
public class AlbumEntity {

    public static final String ALBUM_COLLECTION = "album";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_ARTIST_IDS = "artistIds";
    private static final String FIELD_RELEASE_DATE = "releaseDate";

    private final String id;

    @Field(FIELD_NAME)
    private final String name;

    @Field(FIELD_ARTIST_IDS)
    private final Set<String> artistIds;

    @Field(FIELD_RELEASE_DATE)
    private final String releaseDate;

    public AlbumEntity(String id, String name,
                       Set<String> artistIds,
                       String releaseDate) {
        this.id = id;
        this.name = name;
        this.artistIds = artistIds;
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "AlbumEntity{" +
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
        AlbumEntity that = (AlbumEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
                Objects.equals(artistIds, that.artistIds) &&
                Objects.equals(releaseDate, that.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artistIds, releaseDate);
    }
}
