package co.topper.domain.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Update;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static co.topper.configuration.constants.UserConstants.FIELD_EMAIL;
import static co.topper.configuration.constants.UserConstants.FIELD_LAST_LOGIN;
import static co.topper.configuration.constants.UserConstants.FIELD_PASSWORD;
import static co.topper.configuration.constants.UserConstants.FIELD_TRACK_VOTES;
import static co.topper.configuration.constants.UserConstants.FIELD_USERNAME;

@Document(collection = UserEntity.USER_COLLECTION)
@TypeAlias(UserEntity.USER_COLLECTION)
@CompoundIndex(name = "votes_idx", def = "{'trackVotes' : 1}")
public class UserEntity {

    public static final String USER_COLLECTION = "user";

    @Id
    private final String id;

    @Field(FIELD_USERNAME)
    private final String username;

    @Field(FIELD_PASSWORD)
    private final String password;

    @Field(FIELD_EMAIL)
    private final String email;

    @Field(FIELD_TRACK_VOTES)
    private final Map<String, BigInteger> trackVotes;

    @Field(FIELD_LAST_LOGIN)
    private final Instant lastLogin;

    public UserEntity(String id,
                      String username,
                      String password,
                      String email,
                      Map<String, BigInteger> trackVotes,
                      Instant lastLogin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.trackVotes = trackVotes;
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", trackVotes=" + trackVotes +
                ", lastLogin=" + lastLogin +
                '}';
    }

    public static UserEntity create(String username, String password, String email) {
        final String id = UUID.randomUUID().toString();
        final Map<String, BigInteger> votesMap = new HashMap<>();
        final Instant firstLogin = Instant.now().truncatedTo(ChronoUnit.SECONDS);

        return new UserEntity(id, username, password, email, votesMap, firstLogin);
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

    public String getEmail() {
        return email;
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
                && password.equals(user.password) &&
                email.equals(user.email) &&
                trackVotes.equals(user.trackVotes) &&
                lastLogin.equals(user.lastLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, trackVotes, lastLogin);
    }

    public static class UpdateBuilder {

        private final Update update;

        private UpdateBuilder() {
            update = new Update();
        }

        public static UpdateBuilder create() {
            return new UpdateBuilder();
        }

        public UpdateBuilder setUsername(String username) {
            set(FIELD_USERNAME, username);
            return this;
        }

        public UpdateBuilder setPassword(String password) {
            set(FIELD_PASSWORD, password);
            return this;
        }

        public UpdateBuilder setEmail(String email) {
            set(FIELD_EMAIL, email);
            return this;
        }

        public UpdateBuilder setTrackVotes(Map<String, BigInteger> votes) {
            set(FIELD_TRACK_VOTES, votes);
            return this;
        }

        public UpdateBuilder setLastLogin(Instant lastLogin) {
            set(FIELD_LAST_LOGIN, lastLogin);
            return this;
        }

        private void set(String field, Object value) {
            update.set(field, value);
        }

        public Optional<Update> build() {
            if (!update.equals(new Update())) {
                Optional.of(update);
            }

            return Optional.empty();
        }

    }

}
