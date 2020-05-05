package com.magnias.world.map;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btShapeHull;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.BufferUtils;
import com.magnias.render.Shader;
import com.magnias.util.ArrayUtils;
import com.magnias.util.Vector3i;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;







public class Chunk
{
  public static final int SIZE = 16;
  public static final int Z_POS = 0;
  public static final int X_POS = 1;
  public static final int Y_POS = 2;
  public static final int Z_NEG = 3;
  public static final int X_NEG = 4;
  public static final int Y_NEG = 5;
  private Map map;
  private byte[][][] data = new byte[16][16][16];
  
  private Vector3i position;
  
  private Mesh mesh;
  
  private btRigidBody body;

  
  public Chunk(Map map, Vector3i position) {
    this.map = map;
    
    this.position = position;
    
    this.mesh = new Mesh(true, 786432, 147456, new VertexAttribute[] { new VertexAttribute(1, 3, "a_position", 0), new VertexAttribute(8, 3, "a_normal", 1), new VertexAttribute(16, 2, "a_texCoord0", 2), new VertexAttribute(32, 2, "a_startTexCoord", 3) });
  }






  
  public void updatePhysicsData(World world) {
    if (this.body != null)
    {
      world.getPhysicsWorld().removeRigidBody(this.body);
    }
    
    btDefaultMotionState btDefaultMotionState = new btDefaultMotionState();
    btDefaultMotionState.setWorldTransform((new Matrix4()).translate(new Vector3(this.position.x, this.position.y, this.position.z)));
    
    btCompoundShape btCompoundShape = ChunkMeshing.createCollisionShape(this);
    
    btRigidBody.btRigidBodyConstructionInfo constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(0.0F, (btMotionState)btDefaultMotionState, (btCollisionShape)btCompoundShape);
    this.body = new btRigidBody(constructionInfo);
    world.getPhysicsWorld().addRigidBody(this.body);
    constructionInfo.dispose();
  }

  
  public void updateRenderData() {
    List<Float> vertices = new ArrayList<Float>();
    List<Short> shorts = new ArrayList<Short>();
    for (int x = 0; x < 16; ) { for (int y = 0; y < 16; ) { for (int z = 0; z < 16; z++) {
          
          if (getBlock(x, y, z) != 0)
          {
            for (int face = 0; face < 6; face++) {
              
              if (getAdjacentCube(x, y, z, face) == 0)
              {
                addFace(vertices, shorts, x, y, z, face); } 
            }  } 
        }  y++; }
       x++; }
     this.mesh.setVertices(ArrayUtils.toFloatArray(vertices));
    this.mesh.setIndices(ArrayUtils.toShortArray(shorts));
    
    GreedyMeshing.create(this, this.mesh);
  }


  
  private static FloatBuffer createVertexFloatBuffer(Mesh mesh) {
    FloatBuffer oldBuf = mesh.getVerticesBuffer();
    FloatBuffer newBuf = BufferUtils.newFloatBuffer(oldBuf.limit());
    for (int i = 0; i < oldBuf.limit(); i++) {
      
      if (i % 8 < 4)
      {
        newBuf.put(oldBuf.get(i));
      }
    } 

    
    return newBuf;
  }

  
  public void updateInfo(World world) {
    updateRenderData();
    updatePhysicsData(world);
  }
  
  public static btConvexHullShape createConvexHullShape(Mesh mesh, boolean optimize) {
    btConvexHullShape shape = new btConvexHullShape(createVertexFloatBuffer(mesh), mesh.getNumVertices(), mesh.getVertexSize());
    if (!optimize) return shape;
    
    btShapeHull hull = new btShapeHull((btConvexShape)shape);
    
    hull.buildHull(1.0F);
    btConvexHullShape result = new btConvexHullShape(hull);
    
    shape.dispose();
    hull.dispose();
    return result;
  }


  
  private void addFace(List<Float> vertices, List<Short> shorts, int x, int y, int z, int direction) {
    TextureRegion t = Block.getBlock(this.data[x][y][z]).getTexture(direction);
    
    short currentVertex = (short)(vertices.size() / 8);
    
    switch (direction) {

      
      case 1:
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 1)));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        shorts.add(Short.valueOf((short)(currentVertex + 3)));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV2()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV2()));
        break;


      
      case 4:
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        shorts.add(Short.valueOf((short)(currentVertex + 1)));
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 3)));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV2()));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV2()));
        break;


      
      case 0:
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        shorts.add(Short.valueOf((short)(currentVertex + 1)));
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 3)));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV2()));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV2()));
        break;


      
      case 3:
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 1)));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        shorts.add(Short.valueOf((short)(currentVertex + 3)));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV2()));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV2()));
        break;



      
      case 2:
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        shorts.add(Short.valueOf((short)(currentVertex + 1)));
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 3)));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV2()));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y + 1.0F));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV2()));
        break;


      
      case 5:
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 1)));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        shorts.add(Short.valueOf(currentVertex));
        shorts.add(Short.valueOf((short)(currentVertex + 2)));
        shorts.add(Short.valueOf((short)(currentVertex + 3)));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV()));
        
        vertices.add(Float.valueOf(x + 1.0F));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU2()));
        vertices.add(Float.valueOf(t.getV2()));
        
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(y));
        vertices.add(Float.valueOf(z + 1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(-1.0F));
        vertices.add(Float.valueOf(0.0F));
        vertices.add(Float.valueOf(t.getU()));
        vertices.add(Float.valueOf(t.getV2()));
        break;
    } 
  }


  
  public void render(Shader shader) {
    if (isEmpty()) {
      return;
    }
    shader.setUniformWorldTrans(this.body.getWorldTransform());
    shader.render(this.mesh);
  }

  
  public void setBlock(int x, int y, int z, byte id) {
    this.data[x][y][z] = id;
  }

  
  public byte getBlock(int x, int y, int z) {
    return this.data[x][y][z];
  }

  
  public byte getAdjacentCube(int x, int y, int z, int direction) {
    if (x == 0 && direction == 4) return this.map.getBlock(this.position.cpy().add(x - 1, y, z)); 
    if (x == 15 && direction == 1) return this.map.getBlock(this.position.cpy().add(x + 1, y, z)); 
    if (y == 0 && direction == 5) return this.map.getBlock(this.position.cpy().add(x, y - 1, z)); 
    if (y == 15 && direction == 2) return this.map.getBlock(this.position.cpy().add(x, y + 1, z)); 
    if (z == 0 && direction == 3) return this.map.getBlock(this.position.cpy().add(x, y, z - 1)); 
    if (z == 15 && direction == 0) return this.map.getBlock(this.position.cpy().add(x, y, z + 1)); 
    return this.data[x + getXDir(direction)][y + getYDir(direction)][z + getZDir(direction)];
  }

  
  private int getXDir(int face) {
    if (face == 4) return -1; 
    if (face == 1) return 1; 
    return 0;
  }

  
  private int getYDir(int face) {
    if (face == 5) return -1; 
    if (face == 2) return 1; 
    return 0;
  }

  
  private int getZDir(int face) {
    if (face == 3) return -1; 
    if (face == 0) return 1; 
    return 0;
  }

  
  public boolean isEmpty() {
    for (int x = 0; x < 16; ) { for (int y = 0; y < 16; ) { for (int z = 0; z < 16; z++) {
          
          if (this.data[x][y][z] != 0) return false; 
        }  y++; }
       x++; }
     return true;
  }
}
