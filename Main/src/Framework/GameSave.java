package Framework;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import GameObjects.Player;
import MapGeneration.Map;

public class GameSave implements java.io.Serializable{
    private static final long serialVersionUID = -2943159986346186078L;
    public ArrayList<GameObject> allObjects;
    public Player player;
    public Map m;
    public GameSave(ArrayList<GameObject> allObjects, Player player, Map m){
        this.allObjects = allObjects;
        this.player = player;
        this.m = m;
    }
    public void saveGame(String fileName){
        try{
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            System.out.println("GAME SAVED");
        }catch(Exception e){
        }
    }
    public static GameSave loadGame(String fileName){
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            GameSave gs = (GameSave) ois.readObject();
            ois.close();
            return gs;
        } catch (Exception e) {
            System.out.println("LOAD GAME FAILED");
            System.out.println(e);
            return null;
        }
    }
}
