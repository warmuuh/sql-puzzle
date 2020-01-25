package exercise.exception;

public class IllegalHeaderException extends SqlException {
    public IllegalHeaderException(String message) {
        super(message);
    }
}
