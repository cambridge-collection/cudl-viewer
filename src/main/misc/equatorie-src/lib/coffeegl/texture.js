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


TODO - Probably pass in data and have a convinence method that calls a request?
  - handling no RGBA textures like JPGS?

https://developer.mozilla.org/en-US/docs/WebGL/Animating_textures_in_WebGL


Texture Objects - uses the request object and callbacks. Is bound to a context

- TODO 
  * How does this match with textures in the current shader context? Check that!
  * Video textures and compressed textures as per the spec for HTML5
*/


(function() {
  var Request, Texture, TextureBase, TextureCube,
    __hasProp = {}.hasOwnProperty,
    __extends = function(child, parent) { for (var key in parent) { if (__hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; };

  Request = require('./request').Request;

  /*TextureBase
  */


  TextureBase = (function() {

    function TextureBase(params) {
      var gl;
      if (CoffeeGL.Context.gl == null) {
        console.log("Error - no context or url provided for texture");
      }
      gl = CoffeeGL.Context.gl;
      if (params == null) {
        params = {};
      }
      this.loaded = false;
      this.unit = params.unit == null ? 0 : params.unit;
      this.min = params.min == null ? gl.LINEAR : params.min;
      this.max = params.max == null ? gl.LINEAR : params.max;
      this.wraps = params.wraps == null ? gl.CLAMP_TO_EDGE : params.wraps;
      this.wrapt = params.wrapt == null ? gl.CLAMP_TO_EDGE : params.wrapt;
      this.width = params.width == null ? 512 : params.width;
      this.height = params.height == null ? 512 : params.height;
      this.channels = params.channels == null ? gl.RGBA : params.channels;
      this.datatype = params.datatype == null ? gl.UNSIGNED_BYTE : params.datatype;
      this.texture = gl.createTexture();
    }

    TextureBase.prototype._isPowerOfTwo = function(x) {
      return (x & (x - 1)) === 0;
    };

    TextureBase.prototype._nextHighestPowerOfTwo = function(x) {
      var i;
      --x;
      i = 1;
      while (i < 32) {
        x = x | x >> i;
        i <<= 1;
      }
      return x + 1;
    };

    TextureBase.prototype.build = function(passed_context) {
      var context, gl;
      context = CoffeeGL.Context;
      if (passed_context != null) {
        context = passed_context;
      }
      if (!context) {
        return;
      }
      gl = context.gl;
      gl.bindTexture(gl.TEXTURE_2D, this.texture);
      gl.texImage2D(gl.TEXTURE_2D, 0, this.channels, this.width, this.height, 0, this.channels, this.datatype, null);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, this.max);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, this.min);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, this.wraps);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, this.wrapt);
      gl.bindTexture(gl.TEXTURE_2D, null);
      this.loaded = true;
      return this;
    };

    TextureBase.prototype.bind = function() {
      var gl;
      gl = CoffeeGL.Context.gl;
      if ((gl != null) && this.loaded) {
        gl.activeTexture(gl.TEXTURE0 + this.unit);
        gl.bindTexture(gl.TEXTURE_2D, this.texture);
      }
      return this;
    };

    TextureBase.prototype.unbind = function() {
      var gl;
      gl = CoffeeGL.Context.gl;
      if (this.loaded && (gl != null)) {
        gl.activeTexture(gl.TEXTURE0 + this.unit);
        gl.bindTexture(gl.TEXTURE_2D, null);
      }
      return this;
    };

    TextureBase.prototype._addToNode = function(node) {
      node.textures.push(this);
      return this;
    };

    TextureBase.prototype._removeFromNode = function(node) {
      node.textures.splice(node.textures.indexOf(this));
      return this;
    };

    TextureBase.prototype.washUp = function() {
      var gl;
      gl = CoffeeGL.Context.gl;
      gl.deleteTexture(this.texure);
      return this;
    };

    return TextureBase;

  })();

  /*Texture
  */


  Texture = (function(_super) {

    __extends(Texture, _super);

    function Texture(url, params, callback) {
      var gl, r,
        _this = this;
      this.url = url;
      this.callback = callback;
      Texture.__super__.constructor.call(this, params);
      if ((this.url == null) || (CoffeeGL.Context.gl == null)) {
        console.log("Error - no context or url provided for texture");
      }
      gl = CoffeeGL.Context.gl;
      r = new Request(this.url);
      r.get(function() {
        var cc;
        _this.texImage = new Image();
        _this.texImage.src = _this.url;
        cc = CoffeeGL.Context;
        return _this.texImage.onload = function() {
          CoffeeGL.Context.switchContext(cc);
          _this.width = _this.texImage.width;
          _this.height = _this.texImage.height;
          _this.build(_this.texImage);
          _this.loaded = true;
          return typeof _this.callback === "function" ? _this.callback() : void 0;
        };
      });
    }

    Texture.prototype.build = function(texImage, passed_context) {
      var context, gl;
      context = CoffeeGL.Context;
      if (passed_context != null) {
        context = passed_context;
      }
      if (!context) {
        return;
      }
      gl = context.gl;
      gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);
      gl.bindTexture(gl.TEXTURE_2D, this.texture);
      gl.texImage2D(gl.TEXTURE_2D, 0, this.channels, this.channels, this.datatype, texImage);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, this.min);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, this.max);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, this.wraps);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, this.wrapt);
      return gl.bindTexture(gl.TEXTURE_2D, null);
    };

    Texture;

    return Texture;

  })(TextureBase);

  /*TextureCube
  */


  TextureCube = (function(_super) {

    __extends(TextureCube, _super);

    function TextureCube(paths, params) {
      var cc, i, loadedTextures, _i,
        _this = this;
      this.paths = paths;
      TextureCube.__super__.constructor.call(this, params);
      if ((this.paths == null) || (CoffeeGL.Context == null)) {
        console.log("Error - no context or urls provided for texture");
      }
      this.texImages = [];
      loadedTextures = 0;
      for (i = _i = 0; _i <= 5; i = ++_i) {
        this.texImages[i] = new Image();
        this.texImages[i].cubeID = i;
        cc = CoffeeGL.Context;
        this.texImages[i].onload = function() {
          var gl, j, _j;
          CoffeeGL.Context.switchContext(cc);
          gl = CoffeeGL.Context.gl;
          loadedTextures++;
          if (loadedTextures === 6) {
            gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);
            gl.bindTexture(gl.TEXTURE_CUBE_MAP, _this.texture);
            gl.texParameteri(gl.TEXTURE_CUBE_MAP, gl.TEXTURE_MAG_FILTER, _this.max);
            gl.texParameteri(gl.TEXTURE_CUBE_MAP, gl.TEXTURE_MIN_FILTER, _this.min);
            gl.texParameteri(gl.TEXTURE_CUBE_MAP, gl.TEXTURE_WRAP_S, _this.wraps);
            gl.texParameteri(gl.TEXTURE_CUBE_MAP, gl.TEXTURE_WRAP_T, _this.wrapt);
            for (j = _j = 0; _j <= 5; j = ++_j) {
              gl.texImage2D(gl.TEXTURE_CUBE_MAP_POSITIVE_X + j, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, _this.texImages[j]);
            }
            gl.bindTexture(gl.TEXTURE_CUBE_MAP, null);
            return _this.loaded = true;
          }
        };
        this.texImages[i].src = this.paths[i];
      }
    }

    TextureCube.prototype.bind = function(unit) {
      var gl;
      gl = CoffeeGL.Context.gl;
      if ((gl != null) && this.loaded) {
        gl.activeTexture(gl.TEXTURE0 + this.unit);
        return gl.bindTexture(gl.TEXTURE_CUBE_MAP, this.texture);
      }
    };

    TextureCube.prototype.unbind = function() {
      var gl;
      gl = CoffeeGL.Context.gl;
      if (this.loaded && (gl != null)) {
        gl.activeTexture(gl.TEXTURE0 + this.unit);
        return gl.bindTexture(gl.TEXTURE_CUBE_MAP, null);
      }
    };

    return TextureCube;

  })(TextureBase);

  module.exports = {
    Texture: Texture,
    TextureBase: TextureBase,
    TextureCube: TextureCube
  };

}).call(this);