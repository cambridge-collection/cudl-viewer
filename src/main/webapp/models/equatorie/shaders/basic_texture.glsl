##>VERTEX
{{ShaderLibrary.Basic}}
{{ShaderLibrary.BasicCamera}}
{{ShaderLibrary.VertexTexCoord}}

void main(void) {
  vTexCoord = aVertexTexCoord;
  gl_Position = uProjectionMatrix * uCameraMatrix * uModelMatrix * vec4(aVertexPosition, 1.0);
}


##>FRAGMENT

precision mediump float;

{{ShaderLibrary.Basic}}
{{ShaderLibrary.VertexTexCoord}}
  
uniform sampler2D uSampler;

void main(void) {
  gl_FragColor = texture2D(uSampler, vTexCoord);
}