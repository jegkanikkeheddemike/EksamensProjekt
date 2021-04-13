package GameObjects.Items.Weapons;

import GameObjects.Bullet;
import Setup.Main;

public class Starter extends Weapon {
    public Starter(float x, float y) {
        super(x, y, "Starter");
        wpnType = "Starter";
        damage = 8;
        shotCooldown = 30;
        range = 300;
        spread = 0.05f;
        clipSize = 5;
        cClip = clipSize;
        ammoType = ".45 ACP";
    }

    public void use() {
        new Bullet(Main.player.rotation);
    }
}