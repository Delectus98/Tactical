#version 120

uniform sampler2D texture;
uniform float colorRatio = 0;

void main(void)
{
    gl_FragColor = gl_Color * texture2D(texture, gl_TexCoord[0].st);
    float greyLevel = (gl_FragColor.r + gl_FragColor.g + gl_FragColor.b) / 3.f;

    gl_FragColor = vec4(
    (greyLevel + colorRatio * gl_FragColor.r) / (1 + colorRatio),
    (greyLevel + colorRatio * gl_FragColor.g) / (1 + colorRatio),
    (greyLevel + colorRatio * gl_FragColor.b) / (1 + colorRatio),
    gl_FragColor.a);
}