import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HomePage {
    private Print out = new Print();
    private String userName = null;

    public HomePage(String userName){
        this.userName = userName;
        out.bluePrt("HomePage Shown For ",false);
        out.greenPrt(userName,true);
    }

    public ArrayList<String> getUsers() throws FileNotFoundException {
        File users = new File("usersFile/list.txt");
        Scanner scan = new Scanner(users);
        ArrayList <String> ans = new ArrayList<>();
        String temp;
        while(scan.hasNextLine()){
            temp = scan.nextLine();
            if (temp.equals(userName)){}
            else
                ans.add(temp);
        }
        return ans;
    }

    public ArrayList<String> getInbox() throws FileNotFoundException{
        ArrayList<String> messages = new ArrayList<>();
        File current = new File("usersFile/"+userName+".txt");
        Scanner scan = new Scanner(current);
        scan.nextLine();
        while (scan.hasNextLine())
            messages.add(scan.nextLine());
        return messages;
    }


}
