
{CoffeeGL} = require '../lib/coffeegl/coffeegl'

class EquatorieSystem

  constructor : () ->

    @base_radius = 6.0              # used with epicycle ratio - Blender Units
    @inch_to_base = 0.166666666667  # Convert inches into blender units  
    @epicycle_radius = 6.0          # used with epicycle ratio - Blender Units
    @epicycle_thickness = 0.333334
    @precession = 0.00003838  # Degrees per day # ignored using Sebs values I think
    @moon_radius = 1.17742          # Blender unit radius of the moon's holes

    @planet_data = {}

    # Data for the Equatorie mathematical model
    # Taken from the Evans book
    # Speed is in degrees per day (modified to be decimal)

    ###

    @planet_data.venus =
      deferent_speed : 0.98564734
      epicycle_speed : 0.61652156
      epicycle_ratio : 0.72294
      deferent_eccentricity : 0.0145
      apogee_longitude : 98.1666667
      mean_longitude : 279.7
      mean_anomaly : 63.383

    @planet_data.mars =
      deferent_speed : 0.52407116
      epicycle_speed : 0.46157618
      epicycle_ratio : 0.6563
      deferent_eccentricity : 0.10284
      apogee_longitude : 148.6166667
      mean_longitude : 293.55
      mean_anomaly : 346.15

    @planet_data.jupiter =
      deferent_speed : 0.08312944
      epicycle_speed : 0.90251790
      epicycle_ratio : 0.1922
      deferent_eccentricity : 0.04817
      apogee_longitude : 188.9666667
      mean_longitude : 238.16666667
      mean_anomaly : 41.5333333

     @planet_data.saturn =
      deferent_speed : 0.03349795
      epicycle_speed : 0.95214939
      epicycle_ratio : 0.10483
      deferent_eccentricity : 0.05318
      apogee_longitude : 270.76666667
      mean_longitude : 266.25
      mean_anomaly : 13.45

    ###

    # Epoch from Evans

    #@epoch = new Date ("January 1, 1900 00:00:00")
    #@epoch_julian = 2415020
  
    # Seb's Data

    @planet_data.venus =
      deferent_speed : 0.9856464
      epicycle_speed : 0.61652156
      epicycle_ratio : 0.76639
      deferent_eccentricity : 0.018056
      apogee_longitude : 90.15
      mean_longitude : 288.55
      mean_anomaly : 23.216667

    @planet_data.mars =
      deferent_speed : 0.52406791
      epicycle_speed : 0.46157618
      epicycle_ratio : 0.68611
      deferent_eccentricity : 0.095
      apogee_longitude : 133.93333
      mean_longitude : 92.2
      mean_anomaly : 196.33333

    @planet_data.jupiter =
      deferent_speed : 0.08312709
      epicycle_speed : 0.90251790
      epicycle_ratio : 0.184167
      deferent_eccentricity : 0.049583
      apogee_longitude : 172.333
      mean_longitude : 324.75
      mean_anomaly : 323.78333

    @planet_data.saturn =
      deferent_speed : 0.03349673 # CX This - Might be a mistake from Seb in his PDF
      epicycle_speed : 0.95214939
      epicycle_ratio : 0.10361
      deferent_eccentricity : 0.0543056
      apogee_longitude : 252.11666667
      mean_longitude : 184.75
      mean_anomaly : 103.78333
    

    @planet_data.mercury =
      deferent_speed : 0.9856464
      epicycle_speed : 3.10670237
      epicycle_ratio : 0.36722
      deferent_eccentricity : 0.05056
      apogee_longitude : 209.7666666
      mean_longitude : 288.55
      mean_anomaly : 259.78333

    @planet_data.sun = 
      deferent_speed : 0.9856464
      apogee_longitude : 90.15
      mean_longitude : 288.55
      equant_ratio : 1/32

    @planet_data.moon = 
      deferent_speed : 13.1763947
      epicycle_speed : 13.0649885
      epicycle_ratio : 0.08694
      mean_longitude : 130.46666
      mean_anomaly : 84.63333

    @planet_data.moon_latitude = @planet_data.moon

    @planet_data.caput_draconis = 
      start_motus : 344.633333 #15.35
      speed : -0.05295426  #0.05299066 # degrees per day


    @epoch = new Date(1392,11,31)
    @epoch.setHours(12)
    @epoch_julian = 2229851
   
    # consistent pieces of information at each step
    # The system records its state as we progress through
    # This means things must be done in order.

    # Set Planet and Calculate Date must be called first.

    @reset()

  # This function sets the state above and is the only function to be called externally 
  # along with reset
  
  solveForPlanetDate : (planet, date) ->

    @_setPlanet(planet)
    @_calculateDate(date)
    [@state.sunMeanMotus, @state.sunMeanMotusPosition] = @_calculateMeanMotusBody("sun")


    if planet in ["mercury","venus","mars","jupiter","saturn"]
      @_calculateDeferentAngle()
      @_calculateMeanMotus()
      @_calculateDeferentPosition()
      @_calculateEquantPosition()
      @_calculateParallel()
      @_calculateEpicyclePosition()
      @_calculatePointerAngle()
      @_calculatePointerPoint()
      @_calculateTruePlace()

    else if planet == "sun"
      @_calculateEquantPosition()
      @_calculateMeanMotus()
      @_calculateParallel()
      @_calculateSunCrossingPoint()
      @_calculateTruePlace()
      
    else if planet == "moon" or planet == "moon_latitude"
      @_calculateMeanMotus()
      @_calculateDeferentAngle()
      @_calculateDeferentPosition()
      @_calculateEquantPosition()
      @_calculateEpicyclePosition()
      
      @_calculatePointerAngle()
      @_calculatePointerPoint()
      @_calculateTruePlace()
      @_calculateMoonEquations()

  # set the state of the system to all zeroes
  reset : () ->

    # state represents the variables in our Equatorie we may need at any one time
    @state = 
      meanMotus : 0
      sunMeanMotus: 0
      meanMotusPosition : 0
      sunMeanMotusPosition : 0
      deferentAngle : 0
      deferentPosition : 0
      mercuryDeferentAngle : 0
      sunCirclePoint : 0
      passed : 0
      planet : ''
      date : 0
      parallelPosition : 0
      pointerAngle : 0
      pointerPoint : 0
      equantPosition : 0
      epicycleRotation: 0
      epicyclePosition : 0
      epicyclePrePosition : 0
      basePosition : 0
      truePlace : 0
      meanAux : 0
      mercuryDeferentPosition: 0
      moonEquationCentre: 0
      moonTrueMotus : 0
      caputDraconisMotus : 0
      moonLatitudeDegree : 0
      moonLatitudeLeft : 0
      moonLatitudeRight : 0
      moonLatitudeCentre: 0

  # The following are private subroutines and are executed in a particular order to generate
  # the system state

  _setPlanet : (planet) ->
    @state.planet = planet
    @

  _calculateDate : (date) ->
    # Worked out from the date (current) and the tables above
    # return the number of days - use Julian dates

    #h ttp://www.cs.utsa.edu/~cs1063/projects/Spring2011/Project1/jdn-explanation.html

    # Javascript cant cope with pre epoch dates as it is lame! :O

    # We are dealing here with the calendar change over as mentioned by Pope Gregory XIII in 1582
    # We need to redo leap year calculations and subtract for missing days

    # http://aa.usno.navy.mil/data/docs/JulianDate.php

    preg = false

    if date.getFullYear() < 1582 
      preg = true
    else if date.getFullYear() == 1582
      if (date.getMonth()+1) < 10
        preg = true
      else if (date.getMonth()+1) == 10 and date.getDate() < 15
        preg = true


    a = if date.getMonth() == 0 or date.getMonth() == 1 then 1 else 0
  
    y = date.getFullYear() + 4800 - a 
    m = (date.getMonth()+1) + (12*a) - 3

    d = date.getDate()
    if preg == true
      j = d + Math.floor((153 * m + 2)/5) + (365 * y) + Math.floor(y/4) - 32045 - 38
    else
      j = d + Math.floor((153 * m + 2)/5) + (365 * y) + Math.floor(y/4) - Math.floor(y/100) + Math.floor(y/400) - 32045

    #Math.abs(date - @epoch) / 86400000
    p = j - @epoch_julian

    @state.date = j

    @state.passed = p
    p


  # Calculates the main deferent position for the planets.
  # In the case of mercury, it works out the initial deferent from which the final is derived
  _calculateDeferentAngle : () ->
    if @state.planet in ['mars','venus','jupiter','saturn','mercury']
      angle = -@planet_data[@state.planet].apogee_longitude - ( @precession * @state.passed )
      @state.deferentAngle = angle
      return angle

    else if @state.planet in ["moon","moon_latitude"]
      @state.deferentAngle = @state.meanMotus + Math.abs(@state.meanMotus - @state.sunMeanMotus)
      return @state.deferentAngle

    @

  # Calculate the position of the deferent on the place.
  # In the case of mercury, we also create the final mercury deferent angle and position
  # In the case of the moon, we simply take the moon circle radius and do some basic trig.

  _calculateDeferentPosition : () ->
    if @state.planet in ['mars','venus','jupiter','saturn']
      x = @inch_to_base * 34 * @planet_data[@state.planet].deferent_eccentricity * Math.cos(CoffeeGL.degToRad @state.deferentAngle)
      y = @inch_to_base * 34 * @planet_data[@state.planet].deferent_eccentricity * Math.sin(CoffeeGL.degToRad @state.deferentAngle)
      @state.deferentPosition = new CoffeeGL.Vec2 x,y

      cr = Math.cos CoffeeGL.degToRad @state.deferentAngle
      sr = Math.sin CoffeeGL.degToRad @state.deferentAngle

      @state.basePosition = new CoffeeGL.Vec2(@base_radius * cr, @base_radius * sr)

      return @state.deferentPosition

    else if @state.planet == "mercury"
      # We need to makre sure we have the mean Motus at this point - it moves clockwise by the amount of the mean motus
      # We also only have 180 holes in this model so we need to restrain to 2 degrees of accuracy
      x = @inch_to_base * 34 * @planet_data[@state.planet].deferent_eccentricity * Math.cos(CoffeeGL.degToRad @state.deferentAngle)
      y = @inch_to_base * 34 * @planet_data[@state.planet].deferent_eccentricity * Math.sin(CoffeeGL.degToRad @state.deferentAngle)
      @state.deferentPosition = new CoffeeGL.Vec2 x,y

      # Mean centre is the angle / different between the mean Motus and Apsides line
      meanCentre = Math.abs(@state.deferentAngle - @state.meanMotus)


      l = @state.deferentPosition.length()
      x = Math.cos CoffeeGL.degToRad -@state.meanMotus
      y = Math.sin CoffeeGL.degToRad -@state.meanMotus

      offset = new CoffeeGL.Vec2 x,y
      offset.normalize()
      offset.multScalar(l)

      @state.mercuryDeferentPosition =  @state.deferentPosition.copy()
      @state.mercuryDeferentPosition.multScalar(2)
      @state.mercuryDeferentPosition.add offset
    
      # We need to re-adjust the deferent angle as this is used elsewhere in the code from now on

      md = CoffeeGL.Vec2.normalize @state.mercuryDeferentPosition
      xa = new CoffeeGL.Vec2 1,0

      da = CoffeeGL.radToDeg Math.acos md.dot xa
      if da < 0
        da = 360 + da

      @state.mercuryDeferentAngle = da

      cr = Math.cos CoffeeGL.degToRad da
      sr = Math.sin CoffeeGL.degToRad da

      base_position = new CoffeeGL.Vec2(@base_radius * cr, @base_radius * sr)
      @state.basePosition = base_position

      return @state.mercuryDeferentPosition

    else if @state.planet in ["moon","moon_latitude"]

      cr = Math.cos CoffeeGL.degToRad @state.deferentAngle
      sr = Math.sin CoffeeGL.degToRad @state.deferentAngle

      x = @moon_radius * cr
      y = @moon_radius *  sr
      @state.deferentPosition = new CoffeeGL.Vec2 x,y

      @state.basePosition = new CoffeeGL.Vec2(@base_radius * cr, @base_radius * sr)

    @

  # Find the position of the equant in global co-ordinates
  _calculateEquantPosition : () ->
    if @state.planet in ['mars','venus','jupiter','saturn']
      @state.equantPosition = new CoffeeGL.Vec2 @state.deferentPosition.x*2, @state.deferentPosition.y*2
      return @state.equantPosition
    else if @state.planet == "mercury"
      x = @inch_to_base * 34 * @planet_data[@state.planet].deferent_eccentricity * Math.cos(CoffeeGL.degToRad @state.deferentAngle)
      y = @inch_to_base * 34 * @planet_data[@state.planet].deferent_eccentricity * Math.sin(CoffeeGL.degToRad @state.deferentAngle)
      @state.equantPosition = new CoffeeGL.Vec2 x,y

    else if @state.planet in ["moon","moon_latitude"]
      tm = new CoffeeGL.Matrix2 [-1,0,0,-1]
      @state.equantPosition = new CoffeeGL.Vec2  @state.deferentPosition.x,  @state.deferentPosition.y
      tm.multVec @state.equantPosition

    else if @state.planet == "sun"
      l = @inch_to_base * 34 * @planet_data["sun"].equant_ratio
      x = Math.cos CoffeeGL.degToRad -@planet_data["sun"].apogee_longitude
      y = Math.sin CoffeeGL.degToRad -@planet_data["sun"].apogee_longitude
      @state.equantPosition = new CoffeeGL.Vec2 x,y
      @state.equantPosition.multScalar l

    @

  # Return the mean motus angle plus the postion on the rim of the base
  _calculateMeanMotusBody : (body) ->
    
    passed = @state.passed
    mean_motus_angle = (@planet_data[body].mean_longitude + (@planet_data[body].deferent_speed * passed) ) % 360 * -1
   
    mean_motus_position = new CoffeeGL.Vec2(@base_radius * Math.cos( CoffeeGL.degToRad(mean_motus_angle) ), 
      @base_radius * Math.sin( CoffeeGL.degToRad(mean_motus_angle) ))

  
    [mean_motus_angle, mean_motus_position]


  # calcute the mean motus
  _calculateMeanMotus : () ->

    [mean_motus_angle, mean_motus_position] = @_calculateMeanMotusBody @state.planet
    @state.meanMotusPosition = mean_motus_position
    @state.meanMotus = mean_motus_angle

  # http://stackoverflow.com/questions/1073336/circle-line-collision-detection
  # Given a ray and circle, solve the quadratic and find intersection points
  rayCircleIntersection : (ray_start, ray_dir, circle_centre, circle_radius) ->

    f = CoffeeGL.Vec2.sub(ray_start,circle_centre)
    r = circle_radius

    a = ray_dir.dot( ray_dir )
    b = 2*f.dot( ray_dir )
    c = f.dot( f ) - r*r

    v = new CoffeeGL.Vec2()

    discriminant = b*b-4*a*c

    if discriminant != 0
      discriminant = Math.sqrt(discriminant)
      t1 = (-b - discriminant)/(2*a)
      t2 = (-b + discriminant)/(2*a)

      t = t2
      t = t1 if t2 < 0 

      # Plug back into our line equation
      
      v.copyFrom(ray_start)
      d2 = CoffeeGL.Vec2.multScalar(ray_dir,t)
      v.add(d2)

    v

  # Find where the parallel line from the Equant, parallel with the meanmotus, cuts the rim
  _calculateParallel : () ->

    passed = @state.passed

    deferent_position = @state.deferentPosition
    if @state.planet == "mercury"
      deferent_position = @state.mercuryDeferentPosition

    if @state.planet == "sun"
      deferent_position = @state.equantPosition

    equant_position = @state.equantPosition

    dir = @state.meanMotusPosition.copy()
    dir.normalize()

    @state.parallelPosition = @rayCircleIntersection(equant_position, dir, deferent_position, @base_radius)
    @state.parallelPosition


  _calculateSunCrossingPoint : () ->
    dir = CoffeeGL.Vec2.sub @state.parallelPosition, @state.equantPosition
    dir.normalize()
    @state.sunCirclePoint = @rayCircleIntersection(@state.equantPosition, dir, @state.equantPosition, 32 * @inch_to_base)
    @

  # The moon requires a few more angles and things to be worked out for its latitude
  _calculateMoonEquations : () ->
    
    a = CoffeeGL.Vec2.sub @state.epicyclePosition, @state.equantPosition
    a.normalize()
    b = @state.epicyclePosition.copy().normalize().multScalar(-1)

    @state.moonEquationCentre = CoffeeGL.radToDeg Math.acos a.dot b 
    @state.moonTrueMotus = @state.meanMotus + @state.moonEquationCentre

    @state.caputDraconisMotus =  (@planet_data["caput_draconis"].start_motus + ( @state.passed * @planet_data["caput_draconis"].speed)) % 360

    p = @state.truePlace


    l = p - @state.caputDraconisMotus

    if l > 360
      l = 360 - l

    if l < 0 
      l = 360 + l

    
    sign = 1
    if l > 180
      l = l - 180
      sign = -1

    @state.moonLatitudeDegree = l
    
    x = Math.cos CoffeeGL.degToRad -@state.moonLatitudeDegree
    y = Math.sin CoffeeGL.degToRad -@state.moonLatitudeDegree

    @state.moonLatitudeFinal = y * -5 * sign

    s = new CoffeeGL.Vec2 x,y
    s.multScalar (@base_radius-@epicycle_thickness)

    x2 = Math.cos CoffeeGL.degToRad(@state.moonLatitudeDegree-180)
    y2 = Math.sin CoffeeGL.degToRad(@state.moonLatitudeDegree-180)

    e = new CoffeeGL.Vec2 x2, y2
    e.multScalar (@base_radius-@epicycle_thickness)

    @state.moonLatitudeLeft = s
    @state.moonLatitudeRight = e

    @state.moonLatitudeCentre = new CoffeeGL.Vec2 0,s.y


    @


  _calculateEpicyclePosition : () ->
    # Rotate the epicycle so its 0 point is on the deferent, then rotate around the deferent
    # by the mean motus - two steps

    passed = @state.passed
    dangle = @state.deferentAngle
    if @state.planet == "mercury"
      dangle = @state.mercuryDeferentAngle
    
    deferent_position = @state.deferentPosition
    if @state.planet == "mercury"
      deferent_position = @state.mercuryDeferentPosition
    
    equant_position = @state.equantPosition
   
    l = deferent_position.length() +  @epicycle_radius - @epicycle_thickness 

    @state.epicyclePrePosition = CoffeeGL.Vec2.normalize(deferent_position).multScalar(l)

    # At this point we have the first transform, before we need to rotate the epicycle over
    # the white string in the case of the planets and the black string if its the moon
    fangle = 0

    # Line equation - p = s + tD
  
    if @state.planet in ["mercury","venus","mars","jupiter","saturn"]
    
      edir = CoffeeGL.Vec2.sub @state.parallelPosition, @state.equantPosition
      edir.normalize()
      @state.epicyclePosition =  @rayCircleIntersection @state.equantPosition, edir, @state.deferentPosition, (@base_radius-@epicycle_thickness)
    
      f0 = CoffeeGL.radToDeg Math.atan2 @state.basePosition.y - deferent_position.y, @state.basePosition.x - deferent_position.x
      f1 = CoffeeGL.radToDeg Math.atan2 @state.epicyclePosition.y - deferent_position.y, @state.epicyclePosition.x - deferent_position.x
      fangle = f0 - f1

      @state.epicycleRotation = fangle #CoffeeGL.radToDeg Math.acos dc.dot pp
      
      return @state.epicyclePosition 

    else if @state.planet in ["moon","moon_latitude"]
    
      edir = @state.meanMotusPosition.copy()
      edir.normalize()
      @state.epicyclePosition =  @rayCircleIntersection new CoffeeGL.Vec2(0,0), edir, @state.deferentPosition, (@base_radius-@epicycle_thickness)
    
      f0 = CoffeeGL.radToDeg Math.atan2 @state.basePosition.y - deferent_position.y, @state.basePosition.x - deferent_position.x
      f1 = CoffeeGL.radToDeg Math.atan2 @state.epicyclePosition.y - deferent_position.y, @state.epicyclePosition.x - deferent_position.x
      fangle = f0 - f1

      @state.epicycleRotation = fangle
        
      return @state.epicyclePosition 

  # Turned anti-clockwise around the epicycle
  # We count this as the angle from the white string, not the zero point
  _calculatePointerAngle : () ->
    passed = @state.passed
    angle = (@planet_data[@state.planet].mean_anomaly + (@planet_data[@state.planet].epicycle_speed * passed)) % 360 * -1
    
    # Use the law of cosines to derive the mean aux

    if @state.planet in ["mercury","venus","mars","jupiter","saturn"]

      deferent_position = @state.deferentPosition
      if @state.planet == "mercury"
        deferent_position = @state.mercuryDeferentPosition

      a = @state.equantPosition.dist @state.epicyclePosition
      b = deferent_position.dist @state.epicyclePosition
      c = @state.equantPosition.dist deferent_position

      va = CoffeeGL.Vec2.sub @state.epicyclePosition, @state.equantPosition
      vb = CoffeeGL.Vec2.sub @state.epicyclePosition, deferent_position
      va.normalize()
      vb.normalize()
     
      determinate = va.x * vb.y - va.y * vb.x

      sa = CoffeeGL.radToDeg Math.acos( (a*a + b*b - c*c) /  (2*a*b) )
      @state.meanAux = 90 - sa
     
      if determinate > 0
        @state.meanAux = 90 + sa

      @state.pointerAngle = -angle


    
    else if @state.planet in ["moon","moon_latitude"]
      a = @state.epicyclePosition.length()
      b = @state.equantPosition.dist @state.epicyclePosition
      c = @state.equantPosition.length()

      sa = CoffeeGL.radToDeg Math.acos( (a*a + b*b - c*c) /  (2*a*b) )


      a = @state.epicyclePosition.length()
      b = @state.deferentPosition.dist @state.epicyclePosition
      c = @state.deferentPosition.length()

      sb = CoffeeGL.radToDeg Math.acos( (a*a + b*b - c*c) /  (2*a*b) )

      @state.meanAux = 90 - sa - sb

      va = @state.epicyclePosition.copy()
      va.multScalar -1
      vb = CoffeeGL.Vec2.sub @state.epicyclePosition, @state.equantPosition
      va.normalize()
      vb.normalize()
     
      determinate = va.x * vb.y - va.y * vb.x

      if determinate > 0
        @state.meanAux = 90 + sa + sb

      @state.pointerAngle = angle

    angle
  

  # in Global co-ordinates
  _calculatePointerPoint : () ->
    angle = @state.pointerAngle + @state.meanAux
    epipos = @state.epicyclePosition 

    deferent_position = @state.deferentPosition
    if @state.planet == "mercury"
      deferent_position = @state.mercuryDeferentPosition

    # At this point perp is the point underneath the epicycle
    dir = CoffeeGL.Vec2.normalize CoffeeGL.Vec2.sub @state.epicyclePosition,deferent_position
    
    # Move left down the limb
    perp = dir.copy()
    perp.x = -dir.y
    perp.y = dir.x

    perp.multScalar (@base_radius * @planet_data[@state.planet].epicycle_ratio )

    ca = Math.cos CoffeeGL.degToRad -angle
    sa = Math.sin CoffeeGL.degToRad -angle

    perp = new CoffeeGL.Vec2(perp.x * ca - perp.y * sa, perp.x * sa + perp.y * ca)
     
    perp.add @state.epicyclePosition
  
    @state.pointerPoint = perp
    perp 

  # in degrees from the centre of the base and the sign of aries (x axis in this system)
  _calculateTruePlace : () ->
    if @state.planet in ["mercury","venus","mars","jupiter","saturn","moon", "moon_latitude"]
      pp = @state.pointerPoint
      dir = CoffeeGL.Vec2.normalize pp
      xaxis = new CoffeeGL.Vec2(1,0)
      angle = CoffeeGL.radToDeg Math.acos xaxis.dot dir
      if pp.y > 0
        angle = 360 - angle

      @state.truePlace = angle
    else if @state.planet == "sun"
      # black string crossing the limb

      xaxis = new CoffeeGL.Vec2 1,0
      angle = CoffeeGL.radToDeg Math.acos xaxis.dot CoffeeGL.Vec2.normalize @state.sunCirclePoint
      
      determinate = @state.sunCirclePoint.y
      if determinate > 0
        @state.truePlace = 360 - angle
      else
        @state.truePlace = angle


module.exports = 
  EquatorieSystem : EquatorieSystem