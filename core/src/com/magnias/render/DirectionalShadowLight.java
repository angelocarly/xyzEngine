package com.magnias.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;



public class DirectionalShadowLight
  extends DirectionalLight
{
  private FrameBuffer fbo;
  private Camera camera;
  private float halfViewportWidth;
  private float halfViewportHeight;
  
  public DirectionalShadowLight(int shadowMapWidth, int shadowMapHeight, float viewportWidth, float viewportHeight, float zNear, float zFar) {
    this.fbo = FrameBuffer.create(FrameBuffer.Format.RGBA32F, shadowMapWidth, shadowMapHeight, true, true);
//      this.fbo = new com.badlogic.gdx.graphics.glutils.FrameBuffer(Pixmap.Format.RGBA8888, shadowMapWidth, shadowMapHeight, true, true);
    
    this.camera = (Camera)new OrthographicCamera(viewportWidth, viewportHeight);
    this.camera.near = zNear;
    this.camera.far = zFar;
    
    this.halfViewportWidth = viewportWidth / 2.0F;
    this.halfViewportHeight = viewportHeight / 2.0F;
  }


  
  public void update(Vector3 position, Vector3 direction) {
    this.direction.set(direction);
    this.camera.position.set(position);
    this.camera.direction.set(direction);
    update();
  }

  
  public void update() {
    this.camera.direction.set(this.direction);
    this.camera.update();
  }

  
  public void begin() {
    this.fbo.begin();
    
    Gdx.gl.glClear(16384);
    Gdx.gl.glClear(256);
  }


  
  public void end() {
    this.fbo.end();
  }

  
  public Matrix4 getProjViewTrans() {
    return this.camera.combined;
  }

  
  public FrameBuffer getFrameBuffer() {
    return this.fbo;
  }

  
  public Camera getCamera() {
    return this.camera;
  }
}
