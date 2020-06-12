
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import java.security.*;

public class SymmetricEncryption
{
    public Cipher cipher;
    public IvParameterSpec ivSpec;

    public SymmetricEncryption() throws NoSuchAlgorithmException, NoSuchPaddingException {
        Security.addProvider(new BouncyCastleProvider());

        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] random = new byte[16];
        secureRandom.nextBytes(random);
        ivSpec = new IvParameterSpec(random);
    }

    public Key generateKey() throws NoSuchAlgorithmException
    {
        //System.out.println("Generating key...");
        KeyGenerator myGenerator = KeyGenerator.getInstance("AES");
        myGenerator.init(256);
        Key key =  myGenerator.generateKey();
        //Util.printByteArray("Key Generated:", key.getEncoded());
        return key;
    }
    public byte[] encrypt(String message, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException 
    {
       return encrypt(message.getBytes(), secretKey);
    }

    public byte[] encrypt(byte[] message, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException 
    {
        //System.out.printf("Encrypting message '%s'...\n", message);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encryptedMessage = cipher.doFinal(message);
        //System.out.println("Encryption Complete!");
        //Util.printByteArray("Cipher Text:", encryptedMessage);
        return encryptedMessage;
    }

    public String decrypt(byte[] encryptedMessage, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
    {
        //System.out.println("Decrypting message...");
        //Util.printByteArray("Cipher Text", encryptedMessage);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        String decryptedMessage = new String(cipher.doFinal(encryptedMessage));
        //System.out.printf("Decryption Complete! Output: '%s'\n", decryptedMessage);

        return decryptedMessage;
    }

    public byte[] decryptByteArray(byte[] encryptedMessage, Key secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
    {
        //System.out.println("Decrypting message...");
        //Util.printByteArray("Cipher Text", encryptedMessage);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
        //System.out.printf("Decryption Complete! Output: '%s'\n", decryptedMessage);
        return decryptedMessage;
    }
    

}