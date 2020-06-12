//Client side of Chat App
//Properly deal with errors and stop threads

import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;

public class ChatAppClient implements Runnable {
	private PrivateKey clientPrivateKey;
	private X509Certificate clientPublicCertificate;
	private PublicKey clientPublicKey;
	private int portNumber;
	private User activeUser;

	private ObjectOutputStream outputObject;
	private BufferedReader consoleIn;

	private ClientThread clientThread;
	private Thread thread = null;
	private Socket socket;

	PublicKey serverPublicKey;

	public ChatAppClient(String hostname, int portNumber, String user_name)
			throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException,
			UnrecoverableKeyException {
		// System.out.println("Starting ChatAppClient()");
		Security.addProvider(new BouncyCastleProvider());

		// Read the private keystore and get Private Key
		KeyStore keyStore = KeyStore.getInstance("JKS");
		// keystore password is required to access keystore 
		char[] pass = ("changeit").toCharArray();

		//Keystore File
		FileInputStream keyFile = new FileInputStream("./keys/keystore.jks");
		//load keystore
		keyStore.load(keyFile, pass);

		Util.printlnc("--------------------------------------------", Util.Color.YELLOW_BOLD);
		Util.printlnc("Loading Client Private and Public Keys", Util.Color.YELLOW_BOLD);
		Util.printlnc("--------------------------------------------", Util.Color.YELLOW_BOLD);
		clientPrivateKey = (PrivateKey) keyStore.getKey("clientkey", pass);
	   
		
		clientPublicCertificate = (X509Certificate) keyStore.getCertificate("clientkey");
		clientPublicKey = clientPublicCertificate.getPublicKey();

		X509Certificate serverPublicCertificate = (X509Certificate) keyStore.getCertificate("chatappkeys");
		serverPublicKey = serverPublicCertificate.getPublicKey();

		Util.printByteArray("Private key", clientPrivateKey.getEncoded());
		System.out.println();
		Util.printByteArray("Public key", clientPublicKey.getEncoded());

		this.portNumber = portNumber;
		activeUser = new User(user_name, hostname);

		System.out.println("Logged in as: " + activeUser.getUsername());
		try {
			socket = new Socket(hostname, portNumber);// connect to server
			start();// open IO
		} catch (UnknownHostException u) {
			System.out.println("Error in ChatAppClient con()");
		} catch (IOException ioe) {
			System.out.println("ERR");
		}

	}// end con

	// initialize IO for user. Start threads.
	public void start() throws IOException {
		outputObject = new ObjectOutputStream(socket.getOutputStream());// send a Message obj to server
		consoleIn = new BufferedReader(new InputStreamReader(System.in));// to read from console

		if (thread == null) {
			clientThread = new ClientThread(socket, this);
			thread = new Thread(this);
			thread.start();
		}
	}

	// do the follwoing while ChatAppClient is running
	public void run() {
		// System.out.println("ChatAppClient thread running");
		while (thread != null) {
			try {
				Util.printc(">>> ", Util.Color.GREEN_BOLD);
				String userInput = consoleIn.readLine();// this is the message that will eventually be sent to another
				
				System.out.println("client public key");
				System.out.println(clientPublicKey.hashCode());
				System.out.println("server public key");
				System.out.println(serverPublicKey.hashCode());
				
				PGP myPgp = new PGP(serverPublicKey, clientPrivateKey, userInput);
				Message m = new Message(userInput, activeUser, myPgp);

				outputObject.writeObject(m);
				// System.out.println("Message Sent\n");
			} // end try
			catch (IOException ie) {
				ie.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}// end run

	public void recieve(Message m) {
		System.out.println(m.toString());

		try {
			if (m.getData().split(",")[0].equals("#Welcome")) {
				Util.printlnc("--------------------------------------------", Util.Color.YELLOW_BOLD);
				Util.printlnc("Recieved Public Certificate! Verifying...", Util.Color.YELLOW_BOLD);
				Util.printlnc("--------------------------------------------", Util.Color.YELLOW_BOLD);

				System.out.println(m.publicCert.toString());

				Util.printlnc("--------------------------------------------", Util.Color.YELLOW_BOLD);
				Util.printlnc("Successfully Verified! Extracting Public Key", Util.Color.YELLOW_BOLD);
				Util.printlnc("--------------------------------------------", Util.Color.YELLOW_BOLD);
				PublicKey serverPublicKey = m.publicCert.getPublicKey();
				//publicKey = serverPublicKey;
				Util.printByteArray("Public key", serverPublicKey.getEncoded());

				Util.printlnc("--------------------------------------------", Util.Color.GREEN_BOLD);
				Util.printlnc("Secure Connection Established!\nEnter your message ", Util.Color.GREEN_BOLD);
				Util.printlnc("--------------------------------------------", Util.Color.GREEN_BOLD);
				Util.printc("\r>>>", Util.Color.GREEN_BOLD);
			}
		} catch (Exception e) {
			System.out.println("Error");
		}

	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, CertificateException,
			KeyStoreException, UnrecoverableKeyException {
		
		String hostname = "localhost";
		int port = 6000;


		/* //Start up checks
		if(args.length == 0)//use hostname command to get hostname
		{
			hostname = getHostName(port);
		}
		else if(args.length == 1)
		{
			hostname = args[0];
		}
		else
		{
			hostname = args[0];
			port = Integer.parseInt(args[1]);
		} */
		//get user info
		System.out.println("Enter a username:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String user_in = br.readLine();
		
		ChatAppClient app = new ChatAppClient(hostname, port, user_in);
	}//end main

	//Returns hostname
	public static String getHostName(int portNumber)
	{
		String hostname = null;
		System.out.println("Fetching hostname and using Port Number " + portNumber);
		try{

			Process p = Runtime.getRuntime().exec("hostname");//run the hostname command
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;

			while((line = input.readLine()) != null)
			{
				hostname = line;
				//System.out.println("DEBUG: " + hostname);
				
			}//end while
			//System.out.println("DEBUG2: " + hostname);
			return hostname;
		}//end try
		catch (IOException e)
		{
			System.out.println(e);
			return e.toString();
		}
	}//end getHostName

}//end class