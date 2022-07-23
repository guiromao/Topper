package co.topper.domain.controller;

import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static co.topper.configuration.constants.ControllerConstants.*;

@RestController
@RequestMapping(APP + VERSION + "/friends")
public class FriendsController {

    private final FriendService friendService;

    @Autowired
    public FriendsController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping
    public FriendDto getFriend(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                               @RequestParam("friendId") String friendId) {
        return friendService.getFriend(authHeader, friendId);
    }

    @PostMapping("/request")
    public void sendRequest(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                            @RequestParam("friendId") String friendId) {
        friendService.sendRequest(authHeader, friendId);
    }

    @PutMapping("/request")
    public void acceptRequest(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                              @RequestParam("friendId") String friendId) {
        friendService.acceptRequest(authHeader, friendId);
    }

    @DeleteMapping("/request")
    public void refuseRequest(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                              @RequestParam("friendId") String friendId) {
        friendService.refuseRequest(authHeader, friendId);
    }

    @DeleteMapping
    public void deleteFriend(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                             @RequestParam("friendId") String friendId) {
        friendService.unfriend(authHeader, friendId);
    }

}
