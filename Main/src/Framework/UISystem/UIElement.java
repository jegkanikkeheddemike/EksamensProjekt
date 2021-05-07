package Framework.UISystem;

import java.util.ArrayList;

import Setup.Main;
import processing.core.PGraphics;

public class UIElement {
    String name;
    String description;
    String type = "";
    int localX;
    int localY;
    int sizeX;
    int sizeY;
    int x;
    int y;
  
    String info;  //Kan bruges til at holde data der kun skal bruges i specifikke tilfælde.
  
    Window owner;
    boolean isActive = false;
    boolean isVisible = true; //SKAL IMPLEMENTERS I DET UNIKKE OBJEKTS drawElement() !!
	public ArrayList<UIElement> elements; //KUN NØDVENDIG FOR LISTEN MEN JA :)
    boolean within(float low, float middle, float high) {
        return(low < middle && middle < high);
    }

    void step() {
      if (!isVisible) {
        return;
      }
      if (clickedOn()) {
        isActive = true;
        reactClickedOn();
      }
      if (clickedOff()) {
        isActive = false;
      }
      if (isActive) {
        stepActive();
      }
      if (isActive && Main.keyTapped(10)) {
        reactEnter();
      }
      stepAlways();
    }
    void drawElement() {
      Main.main.fill(255, 150, 150);
      Main.main.rect(x, y, sizeX, sizeY);
      Main.main.textSize(sizeY - 1);
      Main.main.fill(0);
      Main.main.text("NO TEXTURE", x, y + sizeY);
    }
    void drawElementInList(PGraphics window) {
      window.fill(255, 150, 150);
      window.rect(x, y, sizeX, sizeY);
      window.textSize(sizeY - 1);
      window.fill(0);
      window.text("NO TEXTURE", x, y + sizeY);
    }
    boolean clickedOn() {
      if (Main.mouseReleased) {
        if (within(x, Main.main.mouseX, x + sizeX)) {
          if (within(y, Main.main.mouseY, y + sizeY)) {
            return true;
          }
        }
      }
      return false;
    }
    boolean clickedOff() {
      if (Main.mouseReleased) {
        if (within(x, Main.main.mouseX, x + sizeX)) {
          if (within(y, Main.main.mouseY, y + sizeY)) {
            return false;
          }
        }
        return true;
      }
  
      return false;
    }
    boolean mouseOn() {
      if (within(x, Main.main.mouseX, x + sizeX)) {
        if (within(y, Main.main.mouseY, y + sizeY)) {
          return true;
        }
      }
      return false;
    }
    void reactEnter() {
    }
    public void reactClickedOn() {
    }
    void stepActive() {
    }
    void stepAlways() {  //Used only for multiple choice so far
    }
    String getOutput() {
      System.out.println(name + " HAS GIVEN NO OUTPUT AS " + type);
      return "NO OUTPUT GIVEN";
    }


    void deleteMe() {
      owner.removeList.add(this);
    }
    void customInput() {
    }
  
    //TextBox
    void clearText() {
    }
    void makeVisible() {
      isVisible = true;
    }
    void makeInvisible() {
      isVisible = false;
    }
    void calcXY() {
      x = owner.x + 
        localX;
      y = owner.y + 
        localY;
    }
  }