###

  Copyright (c) 2012 cannon.js Authors
  
  Permission is hereby granted, free of charge, to any person
  obtaining a copy of this software and associated documentation
  files (the "Software"), to deal in the Software without
  restriction, including without limitation the rights to use, copy,
  modify, merge, publish, distribute, sublicense, and/or sell copies
  of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be
  included in all copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 

###


# Web Worker for the String Physics in this system

# http://www.html5rocks.com/en/tutorials/workers/basics/

importScripts '/js/cannon.js'

# Using Canon JS for the Physics


# TODO - Limits on how far the string can be stretched
@ping = 0
@string_height = 0.4
@bodies = [] # A Record on all bodies in the system
@constraints = [] # A Record of all constraints in the system

class PhysicsString
  constructor : (length, thickness, segments, start, end, world, bodies, constraints) ->

    seglength = length / segments

    @children = []
    @length  = length


    mass = 1
    radius = seglength /6

    for i in [0..segments-2]
      # Shape and damping

      sphereShape = new CANNON.Sphere(radius)
      sphereBody = new CANNON.RigidBody(mass,sphereShape)
      sphereBody.angularDamping = 0.99
      sphereBody.linearDamping = 0.99
      sphereBody.position.set(0, 0, 15.0 + (i * seglength))

      @children.push sphereBody
      bodies.push sphereBody
      world.add(sphereBody)

    # add Constraints to make this look like string
  
    for i in [0..@children.length-2]

      c = new CANNON.DistanceConstraint( @children[i], @children[i+1], seglength / 1.5)
      world.addConstraint(c)
      constraints.push c
  
    
    # Start position
    sphereShape = new CANNON.Sphere(radius)
    @start = new CANNON.RigidBody(0,sphereShape)
    @start.position.set(start.x, start.z, start.y)

    c = new CANNON.DistanceConstraint(@children[0], @start, seglength / 1.5)

    world.addConstraint(c,true)
    world.add(@start)
    bodies.push @start
    constraints.push c
    
    @end = new CANNON.RigidBody(0,sphereShape)
    @end.position.set(end.x, end.z, end.y)
    
    c = new CANNON.DistanceConstraint(@children[@children.length-1], @end, 0.05 )

    world.addConstraint(c,true)
    world.add(@end)
    bodies.push @end
    constraints.push c
    
  
  update : () ->
    
    list = []

    _getTrans = (b) =>

      obj = {}
    
      obj.q = [b.quaternion.x, b.quaternion.z, b.quaternion.y, b.quaternion.w]
    
      obj.x = b.position.x
      obj.y = b.position.z
      obj.z = b.position.y
      

      obj

    list.push _getTrans @start

    for segment in @children
      list.push _getTrans segment

    list.push _getTrans @end

    list


interval = null

@startUp = () ->
  
  @dynamicsWorld = new CANNON.World()
  @dynamicsWorld.gravity.set 0,0,-9.82
  @dynamicsWorld.broadphase = new CANNON.NaiveBroadphase()
  #@dynamicsWorld.allowSleep = true


  #Create a cylinder
  baseShape = new CANNON.Cylinder 6.0,6.0,0.1,12
  baseBody = new CANNON.RigidBody(0,baseShape)
  @dynamicsWorld.add(baseBody)

  groundShape = new CANNON.Plane()
  groundBody = new CANNON.RigidBody(0,groundShape)
  @dynamicsWorld.add(groundBody)

  # Create the white string
  @white_string = new PhysicsString 8.2, 0.015, 20, {x: 2, y:0.2, z:2}, {x: -2, y:0.2, z:-2}, @dynamicsWorld, @bodies, @constraints

  # ... and the black one
  @black_string = new PhysicsString 8.2, 0.015, 20, {x: -2, y:0.2, z:2}, {x: -4, y:0.2, z:2}, @dynamicsWorld, @bodies, @constraints

  last = Date.now()
  
  mainLoop = () ->
    now = Date.now()
    simulate(now - last)
    last = now

  if (interval)
    clearInterval(interval)

  
  interval = setInterval(mainLoop, 1000/60)


@moveBody = (body, pos) ->

  # Damping seems to kill things :S

  body.position.x = pos.x
  body.position.y = pos.z
  body.position.z = pos.y

  data =
    black :
      segments : []
    white :
      segments : []

  data.white.segments = @white_string.update()
  data.black.segments = @black_string.update()

  postMessage { cmd: 'physics', data: data}


@simulate = (dt) ->
  dt = dt || 1

  @dynamicsWorld.step 1.0/60.0

  data =
    black :
      segments : []
    white :
      segments : []

  data.white.segments = @white_string.update()
  data.black.segments = @black_string.update()

  postMessage { cmd: 'physics', data: data}


@reset = () ->
 
  for c in @bodies
    @dynamicsWorld.remove c
    
  for c in @constraints
    @dynamicsWorld.removeConstraint c
   
  startUp()

# Message format {cmd: <cmd> , data: <data obj> }

@onmessage = (event) -> 

  switch event.data.cmd
    when "startup" then startUp()
    when "reset" then reset()
    when "white_start_move" then moveBody white_string.start, event.data.data 
    when "white_end_move" then moveBody white_string.end, event.data.data    
    when "black_start_move" then moveBody black_string.start, event.data.data    
    when "black_end_move" then moveBody black_string.end, event.data.data    
    else postMessage event.data.cmd


 
