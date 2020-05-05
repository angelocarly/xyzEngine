#version 330

uniform sampler2D u_diffuse;
uniform sampler2D u_normal;
uniform sampler2D u_depth;
uniform sampler2D u_texRandom;
uniform vec2 u_noiseScale;
uniform mat4 u_invProjectionView;
uniform mat4 u_projectionView;
uniform vec3 u_kernels[32];
uniform int u_kernel_size;

uniform vec2 u_screenSize;

void main()
{
	vec2 texCoord = gl_FragCoord.xy / u_screenSize;
	
	//Calculate pixel world position, depth and normal
	float depth = (texture(u_depth, texCoord).r * 2 - 1);
	vec4 position = u_invProjectionView * vec4(texCoord * 2 - 1, depth, 1);
	position /= position.w;
	vec3 normal = texture(u_normal, texCoord).xyz * vec3(2) - vec3(1);
	normal = normalize(normal);
	
	float occlusion = 0.0;
	vec3 samplePos;
	vec4 sampleTexCoord;
	float sampleDepth;
	vec3 r;
	float rangeCheck;
	float range = 0.6;
	
	vec3 rvec = normalize(vec3(texture(u_texRandom, texCoord * u_noiseScale).xy * 2.0 - 1.0, 0));
	vec3 tangent = normalize(rvec - normal * dot(rvec, normal));
	vec3 bitangent = cross(normal, tangent);
	mat3 tbn = mat3(tangent, bitangent, normal);
	
	for(int i = 0; i < 15; i++)
	{
		r = tbn * u_kernels[i];
		samplePos = r.xyz * range + position.xyz;
		
		sampleTexCoord = u_projectionView * vec4(samplePos, 1);
		sampleTexCoord /= sampleTexCoord.w;
		sampleTexCoord = vec4(sampleTexCoord.xyz * vec3(0.5) + vec3(0.5), 0);
		
		sampleDepth = texture(u_depth, sampleTexCoord.xy).r;
		
		rangeCheck = abs(depth - sampleDepth) < range ? 1.0 : 0.0;
		occlusion += (sampleDepth <= sampleTexCoord.z ? 1.0 : 0.0) * rangeCheck;
	}
	
	occlusion = 1.0 - (occlusion / 15.0);
	
	//texture(u_diffuse, texCoord).rgb
	//gl_FragColor = vec4(texture(u_diffuse, texCoord.xy).rgb * (0.3 +  0.7 * (1 - occlusion / 10)), 1);
	gl_FragColor = vec4(vec3(occlusion), 1);
}