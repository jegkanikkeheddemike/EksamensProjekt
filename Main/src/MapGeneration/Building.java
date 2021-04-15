package MapGeneration;
import java.util.Random;

import GameObjects.Wall;
import GameObjects.Zombie;

public class Building {
    int topleftX, topleftY, toprightX, toprightY, botleftX, botleftY, botrightX, botrightY;
    int orientation;
    private static final int wallWidth = 50;
    private static final int doorWidth = 75;
    

    public Building(int topleftX, int topleftY, int toprightX, int toprightY, int botleftX, int botleftY, int botrightX,
            int botrightY, int orientation) {
        this.orientation = orientation;
        this.topleftX = topleftX;
        this.topleftY = topleftY;
        this.toprightX = toprightX;
        this.toprightY = toprightY;
        this.botleftX = botleftX;
        this.botleftY = botleftY;
        this.botrightX = botrightX;
        this.botrightY = botrightY;

        build();
    }

    private void build() {
        Random r = new Random();
        int deltaX = Math.abs(botrightX - botleftX);
        int deltaY = Math.abs(botrightY - toprightY);
        // #region outer walls
        if (orientation == EAST) { //CHANGED
            int doorLocation = wallWidth + doorWidth * r.nextInt((deltaY-2*wallWidth) / doorWidth);
            // DOOR WALLS
            new Wall(toprightX-wallWidth, toprightY, wallWidth, doorLocation);
            new Wall(toprightX-wallWidth, toprightY + doorLocation + doorWidth, wallWidth, deltaY - doorLocation - doorWidth);
            // STANDARD WALLS
            new Wall(topleftX, topleftY, deltaX, wallWidth);
            new Wall(topleftX, botleftY-wallWidth, deltaX, wallWidth);
            new Wall(topleftX, topleftY, wallWidth, deltaY);
        } else if (orientation == WEST) { //CHANGED
            int doorLocation = wallWidth + doorWidth * r.nextInt((deltaY-2*wallWidth) / doorWidth);
            // DOOR WALLS
            new Wall(topleftX, toprightY, wallWidth, doorLocation);
            new Wall(topleftX, toprightY + doorLocation + doorWidth, wallWidth, deltaY - doorLocation - doorWidth);
            // STANDARD WALLS
            new Wall(topleftX, topleftY, deltaX, wallWidth);
            new Wall(topleftX, botleftY-wallWidth, deltaX, wallWidth);
            new Wall(toprightX-wallWidth, topleftY, wallWidth, deltaY);
        } else if (orientation == SOUTH) { //CHANGED
            int doorLocation = wallWidth + doorWidth * r.nextInt((deltaX-2*wallWidth) / doorWidth);
            // DOORWALLS
            new Wall(botleftX + wallWidth, botrightY - wallWidth, doorLocation, wallWidth);
            new Wall(botleftX + wallWidth + doorLocation + doorWidth, botrightY - wallWidth, deltaX - (wallWidth + doorLocation + doorWidth), wallWidth);
            // STANDARD WALLS
            new Wall(topleftX, topleftY, wallWidth, deltaY);
            new Wall(toprightX-wallWidth, topleftY, wallWidth, deltaY);
            new Wall(topleftX, topleftY, deltaX, wallWidth);
        } else if (orientation == NORTH) { //CHANGED
            int doorLocation = wallWidth + doorWidth * r.nextInt((deltaX-2*wallWidth) / doorWidth);
            // DOOR WALLS
            new Wall(botleftX + wallWidth, toprightY, doorLocation, wallWidth);
            new Wall(botleftX + wallWidth + doorLocation + doorWidth, toprightY, deltaX - (wallWidth + doorLocation + doorWidth), wallWidth);
            // STANDARD WALLS
            new Wall(topleftX, topleftY, wallWidth, deltaY);
            new Wall(toprightX-wallWidth, topleftY, wallWidth, deltaY);
            new Wall(topleftX, botleftY-wallWidth, deltaX, wallWidth);
        }

        // #endregion
        // #region Inner Walls

        //First mission:
        //  Free area before door :)
        int wallAmount = 5 + r.nextInt(1+Math.abs(((deltaX-2*wallWidth) * (deltaY-2*wallWidth)) / 200000));
        for (int i = 0; i < wallAmount; i++) {
            //MAKE SURE THE PLAYER CAN GET BY?
            int x = wallWidth + doorWidth + topleftX + doorWidth * r.nextInt((deltaX - 3*doorWidth) / doorWidth);
            int y = wallWidth + doorWidth + topleftY + doorWidth * r.nextInt((deltaY - 3*doorWidth) / doorWidth);
            boolean isVert = r.nextDouble() > 0.5;
            int w, h;
            if (isVert) {
                w = wallWidth + wallWidth * r.nextInt((deltaX / 2) / wallWidth);
                h = wallWidth;

                if (x + w > toprightX)
                    w = toprightX - x;
            } else {
                w = wallWidth;
                h = wallWidth + wallWidth * r.nextInt((deltaY / 2) / wallWidth);

                if (y + h > botleftY)
                    h = botleftY - y;
            }
            new Wall(x, y, w, h);
        }

        // #endregion
        makeRandomZombies(topleftX, topleftY, deltaX, deltaY);
    }

    void makeRandomZombies(int topleftX, int topleftY, int width, int height) {
        Random r = new Random();
        int amount = 2+r.nextInt(5);
        for (int i = 0; i < amount; i++) {
            Zombie zombie = new Zombie(topleftX + r.nextInt(width), topleftY + r.nextInt(height), Zombie.randomGenes());
            while (zombie.getCollisions(0, 0, new String[] {"Wall","Player","Item"}).length > 0) {
                zombie.x = topleftX + r.nextInt(width);
                zombie.y = topleftY + r.nextInt(height);
            }
        }
    }

    public static final int EAST = Map.EAST;
    public static final int SOUTH = Map.SOUTH;
    public static final int WEST = Map.WEST;
    public static final int NORTH = Map.NORTH;
}
