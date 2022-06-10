package exceptions;

/**
 * Throws when in xml file is so many arguments
 */
public class SoManyArgumentsException extends Exception {
    public SoManyArgumentsException(final String msg) {
        super(msg);
    }
}
