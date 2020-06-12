
//Contains details of message
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.X509Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Message implements Serializable {
	// attributes
	private String data;// the actual message
	private User userFrom; // user that send the message
	public X509Certificate publicCert;
	public PGP pgp;

	public Message(String data, User user) {
		this.data = data;
		userFrom = user;
	}

	public Message(String data, User user, X509Certificate x509Certificate) {
		this.data = data;
		userFrom = user;
		this.publicCert = x509Certificate;
	}

	public Message(String data, User user, PGP pgp) {
		this.data = data;
		userFrom = user;
		this.pgp = pgp;
	}

	public String getData() {
		return data;
	}

	public User getUser() {
		return userFrom;
	}

	public String toString() {
		String s = userFrom.getUsername() + " says: " + data;
		return s;
	}

	public String DecryptAndGet(PrivateKey recipientPrivateKey, PublicKey senderPublicKey)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException, SignatureException, NoSuchPaddingException
	{
		return pgp.GetDecryptedMessage(recipientPrivateKey, senderPublicKey);
	}
}