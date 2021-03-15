package Setup;
import java.util.ArrayList;

import Framework.GameObject;
import Objects.Player;
import processing.core.PApplet;


public class Main extends PApplet {
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

    public void render(){
        clear();
        for (GameObject object: allObjects){// ændre til nearObjects
            object.draw();
        }

    }

    public void step(){

    }


    public static void main(String[] args) {
        PApplet.main("Setup.Main");
    }
} 