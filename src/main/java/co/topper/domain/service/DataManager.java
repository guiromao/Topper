package co.topper.domain.service;

import co.topper.domain.data.dto.VoteDto;

/**
 * Responsible for creating Music entities, if they don't already exist in Database
 */
public interface DataManager {

    void handleDataOf(VoteDto voteDto);

}
