package co.topper.domain.service;

import co.topper.domain.data.dto.FriendDto;

public interface FriendService {

    void sendRequest(String userId, String friendId);

    void acceptRequest(String userId, String friendId);

    void refuseRequest(String userId, String senderId);

    FriendDto getFriend(String userId, String friendId);

    void unfriend(String userId, String friendId);

}
