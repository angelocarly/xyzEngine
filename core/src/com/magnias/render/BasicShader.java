package com.magnias.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;



public class BasicShader
  extends Shader
{
  private static BasicShader instance = new BasicShader("res/shader/base.vs", "res/shader/base.fs");

  
  public BasicShader(String vertexShaderPath, String fragmentShaderPath) {
    super(vertexShaderPath, fragmentShaderPath);
  }


  @Override
  public int compareTo(com.badlogic.gdx.graphics.g3d.Shader other) {
    return 0;
  }

  public void begin(Camera camera, RenderContext context) {
    this.camera = camera;
    this.renderContext = context;
    
    this.program.begin();

    
    context.setDepthTest(515);
    
    this.program.setUniformMatrix("u_projTrans", camera.combined);
  }


  
  public void render(Mesh mesh) {
    this.program.setUniformf("u_diffuseColor", Color.BLUE);
    
    mesh.render(this.program, 4);
  }



  
  public void render(Renderable renderable) {
    this.program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
    
    ColorAttribute d = (ColorAttribute)renderable.material.get(ColorAttribute.Diffuse);
    this.program.setUniformf("u_diffuseColor", d.color);



    
    renderable.meshPart.render(this.program);
  }


  
  public static BasicShader getInstance() {
    return instance;
  }
  
  public void setUniforms(Environment environment) {}
}
