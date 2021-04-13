package GameObjects.Items.HealthItems;

import Framework.PlayerEffects.HealingEffect;
import GameObjects.Items.Item;
import Setup.Main;

public abstract class HealthItem extends Item {
    public int healing;
    public int healTime;

    protected HealthItem(float x, float y, int healTime) {
        super(x, y);
        this.healTime = healTime;
    }

    @Override
    public void use() {
        if (!Main.player.occupied)
            Main.player.addPlayerEffect(new HealingEffect(this));
    }
}
