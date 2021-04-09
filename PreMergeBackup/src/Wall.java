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
