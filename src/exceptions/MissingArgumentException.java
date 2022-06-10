package exceptions;

/**
 * Throws when require argument in command was missing
 */
public class MissingArgumentException extends Exception {
    public MissingArgumentException(final String msg) {
        super(msg);
    }
}
