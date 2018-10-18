precision lowp float;
uniform sampler2D textureUnit0;
varying vec2 v_texCoord;
uniform  vec3 inversesize;
uniform  int   firstrun;



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


//Rules to be implemented here.
//    Any live cell with fewer than two live neighbors dies, as if by underpopulation.
//    Any live cell with two or three live neighbors lives on to the next generation.
//    Any live cell with more than three live neighbors dies, as if by overpopulation.
//    Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

vec2 stepX = vec2(inversesize.x, 0);
vec2 stepY = vec2(0, inversesize.y);


//vec2 p = vec2(gl_FragCoord.xy);

vec4 cells   = texture2D(textureUnit0, gl_FragCoord.xy);

vec4 neighbourcells   = texture2D(textureUnit0, gl_FragCoord.xy + stepY);
     neighbourcells   += texture2D(textureUnit0, gl_FragCoord.xy - stepY);
     neighbourcells   += texture2D(textureUnit0, gl_FragCoord.xy - stepX);
     neighbourcells   += texture2D(textureUnit0, gl_FragCoord.xy + stepX);

     neighbourcells   += texture2D(textureUnit0, gl_FragCoord.xy + stepY + stepX);
     neighbourcells   += texture2D(textureUnit0, gl_FragCoord.xy - stepY + stepX);
     neighbourcells   += texture2D(textureUnit0, gl_FragCoord.xy + stepY - stepX);
     neighbourcells   += texture2D(textureUnit0, gl_FragCoord.xy - stepY - stepX);













gl_FragColor = texture2D(textureUnit0, vec2(v_texCoord.x, v_texCoord.y));
//gl_FragColor = texture2D(textureUnit0, vec2(gl_FragCoord.x, gl_FragCoord.y));
}