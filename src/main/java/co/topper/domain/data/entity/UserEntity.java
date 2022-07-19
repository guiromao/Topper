package co.topper.domain.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static co.topper.configuration.constants.UserConstants.FIELD_AVAILABLE_VOTES;
import static co.topper.configuration.constants.UserConstants.FIELD_EMAIL;
import static co.topper.configuration.constants.UserConstants.FIELD_FRIENDS_LIST_IDS;
import static co.topper.configuration.constants.UserConstants.FIELD_LAST_VOTE_ATTEMPT;
import static co.topper.configuration.constants.UserConstants.FIELD_PASSWORD;
import static co.topper.configuration.constants.UserConstants.FIELD_REQUESTS_RECEIVED;
import static co.topper.configuration.constants.UserConstants.FIELD_ROLES;
import static co.topper.configuration.constants.UserConstants.FIELD_TRACK_VOTES;
import static co.topper.configuration.constants.UserConstants.FIELD_USERNAME;

@Document(collection = UserEntity.USER_COLLECTION)
@TypeAlias(UserEntity.USER_COLLECTION)
@CompoundIndex(name = "votes_idx", def = "{'trackVotes' : 1}")
public class UserEntity implements Serializable {

    private static final Long serialId = 1L;

    public static final String USER_COLLECTION = "user";

    @Id
    private final String id;

    @Field(FIELD_USERNAME)
    private final String username;

    @Field(FIELD_PASSWORD)
    private final String password;

    @Field(FIELD_EMAIL)
    private final String email;

    @Field(FIELD_FRIENDS_LIST_IDS)
    private final Set<String> friendsListIds;

    @Field(FIELD_REQUESTS_RECEIVED)
    private final Set<String> requestsReceivedIds;

    @Field(FIELD_TRACK_VOTES)
    private final Map<String, Long> trackVotes;

    @Field(FIELD_AVAILABLE_VOTES)
    private final Long availableVotes;

    @Field(FIELD_LAST_VOTE_ATTEMPT)
    private final Instant lastVoteAttempt;

    @Field(FIELD_ROLES)
    private final Set<Role> roles;

    public UserEntity(String id,
                      String username,
                      String password,
                      String email,
                      Set<String> friendsListIds,
                      Set<String> requestsReceivedIds,
                      Map<String, Long> trackVotes,
                      Long availableVotes,
                      Instant lastVoteAttempt,
                      Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.friendsListIds = friendsListIds;
        this.requestsReceivedIds = requestsReceivedIds;
        this.trackVotes = trackVotes;
        this.availableVotes = availableVotes;
        this.lastVoteAttempt = lastVoteAttempt.truncatedTo(ChronoUnit.DAYS);
        this.roles = roles;
    }

    public UserEntity withPassword(String updatedPassword) {
        return new UserEntity(this.id, this.username, updatedPassword,
                this.email, this.friendsListIds, this.requestsReceivedIds, this.trackVotes,
                this.availableVotes, this.lastVoteAttempt, this.roles);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", friendsListIds=" + friendsListIds +
                ", requestsReceivedIds=" + requestsReceivedIds +
                ", trackVotes=" + trackVotes +
                ", availableVotes=" + availableVotes +
                ", lastVoteAttempt=" + lastVoteAttempt +
                ", roles=" + roles +
                '}';
    }

    public static UserEntity create(String username, String password, String email) {
        final String id = UUID.randomUUID().toString();
        final Set<String> friendsListIds = new HashSet<>();
        final Set<String> requestsReceivedIds = new HashSet<>();
        final Map<String, Long> votesMap = new HashMap<>();
        final Long availableVotes = 1000L;
        final Instant firstLogin = Instant.now();
        final Set<Role> roles = Set.of(Role.USER);

        return new UserEntity(id, username, password, email,
                friendsListIds, requestsReceivedIds, votesMap,
                availableVotes, firstLogin, roles);
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

    public Set<String> getFriendsListIds() {
        return friendsListIds;
    }

    public Set<String> getRequestsReceivedIds() {
        return requestsReceivedIds;
    }

    public Map<String, Long> getTrackVotes() {
        return trackVotes;
    }

    public Long getAvailableVotes() {
        return availableVotes;
    }

    public Instant getLastVoteAttempt() {
        return lastVoteAttempt;
    }

    public Set<Role> getRoles() {
        return roles;
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
                friendsListIds.equals(user.friendsListIds) &&
                requestsReceivedIds.equals(user.requestsReceivedIds) &&
                trackVotes.equals(user.trackVotes) &&
                availableVotes.equals(user.getAvailableVotes()) &&
                lastVoteAttempt.equals(user.lastVoteAttempt) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email,
                friendsListIds, requestsReceivedIds, trackVotes,
                availableVotes, lastVoteAttempt, roles);
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

        public UpdateBuilder setTrackVotes(Map<String, Long> votes) {
            set(FIELD_TRACK_VOTES, votes);
            return this;
        }

        public UpdateBuilder incrementTrackVotes(String trackId, Long votes) {
            update.inc(FIELD_TRACK_VOTES + "." + trackId, votes);
            return this;
        }

        public UpdateBuilder setAvailableVotes(Long votes) {
            set(FIELD_AVAILABLE_VOTES, votes);
            return this;
        }

        public UpdateBuilder decrementAvailableVotes(Long votes) {
            update.inc(FIELD_AVAILABLE_VOTES, -votes);
            return this;
        }

        public UpdateBuilder setLastVoteAttempt(Instant lastVoteAttempt) {
            set(FIELD_LAST_VOTE_ATTEMPT, lastVoteAttempt.truncatedTo(ChronoUnit.DAYS));
            return this;
        }

        public UpdateBuilder setFriendsListIds(Set<String> listIds) {
            set(FIELD_FRIENDS_LIST_IDS, listIds);
            return this;
        }

        public UpdateBuilder setRequestsReceivedIds(Set<String> requests) {
            set(FIELD_REQUESTS_RECEIVED, requests);
            return this;
        }

        public UpdateBuilder setRoles(Set<Role> roles) {
            set(FIELD_ROLES, roles);
            return this;
        }

        private void set(String field, Object value) {
            update.set(field, value);
        }

        public Optional<Update> build() {
            if (update.equals(new Update())) {
                return Optional.empty();
            }

            return Optional.of(update);
        }

    }

}
