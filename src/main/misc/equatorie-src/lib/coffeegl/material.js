// Generated by CoffeeScript 1.6.1

/* ABOUT
                       __  .__              ________ 
   ______ ____   _____/  |_|__| ____   ____/   __   \
  /  ___// __ \_/ ___\   __\  |/  _ \ /    \____    /
  \___ \\  ___/\  \___|  | |  (  <_> )   |  \ /    / 
 /____  >\___  >\___  >__| |__|\____/|___|  //____/  .co.uk
      \/     \/     \/                    \/         
                                              CoffeeGL
                                              Benjamin Blundell - ben@section9.co.uk
                                              http://www.section9.co.uk

This software is released under the MIT Licence. See LICENCE.txt for details
*/


(function() {
  var Material, RGB, RGBA, _ref;

  _ref = require("./colour"), RGB = _ref.RGB, RGBA = _ref.RGBA;

  /*Material
  */


  Material = (function() {

    function Material(ambient, diffuse, specular, shine, emissive) {
      this.ambient = ambient;
      this.diffuse = diffuse;
      this.specular = specular;
      this.shine = shine;
      this.emissive = emissive;
      if (this.ambient == null) {
        this.ambient = new RGB(0, 0, 0);
      }
      if (this.diffuse == null) {
        this.diffuse = new RGB(1.0, 1.0, 1.0);
      }
      if (this.specular == null) {
        this.specular = new RGB(1.0, 1.0, 1.0);
      }
      if (this.shine == null) {
        this.shine = 20.0;
      }
      if (this.emissive == null) {
        this.emissive = new RGB(0.0, 0.0, 0.0);
      }
    }

    Material.prototype._addToNode = function(node) {
      return node.material = this;
    };

    return Material;

  })();

  module.exports = {
    Material: Material
  };

}).call(this);