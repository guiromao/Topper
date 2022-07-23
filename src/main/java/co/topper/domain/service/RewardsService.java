package co.topper.domain.service;

/**
 * Service to increment user Votes, upon winning rewards, eg, for having watched a rewarded video
 */
public interface RewardsService {

    void incrementVotesOfUser(String authHeader, Long rewardedVotes);

}
