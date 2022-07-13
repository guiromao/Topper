package co.topper.domain.controller;

import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/{userId}")
    public FriendDto getFriend(@PathVariable("userId") String userId,
                               @RequestParam("friendId") String friendId) {
        return friendService.getFriend(userId, friendId);
    }

    @PostMapping("/request/{userId}")
    public void sendRequest(@PathVariable("userId") String userId,
                            @RequestParam("friendId") String friendId) {
        friendService.sendRequest(userId, friendId);
    }

    @PutMapping("/request/{userId}")
    public void acceptRequest(@PathVariable("userId") String userId,
                              @RequestParam("friendId") String friendId) {
        friendService.acceptRequest(userId, friendId);
    }

    @DeleteMapping("request/{userId}")
    public void refuseRequest(@PathVariable("userId") String userId,
                              @RequestParam("friendId") String friendId) {
        friendService.refuseRequest(userId, friendId);
    }

    @DeleteMapping("/{userId}")
    public void deleteFriend(@PathVariable("userId") String userId,
                             @RequestParam("friendId") String friendId) {
        friendService.unfriend(userId, friendId);
    }

}
