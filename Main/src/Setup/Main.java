package Setup;

import processing.core.PApplet;

public class Main extends PApplet {
    public static Main main;

    public Main() {
        main = this;
    }

    @Override
    public void settings() {
        size(1920, 1080);
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {
        clear();
    }

    public static void main(String[] args) {
        PApplet.main(new Main().getClass());
    }
}