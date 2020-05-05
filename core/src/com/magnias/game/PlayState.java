package com.magnias.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.magnias.world.map.World;



public class PlayState
  extends GameState
{
  public static String CUSTOM_STRING = "";
  
  private PlayInputProcessor inputProcessor;
  
  public Camera camera;
  
  private OrthographicCamera orthoCamera;
  
  private PerspectiveCamera perspectiveCamera;
  
  private World world;
  
  private SpriteBatch spriteBatch;
  
  public PlayState() {
    super("play");

    
    this.orthoCamera = new OrthographicCamera(150.0F, (150 * Game.HEIGHT / Game.WIDTH));
    this.orthoCamera.translate(16.0F, 0.0F, 16.0F);
    this.orthoCamera.direction.set(0.0F, -1.0F, 1.0F).nor();
    this.orthoCamera.far = 500.0F;
    this.orthoCamera.near = -500.0F;
    this.orthoCamera.update();
    
    this.perspectiveCamera = new PerspectiveCamera(90.0F, Game.WIDTH, Game.HEIGHT);
    this.perspectiveCamera.near = 0.1F;
    this.perspectiveCamera.far = 500.0F;
    this.perspectiveCamera.update();
    
    this.camera = (Camera)this.orthoCamera;
    Game.renderManager.setCamera(this.camera);

    
    this.world = new World();

    
    this.inputProcessor = new PlayInputProcessor(this.world.getPlayer(), this.camera);
    Game.getInputMultiplexer().addProcessor(this.inputProcessor);

    
    this.spriteBatch = new SpriteBatch();
    Matrix4 normalProjection = (new Matrix4()).setToOrtho2D(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), -1.0F, 1.0F);
    this.spriteBatch.setProjectionMatrix(normalProjection);
    this.spriteBatch.getTransformMatrix().setToTranslation(0.0F, 0.0F, 1.0F);
  }




  
  public void init() {}



  
  public void input(float delta) {
    this.inputProcessor.update(delta);
    
    this.world.input(delta);
    
    if (Gdx.input.isKeyJustPressed(45)) {
      
      if (this.camera.equals(this.orthoCamera)) {
        
        this.camera = (Camera)this.perspectiveCamera;
      }
      else {
        
        this.camera = (Camera)this.orthoCamera;
      } 
      Game.renderManager.setCamera(this.camera);
      this.inputProcessor.setCamera(this.camera);
    } 
  }


  
  public void update(float delta) {
    this.world.update(delta);
  }


  
  public void render() {
    Gdx.gl.glEnable(2884);
    this.world.render();
    Gdx.gl.glDisable(2884);

    
    Gdx.gl.glActiveTexture(33984);
    
    this.spriteBatch.begin();
    
    Game.font.draw((Batch)this.spriteBatch, Integer.toString(Game.getFps()), 10.0F, 20.0F);
    Game.font.draw((Batch)this.spriteBatch, 
        String.format("(%.2f, %.2f, %.2f)", new Object[] { Float.valueOf((this.world.getPlayer().getPosition()).x), Float.valueOf((this.world.getPlayer().getPosition()).y), Float.valueOf((this.world.getPlayer().getPosition()).z) }), 10.0F, 50.0F);
    
    Game.font.draw((Batch)this.spriteBatch, CUSTOM_STRING, 10.0F, 80.0F);
    
    this.spriteBatch.end();
  }


  
  public void dispose() {
    this.world.dispose();
  }
}
