package com.magnias.game;



public abstract class GameState
{
  private String name;
  
  public GameState(String name) {
    this.name = name;
  }

  
  public abstract void init();

  
  public abstract void input(float paramFloat);

  
  public String getName() {
    return this.name;
  }
  
  public abstract void update(float paramFloat);
  
  public abstract void render();
  
  public abstract void dispose();
}
