package co.topper.domain.controller;

import co.topper.domain.data.dto.SuccessVoteDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.dto.VoteDto;
import co.topper.domain.service.TrackOfTheHourService;
import co.topper.domain.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static co.topper.configuration.constants.ControllerConstants.APP;
import static co.topper.configuration.constants.ControllerConstants.VERSION;
import static co.topper.configuration.constants.ControllerConstants.VOTE;

@RestController
@RequestMapping(APP + VERSION + VOTE)
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public SuccessVoteDto vote(@RequestHeader("Authorization") String header,
                               @RequestBody VoteDto voteDto) {
        return voteService.vote(voteDto, header);
    }

}
