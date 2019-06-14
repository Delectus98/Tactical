#version 120

uniform sampler2D texture;
uniform float elapsed = 0;
uniform int modulus = 30;
uniform float shining = 1; // [0-1]

int mod(int v, int modulus) {
    while (v >= modulus) v-=modulus;
    return v;
}

int tmod(int v, int pike) {
    int modulus = (pike * 2 + 1);
    int vm = mod(v, modulus);
    if (vm > pike) vm = modulus - vm - 1;
    return vm;
}

void main() {
    float shine = (shining + (tmod(int((gl_TexCoord[0].t*gl_TexCoord[0].s)*modulus + elapsed), modulus) / float(modulus))) / (1.0 + shining);
    gl_FragColor = gl_Color * vec4(shine*shine, shine*shine, shine*shine, 1) * texture2D(texture, gl_TexCoord[0].st);
}
