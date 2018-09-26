##>VERTEX
{{ShaderLibrary.Basic}}
{{ShaderLibrary.BasicCamera}}
{{ShaderLibrary.VertexNormal}}
{{ShaderLibrary.VertexTangent}}
{{ShaderLibrary.VertexTexCoord}}

varying vec4 vTransformedNormal;
varying vec4 vPosition;
varying vec4 vEyePosition;

void main(void) {
  vPosition =   uModelMatrix * vec4(aVertexPosition, 1.0);
  gl_Position =  uProjectionMatrix * uCameraMatrix * vPosition;
  vTexCoord = aVertexTexCoord;
  vEyePosition = uCameraInverseMatrix * vPosition;
  vTangent = aVertexTangent;
  vTransformedNormal = vec4(uNormalMatrix * aVertexNormal,1.0);
}

##>FRAGMENT
precision mediump float;

{{ShaderLibrary.VertexNormal}}
{{ShaderLibrary.VertexTexCoord}}
{{ShaderLibrary.VertexTangent}}
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

uniform vec3 uSpecColour;
uniform float uAlphaX;
uniform float uAlphaY;

uniform sampler2D uSampler;
uniform sampler2D uSamplerNormal;

vec3 perturb_normal( vec3 N, vec3 V, vec2 texcoord ) {
  vec3 map = texture2D(uSamplerNormal, texcoord ).xyz;
  map = map * 2.0 - 1.0;  
  vec3 B = cross(vTangent,N);
  mat3 TBN = mat3(vTangent,B,N);
  return normalize(TBN * map);
}


void main(void) {
  vec3 ambientLightWeighting = uAmbientLightingColor;
  float alpha = 1.0;

  vec3 normal = normalize(vTransformedNormal.xyz);

  vec3 specularLightWeighting = vec3(0.0, 0.0, 0.0);
  vec3 diffuseLightWeighting = vec3(0.0, 0.0, 0.0);
  vec3 eyeDirection = normalize(vEyePosition.xyz);

  normal = perturb_normal(normal, eyeDirection, vTexCoord);

  for (int i =0; i < 10; i++) {
    if (i >= uNumLights)
      break;
    vec3 lightDirection = normalize(uPointLightPos[i].xyz - vPosition.xyz);
    vec3 reflectionDirection = reflect(-lightDirection, normal);

    vec3 halfwayVector = normalize(lightDirection + eyeDirection);
    vec3 binormalDirection = cross(normal, vTangent);
    float dotLN = dot(lightDirection, normal);

    float diffuseLightBrightness = max(dotLN, 0.0);
    diffuseLightWeighting = diffuseLightWeighting + (uPointLightDiffuse[i] * diffuseLightBrightness);

    if (dotLN > 0.0) {
       float dotHN = dot(halfwayVector, normal);
       float dotVN = dot(eyeDirection, normal);
       float dotHTAlphaX =  dot(halfwayVector, vTangent) / uAlphaX;
       float dotHBAlphaY = dot(halfwayVector, binormalDirection) / uAlphaY;

       specularLightWeighting = specularLightWeighting + ( uSpecColour
          * sqrt(max(0.0, dotLN / dotVN))
          * exp(-2.0 * (dotHTAlphaX * dotHTAlphaX
          + dotHBAlphaY * dotHBAlphaY) / (1.0 + dotHN)));
    }

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