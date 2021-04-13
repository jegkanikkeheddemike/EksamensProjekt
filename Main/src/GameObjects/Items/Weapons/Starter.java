package GameObjects.Items.Weapons;

import GameObjects.Bullet;
import Setup.Main;

public class Starter extends Weapon {
    public Starter(float x, float y) {
        super(x, y, "Starter", 60);
        this.wpnType = "Starter";
        damage = 5;
        shotCooldown = 30;
        range = 300;
        spread = 0.05f;
        held = true;
        clipSize = 5;
        cClip = clipSize;
        ammoType = ".45 ACP";
    }

    public void use() {
        new Bullet(Main.player.rotation);
    }
}