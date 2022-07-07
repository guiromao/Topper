package co.topper.domain.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Document(collection = User.USER_COLLECTION)
@TypeAlias(User.USER_COLLECTION)
public class User {

    public static final String USER_COLLECTION = "user";

    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_TRACK_VOTES = "trackVotes";
    private static final String FIELD_LAST_LOGIN = "lastLogin";

    @Id
    private final String id;

    @Field(FIELD_USERNAME)
    private final String username;

    @Field(FIELD_PASSWORD)
    private final String password;

    @Field(FIELD_TRACK_VOTES)
    private final Map<String, BigInteger> trackVotes;

    @Field(FIELD_LAST_LOGIN)
    private final LocalDate lastLogin;

    public User(String id,
                String username,
                String password,
                Map<String, BigInteger> trackVotes,
                LocalDate lastLogin) {
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
                ", lastLogin=" + lastLogin +
                '}';
    }

    public static User create(String username, String password) {
        final String id = UUID.randomUUID().toString();
        final Map<String, BigInteger> votesMap = new HashMap<>();
        final LocalDate firstLogin = LocalDate.now();

        return new User(id, username, password, votesMap, firstLogin);
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

    public LocalDate getLastLogin() {
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
        User user = (User) o;
        return id.equals(user.id) && username.equals(user.username)
                && password.equals(user.password) && trackVotes.equals(user.trackVotes)
                && lastLogin.equals(user.lastLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, trackVotes, lastLogin);
    }

}
