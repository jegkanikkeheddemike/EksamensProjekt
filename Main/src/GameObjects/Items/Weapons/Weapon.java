package GameObjects.Items.Weapons;

import Setup.Main;
import GameObjects.*;
import GameObjects.Items.*;

public class Weapon extends Item {
    public String wpnType;
    public float damage;
    public int shotCooldown;
    public int cooldown;
    public float range;
    public float spread;
    public int clipSize;
    public int cClip;
    public String ammoType;

    Weapon(float x, float y, String wpnType) {
        super(x, y);
        this.wpnType = wpnType;
        classID = "Weapon";
        this.sprite = Main.main.loadImage("Data/Images/" + this.wpnType + ".png");
        this.w = (float) sprite.width / 3;
        this.h = (float) sprite.height / 3;
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



