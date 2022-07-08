package co.topper.domain.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static co.topper.configuration.constants.UserConstants.FIELD_LAST_LOGIN;
import static co.topper.configuration.constants.UserConstants.FIELD_PASSWORD;
import static co.topper.configuration.constants.UserConstants.FIELD_TRACK_VOTES;
import static co.topper.configuration.constants.UserConstants.FIELD_USERNAME;

@Document(collection = UserEntity.USER_COLLECTION)
@TypeAlias(UserEntity.USER_COLLECTION)
public class UserEntity {

    public static final String USER_COLLECTION = "user";

    @Id
    private final String id;

    @Field(FIELD_USERNAME)
    private final String username;

    @Field(FIELD_PASSWORD)
    private final String password;

    @Field(FIELD_TRACK_VOTES)
    private final Map<String, BigInteger> trackVotes;

    @Field(FIELD_LAST_LOGIN)
    private final Instant lastLogin;

    public UserEntity(String id,
                      String username,
                      String password,
                      Map<String, BigInteger> trackVotes,
                      Instant lastLogin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.trackVotes = trackVotes;
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", trackVotes=" + trackVotes + '\'' +
                ", lastLogin=" + lastLogin + '\'' +
                '}';
    }

    public static UserEntity create(String username, String password) {
        final String id = UUID.randomUUID().toString();
        final Map<String, BigInteger> votesMap = new HashMap<>();
        final Instant firstLogin = Instant.now().truncatedTo(ChronoUnit.SECONDS);

        return new UserEntity(id, username, password, votesMap, firstLogin);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, BigInteger> getTrackVotes() {
        return trackVotes;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEntity user = (UserEntity) o;
        return id.equals(user.id) && username.equals(user.username)
                && password.equals(user.password) && trackVotes.equals(user.trackVotes)
                && lastLogin.equals(user.lastLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, trackVotes, lastLogin);
    }

}
