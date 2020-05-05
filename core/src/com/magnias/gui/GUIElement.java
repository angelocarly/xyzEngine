package com.magnias.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public abstract class GUIElement
{
  protected GUI gui;
  protected int x;
  protected int y;
  protected int width;
  protected int height;
  
  protected enum MouseState
  {
    HOVER, HELD, IDLE;
  }
  private MouseState mouseState = MouseState.IDLE;

  
  public GUIElement(GUI gui, int x, int y, int width, int height) {
    this.gui = gui;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  
  protected abstract void onHover();

  
  protected abstract void onHoverExit();

  
  protected abstract void onPress();
  
  public boolean isAbove(int x, int y) {
    return (Math.abs(Gdx.input.getX() - x - this.width / 2) < this.width / 2 && Math.abs(Gdx.input.getY() - y - this.height / 2) < this.height / 2);
  } protected abstract void onRelease();
  protected abstract void onKeyType(char paramChar);
  public abstract void render(SpriteBatch paramSpriteBatch);
  public void setMouseState(MouseState state) {
    if (this.mouseState == state)
      return; 
    this.mouseState = state;
    updateState(state);
  }
  
  protected abstract void updateState(MouseState paramMouseState);
}
