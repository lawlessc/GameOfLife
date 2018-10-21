precision lowp float;
varying vec2 v_texCoord;
uniform  vec3 inversesizex;
uniform  vec3 inversesizey;

const  vec2 scale = vec2(0.5, 0.5);

highp float rand(vec2 co)
{//Thank you http://byteblacksmith.com/improvements-to-the-canonical-one-liner-glsl-rand-for-opengl-es-2-0/
    highp float a = 12.9898;
    highp float b = 78.233;
    highp float c = 43758.5453;
    highp float dt= dot(co.xy ,vec2(a,b));
    highp float sn= mod(dt,3.14);
    return fract(sin(sn) * c);
}


void main() {
float c = step(rand(v_texCoord.xy), 0.6);
gl_FragColor = vec4(c,c,c,1.0);
}