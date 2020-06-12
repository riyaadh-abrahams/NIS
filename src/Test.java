import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

public class Test {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, KeyStoreException,
        CertificateException, IOException, UnrecoverableKeyException, SignatureException {

            /*  SymmetricEncryption symmetricEncryption = new SymmetricEncryption();

             Key key = symmetricEncryption.generateKey();

             String testMessage = "Hello Test Message";
             byte[] cipherString = symmetricEncryption.encrypt(testMessage, key);

             String decryptedMessage = symmetricEncryption.decrypt(cipherString, key); */

            // Read the private keystore and get Private Key
            KeyStore keyStore = KeyStore.getInstance("JKS");
            // keystore password is required to access keystore 
            char[] pass = ("changeit").toCharArray();

            //Keystore File
            FileInputStream keyFile = new FileInputStream("./keys/keystore.jks");
            //load keystore
            keyStore.load(keyFile, pass);

            PrivateKey privateKey = (PrivateKey) keyStore.getKey("chatappkeys", pass);

            //Util.printByteArray("Private key", privateKey.getEncoded());
            PrivateKey clientPrivateKey = (PrivateKey) keyStore.getKey("clientkey", pass);

            //Util.printByteArray("Client Private key", clientPrivateKey.getEncoded());


            //Public key
            X509Certificate publicCertificate = (X509Certificate) keyStore.getCertificate("chatappkeys");
            PublicKey publicKey = publicCertificate.getPublicKey();

            //.printByteArray("Public key", publicKey.getEncoded());

            X509Certificate clientPublicCertificate = (X509Certificate) keyStore.getCertificate("clientkey");
            PublicKey clientPublicKey = clientPublicCertificate.getPublicKey();

            //Util.printByteArray("Client Public key", clientPublicCertificate.getEncoded());

          /*   AsymmetricEncryption asymmetricEncryption = new AsymmetricEncryption();
            byte[] cyphertext = asymmetricEncryption.encrypt("Hello World! Asymmetric Encryption", privateKey);
            
            String decryptedText = asymmetricEncryption.decrypt(cyphertext, publicKey); */

            String testMessage = generate(() -> "Hello").limit(100).collect(joining());
            PGP testpgp = new PGP(clientPublicKey, privateKey, "PGP Test Hello World " + testMessage);

            String finalMessage = testpgp.GetDecryptedMessage(clientPrivateKey, publicKey);
            System.out.println(finalMessage);

        }
}