public class Print {
    public static String red(String s){
        return ("\u001B[31m"+s+"\u001B[0m");
    }
    public static String green(String s){
        return ("\u001B[32m"+s+"\u001B[0m");
    }
    public static String blue(String s){
        return ("\u001B[34m"+s+"\u001B[0m");
    }

    public void redPrt(String s,boolean nexLn){
        if (nexLn == true)
            System.out.println("\u001B[31m"+s+"\u001B[0m");
        else
            System.out.print("\u001B[31m"+s+"\u001B[0m");
    }
    public void greenPrt(String s,boolean nexLn){
        if (nexLn == true)
            System.out.println("\u001B[32m"+s+"\u001B[0m");
        else
            System.out.print("\u001B[32m"+s+"\u001B[0m");
    }
    public void bluePrt(String s,boolean nexLn){
        if (nexLn == true)
            System.out.println("\u001B[34m"+s+"\u001B[0m");
        else
            System.out.print("\u001B[34m"+s+"\u001B[0m");
    }

    public void prtln(String s){
        System.out.println(s);
    }
    public void prt(String s){
        System.out.print(s);
    }
}
