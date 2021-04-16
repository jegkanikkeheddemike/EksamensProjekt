package GameObjects.Items.Weapons;

import Framework.Images;
import GameObjects.Projectiles.Bullet;
import Setup.Main;

public class Pistol extends Weapon {
    public Pistol(float x, float y) {
        super(x, y, "Pistol", 120,true);
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

    @Override
    protected void loadSprite() {
        Main.println(1);
        sprite = Images.SPRITE_PISTOL;

    }
}
