package com.magnias.world.map;

import java.util.Random;



public class MapGenerator
{
  private Random random;
  
  public MapGenerator(long seed) {
    this(new Random(seed));
  }

  
  public MapGenerator(Random random) {
    this.random = random;
  }

  
  public Map generateMap(World world, int xSize, int ySize, int zSize) {
    Map map = new Map(world, xSize, ySize, zSize);

    
    float[][] perlinNoise = PerlinNoise.generateSmoothNoise(
        PerlinNoise.generatePerlinNoise(
          PerlinNoise.GenerateWhiteNoise(xSize * 16, zSize * 16, this.random), 9), 2);

    
    for (int x = 0; x < xSize * 16; ) { for (int z = 0; z < zSize * 16; z++) {
        
        for (int y = 0; y < perlinNoise[x][z] * map.getWorldHeightInBlocks(); y++)
        {
          map.setBlock(x, y, z, (byte)1);
        }
      } 

      
      x++; }


    
    map.updateAllChunkInfo();
    
    return map;
  }
}
