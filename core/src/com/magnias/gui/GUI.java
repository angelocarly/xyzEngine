package com.magnias.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;



public class GUI
{
  private List<GUIElement> elements = new ArrayList<GUIElement>();


  
  private GUIElement selectedElement;



  
  public void render(SpriteBatch spriteBatch) {
    for (int i = 0; i < this.elements.size(); i++)
    {
      ((GUIElement)this.elements.get(i)).render(spriteBatch);
    }
  }


  
  public void input() {
    for (int i = 0; i < this.elements.size(); i++) {
      
      GUIElement element = this.elements.get(i);
      
      if (element.isAbove(Gdx.input.getX(), Gdx.input.getY()))
      
      { if (Gdx.input.isButtonPressed(0)) {
          
          element.setMouseState(GUIElement.MouseState.HELD);
        } else {
          element.setMouseState(GUIElement.MouseState.HOVER);
        }  }
      else { element.setMouseState(GUIElement.MouseState.IDLE); }
    
    } 
  }
  
  public List<GUIElement> getElements() {
    return this.elements;
  }

  
  public GUIElement getSelectedElement() {
    return this.selectedElement;
  }

  
  public void setSelectedElement(GUIElement element) {
    if (this.elements.contains(element)) {
      
      this.selectedElement = element;
    } else {
      throw new IllegalArgumentException("element is not part of window");
    } 
  }
  
  public void addElement(GUIElement element) {
    this.elements.add(element);
  }
}
