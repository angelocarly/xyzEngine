package com.magnias.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3;
import com.magnias.game.Game;
import java.util.Random;




public class SSAOShader
  extends Shader
{
  private static SSAOShader instance = new SSAOShader("res/shader/ssao.vs", "res/shader/ssao.fs");
  private static Texture random = (Texture)Game.assetManager.get("res/texture/random4.png");
  static {
    random.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
  }

  
  private float[] kernels;
  private int kernelSize;
  
  public SSAOShader(String vertexShaderPath, String fragmentShaderPath) {
    super(vertexShaderPath, fragmentShaderPath);
    
    setSamples(30);
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
    
    this.program.setUniform3fv("u_kernels", this.kernels, 0, this.kernelSize * 3);
    this.program.setUniformi("u_kernelSize", this.kernelSize);
    
    this.program.setUniformMatrix("u_projTrans", camera.combined);
    
    this.program.setUniformf("u_screenSize", Game.WIDTH, Game.HEIGHT);
    
    bindTexture("u_texRandom", random);
    this.program.setUniformf("u_noiseScale", Game.WIDTH / 4.0F, Game.HEIGHT / 4.0F);
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


  
  public void setSamples(int amount) {
    Random r = new Random(System.currentTimeMillis());
    
    this.kernelSize = amount;
    
    this.kernels = new float[amount * 3];

    
    Vector3 v = new Vector3();
    for (int i = 0; i < amount; i++) {
      
      do {
        v.set(r.nextFloat() * 2.0F - 1.0F, r.nextFloat() * 2.0F - 1.0F, r.nextFloat());
        v = v.nor();
      } while (v.dot(new Vector3(0.0F, 0.0F, 1.0F)) < 0.5F);

      
      float scale = i / amount;
      scale = lerp(0.1F, 1.0F, scale * scale);
      v = v.scl(scale);
      
      this.kernels[i * 3 + 0] = v.x;
      this.kernels[i * 3 + 1] = v.y;
      this.kernels[i * 3 + 2] = v.z;
    } 
  }

  
  private float lerp(float point1, float point2, float alpha) {
    return point1 + alpha * (point2 - point1);
  }

  
  public static SSAOShader getInstance() {
    return instance;
  }
}
