package GameObjects.Items.HealthItems;

import Framework.Images;

public class HealthPack extends HealthItem {

    public HealthPack(float x, float y) {
        super(x, y,120);
        itemType = "HealthPack";
        healing = 60;
    }

    @Override
    protected void loadSprite() {
        sprite = Images.SPRITE_HEALTHPACK;
        
    }

}
