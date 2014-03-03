##>VERTEX
{{ShaderLibrary.Basic}}
{{ShaderLibrary.BasicCamera}}
{{ShaderLibrary.VertexNormal}}

varying vec4 vTransformedNormal;

void main(void) {
    gl_Position = uProjectionMatrix * uCameraMatrix * uModelMatrix * vec4(aVertexPosition, 1.0);
    vTransformedNormal = vec4(uNormalMatrix * aVertexNormal, 1.0);
    vNormal = aVertexNormal;
}
    
##>FRAGMENT

precision mediump float;

{{ShaderLibrary.Basic}}
{{ShaderLibrary.VertexNormal}}

varying vec4 vTransformedNormal;

uniform samplerCube uSampler;
void main(void) {
    gl_FragColor = textureCube(uSampler,vNormal);
}
