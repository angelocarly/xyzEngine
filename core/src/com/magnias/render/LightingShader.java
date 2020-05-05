package com.magnias.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.magnias.game.Game;









public class LightingShader
  extends Shader
{
  private static LightingShader instance = new LightingShader("res/shader/lighting.vs", "res/shader/lighting.fs");

  
  public LightingShader(String vertexShaderPath, String fragmentShaderPath) {
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
    
    context.setCullFace(1029);
    context.setDepthTest(515);
    
    this.program.setUniformMatrix("u_projTrans", camera.combined);
    this.program.setUniformMatrix("u_invProjectionView", (Game.renderManager.getCamera()).invProjectionView);
    this.program.setUniformf("u_screenSize", Game.WIDTH, Game.HEIGHT);
  }



  
  public void render(Mesh mesh) {
    mesh.render(this.program, 4);
  }



  
  public void render(Renderable renderable) {
    this.program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
    
    renderable.meshPart.render(this.program);
  }



  
  public void setUniforms(Environment environment) {}


  
  public void bindGBuffer(MultiTargetFrameBuffer gBuffer) {
    bindTexture("u_diffuse", gBuffer.getColorBufferTexture(0));
    bindTexture("u_normal", gBuffer.getColorBufferTexture(1));
    bindTexture("u_depth", gBuffer.getDepthBufferHandle());
  }

  
  public void bindLight(DirectionalShadowLight light) {
    String name = "u_directionalLight";
    bindTexture(name + ".shadow", light.getFrameBuffer().getDepthBufferHandle());
    this.program.setUniformMatrix(name + ".transform", (light.getCamera()).combined);
    this.program.setUniformf(name + ".direction", light.direction);
    this.program.setUniformf(name + ".color", light.color);
  }

  
  public static LightingShader getInstance() {
    return instance;
  }
}
