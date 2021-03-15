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
        background(255, 255, 0);
        fill(255, 0, 0);
        circle(width/2, height/2, 100);
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }
} 