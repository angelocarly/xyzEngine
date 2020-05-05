#version 330

layout (location = 0) out vec4 gPosition;
layout (location = 1) out vec4 gNormal;

varying vec4 v_position;
varying vec3 v_normal;
varying vec2 v_texCoord;

uniform sampler2D u_diffuse;
uniform float u_shininess;

void main()
{
	gPosition = vec4(texture(u_diffuse, v_texCoord).rgb, 1.0);
	//gPosition = vec4(1, 0, 1, 1.0);
	
	gNormal = vec4((normalize(v_normal) + vec3(1)) / vec3(2), u_shininess / 1000);
}