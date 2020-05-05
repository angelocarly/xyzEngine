package com.magnias.game;

import java.util.HashMap;








public class GameStateManager
{
  private HashMap<String, GameState> gameStates = new HashMap<String, GameState>();
  
  private GameState currentGameState;
  
  public void cycle(float delta) {
    this.currentGameState.input(delta);
    this.currentGameState.update(delta);
    this.currentGameState.render();
  }

  
  public void setCurrentState(String gameState) {
    if (this.currentGameState != null) this.currentGameState.dispose(); 
    this.currentGameState = this.gameStates.get(gameState);
    this.currentGameState.init();
  }

  
  public void addGameState(GameState gameState) {
    this.gameStates.put(gameState.getName(), gameState);
  }

  
  public void dispose() {
    for (GameState g : this.gameStates.values())
    {
      g.dispose();
    }
  }
}
