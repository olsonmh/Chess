package service.exceptions;

public class AuthTokenNotFoundException extends RuntimeException {
    public AuthTokenNotFoundException(String message) {
        super(message);
    }
}
