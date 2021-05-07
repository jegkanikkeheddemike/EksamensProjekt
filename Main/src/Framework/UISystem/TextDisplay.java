package Framework.UISystem;

import Setup.Main;

public class TextDisplay extends UIElement {
    int textSize;
    int textMode = Main.main.LEFT;
    int textColor = Main.main.color(0);
    TextDisplay(String getName, String getDescription, int getX, int getY, int getSize, Window getOwner, int getTextMode, int getTextColor) {
      name = getName;
      description = getDescription;
      localX = getX;
      localY = getY;
      owner = getOwner;
      textSize = getSize;
      textMode = getTextMode;
      textColor = getTextColor;
      calcXY();
      type = "TextDisplay";
    }
    public TextDisplay(String getName, String getDescription, int getX, int getY, int getSize, Window getOwner, int getTextMode) {
      name = getName;
      description = getDescription;
      localX = getX;
      localY = getY;
      owner = getOwner;
      textSize = getSize;
      textMode = getTextMode;
      calcXY();
    }
    TextDisplay(String getName, String getDescription, int getX, int getY, int getSize, Window getOwner) {
      name = getName;
      description = getDescription;
      localX = getX;
      localY = getY;
      owner = getOwner;
      textSize = getSize;
      calcXY();
    }
    void drawElement() {
      if (isVisible) {
        Main.main.textAlign(textMode);
        Main.main.fill(textColor);
        Main.main.textSize(textSize);
        Main.main.text(description, x, y);
      }
    }
}