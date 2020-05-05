package com.magnias.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;



public class GBufferShader
  extends Shader
{
  private static GBufferShader instance = new GBufferShader();

  
  public GBufferShader() {
    super("res/shader/gbuffer.vs", "res/shader/gbuffer.fs");
  }


  @Override
  public int compareTo(com.badlogic.gdx.graphics.g3d.Shader other) {
    return 0;
  }

  public void begin(Camera camera, RenderContext context) {
    this.camera = camera;
    this.renderContext = context;
    
    this.program.begin();
    
    this.renderContext.setCullFace(1029);
    this.renderContext.setDepthTest(515);
    
    this.program.setUniformMatrix("u_projTrans", camera.combined);
  }


  
  public void render(Mesh mesh) {
    mesh.render(this.program, 4);
  }


  
  public void render(Renderable renderable) {
    this.program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
    
    ColorAttribute d = (ColorAttribute)renderable.material.get(ColorAttribute.Diffuse);
    this.program.setUniformf("u_diffuseColor", d.color);



    
    renderable.meshPart.render(this.program);
  }


  
  public static GBufferShader getInstance() {
    return instance;
  }
  
  public void setUniforms(Environment environment) {}
}
