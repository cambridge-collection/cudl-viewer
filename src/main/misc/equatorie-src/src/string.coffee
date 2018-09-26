
{CoffeeGL} = require '../lib/coffeegl/coffeegl'

# Given some bullet physics represent the string

class EquatorieString extends CoffeeGL.Node
  constructor : (@length, @thickness, @segments) ->
    super()

    geom = new CoffeeGL.Shapes.Cylinder @thickness,24,@segments,@length
    
    # Flatten the y values to 0 (might need to be 1)
    for vert in geom.v
      vert.p.y = 0.0

    @add geom

    # Create a set of matrices to pass to the shader - a matrix palette
    @matrices = []
    for i in [0..@segments]
      @matrices.push new CoffeeGL.Matrix4()


  update : (data) ->
  
    # Potential Hot Path?

    for i in [0..@segments]

      phys = data.segments[i]

      @matrices[i].identity()
      tq = new CoffeeGL.Quaternion( new CoffeeGL.Vec3(phys.q[0],phys.q[1],phys.q[2]) , phys.q[3])
      
      #tv = new CoffeeGL.Vec3 phys.rax, phys.ray, phys.raz
      #tq.fromAxisAngle tv,phys.ra
      
      tmatrix = tq.getMatrix4()
      tmatrix.setPos new CoffeeGL.Vec3 phys.x, phys.y, phys.z
      @matrices[i].copyFrom tmatrix
      #@matrices[i].setPos new CoffeeGL.Vec3 phys.x, phys.y, phys.z

module.exports = 
  EquatorieString : EquatorieString