varying vec4 vertColor;

uniform vec2 cameraPos;
 
void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	vertColor = vec4(0.0, 0.0, 0.0, 1.0);
}