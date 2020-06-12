import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.*;

public class AsymmetricEncryption implements Serializable {

    public static IvParameterSpec ivSpec;

    public AsymmetricEncryption() throws NoSuchAlgorithmException, NoSuchPaddingException {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] encrypt(String message, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException 
    {
        return encrypt(message.getBytes(), secretKey);
    }

    public static byte[] encrypt(byte[] message, Key secretKey) throws InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException 
    {
        Cipher cipher =  Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //System.out.printf("Encrypting message '%s'...\n", message);
        //System.out.printf("Key Type: %s\n", secretKey.getClass());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedMessage = cipher.doFinal(message);
        //System.out.println("Encryption Complete!");
        //Util.printByteArray("Cipher Text", encryptedMessage);
        return encryptedMessage;
    }

    public static String decrypt(byte[] encryptedMessage, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchPaddingException
    {
        Cipher cipher =  Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //System.out.println("Decrypting message...");
        //Util.printByteArray("Cipher Text", encryptedMessage);
        //System.out.printf("Key Type: %s\n", secretKey.getClass());
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        String decryptedMessage = new String(cipher.doFinal(encryptedMessage));
        //System.out.println("Decryption Complete!");
        //System.out.println("Plain Text: " + decryptedMessage);

        return decryptedMessage;
    }

    public static byte[] decryptByteArray(byte[] encryptedMessage, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchPaddingException
    {
        Cipher cipher =  Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //System.out.println("Decrypting Byte Array...");
        //Util.printByteArray("Cipher Text", encryptedMessage);
        //System.out.printf("Key Type: %s\n", secretKey.getClass());
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
        //System.out.println("Decryption Complete!");
        //Util.printByteArray("Plain Byte Array", decryptedMessage);

        return decryptedMessage;
    }
    
}