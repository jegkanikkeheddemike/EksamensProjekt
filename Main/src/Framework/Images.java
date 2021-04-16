package Framework;

import Setup.Main;
import processing.core.PImage;

public abstract class Images {

    public static void loadImages() {
        SPRITE_PISTOL = Main.main.loadImage("Images/Pistol.png");
        SPRITE_STARTER = Main.main.loadImage("Images/Starter.png");
        SPRITE_HEALTHPACK = Main.main.loadImage("Images/Healthpack.png");
        SPRITE_BANDAGE = Main.main.loadImage("Images/Bandage.png");
        SPRITE_AMMOBOX9MM = Main.main.loadImage("Images/AmmoBox9mm.png");
        SPRITE_AMMOBOX45ACP = Main.main.loadImage("Images/AmmoBox45ACP.png");
        SPRITE_MACHETE = Main.main.loadImage("Images/Machete.png");
    }

    public static PImage SPRITE_PISTOL;
    public static PImage SPRITE_STARTER;
    public static PImage SPRITE_HEALTHPACK;
    public static PImage SPRITE_BANDAGE;
    public static PImage SPRITE_AMMOBOX9MM;
    public static PImage SPRITE_AMMOBOX45ACP;
    public static PImage SPRITE_MACHETE;
}
