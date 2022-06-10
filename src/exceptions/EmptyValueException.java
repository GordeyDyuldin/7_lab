package exceptions;

/**
 * Throws when field is skipped (value is empty) for data classes
 */
public class EmptyValueException extends RuntimeException {
    public EmptyValueException(final String msg) {
        super(msg);
    }
}
