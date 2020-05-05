package com.magnias.world.map;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.magnias.util.ArrayUtils;
import java.util.ArrayList;
import java.util.List;






public class GreedyMeshing
{
  public static Face[][][][] CreateSimpleFaceMesh(Chunk chunk) {
    Face[][][][] faces = new Face[16][16][16][6];
    
    for (int i = 0; i < 6; i++) {

      
      for (int x = 0; x < faces.length; x++) {
        
        for (int y = 0; y < faces.length; y++) {
          
          for (int z = 0; z < faces.length; z++) {
            
            if (chunk.getAdjacentCube(x, y, z, i) == 0 && chunk.getBlock(x, y, z) != 0) {
              faces[x][y][z][i] = new Face(x, y, chunk.getBlock(x, y, z));
            }
          } 
        } 
      } 
    } 
    return faces;
  }

  
  public static int random() {
    return 4;
  }

  
  public static void create(Chunk chunk, Mesh mesh) {
    List<Float> vertices = new ArrayList<Float>();
    List<Short> shorts = new ArrayList<Short>();
    
    Face[][][][] faces = CreateSimpleFaceMesh(chunk);
    
    int[] p = new int[3];
    int[] n = new int[3];

    
    for (int i = 0; i < 6; i++) {
      
      int x = i % 3;
      int y = (i + 1) % 3;
      int z = (i + 2) % 3;
      
      for (p[z] = 0; p[z] < 16; p[z] = p[z] + 1) {
        
        n[z] = p[z];
        List<Face> facelist = new ArrayList<Face>();


        
        for (p[x] = 0; p[x] < 16; p[x] = p[x] + 1) {
          
          for (p[y] = 0; p[y] < 16; p[y] = p[y] + 1) {

            
            if (faces[p[0]][p[1]][p[2]][i] != null) {
              
              Face mask = new Face(p[x], p[y], (byte)(faces[p[0]][p[1]][p[2]][i]).id);

              
              n[x] = p[x];
              for (n[y] = p[y]; n[y] < 16 && faces[n[0]][n[1]][n[2]][i] != null && (faces[n[0]][n[1]][n[2]][i]).id == mask.id; n[y] = n[y] + 1)
              {
                mask.height++;
              }

              
              for (n[x] = p[x]; n[x] < 16; n[x] = n[x] + 1) {
                
                boolean validRow = true;

                
                for (n[y] = p[y]; n[y] < p[y] + mask.height; n[y] = n[y] + 1) {

                  
                  if (faces[n[0]][n[1]][n[2]][i] == null || (faces[n[0]][n[1]][n[2]][i]).id != mask.id) {
                    
                    validRow = false;
                    
                    break;
                  } 
                } 
                
                if (!validRow) {
                  break;
                }


                
                mask.width++;
              } 


              
              n[z] = p[z];
              for (n[x] = mask.x; n[x] < mask.x + mask.width; ) { for (n[y] = mask.y; n[y] < mask.y + mask.height; n[y] = n[y] + 1)
                {
                  faces[n[0]][n[1]][n[2]][i] = null;
                }
                n[x] = n[x] + 1; }
              
              addFace(vertices, shorts, x, y, z, i, mask, p[z]);
            } 
          } 
        } 
      } 
    } 
    
    mesh.setVertices(ArrayUtils.toFloatArray(vertices));
    mesh.setIndices(ArrayUtils.toShortArray(shorts));
  }

  
  private static void addFace(List<Float> vertices, List<Short> shorts, int x, int y, int z, int d, Face face, int faceZ) {
    int[] n = new int[3];
    int[] r = new int[3];
    Vector3 dir = getVector(d);
    
    n[x] = face.x;
    n[y] = face.y;
    n[z] = faceZ;
    
    TextureRegion t = Block.getBlock((byte)face.id).getTexture(d);
    
    short currentVertex = (short)(vertices.size() / 10);
    
    if (d == 4 || d == 5 || d == 3) {
      
      shorts.add(Short.valueOf(currentVertex));
      shorts.add(Short.valueOf((short)(currentVertex + 2)));
      shorts.add(Short.valueOf((short)(currentVertex + 1)));
      shorts.add(Short.valueOf(currentVertex));
      shorts.add(Short.valueOf((short)(currentVertex + 3)));
      shorts.add(Short.valueOf((short)(currentVertex + 2)));
    
    }
    else {
      
      n[0] = (int)(n[0] + dir.x);
      n[1] = (int)(n[1] + dir.y);
      n[2] = (int)(n[2] + dir.z);
      
      shorts.add(Short.valueOf(currentVertex));
      shorts.add(Short.valueOf((short)(currentVertex + 1)));
      shorts.add(Short.valueOf((short)(currentVertex + 2)));
      shorts.add(Short.valueOf(currentVertex));
      shorts.add(Short.valueOf((short)(currentVertex + 2)));
      shorts.add(Short.valueOf((short)(currentVertex + 3)));
    } 
    
    r[x] = 0;
    r[y] = 0;
    r[z] = 0;

    
    vertices.add(Float.valueOf(n[0] + r[0]));
    vertices.add(Float.valueOf(n[1] + r[1]));
    vertices.add(Float.valueOf(n[2] + r[2]));
    vertices.add(Float.valueOf(dir.x));
    vertices.add(Float.valueOf(dir.y));
    vertices.add(Float.valueOf(dir.z));
    vertices.add(Float.valueOf(0.0F));
    vertices.add(Float.valueOf(0.0F));
    vertices.add(Float.valueOf(t.getU()));
    vertices.add(Float.valueOf(t.getV()));
    
    r[x] = face.width;
    r[y] = 0;
    r[z] = 0;

    
    vertices.add(Float.valueOf(n[0] + r[0]));
    vertices.add(Float.valueOf(n[1] + r[1]));
    vertices.add(Float.valueOf(n[2] + r[2]));
    vertices.add(Float.valueOf(dir.x));
    vertices.add(Float.valueOf(dir.y));
    vertices.add(Float.valueOf(dir.z));
    vertices.add(Float.valueOf(Block.TEXTURE_SCALE.x * face.width));
    vertices.add(Float.valueOf(0.0F));
    vertices.add(Float.valueOf(t.getU()));
    vertices.add(Float.valueOf(t.getV()));
    
    r[x] = face.width;
    r[y] = face.height;
    r[z] = 0;

    
    vertices.add(Float.valueOf(n[0] + r[0]));
    vertices.add(Float.valueOf(n[1] + r[1]));
    vertices.add(Float.valueOf(n[2] + r[2]));
    vertices.add(Float.valueOf(dir.x));
    vertices.add(Float.valueOf(dir.y));
    vertices.add(Float.valueOf(dir.z));
    vertices.add(Float.valueOf(Block.TEXTURE_SCALE.x * face.width));
    vertices.add(Float.valueOf(Block.TEXTURE_SCALE.y * face.height));
    vertices.add(Float.valueOf(t.getU()));
    vertices.add(Float.valueOf(t.getV()));
    
    r[x] = 0;
    r[y] = face.height;
    r[z] = 0;

    
    vertices.add(Float.valueOf(n[0] + r[0]));
    vertices.add(Float.valueOf(n[1] + r[1]));
    vertices.add(Float.valueOf(n[2] + r[2]));
    vertices.add(Float.valueOf(dir.x));
    vertices.add(Float.valueOf(dir.y));
    vertices.add(Float.valueOf(dir.z));
    vertices.add(Float.valueOf(0.0F));
    vertices.add(Float.valueOf(Block.TEXTURE_SCALE.y * face.height));
    vertices.add(Float.valueOf(t.getU()));
    vertices.add(Float.valueOf(t.getV()));
  }


  
  private static Vector3 getVector(int dir) {
    switch (dir) {
      case 4:
        return new Vector3(-1.0F, 0.0F, 0.0F);
      case 1: return new Vector3(1.0F, 0.0F, 0.0F);
      case 5: return new Vector3(0.0F, -1.0F, 0.0F);
      case 2: return new Vector3(0.0F, 1.0F, 0.0F);
      case 3: return new Vector3(0.0F, 0.0F, -1.0F);
      case 0: return new Vector3(0.0F, 0.0F, 1.0F);
    } 
    return new Vector3(0.0F, 0.0F, 0.0F);
  }
  
  private static class Face
  {
    int x;
    int y;
    int width;
    int height;
    int id;
    boolean isMax = false;
    
    public Face(int x, int y, byte id) {
      this.x = x;
      this.y = y;
      this.id = id;
    }

    
    public boolean canConnect(Face face) {
      return (this.id == face.id && !face.isMax);
    }

    
    public String toString() {
      return "Face[id:" + this.id + ", x:" + this.x + ", y:" + this.y + ", w:" + this.width + ", h:" + this.height + "]";
    }
  }
}
