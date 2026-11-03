#version 330 compatibility
uniform float frameTimeCounter;
uniform mat4 gbufferModelViewInverse;
varying vec2 uv; varying vec4 tint; varying vec2 lm; varying vec3 nWorld; varying vec3 pWorld;
void main(){
 uv=(gl_TextureMatrix[0]*gl_MultiTexCoord0).xy;
 lm=(gl_TextureMatrix[1]*gl_MultiTexCoord1).xy;
 tint=gl_Color;
 vec4 p=gl_ModelViewMatrix*gl_Vertex;
 pWorld=(gbufferModelViewInverse*p).xyz;
 nWorld=normalize(mat3(gbufferModelViewInverse)*gl_NormalMatrix*gl_Normal);
 // Prototype wind sway. Later iterations will identify vegetation precisely.
 float sway=sin(frameTimeCounter*1.5+pWorld.x*.7+pWorld.z*.5)*.025*clamp(gl_Vertex.y*.35+.5,0.,1.);
 p.x+=sway; p.z+=sway*.45;
 gl_Position=gl_ProjectionMatrix*p;
}