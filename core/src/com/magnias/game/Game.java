package com.magnias.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.magnias.render.RenderManager;

public class Game extends ApplicationAdapter {

  public static int WIDTH;
  
  public static int HEIGHT;
  
  public static Mesh screenMesh;
  
  public static Mesh screenMeshFlipped;
  
  public static RenderManager renderManager;
  
  private GameStateManager gameStateManager;
  
  public static AssetManager assetManager;
  
  public static BitmapFont font;
  private static InputMultiplexer inputMultiplexer;
  private static int fps;
  private long lastFpsUpdate;
  private int fpsCount;
  private static Skin skin;
//  private static Stage stage;
  private static Table table;
  
  public void create() {
    renderManager = new RenderManager();
    
    WIDTH = Gdx.graphics.getWidth();
    HEIGHT = Gdx.graphics.getHeight();

    
    inputMultiplexer = new InputMultiplexer();
    Gdx.input.setInputProcessor((InputProcessor)inputMultiplexer);

    
    Bullet.init();

    
    font = new BitmapFont();
    
    assetManager = new AssetManager();
    
    assetManager.load("res/texture/dither.png", Texture.class);
    assetManager.load("res/texture/entities.png", Texture.class);
    assetManager.load("res/texture/terrain.png", Texture.class);
    assetManager.load("res/texture/red.png", Texture.class);
    assetManager.load("res/untitled.obj", Model.class);
    assetManager.load("res/gui/uiskin.atlas", TextureAtlas.class);
    
    assetManager.finishLoading();

    
    this.gameStateManager = new GameStateManager();
    this.gameStateManager.addGameState(new PlayState());
    this.gameStateManager.setCurrentState("play");
    
    MeshBuilder b = new MeshBuilder();
    b.begin(17L, 4);
    b.rect(0.0F, 0.0F, 0.0F, WIDTH, 0.0F, 0.0F, WIDTH, HEIGHT, 0.0F, 0.0F, HEIGHT, 0.0F, 0.0F, 0.0F, -1.0F);





    
    screenMesh = b.end();
    
    Vector3 n = new Vector3(0.0F, 0.0F, -1.0F);
    b.begin(17L, 4);
    b.rect((new MeshPartBuilder.VertexInfo())
        .set(new Vector3(0.0F, 0.0F, 0.0F), n, Color.WHITE, new Vector2(0.0F, 0.0F)), (new MeshPartBuilder.VertexInfo())
        .set(new Vector3(WIDTH, 0.0F, 0.0F), n, Color.WHITE, new Vector2(1.0F, 0.0F)), (new MeshPartBuilder.VertexInfo())
        .set(new Vector3(WIDTH, HEIGHT, 0.0F), n, Color.WHITE, new Vector2(1.0F, 1.0F)), (new MeshPartBuilder.VertexInfo())
        .set(new Vector3(0.0F, HEIGHT, 0.0F), n, Color.WHITE, new Vector2(0.0F, 1.0F)));
    
    screenMeshFlipped = b.end();

    
//    stage = new Stage();
//    getInputMultiplexer().addProcessor((InputProcessor)stage);
    
    TextureAtlas atlas = (TextureAtlas)assetManager.get("res/gui/uiskin.atlas");
    skin = new Skin(Gdx.files.internal("res/gui/uiskin.json"), atlas);
    
//    stage = new Stage((Viewport)new ScreenViewport());
//    inputMultiplexer.addProcessor((InputProcessor)stage);
    
    table = new Table(skin);
    
    int scale = 1;
    
    table.pad(10.0F, 10.0F, 100.0F, 100.0F);
    table.add((Actor)new TextButton("test", skin));
    table.row();
    
    CheckBox c = new CheckBox("test", skin);
    c.setChecked(true);
    
    table.add((Actor)c);
    
    table.pack();
    
    c.addListener((EventListener)new ChangeListener()
        {
          public void changed(ChangeListener.ChangeEvent event, Actor actor) {}
        });



    
//    stage.addActor((Actor)table);
    
    Gdx.gl.glClearColor(0.8F, 0.8F, 0.8F, 1.0F);
  }



  
  public void render() {
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(16640);

    
    this.gameStateManager.cycle(Gdx.graphics.getDeltaTime());
    
//    stage.act(Math.min(Gdx.graphics.getDeltaTime(), 0.016666668F));
//    stage.draw();

    
    if (this.lastFpsUpdate + 1000L <= System.currentTimeMillis()) {
      
      this.lastFpsUpdate = System.currentTimeMillis();
      
      fps = this.fpsCount;
      this.fpsCount = 0;
    } 
    this.fpsCount++;
  }


  
  public void resize(int width, int height) {}


  
  public void dispose() {
    assetManager.dispose();
    this.gameStateManager.dispose();
  }

  
  public static int getFps() {
    return fps;
  }

  
  public static InputMultiplexer getInputMultiplexer() {
    return inputMultiplexer;
  }
}
