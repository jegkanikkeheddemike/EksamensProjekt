package src.MapGeneration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import src.Setup.*;

public class Road {
    int startX, startY, slutX, slutY;
    ArrayList<Road> connected = new ArrayList<Road>();
    int orientation; // 0 = east, 1 = south, 2 = west, 3 = north
    int length;
    int id;

    Road(int startX, int startY, int orientation, int length) {
        this.startX = startX;
        this.startY = startY;
        this.orientation = orientation;
        this.length = length;
        this.id = Main.roads.size();
        switch (orientation) {
            case 0:
                slutX = startX + length;
                slutY = startY;
                break;
            case 1:
                slutX = startX;
                slutY = startY + length;
                break;
            case 2:
                slutX = startX - length;
                slutY = startY;
                break;
            case 3:
                slutX = startX;
                slutY = startY - length;
                break;
        }
    }

    public void drawVej(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawLine(startX, startY, slutX, slutY);
        g.drawString(id + "", slutX, slutY);
        g.setColor(Color.red);
    }

    public void makeRoad() {
        int tries = 0;
        boolean canBePlacedHere = false;
        int newX = 0, newY = 0, newSX = 0, newSY = 0;
        int where = new Random().nextInt(length);
        int newLength = Settings.minRoadLength + new Random().nextInt(Settings.maxRoadLength - Settings.minRoadLength);
        boolean side = new Random().nextBoolean();

        int newOrientation = orientation;
        if (side) {
            newOrientation += 1;
            if (newOrientation == 4) {
                newOrientation = 0;
            }
        } else {
            newOrientation -= 1;
            if (newOrientation == -1) {
                newOrientation = 3;
            }
        }
        while (!canBePlacedHere && tries < 10) {
            where = new Random().nextInt(length);
            switch (orientation) { // get StartPoint
                case 0:
                    newX = startX + where;
                    newY = startY;
                    break;
                case 1:
                    newX = startX;
                    newY = startY + where;
                    break;
                case 2:
                    newX = startX - where;
                    newY = startY;
                    break;
                case 3:
                    newX = startX;
                    newY = startY - where;
                    break;
            }
            switch (newOrientation) { // get endPoint
                case 0:
                    newSX = newX + newLength;
                    newSY = newY;
                    break;
                case 1:
                    newSX = newX;
                    newSY = newY + newLength;
                    break;
                case 2:
                    newSX = newX - newLength;
                    newSY = newY;
                    break;
                case 3:
                    newSX = newX;
                    newSY = newY - newLength;
                    break;
            }

            canBePlacedHere = true;

            // Check if outside map
            if (newSX > Settings.mapSize / 2 || newSX < -Settings.mapSize / 2 || newSY > Settings.mapSize / 2
                    || newSY < -Settings.mapSize / 2) {
                canBePlacedHere = false;
                break;
            }

            // Check new intersections
            ArrayList<RoadIntersection> newIntersections = new ArrayList<RoadIntersection>();
            for (Road v : Main.roads) {
                if (v == this)
                    continue;

                RoadIntersection check = roadCollision(newX, newY, newSX, newSY, v.startX, v.startY, v.slutX, v.slutY);
                if (check != null) {
                    newIntersections.add(check);
                }
            }

            for (RoadIntersection rI : Main.intersections) {
                float pDist = GMath.pointDist(newX, newY, rI.x, rI.y); // Check existing intersections
                if (pDist < Settings.minIntersectionDist) {
                    canBePlacedHere = false;
                    break;
                }

                for (RoadIntersection newRI : newIntersections) {// Check new intersections with existing intersections
                    float newPDist = GMath.pointDist(newRI.x, newRI.y, rI.x, rI.y);
                    if (newPDist < Settings.minIntersectionDist) {
                        canBePlacedHere = false;
                        break; // break inner loop
                    }
                }
                if (!canBePlacedHere) { // break outer loop
                    break;
                }
            }
            for (RoadIntersection rI : newIntersections) { // Check new intersections with new intersections
                for (RoadIntersection newRI : newIntersections) {
                    if (rI == newRI)
                        continue;

                    float newPDist = GMath.pointDist(newRI.x, newRI.y, rI.x, rI.y);
                    if (newPDist < Settings.minIntersectionDist) {
                        canBePlacedHere = false;
                        break; // break inner loop
                    }
                }
                if (!canBePlacedHere) { // break outer loop
                    break;
                }
            }

            for (Road r : Main.roads) {
                if (r == this)
                    continue;

                // check if parallel
                if (orientation == r.orientation || orientation + 2 == r.orientation
                        || orientation - 2 == r.orientation) {
                    // Check if nearby
                    if (GMath.pointDist(newX, newY, r.startX, r.startY) > Settings.parallelCheckDist
                            && GMath.pointDist(newSX, newSY, r.startX, r.startY) > Settings.parallelCheckDist
                            && GMath.pointDist(newX, newY, r.slutX, r.slutY) > Settings.parallelCheckDist
                            && GMath.pointDist(newSX, newSY, r.slutX, r.slutY) > Settings.parallelCheckDist)
                        continue;

                    // if horizontal
                    if (orientation == 0 || orientation == 2) {
                        float dy = Math.abs(newSY - r.startY);
                        if (dy < Settings.parallelMinDist) {
                            canBePlacedHere = false;
                            break;
                        }
                    }

                    // if vertical
                    if (orientation == 1 || orientation == 3) {
                        float dx = Math.abs(newSX - r.startX);
                        if (dx < Settings.parallelMinDist) {
                            canBePlacedHere = false;
                            break;
                        }
                    }
                }
            }
            tries++;
        }
        if (canBePlacedHere) {
            Road newVej = new Road(newX, newY, newOrientation, newLength);
            Main.roads.add(newVej);
            Mapgenerator.roadCreatedThisTick = true;
        }
    }

    void updateConnections() {
        for (Road v : Main.roads) {
            if (v == this) {
                continue;
            }
            RoadIntersection rI = roadCollision(startX, startY, slutX, slutY, v.startX, v.startY, v.slutX, v.slutY);
            if (rI != null) {
                if (!connected.contains(v)) {
                    connected.add(v);
                    boolean iExists = false; // Check if intersection exists
                    for (RoadIntersection rI2 : Main.intersections) {
                        if (rI2.x == rI.x && rI2.y == rI.y) {
                            iExists = true;
                            break;
                        }
                    }
                    if (!iExists) {
                        Main.intersections.add(rI);
                    }
                }
            }
        }
    }

    RoadIntersection roadCollision(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        // calculate the direction of the lines
        float den = ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
        if (den == 0.) {
            return null;
        }

        float uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / den;
        float uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / den;
        // if uA and uB are between 0-1, lines are colliding
        if (uA >= 0. && uA <= 1. && uB >= 0. && uB <= 1.) {
            int intersectionX = (int) (x1 + (uA * (x2 - x1)));
            int intersectionY = (int) (y1 + (uA * (y2 - y1)));
            // System.out.println(intersectionX + " " + intersectionY);
            return new RoadIntersection(intersectionX, intersectionY);
        }
        return null;
    }

}
