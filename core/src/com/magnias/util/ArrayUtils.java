package com.magnias.util;

import java.util.List;



public class ArrayUtils
{
  public static short[] toShortArray(List<Short> shortList) {
    short[] shortArray = new short[shortList.size()];
    for (int i = 0; i < shortList.size(); i++)
    {
      shortArray[i] = ((Short)shortList.get(i)).shortValue();
    }
    return shortArray;
  }

  
  public static float[] toFloatArray(List<Float> floatList) {
    float[] floatArray = new float[floatList.size()];
    for (int i = 0; i < floatList.size(); i++)
    {
      floatArray[i] = ((Float)floatList.get(i)).floatValue();
    }
    return floatArray;
  }
}
