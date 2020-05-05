#version 330

uniform sampler2D u_diffuse;
uniform sampler2D u_normal;
uniform sampler2D u_depth;
uniform sampler2D u_dither;

uniform vec2 u_screenSize;

int dither[64] = int[](
0, 32, 8, 40, 2, 34, 10, 42, /* 8x8 Bayer ordered dithering */
48, 16, 56, 24, 50, 18, 58, 26, /* pattern. Each input pixel */
12, 44, 4, 36, 14, 46, 6, 38, /* is scaled to the 0..63 range */
60, 28, 52, 20, 62, 30, 54, 22, /* before looking in this table */
3, 35, 11, 43, 1, 33, 9, 41, /* to determine the action. */
51, 19, 59, 27, 49, 17, 57, 25,
15, 47, 7, 39, 13, 45, 5, 37,
63, 31, 55, 23, 61, 29, 53, 21); 

float find_closest(float sx, float sy, float c)
{
	float limit = 0.0;
	
	int x = int(mod(sx / 3, 8));
	int y = int(mod(sy / 3, 8));
	
	limit = (dither[x + y*8]+1)/64.0;
	
	if(c < limit) return 0.0;
	else return 1.0;
	
}

	
void main()
{
	float colorRes = 1/6.0;
	
	vec2 texCoord = gl_FragCoord.xy;
	vec4 lum = vec4(0.299, 0.587, 0.114, 0);
	
	float grayscale = dot(texture2D(u_diffuse, texCoord / u_screenSize), lum);
	vec3 rgb = texture(u_diffuse, texCoord / u_screenSize).rgb;
	vec3 rgbMod = mod(rgb, colorRes);
	
	vec3 finalRgb;
	finalRgb.r = rgb.r - rgbMod.r + find_closest(texCoord.x, texCoord.y, rgbMod.r / colorRes) * colorRes;
	finalRgb.g = rgb.g - rgbMod.g + find_closest(texCoord.x, texCoord.y, rgbMod.g / colorRes) * colorRes;
	finalRgb.b = rgb.b - rgbMod.b + find_closest(texCoord.x, texCoord.y, rgbMod.b / colorRes) * colorRes;
	
	gl_FragColor = vec4(finalRgb, 1);
	//gl_FragColor = vec4(vec3(grayscale - mod(colorRes, grayscale) + find_closest(texCoord.x, texCoord.y, mod(colorRes, grayscale)) * colorRes), 1);
}