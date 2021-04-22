package MapGeneration;
import java.util.ArrayList;
import java.util.Random;

import GameObjects.Wall;
import GameObjects.Zombie;
import Framework.GameMath;
import Framework.GeneticAlgorithm.Group;
import Framework.GeneticAlgorithm.ZombieGenerator;

public class Building {
    int topleftX, topleftY, toprightX, toprightY, botleftX, botleftY, botrightX, botrightY;
    int orientation;
    private static final int wallWidth = 50;
    private static final int doorWidth = 75;
    ArrayList<Wall> interiorWalls;
    

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
        interiorWalls = new ArrayList<Wall>();
        build();
    }

    private void build() {
        //                  x, y,         w, h
        int[] doorSquare = {0, 0, doorWidth, doorWidth};
        Random r = new Random();
        int deltaX = Math.abs(botrightX - botleftX);
        int deltaY = Math.abs(botrightY - toprightY);
        // #region outer walls
        if (orientation == EAST) { //CHANGED
            int doorLocation = wallWidth + doorWidth * r.nextInt((deltaY-2*wallWidth) / doorWidth);

            // DOOR WALLS
            new Wall(toprightX-wallWidth, toprightY, wallWidth, doorLocation);
            new Wall(toprightX-wallWidth, toprightY + doorLocation + doorWidth, wallWidth, deltaY - doorLocation - doorWidth);

            doorSquare[0] = toprightX-wallWidth;
            doorSquare[1] = toprightY+doorLocation;
            // STANDARD WALLS
            new Wall(topleftX, topleftY, deltaX, wallWidth);
            new Wall(topleftX, botleftY-wallWidth, deltaX, wallWidth);
            new Wall(topleftX, topleftY, wallWidth, deltaY);
        } else if (orientation == WEST) { //CHANGED
            int doorLocation = wallWidth + doorWidth * r.nextInt((deltaY-2*wallWidth) / doorWidth);
            // DOOR WALLS
            new Wall(topleftX, toprightY, wallWidth, doorLocation);
            new Wall(topleftX, toprightY + doorLocation + doorWidth, wallWidth, deltaY - doorLocation - doorWidth);

            doorSquare[0] = topleftX;
            doorSquare[1] = toprightY+doorLocation;
            // STANDARD WALLS
            new Wall(topleftX, topleftY, deltaX, wallWidth);
            new Wall(topleftX, botleftY-wallWidth, deltaX, wallWidth);
            new Wall(toprightX-wallWidth, topleftY, wallWidth, deltaY);
        } else if (orientation == SOUTH) { //CHANGED
            int doorLocation = wallWidth + doorWidth * r.nextInt((deltaX-2*wallWidth) / doorWidth);
            // DOORWALLS
            new Wall(botleftX + wallWidth, botrightY - wallWidth, doorLocation, wallWidth);
            new Wall(botleftX + wallWidth + doorLocation + doorWidth, botrightY - wallWidth, deltaX - (wallWidth + doorLocation + doorWidth), wallWidth);

            doorSquare[0] = botleftX + wallWidth + doorLocation;
            doorSquare[1] = botrightY - wallWidth;
            // STANDARD WALLS
            new Wall(topleftX, topleftY, wallWidth, deltaY);
            new Wall(toprightX-wallWidth, topleftY, wallWidth, deltaY);
            new Wall(topleftX, topleftY, deltaX, wallWidth);
        } else if (orientation == NORTH) { //CHANGED
            int doorLocation = wallWidth + doorWidth * r.nextInt((deltaX-2*wallWidth) / doorWidth);
            // DOOR WALLS
            new Wall(botleftX + wallWidth, toprightY, doorLocation, wallWidth);
            new Wall(botleftX + wallWidth + doorLocation + doorWidth, toprightY, deltaX - (wallWidth + doorLocation + doorWidth), wallWidth);

            doorSquare[0] = botleftX + wallWidth + doorLocation;
            doorSquare[1] = toprightY;
            // STANDARD WALLS
            new Wall(topleftX, topleftY, wallWidth, deltaY);
            new Wall(toprightX-wallWidth, topleftY, wallWidth, deltaY);
            new Wall(topleftX, botleftY-wallWidth, deltaX, wallWidth);
        }
        // #endregion
        // #region Inner Walls

        int[] newSquare = {-1, -1, -1, -1};
        int wallAmount = 5 + r.nextInt(1+Math.abs(((deltaX-2*wallWidth) * (deltaY-2*wallWidth)) / 20000));
        for (int i = 0; i < wallAmount; i++) {
            int x = wallWidth + topleftX + doorWidth * r.nextInt((deltaX - 2*wallWidth) / doorWidth);
            newSquare[0] = x;
            int y = wallWidth + topleftY + doorWidth * r.nextInt((deltaY - 2*wallWidth) / doorWidth);
            newSquare[1] = y;
            boolean isVert = r.nextDouble() > 0.5;
            int w, h;
            if (isVert) {
                w = 2*wallWidth + wallWidth * r.nextInt((deltaX / 2) / wallWidth);
                h = wallWidth;

                if (x + w > toprightX)
                    w = toprightX - x;
                
                newSquare[2] = w;
                newSquare[3] = h;
            } else {
                w = wallWidth;
                h = 2*wallWidth + wallWidth * r.nextInt((deltaY / 2) / wallWidth);

                if (y + h > botleftY)
                    h = botleftY - y;
                newSquare[2] = w;
                newSquare[3] = h;
            }
            Boolean makeWall = true;
            //Firstly we make sure the area before the door is free
            //Secondly we make sure that none of the other walls are too close
            if(GameMath.rectDistance(doorSquare[0], doorSquare[1], doorSquare[0]+doorSquare[2], doorSquare[1]+doorSquare[3], newSquare[0], newSquare[1], newSquare[0]+newSquare[2], newSquare[1]+newSquare[3]) > doorWidth){
                for(Wall wall : interiorWalls){
                    //The wall should be made if none of the walls are too close, if one wall is too close it shouldn't be made.
                    if(wall.distToRect(newSquare[0], newSquare[1], newSquare[2], newSquare[3]) < doorWidth){
                        makeWall = false;
                        break;
                    }
                }
            }else{
                makeWall = false;
            }
            if(makeWall){
                interiorWalls.add(new Wall(x, y, w, h));
            }
        }

        // #endregion
        //makeRandomZombies(topleftX, topleftY, deltaX, deltaY);
        Group myZombies = ZombieGenerator.generateGeneration(deltaX*deltaY);
        myZombies.setCoordinates(topleftX, topleftY, deltaX, deltaY);
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
