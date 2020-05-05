package com.magnias.world.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.magnias.game.Game;
import com.magnias.render.Shader;
import com.magnias.world.map.World;
import java.util.ArrayList;
import java.util.List;




public class EntityManager
{
  private World world;
  private List<Entity> entities = new ArrayList<Entity>();

  
  public EntityManager(World world) {
    this.world = world;
  }

  
  public void render(Shader shader, Camera camera) {
    shader.bindTexture("u_diffuse", (Texture)Game.assetManager.get("res/texture/entities.png"));
    for (Entity e : this.entities)
    {
      e.render(shader);
    }
  }

  
  public void update(float delta) {
    for (Entity e : this.entities)
    {
      e.update(delta);
    }
  }

  
  public void addEntity(Entity e) {
    this.entities.add(e);
  }
  
  public void dispose() {}
}
