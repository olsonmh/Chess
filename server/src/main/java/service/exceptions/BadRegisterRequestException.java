package service.exceptions;

public class BadRegisterRequestException extends RuntimeException {
    public BadRegisterRequestException(String message) {
        super(message);
    }
}
