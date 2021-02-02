import java.awt.*;

import com.thor.tapplet.TApplet;

class Main extends TApplet {
    public static int width = 1500;
    public static int height = 1000;
    //Node initial = new Node(width/2, height/2);
    //Node latest = initial;
    public Map mainMap = new Map(3);
    float camX = width / 2;
    float camY = height / 2;
    float scale = 1;
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new Main().init(width, height, "Mapgen 2");
        
    }

    @Override
    public void draw() {
        double transX = -camX + WIDTH / 2f / scale;
        double transY = -camY + HEIGHT / 2f / scale;
        g.scale(scale, scale);
        g.translate(transX, transY);


        //g.setColor(Color.BLUE);
        //g.fillRect(500, 500, 200, 200);
        mainMap.draw(g);
    }

    @Override
    public void setup() {
        mainMap.generateMap();
    }

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

        if (keyboard.keyPressed('n')) {
            //Node n = new Node(latest.x, latest.y+100, latest);
            //latest.addNode(n);
            //latest = n;
        }
    }

}