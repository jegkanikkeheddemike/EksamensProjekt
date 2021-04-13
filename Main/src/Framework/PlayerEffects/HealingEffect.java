package Framework.PlayerEffects;

import GameObjects.Items.HealthItems.HealthItem;
import Setup.Main;

public class HealingEffect extends PlayerEffect {
    HealthItem healthItem;

    public HealingEffect(HealthItem healthItem) {
        super(healthItem.healTime, healthItem.itemType);
        this.healthItem = healthItem;
    }

    @Override
    void effect() {
        Main.player.canRun = false;
        Main.player.occupied = true;
    }

    @Override
    void endEffect() {
        Main.player.heal(healthItem.healing);
        Main.player.deleteFromInventory(healthItem);
    }

    @Override
    public void drawEffectSymbol(int x, int y) {
        // DRAW CROSS
        Main.main.noStroke();
        Main.main.fill(255, 200, 200);
        Main.main.rect(x + 170 + 10, y + 3, 5, 25);
        Main.main.rect(x + 170, y + 10 + 3, 25, 5);
    }

}
