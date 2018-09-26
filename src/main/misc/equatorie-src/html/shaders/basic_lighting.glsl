##>VERTEX
{{ShaderLibrary.Basic}}
{{ShaderLibrary.BasicCamera}}
{{ShaderLibrary.VertexNormal}}
{{ShaderLibrary.VertexTexCoord}}

varying vec4 vTransformedNormal;
varying vec4 vPosition;
varying vec4 vEyePosition;

void main(void) {
  vPosition =   uModelMatrix * vec4(aVertexPosition, 1.0);
  gl_Position =  uProjectionMatrix * uCameraMatrix * vPosition;

  vTexCoord = aVertexTexCoord;
  vEyePosition = uCameraInverseMatrix * vPosition;

  vTransformedNormal = vec4(uNormalMatrix * aVertexNormal,1.0);
}

##>FRAGMENT
precision mediump float;

{{ShaderLibrary.VertexNormal}}
{{ShaderLibrary.VertexTexCoord}}
{{ShaderLibrary.BasicMaterial}}

varying vec4 vTransformedNormal;
varying vec4 vPosition;
varying vec4 vEyePosition;

uniform vec3 uPointLightPos[10];

uniform vec3 uAmbientLightingColor;

uniform vec3 uPointLightDiffuse[10];
uniform vec3 uPointLightSpecular[10];
uniform vec3 uPointLightAttenuation[10];
uniform int uNumLights;

uniform sampler2D uSampler;

void main(void) {
  vec3 ambientLightWeighting = uAmbientLightingColor;
  float alpha = 1.0;

  vec3 normal = normalize(vTransformedNormal.xyz);
  vec3 specularLightWeighting = vec3(0.0, 0.0, 0.0);
  vec3 diffuseLightWeighting = vec3(0.0, 0.0, 0.0);
  vec3 eyeDirection = normalize(vEyePosition.xyz);
 
  for (int i =0; i < 10; i++) {
    if (i >= uNumLights)
      break;
    vec3 lightDirection = normalize(uPointLightPos[i].xyz - vPosition.xyz);
    vec3 reflectionDirection = reflect(-lightDirection, normal);

    float specularLightBrightness = pow(max(dot(reflectionDirection, eyeDirection), 0.0), uMaterialShininess);
    specularLightWeighting = specularLightWeighting + (uPointLightSpecular[i] * specularLightBrightness);

    float diffuseLightBrightness = max(dot(normal, lightDirection), 0.0);
    diffuseLightWeighting = diffuseLightWeighting + (uPointLightDiffuse[i] * diffuseLightBrightness);
  }

  vec3 materialSpecularColor = uMaterialSpecularColor;
 
  vec4 textureColor = texture2D(uSampler, vTexCoord);
  vec3 materialAmbientColor = uMaterialAmbientColor * textureColor.rgb;
  vec3 materialDiffuseColor = uMaterialDiffuseColor * textureColor.rgb;
  vec3 materialEmissiveColor = uMaterialEmissiveColor * textureColor.rgb;
 
  alpha = textureColor.a;

  gl_FragColor = vec4( materialAmbientColor * ambientLightWeighting
    + materialDiffuseColor * diffuseLightWeighting
    + materialSpecularColor * specularLightWeighting
    + materialEmissiveColor,
    alpha
  );

} 

   