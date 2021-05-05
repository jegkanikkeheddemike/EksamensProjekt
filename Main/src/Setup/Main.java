package Setup;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.event.MouseEvent;

import Threads.*;
import Framework.*;
import Framework.GeneticAlgorithm.ZombieGenerator;
import GameObjects.*;
import GameObjects.Items.AmmoItems.*;
import GameObjects.Items.HealthItems.Bandage;
import GameObjects.Items.HealthItems.HealthPack;
import GameObjects.Items.Weapons.Machete;
import GameObjects.Items.Weapons.Pistol;
import GameObjects.Items.Weapons.*;
import MapGeneration.*;

public class Main extends PApplet {
    private static boolean startFromFile = false;
    private static boolean saveToFile = false;

    public static boolean isRunning = true;

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

    public Main() {
        main = this;
    }

    @Override
    public void settings() {

        double w = displayWidth * 0.8;
        double h = displayHeight * 0.8;
        

        if (onWindows) {
            size((int)w,(int) h, P2D);
        } else
            size((int)w, (int)h);
        
        
    }

    @Override
    public void setup() {
        surface.setResizable(true);
        surface.setTitle("VORES MEGA SEJE GAMER SPIL! UwU");
        Images.loadImages();
        NearThread.thread.start();
        UpdateGroupsThread.startThread();
        SoundThread.StartThread();


        if (!startFromFile) {
            m = new Map(2);
            player = new Player(m.initialNode);

            // MAKING THE REST OF THE MAP
            m.generateMap();
            m.removeUselessNodes();

            for (Node n : m.initialNode.connected) {
                n.housesAlongParentEdge();
                for (Node nn : n.connected) {
                    if (nn != n && nn != null) {
                        nn.housesAlongParentEdge();
                    }
                }
            }
            Random r = new Random();
            while (player.getCollisions(0, 0, new String[] { "Wall", "Zombie" }).length > 0) {
                player.x = r.nextInt(1920);
                player.y = r.nextInt(1080);
            }
            // #region TestObjects
            new AmmoBox9mm(player.x + 50, player.y - 50);
            new AmmoBox45ACP(player.x - 50, player.y + 50);
            new AmmoBoxShells(player.x + 50, player.y - 50);
            new Pistol(player.x, player.y + 100);
            new Shotgun(player.x + 100, player.y);
            new HealthPack(player.x, player.y);
            new Bandage(player.x, player.y);
            new Machete(player.x, player.y);
            // #endregion
        } else {
            GameSave gs = GameSave.loadGame("src/Setup/GS.sav");
            m = gs.m;
            player = gs.player;
            allObjects = gs.allObjects;
            nearObjects = gs.nearObjects;
            ZombieGenerator.generations = gs.generations;
            UpdateGroupsThread.update();
            
        }

        

        frameRate(60);

        if (onWindows)
            Shaders.loadShaders();
        if (onWindows)
            Sound.setupSound();

        if (onWindows) {
            // ShaderPreRenderWorkThread.thread.start();
        }
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
        float translateX = width / 2 - player.middleX();
        float translateY = height / 2 - player.middleY();

        translate(translateX, translateY);

        try {
            for (int i = 0; i < nearObjects.size(); i++) {
                GameObject gameObject = nearObjects.get(i);
                gameObject.draw();
            }

            // m.draw();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (onWindows && Shaders.shouldDrawShaders()) {
            Shaders.drawZombieFOVCone();
        }
        translate(-translateX, -translateY);
        UI.drawUI();
    }

    public boolean timeStop = false;

    void step() {

        try {
            for (int i = 0; i < nearObjects.size(); i++) {
                GameObject gameObject = nearObjects.get(i);
                gameObject.step();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (keyTapped('t')) {
            if (!timeStop)
                timeStop = true;
            else {
                timeStop = false;
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
    ArrayList<Integer> tappedKeys = new ArrayList<Integer>();
    ArrayList<Integer> ignoredChar = new ArrayList<Integer>();
    public static boolean mousePressed = false;
    public static boolean mouseReleased = false;
    float scrollAmount = 0;

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

            if (key == ' ') {
                if (saveToFile) {
                    System.out.println("THE SPACE BAR WAS PRESSED!!!!!!!");
                    GameSave gs = new GameSave(allObjects, nearObjects, player, m, ZombieGenerator.generations);
                    gs.saveGame("GS.sav");
                    System.out.println("GAME SAVED?¿¿¿");
                }
            }

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
        if (main.tappedKeys.contains((int) input)) {
            return true;
        }
        return false;
    }

    public static boolean keyTapped(int input) {
        if (main.tappedKeys.contains(input)) {
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

    public static void main(String[] args) {

        if (!System.getProperty("os.name").equals("Windows 10"))
            onWindows = false;

        PApplet.main("Setup.Main");
    }
}