import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class Login {
    private String userName = null;
    private File file = null;
    private Print out = new Print();
    static ArrayList <String> online = new ArrayList<>();

    public boolean checkUsername(String userName){
        this.userName = userName;
        file = new File("usersFile/"+userName+".txt");
        if (file.exists() == false)
            return false;

        return true;
    }

    public boolean checkPass(String password) throws FileNotFoundException {
            Scanner scanPass = new Scanner(file);
            String pass = scanPass.nextLine();

            if (pass.equals(password))
                return true;
            else
                return false;
    }

    public void deleteUser(){
        file.delete();//Delete UserFile.txt
        //TO REMOVE from List.txt
        ArrayList<String> finalUser = new ArrayList<>();
        File list = new File("usersFile/list.txt");
        try {
            Scanner scan = new Scanner(list);
            while (scan.hasNextLine()){
                String tt = scan.nextLine();
                if (!tt.equals(userName))
                    finalUser.add(tt);
            }
            file.delete();
            file.createNewFile();
            Formatter fr = new Formatter(list);
            for (int i = 0; i < finalUser.size(); i++) {
                fr.format("%s\n",finalUser.get(i));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setConv(String destUser,String serverIP,long secretKey) throws IOException{
        File destFile = new File("usersFile/"+destUser+".txt");
        destFile.delete();
        destFile.createNewFile();
        Formatter fr = new Formatter(file);
        fr.format("%s %s %s %d","^$^",getUserName(),serverIP,secretKey);
        fr.close();
    }
    public boolean isConvStart(){
        try {
            Scanner scanner = new Scanner(file);
            if (scanner.next().equals("^$^"))
                return true;
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return false;
    }
    public String[] checkForConv(){
        String []ans = new String[3];
        try {
            Scanner scanner = new Scanner(file);
            if (scanner.next().equals("^$^"))
            {
                ans[0] = scanner.next();
                ans[1] = scanner.next();
                ans[2] = scanner.next();
            return ans;
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return null;
    }

    public static void Logout(String userName){
        for (int i = 0; i < online.size(); i++)
            if (online.get(i).equals(userName))
            {
                online.remove(i);
                break;
            }
        System.out.println(Print.blue("-----"+userName+"Loged Out----"));

    }
    public String getUserName(){return this.userName;}
}