package GameObjects.Items.Weapons;

import GameObjects.Bullet;
import Setup.Main;

public class Pistol extends Weapon {
    public Pistol(float x, float y) {
        super(x, y, "Pistol", 120);
        wpnType = "Pistol";
        damage = 10;
        shotCooldown = 20;
        range = 700;
        spread = 0.02f;
        clipSize = 7;
        cClip = clipSize;
        ammoType = "9mm";
    }

    public void use() {
        new Bullet(Main.player.rotation);

    }
}
