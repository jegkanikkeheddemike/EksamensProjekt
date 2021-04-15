package GameObjects.Items.HealthItems;

public class Bandage extends HealthItem {
    public Bandage(float x, float y) {
        super(x, y, 30);
        itemType = "Bandage";
        healing = 30;
    }
}
