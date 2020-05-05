#version 330

uniform sampler2D u_tex1;
uniform sampler2D u_tex2;
uniform sampler2D u_depth;

uniform mat4 u_inverseViewTrans;
uniform vec3 u_viewPos;

struct DirectionalLight
{
vec3 direction;
vec4 color;
};
uniform DirectionalLight u_dirLight;

varying vec2 v_texCoord;

vec3 calcPosition(vec2 screenPosition, float depth)
{
	vec4 pos = u_inverseViewTrans * vec4(screenPosition * vec2(2) - vec2(1), depth * 2.0 - 1, 1);
	return pos.xyz / pos.w;
}

vec3 calcLight(vec3 normal, vec3 lightDir, vec4 color, float shininess)
{
	float diffuseFactor = max(0.0, dot( -lightDir, normal));
	vec3 diffuse = color.w * color.rgb * diffuseFactor;
	
	vec3 position = calcPosition(v_texCoord.xy, texture(u_depth, v_texCoord.xy).r);
	vec3 viewDirection = normalize(u_viewPos - position);
	
	float specularFactor = pow( max(0.0, dot(reflect(lightDir, normal), viewDirection)), shininess);
	vec3 specular = color.w * color.rgb * specularFactor;
	
	return diffuse + specular;
}

vec3 calcDirectionalLight(vec3 normal, DirectionalLight d, float shininess)
{
	return calcLight(normal, d.direction, d.color, shininess);
}

void main()
{
	vec4 t1 = texture(u_tex1, v_texCoord.xy);
	vec4 t2 = texture(u_tex2, v_texCoord.xy);

	vec3 light = calcDirectionalLight(
		normalize(t2.xyz * vec3(2) - vec3(1)),
		u_dirLight,
		t2.w * 1000
	);
	
	gl_FragColor = vec4(t1.rgb * light, 1);
}