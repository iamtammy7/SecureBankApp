import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtil {

    private static final String CHARS =
        "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String hash(String password) {
        try {
            SecureRandom random = new SecureRandom();
            StringBuilder saltBuilder = new StringBuilder(16);
            for (int i = 0; i < 16; i++) {
                saltBuilder.append(
                    CHARS.charAt(random.nextInt(CHARS.length()))
                );
            }
            String salt = saltBuilder.toString();

            String hash = sha256(salt + password);
            return salt + ":" + hash;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public static boolean verify(String password, String stored) {
        if (stored == null || !stored.contains(":")) return false;

        try {
            String[] parts = stored.split(":", 2);
            String salt = parts[0];
            String expectedHash = parts[1];

            String actualHash = sha256(salt + password);
            return expectedHash.equals(actualHash);

        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    private static String sha256(String input)
            throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(input.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}