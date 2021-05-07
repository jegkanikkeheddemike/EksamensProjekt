package Framework.UISystem;

import Setup.Main;
import processing.core.PGraphics;

public class TextBox extends UIElement {
    String text = "";
    int cursorIndex = 0;
  
    public TextBox(String getName, String getDescription, int getX, int getY, int getSizeX, int getSizeY, Window getOwner) {
      name = getName;
      description = getDescription;
      localX = getX;
      localY = getY;
      sizeX = getSizeX;
      sizeY = getSizeY;
      owner = getOwner;
      calcXY();
      type = "TextBox";
    }
    void stepActive() {
      if (isVisible) {
        for (Integer tappedKey : Main.tappedKeys) {
  
  
          if (tappedKey == Main.BACKSPACE && text.substring(0, cursorIndex).length() > 0) {
            text = text.substring(0, cursorIndex - 1) + text.substring(cursorIndex);
            cursorIndex -= 1;
          } else if (tappedKey == -2 && cursorIndex > 0) {  //LEFT ARROW KEY
            cursorIndex -= 1;
          } else if (tappedKey == -3 && cursorIndex < text.length()) {  //RIGHT ARROW KEY
            cursorIndex += 1;
            //}
          } else if ((tappedKey >= 48 && tappedKey <= 57) || (tappedKey >= 65 && tappedKey <= 122) || (tappedKey >= 32 && tappedKey <= 63) || (tappedKey >=197 && tappedKey <=248)) {
            text = text.substring(0, cursorIndex) + ((char) ((int) tappedKey)) + text.substring(cursorIndex);
            cursorIndex += 1;
          }
        }
      }
    }
    void clearText() {
      text = "";
      cursorIndex = 0;
    }
  
    void drawElement() {
      if (isVisible) {
        Main.main.textAlign(Main.LEFT);
        Main.main.stroke(0);
        Main.main.strokeWeight(sizeY / 10);
        if (isActive) {
            Main.main.fill(200, 200, 255);
        } else {
            Main.main.fill(255);
        }
        //WE HAVE TO "SCROLL" THE TEXT WHEN IT IS LONGER THAN THE ACTUAL TEXTBOX!
        //The textbox
        Main.main.rect(x, owner.y + localY, sizeX, sizeY);
        Main.main.fill(0);
        Main.main.textSize((float) (sizeY * 0.8));
        Main.main.text(text, x + 3, (float) (y + sizeY * 0.8));
  
        //The cursor
  
        if (isActive) {
            Main.main.fill(255, 0, 0);
            Main.main.strokeWeight(4);
            float textBeforeCursor = Main.main.textWidth(text.substring(0, cursorIndex));
            Main.main.line(x + textBeforeCursor, owner.y + localY, x + textBeforeCursor, owner.y + localY + sizeY);
        }
  
        //The description
        Main.main.fill(0);
        Main.main.text(description, x + 1, y - 4);
      }
    }
  
    void drawElementInList(PGraphics window) {
      if (isVisible) {
        window.textAlign(Main.LEFT);
        window.stroke(0);
        window.strokeWeight(sizeY / 10);
        if (isActive) {
          window.fill(200, 200, 255);
        } else {
          window.fill(255);
        }
        //WE HAVE TO "SCROLL" THE TEXT WHEN IT IS LONGER THAN THE ACTUAL TEXTBOX!
        //The textbox
        window.rect(localX, localY, sizeX, sizeY);
        window.fill(0);
        window.textSize((float) (sizeY * 0.8));
        window.text(text, localX + 3, (float) (localY + sizeY * 0.8));
  
        //The cursor
  
        if (isActive) {
          window.fill(255, 0, 0);
          window.strokeWeight(4);
          float textBeforeCursor = window.textWidth(text.substring(0, cursorIndex));
          window.line(localX + textBeforeCursor, localY, localX + textBeforeCursor, localY + sizeY);
        }
  
        //The description
        window.fill(0);
        window.text(description, localX + 1, localX - 4);
      }
    }
  
    String getOutput() {
      if (text == "") {
        super.getOutput();
      }
      return text;
    }
  }