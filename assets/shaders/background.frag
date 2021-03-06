#version 110

varying vec4 vertColor;

uniform vec2 cameraPos;

uniform float time;
uniform float bgColor;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main(){
	vec2 actualPos = gl_FragCoord.xy - cameraPos;
	
	float color = rand(actualPos * time / 1000.0) * bgColor;
	gl_FragColor = vec4(color, color, color, 1.0);
}