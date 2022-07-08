package co.topper.domain.data.entity;

import com.mongodb.lang.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Document(collection = ArtistEntity.ARTIST_COLLECTION)
@TypeAlias(ArtistEntity.ARTIST_COLLECTION)
public class ArtistEntity {

    public static final String ARTIST_COLLECTION = "artist";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_IMAGE_URL = "imageUrl";

    @Id
    private final String id;

    @Field(FIELD_NAME)
    private final String name;

    @Field(FIELD_IMAGE_URL)
    private final String imageUrl;

    public ArtistEntity(String id, String name, @Nullable String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl'" + imageUrl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArtistEntity artist = (ArtistEntity) o;
        return id.equals(artist.id) && name.equals(artist.name)
                && Objects.equals(imageUrl, artist.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, imageUrl);
    }

}
