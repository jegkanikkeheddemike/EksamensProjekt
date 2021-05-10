package Setup;

import java.io.File;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import processing.core.PApplet;
import processing.event.MouseEvent;

import Threads.*;
import Framework.*;
import Framework.GeneticAlgorithm.*;
import Framework.Networking.*;
import Framework.UISystem.*;
import GameObjects.*;
import MapGeneration.*;
import S3FileServer.*;

public class Main extends PApplet {
    public static boolean forceShaders = false;
    private static float score = 0;

    public static boolean isRunning = true;
    public static boolean gameStarted = false;

    public static Main main;
    public static volatile ArrayList<GameObject> allObjects = new ArrayList<GameObject>(); // This should be saved
    private static volatile ArrayList<GameObject> nearObjects = new ArrayList<GameObject>(); // HMM, I think we could
                                                                                             // probably just construct
                                                                                             // that at start up?

    public static ArrayList<GameObject> toBeDelted = new ArrayList<GameObject>(); // The objects that are to be deleted,
                                                                                  // should be deleted before a game
                                                                                  // save
    public static Player player; // This, should be saved as well

    public static int gameTime;

    public static Map m;

    public static UIWindows windows;

    //This is actually not for the database but for the S3 fileserver
    public static DBInterface dbi;



    public Main() {
        main = this;
    }

    @Override
    public void settings() {

        int w = (int) (displayWidth * 0.8);
        int h = (int) (displayHeight * 0.8);

        if (onWindows) {
            size((int) w, (int) h, P2D);
        } else
            size((int) w, (int) h);
    }
    public static void createNewGame(){
        allObjects.clear();
        nearObjects.clear();
        gameStarted = true;
        timeStop = false;

        // MAKING THE REST OF THE MAP
        m = new Map(3);
        player = new Player(m.initialNode);

        m.generateMap();
        m.removeUselessNodes();

        for (Node n : m.initialNode.connected) {
            n.housesAlongParentEdge();
            /*for (Node nn : n.connected) {
                if (nn != n && nn != null) {
                    nn.housesAlongParentEdge();
                }
            }*/
        }

        Random r = new Random();
        while (player.getCollisions(0, 0, new String[] { "Wall", "Zombie", "ClosedDoor" }).length > 0) {
            player.x = r.nextInt(1920);
            player.y = r.nextInt(1080);
        }
    }
    
    public static void startGameFromGameSave(String filename){
        allObjects.clear();
        nearObjects.clear();

        gameStarted = true;
        timeStop = false;

        GameSave gs = GameSave.loadGame(filename);
        m = gs.m;
        player = gs.player;
        allObjects = gs.allObjects;
        nearObjects = gs.nearObjects;
        ZombieGenerator.generations = gs.generations;
        UpdateGroupsThread.update();
    }
    public static void saveGame(){
        if(Session.loggedIn){
            String timeStamp = new SimpleDateFormat("MM-dd-HH-mm").format(new Date());
            String filename = Session.username + "-" + timeStamp + ".sav";
            //Making the .sav file
            GameSave gs = new GameSave(allObjects, nearObjects, player, m, ZombieGenerator.generations);
            gs.saveGame(filename);
            //Uploading it to S3
            dbi.uploadToBucket("eksamensprojektddu", filename);
            //Putting the filename and userid in database
            try{
                Statement st = DB.db.createStatement();
                st.executeUpdate("INSERT INTO gamesaves (userID, fileName) VALUES ('"+Session.userID+"', '"+filename+"');");
                st.close();
            }catch(Exception e){
                e.printStackTrace();
                windows.errorScreen.getElement("ErrorMessage").description = "Could not insert into database";
                windows.errorScreen.show();
            }
            File f = new File(filename);
            f.delete();
            windows.successScreen.getElement("SuccessMessage").description = "Game saved";
            windows.successScreen.show();
        }else{
            windows.errorScreen.getElement("ErrorMessage").description = "You are not logged in";
            windows.errorScreen.show();
        }
    }
    
    @Override
    public void setup() {;
        surface.setTitle("VORES MEGA SEJE GAMER SPIL! UwU");
        Images.loadImages();
        
        //Connect to elephantsql
        DB.connectToDatabase();
        //Connect to S3
        dbi = new DBInterface();
        dbi.connect();

        NearThread.thread.start();
        UpdateGroupsThread.startThread();
        SoundThread.StartThread();

        //makeStartScreen();
        windows = new UIWindows();        

        frameRate(60);

        if (onWindows)
            Shaders.loadShaders();
        if (onWindows)
            Sound.setupSound();

        if (onWindows)
            Sound.setupSound();

    }

    @Override
    public void draw() {
        clear();
        step();
        render();

        // MUST BE LAST
        clearLists();
        updateObjectLists();
        cleanKeyboard();
        gameTime++;
    }

    void render() {
        if(gameStarted){
            float translateX = width / 2 - player.middleX();
            float translateY = height / 2 - player.middleY();

            translate(translateX, translateY);

            try {
                for (int i = 0; i < nearObjects.size(); i++) {
                    GameObject gameObject = nearObjects.get(i);
                    if (gameObject != null &&!gameObject.isDeleted)
                        gameObject.draw();
                }
                m.draw();
            }catch (Exception e) {
                e.printStackTrace();
            }
            if (onWindows && Shaders.shouldDrawShaders()) {
                Shaders.drawZombieFOVCone();
            }
            translate(-translateX, -translateY);
            UI.drawUI();
        }
        //startScreen.drawWindow();
        windows.draw();
    }

    public static boolean timeStop = false;

    void step() {
        //startScreen.stepWindow();
        windows.step();
        if(gameStarted){
            if(!timeStop){
                try {
                    for (int i = 0; i < nearObjects.size(); i++) {
                        GameObject gameObject = nearObjects.get(i);
                        if (gameObject != null &&!gameObject.isDeleted)
                            gameObject.step();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (keyTapped('m')) {
                if (!timeStop){
                    timeStop = true;
                    //SHOW PAUSE SCREEN
                    windows.pauseScreen.isActive = true;
                }else {
                    if(!windows.loginScreen.isActive){
                        timeStop = false;
                        windows.pauseScreen.isActive = false;
                    }
                }
            }
        }

    }

    void clearLists() {
        allObjects.removeAll(toBeDelted);
        nearObjects.removeAll(toBeDelted);

        toBeDelted.clear();
    }

    void updateObjectLists() {
        if (NearThread.isReady) {
            nearObjects.clear();
            nearObjects.addAll(NearThread.nearObjectsUpdated);
            NearThread.walls.clear();
            NearThread.walls.addAll(NearThread.tempWalls);
            NearThread.zombies.clear();
            NearThread.zombies.addAll(NearThread.tempZombies);
        }
    }

    public static GameObject[] getNear() {
        GameObject[] nearArray = new GameObject[nearObjects.size()];
        nearArray = nearObjects.toArray(nearArray);
        return nearArray;
    }

    public static void addObjectToLists(GameObject gameObject) {
        allObjects.add(gameObject);
        nearObjects.add(gameObject);
    }

    ArrayList<Integer> downKeys = new ArrayList<Integer>();
    public static ArrayList<Integer> tappedKeys = new ArrayList<Integer>();
    ArrayList<Integer> ignoredChar = new ArrayList<Integer>();
    public static boolean mousePressed = false;
    public static boolean mouseReleased = false;
    public static float scrollAmount = 0;

    public void keyPressed() {
        int k = (int) key;
        if (key == CODED) {
            if (keyCode == SHIFT) {
                k = -1;
            } else if (keyCode == LEFT) {
                k = -2;
            } else if (keyCode == RIGHT) {
                k = -3;
            } else if (keyCode == ALT) {
                k = -4;
            }
        } else {
            k = (int) Character.toLowerCase(key);

            switch (key) {
                case '!':
                    k = '1';
                    break;
                case '\"':
                    k = '2';
                    break;
                default:
                    break;
            }

        }
        if (!downKeys.contains(k) && !ignoredChar.contains(k)) {
            downKeys.add(k);
            tappedKeys.add(k);
        }
    }

    public void keyReleased() {
        int k = (int) key;
        if (key == CODED) {
            if (keyCode == SHIFT) {
                k = -1;
            } else if (keyCode == LEFT) {
                k = -2;
            } else if (keyCode == RIGHT) {
                k = -3;
            } else if (keyCode == ALT) {
                k = -4;
            }
        } else {
            k = (int) Character.toLowerCase(key);
            switch (key) {
                case '!':
                    k = '1';
                    break;
                case '\"':
                    k = '2';
                    break;
                default:
                    break;
            }

        }
        while (downKeys.contains(k)) {
            downKeys.remove(downKeys.indexOf(k));
        }
    }

    public void mouseWheel(MouseEvent e) {
        scrollAmount = -8 * e.getCount();
    }

    public static boolean keyDown(char input) {
        if (main.downKeys.contains((int) input)) {
            return true;
        }
        return false;
    }

    public static boolean keyDown(int input) {
        if (main.downKeys.contains(input)) {
            return true;
        }
        return false;
    }

    public static boolean keyTapped(char input) {
        if (tappedKeys.contains((int) input)) {
            return true;
        }
        return false;
    }

    public static boolean keyTapped(int input) {
        if (tappedKeys.contains(input)) {
            return true;
        }
        return false;
    }

    public void mousePressed() {
        mousePressed = true;
    }

    public static int getMouseX() {
        return main.mouseX + (int) player.middleX() - main.width / 2;
    }

    public void mouseReleased() {
        mouseReleased = true;
    }

    public static int getMouseY() {
        return main.mouseY + (int) player.middleY() - main.height / 2;
    }

    void cleanKeyboard() {
        mousePressed = false;
        mouseReleased = false;
        tappedKeys.clear();
        scrollAmount *= 0.7;
    }

    void defineIgnoredChar() {
        int t = TAB;
        int d = DELETE;
        ignoredChar.add((Integer) t);
        ignoredChar.add((Integer) d);
    }

    public static boolean onWindows = true;

    public static void addToScore(float s){
        score += s;
    }
    public static float getScore(){
        return score;
    }

    public static void main(String[] args) {

        if (!System.getProperty("os.name").equals("Windows 10"))
            onWindows = false;

        PApplet.main(new Main().getClass());
    }
}