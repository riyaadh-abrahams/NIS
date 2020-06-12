//A class that handles the server side of the chat app
//Currently only acts as an echo server

import java.util.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.io.*;

public class ChatAppServer implements Runnable {
	public int port_number;
	public ArrayList<ServerThread> activeClients;// list of current active clients
	ServerSocket server;
	Thread serverThread = null;

	PrivateKey serverPrivateKey;
	public X509Certificate serverPublicCertificate;
	public PublicKey serverPublicKey;
	public  X509Certificate clientPublicCertificate;
	public  PublicKey clientPublicKey;

	public User serverUser;

	public ChatAppServer(int port_num) throws IOException, NoSuchAlgorithmException, CertificateException,
			UnrecoverableKeyException, KeyStoreException {

		 // Read the private keystore and get Private Key
		 KeyStore keyStore = KeyStore.getInstance("JKS");
		 // keystore password is required to access keystore 
		 char[] pass = ("changeit").toCharArray();
 
		 //Keystore File
		 FileInputStream keyFile = new FileInputStream("./keys/keystore.jks");
		 //load keystore
		 keyStore.load(keyFile, pass);
 
		 Util.printlnc("--------------------------------------------", Util.Color.YELLOW_BOLD);
		 Util.printlnc("Loading Server Private and Public Keys", Util.Color.YELLOW_BOLD);
		 Util.printlnc("--------------------------------------------", Util.Color.YELLOW_BOLD);
		 serverPrivateKey = (PrivateKey) keyStore.getKey("chatappkeys", pass);
		 
 
		 //Public key
		 serverPublicCertificate = (X509Certificate) keyStore.getCertificate("chatappkeys");
		 serverPublicKey = serverPublicCertificate.getPublicKey();
		 
		 clientPublicCertificate = (X509Certificate) keyStore.getCertificate("clientkey");
		 clientPublicKey = clientPublicCertificate.getPublicKey();
 
		 Util.printByteArray("Private key", serverPrivateKey.getEncoded());
		 System.out.println();
		 Util.printByteArray("Public key", serverPublicKey.getEncoded());

		serverUser = new User("Server", "hostname");
		activeClients = new ArrayList<>();
		port_number = port_num;
		server = new ServerSocket(port_num);
		start();
	}

	// start threads
	public void start() {
		if (serverThread == null) {
			serverThread = new Thread(this);
			serverThread.start();
		}
	}

	// when thread is running, do this
	public void run() {
		while (serverThread != null) {
			try {
				System.out.println("Connecting to user");
				Socket client = server.accept();
				addClient(client);
			} catch (IOException e) {
				System.out.println("Error in run() method");
				System.out.println(e.getMessage());
			}
		}
	}// end run

	// when new client accepted, add them to list and start ServerThread for that
	// client
	public void addClient(Socket client) {
		System.out.println("User connected");
		activeClients.add(new ServerThread(this, client));
		activeClients.get(activeClients.size() - 1).start();// start thread

		Util.printlnc("--------------------------------------------", Util.Color.YELLOW_BOLD);
		Util.printlnc("Sending Public Certificate to Client", Util.Color.YELLOW_BOLD);
		Util.printlnc("--------------------------------------------", Util.Color.YELLOW_BOLD);
		System.out.println();

		activeClients.get(activeClients.size() - 1).recieveMessage(new Message("#Welcome, Here is our Certificate ", serverUser, serverPublicCertificate));
	}// end addClient

	// send given message to all active clients
	public synchronized void broadcast(int sentFromID, Message m) {
		int size = activeClients.size();
		for (int i = 0; i < size; i++) {
			if (activeClients.get(i).id != sentFromID) {
				activeClients.get(i).recieveMessage(m);
			}
		}
	}

	public static void main(String[] args)
			throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
			UnrecoverableKeyException
	{
		Util.printlnc("--------------------------------------------", Util.Color.CYAN_BOLD_BRIGHT);
        Util.printlnc("Server Started", Util.Color.CYAN_BOLD_BRIGHT);
        Util.printlnc("--------------------------------------------", Util.Color.CYAN_BOLD_BRIGHT);

       
		
		ChatAppServer main_server = null;
		System.out.println("Starting Server");
		try
		{
			main_server = new ChatAppServer(6000);
		}
		catch (IOException ie)
		{
			System.out.println("Error in ChatAppServer main()");
		}
		
	}
}