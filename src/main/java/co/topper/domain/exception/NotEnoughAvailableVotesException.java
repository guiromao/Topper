package co.topper.domain.exception;

public class NotEnoughAvailableVotesException extends RuntimeException {

    public NotEnoughAvailableVotesException(Long submittedVotes, Long availableVotes) {
        super(String.format("Available votes for user are %s. Trying to submit a total of %s",
                availableVotes, submittedVotes));
    }

}
