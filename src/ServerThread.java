//A thread to handle multiple clients

import java.net.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.*;

public class ServerThread extends Thread {
	// Attributes
	ChatAppServer server;// ServerThread runs in ChatAppServer
	Socket client;
	int id;
	ObjectOutputStream output;
	ObjectInputStream input;

	public ServerThread(ChatAppServer server, Socket client) {
		this.client = client;
		this.server = server;
		id = client.getPort();// the port of each socket uniquely identifies them
		try {
			output = new ObjectOutputStream(client.getOutputStream());// send to clients output
			input = new ObjectInputStream(client.getInputStream());// input from client
		} catch (IOException e) {
			System.out.println("Error in ServerThread con(), IO");
		}

	}

	// server thread recieves a message and sends it to an output stream
	public void recieveMessage(Message m) {
		try {
			output.writeObject(m);
		} catch (IOException c) {
			System.out.println("Error recieveMessage() in ServerThread, ClassNot");
		}
	}

	public void run() {
		// System.out.println("ServerThread runnin " + id);
		while (true)// shouldnt be while true, should be a volatile boolean
		{
			try {
				Message incomingMessage;
				incomingMessage = (Message) input.readObject();// read incoming message and cast into Message
				System.out.println(id + " recieved message:");// debug
				System.out.println("client public key");
				System.out.println(server.clientPublicKey.hashCode());
				System.out.println("server public key");
				System.out.println(server.serverPublicKey.hashCode());
				System.out.println(incomingMessage.DecryptAndGet(server.serverPrivateKey, server.clientPublicKey));// print message to server
																							// console

				// send input to server, which will send it to all clients
				server.broadcast(id, incomingMessage);
			} catch (IOException e) {
				System.out.println("Error in ServerThread run(), IO");
				System.out.println(e);
				System.exit(1);
			} catch (ClassNotFoundException ee) {
				System.out.println("Error in ServerThread run(), Class error");
				System.out.println(ee);
				System.exit(1);
			} // end catch
			catch (InvalidKeyException e) {
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
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}