{CoffeeGL} = require '../lib/coffeegl/coffeegl'

class LoadItem
  constructor : (@func, @userOnLoaded) ->

  loaded : () -> 
    @loader.itemCompleted(@)
    @userOnLoaded() if @userOnLoaded?


class LoadQueue

  constructor : (@obj, @onLoaded, @onFinish) ->
    @items = []
    @completed_items = []
    @complete = new CoffeeGL.Signal()

    @complete.add onFinish, @ if @onFinish?

  itemCompleted: (item) ->
    @completed_items.push item
    @onLoaded() if @onLoaded?
    if @completed_items.length == @items.length
      @complete.dispatch()

  add : (item) ->
    item.obj = @obj
    item.loader = @
    @items.push item
 

  start : () ->
    for item in @items
      item.func()


_loadLighting = new LoadItem () ->
  r = new CoffeeGL.Request ('../shaders/basic_lighting.glsl')
  r.get (data) =>
    @obj.shader = new CoffeeGL.Shader(data, {"uAmbientLightingColor" : "uAmbientLightingColor"})
    @loaded()
  @  


_loadAniso = new LoadItem () ->
  r = new CoffeeGL.Request ('../shaders/anisotropic.glsl')
  r.get (data) =>
    @obj.shader_aniso = new CoffeeGL.Shader(data, {
      "uAmbientLightingColor" : "uAmbientLightingColor",
      "uSpecColour" : "uSpecColour",
      "uSamplerNormal" : "uSamplerNormal",
      "uAlphaX" : "uAlphaX",
      "uAlphaY" : "uAlphaY"
    })
  
    @loaded()
  @ 

_loadModel = new LoadItem () ->
  r = new CoffeeGL.Request('../models/equatorie.js')
  r.get (data) =>
    @obj.equatorie_model = new CoffeeGL.JSONModel(data,{ onLoad : ()=>  
         @loaded()
      })
    @
  @ 

_loadBasic = new LoadItem () ->
  r = new CoffeeGL.Request ('../shaders/basic.glsl')
  r.get (data) =>
    @obj.shader_basic = new CoffeeGL.Shader(data, {"uColour" : "uColour"})
    @loaded()
    
  @


_loadPicking = new LoadItem () ->
  r = new CoffeeGL.Request('../shaders/picking.glsl')
  r.get (data) =>
    @obj.shader_picker = new CoffeeGL.Shader(data, {"uPickingColour" : "uPickingColour"})
    @loaded()
  @

_loadStringShader = new LoadItem () ->
  r = new CoffeeGL.Request('../shaders/string.glsl')
  r.get (data) =>
    @obj.shader_string = new CoffeeGL.Shader(data, {"uMatrices" : "matrices", "uNumSegments" : "segments"})
    @loaded()
  @

_loadEpicycleNormal = new LoadItem () ->
  @obj.epicycle_normal = new CoffeeGL.Texture("../models/epicycle_NRM.jpg",{unit : 1}, () => 
    @loaded()
  )
  @


_loadPlateNormal = new LoadItem () ->
  @obj.plate_normal = new CoffeeGL.Texture("../models/plate_NRM.jpg",{unit : 1}, () => 
    @loaded()
  )
  @

_loadRimNormal= new LoadItem () ->
  @obj.rim_normal = new CoffeeGL.Texture("../models/ring_NRM.jpg",{unit : 1}, () => 
    @loaded()
  )
  @

_loadPointerNormal = new LoadItem () ->
  @obj.pointer_normal = new CoffeeGL.Texture("../models/label_NRM.jpg",{unit : 1}, () => 
    @loaded()
  )
  @

_loadBaseNormal = new LoadItem () ->
  @obj.base_normal = new CoffeeGL.Texture("../models/base_texture_NRM.jpg",{unit : 1}, () => 
    @loaded()
  )
  @

_loadBackingShader = new LoadItem () ->
  r = new CoffeeGL.Request('../shaders/background.glsl')
  r.get (data) =>
    @obj.shader_background = new CoffeeGL.Shader(data)
    @loaded()
  @

_loadDepthShader = new LoadItem () ->
  r = new CoffeeGL.Request('../shaders/depth.glsl')
  r.get (data) =>
    @obj.shader_depth = new CoffeeGL.Shader(data)
    @loaded()
  @

_loadSSAOShader = new LoadItem () ->
  r = new CoffeeGL.Request('../shaders/ssao.glsl')

  r.get (data) =>
    @obj.shader_ssao = new CoffeeGL.Shader(data,  {
      "uSampler" : "uSampler"
      "uSamplerDepth" : "uSamplerDepth"
      "uRenderedTextureWidth" : "uRenderedTextureWidth"
      "uRenderedTextureHeight" : "uRenderedTextureHeight"
      })
    
    @loaded()
  @

_loadFXAAShader = new LoadItem () ->
  r = new CoffeeGL.Request('../shaders/fxaa.glsl')
  r.get (data) =>
    @obj.shader_fxaa = new CoffeeGL.Shader(data, {"uViewportSize" : "viewportSize"})
    @loaded()
  @


_loadNeedleModel = new LoadItem () ->
  r = new CoffeeGL.Request('../models/needle.js')
  r.get (data) =>
    @obj.needle_model = new CoffeeGL.JSONModel(data,{ onLoad : ()=>  
         @loaded()
      })
    @
  @ 

_loadNeedleNormal = new LoadItem () ->
  @obj.needle_normal = new CoffeeGL.Texture("../models/steel_NRM.jpg",{unit : 1}, () => 
    @loaded()
  )
  @


loadAssets = (obj, signal, signal_progress) ->
  
  a = () -> 
    signal_progress.dispatch(@completed_items.length / @items.length)

  b = () ->
    signal.dispatch()

  lq = new LoadQueue obj, a, b

  lq.add _loadLighting
  lq.add _loadModel
  lq.add _loadBasic
  lq.add _loadPicking
  lq.add _loadAniso
  lq.add _loadEpicycleNormal
  lq.add _loadPlateNormal
  lq.add _loadRimNormal
  lq.add _loadPointerNormal
  lq.add _loadBaseNormal
  lq.add _loadBackingShader
  lq.add _loadStringShader
  lq.add _loadFXAAShader
  lq.add _loadNeedleModel
  lq.add _loadNeedleNormal
  lq.add _loadSSAOShader
  lq.add _loadDepthShader

  lq.start()
  @


module.exports = 
  loadAssets : loadAssets