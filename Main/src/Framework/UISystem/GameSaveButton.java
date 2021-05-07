package Framework.UISystem;

import java.io.File;

import Setup.Main;

//Only in list
public class GameSaveButton extends Button{
    public GameSaveButton(String getName, String getDescription, int getSizeX, int getSizeY, Window getOwner) {
        super(getName, getDescription, 0, 0, getSizeX, getSizeY, getOwner);
    }

    @Override
    public void reactClickedOn(){
        System.out.println("THIS IS THE GAME SAVE THAT WE WILL TRY TO DOWNLOAD NOW FROM THE FILESERVER :)");
        System.out.println(name);
        //Download the gamesave in question and start the game from it :)
        Main.dbi.downloadFromBucket("eksamensprojektddu", name);
        //Right now the name and description will both just be the filename
        Main.startGameFromGameSave(name);

        File f = new File(name);
        f.delete();

        Main.windows.savesScreen.isActive = false;
    }
    
}
