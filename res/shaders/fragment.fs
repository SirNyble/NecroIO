#version 330

in vec2 outTexCoord;

uniform sampler2D texture_sampler;

out vec4 fragColor;

void main()
{
    fragColor = texture(texture_sampler, outTexCoord);//vec4(0.0, 0.5, 0.5, 1.0);
}