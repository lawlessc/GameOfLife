precision lowp float;
uniform sampler2D textureUnit0;
varying vec2 v_texCoord;
uniform  vec3 inversesize;

void main() {


//Rules to be implemented here.
//    Any live cell with fewer than two live neighbors dies, as if by underpopulation.
//    Any live cell with two or three live neighbors lives on to the next generation.
//    Any live cell with more than three live neighbors dies, as if by overpopulation.
//    Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

vec2 stepX = vec2(inversesize.x, 0);
vec2 stepY = vec2(0, inversesize.y);


vec2 p = vec2(gl_FragCoord.xy);

vec3 cells   = texture2D(textureUnit0, p);

vec3 neighbourcells   = texture2D(textureUnit0, p + stepY);
     neighbourcells   += texture2D(textureUnit0, p - stepY);
     neighbourcells   += texture2D(textureUnit0, p - stepX);
     neighbourcells   += texture2D(textureUnit0, p + stepX);

     neighbourcells   += texture2D(textureUnit0, p + stepY + stepX);
     neighbourcells   += texture2D(textureUnit0, p - stepY + stepX);
     neighbourcells   += texture2D(textureUnit0, p + stepY - stepX);
     neighbourcells   += texture2D(textureUnit0, p - stepY - stepX);













gl_FragColor = texture2D(textureUnit0, vec2(v_texCoord.x, v_texCoord.y));
//gl_FragColor = texture2D(textureUnit0, vec2(gl_FragCoord.x, gl_FragCoord.y));
}