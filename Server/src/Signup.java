import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class Signup {

    private File file = null;
    private Print out = new Print();
    private String userName;
    public boolean setUser(String userName) throws IOException{
        this.userName = userName;
        file = new File("usersFile/"+userName+".txt");
        if (file.exists() == true)
            return false;
        file.getParentFile().mkdirs();
        file.createNewFile();


        return true;
    }

    public void setPass(String pass) throws IOException {
        Formatter fr = new Formatter(file);
        fr.format("%s\n",pass);
        fr.close();
        addTolist(userName);
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

    private static boolean firstUser = false;

    private void addTolist (String username) throws IOException{
        File list = new File("usersFile/list.txt");
        list.createNewFile();
        FileWriter fw = new FileWriter(list,true);
        if (firstUser == false)
        {
            fw.write(username);
            firstUser = true;
        }
        fw.append('\n').write(username);
        fw.close();
    }
}
