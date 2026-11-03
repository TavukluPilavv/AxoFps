#version 330 compatibility
uniform sampler2D texture; uniform sampler2D lightmap; uniform vec3 sunPosition;
varying vec2 uv; varying vec4 tint; varying vec2 lm; varying vec3 nWorld; varying vec3 pWorld;
void main(){
 vec4 albedo=texture2D(texture,uv)*tint; if(albedo.a<.1) discard;
 float sun=max(dot(normalize(nWorld),normalize(sunPosition)),0.);
 vec3 lmColor=texture2D(lightmap,lm).rgb;
 vec3 warm=vec3(1.0,.88,.70)*(.12+.42*sun);
 gl_FragColor=vec4(albedo.rgb*(lmColor*.78+warm),albedo.a);
}