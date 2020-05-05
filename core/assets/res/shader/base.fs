#version 330

uniform sampler2D u_diffuse;

varying vec2 texCoord;
varying vec3 normal;

void main()
{
	gl_FragColor = vec4(texture(u_diffuse, texCoord).rgb, 1);
	//gl_FragColor = vec4(1, 0, 1, 1);
}