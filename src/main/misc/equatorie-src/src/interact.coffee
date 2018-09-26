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


# All the interaction text
eq_text = {}

eq_text["date_init"] = 'Before using the equatorium, the astronomer would first work out how many years and days had passed since 31 December 1392, the "radix" (baseline date) of the tables in the manuscript. They would consult the annual and daily tables to find the planet’s mean longitude and mean anomaly for the desired date.'
eq_text["date_init_sun"] = 'Before using the equatorium, the astronomer would first work out how many years and days had passed since 31 December 1392, the "radix" (baseline date) of the tables in the manuscript. They would consult the annual and daily tables to find the Sun’s mean longitude for the desired date.'
eq_text["date_init_latitude"] = 'Before using the equatorium, the astronomer would first use the tables in the manuscript to work out the Moon’s true motus and the true motus of Caput Draconis (the lunar node).  The latter should be subtracted from the former.'
eq_text["rotate_plate"] = "The circular brass plate stores information about the planets' apogees. This must be rotated to account for changes in the apogees (owing to precession) since 1392."
eq_text["rotate_plate_sun"] = "The circular brass plate stores information about the Sun’s apogee. This rotates to account for changes in the apogees (owing to precession) since 1392."
eq_text["mean_motus"] = "Use the annual and daily tables to find the planet’s mean longitude ( %MM ) and mean anomaly (%MA) for the date you want."
eq_text["mean_motus_moon"] = "Use the annual and daily tables to find the Moon's mean longitude ( %MM ) and mean anomaly (%MA) for the date you want."
eq_text["mean_motus_latitude"] = "To begin using the equatorium, first use the tables in the manuscript to work out the Moon’s true motus and the true motus of Caput Draconis (the lunar node).  Subtract the latter from the former." 
eq_text["mean_motus_sun"] = "Use the annual and daily tables to find the Sun’s mean longitude ( %MM) for the date you want."
eq_text["black_thread"] = "The black thread is moved so it runs from the Earth (centre of the disc) to the mean longitude (%MM) the scale on the disc runs anti-clockwise)."
eq_text["white_thread"] = "The white thread is moved so it is parallel to the black thread, with one end fixed at the planet’s equant point."
eq_text["white_thread_mercury"] = "The white thread is moved so it is parallel to the black thread, with one end fixed at the planet’s equant point. Unlike the other planets, Mercury’s equant point is not fixed in the direction of its apogee, but moves on a little circle that is marked on the brass plate."
eq_text["white_thread_moon"] = "The white thread is moved so it runs from the Moon’s equant point (which is opposite the deferent centre on the little circle), over the centre of the epicycle."
eq_text["white_thread_sun"] = "The white thread is moved so it is parallel to the black thread, with one end fixed at the centre of the Sun’s eccentric circle.  Unlike the planets’ equant circles, the Sun’s eccentric circle is marked in full on the disc."
eq_text["black_thread_sun"] = "The black thread is moved so it runs from the Earth, through the point where the white thread cuts the Sun's eccentric circle, to the scale on the disc. The true longitude can be read there: it is "
eq_text["epicycle"] = 'The brass epicycle is moved and fixed to the planet’s deferent centre with a pin through its "common deferent centre".'
eq_text["epicycle_moon"] ='The brass epicycle is moved and fixed to the Moon\'s deferent centre with a pin through its "common deferent centre". Unlike the planets, the Moon has a deferent centre which moves around the little circle marked near the rim of the brass plate.'
eq_text["epicycle_align"] =  "The epicycle is rotated until its centre is under the white thread."
eq_text["epicycle_align_moon"] = "The epicycle is rotated until its centre is over the black thread."
eq_text["mean_aux"] = 'The "label" (pointer) is aligned with the white thread.'
eq_text["label"] = "Taking this position as zero, the label is turned anti-clockwise to mark out the mean anomaly ( %MA )"
eq_text["label_moon"] = "Taking this position as zero, turn the label to mark out the mean anomaly ( %MA ).  Note that unlike for the planets, the label should be rotated clockwise."
eq_text["black_final"] = "The black thread is moved so it runs from the Earth, through the the planet’s mark on the label, to the scale on the disc.  The true longitude can be read there: it is " 
eq_text["black_final_moon"] = "The black thread is moved so it runs from the Earth, through the the Moon's mark on the label, to the scale on the disc.  The true longitude can be read there: it is "
eq_text["alhudda"] = "The white thread is moved so it is perpendicular to the graduated “Alhudda” line on the disc, and crosses the black thread where it cuts the scale of degrees."
eq_text["black_latitude"] = "The black thread is moved so it runs from the Earth (centre of the disc) to the value found in the last step."
eq_text["read_latitude"] = "The Moon's latitude can be read on the 0-5° scale marked on the Alhudda line: it is "

# The state of the Equatorie at present and the next stage for it to go to
class EquatorieState

  constructor : (@_activate, @func, @duration=3) ->
    if not @duration?
      @duration = 3.0

  # dt being 0 - 1 progress for this bit
  update : (dt) ->
    @func(dt)

  # called when activated
  activate : () ->
     @_activate() if @_activate?


class EquatorieInteract
  constructor : (@system, @physics, @camera, @white_start, @white_end, @black_start, @black_end, @epicycle, @pointer, @marker, @plate, @string_height ) ->

    # Mouse positions
    @mp = new CoffeeGL.Vec2(-1,-1)
    @mpp = new CoffeeGL.Vec2(-1,-1)
    @mpd = new CoffeeGL.Vec2(0,0)

    @picked = false
    @picked_item = undefined
    @dragging = false

    @advance_date  = 0

    # signal for moving the point of interest
    @move_poi = new CoffeeGL.Signal()
    window.EquatorieMovePOI = @move_poi if window?

    # signal for setting the zoom control
    @zoom_signal = new CoffeeGL.Signal()
    window.EquatorieZoomSignal = @zoom_signal if window?

    @stack = []     # A stack for the states of the system
    @stack_idx = 0  # current stack position

    #@_initGUI()

    @time = 
      start : 0
      dt : 0

    Date.prototype.addHours = (h) ->
      @setHours(@getHours()+h)
      return @

  # Takes the current system dt in milliseconds. Can run backwards too!
  # At present the user cant move the items around but we should make that
  # much more explicit. 


  update : (dt) ->
    
    if @stack.length > 0
      if @time.dt / 1000 > @stack[@stack_idx].duration
        @stack[@stack_idx].update(1.0) # Really we could just stop here?
        return
      else if @time.dt < 0
        @stack[@stack_idx].update(0.0)
        return
      else
        # 0 - 1 da progression for stack update
        da = (@time.dt / 1000) / @stack[@stack_idx].duration
        @stack[@stack_idx].update(da)
        @time.dt += dt

    @time.start = new Date().getTime()


  _setPOI : (node) ->
    i0 = node.matrix
    i1 = @camera.m
    i2 = @camera.p.copy()

    vt = new CoffeeGL.Vec3 0,0,0.1
    i2.mult(i1).mult(i0)
    i2.multVec vt

    new CoffeeGL.Vec2 (vt.x+1) / 2 * CoffeeGL.Context.width, CoffeeGL.Context.height - ((vt.y+1) / 2 * CoffeeGL.Context.height)

  _setPOIVec : (vp) ->

    i0 = new CoffeeGL.Matrix4()
    i0.setPos vp
    i1 = @camera.m
    i2 = @camera.p.copy()

    vt = new CoffeeGL.Vec3 0,0,0.1
    i2.mult(i1).mult(i0)
    i2.multVec vt

    new CoffeeGL.Vec2 (vt.x+1) / 2 * CoffeeGL.Context.width, CoffeeGL.Context.height - ((vt.y+1) / 2 * CoffeeGL.Context.height)


  # A set of potential functions for moving parts of the Equatorie around
  
  _stateSetPlanetDateInit : () =>
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["date_init"]
    if @chosen_planet == "sun"
      current_state.text = @_replaceText eq_text["date_init_sun"] 
    else if @chosen_planet == "moon_latitude"
       current_state.text = @_replaceText eq_text["date_init_latitude"]

    current_state.pos = @_setPOI @epicycle
    @marker.descend = false
    @


  # Does some simple changing of text for the angles in the system
  _replaceText : (text) ->
    ta =  @system.state.pointerAngle
    if ta < 1
      ta *= -1
    t = text.replace /%MA/, ta.toFixed(2)
    if @chosen_planet == "sun"
      ta =  @system.state.sunMeanMotus
      if ta < 1
        ta *= -1
      t = t.replace /%MM/, ta.toFixed(2)
    else
      ta =  @system.state.meanMotus
      if ta < 1
        ta *= -1
      t = t.replace /%MM/, ta.toFixed(2)
    t

  _stateSetPlanetDate : (planet, date) =>
    @system.solveForPlanetDate(planet,date)
    @

  _stateRotatePlateInit : () =>
    # Rotate the centre plate by the precession 
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["rotate_plate"]
    
    if @chosen_planet == "sun"
      current_state.text = @_replaceText eq_text["rotate_plate_sun"]
    
    @marker.descend = false

    current_state.pos = @_setPOI @plate
    @plate.matrix.identity()
    current_state.plate_interp = new CoffeeGL.Interpolation 0, CoffeeGL.degToRad(@system.precession * @system.state.passed)

    
  _stateRotatePlate : (dt) =>
    current_state = @stack[@stack_idx]
    current_state.pos = @_setPOI @plate
    @move_poi.dispatch current_state.pos

    @plate.matrix.identity()
    @plate.matrix.rotate new CoffeeGL.Vec3(0,1,0), current_state.plate_interp.set dt

  
  _stateCalculateMeanMotusInit : () =>
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["mean_motus"]
    if @chosen_planet == "moon"
      current_state.text = @_replaceText eq_text["mean_motus_moon"]
    if @chosen_planet == "moon_latitude"
      current_state.text = @_replaceText eq_text["mean_motus_latitude"]
    if @chosen_planet == "sun"
      current_state.text = @_replaceText eq_text["mean_motus_sun"]
    current_state.pos = @_setPOI @epicycle
    @marker.descend = false
    @


  _stateCalculateMeanMotus : (dt) =>
    current_state = @stack[@stack_idx]
    current_state.pos = @_setPOI @epicycle
    @move_poi.dispatch current_state.pos
    @
  
  
  _stateMoveBlackThreadInit : () =>

    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["black_thread"]
    current_state.pos = @_setPOI(@black_end)
    mv = @system.state.meanMotusPosition.copy()
    mv.normalize()
    mv.multScalar(8.0)
    current_state.end_interp = new CoffeeGL.Interpolation @black_end.matrix.getPos(), new CoffeeGL.Vec3(mv.x, @string_height, mv.y) 
    current_state.start_interp = new CoffeeGL.Interpolation @black_start.matrix.getPos(), new CoffeeGL.Vec3(0, @string_height, 0) 
    @marker.descend = false

  _stateMoveBlackThread : (dt) =>
    # Black to Centre and mean motus  
    # Hold an interpolation object on the state in question

    current_state = @stack[@stack_idx]
    @black_start.matrix.setPos current_state.start_interp.set dt
    @black_end.matrix.setPos current_state.end_interp.set dt

    @physics.postMessage {cmd : "black_start_move", data: @black_start.matrix.getPos() }
    @physics.postMessage {cmd : "black_end_move", data: @black_end.matrix.getPos() }

    current_state.pos = @_setPOI @black_end
    @move_poi.dispatch current_state.pos

    @

  _stateMoveWhiteThreadInit : () =>

    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["white_thread"]

    if @chosen_planet == "mercury"
      current_state.text = eq_text["white_thread_mercury"]
    eq = @system.state.equantPosition
    pv = @system.state.parallelPosition
    pv.sub(eq)
    pv.normalize()
    pv.multScalar(8.0)
    pv.add(eq)

    current_state.end_interp = new CoffeeGL.Interpolation @white_end.matrix.getPos(), new CoffeeGL.Vec3(pv.x, @string_height, pv.y) 

    eq = @system.state.equantPosition
    current_state.start_interp = new CoffeeGL.Interpolation @white_start.matrix.getPos(), new CoffeeGL.Vec3(eq.x,@string_height,eq.y) 
    @marker.descend = false

  _stateMoveWhiteThread : (dt) =>
  
    current_state = @stack[@stack_idx]
    @white_start.matrix.setPos current_state.start_interp.set dt
    @white_end.matrix.setPos current_state.end_interp.set dt
  
    @physics.postMessage {cmd : "white_start_move", data: @white_start.matrix.getPos() }
    @physics.postMessage {cmd : "white_end_move", data: @white_end.matrix.getPos() }

    current_state.pos = @_setPOI @white_end
    @move_poi.dispatch current_state.pos


  _stateMoveWhiteThreadMoonInit : () =>

    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["white_thread_moon"]
    pv = @system.state.epicyclePosition.copy()
    pv.sub @system.state.equantPosition
    pv.normalize()
    pv.multScalar(8.0)
    pv.add @system.state.equantPosition
    pv = new CoffeeGL.Vec3 pv.x,@string_height,pv.y

    current_state.end_interp = new CoffeeGL.Interpolation @white_end.matrix.getPos(), pv

    eq = @system.state.equantPosition
    current_state.start_interp = new CoffeeGL.Interpolation @white_start.matrix.getPos(), new CoffeeGL.Vec3(eq.x,@string_height,eq.y) 

  _stateMoveWhiteThreadMoon : (dt) =>
  
    current_state = @stack[@stack_idx]

    @white_start.matrix.setPos current_state.start_interp.set dt
    @white_end.matrix.setPos current_state.end_interp.set dt
  
    @physics.postMessage {cmd : "white_start_move", data: @white_start.matrix.getPos() }
    @physics.postMessage {cmd : "white_end_move", data: @white_end.matrix.getPos() }

    current_state.pos = @_setPOI @white_end
    @move_poi.dispatch current_state.pos

    @

  _stateMoveWhiteThreadSunInit : () =>
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["white_thread_sun"]
    ev = new CoffeeGL.Vec3 @system.state.equantPosition.x, @string_height, @system.state.equantPosition.y
    current_state.end_interp = new CoffeeGL.Interpolation @white_end.matrix.getPos(), ev

    pv = @system.state.parallelPosition.copy()
    pv.sub @system.state.equantPosition
    pv.normalize()
    pv.multScalar(8.0)
    pv.add @system.state.equantPosition
    pv = new CoffeeGL.Vec3 pv.x,@string_height,pv.y

    current_state.start_interp = new CoffeeGL.Interpolation @white_start.matrix.getPos(), pv


  _stateMoveWhiteThreadSun : (dt) =>

    current_state = @stack[@stack_idx]

    current_state.pos = @_setPOI  @white_end
    @move_poi.dispatch current_state.pos

    @white_start.matrix.setPos current_state.start_interp.set dt
    @white_end.matrix.setPos current_state.end_interp.set dt
    @physics.postMessage {cmd : "white_start_move", data: @white_start.matrix.getPos() }
    @physics.postMessage {cmd : "white_end_move", data: @white_end.matrix.getPos() }

    @


  _stateMoveBlackThreadSunInit : () =>
  
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["black_thread_sun"] + @system.state.truePlace.toFixed(2)  + "."
    current_state.pos = @_setPOIVec new CoffeeGL.Vec3 @system.state.sunCirclePoint.x, 0, @system.state.sunCirclePoint.y

    pv = @system.state.sunCirclePoint.copy()
    pv.normalize()
    pv.multScalar(8.0)
    pv = new CoffeeGL.Vec3 pv.x,@string_height,pv.y
    current_state.end_interp = new CoffeeGL.Interpolation @white_start.matrix.getPos(), pv


  _stateMoveBlackThreadSun : (dt) =>
    current_state = @stack[@stack_idx]
    @black_end.matrix.setPos current_state.end_interp.set dt
    current_state.pos = @_setPOIVec new CoffeeGL.Vec3 @system.state.sunCirclePoint.x, 0, @system.state.sunCirclePoint.y
    @move_poi.dispatch current_state.pos

    @physics.postMessage {cmd : "black_end_move", data: @black_end.matrix.getPos() }
    @

  
  _stateMoveEpicycleInit : () =>
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["epicycle"]

    if @chosen_planet == "moon"
          current_state.text = @_replaceText eq_text["epicycle_moon"]
    d = @system.state.deferentPosition
    c = @system.state.basePosition
    v = @system.state.parallelPosition
    dr = @system.state.deferentAngle
    if @chosen_planet == "mercury"
      dr = @system.state.mercuryDeferentAngle
    e = @system.state.epicyclePrePosition
    
    current_state.pos_interp = new CoffeeGL.Interpolation @epicycle.matrix.getPos(), new CoffeeGL.Vec3 e.x,0,e.y
    current_state.rot_interp = new CoffeeGL.Interpolation 0, -90-dr

    @marker.descend = true

  _stateMoveEpicycle : (dt) =>

    current_state = @stack[@stack_idx]

    @epicycle.matrix.identity()
    @epicycle.matrix.translate current_state.pos_interp.set dt
    @epicycle.matrix.rotate new CoffeeGL.Vec3(0,1,0), CoffeeGL.degToRad current_state.rot_interp.set dt
    
    @marker.matrix.identity()

    if @chosen_planet == "mercury"
      @marker.matrix.translate(new CoffeeGL.Vec3(@system.state.mercuryDeferentPosition.x,@string_height,@system.state.mercuryDeferentPosition.y))
    else if @chosen_planet in ["mars","venus","jupiter","saturn","moon"]
      @marker.matrix.translate(new CoffeeGL.Vec3(@system.state.deferentPosition.x,@string_height,@system.state.deferentPosition.y))
    
    current_state.pos = @_setPOI @marker
    @move_poi.dispatch current_state.pos

    @

  _stateRotateEpicycleInit : () =>
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["epicycle_align"]
    
    if @chosen_planet == "moon"
      current_state.text = @_replaceText eq_text["epicycle_align_moon"]

    current_state.rot_interp = new CoffeeGL.Interpolation 0, @system.state.epicycleRotation

    @marker.descend = true

  _stateRotateEpicycle : (dt) =>

    # Now rotate the epicycle around the deferent till it reaches the white line      
    current_state = @stack[@stack_idx]

    #@epicycle.matrix.identity()
    v1 = @system.state.deferentPosition
    if @chosen_planet == "mercury"
      v1 = @system.state.mercuryDeferentPosition

    v2 = CoffeeGL.Vec2.sub @system.state.epicyclePrePosition, v1
    
    
    tmatrix = new CoffeeGL.Matrix4()
    fmatrix = new CoffeeGL.Matrix4()

    deferentAngle = @system.state.deferentAngle
    if @chosen_planet == "mercury"
      deferentAngle = @system.state.mercuryDeferentAngle
 
     
    tmatrix.translate new CoffeeGL.Vec3(v2.x,0,v2.y)
    tmatrix.rotate new CoffeeGL.Vec3(0,1,0), CoffeeGL.degToRad -90-deferentAngle

    fmatrix.translate new CoffeeGL.Vec3(v1.x,0,v1.y)
    fmatrix.rotate new CoffeeGL.Vec3(0,1,0), CoffeeGL.degToRad current_state.rot_interp.set dt

    @epicycle.matrix.copyFrom fmatrix.mult tmatrix 

    current_state.pos = @_setPOI @epicycle
    @move_poi.dispatch current_state.pos
  
    @

  _stateRotateMeanAuxInit : () =>
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["mean_aux"]
    current_state.rot_interp = new CoffeeGL.Interpolation 0, @system.state.meanAux

  _stateRotateMeanAux : (dt) =>
    current_state = @stack[@stack_idx]

    @pointer.matrix.identity()
    @pointer.matrix.rotate new CoffeeGL.Vec3(0,1,0), CoffeeGL.degToRad current_state.rot_interp.set dt

    current_state.pos = @_setPOI @epicycle
    @move_poi.dispatch current_state.pos

    @

  _stateRotateLabelInit : () =>
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["label"]
    if @chosen_planet == "moon"
      current_state.text = @_replaceText eq_text["label_moon"]
    current_state.rot_interp = new CoffeeGL.Interpolation 0, @system.state.pointerAngle

  _stateRotateLabel : (dt) =>

    current_state = @stack[@stack_idx]

    @pointer.matrix.identity()
    @pointer.matrix.rotate new CoffeeGL.Vec3(0,1,0), CoffeeGL.degToRad @system.state.meanAux + current_state.rot_interp.set dt

    current_state.pos = @_setPOI @marker
    @move_poi.dispatch current_state.pos

    @


  _stateMoveBlackStringFinalInit : () =>
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["black_final"] + @system.state.truePlace.toFixed(2) + "."
    if @chosen_planet == "moon"
      current_state.text = @_replaceText eq_text["black_final_moon"] +  @system.state.truePlace.toFixed(2) + "."

    mv = new CoffeeGL.Vec3 @system.state.pointerPoint.x,0, @system.state.pointerPoint.y
    mv.normalize()
    mv.multScalar(8.0)
    mv.y = @string_height
    current_state.end_interp = new CoffeeGL.Interpolation @black_end.matrix.getPos(), mv

  _stateMoveBlackStringFinal : (dt) =>
    current_state = @stack[@stack_idx]
    @black_end.matrix.setPos current_state.end_interp.set dt
    @physics.postMessage {cmd : "black_end_move", data: @black_end.matrix.getPos() }

    current_state.pos = @_setPOI @black_end
    @move_poi.dispatch current_state.pos

    @

  _stateMoveWhiteStringLatitudeInit : () =>

    current_state = @stack[@stack_idx]

    current_state.text = @_replaceText eq_text["alhudda"]
    
    s = new CoffeeGL.Vec3(@system.state.moonLatitudeLeft.x, 0, @system.state.moonLatitudeLeft.y)
    e = new CoffeeGL.Vec3(@system.state.moonLatitudeRight.x, 0, @system.state.moonLatitudeRight.y)

    # stretch the string
    #l = s.dist e
    s.x = -5.5
    e.x = 5.5

    s.y = @string_height
    e.y = @string_height

    
    current_state.start_interp = new CoffeeGL.Interpolation @white_start.matrix.getPos(), s
    current_state.end_interp = new CoffeeGL.Interpolation @white_end.matrix.getPos(), e


  _stateMoveWhiteStringLatitude : (dt) =>

    current_state = @stack[@stack_idx]

    @white_start.matrix.setPos current_state.start_interp.set dt
    @white_end.matrix.setPos current_state.end_interp.set dt

    current_state.pos = @_setPOI @white_end
    @move_poi.dispatch current_state.pos

    @physics.postMessage {cmd : "white_start_move", data: @white_start.matrix.getPos() }
    @physics.postMessage {cmd : "white_end_move", data: @white_end.matrix.getPos() }

    @


  _stateMoveBlackStringLatitudeInit : () =>

    current_state = @stack[@stack_idx]

    current_state.text = @_replaceText eq_text["black_latitude"]
    s = new CoffeeGL.Vec3(0, 0, 0)
    e = new CoffeeGL.Vec3(@system.state.moonLatitudeRight.x, 0, @system.state.moonLatitudeRight.y)


    s.y = @string_height
    e.y = @string_height

    
    current_state.start_interp = new CoffeeGL.Interpolation @black_start.matrix.getPos(), s
    current_state.end_interp = new CoffeeGL.Interpolation @black_end.matrix.getPos(), e


  _stateMoveBlackStringLatitude : (dt) =>

    current_state = @stack[@stack_idx]

    @black_start.matrix.setPos current_state.start_interp.set dt
    @black_end.matrix.setPos current_state.end_interp.set dt

    current_state.pos = @_setPOI @black_end
    @move_poi.dispatch current_state.pos

    @physics.postMessage {cmd : "black_start_move", data: @black_start.matrix.getPos() }
    @physics.postMessage {cmd : "black_end_move", data: @black_end.matrix.getPos() }

    @

  _stateReadLatitudeInit : () =>
    current_state = @stack[@stack_idx]
    current_state.text = @_replaceText eq_text["read_latitude"] + @system.state.moonLatitudeFinal.toFixed(2)

  _stateReadLatitude : (dt) =>
    current_state = @stack[@stack_idx]
    current_state.pos = @_setPOIVec new CoffeeGL.Vec3 @system.state.moonLatitudeCentre.x, @string_height ,@system.state.moonLatitudeCentre.y
    @move_poi.dispatch current_state.pos

  addStates : (planet, date) ->
    @stack = []
    
    if planet in ['mars','venus','jupiter','saturn','mercury']
      @stack.push new EquatorieState @_stateSetPlanetDateInit, () => do (planet,date) => @_stateSetPlanetDate(planet,date)
      @stack.push new EquatorieState @_stateRotatePlateInit, @_stateRotatePlate
      #@stack.push new EquatorieState @_stateCalculateMeanMotusInit, @_stateCalculateMeanMotus
      @stack.push new EquatorieState @_stateMoveBlackThreadInit, @_stateMoveBlackThread 
      @stack.push new EquatorieState @_stateMoveWhiteThreadInit, @_stateMoveWhiteThread 
      @stack.push new EquatorieState @_stateMoveEpicycleInit, @_stateMoveEpicycle
      @stack.push new EquatorieState @_stateRotateEpicycleInit, @_stateRotateEpicycle
      @stack.push new EquatorieState @_stateRotateMeanAuxInit, @_stateRotateMeanAux 
      @stack.push new EquatorieState @_stateRotateLabelInit, @_stateRotateLabel 
      @stack.push new EquatorieState @_stateMoveBlackStringFinalInit, @_stateMoveBlackStringFinal

    else if planet == 'moon'
      @stack.push new EquatorieState @_stateSetPlanetDateInit, () => do (planet,date) => @_stateSetPlanetDate(planet,date)
      #@stack.push new EquatorieState @_stateCalculateMeanMotusInit, @_stateCalculateMeanMotus
      @stack.push new EquatorieState @_stateMoveBlackThreadInit, @_stateMoveBlackThread
      @stack.push new EquatorieState @_stateMoveEpicycleInit, @_stateMoveEpicycle
      @stack.push new EquatorieState @_stateRotateEpicycleInit, @_stateRotateEpicycle
      @stack.push new EquatorieState @_stateMoveWhiteThreadMoonInit, @_stateMoveWhiteThreadMoon
      @stack.push new EquatorieState @_stateRotateMeanAuxInit, @_stateRotateMeanAux
      @stack.push new EquatorieState @_stateRotateLabelInit, @_stateRotateLabel
      @stack.push new EquatorieState @_stateMoveBlackStringFinalInit, @_stateMoveBlackStringFinal

    else if planet == "moon_latitude"
      @system.solveForPlanetDate(planet,date)
      @stack.push new EquatorieState @_stateCalculateMeanMotusInit, @_stateCalculateMeanMotus
      @stack.push new EquatorieState @_stateMoveBlackStringLatitudeInit, @_stateMoveBlackStringLatitude
      @stack.push new EquatorieState @_stateMoveWhiteStringLatitudeInit, @_stateMoveWhiteStringLatitude
      @stack.push new EquatorieState @_stateReadLatitudeInit, @_stateReadLatitude

    else if planet == "sun"
      @stack.push new EquatorieState @_stateSetPlanetDateInit, () => do (planet,date) => @_stateSetPlanetDate(planet,date)
      @stack.push new EquatorieState @_stateRotatePlateInit, @_stateRotatePlate
      #@stack.push new EquatorieState @_stateCalculateMeanMotusInit, @_stateCalculateMeanMotus
      @stack.push new EquatorieState @_stateMoveBlackThreadInit, @_stateMoveBlackThread
      @stack.push new EquatorieState @_stateMoveWhiteThreadSunInit, @_stateMoveWhiteThreadSun
      @stack.push new EquatorieState @_stateMoveBlackThreadSunInit, @_stateMoveBlackThreadSun

  # reset all the things

  reset : () ->
    # Clear the stack
    @stack = []
    @stack_idx = 0
    @system.reset()
    @marker.matrix.identity()
    @epicycle.matrix.identity()
    @pointer.matrix.identity()

    # Move the strings back
    @white_start.matrix.identity().translate new CoffeeGL.Vec3 2,@string_height,2
    @white_end.matrix.identity().translate new CoffeeGL.Vec3 -2,@string_height,-2
    @black_start.matrix.identity().translate new CoffeeGL.Vec3 -2,@string_height,2
    @black_end.matrix.identity().translate new CoffeeGL.Vec3 -4,@string_height,2

    # Move the camera back

    @camera.pos = new CoffeeGL.Vec3 0,0,10
    @camera.look = new CoffeeGL.Vec3 0,0,0
    @camera.up = new CoffeeGL.Vec3 0,1,0
    @camera.orbit new CoffeeGL.Vec3(1,0,0), CoffeeGL.degToRad -25

    @physics.postMessage { cmd: "reset" }

    @marker.descend = false

  # Called when the button is pressed in dat.gui. Solve for the chosen planet
  solveImmediate : () ->

    # Clear the stack
    tidx = @stack_idx
    #@reset()
    @stack = []
    
    @stack_idx = 0
    @system.reset()

    #@marker.matrix.identity()
    #@epicycle.matrix.identity()
    #@pointer.matrix.identity()

    # Move the strings back
    #@white_start.matrix.identity().translate new CoffeeGL.Vec3 2,@string_height,2
    #@white_end.matrix.identity().translate new CoffeeGL.Vec3 -2,@string_height,-2
    #@black_start.matrix.identity().translate new CoffeeGL.Vec3 -2,@string_height,2
    #@black_end.matrix.identity().translate new CoffeeGL.Vec3 -4,@string_height,2

    @addStates(@chosen_planet,@date)

    s = 0
    while s < tidx
      @stepForward()
      s+=1

    @stack[@stack_idx].activate()


    # Return data for the JQuery / Bootstrap front end
    rval = {}

    rval.text = @stack[@stack_idx].text if @stack[@stack_idx].text?
    rval.pos = @stack[@stack_idx].pos if @stack[@stack_idx].pos?
    rval


  onMouseDown : (event) ->
    @mp.x = event.mouseX
    @mp.y = event.mouseY
    @ray = @camera.castRay @mp.x, @mp.y
    
    @mdown = true

    if not @picked
      @camera.onMouseDown(event)
    else
      @dragging = true

  onMouseMove : (event) ->
    @mpp.x = @mp.x
    @mpp.y = @mp.y

    @mp.x = event.mouseX
    @mp.y = event.mouseY

    @mpd.x = @mp.x - @mpp.x
    @mpd.y = @mp.y - @mpp.y

    if @dragging

      tray = @camera.castRay @mp.x, @mp.y
      d = CoffeeGL.rayPlaneIntersect new CoffeeGL.Vec3(0,@string_height,0), new CoffeeGL.Vec3(0,1,0), @camera.pos, @ray
      dd = CoffeeGL.rayPlaneIntersect new CoffeeGL.Vec3(0,@string_height,0), new CoffeeGL.Vec3(0,1,0), @camera.pos, tray

      p0 = tray.copy()
      p0.multScalar(dd)
      p0.add(@camera.pos)

      p1 = @ray.copy()
      p1.multScalar(d)
      p1.add(@camera.pos)

      p0.y = @string_height
      p1.y = @string_height

      np = CoffeeGL.Vec3.sub(p0,p1)

      @ray.copyFrom(tray)

      if @picked_item != @pointer
        tp = @picked_item.matrix.getPos()
        @picked_item.matrix.setPos( tp.add(np) )

      # Update the phyics webworker thread if strings
      if @picked_item == @white_start
          @physics.postMessage {cmd : "white_start_move", data: @picked_item.matrix.getPos() }
      
      else if @picked_item == @white_end
          @physics.postMessage {cmd : "white_end_move", data: @picked_item.matrix.getPos() }
      
      else if @picked_item == @black_start
          @physics.postMessage {cmd : "black_start_move", data: @picked_item.matrix.getPos() }
      
      else if @picked_item == @black_end
          @physics.postMessage {cmd : "black_end_move", data: @picked_item.matrix.getPos() }

    else
      @camera.onMouseMove(event)


  checkPicked : (p) ->
    
    if @dragging
      return

    @picked = false

    # red, green, blue, white for strings, ws, we, bs, be
    # magenta for epi 1,1,0
    # cyan for pointer 0,1,1
    # Bit shift / mask might be faster here I think

    if p[0] == 255 and p[1] == 0 and p[2] == 0
      @picked_item = @white_start
      @picked = true

    else if p[0] == 0 and p[1] == 255 and p[2] == 0
      @picked_item = @white_end
      @picked = true

    else if p[0] == 0 and p[1] == 0 and p[2] == 255
      @picked_item = @black_start
      @picked = true

    else if p[0] == 255 and p[1] == 255 and p[2] == 255
      @picked_item = @black_end
      @picked = true

    else if p[0] == 255 and p[1] == 255 and p[2] == 0
      @picked_item = @epicycle
      @picked = true

    else if p[0] == 0 and p[1] == 255 and p[2] == 255
      @picked_item = @pointer
      @picked = true
      

  _initGUI : () ->
    # DAT Gui stuff
    # https://gist.github.com/ikekou/5589109
    @datgui =new dat.GUI()
    @datgui.remember(@)
    
    planets = ["mars","venus","jupiter","saturn","mercury","moon","sun","moon_latitude"]
    @chosen_planet = "mars"

    controller = @datgui.add(@,'chosen_planet',planets)
    controller = @datgui.add(@,'solveForCurrentDatePlanet')
    controller = @datgui.add(@,'advance_date',0,730)
    controller.onChange( (value) => 
      @solveForCurrentDatePlanet()
    )

    controller = @datgui.add(@, "stepForward")
    controller = @datgui.add(@, "reset")

    # Shader Controls
    controller = @datgui.add(@pointer,'uAlphaX',0,1)
    controller = @datgui.add(@pointer,'uAlphaY',0,1)

    controller = @datgui.add(@epicycle,'uAlphaX',0,1)
    controller = @datgui.add(@epicycle,'uAlphaY',0,1)


  stepForward : () ->

    @time.start = new Date().getTime()

    if @stack.length == 0
      @addStates @chosen_planet, @date
      @stack_idx = 0
    else
      if @stack_idx + 1 < @stack.length
        # Make sure current state has completed
        @stack[@stack_idx].update(1.0)
        @time.dt = 0
        @stack_idx +=1
    
    @stack[@stack_idx].activate()

    # Return data for the JQuery / Bootstrap front end
    rval = {}

    rval.text = @stack[@stack_idx].text if @stack[@stack_idx].text?
    rval.pos = @stack[@stack_idx].pos if @stack[@stack_idx].pos?
    rval

  stepBackward : () ->

    @time.start = new Date().getTime()

    if @stack.length != 0
     
      if @stack_idx - 1 >= 0
        # Make sure current state has completed
        @stack[@stack_idx].update(0.0)
        @time.dt = 0
        @stack_idx -=1
        @stack[@stack_idx].update(0.0)
    
        @stack[@stack_idx].activate()

    # Return data for the JQuery / Bootstrap front end
    rval = {}

    rval.text = @stack[@stack_idx].text if @stack[@stack_idx].text?
    rval.pos = @stack[@stack_idx].pos if @stack[@stack_idx].pos?
    rval

  # Date is a date object
  setDate : (date) ->
    if not date?
      @date = new Date()
      @date.setDate @date.getDate() + @advance_date
    else if date instanceof Date
      @date = date

    


    # Check that its always midday
    if @date.getHours != 12
      @date.setHours(12)
   

  # Given a zoom level (0-1), set the camera

  setZoom: (dz) ->
    @camera.zoom(dz)

  onMouseOver : (event) ->    
    @mp.x = event.mouseX
    @mp.y = event.mouseY

  onMouseUp : (event) ->
    @mdown = false
    @picked = false
    @dragging = false

  onMouseOut : (event) ->
    @mp.x = @mpp.x = -1
    @mp.y = @mpp.y = -1
    @mpd.x = @mpd.y = 0

    @mdown = false
    @picked = false
    @dragging = false

  _checkCameraZoom : () ->
    dir = CoffeeGL.Vec3.sub @camera.look, @camera.pos
    dl = dir.length()
    camera_zoom = dl / (@camera.zoom_far - @camera.zoom_near)
    @zoom_signal.dispatch(camera_zoom)

  onMouseWheel : (event) ->
    # Check the zoom level from the camera
    @_checkCameraZoom()

  onTouchPinch : (event) ->
    @_checkCameraZoom()

  onTouchSpread : (event) ->
    @_checkCameraZoom()




module.exports = 
  EquatorieInteract : EquatorieInteract