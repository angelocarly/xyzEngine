package com.magnias.util;

import com.badlogic.gdx.math.Vector3;



public class Vector3i
{
  public int x;
  public int y;
  public int z;
  
  public Vector3i(Vector3i v) {
    this.x = v.x;
    this.y = v.y;
    this.z = v.z;
  }

  
  public Vector3i() {
    this(0, 0, 0);
  }

  
  public Vector3i(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  
  public Vector3i scl(float s) {
    this.x = (int)(this.x * s);
    this.y = (int)(this.y * s);
    this.z = (int)(this.z * s);
    
    return this;
  }

  
  public Vector3i add(int x, int y, int z) {
    this.x += x;
    this.y += y;
    this.z += z;
    
    return this;
  }

  
  public Vector3 toFloatVector() {
    return new Vector3(this.x, this.y, this.z);
  }

  
  public Vector3i cpy() {
    return new Vector3i(this);
  }
}
