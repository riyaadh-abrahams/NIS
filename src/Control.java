import java.io.*;
import java.net.*;
import java.util.*;
class Control extends Thread
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean loggedIn;

    // constructor
    public Control(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.loggedIn=true;
    }

    @Override
    public void run() {

        String receivedMsg;
        while (true)
        {
            try
            {

                receivedMsg = dis.readUTF();

                System.out.println(receivedMsg);

                if(receivedMsg.equals("logout")){
                    this.loggedIn=false;
                    this.s.close();
                    break;
                }

                //Private Message
                if(receivedMsg.charAt(0)=='@')
                {
                    String[] str = receivedMsg.split(" ");

                    String user = str[0].replace("@", "");
                    String[] msgArray = Arrays.copyOfRange(str, 1, str.length);
                    String finalMsg = String.join(" ", msgArray);
    
                    // search for the recipient in the connected devices list.
                    // ar is the vector storing client of active users
                    for (Control userName : Server.ar)
                    {
                        // if the recipient is found, write on its
                        // output stream
                        if (userName.name.equals(user) && userName.loggedIn==true)
                        {
                            userName.dos.writeUTF("\033[0;33m" + this.name + "\033[0m" + ":\033[0;32m(private message)\033[0m "+ finalMsg);
                            break;
                        }
                    }
                }

             /*    //broadcast message
                else
                {
                    for(Control userName: Server.ar)
                    {
                        if (userName.loggedIn==true)
                        {
                            userName.dos.writeUTF("\033[0;33m" + this.name + ": \033[0m"+ receivedMsg);
                            break;
                        }
                    }
                } */

              

            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }




    }
}
