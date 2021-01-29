package src.MapGeneration;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics;

import src.Setup.*;

public class Mapgenerator {
    public static boolean roadsCreated = false;
    public static boolean tilesCreated = false;
    public static TileMap tileMap;

    public static void generateMap() {
        tileMap = new TileMap(Settings.tileAmount);
        int centerX = 0;
        int centerY = 0;
        Main.roads.add(new Road(centerX, centerY, 0, Settings.startRoadLength)); // east
        Main.roads.add(new Road(centerX, centerY, 1, Settings.startRoadLength)); // south
        Main.roads.add(new Road(centerX, centerY, 2, Settings.startRoadLength)); // west
        Main.roads.add(new Road(centerX, centerY, 3, Settings.startRoadLength)); // north
        if (Settings.createAllAtStart) {
            while (Main.roads.size() < Settings.roadAmount) {
                updateRoads();
            }
            roadsCreated = true;
            System.out.println("Made " + Main.roads.size() + " roads");
            while (!tilesCreated) {
                tileMap.update();
            }
        }
    }

    public static boolean roadCreatedThisTick = false;

    public static void tickGenerate() { // Called if !Settings.createAllAtStart
        
        if (!roadsCreated) {
            roadCreatedThisTick = false;
            while (!roadCreatedThisTick) {
                updateRoads();
            }
            if (Main.roads.size() == Settings.roadAmount && !Mapgenerator.roadsCreated) {
                roadsCreated = true;
                System.out.println("Made " + Main.roads.size() + " roads");

            }
        } else {
            if (!tilesCreated) {
                for (int i = 0; i < Settings.tileAmount / 4; i++)
                    tileMap.update();
            }
        }
    }

    public static void drawMap(Graphics g) {
        for (int x = 0; x < Settings.tileAmount; x++) {
            for (int y = 0; y < Settings.tileAmount; y++) {
                if (tileMap.map[x][y] != null)
                    tileMap.map[x][y].draw(g);
            }
        }
    }

    public static void updateRoads() {
        // Sandsynlighed for vej er baseret på længde, forbindelser og afstand til start
        ArrayList<Road> pool = new ArrayList<Road>();
        for (Road v : Main.roads) {
            for (int i = 0; i < v.length * Settings.lengthMultiplier + v.connected.size() * Settings.connectedMultiplier
                    - GMath.pointDist(v.startX, v.startY, 0, 0) / Settings.distanceFromSpawnMultiplier; i += 50) {
                pool.add(v);
            }
        }

        Road update1 = pool.get(new Random().nextInt(pool.size()));
        update1.makeRoad();

        for (Road v : Main.roads) {
            v.updateConnections();
        }
    }
}
