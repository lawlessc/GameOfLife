precision lowp float;
uniform sampler2D textureUnit0;
uniform sampler2D textureUnit1;
varying vec2 v_texCoord;
uniform  vec3 inversesizex;
uniform  vec3 inversesizey;

//uniform  int   firstrun;
const  vec2 scale = vec2(0.5, 0.5);

void main() {

//Cell information gathering phase.
// get cell value and value of 8 neighbours.
vec4 cell   = texture2D(textureUnit0, v_texCoord.xy);
vec4 neighbourcells   = texture2D(textureUnit0, v_texCoord.xy + inversesizey.xy);
     neighbourcells   += texture2D(textureUnit0, v_texCoord.xy - inversesizey.xy);
     neighbourcells   += texture2D(textureUnit0, v_texCoord.xy - inversesizex.xy);
     neighbourcells   += texture2D(textureUnit0, v_texCoord.xy + inversesizex.xy);
     neighbourcells   += texture2D(textureUnit0, v_texCoord.xy + inversesizey.xy + inversesizex.xy);
     neighbourcells   += texture2D(textureUnit0, v_texCoord.xy - inversesizey.xy + inversesizex.xy);
     neighbourcells   += texture2D(textureUnit0, v_texCoord.xy + inversesizey.xy - inversesizex.xy);
     neighbourcells   += texture2D(textureUnit0, v_texCoord.xy - inversesizey.xy - inversesizex.xy);

//Rules to be implemented here.
//    Any live cell with fewer than two live neighbors dies, as if by underpopulation.
//    Any live cell with two or three live neighbors lives on to the next generation.
//    Any live cell with more than three live neighbors dies, as if by overpopulation.
//    Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

//I implemeted the game of life rules here using step functions as
//step functions are faster than IF statements which cause branching.(allegedly)
float newval = ( step(neighbourcells.x,3.0) * step(  2.0 ,  neighbourcells.x)) * cell.x;
newval += step(neighbourcells.x, 3.0)*step(  3.0 ,  neighbourcells.x); //this will check if exactly 3 neighbours

gl_FragColor = vec4(newval,newval,newval,1.0)  + texture2D(textureUnit1, v_texCoord.xy); //we add the splat  texture here too

}