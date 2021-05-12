package GameObjects.Items.Weapons;

public abstract class MeleeWeapon extends Weapon {

    MeleeWeapon(float x, float y, String wpnType, int reloadTime) {
        super(x, y, wpnType, reloadTime, false);
        shotCooldown = reloadTime;
    }

}
