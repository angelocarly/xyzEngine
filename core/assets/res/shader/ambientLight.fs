#version 330

uniform sampler2D u_tex1;

uniform vec3 u_viewPos;

uniform vec4 u_ambient;

varying vec2 v_texCoord;

void main()
{
	vec3 light = u_ambient.rgb * u_ambient.a;
	
	gl_FragColor = vec4(texture(u_tex1, v_texCoord.xy).rgb * light, 1);
}