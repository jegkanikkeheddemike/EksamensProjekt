package GameObjects.Items.Weapons;

import Framework.Images;
import Framework.Sound;
import GameObjects.Projectiles.Bullet;
import Setup.Main;

public class Shotgun extends Weapon {
    int shotsPrShot = 5;

    public Shotgun(float x, float y) {
        super(x, y, "Shotgun", 120, true);
        wpnType = "Shotgun";
        damage = 6;
        shotCooldown = 60;
        range = 800;
        spread = 0.3f;
        clipSize = 2;
        cClip = clipSize;
        ammoType = "Shells";
    }

    public void use() {
        for (int i = 0; i < shotsPrShot; i++) {
            new Bullet(Main.player.rotation);
        }
        new Sound(Main.player.middleX(), Main.player.middleY(), 50, Sound.shotgunshots);
    }

    @Override
    protected void loadSprite() {
        sprite_ref = Images.SPRITE_SHOTGUN_REF;
    }

}
