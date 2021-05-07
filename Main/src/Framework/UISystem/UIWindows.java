package Framework.UISystem;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.Statement;

import Framework.Networking.DB;
import Framework.Networking.Session;
import Setup.Main;


public class UIWindows {
    public Window startScreen;
    public Window loginScreen;
        public Window priorLoginScreen;
    public Window savesScreen;
        public List saves;
    public Window createUserScreen;
    public Window pauseScreen;
    
    public static int verticalSpacer = 10;
    public static int buttonHeight = 40;
    public static int headlineSize = 50;
    
    public UIWindows(){
        makeStartScreen();
        makeLoginScreen();
        makeSavesScreen();
        makeCreateUserScreen();
        makePauseScreen();
    }
    public void step(){
        startScreen.stepWindow();
        loginScreen.stepWindow();
        savesScreen.stepWindow();
        createUserScreen.stepWindow();
        pauseScreen.stepWindow();
    }
    public void draw(){
        startScreen.drawWindow();
        loginScreen.drawWindow();
        savesScreen.drawWindow();
        createUserScreen.drawWindow();
        pauseScreen.drawWindow();
    }

    public void makeStartScreen(){
        startScreen = new Window(Main.main.width/4, Main.main.height/4, Main.main.width/2, (int) (Main.main.height/2), "STARTSCREEN");
        startScreen.elements.add(new TextDisplay("GameTitle", "ZOMBIE GAME", Main.main.width/4, headlineSize/2+30, headlineSize, startScreen, Main.main.CENTER));
        startScreen.elements.add(new Button("Start", "Start game", (int) (Main.main.width/8), headlineSize+30+verticalSpacer, Main.main.width/4, buttonHeight, startScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("START GAME");
                //Main.startGameFromGameSave();
                Main.createNewGame();
                startScreen.isActive = false;
            };
        });
        startScreen.elements.add(new Button("Saves", "Start from save", (int) (Main.main.width/8), headlineSize+30+2*verticalSpacer+buttonHeight, Main.main.width/4, buttonHeight, startScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("START GAME from save (i.e. switch to login screen)");
                startScreen.isActive = false;
                if(!Session.loggedIn){
                    loginScreen.isActive = true;
                    priorLoginScreen = startScreen;
                }else{
                    saves.elements.clear();
                    updateSavesList();
                    savesScreen.isActive = true;
                }
            };
        });
        startScreen.elements.add(new Button("CreateUser", "Create new user", (int) (Main.main.width/8), headlineSize+30+3*verticalSpacer+2*buttonHeight, Main.main.width/4, buttonHeight, startScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("SWITCH TO MAKE NEW USER SCREEN");
                startScreen.isActive = false;
                createUserScreen.isActive = true;
            };
        });
        startScreen.isActive = true;
    }
    public void makeLoginScreen(){
        loginScreen = new Window(Main.main.width/4, Main.main.height/4, Main.main.width/2, (int) (Main.main.height/2), "LOGINSCREEN");
        loginScreen.elements.add(new TextDisplay("LoginTitle", "LOGIN", Main.main.width/4, headlineSize/2+30, headlineSize, loginScreen, Main.main.CENTER));
        loginScreen.elements.add(new TextBox("Username", "Username:", Main.main.width/16, headlineSize+30+verticalSpacer, (int) (1.5*Main.main.width/4), buttonHeight, loginScreen));
        loginScreen.elements.add(new TextBox("Password", "Password:", Main.main.width/16, headlineSize+30+2*verticalSpacer+2*buttonHeight, (int) (1.5*Main.main.width/4), buttonHeight, loginScreen));
        loginScreen.elements.add(new Button("Login", "Login", (int) (Main.main.width/8), headlineSize+30+3*verticalSpacer+4*buttonHeight, Main.main.width/4, buttonHeight, loginScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("TRY TO LOGIN");
                String username = loginScreen.getElement("Username").getOutput();
                String password = loginScreen.getElement("Password").getOutput();
                
                //Check om textboxene rent faktisk har indhold
                if((!username.equals("")) && (!password.equals(""))){
                    try {
                        //Check om brugeren allerede eksisterer (altså bruger navnet)
                        Statement st = DB.db.createStatement();
                        ResultSet rs = st.executeQuery("SELECT id, name, password FROM users WHERE ( name = '"+username+"' );");
                        if(rs.next()){
                            String dbHashedPassword = rs.getString("password");
                            //Hvis ja, tjek om det hashede password er det samme som i databasen
                            if(dbHashedPassword.equals(getHash(password))){
                                //Hvis ja, så log ind :)
                                System.out.println("SUCCESSFULL LOGIN :)");
                                //Opdatér session
                                Session.update(rs.getInt("id"), rs.getString("name"));
                                //Ryd felterne
                                loginScreen.getElement("Username").clearText();
                                loginScreen.getElement("Password").clearText();
                                if(priorLoginScreen == startScreen){
                                    //Ryk videre til saves skærmen
                                    loginScreen.isActive = false;
                                    //Opdater saves listen med de nye gamesave navne
                                    saves.elements.clear();
                                    updateSavesList();
                                    savesScreen.isActive = true;
                                }
                            }else{
                                System.out.println("WRONG PASSWORD!! : ( ");
                            }
                        }else{
                            System.out.println("THE USER DOES NOT EXIST");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("NOT ALL THE FIELDS ARE FILLED OUT");
                }
            };
        });
        loginScreen.elements.add(new Button("BackToPriorScreen", "Back to prior screen", (int) (Main.main.width/8), headlineSize+30+4*verticalSpacer+5*buttonHeight, Main.main.width/4, buttonHeight, loginScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("GO BACK TO START SCREEN :)");
                loginScreen.isActive = false;
                loginScreen.getElement("Username").clearText();
                loginScreen.getElement("Password").clearText();
                priorLoginScreen.isActive = true;
            };
        });
        loginScreen.isActive = false;
    }
    public void makeSavesScreen(){
        savesScreen = new Window(Main.main.width/4, Main.main.height/4, Main.main.width/2, (int) (Main.main.height/2), "SAVESSCREEN");
        savesScreen.elements.add(new TextDisplay("GameSavesTitle", "CHOOSE GAME SAVE", Main.main.width/4, headlineSize/2+30, headlineSize, savesScreen, Main.main.CENTER));
        
        int savesListHeight = Main.main.height/2 - (headlineSize+30+verticalSpacer) - 2*buttonHeight - 2*verticalSpacer - 30;
        saves = new List("Saves", "Game saves", Main.main.width/16, headlineSize+30+verticalSpacer, (int) (1.5*Main.main.width/4), savesListHeight, savesScreen);
        savesScreen.elements.add(saves);
        
        savesScreen.elements.add(new Button("BackToStartScreen", "Back to start screen", (int) (Main.main.width/8), headlineSize+30+2*verticalSpacer+savesListHeight, Main.main.width/4, buttonHeight, savesScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("GO BACK TO START SCREEN :)");
                savesScreen.isActive = false;
                startScreen.isActive = true;
            };
        });
        savesScreen.isActive = false;
    }
    public void makeCreateUserScreen(){
        createUserScreen = new Window(Main.main.width/4, Main.main.height/4, Main.main.width/2, (int) (Main.main.height/2), "CREATEUSERSCREEN");
        createUserScreen.elements.add(new TextDisplay("CreateUserTitle", "CREATE USER", Main.main.width/4, headlineSize/2+30, headlineSize, createUserScreen, Main.main.CENTER));
        createUserScreen.elements.add(new TextBox("Username", "Choose your username:", Main.main.width/16, headlineSize+30+verticalSpacer, (int) (1.5*Main.main.width/4), buttonHeight, createUserScreen));
        createUserScreen.elements.add(new TextBox("Password", "Choose your password:", Main.main.width/16, headlineSize+30+2*verticalSpacer+2*buttonHeight, (int) (1.5*Main.main.width/4), buttonHeight, createUserScreen));
        createUserScreen.elements.add(new Button("CreateUser", "Create User", (int) (Main.main.width/8), headlineSize+30+3*verticalSpacer+4*buttonHeight, Main.main.width/4, buttonHeight, createUserScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("TRY TO MAKE THE USER IN THE DATABASE");
                Boolean userAlreadyExists = false;
                Boolean userCreated = false;
                String username = createUserScreen.getElement("Username").getOutput();
                String password = createUserScreen.getElement("Password").getOutput();
                if((!username.equals("")) && (!password.equals(""))){
                    //Check if the username already exists
                    try{
                        Statement st = DB.db.createStatement();
                        ResultSet rs = st.executeQuery("SELECT name FROM users  WHERE ( name = '"+username+"');");
                        //Hvis den allerede eksisterer
                        if(rs.next()){
                            String name = rs.getString("name");
                            //SIG NOGET :)
                            userAlreadyExists = true;
                            System.out.println("THE USER ALREADY EXISTS");
                        }
                        rs.close();
                        st.close();    
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    //Prøv at lav brugeren hvis den ikke allerede eksisterer.
                    if(!userAlreadyExists){
                        try{
                            Statement st = DB.db.createStatement();
                            st.executeUpdate("INSERT INTO users (name, password) VALUES ('"+username+"', '"+getHash(password)+"');");
                            st.close();

                            //We get the users userID
                            st = DB.db.createStatement();
                            ResultSet rs = st.executeQuery("SELECT id, name FROM users  WHERE ( name = '"+username+"');");
                            rs.next();
                            Session.update(rs.getInt("id"), rs.getString("name"));
                            rs.close();
                            st.close();

                            System.out.printf("THE USER with the userId", Session.userID, "and username", Session.username, "WAS CREATED WELL DONE :)", "\n");
                            userCreated = true;
                        }catch(Exception e){
                            e.printStackTrace();
                        }  
                    }
                }else{
                    System.out.println("NOT ALL THE FIELDS ARE FILLED OUT");
                }
            
                if(userCreated){
                    //Fjern info fra felterne
                    createUserScreen.getElement("Username").clearText();
                    createUserScreen.getElement("Password").clearText();
                    //Skift tilbage til hovedskærmen
                    createUserScreen.isActive = false;
                    startScreen.isActive = true;
                    //lav success besked :)

                }
            };
        });
        createUserScreen.elements.add(new Button("BackToStartScreen", "Back to start screen", (int) (Main.main.width/8), headlineSize+30+4*verticalSpacer+5*buttonHeight, Main.main.width/4, buttonHeight, createUserScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("GO BACK TO START SCREEN :)");
                createUserScreen.isActive = false;
                createUserScreen.getElement("Username").clearText();
                createUserScreen.getElement("Password").clearText();
                startScreen.isActive = true;
            };
        });
        createUserScreen.isActive = false;
    }
    public void makePauseScreen(){
        pauseScreen = new Window(Main.main.width/4, Main.main.height/4, Main.main.width/2, (int) (Main.main.height/2), "PAUSESCREEN");
        pauseScreen.elements.add(new TextDisplay("Pause", "Game paused", Main.main.width/4, headlineSize/2+30, headlineSize, pauseScreen, Main.main.CENTER));
        pauseScreen.elements.add(new Button("Save", "Save game", (int) (Main.main.width/8), headlineSize+30+verticalSpacer, Main.main.width/4, buttonHeight, pauseScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("SAVE GAME");
                Main.saveGame();

            };
        });
        pauseScreen.elements.add(new Button("Login", "Login", (int) (Main.main.width/8), headlineSize+30+2*verticalSpacer+buttonHeight, Main.main.width/4, buttonHeight, pauseScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("LOGIN");
                pauseScreen.isActive = false;
                if(!Session.loggedIn){
                    loginScreen.isActive = true;
                    priorLoginScreen = pauseScreen;
                }
            };
        });
        pauseScreen.elements.add(new Button("Return", "Return to game", (int) (Main.main.width/8), headlineSize+30+3*verticalSpacer+2*buttonHeight, Main.main.width/4, buttonHeight, pauseScreen){
            @Override
            public void reactClickedOn(){
                System.out.println("RETURN TO GAME");
                pauseScreen.isActive = false;
                loginScreen.getElement("Username").clearText();
                loginScreen.getElement("Password").clearText();
                Main.timeStop = false;
            };
        });
        pauseScreen.isActive = false;
    }    

    //helpers
    static String getHash(String i){
        try{
            byte[] bytes = i.getBytes(StandardCharsets.UTF_8);
            MessageDigest digest;
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(bytes);

            return bytesToHex(hash);
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }
    private static String bytesToHex(byte[] hash){
        StringBuilder hexString = new StringBuilder(2* hash.length);
        for(int i = 0; i < hash.length; i++){
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1){
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    void updateSavesList(){
        //Get the list of gamesaves associated with the player.
        if(Session.loggedIn){
            //Put them in the saves list, where each button is of GameSaveButton
            try{
                Statement st = DB.db.createStatement();
                ResultSet rs = st.executeQuery("SELECT filename FROM gamesaves  WHERE ( userID = '"+ Session.userID+"');");
                while(rs.next()){
                    String filename = rs.getString("filename");
                    saves.elements.add(new GameSaveButton(filename, filename, Main.main.width/4, buttonHeight, savesScreen));
                }
                rs.close();
                st.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}