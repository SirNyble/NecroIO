#version 330

layout (location = 0) in vec3 vPosition;
layout (location = 1) in vec2 vTextureCoord;

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;

out vec2 outTexCoord;
void main()
{
    gl_Position = projectionMatrix * worldMatrix * vec4(vPosition, 1.0);
    outTexCoord = vTextureCoord;
}