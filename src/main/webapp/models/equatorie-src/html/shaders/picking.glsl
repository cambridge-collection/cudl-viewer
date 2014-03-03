##>VERTEX
{{ShaderLibrary.BasicCamera}}
{{ShaderLibrary.Basic}}

void main(void) {
  gl_Position = uProjectionMatrix * uCameraMatrix * uModelMatrix * vec4(aVertexPosition, 1.0);
}

##>FRAGMENT
precision mediump float;

{{ShaderLibrary.Basic}}

uniform vec3 uPickingColour;

void main() { 
  gl_FragColor = vec4(uPickingColour,1.0); 
}