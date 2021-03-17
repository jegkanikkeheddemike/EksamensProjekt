public class Bullet extends GameObject {
    float damage = 10;
    float range = 1000;
    float length;
    static int lifeSpan = 10;
    int timeAlive = 0;
    float xEnd;
    float yEnd;
    GameObject collidedWith;

    Bullet(float rotation) {
        super();
        classID = "Bullet";

        rotation = -rotation + (float) Math.PI / 2f;
        x = Main.player.middleX();
        y = Main.player.middleY();
        xEnd = Main.player.middleX() + (float) Math.sin(rotation) * range;
        yEnd = Main.player.middleY() + (float) Math.cos(rotation) * range;
        checkCollide();

    }

    @Override
    public void draw() {
        Main.main.stroke(255, 0, 0, 255f - 255f * ((float) timeAlive / (float) lifeSpan));
        Main.main.line(x, y, xEnd, yEnd);
    }

    @Override
    public void step() {
        if (timeAlive >= lifeSpan)
            delete();
        timeAlive++;

    }

    void checkCollide() {
        LineData data = LineData.noCollision;

        for (int i = 0; i < Main.nearObjects.size(); i++) {
            GameObject g = Main.nearObjects.get(i);
            if (g.classID != "Wall")
                continue;

            LineData newData = GameMath.lineRect(x, y, xEnd, yEnd, g.x, g.y, g.w, g.h);
            if (newData.collision) {
                if (data.equals(LineData.noCollision) || newData.length < data.length) {
                    data = newData;
                    collidedWith = g;
                }
            }
        }
        if (data.collision) {
            xEnd = data.x;
            yEnd = data.y;
        }
    }
}
