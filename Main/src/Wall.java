public class Wall extends GameObject {
    int color = Main.main.color(255, 100, 100);

    public Wall() {
        super();
        w = 100;
        h = 100;
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
