package Framework;

import Framework.PlayerEffects.PlayerEffect;
import Setup.Main;

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
        // #region KOORDINATES
        Main.main.textSize(12);
        Main.main.fill(255);
        Main.main.text("x:" + (int) Main.player.x + " y:" + (int) Main.player.y, Main.main.width / 2f,
                Main.main.height - 105);
        // #endregion
        // #region WEAPONS
        Main.main.fill(255);
        if (!Main.player.cWNumber)
            Main.main.fill(0, 255, 0);
        Main.main.text(Main.player.cWeapon0.wpnType, Main.main.width / 3f, Main.main.height - 105);
        Main.main.text(Main.player.cWeapon0.cClip + "/" + Main.player.cWeapon0.clipSize, Main.main.width / 3f,
                Main.main.height - 115);
        Main.main.text(Main.player.cWeapon0.ID, Main.main.width / 3f, Main.main.height - 125);
        Main.main.fill(255);
        if (Main.player.cWNumber)
            Main.main.fill(0, 255, 0);
        Main.main.text(Main.player.cWeapon1.wpnType, Main.main.width / 3f + 500, Main.main.height - 105);
        Main.main.text(Main.player.cWeapon1.cClip + "/" + Main.player.cWeapon1.clipSize, Main.main.width / 3f + 500,
                Main.main.height - 115);
        Main.main.text(Main.player.cWeapon1.ID, Main.main.width / 3f + 500, Main.main.height - 125);

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
        // #region PLAYER EFFECTS

        int x = 10;
        int y = 10;
        for (PlayerEffect playerEffect : Main.player.playerEffects) {

            playerEffect.drawOnUI(x, y);

            y += 80;
        }

        // #endregion

        // #region SHADER INFO

        if (!Main.onWindows) {
            Main.main.strokeWeight(3);
            Main.main.stroke(0);
            Main.main.fill(255, 0, 0);
            Main.main.textSize(30);
            Main.main.text("SHADERS ARE DISABLED, USE WINDOWS 10 TO ENABLE SHADERS", 850, 40);
        }

        // #endregion
        // #region GAME INFO
        Main.main.fill(255);
        Main.main.textSize(12);
        Main.main.text("Near Objects " + Main.nearObjects.size(), 10, 20);
        Main.main.text("All Objects " + Main.allObjects.size(), 10, 40);
        Main.main.text("Near Zombies " + Shaders.zombies.size(), 10, 60);
        Main.main.text("Near Walls " + Shaders.walls.size(), 10, 80);
        // #endregion
    }
}
