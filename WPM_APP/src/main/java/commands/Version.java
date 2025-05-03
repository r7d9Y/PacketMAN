package commands;

public class Version {
    public static void printVersion() {
        System.out.println("\r\n" + app.App.getAppName() + " " + app.App.getVERSION() + "\r\n");
    }
}
