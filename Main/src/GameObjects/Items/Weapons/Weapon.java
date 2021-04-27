package GameObjects.Items.Weapons;

import Setup.Main;
import Framework.Images;
import GameObjects.Items.*;

public abstract class Weapon extends Item {
    public String wpnType;
    public float damage;
    public int shotCooldown;
    public int cooldown;
    public float range;
    public float spread;
    public int clipSize;
    public int cClip;
    public String ammoType;
    public boolean usesAmmo;

    public int reloadTime;

    Weapon(float x, float y, String wpnType, int reloadTime, boolean usesAmmo) {
        super(x, y);
        this.wpnType = wpnType;
        classID = "Weapon";
        w = (float) Images.getSprite(sprite_ref).width / 3;
        h = (float) Images.getSprite(sprite_ref).height / 3;
        itemType = "Weapon";
        this.reloadTime = reloadTime;
        this.usesAmmo = usesAmmo;
    }

    @Override
    public void reactPickedUp() {
        if (!Main.player.cWNumber) {
            Main.player.cWeapon0.held = false;
            Main.player.cWeapon0 = this;
        } else {
            Main.player.cWeapon1.held = false;
            Main.player.cWeapon1 = this;
        }
        held = true;
    }
}
