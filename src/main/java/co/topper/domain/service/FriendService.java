package co.topper.domain.service;

import co.topper.domain.data.dto.FriendDto;

public interface FriendService {

    void sendRequest(String authHeader, String friendId);

    void acceptRequest(String authHeader, String friendId);

    void refuseRequest(String authHeader, String senderId);

    FriendDto getFriend(String authHeader, String friendId);

    void unfriend(String userId, String friendId);

}
