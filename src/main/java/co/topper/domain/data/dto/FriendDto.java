package co.topper.domain.data.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class FriendDto {

    private static final String PROPERTY_FRIEND_ID = "friendId";
    private static final String PROPERTY_USERNAME = "username";
    private static final String PROPERTY_LIKED_TRACKS = "likedTracks";

    private final String friendId;
    private final String username;
    private final List<TopDto> likedTracks;

    @JsonCreator
    public FriendDto(@JsonProperty(PROPERTY_FRIEND_ID) String friendId,
                     @JsonProperty(PROPERTY_USERNAME) String username,
                     @JsonProperty(PROPERTY_LIKED_TRACKS) List<TopDto> likedTracks) {
        this.friendId = friendId;
        this.username = username;
        this.likedTracks = likedTracks;
    }

    @Override
    public String toString() {
        return "FriendDto{" +
                "friendId='" + friendId + '\'' +
                ", username='" + username + '\'' +
                ", likedTracks=" + likedTracks +
                ", likedTracks=" + likedTracks +
                '}';
    }

    public String getFriendId() {
        return friendId;
    }

    public String getUsername() {
        return username;
    }

    public List<TopDto> getLikedTracks() {
        return likedTracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FriendDto friendDto = (FriendDto) o;
        return friendId.equals(friendDto.friendId) && username.equals(friendDto.username) &&
                likedTracks.equals(friendDto.likedTracks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, username, likedTracks);
    }
}
