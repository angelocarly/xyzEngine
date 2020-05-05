package com.magnias.game.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.magnias.game.Game;

public class DesktopLauncher
{
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.resizable = false;
    config.height = 900;
    config.width = 1600;
    config.useGL30 = true;
    config.vSyncEnabled = false;
    config.foregroundFPS = 600;
    config.stencil = 8;
    config.title = "Engine";
    new LwjglApplication((ApplicationListener)new Game(), config);
  }
}
