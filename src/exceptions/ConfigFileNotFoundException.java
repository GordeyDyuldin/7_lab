package exceptions;

/**
 * Uses if some configurations file don't exist
 */
public class ConfigFileNotFoundException extends Exception {
    public ConfigFileNotFoundException(String msg) {
        super(msg);
    }
}
