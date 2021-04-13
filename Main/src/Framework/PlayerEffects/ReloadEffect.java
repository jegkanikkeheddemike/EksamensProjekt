package Framework.PlayerEffects;

import GameObjects.Items.Weapons.Weapon;
import Setup.Main;

public class ReloadEffect extends PlayerEffect {

    public ReloadEffect(Weapon weapon) {
        super(weapon.reloadTime, "Reloading: " + weapon.wpnType);
    }

    @Override
    void effect() {
        Main.player.occupied = true;
        Main.player.canRun = false;

    }

    @Override
    void endEffect() {
        Main.player.reload();

    }

    @Override
    protected void drawEffectSymbol(int x, int y) {
        // TODO Auto-generated method stub

    }

}
