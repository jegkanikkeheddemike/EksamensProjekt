package GameObjects.Items.HealthItems;

public class HealthPack extends HealthItem {

    public HealthPack(float x, float y) {
        super(x, y,120);
        itemType = "HealthPack";
        healing = 60;
    }

}
