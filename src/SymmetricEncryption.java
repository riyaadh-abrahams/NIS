
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import java.io.Serializable;
import java.security.*;

public class SymmetricEncryption implements Serializable {
    static IvParameterSpec ivSpec;
    static Cipher cipher;

    static {
        try {
            makeIvSpec();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            makeCipher();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SymmetricEncryption() throws NoSuchAlgorithmException, NoSuchPaddingException {
        Security.addProvider(new BouncyCastleProvider());
       
    }

    public static void makeCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    public static void makeIvSpec() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] random = new byte[16];
        secureRandom.nextBytes(random);
        ivSpec = new IvParameterSpec(random);
    }

    public static Key generateKey() throws NoSuchAlgorithmException
    {
        //System.out.println("Generating key...");
        KeyGenerator myGenerator = KeyGenerator.getInstance("AES");
        myGenerator.init(256);
        Key key =  myGenerator.generateKey();
        //Util.printByteArray("Key Generated:", key.getEncoded());
        return key;
    }
    public static byte[] encrypt(String message, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchPaddingException 
    {
       return encrypt(message.getBytes(), secretKey);
    }

    public static byte[] encrypt(byte[] message, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchPaddingException 
    {

        System.out.println("CIPHER");
        System.out.println(cipher);

        System.out.println("IVSPEC");
        System.out.println(ivSpec);

        //System.out.printf("Encrypting message '%s'...\n", message);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encryptedMessage = cipher.doFinal(message);
        //System.out.println("Encryption Complete!");
        //Util.printByteArray("Cipher Text:", encryptedMessage);
        return encryptedMessage;
    }

    public static String decrypt(byte[] encryptedMessage, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchPaddingException
    {

        //System.out.println("Decrypting message...");
        //Util.printByteArray("Cipher Text", encryptedMessage);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        String decryptedMessage = new String(cipher.doFinal(encryptedMessage));
        //System.out.printf("Decryption Complete! Output: '%s'\n", decryptedMessage);

        return decryptedMessage;
    }

    public static byte[] decryptByteArray(byte[] encryptedMessage, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchPaddingException
    {

        //System.out.println("Decrypting message...");
        //Util.printByteArray("Cipher Text", encryptedMessage);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
        //System.out.printf("Decryption Complete! Output: '%s'\n", decryptedMessage);
        return decryptedMessage;
    }
    

}