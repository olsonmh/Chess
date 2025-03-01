package service.exceptions;

public class PlayerAlreadyTakenException extends RuntimeException {
    public PlayerAlreadyTakenException(String message) {
        super(message);
    }
}
