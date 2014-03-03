##>VERTEX
precision mediump float;
{{ShaderLibrary.Basic}}
{{ShaderLibrary.BasicCamera}}
{{ShaderLibrary.VertexTexCoord}}

uniform float uNumSegments;
uniform mat4 uMatrices[25]; // Maximum number of matrices


void main(void) {
  vTexCoord = aVertexTexCoord;
  int idx = int(clamp(floor(vTexCoord.y * (uNumSegments-1.0) ),0.0,uNumSegments-1.0 ));
  mat4 tm = uMatrices[idx];
  gl_Position = uProjectionMatrix * uCameraMatrix * tm  * vec4(aVertexPosition, 1.0);

}

##>FRAGMENT
precision mediump float;

{{ShaderLibrary.Basic}}
{{ShaderLibrary.VertexTexCoord}}

uniform vec4 uColour;
// There appears to be an error here. If we leave out uNumSegments it disappears! ><

void main() {
  gl_FragColor = uColour;
}