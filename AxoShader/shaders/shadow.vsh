#version 330 compatibility
uniform float frameTimeCounter;
void main(){vec4 p=gl_Vertex;float sway=sin(frameTimeCounter*1.5+p.x*.7+p.z*.5)*.02;p.x+=sway;p.z+=sway*.45;gl_Position=gl_ModelViewProjectionMatrix*p;}