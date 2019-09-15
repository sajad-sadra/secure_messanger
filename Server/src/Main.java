import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(5056);
    }
    public static long generateKey(){
        Random random = new Random();
        return random.nextLong();
    }
}
