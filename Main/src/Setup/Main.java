package Setup;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.event.MouseEvent;

import Threads.*;
import Framework.*;
import GameObjects.*;
import GameObjects.Items.AmmoItems.*;
import GameObjects.Items.HealthItems.Bandage;
import GameObjects.Items.HealthItems.HealthPack;
import GameObjects.Items.Weapons.Pistol;
import MapGeneration.*;

public class Main extends PApplet {
    public static boolean isRunning = true;

    public static Main main;
    public static volatile ArrayList<GameObject> allObjects = new ArrayList<GameObject>();
    public static volatile ArrayList<GameObject> nearObjects = new ArrayList<GameObject>();

    public static ArrayList<GameObject> toBeDelted = new ArrayList<GameObject>();
    public static Player player;

    public static int gameTime;

    public Map m;

    public Main() {
        main = this;
    }

    @Override
    public void settings() {

        if (onWindows) {
            size(1800, 900, P2D);
        } else
            size(1800, 900);
    }

    @Override
    public void setup() {
        frameRate(60);
        if (onWindows)
            Shaders.loadShaders();
        NearThread.thread.start();
        if (onWindows)
            Sound.setupSound();

        m = new Map(3);
        m.generateMap();
        m.removeUselessNodes();

        for (Node n : m.allNodes) {
            //n.wallsAlongParentEdge();w
            //NOW IT ONLY MAKES A HOUSE On THE EDGE TO THE PARENTS THAT ARE TO THE EAST OF NODES.
            //if(n.parent == n.connected[Map.EAST]){
            n.housesAlongParentEdge();
            //}
        }

        //new Building(-100, -100, width+100, -100, -100, height+100, width+100, height+100,Map.EAST);

        if (onWindows)
            ShaderPreRenderWorkThread.thread.start();
        if (onWindows)
            Sound.setupSound();

        // new Building(0, 0, 1900 - 100, 0, 0, 1100 - 100, 1900 - 100, 1100 - 100,
        // Map.EAST);

        player = new Player();

        Random r = new Random();
        while (player.getCollisions(0, 0, new String[] { "Wall", "Zombie" }).length > 0) {
            player.x = r.nextInt(1920);
            player.y = r.nextInt(1080);
        }

        // #region TestObjects
        new AmmoBox9mm(player.x + 50, player.y + 50);
        new AmmoBox9mm(player.x - 50, player.y + 50);
        new AmmoBox9mm(player.x + 50, player.y - 50);
        new AmmoBox9mm(player.x - 50, player.y - 50);
        new Pistol(player.x, player.y + 100);
        new HealthPack(player.x, player.y);
        new Bandage(player.x, player.y);

        // #endregion

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

            m.draw();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (onWindows)
            Shaders.drawZombieFOVCone();

        translate(-translateX, -translateY);
        UI.drawUI();
    }

    void step() {

        try {
            for (int i = 0; i < nearObjects.size(); i++) {
                GameObject gameObject = nearObjects.get(i);
                gameObject.step();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void clearLists() {
        allObjects.removeAll(toBeDelted);
        nearObjects.removeAll(toBeDelted);

        toBeDelted.clear();
    }

    void updateObjectLists() {
        if (NearThread.isReady) {
            nearObjects = NearThread.nearObjectsUpdated;
        }
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