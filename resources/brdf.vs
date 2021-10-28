#version 420 core
layout (location = 0) in vec3 pos;
layout (location = 1) in vec3 normal;
layout (location = 3) in vec2 uv;

out mat3 toEyeSpace;
out vec4 eyeSpacePos;
out vec3 eyeSpaceNormal;
out vec3 eyeSpaceTangent;
out vec3 eyeSpaceBiTangent;

out vec2 Uv;

uniform vec3[100] offset;

uniform mat4 model;

layout (std140, binding = 1) uniform Matrices
{
    mat4 view;          // 0
    mat4 projection;    // 64
};

void main()
{
    vec3 modPos = pos + offset[gl_InstanceID];
    mat4 modelView = model * view;
    toEyeSpace = mat3(view);
    mat3 normalMatrix = mat3(transpose(inverse(modelView))); // can I cast to mat3 before transpose-inverse?

    eyeSpacePos = modelView * vec4(pos, 1.0);
    eyeSpaceNormal = normalMatrix * normal;

    if (abs(eyeSpaceNormal.x) < 0.9999) { // to properly orient tangent/biTangent
        eyeSpaceTangent = normalize(cross(eyeSpaceNormal, vec3(1, 0, 0)));
        eyeSpaceBiTangent = normalize(cross(eyeSpaceNormal, eyeSpaceTangent));
    }else {
        eyeSpaceTangent = normalize(cross(eyeSpaceNormal, vec3(0, 1, 0)));
        eyeSpaceBiTangent = normalize(cross(eyeSpaceNormal, eyeSpaceTangent));
    }

    gl_Position = projection * eyeSpacePos;
    Uv = uv;
}