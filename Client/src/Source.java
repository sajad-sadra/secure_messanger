import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Source {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Socket con = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        System.out.print("Enter IP of Server:");
        String IP = input.nextLine();
        int PORT = 5056;
        System.out.println("Connecting on PORT "+ PORT);
        try {
            con = new Socket(IP, PORT);
            dos = new DataOutputStream(con.getOutputStream());
            dis = new DataInputStream(con.getInputStream());
            while (true){
                String mess = dis.readUTF();
                if (mess.equals("{end}"))
                {
                    dis.close();
                    dos.close();
                    con.close();
                    System.exit(0);
                    break;
                }
                else if (mess.equals("{}")) {
                    dos.writeUTF(input.nextLine());
                    dos.flush();
                }
                else if (mess.equals("{@}")){
                    System.out.print("\u001B[36m"+"@ "+"\u001B[0m");
                    dos.writeUTF(String.format("%d",input.nextLine().hashCode()));
                    dos.flush();
                }
                else if (mess.equals("{create}")){
                    createServer cs = new createServer(dis.readUTF(),dis.readUTF());
                }
                else if (mess.equals("join")){
                    joinClient jc = new joinClient(dis.readUTF(),dis.readUTF());
                }
                else
                    System.out.print(mess);
            }
        }catch (IOException ioe){ioe.printStackTrace();}

    }
}



class createServer{
    public final static int secretPORT = 9494;
    private long secretKey = 000;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;

    public createServer(String IP,String secretKey)throws IOException{
        this.secretKey = Long.parseLong(secretKey);
        Socket s = null;
        ServerSocket ss = new ServerSocket(secretPORT,1, InetAddress.getByName(IP));
        System.out.println("^^DONE^^\nWaiting for Client to Join...");
        s= ss.accept();
        System.out.println("+++JOINED+++");
        dos = new DataOutputStream(s.getOutputStream());
        dis = new DataInputStream(s.getInputStream());

        boolean contin = true;
        while(contin){
            System.out.print("You: ");
            secureSend((new Scanner(System.in)).nextLine());
            contin = printRecived();
        }
    }

    private void secureSend(String message){
        //encrypte the massege Algorithm with (this.secretKey)
        try {
            dos.writeUTF(message);
        }catch (IOException eee)
        {
            System.out.println("EROR in send Secure message");
        }
    }
    private boolean printRecived(){
        try {
            String message = dis.readUTF();
            //de-encrypt message with (this.secretKey)
            if (message.equals("{end}"))
                return false;
            System.out.println("user: "+message);
        }catch (IOException eee)
        {
            System.out.println("EROR in recive Secure message");
        }
        return true;
    }
}


class joinClient {
    private final static int secretPORT = createServer.secretPORT;
    private long secretKey = 000;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;

    public joinClient(String IP, String secretKey) throws IOException {
        this.secretKey = Long.parseLong(secretKey);
        Socket s = new Socket(IP,secretPORT);
        dos = new DataOutputStream(s.getOutputStream());
        dis = new DataInputStream(s.getInputStream());

        boolean contin = true;
        while(contin){
            contin = printRecived();
            System.out.print("You: ");
            secureSend((new Scanner(System.in)).nextLine());
        }
    }

    private void secureSend(String message){
        //encrypte the massege Algorithm with (this.secretKey)
        try {
            dos.writeUTF(message);
        }catch (IOException eee)
        {
            System.out.println("EROR in send Secure message");
        }
    }
    private boolean printRecived(){
        try {
            String message = dis.readUTF();
            //de-encrypt message with (this.secretKey)
            if (message.equals("{end}"))
                return false;
            System.out.println("user: "+message);
        }catch (IOException eee)
        {
            System.out.println("EROR in recive Secure message");
        }
        return true;
    }
}