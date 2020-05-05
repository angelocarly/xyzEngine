package com.magnias.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;






public class RenderManager
{
  private Camera camera;
  private Camera screenCamera = (Camera)new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  public Camera getCamera() {
    return this.camera;
  }
  
  public void setScreenCamera(Camera camera) {
    this.screenCamera = this.screenCamera;
  }
  
  public Camera getScreenCamera() {
    return this.screenCamera;
  }
}
