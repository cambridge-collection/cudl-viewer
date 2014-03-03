##>VERTEX
{{ShaderLibrary.Basic}}
{{ShaderLibrary.BasicCamera}}
{{ShaderLibrary.VertexTexCoord}}

void main(void) {
  vTexCoord = aVertexTexCoord;
  gl_Position = uProjectionMatrix * uCameraMatrix * uModelMatrix * vec4(aVertexPosition, 1.0);
}

##>FRAGMENT

precision highp float;

{{ShaderLibrary.VertexTexCoord}}

uniform float uNearPlane;
uniform float uFarPlane;

uniform sampler2D uSampler;

float unpack (vec4 colour) {
  const vec4 bitShifts = vec4(1.0 / (256.0 * 256.0 * 256.0),
                1.0 / (256.0 * 256.0),
                1.0 / 256.0,
                1.0);
  return dot(colour , bitShifts);
}


void main (void) {
  float depth = unpack(texture2D(uSampler, vTexCoord));
  float colour =  (2.0 * uNearPlane) / (uFarPlane + uNearPlane - depth * (uFarPlane-uNearPlane));
  gl_FragColor = vec4(colour, colour, colour, 1.0);
}