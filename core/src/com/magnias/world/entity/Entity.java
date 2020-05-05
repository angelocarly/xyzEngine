package com.magnias.world.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.magnias.game.Game;
import com.magnias.render.Shader;
import com.magnias.world.map.World;


public abstract class Entity
{
  protected static Texture entityTexture = (Texture)Game.assetManager.get("res/texture/entities.png");
  
  private World world;
  
  protected btRigidBody body;
  
  protected boolean onGround;
  
  protected float size = 0.5F;

  
  public Entity(World world, Vector3 position) {
    this.world = world;
  }


  
  public abstract void render(Shader paramShader);

  
  public void update(float deltaTime) {}

  
  public void move(Vector2 deltaDir) {
    move(new Vector3(deltaDir.x, 0.0F, deltaDir.y));
  }

  
  public void move(Vector3 deltaDir) {
    this.body.applyCentralForce(deltaDir.cpy().scl(100.0F));
  }

  
  public void jump() {
    this.body.applyCentralForce(new Vector3(0.0F, 300.0F, 0.0F));
  }

  
  public void setPosition(Vector3 pos) {
    Matrix4 t = this.body.getWorldTransform().setTranslation(pos);
    this.body.setWorldTransform(t);
  }

  
  public Vector3 getPosition() {
    return this.body.getWorldTransform().getTranslation(new Vector3());
  }

  
  public World getWorld() {
    return this.world;
  }
}
