package co.topper.domain.data.entity;

import com.mongodb.lang.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.util.Objects;

@Document(collection = TrackEntity.TRACK_COLLECTION)
@TypeAlias(TrackEntity.TRACK_COLLECTION)
public class TrackEntity {

    public static final String TRACK_COLLECTION = "track";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_ARTIST_ID = "artistId";
    private static final String FIELD_VOTES = "votes";
    private static final String FIELD_COVER_URL = "coverUrl";

    @Id
    private final String id;

    @Field(FIELD_NAME)
    private final String name;

    @Field(FIELD_ARTIST_ID)
    private final String artistId;

    @Field(FIELD_VOTES)
    private final BigInteger votes;

    @Field(FIELD_COVER_URL)
    private final String coverUrl;

    public TrackEntity(String id,
                       String name,
                       String artistId,
                       BigInteger votes,
                       @Nullable String coverUrl) {
        this.id = id;
        this.name = name;
        this.artistId = artistId;
        this.votes = votes;
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artistId='" + artistId + '\'' +
                ", votes=" + votes + '\'' +
                ", coverUrl=" + coverUrl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtistId() {
        return artistId;
    }

    public BigInteger getVotes() {
        return votes;
    }

    @Nullable
    public String getCoverUrl() {
        return coverUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TrackEntity track = (TrackEntity) o;
        return id.equals(track.id) && name.equals(track.name)
                && artistId.equals(track.artistId)
                && votes.equals(track.votes)
                && Objects.equals(coverUrl, track.coverUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artistId, votes, coverUrl);
    }

}
