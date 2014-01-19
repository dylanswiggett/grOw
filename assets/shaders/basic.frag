#version 110
#extension GL_EXT_gpu_shader4 : enable

varying vec4 vertColor;

uniform vec2 cameraPos;

void main(){
	vec2 actualPos = gl_FragCoord.xy - cameraPos;
	ivec2 modded = ivec2(actualPos) % ivec2(50, 20);
	if (modded.x < 2 || modded.y < 2) {
		gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
	} else {
		gl_FragColor = vec4(.2, .2, .2, 1.0);
	}
}