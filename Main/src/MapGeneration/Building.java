package MapGeneration;
import java.util.Random;

import GameObjects.Wall;
import GameObjects.Zombie;

public class Building {
    int topleftX, topleftY, toprightX, toprightY, botleftX, botleftY, botrightX, botrightY;
    int orientation;

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
        int deltaX = botrightX - botleftX;
        int deltaY = botrightY - toprightY;
        // #region outer walls
        if (orientation == EAST) {
            int doorLocation = 100 * r.nextInt(deltaY / 100);
            // DOOR WALLS
            new Wall(toprightX, toprightY, 100, doorLocation);
            new Wall(toprightX, toprightY + doorLocation + 100, 100, deltaY - doorLocation);
            // STANDARD WALLS
            new Wall(topleftX, topleftY, deltaX, 100);
            new Wall(topleftX, botleftY, deltaX, 100);
            new Wall(topleftX, topleftY, 100, deltaY + 100);

        } else if (orientation == WEST) {
            int doorLocation = 100 * r.nextInt((deltaY) / 100);
            // DOOR WALLS
            new Wall(topleftX, toprightY, 100, doorLocation);
            new Wall(topleftX, toprightY + doorLocation + 100, 100, deltaY - doorLocation);
            // STANDARD WALLS
            new Wall(topleftX, topleftY, deltaX, 100);
            new Wall(topleftX, botleftY, deltaX, 100);
            new Wall(toprightX, topleftY, 100, deltaY + 100);

        } else if (orientation == SOUTH) {
            int doorLocation = 100 * r.nextInt(deltaX / 100);
            // DOORWALLS
            new Wall(botleftX, botrightY, doorLocation, 100);
            new Wall(botleftX + doorLocation + 100, botrightY, deltaX - doorLocation, 100);
            // STANDARD WALLS
            new Wall(topleftX, topleftY, 100, deltaY);
            new Wall(toprightX, topleftY, 100, deltaY);
            new Wall(topleftX, topleftY, deltaX + 100, 100);
        } else if (orientation == NORTH) {

            int doorLocation = 100 * r.nextInt(deltaX / 100);
            // DOOR WALLS
            new Wall(botleftX, toprightY, doorLocation, 100);
            new Wall(botleftX + doorLocation + 100, toprightY, deltaX - doorLocation, 100);
            // STANDARD WALLS
            new Wall(topleftX, topleftY, 100, deltaY);
            new Wall(toprightX, topleftY, 100, deltaY);
            new Wall(topleftX, botleftY, deltaX + 100, 100);
        }

        // #endregion
        // #region Inner Walls
        int wallAmount = 5 + r.nextInt(((deltaX * deltaY) / 200000));
        for (int i = 0; i < wallAmount; i++) {
            int x = 200 + topleftX + 100 * r.nextInt((deltaX - 300) / 100);
            int y = 200 + topleftY + 100 * r.nextInt((deltaY - 300) / 100);
            boolean isVert = r.nextDouble() > 0.5;
            int w, h;
            if (isVert) {
                w = 100 + 100 * r.nextInt((deltaX / 2) / 100);
                h = 100;

                if (x + w > toprightX)
                    w = toprightX - x;
            } else {
                w = 100;
                h = 100 + 100 * r.nextInt((deltaY / 2) / 100);

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
