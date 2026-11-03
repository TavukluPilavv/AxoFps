#version 330 compatibility
uniform sampler2D texture; uniform sampler2D lightmap; uniform float frameTimeCounter; uniform vec3 sunPosition;
varying vec2 uv; varying vec4 tint; varying vec2 lm; varying vec3 nWorld; varying vec3 pWorld;
void main(){
 vec2 movingUV=uv+vec2(sin(frameTimeCounter*.5+pWorld.x*.08),cos(frameTimeCounter*.45+pWorld.z*.07))*.002;
 vec4 water=texture2D(texture,movingUV)*tint;
 vec3 n=normalize(nWorld), l=normalize(sunPosition), v=normalize(-pWorld), h=normalize(l+v);
 float spec=pow(max(dot(n,h),0.),48.);
 vec3 light=texture2D(lightmap,lm).rgb;
 vec3 c=water.rgb*(light*.7+vec3(.10,.22,.28))+vec3(1.,.88,.65)*spec*.35;
 gl_FragColor=vec4(c,water.a);
}