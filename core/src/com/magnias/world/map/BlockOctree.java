package com.magnias.world.map;


public class BlockOctree
{
  private Node node;
  private Chunk chunk;
  
  public BlockOctree(Chunk chunk, boolean ignoreIds) {
    this.node = new Node(16, 0, 0, 0);
    this.chunk = chunk;
  }

  
  public void subDivide() {
    this.node.subDivide(this.chunk);
  }



  
  private static boolean isFilled(Chunk chunk, int sX, int sY, int sZ, int size, int id) {
    for (int x = sX; x < sX + size; ) { for (int y = sY; y < sY + size; ) { for (int z = sZ; z < sZ + size; z++) {
          
          int blockId = chunk.getBlock(x, y, z);
          
          if (blockId == 0 || (id != -1 && blockId != id)) return false; 
        }  y++; }
       x++; }
     return true;
  }

  
  private static boolean hasBlocks(Chunk chunk, int sX, int sY, int sZ, int size) {
    for (int x = sX; x < sX + size; ) { for (int y = sY; y < sY + size; ) { for (int z = sZ; z < sZ + size; z++) {
          
          if (chunk.getBlock(x, y, z) != 0) return true; 
        }  y++; }
       x++; }
     return false;
  }

  
  public Node getMainNode() {
    return this.node;
  }
  
  public class Node
  {
    private Node[] children;
    private int size;
    private int x;
    private int y;
    private int z;
    private boolean hasChildren = false;
    
    public Node(int size, int x, int y, int z) {
      this.size = size;
      this.x = x;
      this.y = y;
      this.z = z;
      this.children = new Node[8];
    }

    
    public void subDivide(Chunk chunk) {
      if (this.size == 1 || BlockOctree.isFilled(chunk, this.x, this.y, this.z, this.size, -1))
        return; 
      int i = 0;
      for (int u = 0; u < 2; ) { for (int v = 0; v < 2; ) { for (int w = 0; w < 2; w++) {
            
            if (BlockOctree.isFilled(chunk, this.x + this.size / 2 * u, this.y + this.size / 2 * v, this.z + this.size / 2 * w, this.size / 2, -1)) {
              
              this.children[i] = new Node(this.size / 2, this.x + this.size / 2 * u, this.y + this.size / 2 * v, this.z + this.size / 2 * w);
              this.children[i].subDivide(chunk);
              this.hasChildren = true;
            }
            else if (BlockOctree.hasBlocks(chunk, this.x + this.size / 2 * u, this.y + this.size / 2 * v, this.z + this.size / 2 * w, this.size / 2)) {
              
              this.children[i] = new Node(this.size / 2, this.x + this.size / 2 * u, this.y + this.size / 2 * v, this.z + this.size / 2 * w);
              this.children[i].subDivide(chunk);
              this.hasChildren = true;
            } 
            i++;
          } 
          v++; }
        
        u++; }
       } public Node[] getChildren() {
      return this.children;
    }

    
    public boolean hasChildren() {
      return this.hasChildren;
    }

    
    public int getSize() {
      return this.size;
    }

    
    public int getX() {
      return this.x;
    }

    
    public int getY() {
      return this.y;
    }

    
    public int getZ() {
      return this.z;
    }
  }
}
