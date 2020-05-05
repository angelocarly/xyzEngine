package com.magnias.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.magnias.game.Game;



public class GUIButton
  extends GUIElement
{
  public GUIButton(GUI gui, int x, int y, int width, int height) {
    super(gui, x, y, width, height);
  }


  
  public void render(SpriteBatch batch) {
    batch.draw((Texture)Game.assetManager.get("res/texture/terrain.png"), this.x, this.y, this.width, this.height);
  }
  
  protected void updateState(GUIElement.MouseState state) {}
  
  protected void onPress() {}
  
  protected void onRelease() {}
  
  protected void onKeyType(char character) {}
  
  protected void onHover() {}
  
  protected void onHoverExit() {}
}
