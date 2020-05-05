package com.magnias.render;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;

public class CustomShaderProvider
        extends BaseShaderProvider {
    private ShaderType currentShader;

    public enum ShaderType {
        BASIC(BasicShader.getInstance());

        private final Shader s;

        ShaderType(Shader s) {
            this.s = s;
        }

        public Shader getShader() {
            return this.s;
        }
    }


    public CustomShaderProvider() {
        for (int i = 0; i < (ShaderType.values()).length; i++) {
            ShaderType.values()[i].getShader().init();
        }
    }


    protected Shader createShader(Renderable renderable) {
        return this.currentShader.getShader();
    }

    public Shader getShader(Renderable renderable) {
        return this.currentShader.getShader();
    }


    public void setShaderType(ShaderType s) {
        this.currentShader = s;
    }
}
