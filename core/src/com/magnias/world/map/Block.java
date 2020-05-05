package com.magnias.world.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.magnias.game.Game;
import java.util.HashMap;



public class Block
{
  private static Texture spriteSheet = (Texture)Game.assetManager.get("res/texture/terrain.png");
  private static int CELL_SIZE = 16;
  
  public static final Vector2 TEXTURE_SCALE = new Vector2(CELL_SIZE / spriteSheet.getWidth(), CELL_SIZE / spriteSheet.getHeight());
  
  private static HashMap<Byte, Block> blocks = new HashMap<Byte, Block>();
  
  public static Block TEST = new Block((byte)1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0);
  public static Block TEST2 = new Block((byte)2, 1, 0);
  
  private byte id;
  
  public static final int Z_POS = 0;
  
  public static final int Z_NEG = 1;
  public static final int X_POS = 2;
  public static final int X_NEG = 3;
  public static final int Y_POS = 4;
  public static final int Y_NEG = 5;
  private TextureRegion[] texture = new TextureRegion[6];

  
  public Block(byte id, int texX, int texY) {
    this(id, texX, texY, texX, texY, texX, texY, texX, texY, texX, texY, texX, texY);
  }

  
  public Block(byte id, int texX0, int texY0, int texX1, int texY1, int texX2, int texY2, int texX3, int texY3, int texX4, int texY4, int texX5, int texY5) {
    this.id = id;
    this.texture[0] = new TextureRegion(spriteSheet, CELL_SIZE * texX0, CELL_SIZE * texY0, CELL_SIZE, CELL_SIZE);
    this.texture[1] = new TextureRegion(spriteSheet, CELL_SIZE * texX1, CELL_SIZE * texY1, CELL_SIZE, CELL_SIZE);
    this.texture[2] = new TextureRegion(spriteSheet, CELL_SIZE * texX2, CELL_SIZE * texY2, CELL_SIZE, CELL_SIZE);
    this.texture[3] = new TextureRegion(spriteSheet, CELL_SIZE * texX3, CELL_SIZE * texY3, CELL_SIZE, CELL_SIZE);
    this.texture[4] = new TextureRegion(spriteSheet, CELL_SIZE * texX4, CELL_SIZE * texY4, CELL_SIZE, CELL_SIZE);
    this.texture[5] = new TextureRegion(spriteSheet, CELL_SIZE * texX5, CELL_SIZE * texY5, CELL_SIZE, CELL_SIZE);
    
    this.blocks.put(Byte.valueOf(id), this);
  }

  
  public TextureRegion getTexture(int face) {
    return this.texture[face];
  }

  
  public byte getId() {
    return this.id;
  }

  
  public static Block getBlock(byte id) {
    return blocks.get(Byte.valueOf(id));
  }
}
