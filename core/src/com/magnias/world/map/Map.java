package com.magnias.world.map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.magnias.game.Game;
import com.magnias.render.Shader;
import com.magnias.util.Vector3i;



public class Map
{
  private World world;
  private int mapWidth = 4;
  private int mapHeight = 2;
  private int mapDepth = 16;
  
  private Chunk[][][] chunks;
  
  private Texture texture = (Texture)Game.assetManager.get("res/texture/terrain.png");

  
  public Map(World world, int mapWidth, int mapHeight, int mapDepth) {
    this.world = world;
    
    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;
    this.mapDepth = mapDepth;
    
    this.chunks = new Chunk[mapWidth][mapHeight][mapDepth];
    
    for (int x = 0; x < this.chunks.length; ) { for (int y = 0; y < (this.chunks[0]).length; ) { for (int z = 0; z < (this.chunks[0][0]).length; z++)
        {
          this.chunks[x][y][z] = new Chunk(this, (new Vector3i(x, y, z)).scl(16.0F)); } 
        y++; }
      
      x++; }
  
  }
  public void render(Shader shader, Camera camera) {
    shader.bindTexture("u_diffuse", this.texture);
    
    Vector3 chunkSize = new Vector3(16.0F, 16.0F, 16.0F);
    Vector3 chunkHalfSize = new Vector3(8.0F, 8.0F, 8.0F);
    
    for (int x = 0; x < this.chunks.length; ) { for (int y = 0; y < (this.chunks[0]).length; ) { for (int z = 0; z < (this.chunks[0][0]).length; z++) {
          
          if (camera.frustum.boundsInFrustum((new Vector3(x, y, z)).scl(16.0F).add(chunkHalfSize), chunkSize))
          {
            this.chunks[x][y][z].render(shader); } 
        } 
        y++; }
      
      x++; }
  
  }
  public void setBlock(Vector3 pos, byte id) {
    setBlock(pos.x, pos.y, pos.z, id);
  }

  
  public void setBlock(float x, float y, float z, byte id) {
    setBlock((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z), id);
  }

  
  public void setBlock(int x, int y, int z, byte id) {
    if (x < 0 || y < 0 || z < 0 || x >= this.mapWidth * 16 || y >= this.mapHeight * 16 || z >= this.mapDepth * 16)
      return; 
    int xMod = x % 16;
    int yMod = y % 16;
    int zMod = z % 16;
    this.chunks[(int)Math.floor((x / 16))][(int)Math.floor((y / 16))][(int)Math.floor((z / 16))].setBlock(xMod, yMod, zMod, id);
  }

  
  public Vector3 raycast(Ray ray) {
    return raycast(ray.origin, ray.direction);
  }

  
  public Vector3 raycast(Vector3 start, Vector3 direction) {
    Vector3 rayPos = start.cpy();
    float stepLength = 0.01F;
    float rayLenght = 800.0F;
    for (int i = 0; i < rayLenght / stepLength; i++) {
      
      if (rayPos.x >= 0.0F && rayPos.y >= 0.0F && rayPos.z >= 0.0F && rayPos.x < (this.mapWidth * 16) && rayPos.y < (this.mapHeight * 16) && rayPos.z < (this.mapDepth * 16))
      {
        if (getBlock(rayPos) != 0)
        {
          return rayPos;
        }
      }
      rayPos.add(direction.cpy().scl(stepLength));
    } 
    
    return null;
  }


  
  public byte getBlock(Vector3 pos) {
    return getBlock(pos.x, pos.y, pos.z);
  }

  
  public byte getBlock(float x, float y, float z) {
    return getBlock((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
  }

  
  public byte getBlock(Vector3i pos) {
    return getBlock(pos.x, pos.y, pos.z);
  }

  
  public byte getBlock(int x, int y, int z) {
    if (x < 0 || y < 0 || z < 0 || x >= this.mapWidth * 16 || y >= this.mapHeight * 16 || z >= this.mapDepth * 16)
    {
      return 0;
    }
    
    int xMod = x % 16;
    int yMod = y % 16;
    int zMod = z % 16;
    return this.chunks[(int)Math.floor((x / 16))][(int)Math.floor((y / 16))][(int)Math.floor((z / 16))].getBlock(xMod, yMod, zMod);
  }

  
  public void updateAllChunkInfo() {
    for (int x = 0; x < this.chunks.length; ) { for (int y = 0; y < (this.chunks[0]).length; ) { for (int z = 0; z < (this.chunks[0][0]).length; z++)
        {
          this.chunks[x][y][z].updateInfo(this.world); } 
        y++; }
      
      x++; }
  
  } public void updateChunkInfoAtBlock(Vector3 position) {
    updateChunkInfoAtBlock((int)Math.floor(position.x), (int)Math.floor(position.y), (int)Math.floor(position.z));
  }

  
  public void updateChunkInfoAtBlock(int x, int y, int z) {
    if (x < 0 || y < 0 || z < 0 || x >= this.mapWidth * 16 || y >= this.mapHeight * 16 || z >= this.mapDepth * 16) {
      return;
    }


    
    int chunkX = (int)Math.floor((x / 16));
    int chunkY = (int)Math.floor((y / 16));
    int chunkZ = (int)Math.floor((z / 16));
    
    this.chunks[chunkX][chunkY][chunkZ].updateInfo(this.world);

    
    int xMod = x % 16;
    int yMod = y % 16;
    int zMod = z % 16;
    
    if (xMod == 0 && chunkX != 0) this.chunks[chunkX - 1][chunkY][chunkZ].updateInfo(this.world); 
    if (xMod == 15 && chunkX != this.mapWidth - 1) this.chunks[chunkX + 1][chunkY][chunkZ].updateInfo(this.world); 
    if (yMod == 0 && chunkY != 0) this.chunks[chunkX][chunkY - 1][chunkZ].updateInfo(this.world); 
    if (yMod == 15 && chunkY != this.mapHeight - 1) this.chunks[chunkX][chunkY + 1][chunkZ].updateInfo(this.world); 
    if (zMod == 0 && chunkZ != 0) this.chunks[chunkX][chunkY][chunkZ - 1].updateInfo(this.world); 
    if (zMod == 15 && chunkZ != this.mapDepth - 1) this.chunks[chunkX][chunkY][chunkZ + 1].updateInfo(this.world);
  
  }
  
  public World getWorld() {
    return this.world;
  }

  
  public int getWorldHeightInBlocks() {
    return this.mapHeight * 16;
  }

  
  public Vector3 getWorldCenter() {
    return (new Vector3(this.mapWidth, this.mapHeight, this.mapDepth)).scl(8.0F);
  }
  
  public void dispose() {}
}
