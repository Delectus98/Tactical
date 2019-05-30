#version 120

uniform sampler2D texture;
uniform float elapsed = 0;
uniform int modulus = 10;
uniform float shining = 0; // [0-1]

void main() {
    float shine = (shining + (int(elapsed) % modulus) / float(modulus)) / (shining + 1);
    gl_FragColor = gl_Color * vec4(shine, shine, shine, shine) * texture2D(texture, gl_TexCoord[0].st);
}
