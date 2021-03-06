package GameObjects.Items.Weapons;

import Framework.Images;
import GameObjects.Projectiles.MeleeAttack;
import Setup.Main;

public class Machete extends MeleeWeapon {

    public Machete(float x, float y) {
        super(x, y, "Machete", 60);
        this.range = 100;
        this.damage = 40;
        this.w = 50; // IGNORE IMAGE WIDTH & HEIGHT
        this.h = 50;
    }

    @Override
    protected void loadSprite() {
        sprite_ref = Images.SPRITE_MACHETE_REF;
    }

    @Override
    public void use() {
        new MeleeAttack(this, Main.player);
    }

}
