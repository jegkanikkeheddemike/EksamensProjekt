package GameObjects.Items.Weapons;

public abstract class MeeleWeapon extends Weapon {

    MeeleWeapon(float x, float y, String wpnType, int reloadTime) {
        super(x, y, wpnType, reloadTime, false);
        shotCooldown = reloadTime;
    }

}
