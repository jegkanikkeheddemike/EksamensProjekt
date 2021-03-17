public class Zombie extends Movables {

    public Zombie(float x, float y) {
        super();
        classID = "Zombie";
        this.x = x;
        this.y = y;
        w = 40;
        h = 40;
    }

    @Override
    public void draw() {

        Main.main.noStroke();
        Main.main.fill(50, 255, 50);
        Main.main.pushMatrix();
        Main.main.translate(middleX(), middleY());
        Main.main.rotate(rotation);
        Main.main.rect(0 - w / 2, -h / 2, w, h);
        Main.main.stroke(255, 0, 0);
        Main.main.strokeWeight(5);
        Main.main.line(0, 0, 35, 0);
        Main.main.popMatrix();

        Main.main.text(playerAwareness, middleX(), y);
    }

    @Override
    public void step() {
        lookForPlayer();
    }

    float playerAwareness;
    static final float fov = 120;

    void lookForPlayer() {
        playerAwareness -= 0.5;

        LineData lineToPlayer = GameMath.lineCollision(middleX(), middleY(), Main.player.middleX(),
                Main.player.middleY(), new String[] { "Zombie", "Player" });

        if (!lineToPlayer.collision)
            playerAwareness += 30 / Math.sqrt(GameMath.objectDistance(this, Main.player));

        if (playerAwareness < 0)
            playerAwareness = 0;
        if (playerAwareness > 100)
            playerAwareness = 100;
    }

}
