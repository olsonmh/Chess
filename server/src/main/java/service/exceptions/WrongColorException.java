package service.exceptions;

public class WrongColorException extends RuntimeException {
    public WrongColorException(String message) {
        super(message);
    }
}
