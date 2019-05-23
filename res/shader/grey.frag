#version 120

uniform sampler2D texture;

void main(void)
{
    gl_FragColor = gl_Color * texture2D(texture, gl_TexCoord[0].st);
    float greyLevel = (gl_FragColor.r + gl_FragColor.g + gl_FragColor.b) / 3.f;
    gl_FragColor = vec4(greyLevel,greyLevel,greyLevel, gl_FragColor.a);
}