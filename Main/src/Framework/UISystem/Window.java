package Framework.UISystem;

import java.util.ArrayList;

import Setup.Main;

public class Window {
    String name;
    String description;
  
    int x;
    int y;
    int sizeX;
    int sizeY;
    
    boolean hasBackdrop = true;
    int backdropColor = Main.main.color(150);
    boolean hasOutline = false;
    int outlineColor = Main.main.color(255);
    int outlineWeight = 2;
  
    public boolean isActive = true;
  
    public ArrayList<UIElement> elements = new ArrayList<UIElement>();
    ArrayList<UIElement> removeList = new ArrayList<UIElement>();
    
    ArrayList<String> interacterable = new ArrayList<String>();
    int multiChoiceIndex = -1;
  
    public Window(int getX, int getY, int getSizeX, int getSizeY, String getName) {
      x = getX;
      y = getY;
      sizeX = getSizeX;
      sizeY = getSizeY;
      name = getName;
      interacterable.add("TextBox");
      interacterable.add("Button");
      interacterable.add("ScreenButton");
      interacterable.add("Choice");
    }
  
    public UIElement getElement(String elementName) {
      for (UIElement element : elements) {
        if (element.name.equals(elementName)) {
          return element;
        }
      }
      return null;
    }
    void removeElement(String elementName) {
      for (int i = 0; i < elements.size(); i ++) {
        UIElement e = elements.get(i);
        if (e.name.equals(elementName)) {
          elements.remove(e);
          break;
        }
      }
    }
    public void stepWindow() {
      if(isActive){
        if (Main.keyTapped(9)) { //SWITCHING BETWEEN ACTIVE ELEMENTS USING SHIFT.
        int direc = 1;
        if (Main.keyDown(-1)) {
          direc = -1;
        }
          for (int i = 0; i < elements.size(); i ++) {
            UIElement e = elements.get(i);
            if (e.isActive) {
              e.isActive = false;
              int nI = i+direc;
              if (nI == elements.size()) {
                  nI = 0;
                } else if (nI < 0) {
                  nI = elements.size()-1;
              }
              UIElement n = elements.get(nI);
              while (!interacterable.contains(n.type)) {
                nI+=direc;
                if (nI == elements.size()) {
                  nI = 0;
                } else if (nI == 0) {
                nI = elements.size()-1;
                }
                n = elements.get(nI);
              }
              n.isActive = true;
              
              break;
            }
          }
        }
        for (int i = 0; i < elements.size();i ++) {
          UIElement element = elements.get(i);
          if (!element.type.equals("Choice")) {
            element.step();
          }
        }
        for (UIElement element : removeList) {
          elements.remove(element);
        }
      }
    }
    
    public void drawWindow() {
      if(isActive){
        if (hasBackdrop) {
          Main.main.fill(backdropColor);
          if (hasOutline) {
            Main.main.stroke(outlineColor);
            Main.main.strokeWeight(outlineWeight);
          } else {
            Main.main.noStroke();
          }
  
          Main.main.rect(x, y, sizeX, sizeY);
        }
        for (UIElement element : elements) {
          if (!element.type.equals("Choice")){
          element.drawElement();}
        }
      }
    }
  }  