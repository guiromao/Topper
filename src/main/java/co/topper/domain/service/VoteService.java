package co.topper.domain.service;

import co.topper.domain.data.dto.SuccessVoteDto;
import co.topper.domain.data.dto.VoteDto;

/**
 * Responsible for handling user votes on music Tracks
 */
public interface VoteService {

    SuccessVoteDto vote(VoteDto vote, String header);

}
