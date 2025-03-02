package service.exceptions;

import dataaccess.DataAccessException;

public class PlayerAlreadyTakenException extends DataAccessException {
    public PlayerAlreadyTakenException(String message) {
        super(message);
    }
}
