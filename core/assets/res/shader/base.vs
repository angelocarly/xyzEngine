#version 330

uniform mat4 u_projTrans;
uniform mat4 u_worldTrans;

attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec2 a_texCoord0;

varying vec2 texCoord;
varying vec3 normal;

void main()
{
	gl_Position = u_projTrans * u_worldTrans * a_position;
	
	texCoord = a_texCoord0;
	normal = a_normal;
}
