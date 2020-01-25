package exercise.exception;

import exercise.exception.SqlException;

public class IllegalHeaderException extends SqlException {
    public IllegalHeaderException(String message) {
        super(message);
    }
}
