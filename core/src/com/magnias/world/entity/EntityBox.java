package com.magnias.world.entity;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.magnias.render.Shader;
import com.magnias.world.map.World;








public class EntityBox
  extends Entity
{
  private static Mesh m;
  private Vector3 size;
  
  static {
    MeshBuilder builder = new MeshBuilder();
    builder.begin(25L, 4);
    builder.box(1.0F, 1.0F, 1.0F);
    m = builder.end();
  }

  
  public EntityBox(World world, Vector3 position, Vector3 size) {
    super(world, position);
    
    this.size = size;
    
    btDefaultMotionState btDefaultMotionState = new btDefaultMotionState();
    btDefaultMotionState.setWorldTransform((new Matrix4()).translate(position));
    btBoxShape btBoxShape = new btBoxShape(new Vector3(size.x / 2.0F, size.y / 2.0F, size.z / 2.0F));
    Vector3 inertia = new Vector3();
    btBoxShape.calculateLocalInertia(1.0F, inertia);
    
    btRigidBody.btRigidBodyConstructionInfo constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(1.0F, (btMotionState)btDefaultMotionState, (btCollisionShape)btBoxShape, inertia);
    this.body = new btRigidBody(constructionInfo);
    constructionInfo.dispose();
    
    this.body.setSleepingThresholds(0.0F, 0.0F);
    
    world.getPhysicsWorld().addRigidBody(this.body);
  }


  
  public void render(Shader shader) {
    shader.setUniformWorldTrans(this.body.getWorldTransform());
    shader.render(m);
  }
}
