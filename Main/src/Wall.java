public class Wall extends GameObject {
    int color = Main.main.color(255, 100, 100);

    public Wall(int x, int y) {
        super();
        this.x = (float)x;
        this.y = (float)y;
        w = 100f;
        h = 100f;
        classID = "Wall";
    }

    public Wall(int x, int y, int w, int h) {
        super();
        this.x = (float)x;
        this.y = (float)y;
        this.w = (float)w;
        this.h = (float)h;
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
