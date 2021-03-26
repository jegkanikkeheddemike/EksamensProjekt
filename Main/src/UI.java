public class UI {
    public static void drawUI() {
        // #region HEALTHBAR
        Main.main.stroke(0);
        Main.main.strokeWeight(10);
        Main.main.noFill();
        Main.main.rect(Main.main.width / 3f, Main.main.height - 100, Main.main.width / 3f, 50);
        Main.main.noStroke();
        Main.main.fill(255 - 255 * (Main.player.health / 100f), 255 * (Main.player.health / 100f), 0);
        Main.main.rect(Main.main.width / 3f, Main.main.height - 100,
                (Main.player.health / 100f) * (Main.main.width / 3f), 50);
        // #endregion
        // #region WEAPONS
        Main.main.fill(255);
        if (!Main.player.cWNumber)
            Main.main.fill(0, 255, 0);
        Main.main.text(Main.player.cWeapon0.wpnType, Main.main.width / 3f, Main.main.height - 105);
        Main.main.fill(255);
        if (Main.player.cWNumber)
            Main.main.fill(0, 255, 0);
        Main.main.text(Main.player.cWeapon1.wpnType, Main.main.width / 3f + 400, Main.main.height - 105);

        // #endregion
        // #region INVENTORY
        Main.main.noFill();
        Main.main.stroke(150);
        Main.main.strokeWeight(3);
        Main.main.rect(Main.main.width - 120, 20, 100, 1000);

        for (int i = 0; i < 10; i++) {
            Main.main.noFill();
            Main.main.stroke(150);
            Main.main.strokeWeight(3);
            Main.main.line(Main.main.width - 120, 20 + 100 * i, Main.main.width - 20, 20 + 100 * i);
            if (Main.player.inventory[i] != null) {
                if (Main.player.inventory[i].sprite == null) {
                    Main.player.inventory[i].drawInInventory(Main.main.width - 120 + 10, 20 + 100 * i + 10);
                } else {
                    Main.main.image(Main.player.inventory[i].sprite, Main.main.width - 120, 20 + 100 * i, 80, 80);
                }
            }
        }
        // #endregion
    }
}
