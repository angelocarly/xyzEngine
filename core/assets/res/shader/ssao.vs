#version 330

uniform mat4 u_projTrans;
uniform mat4 u_worldTrans;

attribute vec4 a_position;
attribute vec2 a_texCoord0;

void main()
{
	gl_Position = u_projTrans * u_worldTrans * a_position;
}
