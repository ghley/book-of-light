#version 430 core
out vec4 FragColor;

in mat3 toEyeSpace;
in vec4 eyeSpacePos;
in vec3 eyeSpaceNormal;
in vec3 eyeSpaceTangent;
in vec3 eyeSpaceBiTangent;

in vec2 Uv;

uniform vec3[8] lightPos;
uniform float[8] lightValue;

uniform vec3 color;
uniform float[10] pbr;

float specular;
float specularTint;
float metallic;
float sheen;
float sheenTint;
float roughness;
float anisotropic;
float clearcoat;
float clearcoatGloss;
float subsurface;

const float PI = 3.141593;

float schlick(float i) {
    return pow(clamp(1-i, 0, 1), 5);
}

float gtr1(float NH, float a) {
    if (a >= 1) {
         return 1/PI;
    }
    float aa = a * a;
    float t = 1 + (aa - 1) * NH * NH;

    return (aa-1) / (PI * log(aa) * t);
}

float gtr2(float NH, float a) {
    float aa = a * a;
    float t = 1 + (aa - 1) * NH * NH;

    return aa / (PI * t * t);
}

float gtr2aniso(float NH, float HT, float HB, float x, float y) {
    float htx = HT / x;
    float hby = HB / y;
    float p = htx * htx + hby * hby + NH * NH;
    return 1 / (PI * x * y * p * p);
}

float smith(float NV, float a) {
    float aa = a * a;
    float nv = NV * NV;
    return 1 / (NV * sqrt(aa + nv - aa * nv));
}

float smithaniso(float NV, float VT, float VB, float x, float y) {
    return 1 / (NV + sqrt(VT * VT * x * x + VB * VB * y * y + NV * NV));
}

vec3 brdf(vec3 lightDir, vec3 normal, vec3 tangent, vec3 bitangent) {
    vec3 V = vec3(0, 0, 1);
    vec3 L = lightDir;
    vec3 N = normal;
    vec3 T = tangent;
    vec3 B = bitangent;

    float NL = dot(N, L);
    float NV = dot(N, V);
    if (NL < 0 || NV < 0) {
        return vec3(0,0,0);
    }

    vec3 H = normalize(L + V);
    float NH = dot(N, H);
    float LH = dot(L, H);

    float schlickL = schlick(NL);
    float schlickV = schlick(NV);
    float schlickH = schlick(LH);

    // gamma correction
    vec3 Cdlin = vec3(pow(color.x, 2.2), pow(color.y, 2.2), pow(color.z, 2.2));
    float Cdlum = .3 * Cdlin.x + .6 * Cdlin.y + .1 * Cdlin.z;
    vec3 Ctint = Cdlum > 0 ? Cdlin / Cdlum : vec3(1);
    vec3 Cspec0 = mix(specular * 0.08 * mix(vec3(1), Ctint, specularTint), Cdlin, metallic);
    vec3 Csheen = mix(vec3(1), Ctint, sheenTint);

    // diff
    float Fd90 = 0.5 + 2 * LH * LH * roughness;
    float Fd = mix(1.0, Fd90, schlickL) * mix(1.0, Fd90, schlickV);

    float Fss90 = LH * LH * roughness;
    float Fss = mix(1.0, Fss90, schlickL) * mix(1.0, Fss90, schlickV);
    float ss = 1.25 * (Fss * (1 / (NL + NV) - .5) + .5);

    //specular
    float aspect = sqrt(1 - anisotropic * 0.9);
    float rr = roughness * roughness;
    float x = max(.001, rr / aspect);
    float y = max(.001, rr * aspect);
    float Ds = gtr2aniso(NH, dot(H, T), dot(H, B), x, y);
    vec3 Fs = mix(Cspec0, vec3(1), schlickH);
    float Gs = 1.;
    Gs *= smithaniso(NL, dot(L, T), dot(L, B), x, y);
    Gs *= smithaniso(NV, dot(V, T), dot(V, B), x, y);

    // sheen
    vec3 Fsheen = schlickH * sheen * Csheen;

    // clearcoat
    float Dr = gtr1(NH, mix(.1, .001, clearcoatGloss));
    float Fr = mix(.04,1.0, schlickH);
    float Gr = smith(NL, .25) * smith(NV, .25);

    return ((1/PI) * mix(Fd, ss, subsurface)*Cdlin + Fsheen) * (1-metallic)
        + Gs * Fs * Ds + .25 * clearcoat * Gr * Fr * Dr;
}

void main()
{
    specular = pbr[0];
    specularTint = pbr[1];
    roughness = pbr[2];
    metallic = pbr[3];
    sheen = pbr[4];
    sheenTint = pbr[5];
    anisotropic = pbr[6];
    clearcoat = pbr[7];
    clearcoatGloss = pbr[8];
    subsurface = pbr[9];

    int i = 0;
    //for (i = 0; i < 8; i++) {
        vec3 eyeSpaceLightDir = normalize((toEyeSpace * lightPos[0]) - eyeSpacePos.xyz);
        vec3 color = brdf(eyeSpaceLightDir, normalize(eyeSpaceNormal), normalize(eyeSpaceTangent), normalize(eyeSpaceBiTangent));
        color = max(color, vec3(0.0));
        color *= dot(eyeSpaceLightDir, normalize(eyeSpaceNormal));
        FragColor = vec4(color, 1.0);
     //   break;
    //}

    //vec4 t = texture(base, Uv);
    if (FragColor.x < -100) {
        FragColor = vec4(Uv.x, Uv.y, Uv.x, 1.0);
    }
}