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
        public Window priorSavesScreen;
    public Window createUserScreen;
    public Window pauseScreen;
    public Window deathScreen;

    public TimedWindow successScreen;
    public TimedWindow errorScreen;
    
    public static int verticalSpacer = 10;
    public static int buttonHeight = 40;
    public static int headlineSize = 50;
    
    public UIWindows(){
        makeStartScreen();
        makeLoginScreen();
        makeSavesScreen();
        makeCreateUserScreen();
        makePauseScreen();
        makeDeathScreen();
        makeTimedScreens();
    }
    
    public void step(){
        startScreen.stepWindow();
        loginScreen.stepWindow();
        savesScreen.stepWindow();
        createUserScreen.stepWindow();
        pauseScreen.stepWindow();
        deathScreen.stepWindow();
        successScreen.stepWindow();
        errorScreen.stepWindow();
    }
    public void draw(){
        deathScreen.drawWindow();
        startScreen.drawWindow();
        loginScreen.drawWindow();
        savesScreen.drawWindow();
        createUserScreen.drawWindow();
        pauseScreen.drawWindow();
        successScreen.drawWindow();
        errorScreen.drawWindow();
    }

    private void makeStartScreen(){
        startScreen = new Window(Main.main.width/4, Main.main.height/4, Main.main.width/2, (int) (Main.main.height/2), "STARTSCREEN");
        startScreen.elements.add(new TextDisplay("GameTitle", "ZOMBIE GAME", Main.main.width/4, headlineSize/2+30, headlineSize, startScreen, Main.main.CENTER));
        startScreen.elements.add(new Button("Start", "Start game", (int) (Main.main.width/8), headlineSize+30+verticalSpacer, Main.main.width/4, buttonHeight, startScreen){
            @Override
            public void reactClickedOn(){
                Main.createNewGame();
                startScreen.isActive = false;
            };
        });
        startScreen.elements.add(new Button("Saves", "Start from save", (int) (Main.main.width/8), headlineSize+30+2*verticalSpacer+buttonHeight, Main.main.width/4, buttonHeight, startScreen){
            @Override
            public void reactClickedOn(){
                startScreen.isActive = false;
                if(!Session.loggedIn){
                    loginScreen.isActive = true;
                    priorLoginScreen = startScreen;
                }else{
                    priorSavesScreen = startScreen;
                    saves.elements.clear();
                    updateSavesList();
                    savesScreen.isActive = true;
                }
            };
        });
        startScreen.elements.add(new Button("CreateUser", "Create new user", (int) (Main.main.width/8), headlineSize+30+3*verticalSpacer+2*buttonHeight, Main.main.width/4, buttonHeight, startScreen){
            @Override
            public void reactClickedOn(){
                startScreen.isActive = false;
                createUserScreen.isActive = true;
            };
        });
        startScreen.isActive = true;
    }
    private void makeLoginScreen(){
        loginScreen = new Window(Main.main.width/4, Main.main.height/4, Main.main.width/2, (int) (Main.main.height/2), "LOGINSCREEN");
        loginScreen.elements.add(new TextDisplay("LoginTitle", "LOGIN", Main.main.width/4, headlineSize/2+30, headlineSize, loginScreen, Main.main.CENTER));
        loginScreen.elements.add(new TextBox("Username", "Username:", Main.main.width/16, headlineSize+30+verticalSpacer, (int) (1.5*Main.main.width/4), buttonHeight, loginScreen));
        loginScreen.elements.add(new TextBox("Password", "Password:", Main.main.width/16, headlineSize+30+2*verticalSpacer+2*buttonHeight, (int) (1.5*Main.main.width/4), buttonHeight, loginScreen));
        loginScreen.elements.add(new Button("Login", "Login", (int) (Main.main.width/8), headlineSize+30+3*verticalSpacer+4*buttonHeight, Main.main.width/4, buttonHeight, loginScreen){
            @Override
            public void reactClickedOn(){
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
                                successScreen.getElement("SuccessMessage").description = "Successful login";
                                successScreen.show();
                                //Opdatér session
                                Session.update(rs.getInt("id"), rs.getString("name"));
                                //Ryd felterne
                                loginScreen.getElement("Username").clearText();
                                loginScreen.getElement("Password").clearText();
                                if(priorLoginScreen == startScreen){
                                    //Ryk videre til saves skærmen
                                    loginScreen.isActive = false;
                                    //Opdater saves listen med de nye gamesave navne
                                    priorSavesScreen = startScreen;
                                    saves.elements.clear();
                                    updateSavesList();
                                    savesScreen.isActive = true;
                                }
                            }else{
                                errorScreen.getElement("ErrorMessage").description = "Wrong password";
                                errorScreen.show();
                            }
                        }else{
                            errorScreen.getElement("ErrorMessage").description = "No user with that username";
                            errorScreen.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    errorScreen.getElement("ErrorMessage").description = "Not all fields are filled out";
                    errorScreen.show();
                }
            };
        });
        loginScreen.elements.add(new Button("BackToPriorScreen", "Back to prior screen", (int) (Main.main.width/8), headlineSize+30+4*verticalSpacer+5*buttonHeight, Main.main.width/4, buttonHeight, loginScreen){
            @Override
            public void reactClickedOn(){
                loginScreen.isActive = false;
                loginScreen.getElement("Username").clearText();
                loginScreen.getElement("Password").clearText();
                priorLoginScreen.isActive = true;
            };
        });
        loginScreen.isActive = false;
    }
    //GØR SÅDAN AT DEN GÅR TILBAGE TIL DEN SKÆRM DER LEDTE DEN DERHEN
    private void makeSavesScreen(){
        savesScreen = new Window(Main.main.width/4, Main.main.height/4, Main.main.width/2, (int) (Main.main.height/2), "SAVESSCREEN");
        savesScreen.elements.add(new TextDisplay("GameSavesTitle", "CHOOSE GAME SAVE", Main.main.width/4, headlineSize/2+30, headlineSize, savesScreen, Main.main.CENTER));
        
        int savesListHeight = Main.main.height/2 - (headlineSize+30+verticalSpacer) - 2*buttonHeight - 2*verticalSpacer - 30;
        saves = new List("Saves", "Game saves", Main.main.width/16, headlineSize+30+verticalSpacer, (int) (1.5*Main.main.width/4), savesListHeight, savesScreen);
        savesScreen.elements.add(saves);
        
        savesScreen.elements.add(new Button("BackToPriorScreen", "Back to prior screen", (int) (Main.main.width/8), headlineSize+30+2*verticalSpacer+savesListHeight, Main.main.width/4, buttonHeight, savesScreen){
            @Override
            public void reactClickedOn(){
                savesScreen.isActive = false;
                priorSavesScreen.isActive = true;
            };
        });
        savesScreen.isActive = false;
    }
    private void makeCreateUserScreen(){
        createUserScreen = new Window(Main.main.width/4, Main.main.height/4, Main.main.width/2, (int) (Main.main.height/2), "CREATEUSERSCREEN");
        createUserScreen.elements.add(new TextDisplay("CreateUserTitle", "CREATE USER", Main.main.width/4, headlineSize/2+30, headlineSize, createUserScreen, Main.main.CENTER));
        createUserScreen.elements.add(new TextBox("Username", "Choose your username:", Main.main.width/16, headlineSize+30+verticalSpacer, (int) (1.5*Main.main.width/4), buttonHeight, createUserScreen));
        createUserScreen.elements.add(new TextBox("Password", "Choose your password:", Main.main.width/16, headlineSize+30+2*verticalSpacer+2*buttonHeight, (int) (1.5*Main.main.width/4), buttonHeight, createUserScreen));
        createUserScreen.elements.add(new Button("CreateUser", "Create User", (int) (Main.main.width/8), headlineSize+30+3*verticalSpacer+4*buttonHeight, Main.main.width/4, buttonHeight, createUserScreen){
            @Override
            public void reactClickedOn(){
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
                            errorScreen.getElement("ErrorMessage").description = "User already exists";
                            errorScreen.show();
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

                            successScreen.getElement("SuccessMessage").description = "User " + Session.username + " was created";
                            successScreen.show();

                            userCreated = true;
                        }catch(Exception e){
                            errorScreen.getElement("ErrorMessage").description = "Can not connect to database";
                            errorScreen.show();
                            e.printStackTrace();
                        }  
                    }
                }else{
                    errorScreen.getElement("ErrorMessage").description = "Not all fields are filled out";
                    errorScreen.show();
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
                createUserScreen.isActive = false;
                createUserScreen.getElement("Username").clearText();
                createUserScreen.getElement("Password").clearText();
                startScreen.isActive = true;
            };
        });
        createUserScreen.isActive = false;
    }
    private void makePauseScreen(){
        pauseScreen = new Window(Main.main.width/4, Main.main.height/4, Main.main.width/2, (int) (Main.main.height/2), "PAUSESCREEN");
        pauseScreen.elements.add(new TextDisplay("Pause", "Game paused", Main.main.width/4, headlineSize/2+30, headlineSize, pauseScreen, Main.CENTER));
        pauseScreen.elements.add(new Button("Save", "Save game", (int) (Main.main.width/8), headlineSize+30+verticalSpacer, Main.main.width/4, buttonHeight, pauseScreen){
            @Override
            public void reactClickedOn(){
                Main.saveGame();
            };
        });
        pauseScreen.elements.add(new Button("Login", "Login", (int) (Main.main.width/8), headlineSize+30+2*verticalSpacer+buttonHeight, Main.main.width/4, buttonHeight, pauseScreen){
            @Override
            public void reactClickedOn(){
                pauseScreen.isActive = false;
                loginScreen.isActive = true;
                priorLoginScreen = pauseScreen;
            };
        });
        pauseScreen.elements.add(new Button("Return", "Return to game", (int) (Main.main.width/8), headlineSize+30+3*verticalSpacer+2*buttonHeight, Main.main.width/4, buttonHeight, pauseScreen){
            @Override
            public void reactClickedOn(){
                pauseScreen.isActive = false;
                loginScreen.getElement("Username").clearText();
                loginScreen.getElement("Password").clearText();
                Main.timeStop = false;
            };
        });
        pauseScreen.isActive = false;
    }    
    private void makeDeathScreen() {
        deathScreen = new Window(Main.main.width/4, Main.main.height/4,Main.main.width/2,Main.main.height/2,"DeathScreen");
        deathScreen.elements.add(new TextDisplay("Death", "You Died", Main.main.width/4, headlineSize/2+30, headlineSize, deathScreen, Main.CENTER));
        deathScreen.elements.add(new Button("LoadSave", "Load Save", (int) (Main.main.width/8), headlineSize+30+verticalSpacer, Main.main.width/4, buttonHeight, deathScreen){
            @Override
            public void reactClickedOn(){
                deathScreen.isActive = false;
                priorSavesScreen = deathScreen;
                saves.elements.clear();
                updateSavesList();
                savesScreen.isActive = true;
                
            }
        });
        deathScreen.elements.add(new Button("MainMenu","Main Menu",(Main.main.width/8), headlineSize+30+2*verticalSpacer+buttonHeight, Main.main.width/4, buttonHeight, deathScreen){
            @Override
            public void reactClickedOn(){
                deathScreen.isActive = false;
                startScreen.isActive = true;
            }
        });

        deathScreen.elements.add(new Button("ExitGame","Exit Game",(Main.main.width/8), headlineSize+30+3*verticalSpacer+2*buttonHeight, Main.main.width/4, buttonHeight, deathScreen){
            @Override
            public void reactClickedOn(){
                Main.main.exit();
            }
        });

        deathScreen.isActive = false;
    }
    public void makeTimedScreens(){
        errorScreen = new TimedWindow(0, 0, Main.main.width, Main.main.height/5, "Error", 1000);
        errorScreen.backdropColor = Main.main.color(255, 0, 0, 125);
        errorScreen.elements.add(new TextDisplay("ErrorMessage", "", Main.main.width/2, (int) 30+(Main.main.height/10), 60, errorScreen, Main.main.CENTER, Main.main.color(255)));
      
        //SUCCESSWINDOW
        successScreen = new TimedWindow(0, 0, Main.main.width, Main.main.height/5, "Success", 1000);
        successScreen.backdropColor = Main.main.color(0, 255, 0, 125);
        successScreen.elements.add(new TextDisplay("SuccessMessage", "", Main.main.width/2, (int) 30+(Main.main.height/10), 60, successScreen, Main.main.CENTER, Main.main.color(255)));
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