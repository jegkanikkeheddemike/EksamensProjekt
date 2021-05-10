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
        // #region WEAPONS
        Main.main.textSize(20);
        Main.main.fill(255);
        if (!Main.player.cWNumber)
            Main.main.fill(0, 255, 0);
        Main.main.text(Main.player.cWeapon0.wpnType + " " + Main.player.cWeapon0.cClip + "/" + Main.player.cWeapon0.clipSize, 
            Main.main.width / 3f, Main.main.height - 110
        );
        Main.main.fill(255);
        if (Main.player.cWNumber)
            Main.main.fill(0, 255, 0);
        
        String text = Main.player.cWeapon1.wpnType + " " + Main.player.cWeapon1.cClip + "/" + Main.player.cWeapon1.clipSize;
        Main.main.text(text, 
        Main.main.width / 1.5f- Main.main.textWidth(text), Main.main.height - 110);

        // #endregion
        // #region INVENTORY

        float inventorySize = (Main.main.height*0.9f > 1000 ? 1000 : Main.main.height*0.9f);
        int boxSize = (int)(inventorySize / Main.player.inventory.length);

        Main.main.noFill();
        Main.main.stroke(150);
        Main.main.strokeWeight(3);
        Main.main.rect(Main.main.width -boxSize-20, 20, boxSize, inventorySize);

        for (int i = 0; i < 10; i++) {
            Main.main.noFill();
            Main.main.stroke(150);
            Main.main.strokeWeight(3);
            Main.main.line(Main.main.width - boxSize-20, 20 + boxSize * i, Main.main.width - 20, 20 + boxSize * i);
            if (Main.player.inventory[i] != null) {
                if (Main.player.inventory[i].sprite_ref != null) {
                    Main.player.inventory[i].drawInInventory(Main.main.width - boxSize-20, 20 + boxSize * i,boxSize);
                } else {
                    Main.main.text("No sprite", Main.main.width - boxSize-20 + 20, 20 + boxSize * i + 10);
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

        if (killedFirst  && !hasShowedM){
            Main.windows.successScreen.getElement("SuccessMessage").description = "You've killed your first enemy\nPress \"m\" to pause and save";
            Main.windows.successScreen.show();
            hasShowedM = true;
        }
    }

    public static boolean killedFirst = false;
    public static boolean hasShowedM = false;
}
