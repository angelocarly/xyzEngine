package com.magnias.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.magnias.game.Game;



public class DitherShader
  extends Shader
{
  private static DitherShader instance = new DitherShader("res/shader/dither.vs", "res/shader/dither.fs");
  private static Texture dither = (Texture)Game.assetManager.get("res/texture/dither.png");
  static {
    dither.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
  }

  
  public DitherShader(String vertexShaderPath, String fragmentShaderPath) {
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
    
    this.program.setUniformf("u_screenSize", Game.WIDTH, Game.HEIGHT);
    
    bindTexture("u_dither", dither);
  }


  
  public void render(Mesh mesh) {
    mesh.render(this.program, 4);
  }



  
  public void render(Renderable renderable) {
    this.program.setUniformMatrix("u_worldTrans", renderable.worldTransform);
    
    renderable.meshPart.render(this.program);
  }


  
  public void setUniforms(Environment environment) {
    DirectionalLight d = (DirectionalLight)((DirectionalLightsAttribute)environment.get(DirectionalLightsAttribute.Type)).lights.get(0);
    
    this.program.setUniformf("u_directionalLight", d.direction);
  }


  
  public static DitherShader getInstance() {
    return instance;
  }
}
