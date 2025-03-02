package service.exceptions;

import dataaccess.DataAccessException;

public class UserExistException extends DataAccessException {
    public UserExistException(String message) {
        super(message);
    }
}
