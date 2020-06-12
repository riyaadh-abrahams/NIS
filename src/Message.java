
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
	public enum MessageType{
		UNENCRYPTED,
		ENCRYPTED,
		SEND_CERTIFICATE
	}
	// attributes
	public MessageType type;
	public String data;// the actual message
	public User userFrom; // user that send the message
	public X509Certificate publicCert;
	public PGP pgp;

	public Message(String data, User user) {
		this.data = data;
		userFrom = user;
		this.type = MessageType.UNENCRYPTED;
	}

	public Message(MessageType type ,String data, User user, X509Certificate x509Certificate) {
		this.data = data;
		userFrom = user;
		this.publicCert = x509Certificate;
		this.type = type;
	}

	public Message(MessageType type ,String data, User user, PGP pgp) {
		this.data = data;
		userFrom = user;
		this.pgp = pgp;
		this.type = type;
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