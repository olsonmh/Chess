package service.exceptions;

import dataaccess.DataAccessException;

public class WrongPasswordException extends DataAccessException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
