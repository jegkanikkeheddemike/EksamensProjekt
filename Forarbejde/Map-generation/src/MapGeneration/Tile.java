package src.MapGeneration;

import java.awt.Color;
import java.awt.Graphics;

import src.Setup.Main;
import src.Setup.Settings;

public class Tile {
    public int realX, realY;
    public int arrayX, arrayY;
    private Color color;
    public int type = TYPE_NULL;

    public Tile(int x, int y) {
        this.arrayX = x;
        this.arrayY = y;
        this.realX = -Settings.mapSize / 2 + x * Settings.tileSize;
        this.realY = -Settings.mapSize / 2 + y * Settings.tileSize;
    }

    public void checkType() {
        for (Road r : Main.roads) {
            // Check BUILDING
            // if horizontal
            if (r.orientation == 0 || r.orientation == 2) {
                if ((r.startX <= realX && r.slutX >= realX) || (r.slutX <= realX && r.startX >= realX)) {
                    // Check if building
                    if (realY <= r.startY + Settings.tileSize
                            && r.startY - Settings.tileSize < realY + Settings.tileSize)
                        type = TYPE_BUILDING;
                }
            }
            // if vertical
            if (r.orientation == 1 || r.orientation == 3) {
                if ((r.startY <= realY && r.slutY >= realY) || (r.slutY <= realY && r.startY >= realY)) {
                    // check if building
                    if (realX <= r.startX + Settings.tileSize
                            && r.startX - Settings.tileSize < realX + Settings.tileSize)
                        type = TYPE_BUILDING;
                }
            }
        }

        for (Road r : Main.roads) {
            // CHECK ROAD
            // if horizontal
            if (r.orientation == 0 || r.orientation == 2) {
                if ((r.startX <= realX && r.slutX >= realX) || (r.slutX <= realX && r.startX >= realX)) {
                    // Check if road
                    if (realY <= r.startY && r.startY < realY + Settings.tileSize)
                        type = TYPE_ROAD;
                }
            }
            // if vertical
            if (r.orientation == 1 || r.orientation == 3) {
                if ((r.startY <= realY && r.slutY >= realY) || (r.slutY <= realY && r.startY >= realY)) {
                    // check if road
                    if (realX <= r.startX && r.startX < realX + Settings.tileSize)
                        type = TYPE_ROAD;
                }
            }
        }

        // check if intersection
        for (RoadIntersection rI : Main.intersections) {
            if (realX <= rI.x && rI.x < realX + Settings.tileSize && realY <= rI.y && rI.y < realY + Settings.tileSize)
                type = TYPE_INTERSECTION;
        }

        // Check if building

        setColor();
    }

    public void draw(Graphics g) {
        if (type != TYPE_NULL) {
            g.setColor(color);
            g.fillRect(realX, realY, Settings.tileSize, Settings.tileSize);
        }
    }

    void setColor() {
        switch (type) {
            case TYPE_ROAD:
                color = Color.gray;
                break;
            case TYPE_INTERSECTION:
                color = Color.white;
                break;
            case TYPE_BUILDING:
                color = Color.blue;
                break;

        }
    }

    public static final int TYPE_NULL = 0;
    public static final int TYPE_ROAD = 1;
    public static final int TYPE_INTERSECTION = 2;
    public static final int TYPE_BUILDING = 3;
}
