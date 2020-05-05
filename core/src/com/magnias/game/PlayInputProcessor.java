package com.magnias.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.magnias.util.VectorMath;
import com.magnias.world.entity.Entity;
import com.magnias.world.entity.EntityBox;
import com.magnias.world.entity.EntityPlayer;

public class PlayInputProcessor
  implements InputProcessor {
  private EntityPlayer player;
  private Camera camera;
  private static final int FORWARD_KEY = 51;
  private static final int BACKWARD_KEY = 47;
  private static final int LEFT_KEY = 29;
  private static final int RIGHT_KEY = 32;
  private static final int UP_KEY = 62;
  private static final int DOWN_KEY = 129;
  private boolean forwardDown;
  private boolean backwardDown;
  private boolean leftDown;
  private boolean rightDown;
  private boolean upDown;
  private boolean downDown;
  private boolean upPressed;
  private float speed = 0.1F;
  
  private Vector3 deltaDir = new Vector3();

  
  public PlayInputProcessor(EntityPlayer player, Camera camera) {
    this.player = player;
    this.camera = camera;
  }


  
  public void update(float delta) {
    this.deltaDir.setZero();
    
    if (this.forwardDown) this.deltaDir.add(this.camera.direction); 
    if (this.backwardDown) this.deltaDir.sub(this.camera.direction); 
    if (this.rightDown) this.deltaDir.add(this.camera.direction.cpy().crs(this.camera.up)); 
    if (this.leftDown) this.deltaDir.sub(this.camera.direction.cpy().crs(this.camera.up)); 
    if (this.upPressed)
    {
      this.player.jump();
    }
    this.upPressed = false;
    
    this.deltaDir.y = 0.0F;
    this.deltaDir.nor();
    this.deltaDir.scl(this.speed);
    if (Gdx.input.isKeyPressed(59)) this.deltaDir.scl(5.0F);
    
    this.player.move(this.deltaDir);
    
    if (Gdx.input.isButtonPressed(1)) {
      
      this.camera.direction.rotate(new Vector3(0.0F, 1.0F, 0.0F), -Gdx.input.getDeltaX() * 0.3F);
      this.camera.direction.rotate((new Vector3(0.0F, 1.0F, 0.0F)).crs(this.camera.direction), Gdx.input.getDeltaY() * 0.3F);
    } 
    if (Gdx.input.isButtonPressed(0));



    
    this.camera.position.set(this.player.getPosition()).add(0.0F, 0.5F, 0.0F);
    this.camera.update();
  }


  
  public boolean keyDown(int keycode) {
    if (keycode == 51) { this.forwardDown = true; }
    else if (keycode == 47) { this.backwardDown = true; }
    else if (keycode == 29) { this.leftDown = true; }
    else if (keycode == 32) { this.rightDown = true; }
    else if (keycode == 62) { this.upDown = true; this.upPressed = true; }
    else if (keycode == 129) { this.downDown = true; }
     return false;
  }


  
  public boolean keyUp(int keycode) {
    if (keycode == 51) { this.forwardDown = false; }
    else if (keycode == 47) { this.backwardDown = false; }
    else if (keycode == 29) { this.leftDown = false; }
    else if (keycode == 32) { this.rightDown = false; }
    else if (keycode == 62) { this.upDown = false; }
    else if (keycode == 129) { this.downDown = false; }
     return false;
  }



  
  public boolean keyTyped(char character) {
    return false;
  }



  
  public boolean touchDown(int x, int y, int pointer, int button) {
    if (button == 0) {
      
      if (Gdx.input.isKeyPressed(45));


      
      if (!Gdx.input.isKeyPressed(59)) {
        
        Vector3 tracePos = this.player.getWorld().getMap().raycast(this.camera.getPickRay(Gdx.input.getX(), Gdx.input.getY()));
        if (tracePos != null)
        {
          Vector3 faceDir = tracePos.cpy().sub(new Vector3((float)Math.floor(tracePos.x) + 0.5F, (float)Math.floor(tracePos.y) + 0.5F, (float)Math.floor(tracePos.z) + 0.5F));
          if (Math.abs(faceDir.x) > Math.abs(faceDir.y)) {
            
            if (Math.abs(faceDir.x) > Math.abs(faceDir.z))
            {
              faceDir.set(faceDir.x / Math.abs(faceDir.x), 0.0F, 0.0F);
            }
            else
            {
              faceDir.set(0.0F, 0.0F, faceDir.z / Math.abs(faceDir.z));
            }
          
          } else if (Math.abs(faceDir.y) > Math.abs(faceDir.z)) {
            
            faceDir.set(0.0F, faceDir.y / Math.abs(faceDir.y), 0.0F);
          }
          else {
            
            faceDir.set(0.0F, 0.0F, faceDir.z / Math.abs(faceDir.z));
          } 
          
          this.player.getWorld().getMap().setBlock(tracePos.add(faceDir), (byte)1);
          this.player.getWorld().getMap().updateChunkInfoAtBlock(tracePos);
        }
      
      } else {
        
        Vector3 tracePos = this.player.getWorld().getMap().raycast(this.camera.getPickRay(Gdx.input.getX(), Gdx.input.getY()));
        if (tracePos != null) {
          
          this.player.getWorld().getMap().setBlock(tracePos, (byte)0);
          this.player.getWorld().getMap().updateChunkInfoAtBlock(tracePos);
          this.player.getWorld().getEntityManager().addEntity((Entity)new EntityBox(this.player.getWorld(), VectorMath.floor(tracePos).add(0.5F), new Vector3(1.0F, 1.0F, 1.0F)));
        } 
      } 
    } 

    
    return false;
  }



  
  public boolean touchUp(int x, int y, int pointer, int button) {
    return false;
  }



  
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }



  
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }


  
  public boolean scrolled(int amount) {
    float zoomSpeed = 2.0F;
    this.camera.viewportWidth += amount * this.camera.viewportWidth / this.camera.viewportHeight * zoomSpeed;
    this.camera.viewportHeight += amount * zoomSpeed;
    return false;
  }

  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
}
