package co.topper.domain.controller;

import co.topper.domain.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static co.topper.configuration.constants.ControllerConstants.*;

@RestController
@RequestMapping(APP + VERSION + "/rewards")
public class RewardsController {

    private final RewardsService rewardsService;

    @Autowired
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @PostMapping
    public void giveRewards(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                            @RequestParam("rewardedVotes") Long rewardVotes) {
        rewardsService.incrementVotesOfUser(authHeader, rewardVotes);
    }

}
