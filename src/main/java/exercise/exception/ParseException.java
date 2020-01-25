package exercise.exception;

public class ParseException extends SqlException {
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
