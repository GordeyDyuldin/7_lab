package server.data_control;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Class for check and create password for hash
 */
public class PasswordManager {
    /**
     * Check password that user was entered and hash of password from database
     *
     * @param password     that need to check
     * @param salt         from database
     * @param hashPassword of user from database
     * @return true if hash of password equals with hashPassword else false
     */
    public static boolean checkPasswords(String password, String salt, String hashPassword) {
        return createHash(password + salt).equals(hashPassword);
    }

    /**
     * Create hash from passwordWithSalt
     *
     * @param passwordWithSalt that need to convert to hash
     * @return hash of this password with salt
     */
    public static String createHash(String passwordWithSalt) {
        try {
            byte[] bytes = MessageDigest.getInstance("sha-256").digest(passwordWithSalt.getBytes());
            return new BigInteger(bytes).toString();
        } catch (NoSuchAlgorithmException e) {
            return passwordWithSalt;
        }
    }

    /**
     * Create random salt
     *
     * @return salt
     */
    public static String generateSalt() {
        byte[] salt = new byte[6];
        new Random().nextBytes(salt);
        return new BigInteger(salt).toString();
    }
}
