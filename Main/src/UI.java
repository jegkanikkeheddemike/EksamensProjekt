public class UI {
    public static void drawUI() {
        // HEALTHBAR
        Main.main.stroke(0);
        Main.main.strokeWeight(10);
        if(!Main.player.cWNumber)
            Main.main.fill(0,255,0);
        Main.main.text(Main.player.cWeapon0.wpnType,Main.main.width / 3f, Main.main.height - 105);
        Main.main.fill(255);
        if(Main.player.cWNumber)
            Main.main.fill(0,255,0);
        Main.main.text(Main.player.cWeapon1.wpnType,Main.main.width / 3f+400, Main.main.height - 105);
        Main.main.noFill();
        Main.main.rect(Main.main.width / 3f, Main.main.height - 100, Main.main.width / 3f, 50);
        Main.main.noStroke();
        Main.main.fill(255 - 255 * (Main.player.health / 100f), 255 * (Main.player.health / 100f), 0);
        Main.main.rect(Main.main.width / 3f, Main.main.height - 100,
                (Main.player.health / 100f) * (Main.main.width / 3f), 50);
    }
}
