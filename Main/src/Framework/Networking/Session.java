package Framework.Networking;

public abstract class Session {
    public static boolean loggedIn = false;
    public static int userID = 0;
    public static String username = null;

    public static void update(int userID_, String username_){
        loggedIn = true;
        userID = userID_;
        username = username_;
    }
}