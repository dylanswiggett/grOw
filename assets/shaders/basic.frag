#version 110

varying vec4 vertColor;

uniform vec2 cameraPos;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main(){
	
	float closeness = length(gl_FragCoord.xy - vec2(400, 300)) / 800.0;
	float color = .2 * (1.0 - closeness);
	gl_FragColor = vec4(color, color, color, 1.0);
}