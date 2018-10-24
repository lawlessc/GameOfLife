precision lowp float;
uniform sampler2D textureUnit0;
varying vec2 v_texCoord;
uniform  vec3 inversesizex;
uniform  vec3 inversesizey;
uniform  vec3 light_col;
uniform  vec3 dark_col;

//uniform  int   firstrun;
const  vec2 scale = vec2(0.5, 0.5);

void main() {

//This is just for displaying contents to the actual device screen.
vec4 cell   = texture2D(textureUnit0, v_texCoord.xy);

//colour switcher with no conditionals =)
gl_FragColor = vec4((cell.xyz*light_col)+( (1.0-cell.x)*dark_col),1.0);

}