package utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Crypto {

    private static final SecureRandom random = new SecureRandom();
    private static final String charPool = "abcdefghijkmnopqrstuvwxyz0123456789";
    private static SecretKeyFactory factory = null;

    static {
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException ex) {
        }
    }

    public static String getRandomString(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += charPool.charAt(random.nextInt(charPool.length()));
        }
        return result;
    }

    public static byte[] getSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] computeHash(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException ex) {
            return null;
        }
    }
}
