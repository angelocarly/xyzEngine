package com.magnias.world.entity;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.magnias.render.Shader;
import com.magnias.world.map.World;


public class EntityBullet
  extends Entity
{
  private static Mesh m;
  private static final float WIDTH = 0.1F;
  private Vector3 direction;
  private float speed;
  
  static {
    MeshBuilder builder = new MeshBuilder();
    builder.begin(9L, 4);
    
    builder.sphere(0.1F, 0.1F, 0.1F, 10, 10);
    m = builder.end();
  }




  
  public EntityBullet(World world, Vector3 position, Vector3 direction, float speed) {
    super(world, position);
    
    btDefaultMotionState btDefaultMotionState = new btDefaultMotionState();
    btDefaultMotionState.setWorldTransform((new Matrix4()).translate(position.cpy().add(direction.scl(1.2F))));
    
    btSphereShape btSphereShape = new btSphereShape(0.05F);
    Vector3 inertia = new Vector3();
    btSphereShape.calculateLocalInertia(1.0F, inertia);
    
    btRigidBody.btRigidBodyConstructionInfo constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(1.0F, (btMotionState)btDefaultMotionState, (btCollisionShape)btSphereShape, inertia);
    this.body = new btRigidBody(constructionInfo);
    constructionInfo.dispose();
    
    this.body.setSleepingThresholds(0.0F, 0.0F);
    
    world.getPhysicsWorld().addRigidBody(this.body);
    
    this.direction = direction;
    this.speed = speed;
    
    this.body.applyCentralForce(direction.scl(4000.0F));
  }



  
  public void update(float delta) {}


  
  public void render(Shader shader) {
    shader.setUniformWorldTrans(this.body.getWorldTransform());
    shader.render(m);
  }
}
