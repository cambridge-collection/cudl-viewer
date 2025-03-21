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

 * TODO
  - Many forms of interpolation - provide an optional function here (S curves, hermite etc)
*/


(function() {
  var CoffeeGLWarning, Interpolation, Matrix4, RGB, RGBA, Vec2, Vec3, Vec4, _ref, _ref1;

  _ref = require('./math'), Vec2 = _ref.Vec2, Vec3 = _ref.Vec3, Vec4 = _ref.Vec4, Matrix4 = _ref.Matrix4;

  _ref1 = require('./colour'), RGB = _ref1.RGB, RGBA = _ref1.RGBA;

  CoffeeGLWarning = require('./error').CoffeeGLWarning;

  /* Interpolation
  */


  Interpolation = (function() {

    function Interpolation(obj0, obj1) {
      this.obj0 = obj0;
      this.obj1 = obj1;
      if (typeof this.obj0 === 'object') {
        if (this.obj0.__proto__ !== this.obj1.__proto__) {
          CoffeeGLWarning("Interpolating two different objects");
        }
      }
      if (this.obj0 instanceof Vec2) {
        this._set = this._setVec2;
      } else if (this.obj0 instanceof Vec3) {
        this._set = this._setVec3;
      } else if (this.obj0 instanceof Vec4) {
        this._set = this._setVec4;
      } else if (this.obj0 instanceof RGB) {
        this._set = this._setRGB;
      } else if (this.obj0 instanceof RGBA) {
        this._set = this._setRGBA;
      } else if (typeof obj0 === 'number') {
        this._set = this._setScalar;
      }
      this._value = 0;
    }

    Interpolation.prototype.set = function(val) {
      this._value = val;
      return this._set();
    };

    Interpolation.prototype._setVec2 = function() {
      return new Vec2(this.obj0.x + ((this.obj.x - this.obj0.x) * this._value), this.obj0.y + ((this.obj1.y - this.obj0.y) * this._value));
    };

    Interpolation.prototype._setVec3 = function() {
      return new Vec3(this.obj0.x + ((this.obj1.x - this.obj0.x) * this._value), this.obj0.y + ((this.obj1.y - this.obj0.y) * this._value), this.obj0.z + ((this.obj1.z - this.obj0.z) * this._value));
    };

    Interpolation.prototype._setVec4 = function() {
      return new Vec4(this.obj0.x + ((this.obj1.x - this.obj0.x) * this._value), this.obj0.y + ((this.obj1.y - this.obj0.y) * this._value), this.obj0.z + ((this.obj1.z - this.obj0.z) * this._value), this.obj0.w + ((this.obj1.w - this.obj0.w) * this._value));
    };

    Interpolation.prototype._setRGB = function() {
      return new RGB(this.obj0.r + ((this.obj1.r - this.obj0.r) * this._value), this.obj0.g + ((this.obj1.g - this.obj0.g) * this._value), this.obj0.b + ((this.obj1.b - this.obj0.b) * this._value));
    };

    Interpolation.prototype._setRGBA = function() {
      return new RGBA(this.obj0.r + ((this.obj1.r - this.obj0.r) * this._value), this.obj0.g + ((this.obj1.g - this.obj0.g) * this._value), this.obj0.b + ((this.obj1.b - this.obj0.b) * this._value), this.obj0.a + ((this.obj1.a - this.obj0.a) * this._value));
    };

    Interpolation.prototype._setScalar = function() {
      return this.obj0 + ((this.obj1 - this.obj0) * this._value);
    };

    return Interpolation;

  })();

  module.exports = {
    Interpolation: Interpolation
  };

}).call(this);
