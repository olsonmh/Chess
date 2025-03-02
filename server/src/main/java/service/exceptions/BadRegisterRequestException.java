package service.exceptions;

import dataaccess.DataAccessException;

public class BadRegisterRequestException extends DataAccessException {
    public BadRegisterRequestException(String message) {
        super(message);
    }
}
