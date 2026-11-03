#version 330 compatibility
uniform mat4 gbufferModelViewInverse;
varying vec2 uv; varying vec4 tint; varying vec2 lm; varying vec3 nWorld; varying vec3 pWorld;
void main(){uv=(gl_TextureMatrix[0]*gl_MultiTexCoord0).xy;lm=(gl_TextureMatrix[1]*gl_MultiTexCoord1).xy;tint=gl_Color;vec4 p=gl_ModelViewMatrix*gl_Vertex;pWorld=(gbufferModelViewInverse*p).xyz;nWorld=normalize(mat3(gbufferModelViewInverse)*gl_NormalMatrix*gl_Normal);gl_Position=gl_ProjectionMatrix*p;}