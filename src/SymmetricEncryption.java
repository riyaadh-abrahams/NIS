
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import java.io.Serializable;
import java.security.*;

public class SymmetricEncryption implements Serializable
{

    public SymmetricEncryption() throws NoSuchAlgorithmException, NoSuchPaddingException {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    public static Cipher makeCipher() throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        return cipher;
    }

    public static IvParameterSpec getIvSpec() throws NoSuchAlgorithmException
    {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] random = new byte[16];
        secureRandom.nextBytes(random);
        return new IvParameterSpec(random);
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
        System.out.println("Cipher");
        Cipher cipher = makeCipher();
        System.out.println(cipher);

        System.out.println("IVSPEC");
        IvParameterSpec ivspec = getIvSpec();
        System.out.println(ivspec);

        System.out.printf("Encrypting message '%s'...\n", message);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        byte[] encryptedMessage = cipher.doFinal(message);
        //System.out.println("Encryption Complete!");
        //Util.printByteArray("Cipher Text:", encryptedMessage);
        return encryptedMessage;
    }

    public static String decrypt(byte[] encryptedMessage, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchPaddingException
    {
        Cipher cipher = makeCipher();
        //System.out.println("Decrypting message...");
        //Util.printByteArray("Cipher Text", encryptedMessage);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, getIvSpec());
        String decryptedMessage = new String(cipher.doFinal(encryptedMessage));
        //System.out.printf("Decryption Complete! Output: '%s'\n", decryptedMessage);

        return decryptedMessage;
    }

    public static byte[] decryptByteArray(byte[] encryptedMessage, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchPaddingException
    {
        Cipher cipher = makeCipher();
        //System.out.println("Decrypting message...");
        //Util.printByteArray("Cipher Text", encryptedMessage);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, getIvSpec());
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
        //System.out.printf("Decryption Complete! Output: '%s'\n", decryptedMessage);
        return decryptedMessage;
    }
    

}