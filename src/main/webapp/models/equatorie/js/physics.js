// Generated by CoffeeScript 1.6.3
/*

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
*/


(function() {
  var PhysicsString, interval;

  importScripts('cannon.js');

  this.ping = 0;

  this.string_height = 0.4;

  this.bodies = [];

  this.constraints = [];

  PhysicsString = (function() {
    function PhysicsString(length, thickness, segments, start, end, world, bodies, constraints) {
      var c, i, mass, radius, seglength, sphereBody, sphereShape, _i, _j, _ref, _ref1;
      seglength = length / segments;
      this.children = [];
      this.length = length;
      mass = 1;
      radius = seglength / 6;
      for (i = _i = 0, _ref = segments - 2; 0 <= _ref ? _i <= _ref : _i >= _ref; i = 0 <= _ref ? ++_i : --_i) {
        sphereShape = new CANNON.Sphere(radius);
        sphereBody = new CANNON.RigidBody(mass, sphereShape);
        sphereBody.angularDamping = 0.99;
        sphereBody.linearDamping = 0.99;
        sphereBody.position.set(0, 0, 15.0 + (i * seglength));
        this.children.push(sphereBody);
        bodies.push(sphereBody);
        world.add(sphereBody);
      }
      for (i = _j = 0, _ref1 = this.children.length - 2; 0 <= _ref1 ? _j <= _ref1 : _j >= _ref1; i = 0 <= _ref1 ? ++_j : --_j) {
        c = new CANNON.DistanceConstraint(this.children[i], this.children[i + 1], seglength / 1.5);
        world.addConstraint(c);
        constraints.push(c);
      }
      sphereShape = new CANNON.Sphere(radius);
      this.start = new CANNON.RigidBody(0, sphereShape);
      this.start.position.set(start.x, start.z, start.y);
      c = new CANNON.DistanceConstraint(this.children[0], this.start, seglength / 1.5);
      world.addConstraint(c, true);
      world.add(this.start);
      bodies.push(this.start);
      constraints.push(c);
      this.end = new CANNON.RigidBody(0, sphereShape);
      this.end.position.set(end.x, end.z, end.y);
      c = new CANNON.DistanceConstraint(this.children[this.children.length - 1], this.end, 0.05);
      world.addConstraint(c, true);
      world.add(this.end);
      bodies.push(this.end);
      constraints.push(c);
    }

    PhysicsString.prototype.update = function() {
      var list, segment, _getTrans, _i, _len, _ref,
        _this = this;
      list = [];
      _getTrans = function(b) {
        var obj;
        obj = {};
        obj.q = [b.quaternion.x, b.quaternion.z, b.quaternion.y, b.quaternion.w];
        obj.x = b.position.x;
        obj.y = b.position.z;
        obj.z = b.position.y;
        return obj;
      };
      list.push(_getTrans(this.start));
      _ref = this.children;
      for (_i = 0, _len = _ref.length; _i < _len; _i++) {
        segment = _ref[_i];
        list.push(_getTrans(segment));
      }
      list.push(_getTrans(this.end));
      return list;
    };

    return PhysicsString;

  })();

  interval = null;

  this.startUp = function() {
    var baseBody, baseShape, groundBody, groundShape, last, mainLoop;
    this.dynamicsWorld = new CANNON.World();
    this.dynamicsWorld.gravity.set(0, 0, -9.82);
    this.dynamicsWorld.broadphase = new CANNON.NaiveBroadphase();
    baseShape = new CANNON.Cylinder(6.0, 6.0, 0.1, 12);
    baseBody = new CANNON.RigidBody(0, baseShape);
    this.dynamicsWorld.add(baseBody);
    groundShape = new CANNON.Plane();
    groundBody = new CANNON.RigidBody(0, groundShape);
    this.dynamicsWorld.add(groundBody);
    this.white_string = new PhysicsString(8.2, 0.015, 20, {
      x: 2,
      y: 0.2,
      z: 2
    }, {
      x: -2,
      y: 0.2,
      z: -2
    }, this.dynamicsWorld, this.bodies, this.constraints);
    this.black_string = new PhysicsString(8.2, 0.015, 20, {
      x: -2,
      y: 0.2,
      z: 2
    }, {
      x: -4,
      y: 0.2,
      z: 2
    }, this.dynamicsWorld, this.bodies, this.constraints);
    last = Date.now();
    mainLoop = function() {
      var now;
      now = Date.now();
      simulate(now - last);
      return last = now;
    };
    if (interval) {
      clearInterval(interval);
    }
    return interval = setInterval(mainLoop, 1000 / 60);
  };

  this.moveBody = function(body, pos) {
    var data;
    body.position.x = pos.x;
    body.position.y = pos.z;
    body.position.z = pos.y;
    data = {
      black: {
        segments: []
      },
      white: {
        segments: []
      }
    };
    data.white.segments = this.white_string.update();
    data.black.segments = this.black_string.update();
    return postMessage({
      cmd: 'physics',
      data: data
    });
  };

  this.simulate = function(dt) {
    var data;
    dt = dt || 1;
    this.dynamicsWorld.step(1.0 / 60.0);
    data = {
      black: {
        segments: []
      },
      white: {
        segments: []
      }
    };
    data.white.segments = this.white_string.update();
    data.black.segments = this.black_string.update();
    return postMessage({
      cmd: 'physics',
      data: data
    });
  };

  this.reset = function() {
    var c, _i, _j, _len, _len1, _ref, _ref1;
    _ref = this.bodies;
    for (_i = 0, _len = _ref.length; _i < _len; _i++) {
      c = _ref[_i];
      this.dynamicsWorld.remove(c);
    }
    _ref1 = this.constraints;
    for (_j = 0, _len1 = _ref1.length; _j < _len1; _j++) {
      c = _ref1[_j];
      this.dynamicsWorld.removeConstraint(c);
    }
    return startUp();
  };

  this.onmessage = function(event) {
    switch (event.data.cmd) {
      case "startup":
        return startUp();
      case "reset":
        return reset();
      case "white_start_move":
        return moveBody(white_string.start, event.data.data);
      case "white_end_move":
        return moveBody(white_string.end, event.data.data);
      case "black_start_move":
        return moveBody(black_string.start, event.data.data);
      case "black_end_move":
        return moveBody(black_string.end, event.data.data);
      default:
        return postMessage(event.data.cmd);
    }
  };

}).call(this);