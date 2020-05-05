package com.magnias.world.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.TextureBinder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.magnias.game.Game;
import com.magnias.game.PlayState;
import com.magnias.render.*;
import com.magnias.util.VectorMath;
import com.magnias.world.entity.Entity;
import com.magnias.world.entity.EntityBox;
import com.magnias.world.entity.EntityManager;
import com.magnias.world.entity.EntityPlayer;


public class World {
    private Map map;
    private EntityManager entityManager;
    private DebugDrawer debugDrawer;
    private btDispatcher dispatcher;
    private btDbvtBroadphase broadphase;
    private btConstraintSolver constraintSolver;
    private btCollisionConfiguration collisionConfig;
    private btDynamicsWorld physicsWorld;
    private EntityPlayer player;
    private Environment environment;
    private MultiTargetFrameBuffer gBuffer;
    private MultiTargetFrameBuffer lightingBuffer;
    private boolean debugDraw = false;
    float a;
    private RenderContext renderContext;

    public void input(float delta) {
        if (Gdx.input.isKeyPressed(54))
            this.entityManager.addEntity((Entity) new EntityBox(this, this.player.getPosition().cpy().add(0.0F, 5.0F, 0.0F), new Vector3(1.0F, 1.0F, 1.0F)));
        if (Gdx.input.isKeyJustPressed(52))
            this.debugDraw = !this.debugDraw;
        if (Gdx.input.isKeyJustPressed(46))
            this.player.setPosition(new Vector3(10.0F, 30.0F, 10.0F));
        if (Gdx.input.isKeyJustPressed(44))
            DitherShader.getInstance().reload();
        if (Gdx.input.isKeyJustPressed(33)) {
            Vector3 rayFrom = new Vector3((Game.renderManager.getCamera()).position);
            Vector3 rayTo = rayFrom.cpy().add((Game.renderManager.getCamera().getPickRay(Gdx.input.getX(), Gdx.input.getY())).direction.cpy().scl(100.0F));
            ClosestRayResultCallback c = new ClosestRayResultCallback(Vector3.Zero, Vector3.Z);
            c.setCollisionObject(null);
            c.setClosestHitFraction(1.0F);
            c.setRayFromWorld(rayFrom);
            c.setRayToWorld(rayTo);
            this.physicsWorld.rayTest(rayFrom, rayTo, (RayResultCallback) c);
            if (c.hasHit()) {
                Vector3 v = new Vector3();
                c.getHitPointWorld(v);
                PlayState.CUSTOM_STRING = v.toString();
                ((btRigidBody) c.getCollisionObject()).applyCentralForce((new Vector3(rayTo)).scl(2000.0F));
            }
        }
        for (DirectionalLight dLight : ((DirectionalLightsAttribute) this.environment.get(DirectionalLightsAttribute.Type)).lights)
            ((DirectionalShadowLight) dLight).update((Game.renderManager.getCamera()).position.cpy(), dLight.direction);
    }

    public World() {
        this.a = 0.0F;

        this.renderContext = new RenderContext((TextureBinder) new DefaultTextureBinder(1));
        this.collisionConfig = (btCollisionConfiguration) new btDefaultCollisionConfiguration();
        this.dispatcher = (btDispatcher) new btCollisionDispatcher(this.collisionConfig);
        this.broadphase = new btDbvtBroadphase();
        this.constraintSolver = (btConstraintSolver) new btSequentialImpulseConstraintSolver();
        this.physicsWorld = (btDynamicsWorld) new btDiscreteDynamicsWorld(this.dispatcher, (btBroadphaseInterface) this.broadphase, this.constraintSolver, this.collisionConfig);
        this.physicsWorld.setGravity(new Vector3(0.0F, -9.81F, 0.0F));
        this.debugDrawer = new DebugDrawer();
        this.debugDrawer.setDebugMode(32769);
        this.physicsWorld.setDebugDrawer((btIDebugDraw) this.debugDrawer);
        long startTime = System.currentTimeMillis();
        MapGenerator mgen = new MapGenerator(120L);
        this.map = mgen.generateMap(this, 10, 2, 10);
        System.out.println("Map generated in " + (System.currentTimeMillis() - startTime) + " ms");
        this.entityManager = new EntityManager(this);
        this.player = new EntityPlayer(this, new Vector3(8.0F, 32.0F, 8.0F));
        this.entityManager.addEntity((Entity) this.player);
        this.environment = new Environment();
        DirectionalShadowLight d = new DirectionalShadowLight(1024, 1024, 64.0F, 64.0F, -400.0F, 400.0F);
        d.color.set(Color.GREEN);
        d.update(this.map.getWorldCenter(), new Vector3(1.0F, -2.0F, -1.2F));
        this.environment.add((DirectionalLight) d);
        d = new DirectionalShadowLight(1024, 1024, 64.0F, 64.0F, -400.0F, 400.0F);
        d.color.set(Color.RED);
        d.update(this.map.getWorldCenter(), (new Vector3(-1.0F, -2.0F, -1.2F)).nor());
        this.environment.add((DirectionalLight) d);
        float scale = 1.0F;
        this.gBuffer = MultiTargetFrameBuffer.create(Pixmap.Format.RGBA8888, 2, (int) (Game.WIDTH * scale), (int) (Game.HEIGHT * scale), true, true);
        this.lightingBuffer = MultiTargetFrameBuffer.create(Pixmap.Format.RGBA8888, 1, (int) (Game.WIDTH * scale), (int) (Game.HEIGHT * scale), true, true);
    }

    public void render() {
        if (this.debugDraw) {

            this.debugDrawer.begin(Game.renderManager.getCamera());
            this.physicsWorld.debugDrawWorld();
            this.debugDrawer.end();


            return;
        }


        BasicShader basicShader2 = BasicShader.getInstance();

        for (DirectionalLight dLight : ((DirectionalLightsAttribute) this.environment.get(DirectionalLightsAttribute.Type)).lights) {

            DirectionalShadowLight d = (DirectionalShadowLight) dLight;
            basicShader2.begin(d.getCamera(), this.renderContext);

            d.begin();

            renderGeometry((Shader) basicShader2, d.getCamera());

            d.end();

            basicShader2.end();
        }


        this.gBuffer.begin();

        Gdx.gl.glClear(0x100 | 0x4000);

        GBufferShader gBufferShader = GBufferShader.getInstance();
        gBufferShader.begin(Game.renderManager.getCamera(), this.renderContext);

        this.entityManager.render((Shader) gBufferShader, Game.renderManager.getCamera());

        gBufferShader.end();

        this.gBuffer.end();


        this.gBuffer.begin();

        GBufferBlockShader gBufferBlockShader = GBufferBlockShader.getInstance();
        gBufferBlockShader.begin(Game.renderManager.getCamera(), this.renderContext);

        this.map.render((Shader) gBufferBlockShader, Game.renderManager.getCamera());

        gBufferBlockShader.end();

        this.gBuffer.end();


        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(768, 768);
        this.lightingBuffer.begin();

        Gdx.gl.glClear(0x100 | 0x4000);

        LightingShader ls = LightingShader.getInstance();
        ls.begin(Game.renderManager.getScreenCamera(), this.renderContext);

        ls.bindGBuffer(this.gBuffer);
        ls.setUniformWorldTrans(new Vector3((-Game.WIDTH / 2), (-Game.HEIGHT / 2), -1.0F));

        for (DirectionalLight dLight : ((DirectionalLightsAttribute) this.environment.get(DirectionalLightsAttribute.Type)).lights) {

            DirectionalShadowLight d = (DirectionalShadowLight) dLight;
            ls.bindLight(d);
            ls.render(Game.screenMesh);
        }

        ls.end();


        this.lightingBuffer.end();
        Gdx.gl.glDisable(3042);

        DitherShader ditherShader = DitherShader.getInstance();


        BasicShader basicShader1 = BasicShader.getInstance();
        basicShader1.begin(Game.renderManager.getScreenCamera(), this.renderContext);

        basicShader1.setUniformWorldTrans(new Vector3((-Game.WIDTH / 2), (-Game.HEIGHT / 2), -1.0F));
        basicShader1.bindTexture("u_diffuse", (Texture) this.lightingBuffer.getColorBufferTexture());
        basicShader1.render(Game.screenMeshFlipped);
    }

    public void update(float delta) {
        if (!this.debugDraw) {
            this.entityManager.update(delta);
            this.physicsWorld.stepSimulation(delta, 0, 0.016666668F);
        }
    }

    public void createExplosion(Vector3 position, float strength) {
        int range = (int) Math.ceil(strength);

        Vector3 blockPos = new Vector3();
        float x;
        for (x = position.x - range / 2.0F; x <= position.x + range / 2.0F; x++) {
            float y;
            for (y = position.y - range / 2.0F; y <= position.y + range / 2.0F; y++) {
                float z;
                for (z = position.z - range / 2.0F; z <= position.z + range / 2.0F; z++) {

                    blockPos.set(x, y, z);
                    if (VectorMath.shorterThan(position, blockPos, range / 2.0F) && this.map.getBlock(blockPos) != 0) {

                        blockPos = VectorMath.floor(blockPos).add(0.5F);

                        this.map.setBlock(blockPos, (byte) 0);

                        EntityBox b = new EntityBox(this, blockPos, new Vector3(1.0F, 1.0F, 1.0F));
                        b.move(blockPos.sub(position).scl(-2.0F));
                        this.entityManager.addEntity((Entity) b);
                    }
                }
            }
        }
        for (x = position.x - range / 2.0F; x <= position.x + range / 2.0F; x += 8.0F) {
            float y;
            for (y = position.y - range / 2.0F; y <= position.y + range / 2.0F; y += 8.0F) {
                float z;
                for (z = position.z - range / 2.0F; z <= position.z + range / 2.0F; z += 8.0F) {
                    this.map.updateChunkInfoAtBlock((int) x, (int) y, (int) z);
                }
            }
        }
    }


    private void renderGeometry(Shader shader, Camera camera) {
        this.entityManager.render(shader, camera);
        this.map.render(shader, camera);
    }


    public EntityPlayer getPlayer() {
        return this.player;
    }


    public Map getMap() {
        return this.map;
    }


    public EntityManager getEntityManager() {
        return this.entityManager;
    }


    public btDynamicsWorld getPhysicsWorld() {
        return this.physicsWorld;
    }


    public Environment getEnvironment() {
        return this.environment;
    }


    public void dispose() {
        this.map.dispose();
        this.entityManager.dispose();
    }
}
