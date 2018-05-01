#version 330

layout (location = 0) in vec3 vPosition;

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;

void main()
{
    gl_Position = projectionMatrix * worldMatrix * vec4(vPosition, 1.0);
}