package Framework;

import java.util.Hashtable;

import Setup.Main;
import processing.core.PImage;

public abstract class Images {

    public static void loadImages() {
        refImageDict = new Hashtable<String, PImage>();
        
        SPRITE_PISTOL = Main.main.loadImage("Images/Pistol.png");
        SPRITE_PISTOL_REF = "Pistol";
        refImageDict.put(SPRITE_PISTOL_REF, SPRITE_PISTOL);

        SPRITE_SHOTGUN = Main.main.loadImage("Images/Shotgun.png");
        SPRITE_SHOTGUN_REF = "Shotgun";
        refImageDict.put(SPRITE_SHOTGUN_REF, SPRITE_SHOTGUN);


        SPRITE_STARTER = Main.main.loadImage("Images/Starter.png");
        SPRITE_STARTER_REF = "Starter";
        refImageDict.put(SPRITE_STARTER_REF, SPRITE_STARTER);

        SPRITE_HEALTHPACK = Main.main.loadImage("Images/Healthpack.png");
        SPRITE_HEALTHPACK_REF = "Healthpack";
        refImageDict.put(SPRITE_HEALTHPACK_REF, SPRITE_HEALTHPACK);

        SPRITE_BANDAGE = Main.main.loadImage("Images/Bandage.png");
        SPRITE_BANDAGE_REF = "Bandage";
        refImageDict.put(SPRITE_BANDAGE_REF, SPRITE_BANDAGE);

        SPRITE_AMMOBOX9MM = Main.main.loadImage("Images/AmmoBox9mm.png");
        SPRITE_AMMOBOX9MM_REF = "AmmoBox9mm";
        refImageDict.put(SPRITE_AMMOBOX9MM_REF, SPRITE_AMMOBOX9MM);

        SPRITE_AMMOBOX45ACP = Main.main.loadImage("Images/AmmoBox45ACP.png");
        SPRITE_AMMOBOX45ACP_REF = "AmmoBox45ACP";
        refImageDict.put(SPRITE_AMMOBOX45ACP_REF, SPRITE_AMMOBOX45ACP);

        SPRITE_MACHETE = Main.main.loadImage("Images/Machete.png");
        SPRITE_MACHETE_REF = "Machete";
        refImageDict.put(SPRITE_MACHETE_REF, SPRITE_MACHETE);

        SPRITE_AMMOBOXSHELLS = Main.main.loadImage("Images/AmmoBoxShells.png");
        SPRITE_AMMOBOXSHELLS_REF = "AmmoBoxShells";
        refImageDict.put(SPRITE_AMMOBOXSHELLS_REF, SPRITE_AMMOBOXSHELLS);
        
    }
    
    public static PImage getSprite(String sprite_ref){
        return refImageDict.get(sprite_ref);
    }
    
    private static Hashtable<String, PImage> refImageDict;
    public static String SPRITE_PISTOL_REF;
    public static String SPRITE_SHOTGUN_REF;
    public static String SPRITE_STARTER_REF;
    public static String SPRITE_HEALTHPACK_REF;
    public static String SPRITE_BANDAGE_REF;
    public static String SPRITE_AMMOBOX9MM_REF;
    public static String SPRITE_AMMOBOX45ACP_REF;
    public static String SPRITE_MACHETE_REF;
    public static String SPRITE_AMMOBOXSHELLS_REF;

    public static PImage SPRITE_PISTOL;
    public static PImage SPRITE_SHOTGUN;
    public static PImage SPRITE_STARTER;
    public static PImage SPRITE_HEALTHPACK;
    public static PImage SPRITE_BANDAGE;
    public static PImage SPRITE_AMMOBOX9MM;
    public static PImage SPRITE_AMMOBOX45ACP;
    public static PImage SPRITE_MACHETE;
    public static PImage SPRITE_AMMOBOXSHELLS;

    
}
