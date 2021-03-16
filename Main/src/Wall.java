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

    @Override
    public void draw() {
        Main.main.fill(color);
        Main.main.rect(x, y, w, h);
    }

    @Override
    public void step() {
    }
}