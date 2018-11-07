package util;

import javafx.util.Pair;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Authentication {

    private static Authentication instance;

    private Authentication() {

    }

    static {
        instance = new Authentication();
    }

    public static Authentication getInstance() {
        return instance;
    }

    public byte[] salt(String password)  {
        byte[] salt = new byte[8];
        new Random().nextBytes(salt);
        return salt;
    }

    public Pair<String, String> hash(String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(salt);
        byte[] digestedPassword = digest.digest(password.getBytes("UTF-8"));

        for (int i = 0; i < 10000; i++) {
            digest.reset();
            digestedPassword = digest.digest(digestedPassword);
        }
        return new Pair<String, String>(byteToBase64(digestedPassword), byteToBase64(salt));
    }

    public String byteToBase64(byte[] data) {
        BASE64Encoder endecoder = new BASE64Encoder();
        return endecoder.encode(data);
    }

    public byte[] base64ToByte(String data) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(data);
    }
}
