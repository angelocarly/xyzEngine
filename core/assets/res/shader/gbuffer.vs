#version 330

uniform mat4 u_projTrans;
uniform mat4 u_worldTrans;

attribute vec3 a_normal;
attribute vec4 a_position;
attribute vec2 a_texCoord0;

varying vec4 v_position;
varying vec3 v_normal;
varying vec2 v_texCoord;

void main()
{
	v_position = u_projTrans * u_worldTrans * a_position;
	v_normal = (u_worldTrans * vec4(normalize(a_normal), 0)).xyz;
	v_texCoord = a_texCoord0;
	
	gl_Position = v_position;
	
}
