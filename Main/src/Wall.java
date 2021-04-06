public class Wall extends GameObject {
    int color = Main.main.color(255, 100, 100);

    public Wall(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        w = 100;
        h = 100;
        classID = "Wall";
    }

    public Wall(int x, int y, int w, int h) {
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        classID = "Wall";
    }
    public Wall(int x1, int y1, int x2, int y2, int w) {
        super();
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
}
