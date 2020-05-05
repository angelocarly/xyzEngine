package com.magnias.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLOnlyTextureData;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.FloatBuffer;

public class FrameBuffer
        extends GLFrameBuffer<Texture> {
    private Texture colorTexture;
    private int depthBufferHandle;
    private int depthStencilBufferHandle;
    private static ColorAttachmentFormat fbCreateFormat;
    private boolean hasDepth;
    private boolean hasStencil;

    public enum Format {
        R32F(33326, 6403, 5126),
        RG32F(33328, 33319, 5126),
        RGB32F(34837, 6407, 5126),
        RGBA32F(34836, 6408, 5126),

        RG16F(33327, 33319, 5126),

        R8(33321, 6403, 5121),
        RG8(33323, 33319, 5121),

        R32I(33333, 36244, 5124),

        PixmapFormat(0, 0, 0);

        private final int internal;

        Format(int internal, int format, int type) {
            this.internal = internal;
            this.format = format;
            this.type = type;
        }

        private final int format;
        private final int type;
    }

    public static class ColorAttachmentFormat {
        FrameBuffer.Format format = FrameBuffer.Format.PixmapFormat;
        Pixmap.Format pixmapFormat = Pixmap.Format.RGB888;
        boolean generateMipmaps = false;
        Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
        Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;
        Texture.TextureWrap wrap = Texture.TextureWrap.ClampToEdge;


        public ColorAttachmentFormat(FrameBuffer.Format format, Pixmap.Format pixmapFormat) {
            this.format = format;
            this.pixmapFormat = pixmapFormat;
        }


        public ColorAttachmentFormat(FrameBuffer.Format format, Pixmap.Format pixmapFormat, boolean generateMipmaps, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter) {
            this.format = format;
            this.pixmapFormat = pixmapFormat;
            this.generateMipmaps = generateMipmaps;
            this.minFilter = minFilter;
            this.magFilter = magFilter;
        }


        public ColorAttachmentFormat(FrameBuffer.Format format, Pixmap.Format pixmapFormat, boolean generateMipmaps, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, Texture.TextureWrap wrap) {
            this.format = format;
            this.pixmapFormat = pixmapFormat;
            this.generateMipmaps = generateMipmaps;
            this.minFilter = minFilter;
            this.magFilter = magFilter;
            this.wrap = wrap;
        }
    }


    private static final FloatBuffer tmpColors = BufferUtils.newFloatBuffer(4);


    public static FrameBuffer create(Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
        return create(format, null, width, height, hasDepth, hasStencil);
    }


    public static FrameBuffer create(Pixmap.Format pixmapFormat, int width, int height, boolean hasDepth, boolean hasStencil) {
        return create(Format.PixmapFormat, pixmapFormat, width, height, hasDepth, hasStencil);
    }


    public static FrameBuffer create(Format format, Pixmap.Format pixmapFormat, int width, int height, boolean hasDepth, boolean hasStencil) {
        fbCreateFormat = new ColorAttachmentFormat(format, pixmapFormat);

        return new FrameBuffer(width, height, hasDepth, hasStencil);
    }


    public static FrameBuffer create(ColorAttachmentFormat format, int width, int height, boolean hasDepth, boolean hasStencil) {
        fbCreateFormat = format;
        return new FrameBuffer(width, height, hasDepth, hasStencil);
    }


    private FrameBuffer(int width, int height, boolean hasDepth, boolean hasStencil) {
        super(Pixmap.Format.RGB888, width, height, false, false);
        build(hasDepth, hasStencil);
    }

    @Override
    protected void build() {

    }

    @Override
    /** Sets viewport to the dimensions of framebuffer. Called by {@link #begin()}. */
    protected void setFrameBufferViewport () {
        Gdx.gl20.glViewport(0, 0, colorTexture.getWidth(), colorTexture.getHeight());
    }

    protected void build(boolean hasDepth, boolean hasStencil) {
        this.hasDepth = hasDepth;
        this.hasStencil = hasStencil;

        bind();


        this.colorTexture = createColorTexture();
        Gdx.gl30.glFramebufferTexture2D(36160, 36064, 3553, this.colorTexture
                .getTextureObjectHandle(), 0);


        if (hasStencil) {

            this.depthStencilBufferHandle = Gdx.gl30.glGenTexture();

            Gdx.gl30.glBindTexture(3553, this.depthStencilBufferHandle);

            Gdx.gl.glTexImage2D(3553, 0, 35056, this.width, this.height, 0, 34041, 34042, null);

            Gdx.gl30.glTexParameteri(3553, 34893, 515);
            Gdx.gl30.glTexParameteri(3553, 34892, 0);
            Gdx.gl30.glTexParameterf(3553, 10241, 9728.0F);


            Gdx.gl30.glFramebufferTexture2D(36009, 33306, 3553, this.depthStencilBufferHandle, 0);


        } else if (hasDepth) {

            this.depthBufferHandle = Gdx.gl30.glGenTexture();

            Gdx.gl30.glBindTexture(3553, this.depthBufferHandle);
            Gdx.gl30.glTexParameteri(3553, 34893, 515);
            Gdx.gl30.glTexParameteri(3553, 34892, 0);
            Gdx.gl30.glTexParameterf(3553, 10241, 9728.0F);
            Gdx.gl30.glTexImage2D(3553, 0, 33190, this.width, this.height, 0, 6402, 5126, null);


            Gdx.gl30.glBindTexture(3553, 0);

            Gdx.gl30.glFramebufferTexture2D(36160, 36096, 3553, this.depthBufferHandle, 0);
        }


        int result = Gdx.gl30.glCheckFramebufferStatus(36160);

        unbind();

        if (result != 36053) {
            dispose();
            throw new IllegalStateException("frame buffer couldn't be constructed: error " + result);
        }
    }


    protected Texture createColorTexture() {
        return createColorTexture(0);
    }


    private Texture createColorTexture(int index) {
        Texture result;
        ColorAttachmentFormat format = fbCreateFormat;

        if (format.format == Format.PixmapFormat) {
            int glFormat = Pixmap.Format.toGlFormat(format.pixmapFormat);
            int glType = Pixmap.Format.toGlType(format.pixmapFormat);
            GLOnlyTextureData data = new GLOnlyTextureData(this.width, this.height, 0, glFormat, glFormat, glType);
            result = new Texture((TextureData) data);
        } else {
            ColorBufferTextureData data = new ColorBufferTextureData(format.format, format.generateMipmaps, this.width, this.height);
            result = new Texture(data);
        }

        result.setFilter(format.minFilter, format.magFilter);
        result.setWrap(format.wrap, format.wrap);

        return result;
    }


    protected void disposeColorTexture(Texture colorTexture) {
        colorTexture.dispose();

        if (this.depthBufferHandle != 0) {
            Gdx.gl30.glDeleteTexture(this.depthBufferHandle);
        }

        if (this.depthStencilBufferHandle != 0) {
            Gdx.gl30.glDeleteRenderbuffer(this.depthStencilBufferHandle);
        }
    }

    @Override
    protected void attachFrameBufferColorTexture() {

    }

    public Texture getColorBufferTexture() {
        return this.colorTexture;
    }

    public void clampToBorder(Color color) {
        int handle = this.colorTexture.getTextureObjectHandle();
        Gdx.gl30.glBindTexture(3553, handle);

        Gdx.gl30.glTexParameterf(3553, 10242, 33071.0F);
        Gdx.gl30.glTexParameterf(3553, 10243, 33071.0F);

        synchronized (tmpColors) {
            tmpColors.clear();
            tmpColors.put(color.r);
            tmpColors.put(color.g);
            tmpColors.put(color.b);
            tmpColors.put(color.a);
            tmpColors.flip();
        }


        Gdx.gl30.glBindTexture(3553, 0);
    }

    public void generateMipmap() {
        int handle = this.colorTexture.getTextureObjectHandle();
        Gdx.gl30.glBindTexture(3553, handle);
        Gdx.gl30.glGenerateMipmap(3553);
        Gdx.gl30.glBindTexture(3553, 0);
    }

    public void clearColorBuffer(Color color, int index) {
        clearColorBuffer(color.r, color.g, color.b, color.a, index);
    }

    public void clearColorBuffer(float r, float g, float b, float a, int index) {
        synchronized (tmpColors) {
            tmpColors.clear();
            tmpColors.put(r);
            tmpColors.put(g);
            tmpColors.put(b);
            tmpColors.put(a);
            tmpColors.flip();

            Gdx.gl30.glClearBufferfv(6144, index, tmpColors);
        }
    }

    public void clearColorBuffers(Color color) {
        clearColorBuffers(color.r, color.g, color.b, color.a);
    }

    public void clearColorBuffers(float r, float g, float b, float a) {
        synchronized (tmpColors) {
            tmpColors.clear();
            tmpColors.put(r);
            tmpColors.put(g);
            tmpColors.put(b);
            tmpColors.put(a);
            tmpColors.flip();

            Gdx.gl30.glClearBufferfv(6144, 0, tmpColors);
        }
    }

    public void clearColorBuffers(Color color, int[] indices) {
        clearColorBuffers(color.r, color.g, color.b, color.a, indices);
    }

    public void clearColorBuffers(float r, float g, float b, float a, int[] indices) {
        synchronized (tmpColors) {
            tmpColors.clear();
            tmpColors.put(r);
            tmpColors.put(g);
            tmpColors.put(b);
            tmpColors.put(a);
            tmpColors.flip();

            for (int index : indices) {
                Gdx.gl30.glClearBufferfv(6144, index, tmpColors);
            }
        }
    }

    public void clearDepthBuffer(float depth) {
        synchronized (tmpColors) {
            tmpColors.clear();
            tmpColors.put(depth);
            tmpColors.flip();

            Gdx.gl30.glClearBufferfv(6145, 0, tmpColors);
        }
    }

    public void clearDepthStencilBuffer(float depth, int stencil) {
        Gdx.gl30.glClearBufferfi(34041, 0, depth, stencil);
    }

    private static class ColorBufferTextureData
            implements TextureData {
        private final FrameBuffer.Format format;
        private final int width;
        private final int height;
        private final boolean generateMipmap;
        private boolean isPrepared = false;

        ColorBufferTextureData(FrameBuffer.Format format, boolean generateMipmap, int width, int height) {
            this.format = format;
            this.generateMipmap = generateMipmap;
            this.width = width;
            this.height = height;
        }


        public TextureData.TextureDataType getType() {
            return TextureData.TextureDataType.Custom;
        }


        public boolean isPrepared() {
            return this.isPrepared;
        }


        public void prepare() {
            this.isPrepared = true;
        }


        public Pixmap consumePixmap() {
            return null;
        }


        public boolean disposePixmap() {
            return false;
        }


        public void consumeCustomData(int target) {
            Gdx.gl30.glTexImage2D(target, 0, this.format.internal, this.width, this.height, 0, this.format.format, this.format.type, null);
            if (this.generateMipmap) {
                Gdx.gl30.glGenerateMipmap(target);
            }
        }


        public int getWidth() {
            return this.width;
        }


        public int getHeight() {
            return this.height;
        }


        public Pixmap.Format getFormat() {
            return null;
        }


        public boolean useMipMaps() {
            return this.generateMipmap;
        }


        public boolean isManaged() {
            return true;
        }
    }


    public static void copyDepthStencilBuffer(FrameBuffer target, int destX0, int destY0, int destX1, int destY1, FrameBuffer source, int srcX0, int srcY0, int srcX1, int srcY1) {
        int mask = 256;

        if (source.hasStencil && target.hasStencil) {
            mask |= 0x400;
        }

        int sourceFbo = source.getFramebufferHandle();
        int targetFbo = target.getFramebufferHandle();

        Gdx.gl30.glBindFramebuffer(36008, sourceFbo);
        Gdx.gl30.glBindFramebuffer(36009, targetFbo);

        Gdx.gl30.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, destX0, destY0, destX1, destY1, mask, 9728);

        Gdx.gl30.glBindFramebuffer(36008, 0);
        Gdx.gl30.glBindFramebuffer(36009, 0);
    }


    public int getDepthBufferHandle() {
        if (this.hasStencil) return this.depthStencilBufferHandle;
        return this.depthBufferHandle;
    }


    public int getStencilBufferHandle() {
        return this.depthStencilBufferHandle;
    }
}
