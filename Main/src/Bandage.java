public class Bandage extends Item {
    public Bandage(float x, float y) {
        super(x, y);
        itemType = "Bandage";
    }

    @Override
    public void reactPickedUp() {
        int index = Main.player.getEmptyInventorySpace();
        if (index != -1) {
            Main.player.inventory[index] = this;
            held = true;
            delete();
        }
    }

    @Override
    public void use() {
        Main.player.heal(30);
        deleteFromInventory();
    }
}
