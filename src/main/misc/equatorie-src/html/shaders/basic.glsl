##>VERTEX
{{ShaderLibrary.Basic}}
{{ShaderLibrary.BasicCamera}}

varying vec4 vTransformedNormal;

void main(void) {
  gl_Position = uProjectionMatrix * uCameraMatrix * uModelMatrix * vec4(aVertexPosition, 1.0);

}

##>FRAGMENT
precision mediump float;

{{ShaderLibrary.Basic}}

uniform vec4 uColour;

void main() {
  gl_FragColor = uColour;
}