##>VERTEX
{{ShaderLibrary.Basic}}
{{ShaderLibrary.BasicCamera}}
{{ShaderLibrary.VertexNormal}}
{{ShaderLibrary.BasicTexture}}
{{ShaderLibrary.VertexTangent}}

varying vec4 vTransformedNormal;
varying vec4 vPosition;
varying vec4 vEyePosition;
uniform int uNumLights;

void main(void) {
  vPosition =   uModelMatrix * vec4(aVertexPosition, 1.0);
  gl_Position =  uProjectionMatrix * uCameraMatrix * vPosition;
  vTexCoord = aVertexTexCoord;
  vEyePosition = uCameraInverseMatrix * vPosition;
  vTransformedNormal = vec4(uNormalMatrix * aVertexNormal,1.0);
  vTangent = aVertexTangent;
}

##>FRAGMENT
precision mediump float;

{{ShaderLibrary.Basic}}
{{ShaderLibrary.VertexNormal}}
{{ShaderLibrary.BasicTexture}}
{{ShaderLibrary.VertexTangent}}

varying vec4 vTransformedNormal;
varying vec4 vPosition;
varying vec4 vEyePosition;

uniform vec3 uPointLightPos[10];
uniform vec3 uAmbientLightingColor;

uniform vec3 uMaterialAmbientColor;
uniform vec3 uMaterialDiffuseColor;
uniform vec3 uMaterialSpecularColor;
uniform float uMaterialShininess;
uniform vec3 uMaterialEmissiveColor;

uniform vec3 uPointLightDiffuse[10];
uniform vec3 uPointLightSpecular[10];
uniform vec3 uPointLightAttenuation[10];
uniform int uNumLights;

uniform sampler2D uSamplerNormal;

// http://www.thetenthplanet.de/archives/1180
// We dont need this just yet as we are passing tangent via an attribute
/*
mat3 cotangent_frame(vec3 N, vec3 p, vec2 uv) {
    // get edge vectors of the pixel triangle
    vec3 dp1 = dFdx( p );
    vec3 dp2 = dFdy( p );
    vec2 duv1 = dFdx( uv );
    vec2 duv2 = dFdy( uv );
 
    // solve the linear system
    vec3 dp2perp = cross( dp2, N );
    vec3 dp1perp = cross( N, dp1 );
    vec3 T = dp2perp * duv1.x + dp1perp * duv2.x;
    vec3 B = dp2perp * duv1.y + dp1perp * duv2.y;
 
    // construct a scale-invariant frame 
    float invmax = inversesqrt( max( dot(T,T), dot(B,B) ) );
    return mat3( T * invmax, B * invmax, N );
}*/


vec3 perturb_normal( vec3 N, vec3 V, vec2 texcoord ) {
  // assume N, the interpolated vertex normal and 
  // V, the view vector (vertex to eye)
  vec3 map = texture2D(uSamplerNormal, texcoord ).xyz;
  map = map * 2.0 - 1.0;
  //mat3 TBN = cotangent_frame(N, -V, texcoord);
  
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

   