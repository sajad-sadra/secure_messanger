import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  private Print out = new Print();
  private Socket s = null;
    public Server(int PORT){
        try {
            ServerSocket ss = new ServerSocket(PORT);
            out.greenPrt("Server Listening on Port "+PORT,true);
            while(true){
                s = null;
                s = ss.accept();
               out.bluePrt("++++++++++++A new Client ADD++++++++++++",true);
                clientDescript(s);

                Thread t = new ClientHandle(s);
                t.start();
            }

        }catch (IOException ioe){ioe.printStackTrace();}
    }




    private void clientDescript(Socket s){
        out.prtln("PORT:"+s.getPort());
        out.prtln("Local PORT:"+s.getLocalPort());
        out.prtln("Channel:"+s.getChannel());
        out.prtln("Local Adress:"+s.getLocalAddress());

        out.bluePrt("_________________________________________",true);

    }

}



