package com.magnias.util;

import com.badlogic.gdx.math.Vector3;



public class VectorMath
{
  public static Vector3 floor(Vector3 v) {
    v.x = (float)Math.floor(v.x);
    v.y = (float)Math.floor(v.y);
    v.z = (float)Math.floor(v.z);
    return v;
  }

  
  public static boolean shorterThan(Vector3 p1, Vector3 p2, float length) {
    return ((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y) + (p1.z - p2.z) * (p1.z - p2.z) < length * length);
  }
}
