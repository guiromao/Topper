package co.topper.domain.data.converter;

import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FriendConverter {

    private final TrackConverter trackConverter;

    public FriendConverter(TrackConverter trackConverter) {
        this.trackConverter = trackConverter;
    }

    public FriendDto toDto(UserEntity user,
                           Map<String, Long> trackVotes,
                           List<TrackEntity> tracks,
                           List<AlbumEntity> albums,
                           List<ArtistEntity> artists) {
        return new FriendDto(
            user.getEmailId(),
            user.getUsername(),
            trackConverter.toTopDtoList(tracks,
                    albums,
                    artists,
                    trackVotes)
        );
    }

}
