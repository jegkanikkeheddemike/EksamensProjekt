import java.awt.Color;

import com.thor.tapplet.TApplet;

class Main extends TApplet {
    public static void main(String[] args) {
        new Main().init(1920, 1080, "Mapgen 2");
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void draw() {
        double transX = -camX + WIDTH / 2f / scale;
        double transY = -camY + HEIGHT / 2f / scale;
        g.scale(scale, scale);
        g.translate(transX, transY);

        g.setColor(Color.BLUE);
        g.fillRect(500, 500, 200, 200);

    }

    @Override
    public void setup() {

    }

    float camX, camY, scale = 1;

    @Override
    public void tick() {

        // camera
        scale -= mouse.scroll / 200;
        if (scale < Settings.minScale) {
            scale = Settings.minScale;
        } else if (scale > Settings.maxScale) {
            scale = Settings.maxScale;
        }

        if (keyboard.keyDown('w')) {
            camY -= Settings.camSpeed * 1 / scale;
        }
        if (keyboard.keyDown('s')) {
            camY += Settings.camSpeed * 1 / scale;
        }
        if (keyboard.keyDown('d')) {
            camX += Settings.camSpeed * 1 / scale;
        }
        if (keyboard.keyDown('a')) {
            camX -= Settings.camSpeed * 1 / scale;
        }
    }

}