package service.exceptions;

import dataaccess.DataAccessException;

public class WrongColorException extends DataAccessException {
    public WrongColorException(String message) {
        super(message);
    }
}
