package co.topper.domain.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.util.Objects;

@Document(collection = Track.TRACK_COLLECTION)
@TypeAlias(Track.TRACK_COLLECTION)
public class Track {

    public static final String TRACK_COLLECTION = "track";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_ARTIST_ID = "artistId";
    private static final String FIELD_VOTES = "votes";

    @Id
    private final String id;

    @Field(FIELD_NAME)
    private final String name;

    @Field(FIELD_ARTIST_ID)
    private final String artistId;

    @Field(FIELD_VOTES)
    private final BigInteger votes;

    public Track(String id,
                 String name,
                 String artistId,
                 BigInteger votes) {
        this.id = id;
        this.name = name;
        this.artistId = artistId;
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artistId='" + artistId + '\'' +
                ", votes=" + votes +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Track track = (Track) o;
        return id.equals(track.id) && name.equals(track.name)
                && artistId.equals(track.artistId)
                && votes.equals(track.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artistId, votes);
    }

}
