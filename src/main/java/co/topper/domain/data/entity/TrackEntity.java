package co.topper.domain.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.Set;

@Document(collection = TrackEntity.TRACK_COLLECTION)
@TypeAlias(TrackEntity.TRACK_COLLECTION)
@CompoundIndex(name = "votes_idx", def = "{'votes' : 1}")
public class TrackEntity {

    public static final String TRACK_COLLECTION = "track";
    public static final String TRACK_OF_THE_HOUR = "track-of-the-hour";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_ARTIST_IDS = "artistIds";
    private static final String FIELD_ALBUM_ID = "albumId";
    private static final String FIELD_VOTES = "votes";

    @Id
    private final String id;

    @Field(FIELD_NAME)
    private final String name;

    @Field(FIELD_ARTIST_IDS)
    private final Set<String> artistIds;

    @Field(FIELD_ALBUM_ID)
    private final String albumId;

    @Field(FIELD_VOTES)
    private final Long votes;

    public TrackEntity(String id,
                       String name,
                       @Nullable Set<String> artistIds,
                       @Nullable String albumId,
                       Long votes) {
        this.id = id;
        this.name = name;
        this.artistIds = artistIds;
        this.albumId = albumId;
        this.votes = votes;
    }

    public static TrackEntity create(String id, String name, Set<String> artistIds, String albumId) {
        return new TrackEntity(id, name, artistIds, albumId, 0L);
    }

    public static TrackEntity createTrackOfTheHour(String name, Set<String> artistsIds, String albumId) {
        return new TrackEntity(TRACK_OF_THE_HOUR, name, artistsIds, albumId, null);
    }

    @Override
    public String toString() {
        return "TrackEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artistIds=" + artistIds +
                ", albumId='" + albumId + '\'' +
                ", votes=" + votes +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public Set<String> getArtistIds() {
        return artistIds;
    }

    @Nullable
    public String getAlbumId() {
        return albumId;
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
        TrackEntity that = (TrackEntity) o;
        return id.equals(that.id) && name.equals(that.name) && Objects.equals(artistIds, that.artistIds) &&
                Objects.equals(albumId, that.albumId) && votes.equals(that.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artistIds, albumId, votes);
    }

}
