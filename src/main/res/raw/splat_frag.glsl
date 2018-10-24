precision lowp float;
uniform  vec3 Point;
uniform  float Radius;
uniform  vec3 FillColor;
//uniform sampler2D textureUnit0;
varying vec2 v_texCoord;
uniform   int on;

//uniform sampler2D density;//Previous DENSITY

//uniform lowp vec3 FillColor;

float Xscale;
float Yscale;


highp float rand(vec2 co)
{//Thank you http://byteblacksmith.com/improvements-to-the-canonical-one-liner-glsl-rand-for-opengl-es-2-0/
    highp float a = 12.9898;
    highp float b = 78.233;
    highp float c = 43758.5453;
    highp float dt= dot(co.xy ,vec2(a,b));
    highp float sn= mod(dt,3.14);
    return fract(sin(sn) * c);
}



void main()
{

vec2 mult;
 mult.x=(2.0*v_texCoord.x - 1.0)/(2.0*Xscale);
 mult.y=(2.0*v_texCoord.y - 1.0)/(2.0*Yscale);

     float d = distance(Point.xy, gl_FragCoord.xy);



    if ((d < Radius) && (on==1) ) {
        float a = (Radius - d) * 0.5;
        a = min(a, 1.0);
        gl_FragColor = vec4(FillColor, a);
          // gl_FragColor = vec4(Point.xy- gl_FragCoord.xy,0, a);
    } else {

      gl_FragColor = vec4(0,0,0,1) ;

    }
}
