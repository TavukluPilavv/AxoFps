#version 330 compatibility
uniform sampler2D texture; uniform sampler2D lightmap; uniform vec3 sunPosition;
varying vec2 uv; varying vec4 tint; varying vec2 lm; varying vec3 nWorld;
void main(){vec4 tex=texture2D(texture,uv)*tint;if(tex.a<.1)discard;float d=max(dot(normalize(nWorld),normalize(sunPosition)),0.);vec3 light=texture2D(lightmap,lm).rgb;gl_FragColor=vec4(tex.rgb*(light*.8+vec3(.16)+d*.25),tex.a);}