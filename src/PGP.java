import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class PGP implements Serializable {
    public byte[] encryptedSessionKey; // used to decrypt message, should be decrypted using recipient private key
    public byte[] signature; // for integrity
    public byte[] encryptedMessage; // compressed and encrypted using symmetric encryption

    public PGP(PublicKey recipientPublicKey, PrivateKey senderPrivateKey, String message)
            throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            UnsupportedEncodingException {

            CreateAndEncryptSessionKeyAndMessage(recipientPublicKey, message);
            SignMessage(senderPrivateKey, message);

            //Util.printByteArray("Encrypted Session Key: ", encryptedSessionKey);
            //Util.printByteArray("Signature: ", signature);
            //Util.printByteArray("Encrypted Message", encryptedMessage);

    }

    void SignMessage(PrivateKey senderPrivateKey, String message)
            throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
        System.out.println("Signing Message");
        Signature signatureAlgorithm = Signature.getInstance("SHA256WithRSA");
        signatureAlgorithm.initSign(senderPrivateKey);
        signatureAlgorithm.update(message.getBytes());

        byte[] signature = signatureAlgorithm.sign();

        Util.printByteArray("Signature: ", signature);

        this.signature = signature;
    }

    void CreateAndEncryptSessionKeyAndMessage(PublicKey recipientPublicKey, String message)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException
    {
        System.out.println("Creating Session Key...");
        Key sessionKey = SymmetricEncryption.generateKey();
        Util.printByteArray("Session key created: ", sessionKey.getEncoded());
        
        System.out.println("Encrypting session Key with recipient public key...");
        byte[] encryptedSessionKey = AsymmetricEncryption.encrypt(sessionKey.getEncoded(), recipientPublicKey);
        this.encryptedSessionKey =  encryptedSessionKey;
        Util.printByteArray("Cipher Session key: ", encryptedSessionKey);

        System.out.println("Compressing Message...");
        byte[] compressedMessage = Util.zip(message);
        Util.printByteArray("Compressed message: ", compressedMessage);
        System.out.println("Size Before: " + message.getBytes().length);
        System.out.println("Size After: " + compressedMessage.length);
        
        System.out.println("Encrypting Message with Session Key...");
        System.out.println("Plain Text: " + message);
        byte[] encryptedMessage = SymmetricEncryption.encrypt(compressedMessage, sessionKey);
        this.encryptedMessage = encryptedMessage;
        Util.printByteArray("Cipher Text: ", encryptedMessage);
    }


    public byte[] DecryptSessionKey(PrivateKey recipientPrivateKey) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchPaddingException {

        byte[] decryptedSessionKey = AsymmetricEncryption.decryptByteArray(this.encryptedSessionKey, recipientPrivateKey);
        return decryptedSessionKey;
    }

    public String GetDecryptedMessage(PrivateKey recipientPrivateKey, PublicKey senderPublickey) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, SignatureException, NoSuchPaddingException
    {   
        Util.printByteArray("Cipher Session key: ", encryptedSessionKey);
        System.out.println("Decrypting session key...");
        Key sessionKey = new SecretKeySpec(DecryptSessionKey(recipientPrivateKey), "AES");
        Util.printByteArray("Decrypted. Plain text session key: ", sessionKey.getEncoded());

        Util.printByteArray("Cipher Message: ", encryptedMessage);
        System.out.println("Decrypting message...");
        byte[] compressedMessage = SymmetricEncryption.decryptByteArray(this.encryptedMessage, sessionKey);
        Util.printByteArray("Decrypted. Compressed message: ", compressedMessage);

        System.out.println("Decompressing message...");
        String finalMessage = Util.unzip(compressedMessage);
        System.out.println("Message Decompressed. Final Output: " +finalMessage);
        
        //verify signature
        System.out.println("Verifying Signature...");
        Signature verificationAlgorithm = Signature.getInstance("SHA256WithRSA");
        verificationAlgorithm.initVerify(senderPublickey);
        verificationAlgorithm.update(finalMessage.getBytes());
        boolean matches = verificationAlgorithm.verify(signature);

        if(matches) {
            System.out.println("Signature Verified");
        } else {
            System.out.println("Signature Not Correct");
        }
        return finalMessage;

    }

    
}