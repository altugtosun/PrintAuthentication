package util;

import javafx.util.Pair;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Authentication {

    private static Authentication instance;
    private static SecureRandom random;

    private Authentication() {

    }

    static {
        instance = new Authentication();
        random = new SecureRandom();
    }

    public static Authentication getInstance() {
        return instance;
    }

    public byte[] salt()  {
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }

    //Assumed that users are in the database. So this function is just for test purposes.
    public Pair<String, String> hashWithRandomSalt(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] salt = salt();
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(salt);
        byte[] digestedPassword = digest.digest(password.getBytes("UTF-8"));

        for (int i = 0; i < 1000; i++) {
            digest.reset();
            digestedPassword = digest.digest(digestedPassword);
        }
        return new Pair<String, String>(byteToBase64(digestedPassword), byteToBase64(salt));
    }

    public String hashWithGivenSalt(String password, String salt) throws NoSuchAlgorithmException, IOException {
        byte[] byteSalt = base64ToByte(salt);
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(byteSalt);
        byte[] digestedPassword = digest.digest(password.getBytes("UTF-8"));

        for (int i = 0; i < 1000; i++) {
            digest.reset();
            digestedPassword = digest.digest(digestedPassword);
        }
        return byteToBase64(digestedPassword);
    }

    public String byteToBase64(byte[] data) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public byte[] base64ToByte(String data) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(data);
    }
}
