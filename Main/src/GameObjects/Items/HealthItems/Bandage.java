package GameObjects.Items.HealthItems;

import Framework.Images;

public class Bandage extends HealthItem {
    public Bandage(float x, float y) {
        super(x, y, 30);
        itemType = "Bandage";
        healing = 30;
    }

    @Override
    protected void loadSprite() {
        sprite = Images.SPRITE_BANDAGE;
    }
}
