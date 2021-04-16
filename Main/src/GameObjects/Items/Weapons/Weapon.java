package GameObjects.Items.Weapons;

import Setup.Main;
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

    public int reloadTime;

    Weapon(float x, float y, String wpnType, int reloadTime) {
        super(x, y);
        this.wpnType = wpnType;
        classID = "Weapon";
        //w = (float) sprite.width / 3;
        //h = (float) sprite.height / 3;
        itemType = "Weapon";
        this.reloadTime = reloadTime;
    }

    @Override
    public void reactPickedUp() {
        if (!Main.player.cWNumber) {
            Main.player.cWeapon0 = this;
        } else {
            Main.player.cWeapon1 = this;
        }

        delete();
    }
}
