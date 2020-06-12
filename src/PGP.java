import java.io.UnsupportedEncodingException;
import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class PGP {
    public byte[] encryptedSessionKey; // used to decrypt message, should be decrypted using recipient private key
    public byte[] signature; // for integrity
    public byte[] encryptedMessage; // compressed and encrypted using symmetric encryption

    SymmetricEncryption symmetricEncryption = new SymmetricEncryption();
    AsymmetricEncryption asymmetricEncryption = new AsymmetricEncryption();

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
        //System.out.println("Signing Message");
        Signature signatureAlgorithm = Signature.getInstance("SHA256WithRSA");
        signatureAlgorithm.initSign(senderPrivateKey);
        signatureAlgorithm.update(message.getBytes());

        byte[] signature = signatureAlgorithm.sign();

        this.signature = signature;
    }

    void CreateAndEncryptSessionKeyAndMessage(PublicKey recipientPublicKey, String message)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException
    {
        //System.out.println("Creating Encrypted Session Key");
        Key sessionKey = symmetricEncryption.generateKey();

        //System.out.println("Compressing Message...");
        byte[] compressedMessage = Util.zip(message);
        //System.out.println("Size Before: " + message.getBytes().length);
        //System.out.println("Size After: " + compressedMessage.length);
        

        byte[] encryptedMessage = symmetricEncryption.encrypt(compressedMessage, sessionKey);
        this.encryptedMessage = encryptedMessage;

        byte[] encryptedSessionKey = asymmetricEncryption.encrypt(sessionKey.getEncoded(), recipientPublicKey);
        this.encryptedSessionKey =  encryptedSessionKey;
    }


    public byte[] DecryptSessionKey(PrivateKey recipientPrivateKey) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        byte[] decryptedSessionKey = asymmetricEncryption.decryptByteArray(this.encryptedSessionKey, recipientPrivateKey);
        return decryptedSessionKey;
    }

    public String GetDecryptedMessage(PrivateKey recipientPrivateKey, PublicKey senderPublickey) throws InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, SignatureException
    {
        Key sessionKey = new SecretKeySpec(DecryptSessionKey(recipientPrivateKey), "AES");
        byte[] compressedMessage = symmetricEncryption.decryptByteArray(this.encryptedMessage, sessionKey);

        //verify signature
        //System.out.println("Verifying Signature...");
        Signature verificationAlgorithm = Signature.getInstance("SHA256WithRSA");
        verificationAlgorithm.initVerify(senderPublickey);
        verificationAlgorithm.update(compressedMessage);
        boolean matches = verificationAlgorithm.verify(signature);

        if(matches) {
            //System.out.println("Signature Verified");
        } else {
           // System.out.println("Signature Not Correct");
        }

        String finalMessage = Util.unzip(compressedMessage);
        return finalMessage;
    }

    
}