package co.topper.domain.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = Artist.ARTIST_COLLECTION)
@TypeAlias(Artist.ARTIST_COLLECTION)
public class Artist {

    public static final String ARTIST_COLLECTION = "artist";

    private static final String FIELD_NAME = "name";

    @Id
    private final String id;

    @Field(FIELD_NAME)
    private final String name;

    public Artist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
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
        Artist artist = (Artist) o;
        return id.equals(artist.id) && name.equals(artist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
