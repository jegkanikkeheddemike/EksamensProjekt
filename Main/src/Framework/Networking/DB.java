package Framework.Networking;

import java.sql.*;

import Setup.Main;

public abstract class DB{
    public static Connection db;
    static String connectionStatus = "Connecting to database...";
    static boolean isConnected = false;
    //HUSK!!
    //Resultset executeQuery(String"") bruges til KUN til at hente data
    //void updateQuery(String"") bruges Kun til at opdatere data i databasen 

    public static void connectToDatabase() {
        Thread connectThread = new Thread() {
            public void run() {
            try {
                Class.forName("org.postgresql.Driver");
            }
            catch (java.lang.ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            String dataBaseUsername = "budnjatm";
            String dataBasePassword = "N7xqaNxm9kabUT1qJOOQ0T_qwXGugjjX";
            String url = "jdbc:postgresql://hattie.db.elephantsql.com:5432/budnjatm";
      
            try {
                db = DriverManager.getConnection(url, dataBaseUsername, dataBasePassword);
                connectionStatus = "Connected to database.";
                isConnected = true;
                System.out.print("Connected to database at");
                System.out.print(Main.main.millis());
                System.out.print("ms \n");
            }
            catch (java.sql.SQLException e) {
                connectionStatus = "No Internet connection";
                System.out.println(e.getMessage());
            }
            }
        };
        connectThread.start();
    }
}