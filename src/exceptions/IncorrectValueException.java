package exceptions;

/**
 * Throws when value for data classes was incorrect
 */
public class IncorrectValueException extends RuntimeException {
    public IncorrectValueException(String msg) {
        super(msg);
    }
}
