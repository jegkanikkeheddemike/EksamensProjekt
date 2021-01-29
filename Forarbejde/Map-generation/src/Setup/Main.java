package src.Setup;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import com.thor.tapplet.TApplet;
import src.MapGeneration.*;

public class Main extends TApplet {
    private static final long serialVersionUID = 1L;
    public static ArrayList<Road> roads = new ArrayList<Road>();
    public static ArrayList<RoadIntersection> intersections = new ArrayList<RoadIntersection>();
    public static float camX, camY;
    public static float scale = 1;

    public static void main(String[] args) {
        new Main().init(1920, 1080, "mapGen");
    }

    @Override
    public void setup() {
        Settings.mapSize = Settings.tileAmount * Settings.tileSize;
        Mapgenerator.generateMap();
    }

    @Override
    public void draw() {
        double transX = -camX + WIDTH / 2f / scale;
        double transY = -camY + HEIGHT / 2f / scale;
        g.scale(scale, scale);
        g.translate(transX, transY);
        g.setColor(Color.WHITE);
        g.fillRect(0 - 10, 0 - 10, 20, 20);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        if (!Mapgenerator.tilesCreated) {
            for (Road v : roads) {
                v.drawVej(g);
            }
            g.setColor(Color.red);
            for (RoadIntersection rI : intersections) {
                g.fillOval(rI.x - 5, rI.y - 5, 10, 10);
            }
        }

        Mapgenerator.drawMap(g);

        g.setColor(Color.white);
        g.drawRect(-Settings.mapSize / 2, -Settings.mapSize / 2, Settings.mapSize, Settings.mapSize);

        // REVERSE TRANSLATION TO DRAW UI
        g.translate(-transX, -transY);
        g.scale(1 / scale, 1 / scale);
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g.drawString("Use WASD to move", 10, 50);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.drawString("Scroll to zoom", 10, 90);
        if (!Settings.createAllAtStart)
            g.drawString("roads: " + Main.roads.size() + " / " + Settings.roadAmount, 10, 130);
        else
            g.drawString("roads: " + Settings.roadAmount, 10, 130);
    }

    
    @Override
    public void tick() {

        if (!Settings.createAllAtStart) {
            Mapgenerator.tickGenerate();
        }

        // camera
        scale -= mouse.scroll / 200;
        if (scale < Settings.minScale) {
            scale = Settings.minScale;
        } else if (scale > Settings.maxScale) {
            scale = Settings.maxScale;
        }

        if (keyboard.keyDown('w')) {
            camY -= Settings.camSpeed * 1 / scale;
        }
        if (keyboard.keyDown('s')) {
            camY += Settings.camSpeed * 1 / scale;
        }
        if (keyboard.keyDown('d')) {
            camX += Settings.camSpeed * 1 / scale;
        }
        if (keyboard.keyDown('a')) {
            camX -= Settings.camSpeed * 1 / scale;
        }
    }

}