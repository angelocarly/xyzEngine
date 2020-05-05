#version 330

uniform sampler2D u_tex1;
uniform sampler2D u_tex2;
uniform sampler2D u_depth;

uniform mat4 u_inverseViewTrans;
uniform vec3 u_viewPos;
uniform vec2 u_inverseScreenSize;

struct PointLight
{
float intensity;
vec3 position;
vec4 color;
};

uniform PointLight u_pointLight;

varying vec2 v_texCoord;

layout (location = 2) out vec4 deferredOut;

vec3 calcPosition(vec2 screenPosition, float depth)
{
	vec4 pos = u_inverseViewTrans * vec4(screenPosition * vec2(2) - vec2(1), depth * 2.0 - 1, 1);
	return pos.xyz / pos.w;
}

vec3 calcPointLight(vec3 normal, PointLight d, float shininess)
{
	//Pixel visible
	float screenDepth = texture(u_depth, gl_FragCoord.xy * u_inverseScreenSize).r;
	if(screenDepth > gl_FragCoord.z) discard;
	
	vec3 position = calcPosition(gl_FragCoord.xy * u_inverseScreenSize, screenDepth);
	vec3 viewDirection = normalize(u_viewPos - position);
	
	vec3 lightDir = normalize(position - d.position);
	
	//Diffuse
	float diffuseFactor = max(0.0, dot( -lightDir, normal));
	
	if(diffuseFactor == 0.0) discard;
	
	vec3 diffuse = d.color.w * d.color.rgb * diffuseFactor;
	
	//specular
	float dist = distance(d.position, position);
	
	float specularFactor = pow( max(0.0, dot(reflect(lightDir, normal), viewDirection)), shininess);
	vec3 specular = d.color.w * d.color.rgb * specularFactor;
	
	vec3 light = (diffuse + specular) * d.intensity / dist;
	
	return light;
}

void main()
{
	gl_FragColor = vec4(0, gl_FragCoord.z, 0, 1);
	return;
	vec4 t1 = texture(u_tex1, gl_FragCoord.xy * u_inverseScreenSize);
	vec4 t2 = texture(u_tex2, gl_FragCoord.xy * u_inverseScreenSize);
	
	vec3 light = calcPointLight(
		normalize(t2.xyz * vec3(2) - vec3(1)),
		u_pointLight,
		t2.w * 1000
	);

	//gl_FragColor = vec4(t1.rgb * light, 1);
	
	deferredOut = vec4(1, 1, 0, 1);
}