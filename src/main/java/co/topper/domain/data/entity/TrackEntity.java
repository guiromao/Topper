package co.topper.domain.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.util.Set;

@Document(collection = TrackEntity.TRACK_COLLECTION)
@TypeAlias(TrackEntity.TRACK_COLLECTION)
@CompoundIndex(name = "votes_idx", def = "{'votes' : 1}")
public class TrackEntity {

    public static final String TRACK_COLLECTION = "track";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_ARTIST_IDS = "artistIds";
    private static final String FIELD_VOTES = "votes";

    @Id
    private final String id;

    @Field(FIELD_NAME)
    private final String name;

    @Field(FIELD_ARTIST_IDS)
    private final Set<String> artistIds;

    @Field(FIELD_VOTES)
    private final BigInteger votes;

    public TrackEntity(String id,
                       String name,
                       Set<String> artistIds,
                       BigInteger votes) {
        this.id = id;
        this.name = name;
        this.artistIds = artistIds;
        this.votes = votes;
    }

    public static TrackEntity create(String id, String name, Set<String> artistIds) {
        BigInteger votes = BigInteger.ZERO;

        return new TrackEntity(id, name, artistIds, votes);
    }

    @Override public String toString() {
        return "TrackEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artistIds=" + artistIds +
                ", votes=" + votes +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getArtistId() {
        return artistIds;
    }

    public BigInteger getVotes() {
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
        TrackEntity track = (TrackEntity) o;
        return id.equals(track.id) && name.equals(track.name)
                && artistIds.equals(track.artistIds)
                && votes.equals(track.votes);
    }

}
