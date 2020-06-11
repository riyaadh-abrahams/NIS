import java.io.UnsupportedEncodingException;
import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Util {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException {

        SymmetricEncryption symmetricEncryption = new SymmetricEncryption();

        Key key = symmetricEncryption.generateKey();
        System.out.println(key);

        String testMessage = "Hello Test Message";
        byte[] cipherString = symmetricEncryption.encrypt(testMessage, key);

        String decryptedMessage = symmetricEncryption.decrypt(cipherString, key);
        

    }
}