package co.topper.domain.service;

import co.topper.domain.data.dto.SuccessVoteDto;
import co.topper.domain.data.dto.VoteDto;

public interface VoteService {

    SuccessVoteDto vote(VoteDto vote, String userId);

}
