package com.magnias.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;



public class ResourceManager
{
  private static HashMap<String, Texture> textures = new HashMap<String, Texture>();

  
  public static void loadTexture(String name, String path) {
    textures.put(name, new Texture(Gdx.files.internal(path)));
  }

  
  public static Texture getTexture(String name) {
    return textures.get(name);
  }
}
