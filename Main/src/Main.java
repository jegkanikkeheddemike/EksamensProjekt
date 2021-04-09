import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.event.MouseEvent;

public class Main extends PApplet {
    public static boolean isRunning = true;

    public static Main main;
    public static volatile ArrayList<GameObject> allObjects = new ArrayList<GameObject>();
    public static volatile ArrayList<GameObject> nearObjects = new ArrayList<GameObject>();

    public static ArrayList<GameObject> toBeDelted = new ArrayList<GameObject>();
    public static Player player;

    public static int gameTime;

    public Main() {
        main = this;
    }

    @Override
    public void settings() {
        if (usesShaders) {
            size(1920, 1080, P2D);
            frameRate(60);
        } else
            size(1920, 1080);
    }

    @Override
    public void setup() {
        if (usesShaders)
            Shaders.loadShaders();
        NearThread.thread.start();
        if (usesShaders)
            ShaderPreRenderWorkThread.thread.start();

        // GroundItems.loadImages();
        Sound.setupSound();
        new Building(0, 0, 1900 - 100, 0, 0, 1100 - 100, 1900 - 100, 1100 - 100, new Random().nextInt(4));
        player = new Player();
        Random r = new Random();
        while (player.getCollisions(0, 0, new String[] {}).length > 0) {
            player.x = r.nextInt(1920);
            player.y = r.nextInt(1080);
        }

        new Bandage(400, 400);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (usesShaders)
            Shaders.drawZombieFOVCone();

        translate(-translateX, -translateY);
        UI.drawUI();
    }

    void step() {
        // BEGYNDER FORBEREDELSERNE TIL SHADERS, PIL IKKE VED!!!!
        if (usesShaders)
            ShaderPreRenderWorkThread.beginWork();
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

    public void mouseReleased() {
        mouseReleased = true;
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

    static boolean usesShaders = true;

    public static void main(String[] args) {
        /*
         * if (!System.getProperty("os.name").equals("Windows 10")) usesShaders = false;
         */

        PApplet.main("Main");
    }
}