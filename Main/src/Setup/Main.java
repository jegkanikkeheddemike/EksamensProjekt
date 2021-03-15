package Setup;

import java.util.ArrayList;

import Framework.GameObject;
import Objects.Player;
import processing.core.PApplet;
import processing.event.MouseEvent;


public class Main extends PApplet {
    public static boolean isRunning = true;
    public static Main main;
    public static ArrayList<GameObject> allObjects = new ArrayList<GameObject>();
    public static ArrayList<GameObject> nearObjects = new ArrayList<GameObject>();
    public static Player player = new Player();

    public Main() {
        main = this;
    }

    @Override
    public void settings() {
        size(1920, 1080);
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {
        render();
        step();
    }

    void render(){
        clear();
        for (GameObject object:allObjects){ // Change to nearObjects
            object.draw();
        }
    }

    void step(){
        for (GameObject object:allObjects){ // Change to nearObjects
            object.step();
        }
    }

    ArrayList<Integer> downKeys = new ArrayList<Integer>();
    ArrayList<Integer> tappedKeys = new ArrayList<Integer>();
    ArrayList<Integer> ignoredChar = new ArrayList<Integer>();
    boolean mouseReleased = false;
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
            }
        } else {
            k = (int) key;
        }
        if (downKeys.contains(k)) {
            downKeys.remove(downKeys.indexOf(k));
        }
    }

    public void mouseWheel(MouseEvent e) {
        scrollAmount = -8 * e.getCount();
    }

    public boolean keyDown(char input) {
        if (downKeys.contains(input)) {
            return true;
        }
        return false;
    }

    public boolean keyDown(int input) {
        if (downKeys.contains(input)) {
            return true;
        }
        return false;
    }

    public boolean keyTapped(char input) {
        if (tappedKeys.contains(input)) {
            return true;
        }
        return false;
    }

    public boolean keyTapped(int input) {
        if (tappedKeys.contains(input)) {
            return true;
        }
        return false;
    }

    public void mouseReleased() {
        mouseReleased = true;
    }

    void cleanKeyboard() {
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

    public static void main(String[] args) {
        PApplet.main("Setup.Main");
    }
}