package com.magnias.world.map;

import java.util.Random;


public class PerlinNoise
{
  public static float[][] GenerateWhiteNoise(int width, int height, Random random) {
    float[][] noise = new float[width][height];
    
    for (int i = 0; i < width; i++) {
      
      for (int j = 0; j < height; j++)
      {
        noise[i][j] = (float)random.nextDouble() % 1.0F;
      }
    } 
    
    return noise;
  }

  
  public static float[][] generateSmoothNoise(float[][] baseNoise, int octave) {
    int width = baseNoise.length;
    int height = (baseNoise[0]).length;
    
    float[][] smoothNoise = new float[width][height];
    
    int samplePeriod = 1 << octave;
    float sampleFrequency = 1.0F / samplePeriod;
    
    for (int i = 0; i < width; i++) {

      
      int sample_i0 = i / samplePeriod * samplePeriod;
      int sample_i1 = (sample_i0 + samplePeriod) % width;
      float horizontal_blend = (i - sample_i0) * sampleFrequency;
      
      for (int j = 0; j < height; j++) {

        
        int sample_j0 = j / samplePeriod * samplePeriod;
        int sample_j1 = (sample_j0 + samplePeriod) % height;
        float vertical_blend = (j - sample_j0) * sampleFrequency;

        
        float top = interpolate(baseNoise[sample_i0][sample_j0], baseNoise[sample_i1][sample_j0], horizontal_blend);


        
        float bottom = interpolate(baseNoise[sample_i0][sample_j1], baseNoise[sample_i1][sample_j1], horizontal_blend);


        
        smoothNoise[i][j] = interpolate(top, bottom, vertical_blend);
      } 
    } 
    
    return smoothNoise;
  }

  
  public static float interpolate(float x0, float x1, float alpha) {
    return x0 * (1.0F - alpha) + alpha * x1;
  }

  
  public static float[][] generatePerlinNoise(float[][] baseNoise, int octaveCount) {
    int width = baseNoise.length;
    int height = (baseNoise[0]).length;
    
    float[][][] smoothNoise = new float[octaveCount][][];
    
    float persistance = 0.5F;

    
    for (int i = 0; i < octaveCount; i++)
    {
      smoothNoise[i] = generateSmoothNoise(baseNoise, i);
    }
    
    float[][] perlinNoise = new float[width][height];
    float amplitude = 1.0F;
    float totalAmplitude = 0.0F;

    
    for (int octave = octaveCount - 1; octave >= 0; octave--) {
      
      amplitude *= persistance;
      totalAmplitude += amplitude;
      
      for (int k = 0; k < width; k++) {
        
        for (int m = 0; m < height; m++)
        {
          perlinNoise[k][m] = perlinNoise[k][m] + smoothNoise[octave][k][m] * amplitude;
        }
      } 
    } 

    
    for (int j = 0; j < width; j++) {
      
      for (int k = 0; k < height; k++)
      {
        perlinNoise[j][k] = perlinNoise[j][k] / totalAmplitude;
      }
    } 
    
    return perlinNoise;
  }
}
