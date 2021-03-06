package GameObjects;

import Framework.GameMath;
import Framework.GameObject;
import Setup.Main;

public class Wall extends GameObject {
    int color = Main.main.color(255, 100, 100);

    public Wall(int x, int y) {
        super(x, y, 100, 100);
        classID = "Wall";
    }

    public Wall(int x, int y, int w, int h) {
        super(x, y, w, h);
        classID = "Wall";
    }
    public Wall(int x1, int y1, int x2, int y2, int w) {
        super(0,0,0,0);
        //Vertical
        if(x1 == x2){
            this.x = Math.min(x1, x2) - w/2;
            this.y = Math.min(y1, y2);
            this.w = w;
            this.h = Math.abs(y2-y1);
        }//Horizontal
        else{
            this.x = Math.min(x1, x2);
            this.y = Math.min(y1, y2) - w/2;
            this.w = Math.abs(x1-x2);
            this.h = w;
        }
        classID = "Wall";
    }

    @Override
    public void draw() {
        Main.main.noStroke();
        Main.main.fill(color);
        Main.main.rect(x, y, w, h);
    }

    @Override
    public void step() {
    }

    public int shortestDistanceBetweenWalls(){
        return 0;

    }
    public float distToRect(float x, float y, float w, float h){
        return GameMath.rectDistance(this.x, this.y, this.x+this.w, this.y+this.h, x, y, x+w, y+h);
    }
}
