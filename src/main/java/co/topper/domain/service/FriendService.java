package co.topper.domain.service;

import co.topper.domain.data.dto.FriendDto;

public interface FriendService {

    void sendRequest(String userId, String targetId);

    void acceptRequest(String userId, String targetId);

    FriendDto getFriend(String userId, String targetId);

}
