###
                       __  .__              ________ 
   ______ ____   _____/  |_|__| ____   ____/   __   \
  /  ___// __ \_/ ___\   __\  |/  _ \ /    \____    /
  \___ \\  ___/\  \___|  | |  (  <_> )   |  \ /    / 
 /____  >\___  >\___  >__| |__|\____/|___|  //____/  .co.uk
      \/     \/     \/                    \/         
                                              Equatorie
                                              Benjamin Blundell - ben@section9.co.uk
                                              http://www.section9.co.uk

###

{CoffeeGL} = require '../lib/coffeegl/coffeegl'
{EquatorieSystem} = require './system'
{EquatorieString} = require './string'
{loadAssets} = require './load'
{EquatorieInteract} = require './interact'

class Equatorie

  init : () =>

    f = () =>
      w = $(window).width();
      h = $(window).height();

      $("#webgl-canvas").attr("width", w)
      $("#webgl-canvas").attr("height", h)

      $("#webgl-canvas").width(w)
      $("#webgl-canvas").height(h)

      cgl.resize(w,h)
      eq.resize(w,h)

    $(window).bind("resize", f)
    $(window).bind("ready", f)
  
    # All nodes to be drawn in colour
    @top_node = new CoffeeGL.Node()

    # All nodes to be depth tested
    @depth_node = new CoffeeGL.Node()

    @string_height = 0.2
    @string_nodes = new CoffeeGL.Node()

    # Nodes for the picking
    @pickable = new CoffeeGL.Node()
    @fbo_picking = new CoffeeGL.Fbo()
    @ray = new CoffeeGL.Vec3(0,0,0)

    @fbo_fxaa = new CoffeeGL.Fbo()
    if not CoffeeGL.Context.profile.mobile
      @fbo_depth = new CoffeeGL.Fbo()
  
    @advance_date = 0

    # Nodes being drawn with the basic shader
    @basic_nodes = new CoffeeGL.Node()

    # Mouse position
    @mp = new CoffeeGL.Vec2(-1,-1)
  
    @system = new EquatorieSystem()

    # Marker - passed to interact. A general use thing represented by a needle

    @marker = new CoffeeGL.Node()
    @marker.descend = false

    @ready = false

    # Loaded signal - export to JQuery / Bootstrap Also
    @loaded = new CoffeeGL.Signal()
    @load_progress = new CoffeeGL.Signal()
    window.EquatorieLoaded = @loaded if window?
    window.EquatorieLoadProgress = @load_progress if window?

   
    # Our basic marker / pin - this is part of our interaction
    cube = new CoffeeGL.Shapes.Cuboid new CoffeeGL.Vec3 0.2,0.2,0.2
    sphere = new CoffeeGL.Node new CoffeeGL.Shapes.Sphere 0.15, 12
    cube_thin = new CoffeeGL.Node new CoffeeGL.Shapes.Cuboid new CoffeeGL.Vec3 0.01,0.5,0.01

    cube_thin.matrix.translate new CoffeeGL.Vec3 0,-0.2,0
    sphere.matrix.translate new CoffeeGL.Vec3 0,0.1,0
  

    # Add Strings
    @white_string = new EquatorieString 8, 0.03, 20
    @black_string = new EquatorieString 8, 0.03, 20

    @pin = new CoffeeGL.Node()
    @pin.add sphere

    # Each start has a pin for pickable and a model for drawing.
    # The matrix is set in the top-most node and replaces the two below, thus is shared

    @white_start = new CoffeeGL.Node
    tp = @pin.copy()
    tp.matrix = @white_start.matrix
    @white_start.add tp
    @pickable.add tp

    @white_start.matrix.translate new CoffeeGL.Vec3 2,@string_height,2
    tp.uPickingColour = new CoffeeGL.Colour.RGBA(1.0,0.0,0.0,1.0)

    @white_end = new CoffeeGL.Node
    tp = @pin.copy()
    tp.matrix = @white_end.matrix
    @white_end.add tp
    @pickable.add tp
    @white_end.matrix.translate new CoffeeGL.Vec3 -2,@string_height,-2
    tp.uPickingColour = new CoffeeGL.Colour.RGBA(0.0,1.0,0.0,1.0)

    @black_start = new CoffeeGL.Node
    tp = @pin.copy()
    tp.matrix = @black_start.matrix
    @black_start.add tp
    @pickable.add tp
    @black_start.matrix.translate new CoffeeGL.Vec3 -2,@string_height,2
    tp.uPickingColour = new CoffeeGL.Colour.RGBA(0.0,0.0,1.0,1.0)

    @black_end = new CoffeeGL.Node
    tp = @pin.copy()
    tp.matrix = @black_end.matrix
    @black_end.add tp
    @pickable.add tp
    @black_end.matrix.translate new CoffeeGL.Vec3 -4,@string_height,2
    tp.uPickingColour = new CoffeeGL.Colour.RGBA(1.0,1.0,1.0,1.0)

    @string_nodes.add(@white_string).add(@black_string)

  
    @white_string.uColour = new CoffeeGL.Colour.RGBA(0.9,0.9,0.9,1.0)
    @black_string.uColour = new CoffeeGL.Colour.RGBA(0.1,0.1,0.1,1.0)
    @white_start.uColour = new CoffeeGL.Colour.RGBA(0.9,0.2,0.2,0.8)
    @white_end.uColour = new CoffeeGL.Colour.RGBA(0.2,0.2,0.9,0.8)
    @black_start.uColour = new CoffeeGL.Colour.RGBA(0.9,0.2,0.2,0.8)
    @black_end.uColour = new CoffeeGL.Colour.RGBA(0.2,0.2,0.9,0.8)

    # Cameras

    @c = new CoffeeGL.Camera.TouchPerspCamera new CoffeeGL.Vec3(0,0,1), new CoffeeGL.Vec3(0,0,0), new CoffeeGL.Vec3(0,1.0,0.0), 55.0, 0.01, 30.0, 0.1
    @c.zoom_near = 0.8
    @c.zoom_far = 18.0
    @c.orbit new CoffeeGL.Vec3(1,0,0), CoffeeGL.degToRad -25

    @o = new CoffeeGL.Camera.OrthoCamera new CoffeeGL.Vec3(0,0,0.1)
    # Function called when everything is loaded
    f = () =>

      #console.log "Loaded Assets"

      @top_node.add @basic_nodes
      @basic_nodes.shader = @shader_basic   
   
      # Backing Node

      @backing = new CoffeeGL.Node new CoffeeGL.Quad()
      #@backing.shader = @shader_background

  

      @string_nodes.shader = @shader_string

      #@top_node.add @string_nodes

      # Should be five children nodes with this model. Attach the shaders
      
      @base     = @equatorie_model.children[4]
      @epicycle = @equatorie_model.children[1]
      @pointer  = @equatorie_model.children[0]
      @rim      = @equatorie_model.children[3]
      @plate    = @equatorie_model.children[2]

      # Create the node for shiny ansio things
      @shiny =  new CoffeeGL.Node()
      @top_node.add @shiny

      # Add the Equatorie Model to render to the depth node
      
      @depth_node.add @epicycle
      @depth_node.add @rim
      @depth_node.add @plate
      @depth_node.add @base

      @depth_node.add  @shader_depth

      # Create the tangents
      @_setTangents @pointer.geometry
      @_setTangents @epicycle.geometry
      @_setTangents @rim.geometry
      @_setTangents @plate.geometry
      @_setTangents @base.geometry

      # Setup our needle model
      for child in @needle_model.children
        @_setTangents child.geometry

      @marker.add @needle_model

      @needle_model.matrix.translate(new CoffeeGL.Vec3(0,-@string_height,0))

      @shiny_needles = new CoffeeGL.Node()

      @shiny_needles.uAmbientLightingColor = new CoffeeGL.Colour.RGBA(0.01,0.01,0.01,1.0)
      @shiny_needles.uSpecColour = new CoffeeGL.Colour.RGBA(0.8,0.8,0.8,1.0)
      @shiny_needles.uAlphaX = 0.1
      @shiny_needles.uAlphaY = 0.5
      @shiny_needles.add @needle_normal
      @shiny_needles.uSamplerNormal = 1
      @shiny_needles.add @shader_aniso

      @shiny_needles.add @marker

      # Add the needle to our strings

      for x in [@white_start,@white_end,@black_start,@black_end]
        tn = new CoffeeGL.Node()
        tn.add @needle_model
        tn.matrix = x.matrix
        x.add tn
        @shiny_needles.add tn
   

      @top_node.add @shiny_needles

      @shiny.shader = @shader_aniso
      @shiny.add @epicycle
      @shiny.add @rim
      @shiny.add @plate
      @shiny.add @base

      # Add the normal textures
      @pointer.add @pointer_normal
      @rim.add @rim_normal
      @plate.add @plate_normal
      @epicycle.add @epicycle_normal
      @base.add @base_normal

      @shiny.uSamplerNormal = 1 # set for the first texture unit
    
      @base.uAmbientLightingColor = new CoffeeGL.Colour.RGBA(1.0,1.0,0.8,1.0)
      @base.uSpecColour = new CoffeeGL.Colour.RGBA(0.5,0.5,0.5,1.0)
      @base.uAlphaX = 0.05
      @base.uAlphaY = 0.05

      @epicycle.uAmbientLightingColor = new CoffeeGL.Colour.RGBA(0.1,0.1,0.1,1.0)
      @epicycle.uSpecColour = new CoffeeGL.Colour.RGBA(1.0,0.9,0.8,1.0)
      @epicycle.uAlphaX = 0.4
      @epicycle.uAlphaY = 0.28

      @pointer.uAmbientLightingColor = new CoffeeGL.Colour.RGBA(0.1,0.1,0.1,1.0)
      @pointer.uSpecColour = new CoffeeGL.Colour.RGBA(1.0,0.9,0.9,1.0)
      @pointer.uAlphaX = 0.2
      @pointer.uAlphaY = 0.1

      @epicycle.uPickingColour = new CoffeeGL.Colour.RGBA 1.0,1.0,0.0,1.0
      @pointer.uPickingColour = new CoffeeGL.Colour.RGBA 0.0,1.0,1.0,1.0
      @pickable.add @epicycle

      # Remove the pointer and add it as a child of the Epicycle
      @equatorie_model.remove @pointer
      @epicycle.add @pointer

      # Rotate the Base so the Sign of Aries is in the right place
      q = new CoffeeGL.Quaternion()
      q.fromAxisAngle(new CoffeeGL.Vec3(0,1,0), CoffeeGL.degToRad(-90.0))

      @base.matrix.mult q.getMatrix4() 
        
      # Set the pickable shader for the pickables
      @pickable.shader = @shader_picker
      @top_node.add @basic_nodes
      @basic_nodes.shader = @shader_basic

      # Launch physics web worker
      @physics = new Worker '/js/physics.js'
      @physics.onmessage = @onPhysicsEvent
      @physics.postMessage { cmd: "startup" }

      # Fire up the interaction class that takes over behaviour from here
      # This class takes quite a lot of objects so its not ideal
      @interact = new EquatorieInteract(@system, @physics, @c, @white_start, @white_end, @black_start, @black_end, @epicycle, @pointer, @marker, @plate, @string_height )
      @interact.setDate new Date "December 31, 1392 12:00:00"

      window.Equatorie = eq.interact if window? # Export the interaction part for JQuery / Bootstrap interaction

      # Register for mouse events
      CoffeeGL.Context.mouseDown.add @interact.onMouseDown, @interact
      CoffeeGL.Context.mouseOver.add @interact.onMouseOver, @interact
      CoffeeGL.Context.mouseOut.add @interact.onMouseOut, @interact
      CoffeeGL.Context.mouseMove.add @interact.onMouseMove, @interact
      CoffeeGL.Context.mouseUp.add @interact.onMouseUp, @interact
      CoffeeGL.Context.mouseWheel.add @interact.onMouseWheel, @interact

      CoffeeGL.Context.touchSpread.add @interact.onTouchSpread, @interact
      CoffeeGL.Context.touchPinch.add @interact.onTouchPinch, @interact

      CoffeeGL.Context.mouseOver.add @onMouseOver, @
      CoffeeGL.Context.mouseOut.add @onMouseOut, @
      CoffeeGL.Context.mouseMove.add @onMouseMove, @

      # Remove the camera mouse event - we want control over that
      CoffeeGL.Context.mouseMove.del @c.onMouseMove, @c
      CoffeeGL.Context.mouseDown.del @c.onMouseDown, @c

      # FXAA/SSAO Shader

      @screen_quad = new CoffeeGL.Node new CoffeeGL.Quad()
      @screen_quad.viewportSize = new CoffeeGL.Vec2 CoffeeGL.Context.width, CoffeeGL.Context.height 
      @screen_quad.uRenderedTextureWidth = CoffeeGL.Context.width
      @screen_quad.uRenderedTextureHeight =  CoffeeGL.Context.height
      @screen_quad.uSampler = 0
      @screen_quad.uSamplerDepth = 1
      @screen_quad.add @o


      @ready = true

  
    # Fire off the loader with the signal
    @loaded.addOnce f, @
    loadAssets @, @loaded, @load_progress

    # Use today for the apsides precession

    date = new Date()
    
    @top_node.add @c
    @pickable.add @c
    @depth_node.add @c
    @string_nodes.add @c

    # Lights

    @light = new CoffeeGL.Light.PointLight(new CoffeeGL.Vec3(0.0,5.0,15.0), new CoffeeGL.Colour.RGB(1.0,1.0,1.0) );
    @light2 = new CoffeeGL.Light.PointLight(new CoffeeGL.Vec3(0.0,-5.0,20.0), new CoffeeGL.Colour.RGB(0.9,1.0,1.0) );
    @light3 = new CoffeeGL.Light.PointLight(new CoffeeGL.Vec3(-1.0,5.0,-8.0), new CoffeeGL.Colour.RGB(0.9,1.0,1.0) );


    @top_node.add(@light)
    @top_node.add(@light2)
    @top_node.add(@light3)

    # OpenGL Constants
    GL.enable(GL.CULL_FACE)
    GL.cullFace(GL.BACK)
    GL.enable(GL.DEPTH_TEST)

    @c.zoom 0.25 

  eotw : () ->
   
    if @interact.date.getTime() == 1356091200000
      if not @_eotw
        alert "ITS THE END OF THE WORLD! Any calculations made today are not guaranteed accurate!"
      @_eotw = true
      @shader_background.setUniform3f "uColour", 1.0,0.0,0.0
      

    else
      @_eotw = false
      @shader_background.setUniform3f "uColour", 1.0,1.0,1.0


  update : (dt) =>
  
    #date = new Date("May 31, 1585 00:00:00")
    if not @ready
      return

    @interact.update(dt)

   

    @

  # update the physics - each body in the string needs to have its position and orientation updated
  updatePhysics : (data) ->
    @white_string.update data.white
    @black_string.update data.black

  
  onPhysicsEvent : (event) =>
    switch event.data.cmd
      when "physics" then @updatePhysics event.data.data
      when "ping" then console.log "Physics Ping: " + event.data.data
      else break


  draw : () =>

    GL.clearColor(0.15, 0.15, 0.15, 1.0)
    GL.clear(GL.COLOR_BUFFER_BIT | GL.DEPTH_BUFFER_BIT)

    @c.update CoffeeGL.Context.width, CoffeeGL.Context.height
    @o.update CoffeeGL.Context.width, CoffeeGL.Context.height

    if not @ready
      return

    # Draw to the Colour Buffer
    @fbo_fxaa.bind()
    @fbo_fxaa.clear()
    GL.disable GL.DEPTH_TEST
    @shader_background.bind()
    @eotw()
    @backing.draw()
    @shader_background.unbind()
    
    GL.enable GL.DEPTH_TEST
    @top_node.draw()
    if CoffeeGL.Context.profile.mobile
      GL.disable GL.DEPTH_TEST
    GL.disable GL.CULL_FACE
    @string_nodes.draw()
    GL.enable GL.CULL_FACE
    if CoffeeGL.Context.profile.mobile
      GL.enable GL.DEPTH_TEST

    @fbo_fxaa.unbind()

    # Draw to the depth fbo
    if not CoffeeGL.Context.profile.mobile
      @fbo_depth.bind()
      @fbo_depth.clear()
      @depth_node.draw()
      @fbo_depth.unbind()

    # Draw everything pickable to the pickable FBO
   
    @fbo_picking.bind()
    @fbo_picking.clear()
    @shader_picker.bind()
    @pickable.draw()
  
    # Cx for picking
    if @mp.y != -1 and @mp.x != -1
      pixel = new Uint8Array(4);
      GL.readPixels(@mp.x, @fbo_picking.height - @mp.y, 1, 1, GL.RGBA, GL.UNSIGNED_BYTE, pixel)

      @interact.checkPicked pixel
  
    @shader_picker.unbind()
    @fbo_picking.unbind()

    # Now draw the screen space effects - temporarily cancelled to test other machines
    if CoffeeGL.Context.profile.mobile or true
      # FXAA
      @fbo_fxaa.texture.bind()
      @shader_fxaa.bind()
      @screen_quad.draw()
      @shader_fxaa.unbind()
      @fbo_fxaa.texture.unbind()

    else

      # SSAO
      @fbo_depth.texture.unit = 1
      @fbo_fxaa.bind()

      @fbo_fxaa.texture.bind()
      @fbo_depth.texture.bind()

      @shader_ssao.bind()
      @shader_ssao.setUniform1f "uNearPlane", @c.near
      @shader_ssao.setUniform1f "uFarPlane", @c.far
      
      @screen_quad.draw()
      @shader_ssao.unbind()
      
      @fbo_depth.texture.unbind()
      @fbo_fxaa.texture.unbind()

      @fbo_fxaa.unbind()

      # FXAA

      @fbo_fxaa.texture.bind()
      @shader_fxaa.bind()
      @screen_quad.draw()
      @shader_fxaa.unbind()
      @fbo_fxaa.texture.unbind()



  onMouseMove : (event) ->
    @mp.x = event.mouseX
    @mp.y = event.mouseY


  onMouseOver : (event) ->    
    @mp.x = event.mouseX
    @mp.y = event.mouseY

  onMouseOut : (event) ->
    @mp.x = -1
    @mp.y = -1


  resize : (w,h) ->
    @fbo_picking.resize w,h
    @fbo_fxaa.resize w,h
    if not CoffeeGL.Context.profile.mobile
      @fbo_depth.resize w,h
    if @screen_quad?
      @screen_quad.viewportSize.x = w
      @screen_quad.viewportSize.y = h
      @screen_quad.uRenderedTextureWidth = w
      @screen_quad.uRenderedTextureHeight =  h

  _setTangents : (geom) ->
    for face in geom.faces
      [a,b,c] = CoffeeGL.precomputeTangent face.v[0].p, face.v[1].p, face.v[2].p, 
        face.v[0].n, face.v[1].n, face.v[2].n, face.v[0].t, face.v[1].t, face.v[2].t

      face.v[0].tangent = a
      face.v[1].tangent = b
      face.v[2].tangent = c
    @
  
eq = new Equatorie()
cgl = new CoffeeGL.App('webgl-canvas', eq, eq.init, eq.draw, eq.update, window.notSupported)





