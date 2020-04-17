package team.nefrapp.security;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class PasswordManager {
    private static Logger log = Logger.getLogger("pm");

    /**
     * Hasha una stringa in chiaro
     * @param plain
     * @return un ArrayList di array di byte contenente la stringa hashata (indice 0) e il salt usato (indice 1)
     */
    public static ArrayList<byte[]> hashPassword(String plain) {
        ArrayList<byte[]> result = new ArrayList();
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec(plain.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hashedPassword = new byte[0];
        try {
            hashedPassword = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        result.add(hashedPassword);
        result.add(salt);
        return result;
    }

    /**
     * Hasha una stringa in chiaro, usando un salt value passato in input
     * @param plain stringa in chiaro
     * @param salt byte array con il salt value da usare
     * @return byte array con la stringa hashata
     */
    public static byte[] hashPassword(String plain, byte[] salt) {
        KeySpec spec = new PBEKeySpec(plain.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hashedPassword = new byte[0];
        try {
            hashedPassword = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return hashedPassword;
    }
}


