import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.DoubleAccumulator;

public class Trans {
    private Socket s =null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private Print out = new Print();

    public Trans(Socket s){
        this.s = s;
        try {
            this.dis = new DataInputStream(s.getInputStream());
            this.dos = new DataOutputStream(s.getOutputStream());
        }catch (IOException ioe){ioe.printStackTrace();}
    }


    public void send(String message,boolean NexLn){
        try {
            if (NexLn == false)
                dos.writeUTF(message);
            else
                dos.writeUTF(message+"\n");
        }catch (IOException ioe){
            out.redPrt("("+message+")" +"NOT SEND",true);
        }
    }

    public String recive(){
        send("{}",false);//Request Client To Recieve
        String message = null;
        try {
            message = dis.readUTF();
        }catch (IOException ioe){
            out.redPrt("EROR in Recieving",true);
        }
        return message;
    }

    public String recieveHash(){
        send("{@}",false);//Request Client To Send password Hash
        String message = null;
        try {
            message = dis.readUTF();
        }catch (IOException ioe){
            out.redPrt("EROR in Recieving",true);
        }
        return message;
    }
}
