package com.magnias.world.map;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;



public class ChunkMeshing
{
  public static btCompoundShape createCollisionShape(Chunk chunk) {
    btCompoundShape compound = new btCompoundShape();

    
    boolean[][][] mask = new boolean[16][16][16];
    for (int x = 0; x < 16; ) { for (int y = 0; y < 16; ) { for (int z = 0; z < 16; z++)
        {
          mask[x][y][z] = (chunk.getBlock(x, y, z) != 0); }  y++; }
      
      x++; }
    
    for (int i = 0; i < 16; ) { for (int y = 0; y < 16; ) { for (int z = 0; z < 16; z++) {
          
          if (mask[i][y][z]) {

            
            int dX = 0;
            int dY = 0;
            int dZ = 0; int u;
            for (u = i; u < 16; u++) {
              
              boolean fullY = true;
              for (int v = y; (dY == 0) ? (v < 16) : (v < y + dY); v++) {
                
                boolean fullZ = true;
                for (int w = z; (dZ == 0) ? (w < 16) : (w < z + dZ); w++) {
                  
                  if (!mask[u][v][w]) {
                    
                    if (dZ == 0) { dZ = w - z; break; }
                     fullZ = false;
                    break;
                  } 
                } 
                if (dZ == 0) dZ = 16 - z;
                
                if (!fullZ) {
                  
                  if (dY == 0) { dY = v - y; break; }
                   fullY = false;
                  break;
                } 
              } 
              if (dY == 0) dY = 16 - y;
              
              if (!fullY) {
                
                if (dX == 0) dX = u - i; 
                break;
              } 
            } 
            if (dX == 0) dX = 16 - i;

            
            for (u = i; u < i + dX; ) { for (int v = y; v < y + dY; ) { for (int w = z; w < z + dZ; w++)
                {
                  mask[u][v][w] = false; }  v++; }
               u++; }
            
            compound.addChildShape((new Matrix4()).setToTranslation(i + dX / 2.0F, y + dY / 2.0F, z + dZ / 2.0F), (btCollisionShape)new btBoxShape(new Vector3(dX / 2.0F, dY / 2.0F, dZ / 2.0F)));
          } 
        }  y++; }
       i++; }
     return compound;
  }
}
