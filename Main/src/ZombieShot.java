public class ZombieShot extends Movables {
    float dmg;
    Zombie parent;
    float speed = 10;

    public ZombieShot(float x, float y, float rotation, float dmg, Zombie parent) {
        super();
        this.x = x;
        this.y = y;
        this.rotation = -rotation + (float) Math.PI / 2f;
        this.dmg = dmg;
        this.parent = parent;
    }

    @Override
    public void step() {
        GameObject[] collision = getCollisions(0, 0, new String[] {});
        for (GameObject gameObject : collision) {
            if (gameObject.classID == "Wall")
                delete();
            else if (gameObject != parent) {
                gameObject.reactGetHit(dmg, "ZRanged");
                delete();
            }
        }
        xSpeed = speed * (float) Math.sin(rotation);
        ySpeed = speed * (float) Math.cos(rotation);

        x += xSpeed;
        y += ySpeed;
    }

    @Override
    public void draw() {
        Main.main.fill(255);
        Main.main.noStroke();
        Main.main.circle(x, y, 10);
    }

}
