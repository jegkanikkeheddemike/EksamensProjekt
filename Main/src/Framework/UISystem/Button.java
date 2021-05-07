package Framework.UISystem;

import Setup.Main;
import processing.core.PGraphics;

public class Button extends UIElement {
    int textAlign = Main.main.CENTER;
    public Button(String getName, String getDescription, int getX, int getY, int getSizeX, int getSizeY, Window getOwner) {
      name = getName;
      description = getDescription;
      localX = getX;
      localY = getY;
      sizeX = getSizeX;
      sizeY = getSizeY;
      owner = getOwner;
      calcXY();
      type = "Button";
    }
    public Button(String getName, String getDescription, int getX, int getY, int getSizeX, int getSizeY, Window getOwner, int getTextAlign) {
      name = getName;
      description = getDescription;
      localX = getX;
      localY = getY;
      sizeX = getSizeX;
      sizeY = getSizeY;
      owner = getOwner;
      this.textAlign = getTextAlign;
      calcXY();
      type = "Button";
    }
    void drawElement() {
      if (isVisible) {
  
        Main.main.textAlign(textAlign);
        Main.main.fill(255);
        if (mouseOn() || isActive) {
            Main.main.fill(200, 200, 255);
        }
        Main.main.textSize((float) (sizeY * 0.8));
        Main.main.rect(x, y, sizeX, sizeY);
        Main.main.fill(0);
        Main.main.text(description, x + (sizeX / 2), (float) (y + (sizeY * 0.8)));
      }
    }
    void drawElementInList(PGraphics g) {
      if (isVisible) {
        int xOffset = 0;
        if (textAlign == 37) {
          xOffset = -sizeX/2 + 5;
        }
        g.textAlign(textAlign);
        g.fill(255);
        if (mouseOn() || isActive) {
          g.fill(200, 200, 255);
        }
        g.textSize((float) (sizeY * 0.8));
        g.rect(localX, localY, sizeX, sizeY);
        g.fill(0);
        g.text(description, localX + (sizeX / 2)+xOffset, (float) (localY + (sizeY * 0.8)));
      }
    }
    void reactEnter() {
      reactClickedOn();
    }
  }