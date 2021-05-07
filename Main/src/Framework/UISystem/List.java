package Framework.UISystem;

import java.util.ArrayList;

import Setup.Main;
import processing.core.PGraphics;

public class List extends UIElement {
    PGraphics listRender;
    float scroll = 0;
    float maxScroll = 0;  //KOMMER ALDRIG TIL AT VÆRE 0!!
    public ArrayList<UIElement> elements = new ArrayList<UIElement>();
    public List(String getName, String getDescription, int getX, int getY, int getSizeX, int getSizeY, Window getOwner) {
      listRender = Main.main.createGraphics(getSizeX, getOwner.sizeY - 20);
      name = getName;
      description = getDescription;
      localX = getX;
      localY = getY;
      sizeX = getSizeX;
      sizeY = getSizeY;
      owner = getOwner;
      calcXY();
      type = "List";
    }
  
    void stepAlways() {
      if (isVisible) {
        for (UIElement element : elements) {
          if(element.y > y && element.y < y+sizeY){
            element.step();
          }
        }
        if (mouseOn()) {
          scroll += Main.scrollAmount;
          if (scroll > 0) {
            scroll = 0;
          }
          if (scroll < maxScroll) {
            scroll = maxScroll;
          }
        }
      }
    }
  
    void drawElement() {
      if (isVisible) {
        PGraphics windowRender = Main.main.createGraphics(sizeX, sizeY-10);
        Main.main.fill(0);
        Main.main.textSize(40);
        Main.main.text(description, x, y-1);
        int yy = 10+(int) scroll;
        int height = 0;
  
        windowRender.beginDraw();
        windowRender.background(0, 0, 0, 100);
        for (UIElement i : elements) {
          i.x = x + 10;
          i.y = y + yy;
          i.localX = 10;
          i.localY = yy;
          i.sizeX = sizeX - 20;
          i.drawElementInList(windowRender);
          yy += i.sizeY + 10;
          height += i.sizeY+10;
        }
        if (height > sizeY) {
          maxScroll = -(height-sizeY)-20;
        }
        windowRender.endDraw();
        Main.main.image(windowRender, x, y);
      }
    }
    void makeVisible() {
      isVisible = true;
      for (UIElement element : elements) {
        element.makeVisible();
      }
    }
    void makeInvisible() {
      isVisible = false;
      for (UIElement element : elements) {
        element.makeInvisible();
      }
    }
  }