package src.Setup;

public class Settings {
    // Road properties
    public static final int minRoadLength = 50;
    public static final int maxRoadLength = 300;
    public static final int startRoadLength = 500;
    public static final int minIntersectionDist = 60;
    public static final int roadAmount = 250;
    public static final int parallelMinDist = 30;
    public static final int parallelCheckDist = 200;

    // Choose road properties
    public static final int lengthMultiplier = 2;
    public static final int connectedMultiplier = 20;
    public static final int distanceFromSpawnMultiplier = 30;

    // Tile properties
    public static final int tileSize = 10;
    public static final int tileAmount = 200;
    public static int mapSize; // Gets calculated in setup

    // Setup
    public static final boolean createAllAtStart = false;

    // Camera
    public static final float camSpeed = 5;
    public static final float minScale = 0.1f;
    public static final float maxScale = 20f;
}
