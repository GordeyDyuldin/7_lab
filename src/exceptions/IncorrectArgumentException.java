package exceptions;

/**
 * Throws when argument for command was incorrect
 */
public class IncorrectArgumentException extends Exception {
    public IncorrectArgumentException(final String msg) {
        super(msg);
    }
}
