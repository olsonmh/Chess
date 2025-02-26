package service.exceptions;

public class AuthTokenNotFound extends RuntimeException {
    public AuthTokenNotFound(String message) {
        super(message);
    }
}
