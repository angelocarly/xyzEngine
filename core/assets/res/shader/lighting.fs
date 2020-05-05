#version 330

uniform sampler2D u_diffuse;
uniform sampler2D u_normal;
uniform sampler2D u_depth;
uniform mat4 u_invProjectionView;

struct DirectionalLight
{
	vec3 direction;
	mat4 transform;
	sampler2D shadow;
	vec4 color;
};

uniform int u_lightCount;
uniform DirectionalLight u_directionalLight;

uniform vec2 u_screenSize;

void main()
{
	vec2 texCoord = gl_FragCoord.xy / u_screenSize;
	vec4 diffuseColor = texture(u_diffuse, texCoord);
	vec4 normalColor = texture(u_normal, texCoord);
	vec4 depthColor = texture(u_depth, texCoord);
	
	//Calculate pixel world position
	float depth = (depthColor.r * 2 - 1);
	vec4 position = u_invProjectionView * vec4(texCoord * 2 - 1, depth, 1);
	position /= position.w;
	
	//Calculate light
	vec4 shadowPos = u_directionalLight.transform * position;
	shadowPos /= shadowPos.w;
	
	//Calulate directional light
	vec3 normal = normalColor.rgb * 2 - vec3(1);
	float cosTheta = dot(normal, - normalize(u_directionalLight.direction));
	vec3 dirlight = vec3(1) * vec3(max(cosTheta, 0));
	
	//calculate shadow
	float shadow = 1.0;
	if(cosTheta > 0)
	{
		vec2 newPos = shadowPos.xy / 2 + 0.5;
		//check to disable shadow sampling when out of bounds
		if(newPos.x < 0.001 || newPos.y < 0.001 || newPos.x > 0.999 || newPos.y > 0.999)
		{
			shadow = 1.0;
		}
		else
		{
			//Shadowing
			float shadowMapDepth = texture(u_directionalLight.shadow, shadowPos.xy / 2 + 0.5).r * 2 - 1;
			float bias = 0.0005 * tan(acos(cosTheta));
			bias = clamp(bias, 0, 0.0002);
			if(shadowMapDepth + bias < shadowPos.z)
			{
				shadow = 0.0;
			}
		}
	}
	
	vec3 light = dirlight * shadow * u_directionalLight.color.rgb;
	
	//gl_FragColor = vec4(light, 1);
	gl_FragColor = vec4(texture(u_diffuse, texCoord).rgb * (light + vec3(0.2)), 1);
	//gl_FragColor = vec4(shadowPos.xyz, 1);
}