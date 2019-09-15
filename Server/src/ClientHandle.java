import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandle extends Thread {
    private Trans trans = null;
    private Print out = new Print();
    private Login login = null;
    private long secretKey = 0000;

    public ClientHandle(Socket s) {
        trans = new Trans(s);
    }

    @Override
    public void run() {
        //Login OR Signup
        trans.send("1. Signup\n2. Login\n0. End", true);
        boolean cmdDone = false;
        while (!cmdDone) {
            cmdDone = false;
            trans.send("Choose Option:", false);
            String cmd = trans.recive();
            cmd = cmd.toLowerCase();

            if (cmd.equals("signup") || cmd.equals("1"))
                SignUp();
            else if (cmd.equals("login") || cmd.equals("2"))
                cmdDone = LogIn();
            else if (cmd.equals("end") || cmd.equals("0"))
                cmdDone = stopClient(false);
            else
                trans.send(Print.red("INVALID--"), false);

        }
        cmdDone = false;
        //HomePage :)
        HomePage hp = new HomePage(login.getUserName());
        trans.send("1. Show Users\n2. BroadCast Inbox\n3. Send BroadCast\n4. Delete ME!\n5. *Send private massage*\n0. End", true);
        while (!cmdDone) {
            if (login.isConvStart())
                startReciveMessage();
            cmdDone = false;
            trans.send("Choose Option:", false);
            String cmd = trans.recive();
            cmd = cmd.toLowerCase();

            if (cmd.equals("end") || cmd.equals("0"))
                cmdDone = stopClient(true);
            else if (cmd.equals("showuser")||cmd.equals("show user")||cmd.equals("1"))
                viewUsers(hp);
            else if (cmd.equals("broadcastinbox")||cmd.equals("broadcast inbox")||cmd.equals("inbox")||cmd.equals("2"))
                viewInbox(hp);
            else if (cmd.equals("sendbroadcast")||cmd.equals("send broad cast")||cmd.equals("send broadcast")||cmd.equals("broadcast")||cmd.equals("broad cast")||cmd.equals("send")||cmd.equals("3"))
                sendBroad(hp);
            else if (cmd.equals("deleteme")||cmd.equals("delete me")||cmd.equals("4"))
                cmdDone = deleteCurrentUser();
            else if (cmd.equals("send")||cmd.equals("send private")||cmd.equals("send message")||cmd.equals("send private message")||cmd.equals("5"))
                startSendingMessage(hp);
            else
                trans.send(Print.red("INVALID--"), false);
        }


    }

private void printFeatureDescript(){
    trans.send("This part of application use End-To-End encryption algorithms",true);
    trans.send("The server has no Interference in this conversation",true);
    trans.send("Server just setup connection between you and your destination contact...",true);
    trans.send("Then you connect to destination "+Print.blue("*Point-To-Point*"),true);
    trans.send("All of your Message will be "+Print.blue("Encrypted"),true);
    trans.send("____________________________________________________________________",true);
}

    private boolean startReciveMessage(){
        trans.send(Print.green(login.checkForConv()[0]),false);
        trans.send(" start a conversation with You...",true);
        trans.send("=======================================================",true);

        out.prt(login.getUserName());
        out.greenPrt(" ^VS^ ",false);
        out.prt(login.checkForConv()[0]);
        out.greenPrt("  SecretKey: ",false);
        out.prtln(String.format("%d",secretKey));

        printFeatureDescript();
        trans.send("{join}",true);
        trans.send(login.checkForConv()[1],true);
        trans.send(login.checkForConv()[2],true);
        return true;
    }
    private boolean startSendingMessage(HomePage hp){
       printFeatureDescript();
        ArrayList<String> users;
        try{
            users = hp.getUsers();
        }
        catch (FileNotFoundException e){
            trans.send(Print.red("EROR in Showing User"),true);
            out.redPrt("EROR in Read list.txt",true);
            return false;
        }
        trans.send("Here is name of Online User:",true);
        for (int i = 0; i < users.size(); i++){
            if (Login.online.contains(users.get(i)))
                trans.send(Print.green(users.get(i)),false);
            trans.send("  ",false);
        }
        trans.send("\nType one of them to start conversation: ",false);
        String destUser = trans.recive();
        if (!users.contains(destUser))
        {
            trans.send(Print.red("NOT FOUND"),true);
            return false;
        }
        trans.send("What is your IP? ",false);
        String IP = trans.recive();
        out.prtln(login.getUserName()+"("+IP+") send request to "+destUser);
        try {
            secretKey = Main.generateKey();
            login.setConv(destUser,IP,secretKey);
        }catch (IOException ioe){
            trans.send(Print.red("EROR in Setup Connection"),true);
            out.prt("EROR in writing request on ");
            out.redPrt(destUser+".txt",true);
            return false;
        }
        trans.send("{create}",true);
        trans.send(IP,true);
        trans.send(String.format("%d",secretKey),true);
        return true;
    }


    private boolean stopClient(boolean logout){
        if (logout == true)
            trans.send(Print.red("*********")+Print.blue("GOOD BYE")+Print.green(login.getUserName())+Print.red("*********"),true);
        else
            trans.send(Print.blue("********BYE*********"),true);

        if (logout == true)
            Login.Logout(login.getUserName());
        trans.send("{end}",false);
        this.stop();
        return true;
    }
    private boolean deleteCurrentUser(){
        trans.send(Print.red("Do you Sure that You want Delete Your Account[Y/N]? "),false);
        String yn = trans.recive();
        if (yn.equals("Y")||yn.equals("y")||yn.equals("Yes")||yn.equals("YES")||yn.equals("yes"))
        {
            trans.send("Enter Your Pass:",false);
            String pass = trans.recieveHash();
            boolean correctPass = false;
            try {
                correctPass = login.checkPass(pass);
            }catch (FileNotFoundException e){e.printStackTrace();}
            if (correctPass == true)
                 out.prtln(Print.blue(login.getUserName())+Print.red("DELETED"));
            else {
                trans.send(Print.red("FALSE--"), false);
                return false;
            }
            stopClient(true);
            return true;
        }
        else
            return false;
    }

    private boolean SignUp() {
        Signup signup = new Signup();
        trans.send("Enter UserName: ", false);
        String username = trans.recive();
        try {
            boolean userNOTexist = signup.setUser(username);
            if (userNOTexist == false){
                trans.send(Print.red("UserName existed"),true);
                return false;
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            trans.send(Print.red("EROR in create account"),true);
            out.prt("EROR in Create File ");
            out.redPrt(username+".txt",true);
            return false;
        }
        trans.send("Enter Pass:", false);
        String pass1 = trans.recieveHash();
        trans.send("ReEnter Pass:",false);
        String pass2 = trans.recieveHash();
        if (!pass1.equals(pass2)){
            trans.send(Print.red("NOT MATCH"),true);
            signup.deleteUser();
            return false;
        }

        try {
            signup.setPass(pass1);
        }catch (IOException ioe){
            out.prt("EROR in Writing Pass To File");
            out.redPrt(username+".txt",true);
            trans.send(Print.red("EROR in SetUp Pass"),true);
            signup.deleteUser();
            return false;
        }
        return true;
    }
    private boolean LogIn(){
        login = new Login();
        trans.send("UserName: ",false);
        String user = trans.recive();
        if (!login.checkUsername(user)){
            trans.send(Print. blue("User Not Found"),true);
            return false;
        }

        trans.send("Password:",false);
        String pass = trans.recieveHash();
        try {
            if (!login.checkPass(pass))
            {
                trans.send(Print.red("FALSE--"),false);
                return false;
            }
        }catch (FileNotFoundException fnfe){
            out.prt("EROR in Read File ");
            out.redPrt(user+".txt",false);
            out.prtln(" for check Pass");
            trans.send(Print.red("EROR in cheking Pass"),true);
            return false;
        }
        out.prtln("^^^^^^"+Print.blue(user)+"Loged In^^^^^^");
        trans.send(Print.green(",,,,,,,,,,Welcome ")+Print.blue(user)+Print.green(",,,,,,,,,,"),true);
        Login.online.add(user);
        return true;
    }

    private boolean viewUsers(HomePage hp){
        ArrayList<String> users;
        try{
            users = hp.getUsers();
        }
        catch (FileNotFoundException e){
            trans.send(Print.red("EROR in Showing User"),true);
            out.redPrt("EROR in Read list.txt",true);
            return false;
        }
        trans.send("Hint: "+ Print.blue("Offline")+"    "+Print.green("Online"),true);
        trans.send("_______________________________________________________________",true);
        for (int i = 0; i < users.size(); i++){
            if (Login.online.contains(users.get(i)))
                trans.send(Print.green(users.get(i)),true);
            else
                trans.send(Print.blue(users.get(i)),true);
        }
        trans.send("_______________________________________________________________",true);

        return true;
    }
    private boolean viewInbox(HomePage hp){
        trans.send(Print.blue("<<<<<<<<<<<<<<<<<<INBOX>>>>>>>>>>>>>>>"),true);
        ArrayList<String> box = null;
        try{
            box = hp.getInbox();
        }catch (FileNotFoundException fnfe){
            trans.send(Print.red("EROR in showing Inbox"),true);
            out.prt("EROR in read File ");
            out.redPrt(login.getUserName()+".txt",true);
            return false;
        }

        for (int i = 0; i <box.size(); i++)
            trans.send(box.get(i),true);
        trans.send(Print.blue("==================================================="),true);
        return true;
    }
    private boolean sendBroad(HomePage hp){
        trans.send(Print.red("Notice:")+"This Message That you Send save to all users Inbox",true);
        trans.send(Print.blue("-->"),false);
        String message = trans.recive();
        ArrayList<String> users = null;
        try {
            users = hp.getUsers();
        }catch (FileNotFoundException fnfe){
            trans.send(Print.red("EROR in Finding User"),true);
            out.redPrt("EROR in Read list.txt",true);
            return false;
        }
        for (int i = 0; i < users.size(); i++) {
            File useri = new File("usersFile/"+users.get(i)+".txt");
            try {
                FileWriter fw = new FileWriter(useri,true);
                fw.append('\n').write(login.getUserName()+": "+message);
                fw.close();
            }catch (IOException ioe){
                trans.send(Print.red("EROR in Send Message to ")+users.get(i),true);
                out.prt("EROR Writing "+Print.red(login.getUserName())+"Message to File ");
                out.redPrt(users.get(i)+".txt",true);
                return false;
            }
        }
        return true;
    }

}

