package exceptions;

/**
 * Uses if something wrong with connection
 */
public class ConnectionException extends Exception {
    public ConnectionException(String msg) {
        super(msg);
    }
}
