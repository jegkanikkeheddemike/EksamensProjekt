package src.MapGeneration;

import src.Setup.Settings;

public class TileMap {
    Tile[][] map;
    int x, y;
    int status = 1; // 1 = check type

    TileMap(int size) {
        map = new Tile[size][size];
        // initialize tiles
        for (int xx = 0; xx < Settings.tileAmount; xx++) {
            for (int yy = 0; yy < Settings.tileAmount; yy++) {
                map[xx][yy] = new Tile(xx, yy);
            }
        }
    }

    public void update() {
        if (status == 1) {
            map[x][y].checkType();
            if (x != Settings.tileAmount - 1) {
                x++;
            } else if (y == Settings.tileAmount - 1) {
                status++;
                System.out.println("tile typecheck done");
                Mapgenerator.tilesCreated = true;
            } else {
                x = 0;
                y++;
            }
        }
    }
}
