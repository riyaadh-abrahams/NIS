import java.io.*;
import java.util.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

// Server class
public class Server {
    // Vector to store active clients
    static ArrayList<Control> ar = new ArrayList<>();

    static PrivateKey serverPrivateKey;
    static X509Certificate serverPublicCertificate;
    static PublicKey serverPublicKey;
    

    // counter for clients
    static int i = 0;

    public static void main(String[] args)
            throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
            UnrecoverableKeyException
    {
        Util.printlnc("--------------------------------------------", Util.Color.CYAN_BOLD_BRIGHT);
        Util.printlnc("Server Started", Util.Color.CYAN_BOLD_BRIGHT);
        Util.printlnc("--------------------------------------------", Util.Color.CYAN_BOLD_BRIGHT);

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

        Util.printByteArray("Private key", serverPrivateKey.getEncoded());
        System.out.println();
        Util.printByteArray("Public key", serverPublicKey.getEncoded());


        // server is listening on port 1234
        ServerSocket ss = new ServerSocket(1234);
        Util.printlnc("\nWaiting for Clients...", Util.Color.YELLOW_BOLD); System.out.println();

        Socket s;

        // running infinite loop for getting
        // client request
        while (true)
        {
            // Accept the incoming request
            s = ss.accept();

            System.out.println("New client request received : " + s);



            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            System.out.println("Creating a new handler for this client...");

            String name = dis.readUTF();
            System.out.println("Client name is: " + name);

            // Create a new handler object for handling this request.
            Control mtch = new Control(s,name, dis, dos);

            // Create a new Thread with this object.
            Thread t = new Thread(mtch);

            System.out.println("Adding this client to active client list");
            // add this client to active clients list
            ar.add(mtch);

            // start the thread.
            t.start();

            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;

        }
    }
}
