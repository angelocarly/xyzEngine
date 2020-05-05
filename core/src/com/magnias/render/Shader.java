package com.magnias.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import java.io.File;





public abstract class Shader
  implements com.badlogic.gdx.graphics.g3d.Shader
{
  private static final String UNIFORM_WORLD_TRANS = "u_worldTrans";
  private FileHandle vertexShader;
  private FileHandle fragmentShader;
  public ShaderProgram program;
  public Camera camera;
  public RenderContext renderContext;
  private Matrix4 transformBuffer = new Matrix4();

  
  public Shader(String vertexShaderPath, String fragmentShaderPath) {
    this.vertexShader = new FileHandle(new File(vertexShaderPath));
    this.fragmentShader = new FileHandle(new File(fragmentShaderPath));
    
    init();
  }


  
  public void init() {
    this.program = new ShaderProgram(this.vertexShader, this.fragmentShader);
    ShaderProgram.pedantic = false;
    if (!this.program.isCompiled()) {
      
      System.err.println("Failed to compile shader: " + this.vertexShader.path() + " - " + this.fragmentShader.path());
      System.err.println(this.program.getLog());
      Gdx.app.exit();
    } 
  }


  
  public void dispose() {
    this.program.dispose();
  }

  
  public void reload() {
    dispose();
    Gdx.app.log("SHADER", "Shader reloaded: " + this.vertexShader.nameWithoutExtension());
    init();
  }



  
  public int compareTo(Shader other) {
    return 0;
  }



  
  public boolean canRender(Renderable instance) {
    return false;
  }

  
  public abstract void begin(Camera paramCamera, RenderContext paramRenderContext);

  
  public abstract void render(Mesh paramMesh);

  
  public abstract void render(Renderable paramRenderable);

  
  public abstract void setUniforms(Environment paramEnvironment);
  
  public void end() {
    this.program.end();
  }


  
  public void bindTexture(String name, Texture tex) {
    Gdx.gl.glActiveTexture(33984 + tex.getTextureObjectHandle());
    tex.bind();
    this.program.setUniformi(name, tex.getTextureObjectHandle());
  }


  
  public void bindTexture(String name, int tex) {
    Gdx.gl.glActiveTexture(33984 + tex);
    Gdx.gl.glBindTexture(3553, tex);
    this.program.setUniformi(name, tex);
  }

  
  public void setUniformWorldTrans(Matrix4 transform) {
    this.program.setUniformMatrix("u_worldTrans", transform);
  }

  
  public void setUniformWorldTrans(Vector3 position) {
    this.transformBuffer.idt();
    this.transformBuffer.translate(position);
    this.program.setUniformMatrix("u_worldTrans", this.transformBuffer);
  }

  
  public ShaderProgram getProgram() {
    return this.program;
  }
}
