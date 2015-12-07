var tagging =
/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	"use strict";

	__webpack_require__(1);
	__webpack_require__(2);

/***/ },
/* 1 */
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },
/* 2 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});
	exports['default'] = setUpTaggingTab;

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	var _cudl = __webpack_require__(3);

	var _cudl2 = _interopRequireDefault(_cudl);

	/** controllers */

	var _controllersRestclientagent = __webpack_require__(4);

	var _controllersRestclientagent2 = _interopRequireDefault(_controllersRestclientagent);

	var _controllersTaggingcontroller = __webpack_require__(77);

	var _controllersTaggingcontroller2 = _interopRequireDefault(_controllersTaggingcontroller);

	/** models */

	var _modelsMetadata = __webpack_require__(106);

	var _modelsMetadata2 = _interopRequireDefault(_modelsMetadata);

	function setUpTaggingTab(data, docId) {

	    if (taggable === 'false') {
	        console.log('Tagging is disabled');
	        // remove tagging tab and panel
	        $('#taggingtab').parent().remove();
	        $('#tagging').remove();
	        return false;
	    }

	    //
	    //
	    //

	    var metadata = new _modelsMetadata2['default'](data, docId);

	    var ajax_c = new _controllersRestclientagent2['default']();

	    var tagging_c = new _controllersTaggingcontroller2['default']({
	        metadata: metadata,
	        // page: page,
	        ajax_c: ajax_c
	    }).init();

	    //
	    // bootstrap tab event handlers
	    //

	    $('#taggingtab').on('show.bs.tab', function (e) {
	        // check authentication
	        if (global_user === "false") {
	            // prevent tab panel from showing before
	            // redirecting user to login page
	            e.preventDefault();
	            e.stopPropagation();

	            // get relative page url for this page
	            var a = document.createElement('a');
	            a.href = window.location.href;

	            window.location.href = '/auth/login?access=' + encodeURIComponent(a.pathname + '#tagging');
	        } else {
	            // start tagging
	            tagging_c.startTagging();
	        }
	    });

	    $('#taggingtab').on('hide.bs.tab', function (e) {
	        // end tagging
	        tagging_c.endTagging();
	    });
	}

	// Export entry point to cudl.
	_cudl2['default'].setupTaggingTab = setUpTaggingTab;

	//
	// this tagging library assumes the presence of cudl, cudl.viewer and cudl.pagnum
	//
	// user attribute is passed from session to js variable 'global_user' in document.jsp page, which is
	// used to check if user has been authenticated. this is just a temporary solution. in practice, should
	// use cookie - server sends a small piece of cookie indicating user authentication status back to
	// client broser. the cookie should have the same expiry date.
	//
	module.exports = exports['default'];

/***/ },
/* 3 */
/***/ function(module, exports) {

	module.exports = cudl;

/***/ },
/* 4 */
/***/ function(module, exports, __webpack_require__) {

	"use strict";

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	var _lodashLang = __webpack_require__(5);

	var _lodashLang2 = _interopRequireDefault(_lodashLang);

	var path = __webpack_require__(75);

	var RestClientAgent = (function () {
	    function RestClientAgent() {
	        _classCallCheck(this, RestClientAgent);
	    }

	    /**
	     * api endpoints
	     */

	    _createClass(RestClientAgent, [{
	        key: 'addOrUpdateAnnotation',

	        /** add or update annotation */
	        value: function addOrUpdateAnnotation(anno, callback) {
	            var url = path.join(API_ADD_ANNO, anno.getDocumentId(), anno.getPageNum().toString());

	            this._ajax({ url: url, data: anno }, function (resp) {
	                callback(anno, resp);
	            });
	        }

	        /** remove tag or undo removed tag */
	    }, {
	        key: 'addOrUpdateRemovedTag',
	        value: function addOrUpdateRemovedTag(tag, callback) {
	            var url = path.join(API_ADD_RTAG, tag.getDocumentId());

	            this._ajax({ url: url, data: tag }, function (resp) {
	                callback(tag, resp);
	            });
	        }

	        /** remove annotation */
	    }, {
	        key: 'removeAnnotation',
	        value: function removeAnnotation(anno, callback) {
	            var url = path.join(API_RMV_ANNO, anno.getDocumentId(), anno.getUUID());

	            this._ajax({ url: url, data: anno }, function (resp) {
	                callback(anno, resp);
	            });
	        }

	        /** get annotations by document id */
	    }, {
	        key: 'getAnnotations',
	        value: function getAnnotations(docId, page, callback) {
	            var url = path.join(API_GET_ANNO, docId, page.toString());

	            this._ajax({ url: url }, function (resp) {
	                console.log('anno found: ' + resp.result.annotations.length);
	                callback(resp.result.annotations);
	            });
	        }

	        /** get tags by document id */
	    }, {
	        key: 'getTags',
	        value: function getTags(docId, callback) {
	            var url = path.join(API_GET_TAG, docId);

	            this._ajax({ url: url }, function (resp) {
	                console.log('tags found: ' + resp.result.terms.length);
	                callback(resp.result.terms);
	            });
	        }

	        /** get remove tags */
	    }, {
	        key: 'getRemovedTags',
	        value: function getRemovedTags(docId, callback) {
	            var url = path.join(API_GET_RTAG, docId);

	            this._ajax({ url: url }, function (resp) {
	                console.log('removed tags found: ' + resp.result.tags.length);
	                callback(resp.result.tags);
	            });
	        }

	        /**
	         * @opts.url    string, required
	         * @opts.data   object, optional
	         * callback     function, required
	         */

	    }, {
	        key: '_ajax',
	        value: function _ajax(opts, callback) {

	            console.log(opts.url);
	            // console.log('payload: '+opts.data);

	            var method = _lodashLang2['default'].isUndefined(opts.data) ? 'GET' : 'POST';
	            var payload = _lodashLang2['default'].isUndefined(opts.data) ? '' : JSON.stringify(opts.data);
	            var cntType = _lodashLang2['default'].isUndefined(opts.data) ? 'text/plain' : 'application/json; charset=utf-8';

	            $.ajax({
	                url: opts.url,
	                type: method,
	                data: payload,
	                contentType: cntType,
	                dataType: 'json', // response data type
	                timeout: timeout }). // timeout in milliseconds
	            done(function (data, status) {
	                // do something
	            }).success(function (data, status) {
	                if (data.redirect) {
	                    // user unauthorized, redirect to login page
	                    window.location.href = data.redirectURL;
	                } else {
	                    callback(data);
	                }
	            }).fail(function (jqxhr, status, error) {
	                console.log('ajax fail: ' + status);
	            });
	        }
	    }]);

	    return RestClientAgent;
	})();

	exports['default'] = RestClientAgent;
	var BASE = '/crowdsourcing';
	var API_ADD_ANNO = BASE + '/anno/update';
	var API_GET_ANNO = BASE + '/anno/get';
	var API_RMV_ANNO = BASE + '/anno/remove';
	var API_ADD_RTAG = BASE + '/rmvtag/update';
	var API_GET_RTAG = BASE + '/rmvtag/get';
	var API_GET_TAG = BASE + '/tag/get';
	var API_AUTH = BASE + '/auth';

	var timeout = 10000; // milliseconds
	module.exports = exports['default'];

/***/ },
/* 5 */
/***/ function(module, exports, __webpack_require__) {

	module.exports = {
	  'clone': __webpack_require__(6),
	  'cloneDeep': __webpack_require__(38),
	  'eq': __webpack_require__(39),
	  'gt': __webpack_require__(48),
	  'gte': __webpack_require__(49),
	  'isArguments': __webpack_require__(23),
	  'isArray': __webpack_require__(24),
	  'isBoolean': __webpack_require__(50),
	  'isDate': __webpack_require__(51),
	  'isElement': __webpack_require__(52),
	  'isEmpty': __webpack_require__(55),
	  'isEqual': __webpack_require__(40),
	  'isError': __webpack_require__(57),
	  'isFinite': __webpack_require__(58),
	  'isFunction': __webpack_require__(15),
	  'isMatch': __webpack_require__(59),
	  'isNaN': __webpack_require__(64),
	  'isNative': __webpack_require__(14),
	  'isNull': __webpack_require__(66),
	  'isNumber': __webpack_require__(65),
	  'isObject': __webpack_require__(16),
	  'isPlainObject': __webpack_require__(53),
	  'isRegExp': __webpack_require__(67),
	  'isString': __webpack_require__(56),
	  'isTypedArray': __webpack_require__(47),
	  'isUndefined': __webpack_require__(68),
	  'lt': __webpack_require__(69),
	  'lte': __webpack_require__(70),
	  'toArray': __webpack_require__(71),
	  'toPlainObject': __webpack_require__(74)
	};


/***/ },
/* 6 */
/***/ function(module, exports, __webpack_require__) {

	var baseClone = __webpack_require__(7),
	    bindCallback = __webpack_require__(35),
	    isIterateeCall = __webpack_require__(37);

	/**
	 * Creates a clone of `value`. If `isDeep` is `true` nested objects are cloned,
	 * otherwise they are assigned by reference. If `customizer` is provided it's
	 * invoked to produce the cloned values. If `customizer` returns `undefined`
	 * cloning is handled by the method instead. The `customizer` is bound to
	 * `thisArg` and invoked with up to three argument; (value [, index|key, object]).
	 *
	 * **Note:** This method is loosely based on the
	 * [structured clone algorithm](http://www.w3.org/TR/html5/infrastructure.html#internal-structured-cloning-algorithm).
	 * The enumerable properties of `arguments` objects and objects created by
	 * constructors other than `Object` are cloned to plain `Object` objects. An
	 * empty object is returned for uncloneable values such as functions, DOM nodes,
	 * Maps, Sets, and WeakMaps.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to clone.
	 * @param {boolean} [isDeep] Specify a deep clone.
	 * @param {Function} [customizer] The function to customize cloning values.
	 * @param {*} [thisArg] The `this` binding of `customizer`.
	 * @returns {*} Returns the cloned value.
	 * @example
	 *
	 * var users = [
	 *   { 'user': 'barney' },
	 *   { 'user': 'fred' }
	 * ];
	 *
	 * var shallow = _.clone(users);
	 * shallow[0] === users[0];
	 * // => true
	 *
	 * var deep = _.clone(users, true);
	 * deep[0] === users[0];
	 * // => false
	 *
	 * // using a customizer callback
	 * var el = _.clone(document.body, function(value) {
	 *   if (_.isElement(value)) {
	 *     return value.cloneNode(false);
	 *   }
	 * });
	 *
	 * el === document.body
	 * // => false
	 * el.nodeName
	 * // => BODY
	 * el.childNodes.length;
	 * // => 0
	 */
	function clone(value, isDeep, customizer, thisArg) {
	  if (isDeep && typeof isDeep != 'boolean' && isIterateeCall(value, isDeep, customizer)) {
	    isDeep = false;
	  }
	  else if (typeof isDeep == 'function') {
	    thisArg = customizer;
	    customizer = isDeep;
	    isDeep = false;
	  }
	  return typeof customizer == 'function'
	    ? baseClone(value, isDeep, bindCallback(customizer, thisArg, 3))
	    : baseClone(value, isDeep);
	}

	module.exports = clone;


/***/ },
/* 7 */
/***/ function(module, exports, __webpack_require__) {

	var arrayCopy = __webpack_require__(8),
	    arrayEach = __webpack_require__(9),
	    baseAssign = __webpack_require__(10),
	    baseForOwn = __webpack_require__(27),
	    initCloneArray = __webpack_require__(31),
	    initCloneByTag = __webpack_require__(32),
	    initCloneObject = __webpack_require__(34),
	    isArray = __webpack_require__(24),
	    isObject = __webpack_require__(16);

	/** `Object#toString` result references. */
	var argsTag = '[object Arguments]',
	    arrayTag = '[object Array]',
	    boolTag = '[object Boolean]',
	    dateTag = '[object Date]',
	    errorTag = '[object Error]',
	    funcTag = '[object Function]',
	    mapTag = '[object Map]',
	    numberTag = '[object Number]',
	    objectTag = '[object Object]',
	    regexpTag = '[object RegExp]',
	    setTag = '[object Set]',
	    stringTag = '[object String]',
	    weakMapTag = '[object WeakMap]';

	var arrayBufferTag = '[object ArrayBuffer]',
	    float32Tag = '[object Float32Array]',
	    float64Tag = '[object Float64Array]',
	    int8Tag = '[object Int8Array]',
	    int16Tag = '[object Int16Array]',
	    int32Tag = '[object Int32Array]',
	    uint8Tag = '[object Uint8Array]',
	    uint8ClampedTag = '[object Uint8ClampedArray]',
	    uint16Tag = '[object Uint16Array]',
	    uint32Tag = '[object Uint32Array]';

	/** Used to identify `toStringTag` values supported by `_.clone`. */
	var cloneableTags = {};
	cloneableTags[argsTag] = cloneableTags[arrayTag] =
	cloneableTags[arrayBufferTag] = cloneableTags[boolTag] =
	cloneableTags[dateTag] = cloneableTags[float32Tag] =
	cloneableTags[float64Tag] = cloneableTags[int8Tag] =
	cloneableTags[int16Tag] = cloneableTags[int32Tag] =
	cloneableTags[numberTag] = cloneableTags[objectTag] =
	cloneableTags[regexpTag] = cloneableTags[stringTag] =
	cloneableTags[uint8Tag] = cloneableTags[uint8ClampedTag] =
	cloneableTags[uint16Tag] = cloneableTags[uint32Tag] = true;
	cloneableTags[errorTag] = cloneableTags[funcTag] =
	cloneableTags[mapTag] = cloneableTags[setTag] =
	cloneableTags[weakMapTag] = false;

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * The base implementation of `_.clone` without support for argument juggling
	 * and `this` binding `customizer` functions.
	 *
	 * @private
	 * @param {*} value The value to clone.
	 * @param {boolean} [isDeep] Specify a deep clone.
	 * @param {Function} [customizer] The function to customize cloning values.
	 * @param {string} [key] The key of `value`.
	 * @param {Object} [object] The object `value` belongs to.
	 * @param {Array} [stackA=[]] Tracks traversed source objects.
	 * @param {Array} [stackB=[]] Associates clones with source counterparts.
	 * @returns {*} Returns the cloned value.
	 */
	function baseClone(value, isDeep, customizer, key, object, stackA, stackB) {
	  var result;
	  if (customizer) {
	    result = object ? customizer(value, key, object) : customizer(value);
	  }
	  if (result !== undefined) {
	    return result;
	  }
	  if (!isObject(value)) {
	    return value;
	  }
	  var isArr = isArray(value);
	  if (isArr) {
	    result = initCloneArray(value);
	    if (!isDeep) {
	      return arrayCopy(value, result);
	    }
	  } else {
	    var tag = objToString.call(value),
	        isFunc = tag == funcTag;

	    if (tag == objectTag || tag == argsTag || (isFunc && !object)) {
	      result = initCloneObject(isFunc ? {} : value);
	      if (!isDeep) {
	        return baseAssign(result, value);
	      }
	    } else {
	      return cloneableTags[tag]
	        ? initCloneByTag(value, tag, isDeep)
	        : (object ? value : {});
	    }
	  }
	  // Check for circular references and return its corresponding clone.
	  stackA || (stackA = []);
	  stackB || (stackB = []);

	  var length = stackA.length;
	  while (length--) {
	    if (stackA[length] == value) {
	      return stackB[length];
	    }
	  }
	  // Add the source value to the stack of traversed objects and associate it with its clone.
	  stackA.push(value);
	  stackB.push(result);

	  // Recursively populate clone (susceptible to call stack limits).
	  (isArr ? arrayEach : baseForOwn)(value, function(subValue, key) {
	    result[key] = baseClone(subValue, isDeep, customizer, key, value, stackA, stackB);
	  });
	  return result;
	}

	module.exports = baseClone;


/***/ },
/* 8 */
/***/ function(module, exports) {

	/**
	 * Copies the values of `source` to `array`.
	 *
	 * @private
	 * @param {Array} source The array to copy values from.
	 * @param {Array} [array=[]] The array to copy values to.
	 * @returns {Array} Returns `array`.
	 */
	function arrayCopy(source, array) {
	  var index = -1,
	      length = source.length;

	  array || (array = Array(length));
	  while (++index < length) {
	    array[index] = source[index];
	  }
	  return array;
	}

	module.exports = arrayCopy;


/***/ },
/* 9 */
/***/ function(module, exports) {

	/**
	 * A specialized version of `_.forEach` for arrays without support for callback
	 * shorthands and `this` binding.
	 *
	 * @private
	 * @param {Array} array The array to iterate over.
	 * @param {Function} iteratee The function invoked per iteration.
	 * @returns {Array} Returns `array`.
	 */
	function arrayEach(array, iteratee) {
	  var index = -1,
	      length = array.length;

	  while (++index < length) {
	    if (iteratee(array[index], index, array) === false) {
	      break;
	    }
	  }
	  return array;
	}

	module.exports = arrayEach;


/***/ },
/* 10 */
/***/ function(module, exports, __webpack_require__) {

	var baseCopy = __webpack_require__(11),
	    keys = __webpack_require__(12);

	/**
	 * The base implementation of `_.assign` without support for argument juggling,
	 * multiple sources, and `customizer` functions.
	 *
	 * @private
	 * @param {Object} object The destination object.
	 * @param {Object} source The source object.
	 * @returns {Object} Returns `object`.
	 */
	function baseAssign(object, source) {
	  return source == null
	    ? object
	    : baseCopy(source, keys(source), object);
	}

	module.exports = baseAssign;


/***/ },
/* 11 */
/***/ function(module, exports) {

	/**
	 * Copies properties of `source` to `object`.
	 *
	 * @private
	 * @param {Object} source The object to copy properties from.
	 * @param {Array} props The property names to copy.
	 * @param {Object} [object={}] The object to copy properties to.
	 * @returns {Object} Returns `object`.
	 */
	function baseCopy(source, props, object) {
	  object || (object = {});

	  var index = -1,
	      length = props.length;

	  while (++index < length) {
	    var key = props[index];
	    object[key] = source[key];
	  }
	  return object;
	}

	module.exports = baseCopy;


/***/ },
/* 12 */
/***/ function(module, exports, __webpack_require__) {

	var getNative = __webpack_require__(13),
	    isArrayLike = __webpack_require__(18),
	    isObject = __webpack_require__(16),
	    shimKeys = __webpack_require__(22);

	/* Native method references for those with the same name as other `lodash` methods. */
	var nativeKeys = getNative(Object, 'keys');

	/**
	 * Creates an array of the own enumerable property names of `object`.
	 *
	 * **Note:** Non-object values are coerced to objects. See the
	 * [ES spec](http://ecma-international.org/ecma-262/6.0/#sec-object.keys)
	 * for more details.
	 *
	 * @static
	 * @memberOf _
	 * @category Object
	 * @param {Object} object The object to query.
	 * @returns {Array} Returns the array of property names.
	 * @example
	 *
	 * function Foo() {
	 *   this.a = 1;
	 *   this.b = 2;
	 * }
	 *
	 * Foo.prototype.c = 3;
	 *
	 * _.keys(new Foo);
	 * // => ['a', 'b'] (iteration order is not guaranteed)
	 *
	 * _.keys('hi');
	 * // => ['0', '1']
	 */
	var keys = !nativeKeys ? shimKeys : function(object) {
	  var Ctor = object == null ? undefined : object.constructor;
	  if ((typeof Ctor == 'function' && Ctor.prototype === object) ||
	      (typeof object != 'function' && isArrayLike(object))) {
	    return shimKeys(object);
	  }
	  return isObject(object) ? nativeKeys(object) : [];
	};

	module.exports = keys;


/***/ },
/* 13 */
/***/ function(module, exports, __webpack_require__) {

	var isNative = __webpack_require__(14);

	/**
	 * Gets the native function at `key` of `object`.
	 *
	 * @private
	 * @param {Object} object The object to query.
	 * @param {string} key The key of the method to get.
	 * @returns {*} Returns the function if it's native, else `undefined`.
	 */
	function getNative(object, key) {
	  var value = object == null ? undefined : object[key];
	  return isNative(value) ? value : undefined;
	}

	module.exports = getNative;


/***/ },
/* 14 */
/***/ function(module, exports, __webpack_require__) {

	var isFunction = __webpack_require__(15),
	    isObjectLike = __webpack_require__(17);

	/** Used to detect host constructors (Safari > 5). */
	var reIsHostCtor = /^\[object .+?Constructor\]$/;

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/** Used to resolve the decompiled source of functions. */
	var fnToString = Function.prototype.toString;

	/** Used to check objects for own properties. */
	var hasOwnProperty = objectProto.hasOwnProperty;

	/** Used to detect if a method is native. */
	var reIsNative = RegExp('^' +
	  fnToString.call(hasOwnProperty).replace(/[\\^$.*+?()[\]{}|]/g, '\\$&')
	  .replace(/hasOwnProperty|(function).*?(?=\\\()| for .+?(?=\\\])/g, '$1.*?') + '$'
	);

	/**
	 * Checks if `value` is a native function.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is a native function, else `false`.
	 * @example
	 *
	 * _.isNative(Array.prototype.push);
	 * // => true
	 *
	 * _.isNative(_);
	 * // => false
	 */
	function isNative(value) {
	  if (value == null) {
	    return false;
	  }
	  if (isFunction(value)) {
	    return reIsNative.test(fnToString.call(value));
	  }
	  return isObjectLike(value) && reIsHostCtor.test(value);
	}

	module.exports = isNative;


/***/ },
/* 15 */
/***/ function(module, exports, __webpack_require__) {

	var isObject = __webpack_require__(16);

	/** `Object#toString` result references. */
	var funcTag = '[object Function]';

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * Checks if `value` is classified as a `Function` object.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is correctly classified, else `false`.
	 * @example
	 *
	 * _.isFunction(_);
	 * // => true
	 *
	 * _.isFunction(/abc/);
	 * // => false
	 */
	function isFunction(value) {
	  // The use of `Object#toString` avoids issues with the `typeof` operator
	  // in older versions of Chrome and Safari which return 'function' for regexes
	  // and Safari 8 which returns 'object' for typed array constructors.
	  return isObject(value) && objToString.call(value) == funcTag;
	}

	module.exports = isFunction;


/***/ },
/* 16 */
/***/ function(module, exports) {

	/**
	 * Checks if `value` is the [language type](https://es5.github.io/#x8) of `Object`.
	 * (e.g. arrays, functions, objects, regexes, `new Number(0)`, and `new String('')`)
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is an object, else `false`.
	 * @example
	 *
	 * _.isObject({});
	 * // => true
	 *
	 * _.isObject([1, 2, 3]);
	 * // => true
	 *
	 * _.isObject(1);
	 * // => false
	 */
	function isObject(value) {
	  // Avoid a V8 JIT bug in Chrome 19-20.
	  // See https://code.google.com/p/v8/issues/detail?id=2291 for more details.
	  var type = typeof value;
	  return !!value && (type == 'object' || type == 'function');
	}

	module.exports = isObject;


/***/ },
/* 17 */
/***/ function(module, exports) {

	/**
	 * Checks if `value` is object-like.
	 *
	 * @private
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is object-like, else `false`.
	 */
	function isObjectLike(value) {
	  return !!value && typeof value == 'object';
	}

	module.exports = isObjectLike;


/***/ },
/* 18 */
/***/ function(module, exports, __webpack_require__) {

	var getLength = __webpack_require__(19),
	    isLength = __webpack_require__(21);

	/**
	 * Checks if `value` is array-like.
	 *
	 * @private
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is array-like, else `false`.
	 */
	function isArrayLike(value) {
	  return value != null && isLength(getLength(value));
	}

	module.exports = isArrayLike;


/***/ },
/* 19 */
/***/ function(module, exports, __webpack_require__) {

	var baseProperty = __webpack_require__(20);

	/**
	 * Gets the "length" property value of `object`.
	 *
	 * **Note:** This function is used to avoid a [JIT bug](https://bugs.webkit.org/show_bug.cgi?id=142792)
	 * that affects Safari on at least iOS 8.1-8.3 ARM64.
	 *
	 * @private
	 * @param {Object} object The object to query.
	 * @returns {*} Returns the "length" value.
	 */
	var getLength = baseProperty('length');

	module.exports = getLength;


/***/ },
/* 20 */
/***/ function(module, exports) {

	/**
	 * The base implementation of `_.property` without support for deep paths.
	 *
	 * @private
	 * @param {string} key The key of the property to get.
	 * @returns {Function} Returns the new function.
	 */
	function baseProperty(key) {
	  return function(object) {
	    return object == null ? undefined : object[key];
	  };
	}

	module.exports = baseProperty;


/***/ },
/* 21 */
/***/ function(module, exports) {

	/**
	 * Used as the [maximum length](http://ecma-international.org/ecma-262/6.0/#sec-number.max_safe_integer)
	 * of an array-like value.
	 */
	var MAX_SAFE_INTEGER = 9007199254740991;

	/**
	 * Checks if `value` is a valid array-like length.
	 *
	 * **Note:** This function is based on [`ToLength`](http://ecma-international.org/ecma-262/6.0/#sec-tolength).
	 *
	 * @private
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is a valid length, else `false`.
	 */
	function isLength(value) {
	  return typeof value == 'number' && value > -1 && value % 1 == 0 && value <= MAX_SAFE_INTEGER;
	}

	module.exports = isLength;


/***/ },
/* 22 */
/***/ function(module, exports, __webpack_require__) {

	var isArguments = __webpack_require__(23),
	    isArray = __webpack_require__(24),
	    isIndex = __webpack_require__(25),
	    isLength = __webpack_require__(21),
	    keysIn = __webpack_require__(26);

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/** Used to check objects for own properties. */
	var hasOwnProperty = objectProto.hasOwnProperty;

	/**
	 * A fallback implementation of `Object.keys` which creates an array of the
	 * own enumerable property names of `object`.
	 *
	 * @private
	 * @param {Object} object The object to query.
	 * @returns {Array} Returns the array of property names.
	 */
	function shimKeys(object) {
	  var props = keysIn(object),
	      propsLength = props.length,
	      length = propsLength && object.length;

	  var allowIndexes = !!length && isLength(length) &&
	    (isArray(object) || isArguments(object));

	  var index = -1,
	      result = [];

	  while (++index < propsLength) {
	    var key = props[index];
	    if ((allowIndexes && isIndex(key, length)) || hasOwnProperty.call(object, key)) {
	      result.push(key);
	    }
	  }
	  return result;
	}

	module.exports = shimKeys;


/***/ },
/* 23 */
/***/ function(module, exports, __webpack_require__) {

	var isArrayLike = __webpack_require__(18),
	    isObjectLike = __webpack_require__(17);

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/** Used to check objects for own properties. */
	var hasOwnProperty = objectProto.hasOwnProperty;

	/** Native method references. */
	var propertyIsEnumerable = objectProto.propertyIsEnumerable;

	/**
	 * Checks if `value` is classified as an `arguments` object.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is correctly classified, else `false`.
	 * @example
	 *
	 * _.isArguments(function() { return arguments; }());
	 * // => true
	 *
	 * _.isArguments([1, 2, 3]);
	 * // => false
	 */
	function isArguments(value) {
	  return isObjectLike(value) && isArrayLike(value) &&
	    hasOwnProperty.call(value, 'callee') && !propertyIsEnumerable.call(value, 'callee');
	}

	module.exports = isArguments;


/***/ },
/* 24 */
/***/ function(module, exports, __webpack_require__) {

	var getNative = __webpack_require__(13),
	    isLength = __webpack_require__(21),
	    isObjectLike = __webpack_require__(17);

	/** `Object#toString` result references. */
	var arrayTag = '[object Array]';

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/* Native method references for those with the same name as other `lodash` methods. */
	var nativeIsArray = getNative(Array, 'isArray');

	/**
	 * Checks if `value` is classified as an `Array` object.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is correctly classified, else `false`.
	 * @example
	 *
	 * _.isArray([1, 2, 3]);
	 * // => true
	 *
	 * _.isArray(function() { return arguments; }());
	 * // => false
	 */
	var isArray = nativeIsArray || function(value) {
	  return isObjectLike(value) && isLength(value.length) && objToString.call(value) == arrayTag;
	};

	module.exports = isArray;


/***/ },
/* 25 */
/***/ function(module, exports) {

	/** Used to detect unsigned integer values. */
	var reIsUint = /^\d+$/;

	/**
	 * Used as the [maximum length](http://ecma-international.org/ecma-262/6.0/#sec-number.max_safe_integer)
	 * of an array-like value.
	 */
	var MAX_SAFE_INTEGER = 9007199254740991;

	/**
	 * Checks if `value` is a valid array-like index.
	 *
	 * @private
	 * @param {*} value The value to check.
	 * @param {number} [length=MAX_SAFE_INTEGER] The upper bounds of a valid index.
	 * @returns {boolean} Returns `true` if `value` is a valid index, else `false`.
	 */
	function isIndex(value, length) {
	  value = (typeof value == 'number' || reIsUint.test(value)) ? +value : -1;
	  length = length == null ? MAX_SAFE_INTEGER : length;
	  return value > -1 && value % 1 == 0 && value < length;
	}

	module.exports = isIndex;


/***/ },
/* 26 */
/***/ function(module, exports, __webpack_require__) {

	var isArguments = __webpack_require__(23),
	    isArray = __webpack_require__(24),
	    isIndex = __webpack_require__(25),
	    isLength = __webpack_require__(21),
	    isObject = __webpack_require__(16);

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/** Used to check objects for own properties. */
	var hasOwnProperty = objectProto.hasOwnProperty;

	/**
	 * Creates an array of the own and inherited enumerable property names of `object`.
	 *
	 * **Note:** Non-object values are coerced to objects.
	 *
	 * @static
	 * @memberOf _
	 * @category Object
	 * @param {Object} object The object to query.
	 * @returns {Array} Returns the array of property names.
	 * @example
	 *
	 * function Foo() {
	 *   this.a = 1;
	 *   this.b = 2;
	 * }
	 *
	 * Foo.prototype.c = 3;
	 *
	 * _.keysIn(new Foo);
	 * // => ['a', 'b', 'c'] (iteration order is not guaranteed)
	 */
	function keysIn(object) {
	  if (object == null) {
	    return [];
	  }
	  if (!isObject(object)) {
	    object = Object(object);
	  }
	  var length = object.length;
	  length = (length && isLength(length) &&
	    (isArray(object) || isArguments(object)) && length) || 0;

	  var Ctor = object.constructor,
	      index = -1,
	      isProto = typeof Ctor == 'function' && Ctor.prototype === object,
	      result = Array(length),
	      skipIndexes = length > 0;

	  while (++index < length) {
	    result[index] = (index + '');
	  }
	  for (var key in object) {
	    if (!(skipIndexes && isIndex(key, length)) &&
	        !(key == 'constructor' && (isProto || !hasOwnProperty.call(object, key)))) {
	      result.push(key);
	    }
	  }
	  return result;
	}

	module.exports = keysIn;


/***/ },
/* 27 */
/***/ function(module, exports, __webpack_require__) {

	var baseFor = __webpack_require__(28),
	    keys = __webpack_require__(12);

	/**
	 * The base implementation of `_.forOwn` without support for callback
	 * shorthands and `this` binding.
	 *
	 * @private
	 * @param {Object} object The object to iterate over.
	 * @param {Function} iteratee The function invoked per iteration.
	 * @returns {Object} Returns `object`.
	 */
	function baseForOwn(object, iteratee) {
	  return baseFor(object, iteratee, keys);
	}

	module.exports = baseForOwn;


/***/ },
/* 28 */
/***/ function(module, exports, __webpack_require__) {

	var createBaseFor = __webpack_require__(29);

	/**
	 * The base implementation of `baseForIn` and `baseForOwn` which iterates
	 * over `object` properties returned by `keysFunc` invoking `iteratee` for
	 * each property. Iteratee functions may exit iteration early by explicitly
	 * returning `false`.
	 *
	 * @private
	 * @param {Object} object The object to iterate over.
	 * @param {Function} iteratee The function invoked per iteration.
	 * @param {Function} keysFunc The function to get the keys of `object`.
	 * @returns {Object} Returns `object`.
	 */
	var baseFor = createBaseFor();

	module.exports = baseFor;


/***/ },
/* 29 */
/***/ function(module, exports, __webpack_require__) {

	var toObject = __webpack_require__(30);

	/**
	 * Creates a base function for `_.forIn` or `_.forInRight`.
	 *
	 * @private
	 * @param {boolean} [fromRight] Specify iterating from right to left.
	 * @returns {Function} Returns the new base function.
	 */
	function createBaseFor(fromRight) {
	  return function(object, iteratee, keysFunc) {
	    var iterable = toObject(object),
	        props = keysFunc(object),
	        length = props.length,
	        index = fromRight ? length : -1;

	    while ((fromRight ? index-- : ++index < length)) {
	      var key = props[index];
	      if (iteratee(iterable[key], key, iterable) === false) {
	        break;
	      }
	    }
	    return object;
	  };
	}

	module.exports = createBaseFor;


/***/ },
/* 30 */
/***/ function(module, exports, __webpack_require__) {

	var isObject = __webpack_require__(16);

	/**
	 * Converts `value` to an object if it's not one.
	 *
	 * @private
	 * @param {*} value The value to process.
	 * @returns {Object} Returns the object.
	 */
	function toObject(value) {
	  return isObject(value) ? value : Object(value);
	}

	module.exports = toObject;


/***/ },
/* 31 */
/***/ function(module, exports) {

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/** Used to check objects for own properties. */
	var hasOwnProperty = objectProto.hasOwnProperty;

	/**
	 * Initializes an array clone.
	 *
	 * @private
	 * @param {Array} array The array to clone.
	 * @returns {Array} Returns the initialized clone.
	 */
	function initCloneArray(array) {
	  var length = array.length,
	      result = new array.constructor(length);

	  // Add array properties assigned by `RegExp#exec`.
	  if (length && typeof array[0] == 'string' && hasOwnProperty.call(array, 'index')) {
	    result.index = array.index;
	    result.input = array.input;
	  }
	  return result;
	}

	module.exports = initCloneArray;


/***/ },
/* 32 */
/***/ function(module, exports, __webpack_require__) {

	var bufferClone = __webpack_require__(33);

	/** `Object#toString` result references. */
	var boolTag = '[object Boolean]',
	    dateTag = '[object Date]',
	    numberTag = '[object Number]',
	    regexpTag = '[object RegExp]',
	    stringTag = '[object String]';

	var arrayBufferTag = '[object ArrayBuffer]',
	    float32Tag = '[object Float32Array]',
	    float64Tag = '[object Float64Array]',
	    int8Tag = '[object Int8Array]',
	    int16Tag = '[object Int16Array]',
	    int32Tag = '[object Int32Array]',
	    uint8Tag = '[object Uint8Array]',
	    uint8ClampedTag = '[object Uint8ClampedArray]',
	    uint16Tag = '[object Uint16Array]',
	    uint32Tag = '[object Uint32Array]';

	/** Used to match `RegExp` flags from their coerced string values. */
	var reFlags = /\w*$/;

	/**
	 * Initializes an object clone based on its `toStringTag`.
	 *
	 * **Note:** This function only supports cloning values with tags of
	 * `Boolean`, `Date`, `Error`, `Number`, `RegExp`, or `String`.
	 *
	 * @private
	 * @param {Object} object The object to clone.
	 * @param {string} tag The `toStringTag` of the object to clone.
	 * @param {boolean} [isDeep] Specify a deep clone.
	 * @returns {Object} Returns the initialized clone.
	 */
	function initCloneByTag(object, tag, isDeep) {
	  var Ctor = object.constructor;
	  switch (tag) {
	    case arrayBufferTag:
	      return bufferClone(object);

	    case boolTag:
	    case dateTag:
	      return new Ctor(+object);

	    case float32Tag: case float64Tag:
	    case int8Tag: case int16Tag: case int32Tag:
	    case uint8Tag: case uint8ClampedTag: case uint16Tag: case uint32Tag:
	      var buffer = object.buffer;
	      return new Ctor(isDeep ? bufferClone(buffer) : buffer, object.byteOffset, object.length);

	    case numberTag:
	    case stringTag:
	      return new Ctor(object);

	    case regexpTag:
	      var result = new Ctor(object.source, reFlags.exec(object));
	      result.lastIndex = object.lastIndex;
	  }
	  return result;
	}

	module.exports = initCloneByTag;


/***/ },
/* 33 */
/***/ function(module, exports) {

	/* WEBPACK VAR INJECTION */(function(global) {/** Native method references. */
	var ArrayBuffer = global.ArrayBuffer,
	    Uint8Array = global.Uint8Array;

	/**
	 * Creates a clone of the given array buffer.
	 *
	 * @private
	 * @param {ArrayBuffer} buffer The array buffer to clone.
	 * @returns {ArrayBuffer} Returns the cloned array buffer.
	 */
	function bufferClone(buffer) {
	  var result = new ArrayBuffer(buffer.byteLength),
	      view = new Uint8Array(result);

	  view.set(new Uint8Array(buffer));
	  return result;
	}

	module.exports = bufferClone;

	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ },
/* 34 */
/***/ function(module, exports) {

	/**
	 * Initializes an object clone.
	 *
	 * @private
	 * @param {Object} object The object to clone.
	 * @returns {Object} Returns the initialized clone.
	 */
	function initCloneObject(object) {
	  var Ctor = object.constructor;
	  if (!(typeof Ctor == 'function' && Ctor instanceof Ctor)) {
	    Ctor = Object;
	  }
	  return new Ctor;
	}

	module.exports = initCloneObject;


/***/ },
/* 35 */
/***/ function(module, exports, __webpack_require__) {

	var identity = __webpack_require__(36);

	/**
	 * A specialized version of `baseCallback` which only supports `this` binding
	 * and specifying the number of arguments to provide to `func`.
	 *
	 * @private
	 * @param {Function} func The function to bind.
	 * @param {*} thisArg The `this` binding of `func`.
	 * @param {number} [argCount] The number of arguments to provide to `func`.
	 * @returns {Function} Returns the callback.
	 */
	function bindCallback(func, thisArg, argCount) {
	  if (typeof func != 'function') {
	    return identity;
	  }
	  if (thisArg === undefined) {
	    return func;
	  }
	  switch (argCount) {
	    case 1: return function(value) {
	      return func.call(thisArg, value);
	    };
	    case 3: return function(value, index, collection) {
	      return func.call(thisArg, value, index, collection);
	    };
	    case 4: return function(accumulator, value, index, collection) {
	      return func.call(thisArg, accumulator, value, index, collection);
	    };
	    case 5: return function(value, other, key, object, source) {
	      return func.call(thisArg, value, other, key, object, source);
	    };
	  }
	  return function() {
	    return func.apply(thisArg, arguments);
	  };
	}

	module.exports = bindCallback;


/***/ },
/* 36 */
/***/ function(module, exports) {

	/**
	 * This method returns the first argument provided to it.
	 *
	 * @static
	 * @memberOf _
	 * @category Utility
	 * @param {*} value Any value.
	 * @returns {*} Returns `value`.
	 * @example
	 *
	 * var object = { 'user': 'fred' };
	 *
	 * _.identity(object) === object;
	 * // => true
	 */
	function identity(value) {
	  return value;
	}

	module.exports = identity;


/***/ },
/* 37 */
/***/ function(module, exports, __webpack_require__) {

	var isArrayLike = __webpack_require__(18),
	    isIndex = __webpack_require__(25),
	    isObject = __webpack_require__(16);

	/**
	 * Checks if the provided arguments are from an iteratee call.
	 *
	 * @private
	 * @param {*} value The potential iteratee value argument.
	 * @param {*} index The potential iteratee index or key argument.
	 * @param {*} object The potential iteratee object argument.
	 * @returns {boolean} Returns `true` if the arguments are from an iteratee call, else `false`.
	 */
	function isIterateeCall(value, index, object) {
	  if (!isObject(object)) {
	    return false;
	  }
	  var type = typeof index;
	  if (type == 'number'
	      ? (isArrayLike(object) && isIndex(index, object.length))
	      : (type == 'string' && index in object)) {
	    var other = object[index];
	    return value === value ? (value === other) : (other !== other);
	  }
	  return false;
	}

	module.exports = isIterateeCall;


/***/ },
/* 38 */
/***/ function(module, exports, __webpack_require__) {

	var baseClone = __webpack_require__(7),
	    bindCallback = __webpack_require__(35);

	/**
	 * Creates a deep clone of `value`. If `customizer` is provided it's invoked
	 * to produce the cloned values. If `customizer` returns `undefined` cloning
	 * is handled by the method instead. The `customizer` is bound to `thisArg`
	 * and invoked with up to three argument; (value [, index|key, object]).
	 *
	 * **Note:** This method is loosely based on the
	 * [structured clone algorithm](http://www.w3.org/TR/html5/infrastructure.html#internal-structured-cloning-algorithm).
	 * The enumerable properties of `arguments` objects and objects created by
	 * constructors other than `Object` are cloned to plain `Object` objects. An
	 * empty object is returned for uncloneable values such as functions, DOM nodes,
	 * Maps, Sets, and WeakMaps.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to deep clone.
	 * @param {Function} [customizer] The function to customize cloning values.
	 * @param {*} [thisArg] The `this` binding of `customizer`.
	 * @returns {*} Returns the deep cloned value.
	 * @example
	 *
	 * var users = [
	 *   { 'user': 'barney' },
	 *   { 'user': 'fred' }
	 * ];
	 *
	 * var deep = _.cloneDeep(users);
	 * deep[0] === users[0];
	 * // => false
	 *
	 * // using a customizer callback
	 * var el = _.cloneDeep(document.body, function(value) {
	 *   if (_.isElement(value)) {
	 *     return value.cloneNode(true);
	 *   }
	 * });
	 *
	 * el === document.body
	 * // => false
	 * el.nodeName
	 * // => BODY
	 * el.childNodes.length;
	 * // => 20
	 */
	function cloneDeep(value, customizer, thisArg) {
	  return typeof customizer == 'function'
	    ? baseClone(value, true, bindCallback(customizer, thisArg, 3))
	    : baseClone(value, true);
	}

	module.exports = cloneDeep;


/***/ },
/* 39 */
/***/ function(module, exports, __webpack_require__) {

	module.exports = __webpack_require__(40);


/***/ },
/* 40 */
/***/ function(module, exports, __webpack_require__) {

	var baseIsEqual = __webpack_require__(41),
	    bindCallback = __webpack_require__(35);

	/**
	 * Performs a deep comparison between two values to determine if they are
	 * equivalent. If `customizer` is provided it's invoked to compare values.
	 * If `customizer` returns `undefined` comparisons are handled by the method
	 * instead. The `customizer` is bound to `thisArg` and invoked with up to
	 * three arguments: (value, other [, index|key]).
	 *
	 * **Note:** This method supports comparing arrays, booleans, `Date` objects,
	 * numbers, `Object` objects, regexes, and strings. Objects are compared by
	 * their own, not inherited, enumerable properties. Functions and DOM nodes
	 * are **not** supported. Provide a customizer function to extend support
	 * for comparing other values.
	 *
	 * @static
	 * @memberOf _
	 * @alias eq
	 * @category Lang
	 * @param {*} value The value to compare.
	 * @param {*} other The other value to compare.
	 * @param {Function} [customizer] The function to customize value comparisons.
	 * @param {*} [thisArg] The `this` binding of `customizer`.
	 * @returns {boolean} Returns `true` if the values are equivalent, else `false`.
	 * @example
	 *
	 * var object = { 'user': 'fred' };
	 * var other = { 'user': 'fred' };
	 *
	 * object == other;
	 * // => false
	 *
	 * _.isEqual(object, other);
	 * // => true
	 *
	 * // using a customizer callback
	 * var array = ['hello', 'goodbye'];
	 * var other = ['hi', 'goodbye'];
	 *
	 * _.isEqual(array, other, function(value, other) {
	 *   if (_.every([value, other], RegExp.prototype.test, /^h(?:i|ello)$/)) {
	 *     return true;
	 *   }
	 * });
	 * // => true
	 */
	function isEqual(value, other, customizer, thisArg) {
	  customizer = typeof customizer == 'function' ? bindCallback(customizer, thisArg, 3) : undefined;
	  var result = customizer ? customizer(value, other) : undefined;
	  return  result === undefined ? baseIsEqual(value, other, customizer) : !!result;
	}

	module.exports = isEqual;


/***/ },
/* 41 */
/***/ function(module, exports, __webpack_require__) {

	var baseIsEqualDeep = __webpack_require__(42),
	    isObject = __webpack_require__(16),
	    isObjectLike = __webpack_require__(17);

	/**
	 * The base implementation of `_.isEqual` without support for `this` binding
	 * `customizer` functions.
	 *
	 * @private
	 * @param {*} value The value to compare.
	 * @param {*} other The other value to compare.
	 * @param {Function} [customizer] The function to customize comparing values.
	 * @param {boolean} [isLoose] Specify performing partial comparisons.
	 * @param {Array} [stackA] Tracks traversed `value` objects.
	 * @param {Array} [stackB] Tracks traversed `other` objects.
	 * @returns {boolean} Returns `true` if the values are equivalent, else `false`.
	 */
	function baseIsEqual(value, other, customizer, isLoose, stackA, stackB) {
	  if (value === other) {
	    return true;
	  }
	  if (value == null || other == null || (!isObject(value) && !isObjectLike(other))) {
	    return value !== value && other !== other;
	  }
	  return baseIsEqualDeep(value, other, baseIsEqual, customizer, isLoose, stackA, stackB);
	}

	module.exports = baseIsEqual;


/***/ },
/* 42 */
/***/ function(module, exports, __webpack_require__) {

	var equalArrays = __webpack_require__(43),
	    equalByTag = __webpack_require__(45),
	    equalObjects = __webpack_require__(46),
	    isArray = __webpack_require__(24),
	    isTypedArray = __webpack_require__(47);

	/** `Object#toString` result references. */
	var argsTag = '[object Arguments]',
	    arrayTag = '[object Array]',
	    objectTag = '[object Object]';

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/** Used to check objects for own properties. */
	var hasOwnProperty = objectProto.hasOwnProperty;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * A specialized version of `baseIsEqual` for arrays and objects which performs
	 * deep comparisons and tracks traversed objects enabling objects with circular
	 * references to be compared.
	 *
	 * @private
	 * @param {Object} object The object to compare.
	 * @param {Object} other The other object to compare.
	 * @param {Function} equalFunc The function to determine equivalents of values.
	 * @param {Function} [customizer] The function to customize comparing objects.
	 * @param {boolean} [isLoose] Specify performing partial comparisons.
	 * @param {Array} [stackA=[]] Tracks traversed `value` objects.
	 * @param {Array} [stackB=[]] Tracks traversed `other` objects.
	 * @returns {boolean} Returns `true` if the objects are equivalent, else `false`.
	 */
	function baseIsEqualDeep(object, other, equalFunc, customizer, isLoose, stackA, stackB) {
	  var objIsArr = isArray(object),
	      othIsArr = isArray(other),
	      objTag = arrayTag,
	      othTag = arrayTag;

	  if (!objIsArr) {
	    objTag = objToString.call(object);
	    if (objTag == argsTag) {
	      objTag = objectTag;
	    } else if (objTag != objectTag) {
	      objIsArr = isTypedArray(object);
	    }
	  }
	  if (!othIsArr) {
	    othTag = objToString.call(other);
	    if (othTag == argsTag) {
	      othTag = objectTag;
	    } else if (othTag != objectTag) {
	      othIsArr = isTypedArray(other);
	    }
	  }
	  var objIsObj = objTag == objectTag,
	      othIsObj = othTag == objectTag,
	      isSameTag = objTag == othTag;

	  if (isSameTag && !(objIsArr || objIsObj)) {
	    return equalByTag(object, other, objTag);
	  }
	  if (!isLoose) {
	    var objIsWrapped = objIsObj && hasOwnProperty.call(object, '__wrapped__'),
	        othIsWrapped = othIsObj && hasOwnProperty.call(other, '__wrapped__');

	    if (objIsWrapped || othIsWrapped) {
	      return equalFunc(objIsWrapped ? object.value() : object, othIsWrapped ? other.value() : other, customizer, isLoose, stackA, stackB);
	    }
	  }
	  if (!isSameTag) {
	    return false;
	  }
	  // Assume cyclic values are equal.
	  // For more information on detecting circular references see https://es5.github.io/#JO.
	  stackA || (stackA = []);
	  stackB || (stackB = []);

	  var length = stackA.length;
	  while (length--) {
	    if (stackA[length] == object) {
	      return stackB[length] == other;
	    }
	  }
	  // Add `object` and `other` to the stack of traversed objects.
	  stackA.push(object);
	  stackB.push(other);

	  var result = (objIsArr ? equalArrays : equalObjects)(object, other, equalFunc, customizer, isLoose, stackA, stackB);

	  stackA.pop();
	  stackB.pop();

	  return result;
	}

	module.exports = baseIsEqualDeep;


/***/ },
/* 43 */
/***/ function(module, exports, __webpack_require__) {

	var arraySome = __webpack_require__(44);

	/**
	 * A specialized version of `baseIsEqualDeep` for arrays with support for
	 * partial deep comparisons.
	 *
	 * @private
	 * @param {Array} array The array to compare.
	 * @param {Array} other The other array to compare.
	 * @param {Function} equalFunc The function to determine equivalents of values.
	 * @param {Function} [customizer] The function to customize comparing arrays.
	 * @param {boolean} [isLoose] Specify performing partial comparisons.
	 * @param {Array} [stackA] Tracks traversed `value` objects.
	 * @param {Array} [stackB] Tracks traversed `other` objects.
	 * @returns {boolean} Returns `true` if the arrays are equivalent, else `false`.
	 */
	function equalArrays(array, other, equalFunc, customizer, isLoose, stackA, stackB) {
	  var index = -1,
	      arrLength = array.length,
	      othLength = other.length;

	  if (arrLength != othLength && !(isLoose && othLength > arrLength)) {
	    return false;
	  }
	  // Ignore non-index properties.
	  while (++index < arrLength) {
	    var arrValue = array[index],
	        othValue = other[index],
	        result = customizer ? customizer(isLoose ? othValue : arrValue, isLoose ? arrValue : othValue, index) : undefined;

	    if (result !== undefined) {
	      if (result) {
	        continue;
	      }
	      return false;
	    }
	    // Recursively compare arrays (susceptible to call stack limits).
	    if (isLoose) {
	      if (!arraySome(other, function(othValue) {
	            return arrValue === othValue || equalFunc(arrValue, othValue, customizer, isLoose, stackA, stackB);
	          })) {
	        return false;
	      }
	    } else if (!(arrValue === othValue || equalFunc(arrValue, othValue, customizer, isLoose, stackA, stackB))) {
	      return false;
	    }
	  }
	  return true;
	}

	module.exports = equalArrays;


/***/ },
/* 44 */
/***/ function(module, exports) {

	/**
	 * A specialized version of `_.some` for arrays without support for callback
	 * shorthands and `this` binding.
	 *
	 * @private
	 * @param {Array} array The array to iterate over.
	 * @param {Function} predicate The function invoked per iteration.
	 * @returns {boolean} Returns `true` if any element passes the predicate check,
	 *  else `false`.
	 */
	function arraySome(array, predicate) {
	  var index = -1,
	      length = array.length;

	  while (++index < length) {
	    if (predicate(array[index], index, array)) {
	      return true;
	    }
	  }
	  return false;
	}

	module.exports = arraySome;


/***/ },
/* 45 */
/***/ function(module, exports) {

	/** `Object#toString` result references. */
	var boolTag = '[object Boolean]',
	    dateTag = '[object Date]',
	    errorTag = '[object Error]',
	    numberTag = '[object Number]',
	    regexpTag = '[object RegExp]',
	    stringTag = '[object String]';

	/**
	 * A specialized version of `baseIsEqualDeep` for comparing objects of
	 * the same `toStringTag`.
	 *
	 * **Note:** This function only supports comparing values with tags of
	 * `Boolean`, `Date`, `Error`, `Number`, `RegExp`, or `String`.
	 *
	 * @private
	 * @param {Object} object The object to compare.
	 * @param {Object} other The other object to compare.
	 * @param {string} tag The `toStringTag` of the objects to compare.
	 * @returns {boolean} Returns `true` if the objects are equivalent, else `false`.
	 */
	function equalByTag(object, other, tag) {
	  switch (tag) {
	    case boolTag:
	    case dateTag:
	      // Coerce dates and booleans to numbers, dates to milliseconds and booleans
	      // to `1` or `0` treating invalid dates coerced to `NaN` as not equal.
	      return +object == +other;

	    case errorTag:
	      return object.name == other.name && object.message == other.message;

	    case numberTag:
	      // Treat `NaN` vs. `NaN` as equal.
	      return (object != +object)
	        ? other != +other
	        : object == +other;

	    case regexpTag:
	    case stringTag:
	      // Coerce regexes to strings and treat strings primitives and string
	      // objects as equal. See https://es5.github.io/#x15.10.6.4 for more details.
	      return object == (other + '');
	  }
	  return false;
	}

	module.exports = equalByTag;


/***/ },
/* 46 */
/***/ function(module, exports, __webpack_require__) {

	var keys = __webpack_require__(12);

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/** Used to check objects for own properties. */
	var hasOwnProperty = objectProto.hasOwnProperty;

	/**
	 * A specialized version of `baseIsEqualDeep` for objects with support for
	 * partial deep comparisons.
	 *
	 * @private
	 * @param {Object} object The object to compare.
	 * @param {Object} other The other object to compare.
	 * @param {Function} equalFunc The function to determine equivalents of values.
	 * @param {Function} [customizer] The function to customize comparing values.
	 * @param {boolean} [isLoose] Specify performing partial comparisons.
	 * @param {Array} [stackA] Tracks traversed `value` objects.
	 * @param {Array} [stackB] Tracks traversed `other` objects.
	 * @returns {boolean} Returns `true` if the objects are equivalent, else `false`.
	 */
	function equalObjects(object, other, equalFunc, customizer, isLoose, stackA, stackB) {
	  var objProps = keys(object),
	      objLength = objProps.length,
	      othProps = keys(other),
	      othLength = othProps.length;

	  if (objLength != othLength && !isLoose) {
	    return false;
	  }
	  var index = objLength;
	  while (index--) {
	    var key = objProps[index];
	    if (!(isLoose ? key in other : hasOwnProperty.call(other, key))) {
	      return false;
	    }
	  }
	  var skipCtor = isLoose;
	  while (++index < objLength) {
	    key = objProps[index];
	    var objValue = object[key],
	        othValue = other[key],
	        result = customizer ? customizer(isLoose ? othValue : objValue, isLoose? objValue : othValue, key) : undefined;

	    // Recursively compare objects (susceptible to call stack limits).
	    if (!(result === undefined ? equalFunc(objValue, othValue, customizer, isLoose, stackA, stackB) : result)) {
	      return false;
	    }
	    skipCtor || (skipCtor = key == 'constructor');
	  }
	  if (!skipCtor) {
	    var objCtor = object.constructor,
	        othCtor = other.constructor;

	    // Non `Object` object instances with different constructors are not equal.
	    if (objCtor != othCtor &&
	        ('constructor' in object && 'constructor' in other) &&
	        !(typeof objCtor == 'function' && objCtor instanceof objCtor &&
	          typeof othCtor == 'function' && othCtor instanceof othCtor)) {
	      return false;
	    }
	  }
	  return true;
	}

	module.exports = equalObjects;


/***/ },
/* 47 */
/***/ function(module, exports, __webpack_require__) {

	var isLength = __webpack_require__(21),
	    isObjectLike = __webpack_require__(17);

	/** `Object#toString` result references. */
	var argsTag = '[object Arguments]',
	    arrayTag = '[object Array]',
	    boolTag = '[object Boolean]',
	    dateTag = '[object Date]',
	    errorTag = '[object Error]',
	    funcTag = '[object Function]',
	    mapTag = '[object Map]',
	    numberTag = '[object Number]',
	    objectTag = '[object Object]',
	    regexpTag = '[object RegExp]',
	    setTag = '[object Set]',
	    stringTag = '[object String]',
	    weakMapTag = '[object WeakMap]';

	var arrayBufferTag = '[object ArrayBuffer]',
	    float32Tag = '[object Float32Array]',
	    float64Tag = '[object Float64Array]',
	    int8Tag = '[object Int8Array]',
	    int16Tag = '[object Int16Array]',
	    int32Tag = '[object Int32Array]',
	    uint8Tag = '[object Uint8Array]',
	    uint8ClampedTag = '[object Uint8ClampedArray]',
	    uint16Tag = '[object Uint16Array]',
	    uint32Tag = '[object Uint32Array]';

	/** Used to identify `toStringTag` values of typed arrays. */
	var typedArrayTags = {};
	typedArrayTags[float32Tag] = typedArrayTags[float64Tag] =
	typedArrayTags[int8Tag] = typedArrayTags[int16Tag] =
	typedArrayTags[int32Tag] = typedArrayTags[uint8Tag] =
	typedArrayTags[uint8ClampedTag] = typedArrayTags[uint16Tag] =
	typedArrayTags[uint32Tag] = true;
	typedArrayTags[argsTag] = typedArrayTags[arrayTag] =
	typedArrayTags[arrayBufferTag] = typedArrayTags[boolTag] =
	typedArrayTags[dateTag] = typedArrayTags[errorTag] =
	typedArrayTags[funcTag] = typedArrayTags[mapTag] =
	typedArrayTags[numberTag] = typedArrayTags[objectTag] =
	typedArrayTags[regexpTag] = typedArrayTags[setTag] =
	typedArrayTags[stringTag] = typedArrayTags[weakMapTag] = false;

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * Checks if `value` is classified as a typed array.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is correctly classified, else `false`.
	 * @example
	 *
	 * _.isTypedArray(new Uint8Array);
	 * // => true
	 *
	 * _.isTypedArray([]);
	 * // => false
	 */
	function isTypedArray(value) {
	  return isObjectLike(value) && isLength(value.length) && !!typedArrayTags[objToString.call(value)];
	}

	module.exports = isTypedArray;


/***/ },
/* 48 */
/***/ function(module, exports) {

	/**
	 * Checks if `value` is greater than `other`.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to compare.
	 * @param {*} other The other value to compare.
	 * @returns {boolean} Returns `true` if `value` is greater than `other`, else `false`.
	 * @example
	 *
	 * _.gt(3, 1);
	 * // => true
	 *
	 * _.gt(3, 3);
	 * // => false
	 *
	 * _.gt(1, 3);
	 * // => false
	 */
	function gt(value, other) {
	  return value > other;
	}

	module.exports = gt;


/***/ },
/* 49 */
/***/ function(module, exports) {

	/**
	 * Checks if `value` is greater than or equal to `other`.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to compare.
	 * @param {*} other The other value to compare.
	 * @returns {boolean} Returns `true` if `value` is greater than or equal to `other`, else `false`.
	 * @example
	 *
	 * _.gte(3, 1);
	 * // => true
	 *
	 * _.gte(3, 3);
	 * // => true
	 *
	 * _.gte(1, 3);
	 * // => false
	 */
	function gte(value, other) {
	  return value >= other;
	}

	module.exports = gte;


/***/ },
/* 50 */
/***/ function(module, exports, __webpack_require__) {

	var isObjectLike = __webpack_require__(17);

	/** `Object#toString` result references. */
	var boolTag = '[object Boolean]';

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * Checks if `value` is classified as a boolean primitive or object.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is correctly classified, else `false`.
	 * @example
	 *
	 * _.isBoolean(false);
	 * // => true
	 *
	 * _.isBoolean(null);
	 * // => false
	 */
	function isBoolean(value) {
	  return value === true || value === false || (isObjectLike(value) && objToString.call(value) == boolTag);
	}

	module.exports = isBoolean;


/***/ },
/* 51 */
/***/ function(module, exports, __webpack_require__) {

	var isObjectLike = __webpack_require__(17);

	/** `Object#toString` result references. */
	var dateTag = '[object Date]';

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * Checks if `value` is classified as a `Date` object.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is correctly classified, else `false`.
	 * @example
	 *
	 * _.isDate(new Date);
	 * // => true
	 *
	 * _.isDate('Mon April 23 2012');
	 * // => false
	 */
	function isDate(value) {
	  return isObjectLike(value) && objToString.call(value) == dateTag;
	}

	module.exports = isDate;


/***/ },
/* 52 */
/***/ function(module, exports, __webpack_require__) {

	var isObjectLike = __webpack_require__(17),
	    isPlainObject = __webpack_require__(53);

	/**
	 * Checks if `value` is a DOM element.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is a DOM element, else `false`.
	 * @example
	 *
	 * _.isElement(document.body);
	 * // => true
	 *
	 * _.isElement('<body>');
	 * // => false
	 */
	function isElement(value) {
	  return !!value && value.nodeType === 1 && isObjectLike(value) && !isPlainObject(value);
	}

	module.exports = isElement;


/***/ },
/* 53 */
/***/ function(module, exports, __webpack_require__) {

	var baseForIn = __webpack_require__(54),
	    isArguments = __webpack_require__(23),
	    isObjectLike = __webpack_require__(17);

	/** `Object#toString` result references. */
	var objectTag = '[object Object]';

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/** Used to check objects for own properties. */
	var hasOwnProperty = objectProto.hasOwnProperty;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * Checks if `value` is a plain object, that is, an object created by the
	 * `Object` constructor or one with a `[[Prototype]]` of `null`.
	 *
	 * **Note:** This method assumes objects created by the `Object` constructor
	 * have no inherited enumerable properties.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is a plain object, else `false`.
	 * @example
	 *
	 * function Foo() {
	 *   this.a = 1;
	 * }
	 *
	 * _.isPlainObject(new Foo);
	 * // => false
	 *
	 * _.isPlainObject([1, 2, 3]);
	 * // => false
	 *
	 * _.isPlainObject({ 'x': 0, 'y': 0 });
	 * // => true
	 *
	 * _.isPlainObject(Object.create(null));
	 * // => true
	 */
	function isPlainObject(value) {
	  var Ctor;

	  // Exit early for non `Object` objects.
	  if (!(isObjectLike(value) && objToString.call(value) == objectTag && !isArguments(value)) ||
	      (!hasOwnProperty.call(value, 'constructor') && (Ctor = value.constructor, typeof Ctor == 'function' && !(Ctor instanceof Ctor)))) {
	    return false;
	  }
	  // IE < 9 iterates inherited properties before own properties. If the first
	  // iterated property is an object's own property then there are no inherited
	  // enumerable properties.
	  var result;
	  // In most environments an object's own properties are iterated before
	  // its inherited properties. If the last iterated property is an object's
	  // own property then there are no inherited enumerable properties.
	  baseForIn(value, function(subValue, key) {
	    result = key;
	  });
	  return result === undefined || hasOwnProperty.call(value, result);
	}

	module.exports = isPlainObject;


/***/ },
/* 54 */
/***/ function(module, exports, __webpack_require__) {

	var baseFor = __webpack_require__(28),
	    keysIn = __webpack_require__(26);

	/**
	 * The base implementation of `_.forIn` without support for callback
	 * shorthands and `this` binding.
	 *
	 * @private
	 * @param {Object} object The object to iterate over.
	 * @param {Function} iteratee The function invoked per iteration.
	 * @returns {Object} Returns `object`.
	 */
	function baseForIn(object, iteratee) {
	  return baseFor(object, iteratee, keysIn);
	}

	module.exports = baseForIn;


/***/ },
/* 55 */
/***/ function(module, exports, __webpack_require__) {

	var isArguments = __webpack_require__(23),
	    isArray = __webpack_require__(24),
	    isArrayLike = __webpack_require__(18),
	    isFunction = __webpack_require__(15),
	    isObjectLike = __webpack_require__(17),
	    isString = __webpack_require__(56),
	    keys = __webpack_require__(12);

	/**
	 * Checks if `value` is empty. A value is considered empty unless it's an
	 * `arguments` object, array, string, or jQuery-like collection with a length
	 * greater than `0` or an object with own enumerable properties.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {Array|Object|string} value The value to inspect.
	 * @returns {boolean} Returns `true` if `value` is empty, else `false`.
	 * @example
	 *
	 * _.isEmpty(null);
	 * // => true
	 *
	 * _.isEmpty(true);
	 * // => true
	 *
	 * _.isEmpty(1);
	 * // => true
	 *
	 * _.isEmpty([1, 2, 3]);
	 * // => false
	 *
	 * _.isEmpty({ 'a': 1 });
	 * // => false
	 */
	function isEmpty(value) {
	  if (value == null) {
	    return true;
	  }
	  if (isArrayLike(value) && (isArray(value) || isString(value) || isArguments(value) ||
	      (isObjectLike(value) && isFunction(value.splice)))) {
	    return !value.length;
	  }
	  return !keys(value).length;
	}

	module.exports = isEmpty;


/***/ },
/* 56 */
/***/ function(module, exports, __webpack_require__) {

	var isObjectLike = __webpack_require__(17);

	/** `Object#toString` result references. */
	var stringTag = '[object String]';

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * Checks if `value` is classified as a `String` primitive or object.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is correctly classified, else `false`.
	 * @example
	 *
	 * _.isString('abc');
	 * // => true
	 *
	 * _.isString(1);
	 * // => false
	 */
	function isString(value) {
	  return typeof value == 'string' || (isObjectLike(value) && objToString.call(value) == stringTag);
	}

	module.exports = isString;


/***/ },
/* 57 */
/***/ function(module, exports, __webpack_require__) {

	var isObjectLike = __webpack_require__(17);

	/** `Object#toString` result references. */
	var errorTag = '[object Error]';

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * Checks if `value` is an `Error`, `EvalError`, `RangeError`, `ReferenceError`,
	 * `SyntaxError`, `TypeError`, or `URIError` object.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is an error object, else `false`.
	 * @example
	 *
	 * _.isError(new Error);
	 * // => true
	 *
	 * _.isError(Error);
	 * // => false
	 */
	function isError(value) {
	  return isObjectLike(value) && typeof value.message == 'string' && objToString.call(value) == errorTag;
	}

	module.exports = isError;


/***/ },
/* 58 */
/***/ function(module, exports) {

	/* WEBPACK VAR INJECTION */(function(global) {/* Native method references for those with the same name as other `lodash` methods. */
	var nativeIsFinite = global.isFinite;

	/**
	 * Checks if `value` is a finite primitive number.
	 *
	 * **Note:** This method is based on [`Number.isFinite`](http://ecma-international.org/ecma-262/6.0/#sec-number.isfinite).
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is a finite number, else `false`.
	 * @example
	 *
	 * _.isFinite(10);
	 * // => true
	 *
	 * _.isFinite('10');
	 * // => false
	 *
	 * _.isFinite(true);
	 * // => false
	 *
	 * _.isFinite(Object(10));
	 * // => false
	 *
	 * _.isFinite(Infinity);
	 * // => false
	 */
	function isFinite(value) {
	  return typeof value == 'number' && nativeIsFinite(value);
	}

	module.exports = isFinite;

	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ },
/* 59 */
/***/ function(module, exports, __webpack_require__) {

	var baseIsMatch = __webpack_require__(60),
	    bindCallback = __webpack_require__(35),
	    getMatchData = __webpack_require__(61);

	/**
	 * Performs a deep comparison between `object` and `source` to determine if
	 * `object` contains equivalent property values. If `customizer` is provided
	 * it's invoked to compare values. If `customizer` returns `undefined`
	 * comparisons are handled by the method instead. The `customizer` is bound
	 * to `thisArg` and invoked with three arguments: (value, other, index|key).
	 *
	 * **Note:** This method supports comparing properties of arrays, booleans,
	 * `Date` objects, numbers, `Object` objects, regexes, and strings. Functions
	 * and DOM nodes are **not** supported. Provide a customizer function to extend
	 * support for comparing other values.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {Object} object The object to inspect.
	 * @param {Object} source The object of property values to match.
	 * @param {Function} [customizer] The function to customize value comparisons.
	 * @param {*} [thisArg] The `this` binding of `customizer`.
	 * @returns {boolean} Returns `true` if `object` is a match, else `false`.
	 * @example
	 *
	 * var object = { 'user': 'fred', 'age': 40 };
	 *
	 * _.isMatch(object, { 'age': 40 });
	 * // => true
	 *
	 * _.isMatch(object, { 'age': 36 });
	 * // => false
	 *
	 * // using a customizer callback
	 * var object = { 'greeting': 'hello' };
	 * var source = { 'greeting': 'hi' };
	 *
	 * _.isMatch(object, source, function(value, other) {
	 *   return _.every([value, other], RegExp.prototype.test, /^h(?:i|ello)$/) || undefined;
	 * });
	 * // => true
	 */
	function isMatch(object, source, customizer, thisArg) {
	  customizer = typeof customizer == 'function' ? bindCallback(customizer, thisArg, 3) : undefined;
	  return baseIsMatch(object, getMatchData(source), customizer);
	}

	module.exports = isMatch;


/***/ },
/* 60 */
/***/ function(module, exports, __webpack_require__) {

	var baseIsEqual = __webpack_require__(41),
	    toObject = __webpack_require__(30);

	/**
	 * The base implementation of `_.isMatch` without support for callback
	 * shorthands and `this` binding.
	 *
	 * @private
	 * @param {Object} object The object to inspect.
	 * @param {Array} matchData The propery names, values, and compare flags to match.
	 * @param {Function} [customizer] The function to customize comparing objects.
	 * @returns {boolean} Returns `true` if `object` is a match, else `false`.
	 */
	function baseIsMatch(object, matchData, customizer) {
	  var index = matchData.length,
	      length = index,
	      noCustomizer = !customizer;

	  if (object == null) {
	    return !length;
	  }
	  object = toObject(object);
	  while (index--) {
	    var data = matchData[index];
	    if ((noCustomizer && data[2])
	          ? data[1] !== object[data[0]]
	          : !(data[0] in object)
	        ) {
	      return false;
	    }
	  }
	  while (++index < length) {
	    data = matchData[index];
	    var key = data[0],
	        objValue = object[key],
	        srcValue = data[1];

	    if (noCustomizer && data[2]) {
	      if (objValue === undefined && !(key in object)) {
	        return false;
	      }
	    } else {
	      var result = customizer ? customizer(objValue, srcValue, key) : undefined;
	      if (!(result === undefined ? baseIsEqual(srcValue, objValue, customizer, true) : result)) {
	        return false;
	      }
	    }
	  }
	  return true;
	}

	module.exports = baseIsMatch;


/***/ },
/* 61 */
/***/ function(module, exports, __webpack_require__) {

	var isStrictComparable = __webpack_require__(62),
	    pairs = __webpack_require__(63);

	/**
	 * Gets the propery names, values, and compare flags of `object`.
	 *
	 * @private
	 * @param {Object} object The object to query.
	 * @returns {Array} Returns the match data of `object`.
	 */
	function getMatchData(object) {
	  var result = pairs(object),
	      length = result.length;

	  while (length--) {
	    result[length][2] = isStrictComparable(result[length][1]);
	  }
	  return result;
	}

	module.exports = getMatchData;


/***/ },
/* 62 */
/***/ function(module, exports, __webpack_require__) {

	var isObject = __webpack_require__(16);

	/**
	 * Checks if `value` is suitable for strict equality comparisons, i.e. `===`.
	 *
	 * @private
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` if suitable for strict
	 *  equality comparisons, else `false`.
	 */
	function isStrictComparable(value) {
	  return value === value && !isObject(value);
	}

	module.exports = isStrictComparable;


/***/ },
/* 63 */
/***/ function(module, exports, __webpack_require__) {

	var keys = __webpack_require__(12),
	    toObject = __webpack_require__(30);

	/**
	 * Creates a two dimensional array of the key-value pairs for `object`,
	 * e.g. `[[key1, value1], [key2, value2]]`.
	 *
	 * @static
	 * @memberOf _
	 * @category Object
	 * @param {Object} object The object to query.
	 * @returns {Array} Returns the new array of key-value pairs.
	 * @example
	 *
	 * _.pairs({ 'barney': 36, 'fred': 40 });
	 * // => [['barney', 36], ['fred', 40]] (iteration order is not guaranteed)
	 */
	function pairs(object) {
	  object = toObject(object);

	  var index = -1,
	      props = keys(object),
	      length = props.length,
	      result = Array(length);

	  while (++index < length) {
	    var key = props[index];
	    result[index] = [key, object[key]];
	  }
	  return result;
	}

	module.exports = pairs;


/***/ },
/* 64 */
/***/ function(module, exports, __webpack_require__) {

	var isNumber = __webpack_require__(65);

	/**
	 * Checks if `value` is `NaN`.
	 *
	 * **Note:** This method is not the same as [`isNaN`](https://es5.github.io/#x15.1.2.4)
	 * which returns `true` for `undefined` and other non-numeric values.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is `NaN`, else `false`.
	 * @example
	 *
	 * _.isNaN(NaN);
	 * // => true
	 *
	 * _.isNaN(new Number(NaN));
	 * // => true
	 *
	 * isNaN(undefined);
	 * // => true
	 *
	 * _.isNaN(undefined);
	 * // => false
	 */
	function isNaN(value) {
	  // An `NaN` primitive is the only value that is not equal to itself.
	  // Perform the `toStringTag` check first to avoid errors with some host objects in IE.
	  return isNumber(value) && value != +value;
	}

	module.exports = isNaN;


/***/ },
/* 65 */
/***/ function(module, exports, __webpack_require__) {

	var isObjectLike = __webpack_require__(17);

	/** `Object#toString` result references. */
	var numberTag = '[object Number]';

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * Checks if `value` is classified as a `Number` primitive or object.
	 *
	 * **Note:** To exclude `Infinity`, `-Infinity`, and `NaN`, which are classified
	 * as numbers, use the `_.isFinite` method.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is correctly classified, else `false`.
	 * @example
	 *
	 * _.isNumber(8.4);
	 * // => true
	 *
	 * _.isNumber(NaN);
	 * // => true
	 *
	 * _.isNumber('8.4');
	 * // => false
	 */
	function isNumber(value) {
	  return typeof value == 'number' || (isObjectLike(value) && objToString.call(value) == numberTag);
	}

	module.exports = isNumber;


/***/ },
/* 66 */
/***/ function(module, exports) {

	/**
	 * Checks if `value` is `null`.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is `null`, else `false`.
	 * @example
	 *
	 * _.isNull(null);
	 * // => true
	 *
	 * _.isNull(void 0);
	 * // => false
	 */
	function isNull(value) {
	  return value === null;
	}

	module.exports = isNull;


/***/ },
/* 67 */
/***/ function(module, exports, __webpack_require__) {

	var isObject = __webpack_require__(16);

	/** `Object#toString` result references. */
	var regexpTag = '[object RegExp]';

	/** Used for native method references. */
	var objectProto = Object.prototype;

	/**
	 * Used to resolve the [`toStringTag`](http://ecma-international.org/ecma-262/6.0/#sec-object.prototype.tostring)
	 * of values.
	 */
	var objToString = objectProto.toString;

	/**
	 * Checks if `value` is classified as a `RegExp` object.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is correctly classified, else `false`.
	 * @example
	 *
	 * _.isRegExp(/abc/);
	 * // => true
	 *
	 * _.isRegExp('/abc/');
	 * // => false
	 */
	function isRegExp(value) {
	  return isObject(value) && objToString.call(value) == regexpTag;
	}

	module.exports = isRegExp;


/***/ },
/* 68 */
/***/ function(module, exports) {

	/**
	 * Checks if `value` is `undefined`.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to check.
	 * @returns {boolean} Returns `true` if `value` is `undefined`, else `false`.
	 * @example
	 *
	 * _.isUndefined(void 0);
	 * // => true
	 *
	 * _.isUndefined(null);
	 * // => false
	 */
	function isUndefined(value) {
	  return value === undefined;
	}

	module.exports = isUndefined;


/***/ },
/* 69 */
/***/ function(module, exports) {

	/**
	 * Checks if `value` is less than `other`.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to compare.
	 * @param {*} other The other value to compare.
	 * @returns {boolean} Returns `true` if `value` is less than `other`, else `false`.
	 * @example
	 *
	 * _.lt(1, 3);
	 * // => true
	 *
	 * _.lt(3, 3);
	 * // => false
	 *
	 * _.lt(3, 1);
	 * // => false
	 */
	function lt(value, other) {
	  return value < other;
	}

	module.exports = lt;


/***/ },
/* 70 */
/***/ function(module, exports) {

	/**
	 * Checks if `value` is less than or equal to `other`.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to compare.
	 * @param {*} other The other value to compare.
	 * @returns {boolean} Returns `true` if `value` is less than or equal to `other`, else `false`.
	 * @example
	 *
	 * _.lte(1, 3);
	 * // => true
	 *
	 * _.lte(3, 3);
	 * // => true
	 *
	 * _.lte(3, 1);
	 * // => false
	 */
	function lte(value, other) {
	  return value <= other;
	}

	module.exports = lte;


/***/ },
/* 71 */
/***/ function(module, exports, __webpack_require__) {

	var arrayCopy = __webpack_require__(8),
	    getLength = __webpack_require__(19),
	    isLength = __webpack_require__(21),
	    values = __webpack_require__(72);

	/**
	 * Converts `value` to an array.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to convert.
	 * @returns {Array} Returns the converted array.
	 * @example
	 *
	 * (function() {
	 *   return _.toArray(arguments).slice(1);
	 * }(1, 2, 3));
	 * // => [2, 3]
	 */
	function toArray(value) {
	  var length = value ? getLength(value) : 0;
	  if (!isLength(length)) {
	    return values(value);
	  }
	  if (!length) {
	    return [];
	  }
	  return arrayCopy(value);
	}

	module.exports = toArray;


/***/ },
/* 72 */
/***/ function(module, exports, __webpack_require__) {

	var baseValues = __webpack_require__(73),
	    keys = __webpack_require__(12);

	/**
	 * Creates an array of the own enumerable property values of `object`.
	 *
	 * **Note:** Non-object values are coerced to objects.
	 *
	 * @static
	 * @memberOf _
	 * @category Object
	 * @param {Object} object The object to query.
	 * @returns {Array} Returns the array of property values.
	 * @example
	 *
	 * function Foo() {
	 *   this.a = 1;
	 *   this.b = 2;
	 * }
	 *
	 * Foo.prototype.c = 3;
	 *
	 * _.values(new Foo);
	 * // => [1, 2] (iteration order is not guaranteed)
	 *
	 * _.values('hi');
	 * // => ['h', 'i']
	 */
	function values(object) {
	  return baseValues(object, keys(object));
	}

	module.exports = values;


/***/ },
/* 73 */
/***/ function(module, exports) {

	/**
	 * The base implementation of `_.values` and `_.valuesIn` which creates an
	 * array of `object` property values corresponding to the property names
	 * of `props`.
	 *
	 * @private
	 * @param {Object} object The object to query.
	 * @param {Array} props The property names to get values for.
	 * @returns {Object} Returns the array of property values.
	 */
	function baseValues(object, props) {
	  var index = -1,
	      length = props.length,
	      result = Array(length);

	  while (++index < length) {
	    result[index] = object[props[index]];
	  }
	  return result;
	}

	module.exports = baseValues;


/***/ },
/* 74 */
/***/ function(module, exports, __webpack_require__) {

	var baseCopy = __webpack_require__(11),
	    keysIn = __webpack_require__(26);

	/**
	 * Converts `value` to a plain object flattening inherited enumerable
	 * properties of `value` to own properties of the plain object.
	 *
	 * @static
	 * @memberOf _
	 * @category Lang
	 * @param {*} value The value to convert.
	 * @returns {Object} Returns the converted plain object.
	 * @example
	 *
	 * function Foo() {
	 *   this.b = 2;
	 * }
	 *
	 * Foo.prototype.c = 3;
	 *
	 * _.assign({ 'a': 1 }, new Foo);
	 * // => { 'a': 1, 'b': 2 }
	 *
	 * _.assign({ 'a': 1 }, _.toPlainObject(new Foo));
	 * // => { 'a': 1, 'b': 2, 'c': 3 }
	 */
	function toPlainObject(value) {
	  return baseCopy(value, keysIn(value));
	}

	module.exports = toPlainObject;


/***/ },
/* 75 */
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function(process) {// Copyright Joyent, Inc. and other Node contributors.
	//
	// Permission is hereby granted, free of charge, to any person obtaining a
	// copy of this software and associated documentation files (the
	// "Software"), to deal in the Software without restriction, including
	// without limitation the rights to use, copy, modify, merge, publish,
	// distribute, sublicense, and/or sell copies of the Software, and to permit
	// persons to whom the Software is furnished to do so, subject to the
	// following conditions:
	//
	// The above copyright notice and this permission notice shall be included
	// in all copies or substantial portions of the Software.
	//
	// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
	// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
	// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
	// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
	// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
	// USE OR OTHER DEALINGS IN THE SOFTWARE.

	// resolves . and .. elements in a path array with directory names there
	// must be no slashes, empty elements, or device names (c:\) in the array
	// (so also no leading and trailing slashes - it does not distinguish
	// relative and absolute paths)
	function normalizeArray(parts, allowAboveRoot) {
	  // if the path tries to go above the root, `up` ends up > 0
	  var up = 0;
	  for (var i = parts.length - 1; i >= 0; i--) {
	    var last = parts[i];
	    if (last === '.') {
	      parts.splice(i, 1);
	    } else if (last === '..') {
	      parts.splice(i, 1);
	      up++;
	    } else if (up) {
	      parts.splice(i, 1);
	      up--;
	    }
	  }

	  // if the path is allowed to go above the root, restore leading ..s
	  if (allowAboveRoot) {
	    for (; up--; up) {
	      parts.unshift('..');
	    }
	  }

	  return parts;
	}

	// Split a filename into [root, dir, basename, ext], unix version
	// 'root' is just a slash, or nothing.
	var splitPathRe =
	    /^(\/?|)([\s\S]*?)((?:\.{1,2}|[^\/]+?|)(\.[^.\/]*|))(?:[\/]*)$/;
	var splitPath = function(filename) {
	  return splitPathRe.exec(filename).slice(1);
	};

	// path.resolve([from ...], to)
	// posix version
	exports.resolve = function() {
	  var resolvedPath = '',
	      resolvedAbsolute = false;

	  for (var i = arguments.length - 1; i >= -1 && !resolvedAbsolute; i--) {
	    var path = (i >= 0) ? arguments[i] : process.cwd();

	    // Skip empty and invalid entries
	    if (typeof path !== 'string') {
	      throw new TypeError('Arguments to path.resolve must be strings');
	    } else if (!path) {
	      continue;
	    }

	    resolvedPath = path + '/' + resolvedPath;
	    resolvedAbsolute = path.charAt(0) === '/';
	  }

	  // At this point the path should be resolved to a full absolute path, but
	  // handle relative paths to be safe (might happen when process.cwd() fails)

	  // Normalize the path
	  resolvedPath = normalizeArray(filter(resolvedPath.split('/'), function(p) {
	    return !!p;
	  }), !resolvedAbsolute).join('/');

	  return ((resolvedAbsolute ? '/' : '') + resolvedPath) || '.';
	};

	// path.normalize(path)
	// posix version
	exports.normalize = function(path) {
	  var isAbsolute = exports.isAbsolute(path),
	      trailingSlash = substr(path, -1) === '/';

	  // Normalize the path
	  path = normalizeArray(filter(path.split('/'), function(p) {
	    return !!p;
	  }), !isAbsolute).join('/');

	  if (!path && !isAbsolute) {
	    path = '.';
	  }
	  if (path && trailingSlash) {
	    path += '/';
	  }

	  return (isAbsolute ? '/' : '') + path;
	};

	// posix version
	exports.isAbsolute = function(path) {
	  return path.charAt(0) === '/';
	};

	// posix version
	exports.join = function() {
	  var paths = Array.prototype.slice.call(arguments, 0);
	  return exports.normalize(filter(paths, function(p, index) {
	    if (typeof p !== 'string') {
	      throw new TypeError('Arguments to path.join must be strings');
	    }
	    return p;
	  }).join('/'));
	};


	// path.relative(from, to)
	// posix version
	exports.relative = function(from, to) {
	  from = exports.resolve(from).substr(1);
	  to = exports.resolve(to).substr(1);

	  function trim(arr) {
	    var start = 0;
	    for (; start < arr.length; start++) {
	      if (arr[start] !== '') break;
	    }

	    var end = arr.length - 1;
	    for (; end >= 0; end--) {
	      if (arr[end] !== '') break;
	    }

	    if (start > end) return [];
	    return arr.slice(start, end - start + 1);
	  }

	  var fromParts = trim(from.split('/'));
	  var toParts = trim(to.split('/'));

	  var length = Math.min(fromParts.length, toParts.length);
	  var samePartsLength = length;
	  for (var i = 0; i < length; i++) {
	    if (fromParts[i] !== toParts[i]) {
	      samePartsLength = i;
	      break;
	    }
	  }

	  var outputParts = [];
	  for (var i = samePartsLength; i < fromParts.length; i++) {
	    outputParts.push('..');
	  }

	  outputParts = outputParts.concat(toParts.slice(samePartsLength));

	  return outputParts.join('/');
	};

	exports.sep = '/';
	exports.delimiter = ':';

	exports.dirname = function(path) {
	  var result = splitPath(path),
	      root = result[0],
	      dir = result[1];

	  if (!root && !dir) {
	    // No dirname whatsoever
	    return '.';
	  }

	  if (dir) {
	    // It has a dirname, strip trailing slash
	    dir = dir.substr(0, dir.length - 1);
	  }

	  return root + dir;
	};


	exports.basename = function(path, ext) {
	  var f = splitPath(path)[2];
	  // TODO: make this comparison case-insensitive on windows?
	  if (ext && f.substr(-1 * ext.length) === ext) {
	    f = f.substr(0, f.length - ext.length);
	  }
	  return f;
	};


	exports.extname = function(path) {
	  return splitPath(path)[3];
	};

	function filter (xs, f) {
	    if (xs.filter) return xs.filter(f);
	    var res = [];
	    for (var i = 0; i < xs.length; i++) {
	        if (f(xs[i], i, xs)) res.push(xs[i]);
	    }
	    return res;
	}

	// String.prototype.substr - negative index don't work in IE8
	var substr = 'ab'.substr(-1) === 'b'
	    ? function (str, start, len) { return str.substr(start, len) }
	    : function (str, start, len) {
	        if (start < 0) start = str.length + start;
	        return str.substr(start, len);
	    }
	;

	/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__(76)))

/***/ },
/* 76 */
/***/ function(module, exports) {

	// shim for using process in browser

	var process = module.exports = {};
	var queue = [];
	var draining = false;
	var currentQueue;
	var queueIndex = -1;

	function cleanUpNextTick() {
	    draining = false;
	    if (currentQueue.length) {
	        queue = currentQueue.concat(queue);
	    } else {
	        queueIndex = -1;
	    }
	    if (queue.length) {
	        drainQueue();
	    }
	}

	function drainQueue() {
	    if (draining) {
	        return;
	    }
	    var timeout = setTimeout(cleanUpNextTick);
	    draining = true;

	    var len = queue.length;
	    while(len) {
	        currentQueue = queue;
	        queue = [];
	        while (++queueIndex < len) {
	            if (currentQueue) {
	                currentQueue[queueIndex].run();
	            }
	        }
	        queueIndex = -1;
	        len = queue.length;
	    }
	    currentQueue = null;
	    draining = false;
	    clearTimeout(timeout);
	}

	process.nextTick = function (fun) {
	    var args = new Array(arguments.length - 1);
	    if (arguments.length > 1) {
	        for (var i = 1; i < arguments.length; i++) {
	            args[i - 1] = arguments[i];
	        }
	    }
	    queue.push(new Item(fun, args));
	    if (queue.length === 1 && !draining) {
	        setTimeout(drainQueue, 0);
	    }
	};

	// v8 likes predictible objects
	function Item(fun, array) {
	    this.fun = fun;
	    this.array = array;
	}
	Item.prototype.run = function () {
	    this.fun.apply(null, this.array);
	};
	process.title = 'browser';
	process.browser = true;
	process.env = {};
	process.argv = [];
	process.version = ''; // empty string to avoid regexp issues
	process.versions = {};

	function noop() {}

	process.on = noop;
	process.addListener = noop;
	process.once = noop;
	process.off = noop;
	process.removeListener = noop;
	process.removeAllListeners = noop;
	process.emit = noop;

	process.binding = function (name) {
	    throw new Error('process.binding is not supported');
	};

	process.cwd = function () { return '/' };
	process.chdir = function (dir) {
	    throw new Error('process.chdir is not supported');
	};
	process.umask = function() { return 0; };


/***/ },
/* 77 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var _cudl = __webpack_require__(3);

	var _cudl2 = _interopRequireDefault(_cudl);

	/** views */

	var _viewsPanel = __webpack_require__(78);

	var _viewsPanel2 = _interopRequireDefault(_viewsPanel);

	var _viewsToolbar = __webpack_require__(87);

	var _viewsToolbar2 = _interopRequireDefault(_viewsToolbar);

	var _viewsDialog = __webpack_require__(88);

	var _viewsDialog2 = _interopRequireDefault(_viewsDialog);

	/** controllers */

	var _commonController = __webpack_require__(89);

	var _commonController2 = _interopRequireDefault(_commonController);

	var _panelcontroller = __webpack_require__(90);

	var _panelcontroller2 = _interopRequireDefault(_panelcontroller);

	var _toolbarcontroller = __webpack_require__(91);

	var _toolbarcontroller2 = _interopRequireDefault(_toolbarcontroller);

	var _dialogcontroller = __webpack_require__(94);

	var _dialogcontroller2 = _interopRequireDefault(_dialogcontroller);

	var _osdcontroller = __webpack_require__(99);

	var _osdcontroller2 = _interopRequireDefault(_osdcontroller);

	var _tagcloudcontroller = __webpack_require__(101);

	var _tagcloudcontroller2 = _interopRequireDefault(_tagcloudcontroller);

	var TaggingController = (function (_Controller) {
	    _inherits(TaggingController, _Controller);

	    function TaggingController(options) {
	        _classCallCheck(this, TaggingController);

	        _get(Object.getPrototypeOf(TaggingController.prototype), 'constructor', this).call(this, options);
	        this.ajax_c = options.ajax_c;
	    }

	    _createClass(TaggingController, [{
	        key: 'init',
	        value: function init() {

	            //
	            // render views
	            //

	            var panel = new _viewsPanel2['default']({
	                el: $('#tagging')[0]
	            }).render();

	            var toolbar = new _viewsToolbar2['default']({
	                of: _cudl2['default'].viewer.element
	            }).render();

	            var dialog = new _viewsDialog2['default']({
	                of: _cudl2['default'].viewer.element
	            }).render();

	            //
	            // controllers
	            //

	            this.panel_c = new _panelcontroller2['default']({
	                metadata: this.metadata,
	                // page:        this.page,
	                panel: panel
	            });

	            this.tagcloud_c = new _tagcloudcontroller2['default']({
	                metadata: this.metadata,
	                // page:        this.page,
	                tagcloud: panel.tagcloud,
	                ajax_c: this.ajax_c
	            });

	            this.dialog_c = new _dialogcontroller2['default']({
	                metadata: this.metadata,
	                // page:        this.page,
	                dialog: dialog,
	                ajax_c: this.ajax_c
	            });

	            this.toolbar_c = new _toolbarcontroller2['default']({
	                metadata: this.metadata,
	                // page:        this.page,
	                toolbar: toolbar,
	                ajax_c: this.ajax_c
	            });

	            this.osd_c = new _osdcontroller2['default']({
	                metadata: this.metadata,
	                // page:        this.page,
	                osd: _cudl2['default'].viewer
	            });

	            //
	            // set controllers
	            //

	            this.dialog_c.setControllers(this.toolbar_c, this.osd_c);

	            this.osd_c.setControllers(this.dialog_c, this.toolbar_c);

	            this.toolbar_c.setControllers(this.dialog_c, this.osd_c);

	            //
	            // init controllers
	            //

	            this.panel_c.init();

	            this.tagcloud_c.init();

	            this.dialog_c.init();

	            this.toolbar_c.init();

	            this.osd_c.init();

	            //
	            // override page navigation functions
	            //

	            this.overridePrototype({
	                toolbar_c: this.toolbar_c
	            });

	            return this;
	        }
	    }, {
	        key: 'startTagging',
	        value: function startTagging() {
	            // show tagging toolbar
	            this.toolbar_c.openToolbar();
	            // show wordcloud
	            this.tagcloud_c.openCloud();
	        }
	    }, {
	        key: 'endTagging',
	        value: function endTagging() {
	            // close toolbar
	            this.toolbar_c.closeToolbar();
	            // close dialog
	            this.dialog_c.closeDialogs();
	            // clear markers
	            this.osd_c.clearMarkers();
	            // remove wordcloud from dom
	            this.tagcloud_c.closeCloud();
	        }

	        /**
	         * override page navigation functions
	         */
	    }, {
	        key: 'overridePrototype',
	        value: function overridePrototype(opts) {
	            _cudl2['default'].setupSeaDragon.prototype.nextPage1 = function () {
	                console.log('nn');
	                // draw annotation markers if toggle is on
	                if (this.opts.toolbar_c.toolbar.colorIndicator.shown) {
	                    this.opts.toolbar_c.drawMarkersAction();
	                }
	            };

	            _cudl2['default'].setupSeaDragon.prototype.prevPage = function () {
	                console.log('pp');
	                // draw annotation markers if toggle is on
	                if (this.opts.toolbar.colorIndicator.shown) {
	                    this.opts.toolbar_c.drawMarkersAction();
	                }
	            };
	        }
	    }]);

	    return TaggingController;
	})(_commonController2['default']);

	exports['default'] = TaggingController;
	module.exports = exports['default'];

/***/ },
/* 78 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var _commonView = __webpack_require__(79);

	var _commonView2 = _interopRequireDefault(_commonView);

	var _tagcloud = __webpack_require__(85);

	var _tagcloud2 = _interopRequireDefault(_tagcloud);

	var _tagexport = __webpack_require__(86);

	var _tagexport2 = _interopRequireDefault(_tagexport);

	var Panel = (function (_View) {
	    _inherits(Panel, _View);

	    function Panel(options) {
	        _classCallCheck(this, Panel);

	        _get(Object.getPrototypeOf(Panel.prototype), 'constructor', this).call(this, options);
	    }

	    _createClass(Panel, [{
	        key: 'render',
	        value: function render() {

	            this.tagcloud = new _tagcloud2['default']({
	                of: $(this.el)[0]
	            }).render();

	            this.tagexport = new _tagexport2['default']({
	                of: $(this.el)[0]
	            }).render();

	            return this;
	        }
	    }]);

	    return Panel;
	})(_commonView2['default']);

	exports['default'] = Panel;
	module.exports = exports['default'];

/***/ },
/* 79 */
/***/ function(module, exports, __webpack_require__) {

	
	/**
	 * Author: Hal Blackburn
	 */

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	var _assert = __webpack_require__(80);

	var _assert2 = _interopRequireDefault(_assert);

	var _jquery = __webpack_require__(84);

	var _jquery2 = _interopRequireDefault(_jquery);

	/**
	 * A minimal view class based on those created for cudl-embedded
	 */

	var View = (function () {
	    function View(options) {
	        _classCallCheck(this, View);

	        this.className = options.className || this.className || null;
	        this.id = options.id || this.id || null;

	        var el = options.el || this.createElement();
	        var of = options.of || this.createElement();

	        (0, _assert2['default'])(el instanceof Element, "el must be an Element", el);
	        (0, _assert2['default'])(of instanceof Element, "of must be an Element", of);
	        this.setEl(el);
	        this.setOf(of);
	    }

	    _createClass(View, [{
	        key: 'createElement',
	        value: function createElement() {
	            return (0, _jquery2['default'])('<div>').addClass(this.className).attr('id', this.id)[0];
	        }
	    }, {
	        key: 'setEl',
	        value: function setEl(el) {
	            this.$el = (0, _jquery2['default'])(el).first();
	            this.el = this.$el[0];
	        }
	    }, {
	        key: 'setOf',
	        value: function setOf(of) {
	            this.$of = (0, _jquery2['default'])(of).first();
	            this.of = this.$of[0];
	        }
	    }]);

	    return View;
	})();

	exports['default'] = View;
	module.exports = exports['default'];

/***/ },
/* 80 */
/***/ function(module, exports, __webpack_require__) {

	// http://wiki.commonjs.org/wiki/Unit_Testing/1.0
	//
	// THIS IS NOT TESTED NOR LIKELY TO WORK OUTSIDE V8!
	//
	// Originally from narwhal.js (http://narwhaljs.org)
	// Copyright (c) 2009 Thomas Robinson <280north.com>
	//
	// Permission is hereby granted, free of charge, to any person obtaining a copy
	// of this software and associated documentation files (the 'Software'), to
	// deal in the Software without restriction, including without limitation the
	// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
	// sell copies of the Software, and to permit persons to whom the Software is
	// furnished to do so, subject to the following conditions:
	//
	// The above copyright notice and this permission notice shall be included in
	// all copies or substantial portions of the Software.
	//
	// THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	// AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
	// ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
	// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

	// when used in node, this will actually load the util module we depend on
	// versus loading the builtin util module as happens otherwise
	// this is a bug in node module loading as far as I am concerned
	var util = __webpack_require__(81);

	var pSlice = Array.prototype.slice;
	var hasOwn = Object.prototype.hasOwnProperty;

	// 1. The assert module provides functions that throw
	// AssertionError's when particular conditions are not met. The
	// assert module must conform to the following interface.

	var assert = module.exports = ok;

	// 2. The AssertionError is defined in assert.
	// new assert.AssertionError({ message: message,
	//                             actual: actual,
	//                             expected: expected })

	assert.AssertionError = function AssertionError(options) {
	  this.name = 'AssertionError';
	  this.actual = options.actual;
	  this.expected = options.expected;
	  this.operator = options.operator;
	  if (options.message) {
	    this.message = options.message;
	    this.generatedMessage = false;
	  } else {
	    this.message = getMessage(this);
	    this.generatedMessage = true;
	  }
	  var stackStartFunction = options.stackStartFunction || fail;

	  if (Error.captureStackTrace) {
	    Error.captureStackTrace(this, stackStartFunction);
	  }
	  else {
	    // non v8 browsers so we can have a stacktrace
	    var err = new Error();
	    if (err.stack) {
	      var out = err.stack;

	      // try to strip useless frames
	      var fn_name = stackStartFunction.name;
	      var idx = out.indexOf('\n' + fn_name);
	      if (idx >= 0) {
	        // once we have located the function frame
	        // we need to strip out everything before it (and its line)
	        var next_line = out.indexOf('\n', idx + 1);
	        out = out.substring(next_line + 1);
	      }

	      this.stack = out;
	    }
	  }
	};

	// assert.AssertionError instanceof Error
	util.inherits(assert.AssertionError, Error);

	function replacer(key, value) {
	  if (util.isUndefined(value)) {
	    return '' + value;
	  }
	  if (util.isNumber(value) && !isFinite(value)) {
	    return value.toString();
	  }
	  if (util.isFunction(value) || util.isRegExp(value)) {
	    return value.toString();
	  }
	  return value;
	}

	function truncate(s, n) {
	  if (util.isString(s)) {
	    return s.length < n ? s : s.slice(0, n);
	  } else {
	    return s;
	  }
	}

	function getMessage(self) {
	  return truncate(JSON.stringify(self.actual, replacer), 128) + ' ' +
	         self.operator + ' ' +
	         truncate(JSON.stringify(self.expected, replacer), 128);
	}

	// At present only the three keys mentioned above are used and
	// understood by the spec. Implementations or sub modules can pass
	// other keys to the AssertionError's constructor - they will be
	// ignored.

	// 3. All of the following functions must throw an AssertionError
	// when a corresponding condition is not met, with a message that
	// may be undefined if not provided.  All assertion methods provide
	// both the actual and expected values to the assertion error for
	// display purposes.

	function fail(actual, expected, message, operator, stackStartFunction) {
	  throw new assert.AssertionError({
	    message: message,
	    actual: actual,
	    expected: expected,
	    operator: operator,
	    stackStartFunction: stackStartFunction
	  });
	}

	// EXTENSION! allows for well behaved errors defined elsewhere.
	assert.fail = fail;

	// 4. Pure assertion tests whether a value is truthy, as determined
	// by !!guard.
	// assert.ok(guard, message_opt);
	// This statement is equivalent to assert.equal(true, !!guard,
	// message_opt);. To test strictly for the value true, use
	// assert.strictEqual(true, guard, message_opt);.

	function ok(value, message) {
	  if (!value) fail(value, true, message, '==', assert.ok);
	}
	assert.ok = ok;

	// 5. The equality assertion tests shallow, coercive equality with
	// ==.
	// assert.equal(actual, expected, message_opt);

	assert.equal = function equal(actual, expected, message) {
	  if (actual != expected) fail(actual, expected, message, '==', assert.equal);
	};

	// 6. The non-equality assertion tests for whether two objects are not equal
	// with != assert.notEqual(actual, expected, message_opt);

	assert.notEqual = function notEqual(actual, expected, message) {
	  if (actual == expected) {
	    fail(actual, expected, message, '!=', assert.notEqual);
	  }
	};

	// 7. The equivalence assertion tests a deep equality relation.
	// assert.deepEqual(actual, expected, message_opt);

	assert.deepEqual = function deepEqual(actual, expected, message) {
	  if (!_deepEqual(actual, expected)) {
	    fail(actual, expected, message, 'deepEqual', assert.deepEqual);
	  }
	};

	function _deepEqual(actual, expected) {
	  // 7.1. All identical values are equivalent, as determined by ===.
	  if (actual === expected) {
	    return true;

	  } else if (util.isBuffer(actual) && util.isBuffer(expected)) {
	    if (actual.length != expected.length) return false;

	    for (var i = 0; i < actual.length; i++) {
	      if (actual[i] !== expected[i]) return false;
	    }

	    return true;

	  // 7.2. If the expected value is a Date object, the actual value is
	  // equivalent if it is also a Date object that refers to the same time.
	  } else if (util.isDate(actual) && util.isDate(expected)) {
	    return actual.getTime() === expected.getTime();

	  // 7.3 If the expected value is a RegExp object, the actual value is
	  // equivalent if it is also a RegExp object with the same source and
	  // properties (`global`, `multiline`, `lastIndex`, `ignoreCase`).
	  } else if (util.isRegExp(actual) && util.isRegExp(expected)) {
	    return actual.source === expected.source &&
	           actual.global === expected.global &&
	           actual.multiline === expected.multiline &&
	           actual.lastIndex === expected.lastIndex &&
	           actual.ignoreCase === expected.ignoreCase;

	  // 7.4. Other pairs that do not both pass typeof value == 'object',
	  // equivalence is determined by ==.
	  } else if (!util.isObject(actual) && !util.isObject(expected)) {
	    return actual == expected;

	  // 7.5 For all other Object pairs, including Array objects, equivalence is
	  // determined by having the same number of owned properties (as verified
	  // with Object.prototype.hasOwnProperty.call), the same set of keys
	  // (although not necessarily the same order), equivalent values for every
	  // corresponding key, and an identical 'prototype' property. Note: this
	  // accounts for both named and indexed properties on Arrays.
	  } else {
	    return objEquiv(actual, expected);
	  }
	}

	function isArguments(object) {
	  return Object.prototype.toString.call(object) == '[object Arguments]';
	}

	function objEquiv(a, b) {
	  if (util.isNullOrUndefined(a) || util.isNullOrUndefined(b))
	    return false;
	  // an identical 'prototype' property.
	  if (a.prototype !== b.prototype) return false;
	  // if one is a primitive, the other must be same
	  if (util.isPrimitive(a) || util.isPrimitive(b)) {
	    return a === b;
	  }
	  var aIsArgs = isArguments(a),
	      bIsArgs = isArguments(b);
	  if ((aIsArgs && !bIsArgs) || (!aIsArgs && bIsArgs))
	    return false;
	  if (aIsArgs) {
	    a = pSlice.call(a);
	    b = pSlice.call(b);
	    return _deepEqual(a, b);
	  }
	  var ka = objectKeys(a),
	      kb = objectKeys(b),
	      key, i;
	  // having the same number of owned properties (keys incorporates
	  // hasOwnProperty)
	  if (ka.length != kb.length)
	    return false;
	  //the same set of keys (although not necessarily the same order),
	  ka.sort();
	  kb.sort();
	  //~~~cheap key test
	  for (i = ka.length - 1; i >= 0; i--) {
	    if (ka[i] != kb[i])
	      return false;
	  }
	  //equivalent values for every corresponding key, and
	  //~~~possibly expensive deep test
	  for (i = ka.length - 1; i >= 0; i--) {
	    key = ka[i];
	    if (!_deepEqual(a[key], b[key])) return false;
	  }
	  return true;
	}

	// 8. The non-equivalence assertion tests for any deep inequality.
	// assert.notDeepEqual(actual, expected, message_opt);

	assert.notDeepEqual = function notDeepEqual(actual, expected, message) {
	  if (_deepEqual(actual, expected)) {
	    fail(actual, expected, message, 'notDeepEqual', assert.notDeepEqual);
	  }
	};

	// 9. The strict equality assertion tests strict equality, as determined by ===.
	// assert.strictEqual(actual, expected, message_opt);

	assert.strictEqual = function strictEqual(actual, expected, message) {
	  if (actual !== expected) {
	    fail(actual, expected, message, '===', assert.strictEqual);
	  }
	};

	// 10. The strict non-equality assertion tests for strict inequality, as
	// determined by !==.  assert.notStrictEqual(actual, expected, message_opt);

	assert.notStrictEqual = function notStrictEqual(actual, expected, message) {
	  if (actual === expected) {
	    fail(actual, expected, message, '!==', assert.notStrictEqual);
	  }
	};

	function expectedException(actual, expected) {
	  if (!actual || !expected) {
	    return false;
	  }

	  if (Object.prototype.toString.call(expected) == '[object RegExp]') {
	    return expected.test(actual);
	  } else if (actual instanceof expected) {
	    return true;
	  } else if (expected.call({}, actual) === true) {
	    return true;
	  }

	  return false;
	}

	function _throws(shouldThrow, block, expected, message) {
	  var actual;

	  if (util.isString(expected)) {
	    message = expected;
	    expected = null;
	  }

	  try {
	    block();
	  } catch (e) {
	    actual = e;
	  }

	  message = (expected && expected.name ? ' (' + expected.name + ').' : '.') +
	            (message ? ' ' + message : '.');

	  if (shouldThrow && !actual) {
	    fail(actual, expected, 'Missing expected exception' + message);
	  }

	  if (!shouldThrow && expectedException(actual, expected)) {
	    fail(actual, expected, 'Got unwanted exception' + message);
	  }

	  if ((shouldThrow && actual && expected &&
	      !expectedException(actual, expected)) || (!shouldThrow && actual)) {
	    throw actual;
	  }
	}

	// 11. Expected to throw an error:
	// assert.throws(block, Error_opt, message_opt);

	assert.throws = function(block, /*optional*/error, /*optional*/message) {
	  _throws.apply(this, [true].concat(pSlice.call(arguments)));
	};

	// EXTENSION! This is annoying to write outside this module.
	assert.doesNotThrow = function(block, /*optional*/message) {
	  _throws.apply(this, [false].concat(pSlice.call(arguments)));
	};

	assert.ifError = function(err) { if (err) {throw err;}};

	var objectKeys = Object.keys || function (obj) {
	  var keys = [];
	  for (var key in obj) {
	    if (hasOwn.call(obj, key)) keys.push(key);
	  }
	  return keys;
	};


/***/ },
/* 81 */
/***/ function(module, exports, __webpack_require__) {

	/* WEBPACK VAR INJECTION */(function(global, process) {// Copyright Joyent, Inc. and other Node contributors.
	//
	// Permission is hereby granted, free of charge, to any person obtaining a
	// copy of this software and associated documentation files (the
	// "Software"), to deal in the Software without restriction, including
	// without limitation the rights to use, copy, modify, merge, publish,
	// distribute, sublicense, and/or sell copies of the Software, and to permit
	// persons to whom the Software is furnished to do so, subject to the
	// following conditions:
	//
	// The above copyright notice and this permission notice shall be included
	// in all copies or substantial portions of the Software.
	//
	// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
	// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
	// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
	// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
	// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
	// USE OR OTHER DEALINGS IN THE SOFTWARE.

	var formatRegExp = /%[sdj%]/g;
	exports.format = function(f) {
	  if (!isString(f)) {
	    var objects = [];
	    for (var i = 0; i < arguments.length; i++) {
	      objects.push(inspect(arguments[i]));
	    }
	    return objects.join(' ');
	  }

	  var i = 1;
	  var args = arguments;
	  var len = args.length;
	  var str = String(f).replace(formatRegExp, function(x) {
	    if (x === '%%') return '%';
	    if (i >= len) return x;
	    switch (x) {
	      case '%s': return String(args[i++]);
	      case '%d': return Number(args[i++]);
	      case '%j':
	        try {
	          return JSON.stringify(args[i++]);
	        } catch (_) {
	          return '[Circular]';
	        }
	      default:
	        return x;
	    }
	  });
	  for (var x = args[i]; i < len; x = args[++i]) {
	    if (isNull(x) || !isObject(x)) {
	      str += ' ' + x;
	    } else {
	      str += ' ' + inspect(x);
	    }
	  }
	  return str;
	};


	// Mark that a method should not be used.
	// Returns a modified function which warns once by default.
	// If --no-deprecation is set, then it is a no-op.
	exports.deprecate = function(fn, msg) {
	  // Allow for deprecating things in the process of starting up.
	  if (isUndefined(global.process)) {
	    return function() {
	      return exports.deprecate(fn, msg).apply(this, arguments);
	    };
	  }

	  if (process.noDeprecation === true) {
	    return fn;
	  }

	  var warned = false;
	  function deprecated() {
	    if (!warned) {
	      if (process.throwDeprecation) {
	        throw new Error(msg);
	      } else if (process.traceDeprecation) {
	        console.trace(msg);
	      } else {
	        console.error(msg);
	      }
	      warned = true;
	    }
	    return fn.apply(this, arguments);
	  }

	  return deprecated;
	};


	var debugs = {};
	var debugEnviron;
	exports.debuglog = function(set) {
	  if (isUndefined(debugEnviron))
	    debugEnviron = process.env.NODE_DEBUG || '';
	  set = set.toUpperCase();
	  if (!debugs[set]) {
	    if (new RegExp('\\b' + set + '\\b', 'i').test(debugEnviron)) {
	      var pid = process.pid;
	      debugs[set] = function() {
	        var msg = exports.format.apply(exports, arguments);
	        console.error('%s %d: %s', set, pid, msg);
	      };
	    } else {
	      debugs[set] = function() {};
	    }
	  }
	  return debugs[set];
	};


	/**
	 * Echos the value of a value. Trys to print the value out
	 * in the best way possible given the different types.
	 *
	 * @param {Object} obj The object to print out.
	 * @param {Object} opts Optional options object that alters the output.
	 */
	/* legacy: obj, showHidden, depth, colors*/
	function inspect(obj, opts) {
	  // default options
	  var ctx = {
	    seen: [],
	    stylize: stylizeNoColor
	  };
	  // legacy...
	  if (arguments.length >= 3) ctx.depth = arguments[2];
	  if (arguments.length >= 4) ctx.colors = arguments[3];
	  if (isBoolean(opts)) {
	    // legacy...
	    ctx.showHidden = opts;
	  } else if (opts) {
	    // got an "options" object
	    exports._extend(ctx, opts);
	  }
	  // set default options
	  if (isUndefined(ctx.showHidden)) ctx.showHidden = false;
	  if (isUndefined(ctx.depth)) ctx.depth = 2;
	  if (isUndefined(ctx.colors)) ctx.colors = false;
	  if (isUndefined(ctx.customInspect)) ctx.customInspect = true;
	  if (ctx.colors) ctx.stylize = stylizeWithColor;
	  return formatValue(ctx, obj, ctx.depth);
	}
	exports.inspect = inspect;


	// http://en.wikipedia.org/wiki/ANSI_escape_code#graphics
	inspect.colors = {
	  'bold' : [1, 22],
	  'italic' : [3, 23],
	  'underline' : [4, 24],
	  'inverse' : [7, 27],
	  'white' : [37, 39],
	  'grey' : [90, 39],
	  'black' : [30, 39],
	  'blue' : [34, 39],
	  'cyan' : [36, 39],
	  'green' : [32, 39],
	  'magenta' : [35, 39],
	  'red' : [31, 39],
	  'yellow' : [33, 39]
	};

	// Don't use 'blue' not visible on cmd.exe
	inspect.styles = {
	  'special': 'cyan',
	  'number': 'yellow',
	  'boolean': 'yellow',
	  'undefined': 'grey',
	  'null': 'bold',
	  'string': 'green',
	  'date': 'magenta',
	  // "name": intentionally not styling
	  'regexp': 'red'
	};


	function stylizeWithColor(str, styleType) {
	  var style = inspect.styles[styleType];

	  if (style) {
	    return '\u001b[' + inspect.colors[style][0] + 'm' + str +
	           '\u001b[' + inspect.colors[style][1] + 'm';
	  } else {
	    return str;
	  }
	}


	function stylizeNoColor(str, styleType) {
	  return str;
	}


	function arrayToHash(array) {
	  var hash = {};

	  array.forEach(function(val, idx) {
	    hash[val] = true;
	  });

	  return hash;
	}


	function formatValue(ctx, value, recurseTimes) {
	  // Provide a hook for user-specified inspect functions.
	  // Check that value is an object with an inspect function on it
	  if (ctx.customInspect &&
	      value &&
	      isFunction(value.inspect) &&
	      // Filter out the util module, it's inspect function is special
	      value.inspect !== exports.inspect &&
	      // Also filter out any prototype objects using the circular check.
	      !(value.constructor && value.constructor.prototype === value)) {
	    var ret = value.inspect(recurseTimes, ctx);
	    if (!isString(ret)) {
	      ret = formatValue(ctx, ret, recurseTimes);
	    }
	    return ret;
	  }

	  // Primitive types cannot have properties
	  var primitive = formatPrimitive(ctx, value);
	  if (primitive) {
	    return primitive;
	  }

	  // Look up the keys of the object.
	  var keys = Object.keys(value);
	  var visibleKeys = arrayToHash(keys);

	  if (ctx.showHidden) {
	    keys = Object.getOwnPropertyNames(value);
	  }

	  // IE doesn't make error fields non-enumerable
	  // http://msdn.microsoft.com/en-us/library/ie/dww52sbt(v=vs.94).aspx
	  if (isError(value)
	      && (keys.indexOf('message') >= 0 || keys.indexOf('description') >= 0)) {
	    return formatError(value);
	  }

	  // Some type of object without properties can be shortcutted.
	  if (keys.length === 0) {
	    if (isFunction(value)) {
	      var name = value.name ? ': ' + value.name : '';
	      return ctx.stylize('[Function' + name + ']', 'special');
	    }
	    if (isRegExp(value)) {
	      return ctx.stylize(RegExp.prototype.toString.call(value), 'regexp');
	    }
	    if (isDate(value)) {
	      return ctx.stylize(Date.prototype.toString.call(value), 'date');
	    }
	    if (isError(value)) {
	      return formatError(value);
	    }
	  }

	  var base = '', array = false, braces = ['{', '}'];

	  // Make Array say that they are Array
	  if (isArray(value)) {
	    array = true;
	    braces = ['[', ']'];
	  }

	  // Make functions say that they are functions
	  if (isFunction(value)) {
	    var n = value.name ? ': ' + value.name : '';
	    base = ' [Function' + n + ']';
	  }

	  // Make RegExps say that they are RegExps
	  if (isRegExp(value)) {
	    base = ' ' + RegExp.prototype.toString.call(value);
	  }

	  // Make dates with properties first say the date
	  if (isDate(value)) {
	    base = ' ' + Date.prototype.toUTCString.call(value);
	  }

	  // Make error with message first say the error
	  if (isError(value)) {
	    base = ' ' + formatError(value);
	  }

	  if (keys.length === 0 && (!array || value.length == 0)) {
	    return braces[0] + base + braces[1];
	  }

	  if (recurseTimes < 0) {
	    if (isRegExp(value)) {
	      return ctx.stylize(RegExp.prototype.toString.call(value), 'regexp');
	    } else {
	      return ctx.stylize('[Object]', 'special');
	    }
	  }

	  ctx.seen.push(value);

	  var output;
	  if (array) {
	    output = formatArray(ctx, value, recurseTimes, visibleKeys, keys);
	  } else {
	    output = keys.map(function(key) {
	      return formatProperty(ctx, value, recurseTimes, visibleKeys, key, array);
	    });
	  }

	  ctx.seen.pop();

	  return reduceToSingleString(output, base, braces);
	}


	function formatPrimitive(ctx, value) {
	  if (isUndefined(value))
	    return ctx.stylize('undefined', 'undefined');
	  if (isString(value)) {
	    var simple = '\'' + JSON.stringify(value).replace(/^"|"$/g, '')
	                                             .replace(/'/g, "\\'")
	                                             .replace(/\\"/g, '"') + '\'';
	    return ctx.stylize(simple, 'string');
	  }
	  if (isNumber(value))
	    return ctx.stylize('' + value, 'number');
	  if (isBoolean(value))
	    return ctx.stylize('' + value, 'boolean');
	  // For some reason typeof null is "object", so special case here.
	  if (isNull(value))
	    return ctx.stylize('null', 'null');
	}


	function formatError(value) {
	  return '[' + Error.prototype.toString.call(value) + ']';
	}


	function formatArray(ctx, value, recurseTimes, visibleKeys, keys) {
	  var output = [];
	  for (var i = 0, l = value.length; i < l; ++i) {
	    if (hasOwnProperty(value, String(i))) {
	      output.push(formatProperty(ctx, value, recurseTimes, visibleKeys,
	          String(i), true));
	    } else {
	      output.push('');
	    }
	  }
	  keys.forEach(function(key) {
	    if (!key.match(/^\d+$/)) {
	      output.push(formatProperty(ctx, value, recurseTimes, visibleKeys,
	          key, true));
	    }
	  });
	  return output;
	}


	function formatProperty(ctx, value, recurseTimes, visibleKeys, key, array) {
	  var name, str, desc;
	  desc = Object.getOwnPropertyDescriptor(value, key) || { value: value[key] };
	  if (desc.get) {
	    if (desc.set) {
	      str = ctx.stylize('[Getter/Setter]', 'special');
	    } else {
	      str = ctx.stylize('[Getter]', 'special');
	    }
	  } else {
	    if (desc.set) {
	      str = ctx.stylize('[Setter]', 'special');
	    }
	  }
	  if (!hasOwnProperty(visibleKeys, key)) {
	    name = '[' + key + ']';
	  }
	  if (!str) {
	    if (ctx.seen.indexOf(desc.value) < 0) {
	      if (isNull(recurseTimes)) {
	        str = formatValue(ctx, desc.value, null);
	      } else {
	        str = formatValue(ctx, desc.value, recurseTimes - 1);
	      }
	      if (str.indexOf('\n') > -1) {
	        if (array) {
	          str = str.split('\n').map(function(line) {
	            return '  ' + line;
	          }).join('\n').substr(2);
	        } else {
	          str = '\n' + str.split('\n').map(function(line) {
	            return '   ' + line;
	          }).join('\n');
	        }
	      }
	    } else {
	      str = ctx.stylize('[Circular]', 'special');
	    }
	  }
	  if (isUndefined(name)) {
	    if (array && key.match(/^\d+$/)) {
	      return str;
	    }
	    name = JSON.stringify('' + key);
	    if (name.match(/^"([a-zA-Z_][a-zA-Z_0-9]*)"$/)) {
	      name = name.substr(1, name.length - 2);
	      name = ctx.stylize(name, 'name');
	    } else {
	      name = name.replace(/'/g, "\\'")
	                 .replace(/\\"/g, '"')
	                 .replace(/(^"|"$)/g, "'");
	      name = ctx.stylize(name, 'string');
	    }
	  }

	  return name + ': ' + str;
	}


	function reduceToSingleString(output, base, braces) {
	  var numLinesEst = 0;
	  var length = output.reduce(function(prev, cur) {
	    numLinesEst++;
	    if (cur.indexOf('\n') >= 0) numLinesEst++;
	    return prev + cur.replace(/\u001b\[\d\d?m/g, '').length + 1;
	  }, 0);

	  if (length > 60) {
	    return braces[0] +
	           (base === '' ? '' : base + '\n ') +
	           ' ' +
	           output.join(',\n  ') +
	           ' ' +
	           braces[1];
	  }

	  return braces[0] + base + ' ' + output.join(', ') + ' ' + braces[1];
	}


	// NOTE: These type checking functions intentionally don't use `instanceof`
	// because it is fragile and can be easily faked with `Object.create()`.
	function isArray(ar) {
	  return Array.isArray(ar);
	}
	exports.isArray = isArray;

	function isBoolean(arg) {
	  return typeof arg === 'boolean';
	}
	exports.isBoolean = isBoolean;

	function isNull(arg) {
	  return arg === null;
	}
	exports.isNull = isNull;

	function isNullOrUndefined(arg) {
	  return arg == null;
	}
	exports.isNullOrUndefined = isNullOrUndefined;

	function isNumber(arg) {
	  return typeof arg === 'number';
	}
	exports.isNumber = isNumber;

	function isString(arg) {
	  return typeof arg === 'string';
	}
	exports.isString = isString;

	function isSymbol(arg) {
	  return typeof arg === 'symbol';
	}
	exports.isSymbol = isSymbol;

	function isUndefined(arg) {
	  return arg === void 0;
	}
	exports.isUndefined = isUndefined;

	function isRegExp(re) {
	  return isObject(re) && objectToString(re) === '[object RegExp]';
	}
	exports.isRegExp = isRegExp;

	function isObject(arg) {
	  return typeof arg === 'object' && arg !== null;
	}
	exports.isObject = isObject;

	function isDate(d) {
	  return isObject(d) && objectToString(d) === '[object Date]';
	}
	exports.isDate = isDate;

	function isError(e) {
	  return isObject(e) &&
	      (objectToString(e) === '[object Error]' || e instanceof Error);
	}
	exports.isError = isError;

	function isFunction(arg) {
	  return typeof arg === 'function';
	}
	exports.isFunction = isFunction;

	function isPrimitive(arg) {
	  return arg === null ||
	         typeof arg === 'boolean' ||
	         typeof arg === 'number' ||
	         typeof arg === 'string' ||
	         typeof arg === 'symbol' ||  // ES6 symbol
	         typeof arg === 'undefined';
	}
	exports.isPrimitive = isPrimitive;

	exports.isBuffer = __webpack_require__(82);

	function objectToString(o) {
	  return Object.prototype.toString.call(o);
	}


	function pad(n) {
	  return n < 10 ? '0' + n.toString(10) : n.toString(10);
	}


	var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep',
	              'Oct', 'Nov', 'Dec'];

	// 26 Feb 16:19:34
	function timestamp() {
	  var d = new Date();
	  var time = [pad(d.getHours()),
	              pad(d.getMinutes()),
	              pad(d.getSeconds())].join(':');
	  return [d.getDate(), months[d.getMonth()], time].join(' ');
	}


	// log is just a thin wrapper to console.log that prepends a timestamp
	exports.log = function() {
	  console.log('%s - %s', timestamp(), exports.format.apply(exports, arguments));
	};


	/**
	 * Inherit the prototype methods from one constructor into another.
	 *
	 * The Function.prototype.inherits from lang.js rewritten as a standalone
	 * function (not on Function.prototype). NOTE: If this file is to be loaded
	 * during bootstrapping this function needs to be rewritten using some native
	 * functions as prototype setup using normal JavaScript does not work as
	 * expected during bootstrapping (see mirror.js in r114903).
	 *
	 * @param {function} ctor Constructor function which needs to inherit the
	 *     prototype.
	 * @param {function} superCtor Constructor function to inherit prototype from.
	 */
	exports.inherits = __webpack_require__(83);

	exports._extend = function(origin, add) {
	  // Don't do anything if add isn't an object
	  if (!add || !isObject(add)) return origin;

	  var keys = Object.keys(add);
	  var i = keys.length;
	  while (i--) {
	    origin[keys[i]] = add[keys[i]];
	  }
	  return origin;
	};

	function hasOwnProperty(obj, prop) {
	  return Object.prototype.hasOwnProperty.call(obj, prop);
	}

	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }()), __webpack_require__(76)))

/***/ },
/* 82 */
/***/ function(module, exports) {

	module.exports = function isBuffer(arg) {
	  return arg && typeof arg === 'object'
	    && typeof arg.copy === 'function'
	    && typeof arg.fill === 'function'
	    && typeof arg.readUInt8 === 'function';
	}

/***/ },
/* 83 */
/***/ function(module, exports) {

	if (typeof Object.create === 'function') {
	  // implementation from standard node.js 'util' module
	  module.exports = function inherits(ctor, superCtor) {
	    ctor.super_ = superCtor
	    ctor.prototype = Object.create(superCtor.prototype, {
	      constructor: {
	        value: ctor,
	        enumerable: false,
	        writable: true,
	        configurable: true
	      }
	    });
	  };
	} else {
	  // old school shim for old browsers
	  module.exports = function inherits(ctor, superCtor) {
	    ctor.super_ = superCtor
	    var TempCtor = function () {}
	    TempCtor.prototype = superCtor.prototype
	    ctor.prototype = new TempCtor()
	    ctor.prototype.constructor = ctor
	  }
	}


/***/ },
/* 84 */
/***/ function(module, exports) {

	module.exports = jQuery;

/***/ },
/* 85 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var _commonView = __webpack_require__(79);

	var _commonView2 = _interopRequireDefault(_commonView);

	var TagCloud = (function (_View) {
	    _inherits(TagCloud, _View);

	    function TagCloud(options) {
	        _classCallCheck(this, TagCloud);

	        _get(Object.getPrototypeOf(TagCloud.prototype), 'constructor', this).call(this, options);
	    }

	    _createClass(TagCloud, [{
	        key: 'render',
	        value: function render() {

	            $(this.of).append('<div class="panel panel-default cloud">' + '<div class="panel-heading">' + '<h3 class="panel-title">Keywords from mining the associated literature</h3>' + '<div class="status"></div>' + '</div>' + '<div class="panel-body">' + '<button type="button" id="refreshCloud" title="Refresh keywords"><i class="fa fa-refresh fa-lg"></i></button>' + '<div id="wordCloud"><label style="display:none;">No keywords are found</label></div>' + '</div>' + '<div class="panel-footer">' + '<div>' + '<i class="fa fa-info-circle fa-lg"></i>' + '<h6>You can remove the keywords that you think irrelevant by clicking them.</h6>' + '</div>' + '</div>' + '</div>');

	            return this;
	        }

	        /**
	         * element/component getter
	         */

	    }, {
	        key: 'cloud',
	        get: function get() {
	            return {
	                el: $('#wordCloud')[0],
	                label: $('#wordCloud label')[0],
	                refresh: $('#refreshCloud')[0],
	                status: $('.cloud .status')[0],
	                text: 'svg text'
	            };
	        }
	    }]);

	    return TagCloud;
	})(_commonView2['default']);

	exports['default'] = TagCloud;
	module.exports = exports['default'];

/***/ },
/* 86 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var _commonView = __webpack_require__(79);

	var _commonView2 = _interopRequireDefault(_commonView);

	var TagExport = (function (_View) {
	    _inherits(TagExport, _View);

	    function TagExport(options) {
	        _classCallCheck(this, TagExport);

	        _get(Object.getPrototypeOf(TagExport.prototype), 'constructor', this).call(this, options);
	    }

	    _createClass(TagExport, [{
	        key: 'render',
	        value: function render() {

	            $(this.of).append('<div class="panel panel-default tagexport">' + '<div class="panel-heading">' + '<h3 class="panel-title">Export contributions</h3>' + '</div>' + '<div class="panel-body">' + '<div class="button" id="tagExportAll">' + '<a class="btn btn-info left" href="/crowdsourcing/export/">' + '<i class="fa fa-download fa-2x pull-left"></i> Export All <br> Contributions ' + '</a>' + '</div>' + '<div class="btn-tip"><p>Export my contributions in RDF/XML format</p></div>' + '<div class="button" id="tagExport">' + '<a class="btn btn-info left" href="/crowdsourcing/export/">' + '<i class="fa fa-download fa-2x pull-left"></i> Export Document <br> Contributions ' + '</a>' + '</div>' + '<div class="btn-tip"><p>Export the contributions to the current document in RDF/XML format</p></div>' + '</div>' + '</div>');

	            this.setEl('.tagexport');

	            return this;
	        }

	        /**
	         * element/component getter
	         */

	    }, {
	        key: 'exportDoc',
	        get: function get() {
	            return {
	                el: $(this.el).find('#tagExport')[0]
	            };
	        }
	    }]);

	    return TagExport;
	})(_commonView2['default']);

	exports['default'] = TagExport;
	module.exports = exports['default'];

/***/ },
/* 87 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var _commonView = __webpack_require__(79);

	var _commonView2 = _interopRequireDefault(_commonView);

	var ToolbarView = (function (_View) {
	    _inherits(ToolbarView, _View);

	    function ToolbarView(options) {
	        _classCallCheck(this, ToolbarView);

	        _get(Object.getPrototypeOf(ToolbarView.prototype), 'constructor', this).call(this, options);
	    }

	    /**
	     * Color indicator
	     */

	    _createClass(ToolbarView, [{
	        key: 'render',
	        value: function render() {

	            // toolbar container
	            $(this.of).append('<div class="btn-toolbar toolbar-tagging"></div>');

	            // set el
	            this.setEl('.toolbar-tagging');

	            // primary toolbar
	            $(this.el).append('<div class="btn-group-vertical toolbar-primary" role="toolbar" data-toggle="buttons" aria-label="toolbar-primary">' + '<div class="btn btn-primary">' + '<i class="fa fa-tags fa-lg"/><div class="tb-label">Annotation Toolbar</div>' + '</div>' + '<label class="btn btn-primary">' + '<input type="radio" name="options" id="option1" autocomplete="off">' + '<i class="fa fa-file-text-o fa-lg"/><div class="tb-label">Fragment</div>' + '</label>' + '<label class="btn btn-primary">' + '<input type="radio" name="options" id="option2" autocomplete="off">' + '<i class="fa fa-thumb-tack fa-lg"/><div class="tb-label">Point</div>' + '</label>' + '<label class="btn btn-primary">' + '<input type="radio" name="options" id="option3" autocomplete="off">' + '<i class="fa fa-square-o fa-lg"/><div class="tb-label">Region</div>' + '</label>' + '<label class="btn btn-primary indicator">' + '<input type="radio" name="options" id="option4" autocomplete="off">' + '<i class="fa fa-toggle-off fa-lg"/><div class="tb-label">Toggle</div>' + '</label>' + '</div>');

	            // secondary toolbar
	            $(this.el).append('<div class="btn-group-vertical toolbar-secondary" role="toolbar" data-toggle="buttons" aria-label="toolbar-secondary">' + '<label class="btn btn-primary" title="Person">' + '<input type="radio" name="options" id="option1" autocomplete="off"><i class="fa fa-user fa-lg"/>' + '</label>' + '<label class="btn btn-primary" title="About">' + '<input type="radio" name="options" id="option2" autocomplete="off"><i class="fa fa-info-circle fa-lg"/>' + '</label>' + '<label class="btn btn-primary" title="Date">' + '<input type="radio" name="options" id="option3" autocomplete="off"><i class="fa fa-clock-o fa-lg"/>' + '</label>' + '<label class="btn btn-primary" title="Place">' + '<input type="radio" name="options" id="option4" autocomplete="off"><i class="fa fa-map-marker fa-lg"/>' + '</label>' + '</div>');

	            // color indicator
	            this.indicator = new ColorIndicator({ of: this.of }).render();

	            return this;
	        }

	        /**
	         * element/component getter
	         */

	    }, {
	        key: 'primary',
	        get: function get() {
	            return {
	                el: $('.toolbar-primary')[0],
	                toggle: $('.toolbar-primary').find('.indicator')[0],
	                shown: $('.toolbar-primary').hasClass('show'),
	                btns: '.toolbar-primary .btn'
	            };
	        }
	    }, {
	        key: 'secondary',
	        get: function get() {
	            return {
	                el: $('.toolbar-secondary')[0],
	                shown: $('.toolbar-secondary').hasClass('show'),
	                btns: '.toolbar-secondary .btn'
	            };
	        }
	    }, {
	        key: 'colorIndicator',
	        get: function get() {
	            return {
	                el: $('.color-indicator')[0],
	                shown: $('.color-indicator').hasClass('show')
	            };
	        }
	    }]);

	    return ToolbarView;
	})(_commonView2['default']);

	exports['default'] = ToolbarView;

	var ColorIndicator = (function (_View2) {
	    _inherits(ColorIndicator, _View2);

	    function ColorIndicator(options) {
	        _classCallCheck(this, ColorIndicator);

	        _get(Object.getPrototypeOf(ColorIndicator.prototype), 'constructor', this).call(this, options);
	    }

	    _createClass(ColorIndicator, [{
	        key: 'render',
	        value: function render() {

	            $(this.of).append('<div class="color-indicator">' + '<span class="label">COLOURS</span>' + '<div class="person"><span></span><label>Person</label></div>' + '<div class="about"><span></span><label>About</label></div>' + '<div class="date"><span></span><label>Date</label></div>' + '<div class="place"><span></span><label>Place</label></div>' + '</div>');

	            // set el
	            this.setEl('.color-indicator');

	            return this;
	        }
	    }]);

	    return ColorIndicator;
	})(_commonView2['default']);

	module.exports = exports['default'];

/***/ },
/* 88 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var _commonView = __webpack_require__(79);

	var _commonView2 = _interopRequireDefault(_commonView);

	var DialogView = (function (_View) {
	    _inherits(DialogView, _View);

	    function DialogView(options) {
	        _classCallCheck(this, DialogView);

	        _get(Object.getPrototypeOf(DialogView.prototype), 'constructor', this).call(this, options);
	    }

	    _createClass(DialogView, [{
	        key: 'render',
	        value: function render() {
	            //
	            // four tag types that user can create: person, about, date and place.
	            // The tag info dialog is to show the content of the selected
	            // tag-marker on osd.
	            //

	            $(this.of).append('<div class="dialog-tagging">'
	            // person dialog
	             + '<div class="dialog-person" title="Person">' + '<input type="text" name="fullname" value="" placeholder="Full name" id="tagName" >' + '</div>'
	            // about dialog
	             + '<div class="dialog-about" title="About">' + '<input type="text" name="about" value="" placeholder="Description" id="tagAbout" >' + '</div>'
	            // date dialog
	             + '<div class="dialog-date" title="Date">' + '<div class="date-from">' + '<label for="from">From (AD)</label>' + '<input type="text" name="from" value="" id="tagDateFrom">' + '</div>' + '<div class="date-to">' + '<label for="to">To (AD)</label>' + '<input type="text" name="to" value="" id="tagDateTo">' + '</div>' + '<div id="datepicker">' + '<label>Select date by clicking and dragging</label>' + '</div>' + '</div>'
	            // place dialog
	             + '<div class="dialog-place" title="Place">' + '<div id="map"></div>' + '<input type="text" name="place" value="" placeholder="City or Country" id="tagPlace" >' + '</div>'
	            // tag info dialog
	             + '<div class="dialog-info" title="Tag Info"></div>' + '</div>');

	            // set el
	            this.setEl('.dialog-tagging');

	            return this;
	        }
	    }, {
	        key: 'renderDeleteCnfm',
	        value: function renderDeleteCnfm() {
	            $(this.info.el).after('<div class="dlg-delete">' + '<span class="title">Are you sure to remove this annotation?</span>' + '<div class="ui-dialog-buttonset">' + '<button type="button" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary confirm-btn" role="button" id="delAnno">' + '<span class="ui-button-icon-primary ui-icon fa fa-check"></span> <span class="ui-button-text">Confirm</span>' + '</button>' + '<button type="button" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary cancel-btn" role="button" id="cancelDel">' + '<span class="ui-button-icon-primary ui-icon fa fa-close"></span> <span class="ui-button-text">Cancel</span>' + '</button>' + '</div>' + '</div>');
	        }
	    }, {
	        key: 'renderConfirmationMsg',
	        value: function renderConfirmationMsg(el, msg) {
	            $(el).after('<div class="dlg-confirmation">' + '<span class="helper"></span>' + '<span class="title">' + msg + '</span>' + '</div>');
	        }

	        /**
	         * element/component getter
	         */

	    }, {
	        key: 'person',
	        get: function get() {
	            return {
	                el: $('.dialog-person')[0],
	                input: $('#tagName')[0]
	            };
	        }
	    }, {
	        key: 'about',
	        get: function get() {
	            return {
	                el: $('.dialog-about')[0],
	                input: $('#tagAbout')[0]
	            };
	        }
	    }, {
	        key: 'place',
	        get: function get() {
	            return {
	                el: $('.dialog-place')[0],
	                map: $('#map')[0],
	                input: $('#tagPlace')[0]
	            };
	        }
	    }, {
	        key: 'date',
	        get: function get() {
	            return {
	                el: $('.dialog-date')[0],
	                datepicker: $('#datepicker')[0],
	                input1: $('#tagDateFrom')[0],
	                input2: $('#tagDateTo')[0]
	            };
	        }
	    }, {
	        key: 'info',
	        get: function get() {
	            return {
	                el: $('.dialog-info')[0]
	            };
	        }
	    }, {
	        key: 'deletecnfm',
	        get: function get() {
	            return {
	                el: $('.dlg-delete')[0],
	                btn1: '#delAnno',
	                btn2: '#cancelDel'
	            };
	        }
	    }, {
	        key: 'confirmation',
	        get: function get() {
	            return {
	                el: $('.dlg-confirmation')[0]
	            };
	        }
	    }]);

	    return DialogView;
	})(_commonView2['default']);

	exports['default'] = DialogView;
	module.exports = exports['default'];

/***/ },
/* 89 */
/***/ function(module, exports) {

	"use strict";

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

	var Controller = function Controller(options) {
	    _classCallCheck(this, Controller);

	    this.metadata = options.metadata;
	    // this.page  = options.page || 1;
	};

	exports["default"] = Controller;
	module.exports = exports["default"];

/***/ },
/* 90 */
/***/ function(module, exports, __webpack_require__) {

	"use strict";

	/** controllers */
	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var _commonController = __webpack_require__(89);

	var _commonController2 = _interopRequireDefault(_commonController);

	var path = __webpack_require__(75);

	var PanelController = (function (_Controller) {
	    _inherits(PanelController, _Controller);

	    function PanelController(options) {
	        _classCallCheck(this, PanelController);

	        _get(Object.getPrototypeOf(PanelController.prototype), 'constructor', this).call(this, options);
	        this.panel = options.panel;
	    }

	    _createClass(PanelController, [{
	        key: 'init',
	        value: function init() {
	            this.setExportUrls();
	            return this;
	        }
	    }, {
	        key: 'setExportUrls',
	        value: function setExportUrls() {
	            var $exportdoc_url = $(this.panel.tagexport.exportDoc.el).find('a');
	            var url = $exportdoc_url.attr('href');
	            $exportdoc_url.attr('href', url + this.metadata.getItemId());
	        }
	    }]);

	    return PanelController;
	})(_commonController2['default']);

	exports['default'] = PanelController;
	module.exports = exports['default'];

/***/ },
/* 91 */
/***/ function(module, exports, __webpack_require__) {

	"use strict";

	/** controllers */
	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var _commonController = __webpack_require__(89);

	var _commonController2 = _interopRequireDefault(_commonController);

	/** models */

	var _modelsAnnotation = __webpack_require__(92);

	var _modelsAnnotation2 = _interopRequireDefault(_modelsAnnotation);

	/** constants */

	var _modelsConsts = __webpack_require__(93);

	var ToolbarController = (function (_Controller) {
	    _inherits(ToolbarController, _Controller);

	    function ToolbarController(options) {
	        _classCallCheck(this, ToolbarController);

	        _get(Object.getPrototypeOf(ToolbarController.prototype), 'constructor', this).call(this, options);
	        this.toolbar = options.toolbar;
	        this.ajax_c = options.ajax_c;

	        // constants
	        this.TOOLBAR = _modelsConsts.TOOLBAR;
	        this.TTARGETS = _modelsConsts.TTARGETS;
	    }

	    _createClass(ToolbarController, [{
	        key: 'setControllers',
	        value: function setControllers(dialog_c, osd_c) {
	            this.dialog_c = dialog_c;
	            this.osd_c = osd_c;
	        }
	    }, {
	        key: 'init',
	        value: function init() {
	            this.addEventHandlers();
	            return this;
	        }
	    }, {
	        key: 'addEventHandlers',
	        value: function addEventHandlers() {

	            var self = this;

	            // primary toolbar
	            $(this.toolbar.el).on('click', this.toolbar.primary.btns, function () {
	                self.actionOnPrimaryTb(this);
	            });

	            // secondary toolbar
	            $(this.toolbar.el).on('click', this.toolbar.secondary.btns, function () {
	                self.actionOnSecondaryTb(this);
	            });
	        }

	        /*************
	         * action bit
	         *************/

	    }, {
	        key: 'actionOnPrimaryTb',
	        value: function actionOnPrimaryTb(el) {

	            var idx = $(el).index();

	            if (idx >= this.TOOLBAR.PRIMARY.DOC && idx <= this.TOOLBAR.PRIMARY.REGION) {
	                this.actionOnPrimaryTbItem(el);
	            } else if (idx == this.TOOLBAR.PRIMARY.TOGGLE) {
	                this.actionOnToggle(el);
	            }
	        }
	    }, {
	        key: 'actionOnPrimaryTbItem',
	        value: function actionOnPrimaryTbItem(el) {
	            // toggle secondary toolbar
	            this.toggleSecondaryToolbar(el);

	            // clear any overlays on osd
	            if (this.toolbar.secondary.shown) {
	                this.closeDialogs();
	                this.clearGuides();
	            }
	        }
	    }, {
	        key: 'actionOnToggle',
	        value: function actionOnToggle(el) {
	            // toggle color indicator, toggle btn
	            this.toggleColorIndicator();
	            this.toggleSwitch();
	            // hide secondary toolbar
	            this.hideSecondaryToolbar();

	            if (this.toolbar.colorIndicator.shown) {
	                // fetch and draw annotation markers
	                this.drawMarkersAction();
	            } else {
	                // clear markers
	                this.clearMarkers();
	            }
	        }
	    }, {
	        key: 'actionOnSecondaryTb',
	        value: function actionOnSecondaryTb(el) {
	            var _this = this;

	            // wait clicked toolbar item to change its state
	            // or the dialog (doc tagging) does not shown at the first time
	            setTimeout(function (e) {
	                _this.openDialog();
	            }, 0);
	        }
	    }, {
	        key: 'drawMarkersAction',
	        value: function drawMarkersAction() {
	            var _this2 = this;

	            // fetch annotations
	            this.fetchAnnotations(function (annos) {
	                // callback
	                // draw markers
	                _this2.drawMarkers(annos);
	            });
	        }

	        /**************
	         * toolbar bit
	         **************/

	    }, {
	        key: 'togglePrimaryToolbar',
	        value: function togglePrimaryToolbar() {
	            var primary = this.toolbar.primary.el;

	            $(primary).hasClass('show') ? $(primary).removeClass('show') : $(primary).addClass('show');
	        }

	        /** @el  selected item element on primary toolbar */
	    }, {
	        key: 'toggleSecondaryToolbar',
	        value: function toggleSecondaryToolbar(el) {
	            var i = $(el).index(),
	                secondary = this.toolbar.secondary.el;

	            $(this.toolbar.secondary.btns).removeClass('active');

	            if ($(el).hasClass('active')) {
	                // click on itself
	                $(secondary).hasClass('show') ? $(secondary).removeClass('show') : $(secondary).addClass('show').css({ top: i * 50 });
	            } else {
	                // click on others
	                $(el).siblings().removeClass('active');
	                $(this.toolbar.secondary.btns).removeClass('active');
	                $(secondary).addClass('show').css({ top: i * 50 });
	            }
	        }
	    }, {
	        key: 'toggleColorIndicator',
	        value: function toggleColorIndicator() {
	            var indicator = this.toolbar.colorIndicator.el;

	            $(indicator).hasClass('show') ? $(indicator).removeClass('show') : $(indicator).addClass('show');
	        }
	    }, {
	        key: 'toggleSwitch',
	        value: function toggleSwitch() {
	            $(this.toolbar.primary.toggle).find('i').toggleClass('fa-toggle-on fa-toggle-off');
	        }
	    }, {
	        key: 'hidePrimaryToolbar',
	        value: function hidePrimaryToolbar() {
	            $(this.toolbar.primary.el).removeClass('show');
	            $(this.toolbar.primary.el).find('.btn').removeClass('active');
	        }
	    }, {
	        key: 'hideSecondaryToolbar',
	        value: function hideSecondaryToolbar() {
	            $(this.toolbar.secondary.el).removeClass('show');
	            $(this.toolbar.secondary.el).find('.btn').removeClass('active');
	        }
	    }, {
	        key: 'hideColorIndicator',
	        value: function hideColorIndicator() {
	            $(this.toolbar.colorIndicator.el).removeClass('show');
	            $(this.toolbar.primary.el).find('.indicator i').removeClass('fa-toggle-on').addClass('fa-toggle-off');
	        }
	    }, {
	        key: 'hideToggle',
	        value: function hideToggle() {
	            $(this.toolbar.primary.toggle).find('i').removeClass('fa-toggle-on').addClass('fa-toggle-off');
	        }

	        /*************
	         * dialog big
	         *************/

	    }, {
	        key: 'openDialog',
	        value: function openDialog() {

	            var idx = this.getActiveTbId();

	            if (idx[0] == this.TOOLBAR.PRIMARY.DOC) {

	                this.dialog_c.openDialog({
	                    target: idx[0],
	                    type: idx[1],
	                    of: this.toolbar.el
	                });
	            }
	        }
	    }, {
	        key: 'closeDialogs',
	        value: function closeDialogs() {
	            this.dialog_c.closeDialogs();
	        }

	        /*************
	         * marker bit
	         *************/

	    }, {
	        key: 'drawMarkers',
	        value: function drawMarkers(annos) {
	            if (annos.length <= 0) return;

	            for (var i = 0; i < annos.length; i++) {
	                var anno = annos[i];

	                // skip if not for the current page
	                if (anno.page != cudl.pagenum) // assume the presence of cudl.pagenum
	                    continue;
	                // skip if for document
	                if (anno.type == this.TTARGETS.DOC) continue;

	                this.osd_c.drawMarker(i, new _modelsAnnotation2['default']({ annotation: anno }));
	            }
	        }
	    }, {
	        key: 'clearMarkers',
	        value: function clearMarkers() {
	            this.osd_c.clearMarkers();
	        }

	        /***********
	         * ajax bit
	         ***********/

	    }, {
	        key: 'fetchAnnotations',
	        value: function fetchAnnotations(callback) {
	            //
	            // ajax call to fetch annotations
	            //
	            this.ajax_c.getAnnotations(this.metadata.getItemId(), cudl.pagenum, callback); // assume the presence of cudl.pagenum
	        }

	        /**
	         *
	         */

	        /** get selected toolbar item ids, both primary and secondary toolbars */
	    }, {
	        key: 'getActiveTbId',
	        value: function getActiveTbId() {
	            return [$(this.toolbar.primary.el).find('.btn.active').index(), $(this.toolbar.secondary.el).find('.btn.active').index()];
	        }
	    }, {
	        key: 'clearGuides',
	        value: function clearGuides() {
	            this.osd_c.clearGuides();
	        }

	        /**
	         *
	         */

	    }, {
	        key: 'openToolbar',
	        value: function openToolbar() {
	            this.togglePrimaryToolbar();
	        }
	    }, {
	        key: 'closeToolbar',
	        value: function closeToolbar() {
	            // hide primary toolbar
	            this.hidePrimaryToolbar();
	            // hide secondary toolbar
	            this.hideSecondaryToolbar();
	            // hide color indicator
	            this.hideColorIndicator();
	            // switch off toggle
	            this.hideToggle();
	        }
	    }]);

	    return ToolbarController;
	})(_commonController2['default']);

	exports['default'] = ToolbarController;
	module.exports = exports['default'];

/***/ },
/* 92 */
/***/ function(module, exports) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	var Annotation = (function () {
	    function Annotation(options) {
	        _classCallCheck(this, Annotation);

	        // TODO validate values

	        if (options.hasOwnProperty('annotation')) {
	            //
	            // annotation resouce is in json format,
	            // received from the server
	            //
	            var anno = options.annotation;

	            this.target = anno.target;
	            this.type = anno.type;
	            this.name = anno.name;
	            this.raw = anno.raw;
	            this.value = anno.value;
	            this.position = anno.position;
	            this.date = anno.date;
	            this.uuid = anno.uuid;
	        } else {
	            //
	            // annotation resource is collected from
	            // user inputs
	            //
	            this.docId = options.docId;
	            this.page = options.page;

	            this.target = options.target;
	            this.type = options.type;
	            this.name = options.name;
	            this.raw = options.raw;
	            this.value = options.value;
	            this.position = options.position;
	            this.date = options.date;
	            this.uuid = options.uuid;
	        }
	    }

	    _createClass(Annotation, [{
	        key: 'getDocumentId',
	        value: function getDocumentId() {
	            return this.docId;
	        }
	    }, {
	        key: 'getPageNum',
	        value: function getPageNum() {
	            return this.page;
	        }
	    }, {
	        key: 'getTarget',
	        value: function getTarget() {
	            return this.target;
	        }
	    }, {
	        key: 'getType',
	        value: function getType() {
	            return this.type;
	        }
	    }, {
	        key: 'getName',
	        value: function getName() {
	            return this.name;
	        }
	    }, {
	        key: 'getRaw',
	        value: function getRaw() {
	            return this.raw;
	        }
	    }, {
	        key: 'getValue',
	        value: function getValue() {
	            return this.value;
	        }
	    }, {
	        key: 'getPosition',
	        value: function getPosition() {
	            return this.position;
	        }
	    }, {
	        key: 'getPositionType',
	        value: function getPositionType() {
	            return this.position.type;
	        }
	    }, {
	        key: 'getCoordinates',
	        value: function getCoordinates() {
	            return this.position.coordinates;
	        }
	    }, {
	        key: 'getDate',
	        value: function getDate() {
	            return this.date;
	        }
	    }, {
	        key: 'getUUID',
	        value: function getUUID() {
	            return this.uuid;
	        }
	    }]);

	    return Annotation;
	})();

	exports['default'] = Annotation;
	module.exports = exports['default'];

/***/ },
/* 93 */
/***/ function(module, exports) {

	/** tagging target */
	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});
	var TTARGETS = Object.freeze({
	    DOC: 'doc',
	    POINT: 'point',
	    REGION: 'region'
	});

	exports.TTARGETS = TTARGETS;
	/** tagging types */
	var TTYPES = Object.freeze({
	    PERSON: 'person',
	    ABOUT: 'about',
	    DATE: 'date',
	    PLACE: 'place',
	    INFO: 'info'
	});

	exports.TTYPES = TTYPES;
	/** position types */
	var PTYPES = Object.freeze({
	    POINT: 'point',
	    POLYGON: 'polygon'
	});

	exports.PTYPES = PTYPES;
	/** toolbar */
	var TOOLBAR = Object.freeze({
	    PRIMARY: {
	        TITLE: 0,
	        DOC: 1,
	        POINT: 2,
	        REGION: 3,
	        TOGGLE: 4
	    },
	    SECONDARY: {
	        PERSON: 0,
	        ABOUT: 1,
	        DATE: 2,
	        PLACE: 3
	    }
	});
	exports.TOOLBAR = TOOLBAR;

/***/ },
/* 94 */
/***/ function(module, exports, __webpack_require__) {

	"use strict";

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	__webpack_require__(95);

	/** controllers */

	var _commonController = __webpack_require__(89);

	var _commonController2 = _interopRequireDefault(_commonController);

	/** models */

	var _modelsAnnotation = __webpack_require__(92);

	var _modelsAnnotation2 = _interopRequireDefault(_modelsAnnotation);

	/** constants */

	var _modelsConsts = __webpack_require__(93);

	/** impls */

	var _implsGmapImpl = __webpack_require__(96);

	var _implsDatepickerImpl = __webpack_require__(98);

	var DialogController = (function (_Controller) {
	    _inherits(DialogController, _Controller);

	    function DialogController(options) {
	        _classCallCheck(this, DialogController);

	        _get(Object.getPrototypeOf(DialogController.prototype), 'constructor', this).call(this, options);
	        this.dialog = options.dialog;
	        this.ajax_c = options.ajax_c;

	        // constants
	        this.TOOLBAR = _modelsConsts.TOOLBAR;

	        // impl
	        this.gmapImpl = _implsGmapImpl.gmapImpl;
	        this.datepickerImpl = _implsDatepickerImpl.datepickerImpl;

	        // animation
	        // this.animation = {
	        //  effect: 'fade',
	        //  duration: 300
	        // };
	    }

	    _createClass(DialogController, [{
	        key: 'setControllers',
	        value: function setControllers(toolbar_c, osd_c) {
	            this.toolbar_c = toolbar_c;
	            this.osd_c = osd_c;
	        }
	    }, {
	        key: 'init',
	        value: function init() {
	            this.initDialogs();

	            this.addEventHandler();

	            // init map component
	            this.gmapImpl.init({
	                el: this.dialog.place.map,
	                input: this.dialog.place.input
	            });

	            // init datepicker component
	            this.datepickerImpl.init({
	                el: this.dialog.date.datepicker,
	                input: [this.dialog.date.input1, this.dialog.date.input2]
	            });

	            return this;
	        }
	    }, {
	        key: 'initDialogs',
	        value: function initDialogs() {

	            var person = this.dialog.person.el,
	                about = this.dialog.about.el,
	                date = this.dialog.date.el,
	                place = this.dialog.place.el,
	                info = this.dialog.info.el;

	            var dialogs = [person, about, date, place, info];

	            var self = this;

	            $.each(dialogs, function (i, v) {
	                $(v).dialog({
	                    appendTo: self.dialog.el,
	                    // show : self.animation,
	                    // hide : self.animation,
	                    autoOpen: false,
	                    resizable: false,
	                    dialogClass: 'dialog-style',
	                    create: function create(e, ui) {
	                        // replace close icon
	                        var widget = $(this).dialog("widget");
	                        $(".ui-dialog-titlebar-close span:first-child", widget).removeClass("ui-icon-closethick").addClass("fa fa-close");
	                    },
	                    open: function open(e, ui) {

	                        // fix: refresh map
	                        self.refreshMap();
	                    },
	                    beforeClose: function beforeClose(e, ui) {
	                        // $(this).dialog({hide : self.animation});
	                    },
	                    close: function close() {

	                        // close dialog
	                        self.actionOnClose(this);
	                    },
	                    buttons: [{
	                        text: self._setSubmitText(i),
	                        icons: {
	                            primary: 'fa fa-check'
	                        },
	                        click: function click() {

	                            // add or remove annotation
	                            self.actionOnSubmit(this);
	                        }
	                    }, {
	                        text: "Cancel",
	                        icons: {
	                            primary: "fa fa-close"
	                        },
	                        click: function click() {

	                            // close dialog
	                            self.actionOnClose(this);
	                        }
	                    }],
	                    width: self._setWidth(i),
	                    title: self._setTitle(i)
	                });
	            });
	        }
	    }, {
	        key: '_setTitle',
	        value: function _setTitle(idx) {
	            switch (idx) {
	                case 0:
	                    return 'Person';
	                case 1:
	                    return 'About';
	                case 2:
	                    return 'Date';
	                case 3:
	                    return 'Place';
	                case 4:
	                    return 'Info';
	                default:
	                    return 0;
	            }
	        }
	    }, {
	        key: '_setWidth',
	        value: function _setWidth(idx) {
	            switch (idx) {
	                case 0:
	                    return '14em'; // person
	                case 1:
	                    return '14em'; // about
	                case 2:
	                    return '26em'; // date
	                case 3:
	                    return '14em'; // place
	                case 4:
	                    return '14em'; // info
	                default:
	                    return 0;
	            }
	        }
	    }, {
	        key: '_setSubmitText',
	        value: function _setSubmitText(idx) {
	            switch (idx) {
	                case 0:
	                    return 'Submit'; // person
	                case 1:
	                    return 'Submit'; // about
	                case 2:
	                    return 'Submit'; // date
	                case 3:
	                    return 'Submit'; // place
	                case 4:
	                    return 'Delete'; // info
	                default:
	                    return 0;
	            }
	        }
	    }, {
	        key: 'addEventHandler',
	        value: function addEventHandler() {
	            var _this = this;

	            // delete dialog submit event handlers
	            $(this.dialog.el).on('click', this.dialog.deletecnfm.btn1, function (e) {
	                _this.actionOnDelete(_this.dialog.info.el);
	            });

	            // // delete dialog cancel event handlers
	            $(this.dialog.el).on('click', this.dialog.deletecnfm.btn2, function (e) {
	                _this.actionOnClose(_this.dialog.info.el);
	            });
	        }

	        /*************
	         * action bit
	         *************/

	    }, {
	        key: 'actionOnSubmit',
	        value: function actionOnSubmit(el) {
	            var _this2 = this;

	            // add annotation if any tagging dialog
	            if (el != this.dialog.info.el) {

	                // add annotation
	                this.addAnnotation(function (anno, resp) {
	                    // callback
	                    // show confirmation message
	                    _this2.showConfirmationMsg(el, 'add');
	                    // draw marker if toggle is on
	                    if (_this2.toolbar_c.toolbar.colorIndicator.shown) {
	                        // draw new added marker
	                        _this2.drawMarker(anno);
	                    }
	                });
	            }
	            // remove annotation if info dialog
	            else {
	                    // show delete confirmation
	                    this.showDeleteDlg();
	                }
	        }
	    }, {
	        key: 'actionOnClose',
	        value: function actionOnClose(el) {
	            // close dialog
	            this.closeDialog(el);
	            // remove osd guide stuffs
	            this.clearGuides();
	            // remove markers
	            // this.clearMarkers();
	        }
	    }, {
	        key: 'actionOnDelete',
	        value: function actionOnDelete(el) {
	            var _this3 = this;

	            // remove annotation
	            this.removeAnnotation(function (anno, resp) {
	                // callback
	                // remove confirmation
	                _this3.hideDeleteDlg();
	                // show result message
	                _this3.showConfirmationMsg(el, 'del');

	                // clear corresponding marker
	                _this3.clearMarker($(el).data('id'));
	            });
	        }

	        /*************
	         * dialog bit
	         *************/

	        /**
	         * @opts.target     the selected idx of primary toolbar
	         * @opts.type       the selected idx of secondary toolbar
	         * @opts.position   if it's not null, it is the center coordinates of element
	         * @opts.of         which element to position against
	         */
	    }, {
	        key: 'openDialog',
	        value: function openDialog(opts) {
	            // try to close any opened dialogs
	            this.closeDialogs();

	            var dlg, my, at;

	            // tagging dialogs
	            if (!_.isUndefined(opts.target)) {
	                switch (opts.type) {
	                    case this.TOOLBAR.SECONDARY.PERSON:
	                        dlg = this.dialog.person.el;break;
	                    case this.TOOLBAR.SECONDARY.ABOUT:
	                        dlg = this.dialog.about.el;break;
	                    case this.TOOLBAR.SECONDARY.DATE:
	                        dlg = this.dialog.date.el;break;
	                    case this.TOOLBAR.SECONDARY.PLACE:
	                        dlg = this.dialog.place.el;break;
	                }
	                switch (opts.target) {
	                    case this.TOOLBAR.PRIMARY.DOC:
	                        my = 'left+25 top+125';at = 'right top';break;
	                    case this.TOOLBAR.PRIMARY.POINT:
	                        my = 'center top+40';at = 'left+' + opts.position.x + ' top+' + opts.position.y;break;
	                    case this.TOOLBAR.PRIMARY.REGION:
	                        my = 'center top+40';at = 'left+' + opts.position.x + ' top+' + opts.position.y;break;
	                }
	            }
	            // info dialog
	            else {
	                    dlg = this.dialog.info.el;
	                    my = 'center top+20', at = 'center bottom';

	                    if (!_.isUndefined(opts.anno)) {
	                        $(dlg).data('uuid', opts.anno.uuid);
	                        $(dlg).data('id', opts.anno.id);
	                        $(dlg).html(opts.anno.content);
	                    }
	                }

	            // open dialog
	            $(dlg).dialog({
	                position: {
	                    my: my,
	                    at: at,
	                    of: opts.of || this.dialog.of,
	                    collision: 'fit'
	                },
	                hide: false // disable animation
	            });
	            $(dlg).dialog('open');
	        }
	    }, {
	        key: 'closeDialog',
	        value: function closeDialog(el) {
	            // close
	            $(el).dialog('close');

	            // remove confirmation and result message
	            this.hideDeleteDlg();
	            this.hideConfirmationMsg();

	            // clear inputs
	            this.clearInputs();
	        }
	    }, {
	        key: 'closeDialogs',
	        value: function closeDialogs() {
	            $(this.dialog.el).find('div[class^=dialog]').each(function () {
	                $(this).dialog('close');
	            });
	        }

	        /********************
	         * delete-dialog bit
	         ********************/

	    }, {
	        key: 'showDeleteDlg',
	        value: function showDeleteDlg() {
	            this.dialog.renderDeleteCnfm();
	        }
	    }, {
	        key: 'hideDeleteDlg',
	        value: function hideDeleteDlg() {
	            $(this.dialog.deletecnfm.el).remove();
	        }

	        /***************************
	         * confirmation-message bit
	         ***************************/

	    }, {
	        key: 'showConfirmationMsg',
	        value: function showConfirmationMsg(of, type) {
	            var msg = '';
	            switch (type) {
	                case 'add':
	                    msg = 'New annotation added';break;
	                case 'del':
	                    msg = 'Annotation removed';break;
	            }

	            // show confirmation message
	            this.dialog.renderConfirmationMsg(of, msg);

	            // set close animation, only for confirmation message
	            $(of).dialog({
	                hide: {
	                    effect: 'fade',
	                    delay: 900,
	                    duration: 300
	                }
	            });
	            $(of).dialog('close');
	        }
	    }, {
	        key: 'hideConfirmationMsg',
	        value: function hideConfirmationMsg() {
	            $(this.dialog.confirmation.el).remove();
	        }

	        /***********
	         * ajax bit
	         ***********/

	    }, {
	        key: 'addAnnotation',
	        value: function addAnnotation(callback) {
	            var anno = this.createAnnotation();

	            if (anno == null) {
	                alert('Input cannot be empty');
	                return;
	            }

	            //
	            // ajax call to add annotation
	            //
	            this.ajax_c.addOrUpdateAnnotation(anno, callback);
	        }
	    }, {
	        key: 'removeAnnotation',
	        value: function removeAnnotation(callback) {
	            var el = $(this.dialog.info.el);
	            var uuid = $(el).data('uuid');;
	            var anno = new _modelsAnnotation2['default']({ docId: this.metadata.getItemId(), uuid: uuid });

	            //
	            // ajax call to remove annotation
	            //
	            this.ajax_c.removeAnnotation(anno, callback);
	        }

	        /**
	         *
	         */

	    }, {
	        key: 'createAnnotation',
	        value: function createAnnotation() {

	            var idx = this.toolbar_c.getActiveTbId();

	            // get content
	            var name = '';
	            switch (idx[1]) {
	                case this.TOOLBAR.SECONDARY.PERSON:
	                    name = $(this.dialog.person.input).val();break;
	                case this.TOOLBAR.SECONDARY.ABOUT:
	                    name = $(this.dialog.about.input).val();break;
	                case this.TOOLBAR.SECONDARY.DATE:
	                    name = $(this.dialog.date.input1).val() + '-' + $(this.dialog.date.input2).val();break;
	                case this.TOOLBAR.SECONDARY.PLACE:
	                    name = $(this.dialog.place.input).val();break;
	            }

	            if (name.length <= 0 || name === '-') return null;

	            // get target, e.g., doc or tag
	            var target = '';
	            switch (idx[0]) {
	                case this.TOOLBAR.PRIMARY.DOC:
	                    target = 'doc';break;
	                case this.TOOLBAR.PRIMARY.POINT:
	                    target = 'tag';break;
	                case this.TOOLBAR.PRIMARY.REGION:
	                    target = 'tag';break;
	            }

	            // get tagging type, e.g., person, about, date or place
	            var type = '';
	            switch (idx[1]) {
	                case this.TOOLBAR.SECONDARY.PERSON:
	                    type = 'person';break;
	                case this.TOOLBAR.SECONDARY.ABOUT:
	                    type = 'about';break;
	                case this.TOOLBAR.SECONDARY.DATE:
	                    type = 'date';break;
	                case this.TOOLBAR.SECONDARY.PLACE:
	                    type = 'place';break;
	            }

	            // get position
	            var position = {};
	            switch (idx[0]) {
	                case this.TOOLBAR.PRIMARY.POINT:
	                    var el = this.osd_c.guideicon,
	                        p = $(el).position(),
	                        ow = $(el).outerWidth(),
	                        oh = $(el).outerHeight();
	                    position = {
	                        type: 'point',
	                        coordinates: [this.osd_c.getGuideIconImageCoords(p.left, p.top, ow, oh)]
	                    };
	                    break;
	                case this.TOOLBAR.PRIMARY.REGION:
	                    var el = this.osd_c.boxhelper,
	                        p = $(el).position(),
	                        ow = $(el).outerWidth(),
	                        oh = $(el).outerHeight();
	                    position = {
	                        type: 'polygon',
	                        coordinates: this.osd_c.getGuideBoxHelperImageCoords(p.left, p.top, ow, oh)
	                    };
	                    break;
	            }

	            return new _modelsAnnotation2['default']({
	                docId: this.metadata.getItemId(),
	                page: cudl.pagenum, // assume the presence of cudl.pagenum
	                target: target,
	                type: type,
	                name: name,
	                position: position,
	                raw: 1
	            });
	        }
	    }, {
	        key: 'refreshMap',
	        value: function refreshMap() {

	            var idx = this.toolbar_c.getActiveTbId();

	            if (idx[1] == this.TOOLBAR.SECONDARY.PLACE) this.gmapImpl.refresh();
	        }
	    }, {
	        key: 'clearInputs',
	        value: function clearInputs() {
	            $(this.dialog.el).find('input').val('');
	        }
	    }, {
	        key: 'clearGuides',
	        value: function clearGuides() {
	            this.osd_c.clearGuides();
	        }
	    }, {
	        key: 'clearMarkers',
	        value: function clearMarkers() {
	            this.osd_c.clearMarkers();
	        }
	    }, {
	        key: 'clearMarker',
	        value: function clearMarker(id) {
	            this.osd_c.clearMarker(id);
	        }

	        /** draw new added annotation */
	    }, {
	        key: 'drawMarker',
	        value: function drawMarker(anno) {
	            var _this4 = this;

	            this.toolbar_c.fetchAnnotations(function (annos) {
	                var size = annos.length;
	                _this4.osd_c.drawMarker(size, new _modelsAnnotation2['default'](anno));
	            });
	        }
	    }]);

	    return DialogController;
	})(_commonController2['default']);

	exports['default'] = DialogController;
	module.exports = exports['default'];

/***/ },
/* 95 */
/***/ function(module, exports, __webpack_require__) {

	var jQuery = __webpack_require__(84);

	/*! jQuery UI - v1.10.3 - 2013-05-03
	* http://jqueryui.com
	* Includes: jquery.ui.core.js, jquery.ui.widget.js, jquery.ui.mouse.js, jquery.ui.draggable.js, jquery.ui.droppable.js, jquery.ui.resizable.js, jquery.ui.selectable.js, jquery.ui.sortable.js, jquery.ui.effect.js, jquery.ui.accordion.js, jquery.ui.autocomplete.js, jquery.ui.button.js, jquery.ui.datepicker.js, jquery.ui.dialog.js, jquery.ui.effect-blind.js, jquery.ui.effect-bounce.js, jquery.ui.effect-clip.js, jquery.ui.effect-drop.js, jquery.ui.effect-explode.js, jquery.ui.effect-fade.js, jquery.ui.effect-fold.js, jquery.ui.effect-highlight.js, jquery.ui.effect-pulsate.js, jquery.ui.effect-scale.js, jquery.ui.effect-shake.js, jquery.ui.effect-slide.js, jquery.ui.effect-transfer.js, jquery.ui.menu.js, jquery.ui.position.js, jquery.ui.progressbar.js, jquery.ui.slider.js, jquery.ui.spinner.js, jquery.ui.tabs.js, jquery.ui.tooltip.js
	* Copyright 2013 jQuery Foundation and other contributors; Licensed MIT */
	(function( $, undefined ) {

	var uuid = 0,
		runiqueId = /^ui-id-\d+$/;

	// $.ui might exist from components with no dependencies, e.g., $.ui.position
	$.ui = $.ui || {};

	$.extend( $.ui, {
		version: "1.10.3",

		keyCode: {
			BACKSPACE: 8,
			COMMA: 188,
			DELETE: 46,
			DOWN: 40,
			END: 35,
			ENTER: 13,
			ESCAPE: 27,
			HOME: 36,
			LEFT: 37,
			NUMPAD_ADD: 107,
			NUMPAD_DECIMAL: 110,
			NUMPAD_DIVIDE: 111,
			NUMPAD_ENTER: 108,
			NUMPAD_MULTIPLY: 106,
			NUMPAD_SUBTRACT: 109,
			PAGE_DOWN: 34,
			PAGE_UP: 33,
			PERIOD: 190,
			RIGHT: 39,
			SPACE: 32,
			TAB: 9,
			UP: 38
		}
	});

	// plugins
	$.fn.extend({
		focus: (function( orig ) {
			return function( delay, fn ) {
				return typeof delay === "number" ?
					this.each(function() {
						var elem = this;
						setTimeout(function() {
							$( elem ).focus();
							if ( fn ) {
								fn.call( elem );
							}
						}, delay );
					}) :
					orig.apply( this, arguments );
			};
		})( $.fn.focus ),

		scrollParent: function() {
			var scrollParent;
			if (($.ui.ie && (/(static|relative)/).test(this.css("position"))) || (/absolute/).test(this.css("position"))) {
				scrollParent = this.parents().filter(function() {
					return (/(relative|absolute|fixed)/).test($.css(this,"position")) && (/(auto|scroll)/).test($.css(this,"overflow")+$.css(this,"overflow-y")+$.css(this,"overflow-x"));
				}).eq(0);
			} else {
				scrollParent = this.parents().filter(function() {
					return (/(auto|scroll)/).test($.css(this,"overflow")+$.css(this,"overflow-y")+$.css(this,"overflow-x"));
				}).eq(0);
			}

			return (/fixed/).test(this.css("position")) || !scrollParent.length ? $(document) : scrollParent;
		},

		zIndex: function( zIndex ) {
			if ( zIndex !== undefined ) {
				return this.css( "zIndex", zIndex );
			}

			if ( this.length ) {
				var elem = $( this[ 0 ] ), position, value;
				while ( elem.length && elem[ 0 ] !== document ) {
					// Ignore z-index if position is set to a value where z-index is ignored by the browser
					// This makes behavior of this function consistent across browsers
					// WebKit always returns auto if the element is positioned
					position = elem.css( "position" );
					if ( position === "absolute" || position === "relative" || position === "fixed" ) {
						// IE returns 0 when zIndex is not specified
						// other browsers return a string
						// we ignore the case of nested elements with an explicit value of 0
						// <div style="z-index: -10;"><div style="z-index: 0;"></div></div>
						value = parseInt( elem.css( "zIndex" ), 10 );
						if ( !isNaN( value ) && value !== 0 ) {
							return value;
						}
					}
					elem = elem.parent();
				}
			}

			return 0;
		},

		uniqueId: function() {
			return this.each(function() {
				if ( !this.id ) {
					this.id = "ui-id-" + (++uuid);
				}
			});
		},

		removeUniqueId: function() {
			return this.each(function() {
				if ( runiqueId.test( this.id ) ) {
					$( this ).removeAttr( "id" );
				}
			});
		}
	});

	// selectors
	function focusable( element, isTabIndexNotNaN ) {
		var map, mapName, img,
			nodeName = element.nodeName.toLowerCase();
		if ( "area" === nodeName ) {
			map = element.parentNode;
			mapName = map.name;
			if ( !element.href || !mapName || map.nodeName.toLowerCase() !== "map" ) {
				return false;
			}
			img = $( "img[usemap=#" + mapName + "]" )[0];
			return !!img && visible( img );
		}
		return ( /input|select|textarea|button|object/.test( nodeName ) ?
			!element.disabled :
			"a" === nodeName ?
				element.href || isTabIndexNotNaN :
				isTabIndexNotNaN) &&
			// the element and all of its ancestors must be visible
			visible( element );
	}

	function visible( element ) {
		return $.expr.filters.visible( element ) &&
			!$( element ).parents().addBack().filter(function() {
				return $.css( this, "visibility" ) === "hidden";
			}).length;
	}

	$.extend( $.expr[ ":" ], {
		data: $.expr.createPseudo ?
			$.expr.createPseudo(function( dataName ) {
				return function( elem ) {
					return !!$.data( elem, dataName );
				};
			}) :
			// support: jQuery <1.8
			function( elem, i, match ) {
				return !!$.data( elem, match[ 3 ] );
			},

		focusable: function( element ) {
			return focusable( element, !isNaN( $.attr( element, "tabindex" ) ) );
		},

		tabbable: function( element ) {
			var tabIndex = $.attr( element, "tabindex" ),
				isTabIndexNaN = isNaN( tabIndex );
			return ( isTabIndexNaN || tabIndex >= 0 ) && focusable( element, !isTabIndexNaN );
		}
	});

	// support: jQuery <1.8
	if ( !$( "<a>" ).outerWidth( 1 ).jquery ) {
		$.each( [ "Width", "Height" ], function( i, name ) {
			var side = name === "Width" ? [ "Left", "Right" ] : [ "Top", "Bottom" ],
				type = name.toLowerCase(),
				orig = {
					innerWidth: $.fn.innerWidth,
					innerHeight: $.fn.innerHeight,
					outerWidth: $.fn.outerWidth,
					outerHeight: $.fn.outerHeight
				};

			function reduce( elem, size, border, margin ) {
				$.each( side, function() {
					size -= parseFloat( $.css( elem, "padding" + this ) ) || 0;
					if ( border ) {
						size -= parseFloat( $.css( elem, "border" + this + "Width" ) ) || 0;
					}
					if ( margin ) {
						size -= parseFloat( $.css( elem, "margin" + this ) ) || 0;
					}
				});
				return size;
			}

			$.fn[ "inner" + name ] = function( size ) {
				if ( size === undefined ) {
					return orig[ "inner" + name ].call( this );
				}

				return this.each(function() {
					$( this ).css( type, reduce( this, size ) + "px" );
				});
			};

			$.fn[ "outer" + name] = function( size, margin ) {
				if ( typeof size !== "number" ) {
					return orig[ "outer" + name ].call( this, size );
				}

				return this.each(function() {
					$( this).css( type, reduce( this, size, true, margin ) + "px" );
				});
			};
		});
	}

	// support: jQuery <1.8
	if ( !$.fn.addBack ) {
		$.fn.addBack = function( selector ) {
			return this.add( selector == null ?
				this.prevObject : this.prevObject.filter( selector )
			);
		};
	}

	// support: jQuery 1.6.1, 1.6.2 (http://bugs.jquery.com/ticket/9413)
	if ( $( "<a>" ).data( "a-b", "a" ).removeData( "a-b" ).data( "a-b" ) ) {
		$.fn.removeData = (function( removeData ) {
			return function( key ) {
				if ( arguments.length ) {
					return removeData.call( this, $.camelCase( key ) );
				} else {
					return removeData.call( this );
				}
			};
		})( $.fn.removeData );
	}





	// deprecated
	$.ui.ie = !!/msie [\w.]+/.exec( navigator.userAgent.toLowerCase() );

	$.support.selectstart = "onselectstart" in document.createElement( "div" );
	$.fn.extend({
		disableSelection: function() {
			return this.bind( ( $.support.selectstart ? "selectstart" : "mousedown" ) +
				".ui-disableSelection", function( event ) {
					event.preventDefault();
				});
		},

		enableSelection: function() {
			return this.unbind( ".ui-disableSelection" );
		}
	});

	$.extend( $.ui, {
		// $.ui.plugin is deprecated. Use $.widget() extensions instead.
		plugin: {
			add: function( module, option, set ) {
				var i,
					proto = $.ui[ module ].prototype;
				for ( i in set ) {
					proto.plugins[ i ] = proto.plugins[ i ] || [];
					proto.plugins[ i ].push( [ option, set[ i ] ] );
				}
			},
			call: function( instance, name, args ) {
				var i,
					set = instance.plugins[ name ];
				if ( !set || !instance.element[ 0 ].parentNode || instance.element[ 0 ].parentNode.nodeType === 11 ) {
					return;
				}

				for ( i = 0; i < set.length; i++ ) {
					if ( instance.options[ set[ i ][ 0 ] ] ) {
						set[ i ][ 1 ].apply( instance.element, args );
					}
				}
			}
		},

		// only used by resizable
		hasScroll: function( el, a ) {

			//If overflow is hidden, the element might have extra content, but the user wants to hide it
			if ( $( el ).css( "overflow" ) === "hidden") {
				return false;
			}

			var scroll = ( a && a === "left" ) ? "scrollLeft" : "scrollTop",
				has = false;

			if ( el[ scroll ] > 0 ) {
				return true;
			}

			// TODO: determine which cases actually cause this to happen
			// if the element doesn't have the scroll set, see if it's possible to
			// set the scroll
			el[ scroll ] = 1;
			has = ( el[ scroll ] > 0 );
			el[ scroll ] = 0;
			return has;
		}
	});

	})( jQuery );

	(function( $, undefined ) {

	var uuid = 0,
		slice = Array.prototype.slice,
		_cleanData = $.cleanData;
	$.cleanData = function( elems ) {
		for ( var i = 0, elem; (elem = elems[i]) != null; i++ ) {
			try {
				$( elem ).triggerHandler( "remove" );
			// http://bugs.jquery.com/ticket/8235
			} catch( e ) {}
		}
		_cleanData( elems );
	};

	$.widget = function( name, base, prototype ) {
		var fullName, existingConstructor, constructor, basePrototype,
			// proxiedPrototype allows the provided prototype to remain unmodified
			// so that it can be used as a mixin for multiple widgets (#8876)
			proxiedPrototype = {},
			namespace = name.split( "." )[ 0 ];

		name = name.split( "." )[ 1 ];
		fullName = namespace + "-" + name;

		if ( !prototype ) {
			prototype = base;
			base = $.Widget;
		}

		// create selector for plugin
		$.expr[ ":" ][ fullName.toLowerCase() ] = function( elem ) {
			return !!$.data( elem, fullName );
		};

		$[ namespace ] = $[ namespace ] || {};
		existingConstructor = $[ namespace ][ name ];
		constructor = $[ namespace ][ name ] = function( options, element ) {
			// allow instantiation without "new" keyword
			if ( !this._createWidget ) {
				return new constructor( options, element );
			}

			// allow instantiation without initializing for simple inheritance
			// must use "new" keyword (the code above always passes args)
			if ( arguments.length ) {
				this._createWidget( options, element );
			}
		};
		// extend with the existing constructor to carry over any static properties
		$.extend( constructor, existingConstructor, {
			version: prototype.version,
			// copy the object used to create the prototype in case we need to
			// redefine the widget later
			_proto: $.extend( {}, prototype ),
			// track widgets that inherit from this widget in case this widget is
			// redefined after a widget inherits from it
			_childConstructors: []
		});

		basePrototype = new base();
		// we need to make the options hash a property directly on the new instance
		// otherwise we'll modify the options hash on the prototype that we're
		// inheriting from
		basePrototype.options = $.widget.extend( {}, basePrototype.options );
		$.each( prototype, function( prop, value ) {
			if ( !$.isFunction( value ) ) {
				proxiedPrototype[ prop ] = value;
				return;
			}
			proxiedPrototype[ prop ] = (function() {
				var _super = function() {
						return base.prototype[ prop ].apply( this, arguments );
					},
					_superApply = function( args ) {
						return base.prototype[ prop ].apply( this, args );
					};
				return function() {
					var __super = this._super,
						__superApply = this._superApply,
						returnValue;

					this._super = _super;
					this._superApply = _superApply;

					returnValue = value.apply( this, arguments );

					this._super = __super;
					this._superApply = __superApply;

					return returnValue;
				};
			})();
		});
		constructor.prototype = $.widget.extend( basePrototype, {
			// TODO: remove support for widgetEventPrefix
			// always use the name + a colon as the prefix, e.g., draggable:start
			// don't prefix for widgets that aren't DOM-based
			widgetEventPrefix: existingConstructor ? basePrototype.widgetEventPrefix : name
		}, proxiedPrototype, {
			constructor: constructor,
			namespace: namespace,
			widgetName: name,
			widgetFullName: fullName
		});

		// If this widget is being redefined then we need to find all widgets that
		// are inheriting from it and redefine all of them so that they inherit from
		// the new version of this widget. We're essentially trying to replace one
		// level in the prototype chain.
		if ( existingConstructor ) {
			$.each( existingConstructor._childConstructors, function( i, child ) {
				var childPrototype = child.prototype;

				// redefine the child widget using the same prototype that was
				// originally used, but inherit from the new version of the base
				$.widget( childPrototype.namespace + "." + childPrototype.widgetName, constructor, child._proto );
			});
			// remove the list of existing child constructors from the old constructor
			// so the old child constructors can be garbage collected
			delete existingConstructor._childConstructors;
		} else {
			base._childConstructors.push( constructor );
		}

		$.widget.bridge( name, constructor );
	};

	$.widget.extend = function( target ) {
		var input = slice.call( arguments, 1 ),
			inputIndex = 0,
			inputLength = input.length,
			key,
			value;
		for ( ; inputIndex < inputLength; inputIndex++ ) {
			for ( key in input[ inputIndex ] ) {
				value = input[ inputIndex ][ key ];
				if ( input[ inputIndex ].hasOwnProperty( key ) && value !== undefined ) {
					// Clone objects
					if ( $.isPlainObject( value ) ) {
						target[ key ] = $.isPlainObject( target[ key ] ) ?
							$.widget.extend( {}, target[ key ], value ) :
							// Don't extend strings, arrays, etc. with objects
							$.widget.extend( {}, value );
					// Copy everything else by reference
					} else {
						target[ key ] = value;
					}
				}
			}
		}
		return target;
	};

	$.widget.bridge = function( name, object ) {
		var fullName = object.prototype.widgetFullName || name;
		$.fn[ name ] = function( options ) {
			var isMethodCall = typeof options === "string",
				args = slice.call( arguments, 1 ),
				returnValue = this;

			// allow multiple hashes to be passed on init
			options = !isMethodCall && args.length ?
				$.widget.extend.apply( null, [ options ].concat(args) ) :
				options;

			if ( isMethodCall ) {
				this.each(function() {
					var methodValue,
						instance = $.data( this, fullName );
					if ( !instance ) {
						return $.error( "cannot call methods on " + name + " prior to initialization; " +
							"attempted to call method '" + options + "'" );
					}
					if ( !$.isFunction( instance[options] ) || options.charAt( 0 ) === "_" ) {
						return $.error( "no such method '" + options + "' for " + name + " widget instance" );
					}
					methodValue = instance[ options ].apply( instance, args );
					if ( methodValue !== instance && methodValue !== undefined ) {
						returnValue = methodValue && methodValue.jquery ?
							returnValue.pushStack( methodValue.get() ) :
							methodValue;
						return false;
					}
				});
			} else {
				this.each(function() {
					var instance = $.data( this, fullName );
					if ( instance ) {
						instance.option( options || {} )._init();
					} else {
						$.data( this, fullName, new object( options, this ) );
					}
				});
			}

			return returnValue;
		};
	};

	$.Widget = function( /* options, element */ ) {};
	$.Widget._childConstructors = [];

	$.Widget.prototype = {
		widgetName: "widget",
		widgetEventPrefix: "",
		defaultElement: "<div>",
		options: {
			disabled: false,

			// callbacks
			create: null
		},
		_createWidget: function( options, element ) {
			element = $( element || this.defaultElement || this )[ 0 ];
			this.element = $( element );
			this.uuid = uuid++;
			this.eventNamespace = "." + this.widgetName + this.uuid;
			this.options = $.widget.extend( {},
				this.options,
				this._getCreateOptions(),
				options );

			this.bindings = $();
			this.hoverable = $();
			this.focusable = $();

			if ( element !== this ) {
				$.data( element, this.widgetFullName, this );
				this._on( true, this.element, {
					remove: function( event ) {
						if ( event.target === element ) {
							this.destroy();
						}
					}
				});
				this.document = $( element.style ?
					// element within the document
					element.ownerDocument :
					// element is window or document
					element.document || element );
				this.window = $( this.document[0].defaultView || this.document[0].parentWindow );
			}

			this._create();
			this._trigger( "create", null, this._getCreateEventData() );
			this._init();
		},
		_getCreateOptions: $.noop,
		_getCreateEventData: $.noop,
		_create: $.noop,
		_init: $.noop,

		destroy: function() {
			this._destroy();
			// we can probably remove the unbind calls in 2.0
			// all event bindings should go through this._on()
			this.element
				.unbind( this.eventNamespace )
				// 1.9 BC for #7810
				// TODO remove dual storage
				.removeData( this.widgetName )
				.removeData( this.widgetFullName )
				// support: jquery <1.6.3
				// http://bugs.jquery.com/ticket/9413
				.removeData( $.camelCase( this.widgetFullName ) );
			this.widget()
				.unbind( this.eventNamespace )
				.removeAttr( "aria-disabled" )
				.removeClass(
					this.widgetFullName + "-disabled " +
					"ui-state-disabled" );

			// clean up events and states
			this.bindings.unbind( this.eventNamespace );
			this.hoverable.removeClass( "ui-state-hover" );
			this.focusable.removeClass( "ui-state-focus" );
		},
		_destroy: $.noop,

		widget: function() {
			return this.element;
		},

		option: function( key, value ) {
			var options = key,
				parts,
				curOption,
				i;

			if ( arguments.length === 0 ) {
				// don't return a reference to the internal hash
				return $.widget.extend( {}, this.options );
			}

			if ( typeof key === "string" ) {
				// handle nested keys, e.g., "foo.bar" => { foo: { bar: ___ } }
				options = {};
				parts = key.split( "." );
				key = parts.shift();
				if ( parts.length ) {
					curOption = options[ key ] = $.widget.extend( {}, this.options[ key ] );
					for ( i = 0; i < parts.length - 1; i++ ) {
						curOption[ parts[ i ] ] = curOption[ parts[ i ] ] || {};
						curOption = curOption[ parts[ i ] ];
					}
					key = parts.pop();
					if ( value === undefined ) {
						return curOption[ key ] === undefined ? null : curOption[ key ];
					}
					curOption[ key ] = value;
				} else {
					if ( value === undefined ) {
						return this.options[ key ] === undefined ? null : this.options[ key ];
					}
					options[ key ] = value;
				}
			}

			this._setOptions( options );

			return this;
		},
		_setOptions: function( options ) {
			var key;

			for ( key in options ) {
				this._setOption( key, options[ key ] );
			}

			return this;
		},
		_setOption: function( key, value ) {
			this.options[ key ] = value;

			if ( key === "disabled" ) {
				this.widget()
					.toggleClass( this.widgetFullName + "-disabled ui-state-disabled", !!value )
					.attr( "aria-disabled", value );
				this.hoverable.removeClass( "ui-state-hover" );
				this.focusable.removeClass( "ui-state-focus" );
			}

			return this;
		},

		enable: function() {
			return this._setOption( "disabled", false );
		},
		disable: function() {
			return this._setOption( "disabled", true );
		},

		_on: function( suppressDisabledCheck, element, handlers ) {
			var delegateElement,
				instance = this;

			// no suppressDisabledCheck flag, shuffle arguments
			if ( typeof suppressDisabledCheck !== "boolean" ) {
				handlers = element;
				element = suppressDisabledCheck;
				suppressDisabledCheck = false;
			}

			// no element argument, shuffle and use this.element
			if ( !handlers ) {
				handlers = element;
				element = this.element;
				delegateElement = this.widget();
			} else {
				// accept selectors, DOM elements
				element = delegateElement = $( element );
				this.bindings = this.bindings.add( element );
			}

			$.each( handlers, function( event, handler ) {
				function handlerProxy() {
					// allow widgets to customize the disabled handling
					// - disabled as an array instead of boolean
					// - disabled class as method for disabling individual parts
					if ( !suppressDisabledCheck &&
							( instance.options.disabled === true ||
								$( this ).hasClass( "ui-state-disabled" ) ) ) {
						return;
					}
					return ( typeof handler === "string" ? instance[ handler ] : handler )
						.apply( instance, arguments );
				}

				// copy the guid so direct unbinding works
				if ( typeof handler !== "string" ) {
					handlerProxy.guid = handler.guid =
						handler.guid || handlerProxy.guid || $.guid++;
				}

				var match = event.match( /^(\w+)\s*(.*)$/ ),
					eventName = match[1] + instance.eventNamespace,
					selector = match[2];
				if ( selector ) {
					delegateElement.delegate( selector, eventName, handlerProxy );
				} else {
					element.bind( eventName, handlerProxy );
				}
			});
		},

		_off: function( element, eventName ) {
			eventName = (eventName || "").split( " " ).join( this.eventNamespace + " " ) + this.eventNamespace;
			element.unbind( eventName ).undelegate( eventName );
		},

		_delay: function( handler, delay ) {
			function handlerProxy() {
				return ( typeof handler === "string" ? instance[ handler ] : handler )
					.apply( instance, arguments );
			}
			var instance = this;
			return setTimeout( handlerProxy, delay || 0 );
		},

		_hoverable: function( element ) {
			this.hoverable = this.hoverable.add( element );
			this._on( element, {
				mouseenter: function( event ) {
					$( event.currentTarget ).addClass( "ui-state-hover" );
				},
				mouseleave: function( event ) {
					$( event.currentTarget ).removeClass( "ui-state-hover" );
				}
			});
		},

		_focusable: function( element ) {
			this.focusable = this.focusable.add( element );
			this._on( element, {
				focusin: function( event ) {
					$( event.currentTarget ).addClass( "ui-state-focus" );
				},
				focusout: function( event ) {
					$( event.currentTarget ).removeClass( "ui-state-focus" );
				}
			});
		},

		_trigger: function( type, event, data ) {
			var prop, orig,
				callback = this.options[ type ];

			data = data || {};
			event = $.Event( event );
			event.type = ( type === this.widgetEventPrefix ?
				type :
				this.widgetEventPrefix + type ).toLowerCase();
			// the original event may come from any element
			// so we need to reset the target on the new event
			event.target = this.element[ 0 ];

			// copy original event properties over to the new event
			orig = event.originalEvent;
			if ( orig ) {
				for ( prop in orig ) {
					if ( !( prop in event ) ) {
						event[ prop ] = orig[ prop ];
					}
				}
			}

			this.element.trigger( event, data );
			return !( $.isFunction( callback ) &&
				callback.apply( this.element[0], [ event ].concat( data ) ) === false ||
				event.isDefaultPrevented() );
		}
	};

	$.each( { show: "fadeIn", hide: "fadeOut" }, function( method, defaultEffect ) {
		$.Widget.prototype[ "_" + method ] = function( element, options, callback ) {
			if ( typeof options === "string" ) {
				options = { effect: options };
			}
			var hasOptions,
				effectName = !options ?
					method :
					options === true || typeof options === "number" ?
						defaultEffect :
						options.effect || defaultEffect;
			options = options || {};
			if ( typeof options === "number" ) {
				options = { duration: options };
			}
			hasOptions = !$.isEmptyObject( options );
			options.complete = callback;
			if ( options.delay ) {
				element.delay( options.delay );
			}
			if ( hasOptions && $.effects && $.effects.effect[ effectName ] ) {
				element[ method ]( options );
			} else if ( effectName !== method && element[ effectName ] ) {
				element[ effectName ]( options.duration, options.easing, callback );
			} else {
				element.queue(function( next ) {
					$( this )[ method ]();
					if ( callback ) {
						callback.call( element[ 0 ] );
					}
					next();
				});
			}
		};
	});

	})( jQuery );

	(function( $, undefined ) {

	var mouseHandled = false;
	$( document ).mouseup( function() {
		mouseHandled = false;
	});

	$.widget("ui.mouse", {
		version: "1.10.3",
		options: {
			cancel: "input,textarea,button,select,option",
			distance: 1,
			delay: 0
		},
		_mouseInit: function() {
			var that = this;

			this.element
				.bind("mousedown."+this.widgetName, function(event) {
					return that._mouseDown(event);
				})
				.bind("click."+this.widgetName, function(event) {
					if (true === $.data(event.target, that.widgetName + ".preventClickEvent")) {
						$.removeData(event.target, that.widgetName + ".preventClickEvent");
						event.stopImmediatePropagation();
						return false;
					}
				});

			this.started = false;
		},

		// TODO: make sure destroying one instance of mouse doesn't mess with
		// other instances of mouse
		_mouseDestroy: function() {
			this.element.unbind("."+this.widgetName);
			if ( this._mouseMoveDelegate ) {
				$(document)
					.unbind("mousemove."+this.widgetName, this._mouseMoveDelegate)
					.unbind("mouseup."+this.widgetName, this._mouseUpDelegate);
			}
		},

		_mouseDown: function(event) {
			// don't let more than one widget handle mouseStart
			if( mouseHandled ) { return; }

			// we may have missed mouseup (out of window)
			(this._mouseStarted && this._mouseUp(event));

			this._mouseDownEvent = event;

			var that = this,
				btnIsLeft = (event.which === 1),
				// event.target.nodeName works around a bug in IE 8 with
				// disabled inputs (#7620)
				elIsCancel = (typeof this.options.cancel === "string" && event.target.nodeName ? $(event.target).closest(this.options.cancel).length : false);
			if (!btnIsLeft || elIsCancel || !this._mouseCapture(event)) {
				return true;
			}

			this.mouseDelayMet = !this.options.delay;
			if (!this.mouseDelayMet) {
				this._mouseDelayTimer = setTimeout(function() {
					that.mouseDelayMet = true;
				}, this.options.delay);
			}

			if (this._mouseDistanceMet(event) && this._mouseDelayMet(event)) {
				this._mouseStarted = (this._mouseStart(event) !== false);
				if (!this._mouseStarted) {
					event.preventDefault();
					return true;
				}
			}

			// Click event may never have fired (Gecko & Opera)
			if (true === $.data(event.target, this.widgetName + ".preventClickEvent")) {
				$.removeData(event.target, this.widgetName + ".preventClickEvent");
			}

			// these delegates are required to keep context
			this._mouseMoveDelegate = function(event) {
				return that._mouseMove(event);
			};
			this._mouseUpDelegate = function(event) {
				return that._mouseUp(event);
			};
			$(document)
				.bind("mousemove."+this.widgetName, this._mouseMoveDelegate)
				.bind("mouseup."+this.widgetName, this._mouseUpDelegate);

			event.preventDefault();

			mouseHandled = true;
			return true;
		},

		_mouseMove: function(event) {
			// IE mouseup check - mouseup happened when mouse was out of window
			if ($.ui.ie && ( !document.documentMode || document.documentMode < 9 ) && !event.button) {
				return this._mouseUp(event);
			}

			if (this._mouseStarted) {
				this._mouseDrag(event);
				return event.preventDefault();
			}

			if (this._mouseDistanceMet(event) && this._mouseDelayMet(event)) {
				this._mouseStarted =
					(this._mouseStart(this._mouseDownEvent, event) !== false);
				(this._mouseStarted ? this._mouseDrag(event) : this._mouseUp(event));
			}

			return !this._mouseStarted;
		},

		_mouseUp: function(event) {
			$(document)
				.unbind("mousemove."+this.widgetName, this._mouseMoveDelegate)
				.unbind("mouseup."+this.widgetName, this._mouseUpDelegate);

			if (this._mouseStarted) {
				this._mouseStarted = false;

				if (event.target === this._mouseDownEvent.target) {
					$.data(event.target, this.widgetName + ".preventClickEvent", true);
				}

				this._mouseStop(event);
			}

			return false;
		},

		_mouseDistanceMet: function(event) {
			return (Math.max(
					Math.abs(this._mouseDownEvent.pageX - event.pageX),
					Math.abs(this._mouseDownEvent.pageY - event.pageY)
				) >= this.options.distance
			);
		},

		_mouseDelayMet: function(/* event */) {
			return this.mouseDelayMet;
		},

		// These are placeholder methods, to be overriden by extending plugin
		_mouseStart: function(/* event */) {},
		_mouseDrag: function(/* event */) {},
		_mouseStop: function(/* event */) {},
		_mouseCapture: function(/* event */) { return true; }
	});

	})(jQuery);

	(function( $, undefined ) {

	$.widget("ui.draggable", $.ui.mouse, {
		version: "1.10.3",
		widgetEventPrefix: "drag",
		options: {
			addClasses: true,
			appendTo: "parent",
			axis: false,
			connectToSortable: false,
			containment: false,
			cursor: "auto",
			cursorAt: false,
			grid: false,
			handle: false,
			helper: "original",
			iframeFix: false,
			opacity: false,
			refreshPositions: false,
			revert: false,
			revertDuration: 500,
			scope: "default",
			scroll: true,
			scrollSensitivity: 20,
			scrollSpeed: 20,
			snap: false,
			snapMode: "both",
			snapTolerance: 20,
			stack: false,
			zIndex: false,

			// callbacks
			drag: null,
			start: null,
			stop: null
		},
		_create: function() {

			if (this.options.helper === "original" && !(/^(?:r|a|f)/).test(this.element.css("position"))) {
				this.element[0].style.position = "relative";
			}
			if (this.options.addClasses){
				this.element.addClass("ui-draggable");
			}
			if (this.options.disabled){
				this.element.addClass("ui-draggable-disabled");
			}

			this._mouseInit();

		},

		_destroy: function() {
			this.element.removeClass( "ui-draggable ui-draggable-dragging ui-draggable-disabled" );
			this._mouseDestroy();
		},

		_mouseCapture: function(event) {

			var o = this.options;

			// among others, prevent a drag on a resizable-handle
			if (this.helper || o.disabled || $(event.target).closest(".ui-resizable-handle").length > 0) {
				return false;
			}

			//Quit if we're not on a valid handle
			this.handle = this._getHandle(event);
			if (!this.handle) {
				return false;
			}

			$(o.iframeFix === true ? "iframe" : o.iframeFix).each(function() {
				$("<div class='ui-draggable-iframeFix' style='background: #fff;'></div>")
				.css({
					width: this.offsetWidth+"px", height: this.offsetHeight+"px",
					position: "absolute", opacity: "0.001", zIndex: 1000
				})
				.css($(this).offset())
				.appendTo("body");
			});

			return true;

		},

		_mouseStart: function(event) {

			var o = this.options;

			//Create and append the visible helper
			this.helper = this._createHelper(event);

			this.helper.addClass("ui-draggable-dragging");

			//Cache the helper size
			this._cacheHelperProportions();

			//If ddmanager is used for droppables, set the global draggable
			if($.ui.ddmanager) {
				$.ui.ddmanager.current = this;
			}

			/*
			 * - Position generation -
			 * This block generates everything position related - it's the core of draggables.
			 */

			//Cache the margins of the original element
			this._cacheMargins();

			//Store the helper's css position
			this.cssPosition = this.helper.css( "position" );
			this.scrollParent = this.helper.scrollParent();
			this.offsetParent = this.helper.offsetParent();
			this.offsetParentCssPosition = this.offsetParent.css( "position" );

			//The element's absolute position on the page minus margins
			this.offset = this.positionAbs = this.element.offset();
			this.offset = {
				top: this.offset.top - this.margins.top,
				left: this.offset.left - this.margins.left
			};

			//Reset scroll cache
			this.offset.scroll = false;

			$.extend(this.offset, {
				click: { //Where the click happened, relative to the element
					left: event.pageX - this.offset.left,
					top: event.pageY - this.offset.top
				},
				parent: this._getParentOffset(),
				relative: this._getRelativeOffset() //This is a relative to absolute position minus the actual position calculation - only used for relative positioned helper
			});

			//Generate the original position
			this.originalPosition = this.position = this._generatePosition(event);
			this.originalPageX = event.pageX;
			this.originalPageY = event.pageY;

			//Adjust the mouse offset relative to the helper if "cursorAt" is supplied
			(o.cursorAt && this._adjustOffsetFromHelper(o.cursorAt));

			//Set a containment if given in the options
			this._setContainment();

			//Trigger event + callbacks
			if(this._trigger("start", event) === false) {
				this._clear();
				return false;
			}

			//Recache the helper size
			this._cacheHelperProportions();

			//Prepare the droppable offsets
			if ($.ui.ddmanager && !o.dropBehaviour) {
				$.ui.ddmanager.prepareOffsets(this, event);
			}


			this._mouseDrag(event, true); //Execute the drag once - this causes the helper not to be visible before getting its correct position

			//If the ddmanager is used for droppables, inform the manager that dragging has started (see #5003)
			if ( $.ui.ddmanager ) {
				$.ui.ddmanager.dragStart(this, event);
			}

			return true;
		},

		_mouseDrag: function(event, noPropagation) {
			// reset any necessary cached properties (see #5009)
			if ( this.offsetParentCssPosition === "fixed" ) {
				this.offset.parent = this._getParentOffset();
			}

			//Compute the helpers position
			this.position = this._generatePosition(event);
			this.positionAbs = this._convertPositionTo("absolute");

			//Call plugins and callbacks and use the resulting position if something is returned
			if (!noPropagation) {
				var ui = this._uiHash();
				if(this._trigger("drag", event, ui) === false) {
					this._mouseUp({});
					return false;
				}
				this.position = ui.position;
			}

			if(!this.options.axis || this.options.axis !== "y") {
				this.helper[0].style.left = this.position.left+"px";
			}
			if(!this.options.axis || this.options.axis !== "x") {
				this.helper[0].style.top = this.position.top+"px";
			}
			if($.ui.ddmanager) {
				$.ui.ddmanager.drag(this, event);
			}

			return false;
		},

		_mouseStop: function(event) {

			//If we are using droppables, inform the manager about the drop
			var that = this,
				dropped = false;
			if ($.ui.ddmanager && !this.options.dropBehaviour) {
				dropped = $.ui.ddmanager.drop(this, event);
			}

			//if a drop comes from outside (a sortable)
			if(this.dropped) {
				dropped = this.dropped;
				this.dropped = false;
			}

			//if the original element is no longer in the DOM don't bother to continue (see #8269)
			if ( this.options.helper === "original" && !$.contains( this.element[ 0 ].ownerDocument, this.element[ 0 ] ) ) {
				return false;
			}

			if((this.options.revert === "invalid" && !dropped) || (this.options.revert === "valid" && dropped) || this.options.revert === true || ($.isFunction(this.options.revert) && this.options.revert.call(this.element, dropped))) {
				$(this.helper).animate(this.originalPosition, parseInt(this.options.revertDuration, 10), function() {
					if(that._trigger("stop", event) !== false) {
						that._clear();
					}
				});
			} else {
				if(this._trigger("stop", event) !== false) {
					this._clear();
				}
			}

			return false;
		},

		_mouseUp: function(event) {
			//Remove frame helpers
			$("div.ui-draggable-iframeFix").each(function() {
				this.parentNode.removeChild(this);
			});

			//If the ddmanager is used for droppables, inform the manager that dragging has stopped (see #5003)
			if( $.ui.ddmanager ) {
				$.ui.ddmanager.dragStop(this, event);
			}

			return $.ui.mouse.prototype._mouseUp.call(this, event);
		},

		cancel: function() {

			if(this.helper.is(".ui-draggable-dragging")) {
				this._mouseUp({});
			} else {
				this._clear();
			}

			return this;

		},

		_getHandle: function(event) {
			return this.options.handle ?
				!!$( event.target ).closest( this.element.find( this.options.handle ) ).length :
				true;
		},

		_createHelper: function(event) {

			var o = this.options,
				helper = $.isFunction(o.helper) ? $(o.helper.apply(this.element[0], [event])) : (o.helper === "clone" ? this.element.clone().removeAttr("id") : this.element);

			if(!helper.parents("body").length) {
				helper.appendTo((o.appendTo === "parent" ? this.element[0].parentNode : o.appendTo));
			}

			if(helper[0] !== this.element[0] && !(/(fixed|absolute)/).test(helper.css("position"))) {
				helper.css("position", "absolute");
			}

			return helper;

		},

		_adjustOffsetFromHelper: function(obj) {
			if (typeof obj === "string") {
				obj = obj.split(" ");
			}
			if ($.isArray(obj)) {
				obj = {left: +obj[0], top: +obj[1] || 0};
			}
			if ("left" in obj) {
				this.offset.click.left = obj.left + this.margins.left;
			}
			if ("right" in obj) {
				this.offset.click.left = this.helperProportions.width - obj.right + this.margins.left;
			}
			if ("top" in obj) {
				this.offset.click.top = obj.top + this.margins.top;
			}
			if ("bottom" in obj) {
				this.offset.click.top = this.helperProportions.height - obj.bottom + this.margins.top;
			}
		},

		_getParentOffset: function() {

			//Get the offsetParent and cache its position
			var po = this.offsetParent.offset();

			// This is a special case where we need to modify a offset calculated on start, since the following happened:
			// 1. The position of the helper is absolute, so it's position is calculated based on the next positioned parent
			// 2. The actual offset parent is a child of the scroll parent, and the scroll parent isn't the document, which means that
			//    the scroll is included in the initial calculation of the offset of the parent, and never recalculated upon drag
			if(this.cssPosition === "absolute" && this.scrollParent[0] !== document && $.contains(this.scrollParent[0], this.offsetParent[0])) {
				po.left += this.scrollParent.scrollLeft();
				po.top += this.scrollParent.scrollTop();
			}

			//This needs to be actually done for all browsers, since pageX/pageY includes this information
			//Ugly IE fix
			if((this.offsetParent[0] === document.body) ||
				(this.offsetParent[0].tagName && this.offsetParent[0].tagName.toLowerCase() === "html" && $.ui.ie)) {
				po = { top: 0, left: 0 };
			}

			return {
				top: po.top + (parseInt(this.offsetParent.css("borderTopWidth"),10) || 0),
				left: po.left + (parseInt(this.offsetParent.css("borderLeftWidth"),10) || 0)
			};

		},

		_getRelativeOffset: function() {

			if(this.cssPosition === "relative") {
				var p = this.element.position();
				return {
					top: p.top - (parseInt(this.helper.css("top"),10) || 0) + this.scrollParent.scrollTop(),
					left: p.left - (parseInt(this.helper.css("left"),10) || 0) + this.scrollParent.scrollLeft()
				};
			} else {
				return { top: 0, left: 0 };
			}

		},

		_cacheMargins: function() {
			this.margins = {
				left: (parseInt(this.element.css("marginLeft"),10) || 0),
				top: (parseInt(this.element.css("marginTop"),10) || 0),
				right: (parseInt(this.element.css("marginRight"),10) || 0),
				bottom: (parseInt(this.element.css("marginBottom"),10) || 0)
			};
		},

		_cacheHelperProportions: function() {
			this.helperProportions = {
				width: this.helper.outerWidth(),
				height: this.helper.outerHeight()
			};
		},

		_setContainment: function() {

			var over, c, ce,
				o = this.options;

			if ( !o.containment ) {
				this.containment = null;
				return;
			}

			if ( o.containment === "window" ) {
				this.containment = [
					$( window ).scrollLeft() - this.offset.relative.left - this.offset.parent.left,
					$( window ).scrollTop() - this.offset.relative.top - this.offset.parent.top,
					$( window ).scrollLeft() + $( window ).width() - this.helperProportions.width - this.margins.left,
					$( window ).scrollTop() + ( $( window ).height() || document.body.parentNode.scrollHeight ) - this.helperProportions.height - this.margins.top
				];
				return;
			}

			if ( o.containment === "document") {
				this.containment = [
					0,
					0,
					$( document ).width() - this.helperProportions.width - this.margins.left,
					( $( document ).height() || document.body.parentNode.scrollHeight ) - this.helperProportions.height - this.margins.top
				];
				return;
			}

			if ( o.containment.constructor === Array ) {
				this.containment = o.containment;
				return;
			}

			if ( o.containment === "parent" ) {
				o.containment = this.helper[ 0 ].parentNode;
			}

			c = $( o.containment );
			ce = c[ 0 ];

			if( !ce ) {
				return;
			}

			over = c.css( "overflow" ) !== "hidden";

			this.containment = [
				( parseInt( c.css( "borderLeftWidth" ), 10 ) || 0 ) + ( parseInt( c.css( "paddingLeft" ), 10 ) || 0 ),
				( parseInt( c.css( "borderTopWidth" ), 10 ) || 0 ) + ( parseInt( c.css( "paddingTop" ), 10 ) || 0 ) ,
				( over ? Math.max( ce.scrollWidth, ce.offsetWidth ) : ce.offsetWidth ) - ( parseInt( c.css( "borderRightWidth" ), 10 ) || 0 ) - ( parseInt( c.css( "paddingRight" ), 10 ) || 0 ) - this.helperProportions.width - this.margins.left - this.margins.right,
				( over ? Math.max( ce.scrollHeight, ce.offsetHeight ) : ce.offsetHeight ) - ( parseInt( c.css( "borderBottomWidth" ), 10 ) || 0 ) - ( parseInt( c.css( "paddingBottom" ), 10 ) || 0 ) - this.helperProportions.height - this.margins.top  - this.margins.bottom
			];
			this.relative_container = c;
		},

		_convertPositionTo: function(d, pos) {

			if(!pos) {
				pos = this.position;
			}

			var mod = d === "absolute" ? 1 : -1,
				scroll = this.cssPosition === "absolute" && !( this.scrollParent[ 0 ] !== document && $.contains( this.scrollParent[ 0 ], this.offsetParent[ 0 ] ) ) ? this.offsetParent : this.scrollParent;

			//Cache the scroll
			if (!this.offset.scroll) {
				this.offset.scroll = {top : scroll.scrollTop(), left : scroll.scrollLeft()};
			}

			return {
				top: (
					pos.top	+																// The absolute mouse position
					this.offset.relative.top * mod +										// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.top * mod -										// The offsetParent's offset without borders (offset + border)
					( ( this.cssPosition === "fixed" ? -this.scrollParent.scrollTop() : this.offset.scroll.top ) * mod )
				),
				left: (
					pos.left +																// The absolute mouse position
					this.offset.relative.left * mod +										// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.left * mod	-										// The offsetParent's offset without borders (offset + border)
					( ( this.cssPosition === "fixed" ? -this.scrollParent.scrollLeft() : this.offset.scroll.left ) * mod )
				)
			};

		},

		_generatePosition: function(event) {

			var containment, co, top, left,
				o = this.options,
				scroll = this.cssPosition === "absolute" && !( this.scrollParent[ 0 ] !== document && $.contains( this.scrollParent[ 0 ], this.offsetParent[ 0 ] ) ) ? this.offsetParent : this.scrollParent,
				pageX = event.pageX,
				pageY = event.pageY;

			//Cache the scroll
			if (!this.offset.scroll) {
				this.offset.scroll = {top : scroll.scrollTop(), left : scroll.scrollLeft()};
			}

			/*
			 * - Position constraining -
			 * Constrain the position to a mix of grid, containment.
			 */

			// If we are not dragging yet, we won't check for options
			if ( this.originalPosition ) {
				if ( this.containment ) {
					if ( this.relative_container ){
						co = this.relative_container.offset();
						containment = [
							this.containment[ 0 ] + co.left,
							this.containment[ 1 ] + co.top,
							this.containment[ 2 ] + co.left,
							this.containment[ 3 ] + co.top
						];
					}
					else {
						containment = this.containment;
					}

					if(event.pageX - this.offset.click.left < containment[0]) {
						pageX = containment[0] + this.offset.click.left;
					}
					if(event.pageY - this.offset.click.top < containment[1]) {
						pageY = containment[1] + this.offset.click.top;
					}
					if(event.pageX - this.offset.click.left > containment[2]) {
						pageX = containment[2] + this.offset.click.left;
					}
					if(event.pageY - this.offset.click.top > containment[3]) {
						pageY = containment[3] + this.offset.click.top;
					}
				}

				if(o.grid) {
					//Check for grid elements set to 0 to prevent divide by 0 error causing invalid argument errors in IE (see ticket #6950)
					top = o.grid[1] ? this.originalPageY + Math.round((pageY - this.originalPageY) / o.grid[1]) * o.grid[1] : this.originalPageY;
					pageY = containment ? ((top - this.offset.click.top >= containment[1] || top - this.offset.click.top > containment[3]) ? top : ((top - this.offset.click.top >= containment[1]) ? top - o.grid[1] : top + o.grid[1])) : top;

					left = o.grid[0] ? this.originalPageX + Math.round((pageX - this.originalPageX) / o.grid[0]) * o.grid[0] : this.originalPageX;
					pageX = containment ? ((left - this.offset.click.left >= containment[0] || left - this.offset.click.left > containment[2]) ? left : ((left - this.offset.click.left >= containment[0]) ? left - o.grid[0] : left + o.grid[0])) : left;
				}

			}

			return {
				top: (
					pageY -																	// The absolute mouse position
					this.offset.click.top	-												// Click offset (relative to the element)
					this.offset.relative.top -												// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.top +												// The offsetParent's offset without borders (offset + border)
					( this.cssPosition === "fixed" ? -this.scrollParent.scrollTop() : this.offset.scroll.top )
				),
				left: (
					pageX -																	// The absolute mouse position
					this.offset.click.left -												// Click offset (relative to the element)
					this.offset.relative.left -												// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.left +												// The offsetParent's offset without borders (offset + border)
					( this.cssPosition === "fixed" ? -this.scrollParent.scrollLeft() : this.offset.scroll.left )
				)
			};

		},

		_clear: function() {
			this.helper.removeClass("ui-draggable-dragging");
			if(this.helper[0] !== this.element[0] && !this.cancelHelperRemoval) {
				this.helper.remove();
			}
			this.helper = null;
			this.cancelHelperRemoval = false;
		},

		// From now on bulk stuff - mainly helpers

		_trigger: function(type, event, ui) {
			ui = ui || this._uiHash();
			$.ui.plugin.call(this, type, [event, ui]);
			//The absolute position has to be recalculated after plugins
			if(type === "drag") {
				this.positionAbs = this._convertPositionTo("absolute");
			}
			return $.Widget.prototype._trigger.call(this, type, event, ui);
		},

		plugins: {},

		_uiHash: function() {
			return {
				helper: this.helper,
				position: this.position,
				originalPosition: this.originalPosition,
				offset: this.positionAbs
			};
		}

	});

	$.ui.plugin.add("draggable", "connectToSortable", {
		start: function(event, ui) {

			var inst = $(this).data("ui-draggable"), o = inst.options,
				uiSortable = $.extend({}, ui, { item: inst.element });
			inst.sortables = [];
			$(o.connectToSortable).each(function() {
				var sortable = $.data(this, "ui-sortable");
				if (sortable && !sortable.options.disabled) {
					inst.sortables.push({
						instance: sortable,
						shouldRevert: sortable.options.revert
					});
					sortable.refreshPositions();	// Call the sortable's refreshPositions at drag start to refresh the containerCache since the sortable container cache is used in drag and needs to be up to date (this will ensure it's initialised as well as being kept in step with any changes that might have happened on the page).
					sortable._trigger("activate", event, uiSortable);
				}
			});

		},
		stop: function(event, ui) {

			//If we are still over the sortable, we fake the stop event of the sortable, but also remove helper
			var inst = $(this).data("ui-draggable"),
				uiSortable = $.extend({}, ui, { item: inst.element });

			$.each(inst.sortables, function() {
				if(this.instance.isOver) {

					this.instance.isOver = 0;

					inst.cancelHelperRemoval = true; //Don't remove the helper in the draggable instance
					this.instance.cancelHelperRemoval = false; //Remove it in the sortable instance (so sortable plugins like revert still work)

					//The sortable revert is supported, and we have to set a temporary dropped variable on the draggable to support revert: "valid/invalid"
					if(this.shouldRevert) {
						this.instance.options.revert = this.shouldRevert;
					}

					//Trigger the stop of the sortable
					this.instance._mouseStop(event);

					this.instance.options.helper = this.instance.options._helper;

					//If the helper has been the original item, restore properties in the sortable
					if(inst.options.helper === "original") {
						this.instance.currentItem.css({ top: "auto", left: "auto" });
					}

				} else {
					this.instance.cancelHelperRemoval = false; //Remove the helper in the sortable instance
					this.instance._trigger("deactivate", event, uiSortable);
				}

			});

		},
		drag: function(event, ui) {

			var inst = $(this).data("ui-draggable"), that = this;

			$.each(inst.sortables, function() {

				var innermostIntersecting = false,
					thisSortable = this;

				//Copy over some variables to allow calling the sortable's native _intersectsWith
				this.instance.positionAbs = inst.positionAbs;
				this.instance.helperProportions = inst.helperProportions;
				this.instance.offset.click = inst.offset.click;

				if(this.instance._intersectsWith(this.instance.containerCache)) {
					innermostIntersecting = true;
					$.each(inst.sortables, function () {
						this.instance.positionAbs = inst.positionAbs;
						this.instance.helperProportions = inst.helperProportions;
						this.instance.offset.click = inst.offset.click;
						if (this !== thisSortable &&
							this.instance._intersectsWith(this.instance.containerCache) &&
							$.contains(thisSortable.instance.element[0], this.instance.element[0])
						) {
							innermostIntersecting = false;
						}
						return innermostIntersecting;
					});
				}


				if(innermostIntersecting) {
					//If it intersects, we use a little isOver variable and set it once, so our move-in stuff gets fired only once
					if(!this.instance.isOver) {

						this.instance.isOver = 1;
						//Now we fake the start of dragging for the sortable instance,
						//by cloning the list group item, appending it to the sortable and using it as inst.currentItem
						//We can then fire the start event of the sortable with our passed browser event, and our own helper (so it doesn't create a new one)
						this.instance.currentItem = $(that).clone().removeAttr("id").appendTo(this.instance.element).data("ui-sortable-item", true);
						this.instance.options._helper = this.instance.options.helper; //Store helper option to later restore it
						this.instance.options.helper = function() { return ui.helper[0]; };

						event.target = this.instance.currentItem[0];
						this.instance._mouseCapture(event, true);
						this.instance._mouseStart(event, true, true);

						//Because the browser event is way off the new appended portlet, we modify a couple of variables to reflect the changes
						this.instance.offset.click.top = inst.offset.click.top;
						this.instance.offset.click.left = inst.offset.click.left;
						this.instance.offset.parent.left -= inst.offset.parent.left - this.instance.offset.parent.left;
						this.instance.offset.parent.top -= inst.offset.parent.top - this.instance.offset.parent.top;

						inst._trigger("toSortable", event);
						inst.dropped = this.instance.element; //draggable revert needs that
						//hack so receive/update callbacks work (mostly)
						inst.currentItem = inst.element;
						this.instance.fromOutside = inst;

					}

					//Provided we did all the previous steps, we can fire the drag event of the sortable on every draggable drag, when it intersects with the sortable
					if(this.instance.currentItem) {
						this.instance._mouseDrag(event);
					}

				} else {

					//If it doesn't intersect with the sortable, and it intersected before,
					//we fake the drag stop of the sortable, but make sure it doesn't remove the helper by using cancelHelperRemoval
					if(this.instance.isOver) {

						this.instance.isOver = 0;
						this.instance.cancelHelperRemoval = true;

						//Prevent reverting on this forced stop
						this.instance.options.revert = false;

						// The out event needs to be triggered independently
						this.instance._trigger("out", event, this.instance._uiHash(this.instance));

						this.instance._mouseStop(event, true);
						this.instance.options.helper = this.instance.options._helper;

						//Now we remove our currentItem, the list group clone again, and the placeholder, and animate the helper back to it's original size
						this.instance.currentItem.remove();
						if(this.instance.placeholder) {
							this.instance.placeholder.remove();
						}

						inst._trigger("fromSortable", event);
						inst.dropped = false; //draggable revert needs that
					}

				}

			});

		}
	});

	$.ui.plugin.add("draggable", "cursor", {
		start: function() {
			var t = $("body"), o = $(this).data("ui-draggable").options;
			if (t.css("cursor")) {
				o._cursor = t.css("cursor");
			}
			t.css("cursor", o.cursor);
		},
		stop: function() {
			var o = $(this).data("ui-draggable").options;
			if (o._cursor) {
				$("body").css("cursor", o._cursor);
			}
		}
	});

	$.ui.plugin.add("draggable", "opacity", {
		start: function(event, ui) {
			var t = $(ui.helper), o = $(this).data("ui-draggable").options;
			if(t.css("opacity")) {
				o._opacity = t.css("opacity");
			}
			t.css("opacity", o.opacity);
		},
		stop: function(event, ui) {
			var o = $(this).data("ui-draggable").options;
			if(o._opacity) {
				$(ui.helper).css("opacity", o._opacity);
			}
		}
	});

	$.ui.plugin.add("draggable", "scroll", {
		start: function() {
			var i = $(this).data("ui-draggable");
			if(i.scrollParent[0] !== document && i.scrollParent[0].tagName !== "HTML") {
				i.overflowOffset = i.scrollParent.offset();
			}
		},
		drag: function( event ) {

			var i = $(this).data("ui-draggable"), o = i.options, scrolled = false;

			if(i.scrollParent[0] !== document && i.scrollParent[0].tagName !== "HTML") {

				if(!o.axis || o.axis !== "x") {
					if((i.overflowOffset.top + i.scrollParent[0].offsetHeight) - event.pageY < o.scrollSensitivity) {
						i.scrollParent[0].scrollTop = scrolled = i.scrollParent[0].scrollTop + o.scrollSpeed;
					} else if(event.pageY - i.overflowOffset.top < o.scrollSensitivity) {
						i.scrollParent[0].scrollTop = scrolled = i.scrollParent[0].scrollTop - o.scrollSpeed;
					}
				}

				if(!o.axis || o.axis !== "y") {
					if((i.overflowOffset.left + i.scrollParent[0].offsetWidth) - event.pageX < o.scrollSensitivity) {
						i.scrollParent[0].scrollLeft = scrolled = i.scrollParent[0].scrollLeft + o.scrollSpeed;
					} else if(event.pageX - i.overflowOffset.left < o.scrollSensitivity) {
						i.scrollParent[0].scrollLeft = scrolled = i.scrollParent[0].scrollLeft - o.scrollSpeed;
					}
				}

			} else {

				if(!o.axis || o.axis !== "x") {
					if(event.pageY - $(document).scrollTop() < o.scrollSensitivity) {
						scrolled = $(document).scrollTop($(document).scrollTop() - o.scrollSpeed);
					} else if($(window).height() - (event.pageY - $(document).scrollTop()) < o.scrollSensitivity) {
						scrolled = $(document).scrollTop($(document).scrollTop() + o.scrollSpeed);
					}
				}

				if(!o.axis || o.axis !== "y") {
					if(event.pageX - $(document).scrollLeft() < o.scrollSensitivity) {
						scrolled = $(document).scrollLeft($(document).scrollLeft() - o.scrollSpeed);
					} else if($(window).width() - (event.pageX - $(document).scrollLeft()) < o.scrollSensitivity) {
						scrolled = $(document).scrollLeft($(document).scrollLeft() + o.scrollSpeed);
					}
				}

			}

			if(scrolled !== false && $.ui.ddmanager && !o.dropBehaviour) {
				$.ui.ddmanager.prepareOffsets(i, event);
			}

		}
	});

	$.ui.plugin.add("draggable", "snap", {
		start: function() {

			var i = $(this).data("ui-draggable"),
				o = i.options;

			i.snapElements = [];

			$(o.snap.constructor !== String ? ( o.snap.items || ":data(ui-draggable)" ) : o.snap).each(function() {
				var $t = $(this),
					$o = $t.offset();
				if(this !== i.element[0]) {
					i.snapElements.push({
						item: this,
						width: $t.outerWidth(), height: $t.outerHeight(),
						top: $o.top, left: $o.left
					});
				}
			});

		},
		drag: function(event, ui) {

			var ts, bs, ls, rs, l, r, t, b, i, first,
				inst = $(this).data("ui-draggable"),
				o = inst.options,
				d = o.snapTolerance,
				x1 = ui.offset.left, x2 = x1 + inst.helperProportions.width,
				y1 = ui.offset.top, y2 = y1 + inst.helperProportions.height;

			for (i = inst.snapElements.length - 1; i >= 0; i--){

				l = inst.snapElements[i].left;
				r = l + inst.snapElements[i].width;
				t = inst.snapElements[i].top;
				b = t + inst.snapElements[i].height;

				if ( x2 < l - d || x1 > r + d || y2 < t - d || y1 > b + d || !$.contains( inst.snapElements[ i ].item.ownerDocument, inst.snapElements[ i ].item ) ) {
					if(inst.snapElements[i].snapping) {
						(inst.options.snap.release && inst.options.snap.release.call(inst.element, event, $.extend(inst._uiHash(), { snapItem: inst.snapElements[i].item })));
					}
					inst.snapElements[i].snapping = false;
					continue;
				}

				if(o.snapMode !== "inner") {
					ts = Math.abs(t - y2) <= d;
					bs = Math.abs(b - y1) <= d;
					ls = Math.abs(l - x2) <= d;
					rs = Math.abs(r - x1) <= d;
					if(ts) {
						ui.position.top = inst._convertPositionTo("relative", { top: t - inst.helperProportions.height, left: 0 }).top - inst.margins.top;
					}
					if(bs) {
						ui.position.top = inst._convertPositionTo("relative", { top: b, left: 0 }).top - inst.margins.top;
					}
					if(ls) {
						ui.position.left = inst._convertPositionTo("relative", { top: 0, left: l - inst.helperProportions.width }).left - inst.margins.left;
					}
					if(rs) {
						ui.position.left = inst._convertPositionTo("relative", { top: 0, left: r }).left - inst.margins.left;
					}
				}

				first = (ts || bs || ls || rs);

				if(o.snapMode !== "outer") {
					ts = Math.abs(t - y1) <= d;
					bs = Math.abs(b - y2) <= d;
					ls = Math.abs(l - x1) <= d;
					rs = Math.abs(r - x2) <= d;
					if(ts) {
						ui.position.top = inst._convertPositionTo("relative", { top: t, left: 0 }).top - inst.margins.top;
					}
					if(bs) {
						ui.position.top = inst._convertPositionTo("relative", { top: b - inst.helperProportions.height, left: 0 }).top - inst.margins.top;
					}
					if(ls) {
						ui.position.left = inst._convertPositionTo("relative", { top: 0, left: l }).left - inst.margins.left;
					}
					if(rs) {
						ui.position.left = inst._convertPositionTo("relative", { top: 0, left: r - inst.helperProportions.width }).left - inst.margins.left;
					}
				}

				if(!inst.snapElements[i].snapping && (ts || bs || ls || rs || first)) {
					(inst.options.snap.snap && inst.options.snap.snap.call(inst.element, event, $.extend(inst._uiHash(), { snapItem: inst.snapElements[i].item })));
				}
				inst.snapElements[i].snapping = (ts || bs || ls || rs || first);

			}

		}
	});

	$.ui.plugin.add("draggable", "stack", {
		start: function() {
			var min,
				o = this.data("ui-draggable").options,
				group = $.makeArray($(o.stack)).sort(function(a,b) {
					return (parseInt($(a).css("zIndex"),10) || 0) - (parseInt($(b).css("zIndex"),10) || 0);
				});

			if (!group.length) { return; }

			min = parseInt($(group[0]).css("zIndex"), 10) || 0;
			$(group).each(function(i) {
				$(this).css("zIndex", min + i);
			});
			this.css("zIndex", (min + group.length));
		}
	});

	$.ui.plugin.add("draggable", "zIndex", {
		start: function(event, ui) {
			var t = $(ui.helper), o = $(this).data("ui-draggable").options;
			if(t.css("zIndex")) {
				o._zIndex = t.css("zIndex");
			}
			t.css("zIndex", o.zIndex);
		},
		stop: function(event, ui) {
			var o = $(this).data("ui-draggable").options;
			if(o._zIndex) {
				$(ui.helper).css("zIndex", o._zIndex);
			}
		}
	});

	})(jQuery);

	(function( $, undefined ) {

	function isOverAxis( x, reference, size ) {
		return ( x > reference ) && ( x < ( reference + size ) );
	}

	$.widget("ui.droppable", {
		version: "1.10.3",
		widgetEventPrefix: "drop",
		options: {
			accept: "*",
			activeClass: false,
			addClasses: true,
			greedy: false,
			hoverClass: false,
			scope: "default",
			tolerance: "intersect",

			// callbacks
			activate: null,
			deactivate: null,
			drop: null,
			out: null,
			over: null
		},
		_create: function() {

			var o = this.options,
				accept = o.accept;

			this.isover = false;
			this.isout = true;

			this.accept = $.isFunction(accept) ? accept : function(d) {
				return d.is(accept);
			};

			//Store the droppable's proportions
			this.proportions = { width: this.element[0].offsetWidth, height: this.element[0].offsetHeight };

			// Add the reference and positions to the manager
			$.ui.ddmanager.droppables[o.scope] = $.ui.ddmanager.droppables[o.scope] || [];
			$.ui.ddmanager.droppables[o.scope].push(this);

			(o.addClasses && this.element.addClass("ui-droppable"));

		},

		_destroy: function() {
			var i = 0,
				drop = $.ui.ddmanager.droppables[this.options.scope];

			for ( ; i < drop.length; i++ ) {
				if ( drop[i] === this ) {
					drop.splice(i, 1);
				}
			}

			this.element.removeClass("ui-droppable ui-droppable-disabled");
		},

		_setOption: function(key, value) {

			if(key === "accept") {
				this.accept = $.isFunction(value) ? value : function(d) {
					return d.is(value);
				};
			}
			$.Widget.prototype._setOption.apply(this, arguments);
		},

		_activate: function(event) {
			var draggable = $.ui.ddmanager.current;
			if(this.options.activeClass) {
				this.element.addClass(this.options.activeClass);
			}
			if(draggable){
				this._trigger("activate", event, this.ui(draggable));
			}
		},

		_deactivate: function(event) {
			var draggable = $.ui.ddmanager.current;
			if(this.options.activeClass) {
				this.element.removeClass(this.options.activeClass);
			}
			if(draggable){
				this._trigger("deactivate", event, this.ui(draggable));
			}
		},

		_over: function(event) {

			var draggable = $.ui.ddmanager.current;

			// Bail if draggable and droppable are same element
			if (!draggable || (draggable.currentItem || draggable.element)[0] === this.element[0]) {
				return;
			}

			if (this.accept.call(this.element[0],(draggable.currentItem || draggable.element))) {
				if(this.options.hoverClass) {
					this.element.addClass(this.options.hoverClass);
				}
				this._trigger("over", event, this.ui(draggable));
			}

		},

		_out: function(event) {

			var draggable = $.ui.ddmanager.current;

			// Bail if draggable and droppable are same element
			if (!draggable || (draggable.currentItem || draggable.element)[0] === this.element[0]) {
				return;
			}

			if (this.accept.call(this.element[0],(draggable.currentItem || draggable.element))) {
				if(this.options.hoverClass) {
					this.element.removeClass(this.options.hoverClass);
				}
				this._trigger("out", event, this.ui(draggable));
			}

		},

		_drop: function(event,custom) {

			var draggable = custom || $.ui.ddmanager.current,
				childrenIntersection = false;

			// Bail if draggable and droppable are same element
			if (!draggable || (draggable.currentItem || draggable.element)[0] === this.element[0]) {
				return false;
			}

			this.element.find(":data(ui-droppable)").not(".ui-draggable-dragging").each(function() {
				var inst = $.data(this, "ui-droppable");
				if(
					inst.options.greedy &&
					!inst.options.disabled &&
					inst.options.scope === draggable.options.scope &&
					inst.accept.call(inst.element[0], (draggable.currentItem || draggable.element)) &&
					$.ui.intersect(draggable, $.extend(inst, { offset: inst.element.offset() }), inst.options.tolerance)
				) { childrenIntersection = true; return false; }
			});
			if(childrenIntersection) {
				return false;
			}

			if(this.accept.call(this.element[0],(draggable.currentItem || draggable.element))) {
				if(this.options.activeClass) {
					this.element.removeClass(this.options.activeClass);
				}
				if(this.options.hoverClass) {
					this.element.removeClass(this.options.hoverClass);
				}
				this._trigger("drop", event, this.ui(draggable));
				return this.element;
			}

			return false;

		},

		ui: function(c) {
			return {
				draggable: (c.currentItem || c.element),
				helper: c.helper,
				position: c.position,
				offset: c.positionAbs
			};
		}

	});

	$.ui.intersect = function(draggable, droppable, toleranceMode) {

		if (!droppable.offset) {
			return false;
		}

		var draggableLeft, draggableTop,
			x1 = (draggable.positionAbs || draggable.position.absolute).left, x2 = x1 + draggable.helperProportions.width,
			y1 = (draggable.positionAbs || draggable.position.absolute).top, y2 = y1 + draggable.helperProportions.height,
			l = droppable.offset.left, r = l + droppable.proportions.width,
			t = droppable.offset.top, b = t + droppable.proportions.height;

		switch (toleranceMode) {
			case "fit":
				return (l <= x1 && x2 <= r && t <= y1 && y2 <= b);
			case "intersect":
				return (l < x1 + (draggable.helperProportions.width / 2) && // Right Half
					x2 - (draggable.helperProportions.width / 2) < r && // Left Half
					t < y1 + (draggable.helperProportions.height / 2) && // Bottom Half
					y2 - (draggable.helperProportions.height / 2) < b ); // Top Half
			case "pointer":
				draggableLeft = ((draggable.positionAbs || draggable.position.absolute).left + (draggable.clickOffset || draggable.offset.click).left);
				draggableTop = ((draggable.positionAbs || draggable.position.absolute).top + (draggable.clickOffset || draggable.offset.click).top);
				return isOverAxis( draggableTop, t, droppable.proportions.height ) && isOverAxis( draggableLeft, l, droppable.proportions.width );
			case "touch":
				return (
					(y1 >= t && y1 <= b) ||	// Top edge touching
					(y2 >= t && y2 <= b) ||	// Bottom edge touching
					(y1 < t && y2 > b)		// Surrounded vertically
				) && (
					(x1 >= l && x1 <= r) ||	// Left edge touching
					(x2 >= l && x2 <= r) ||	// Right edge touching
					(x1 < l && x2 > r)		// Surrounded horizontally
				);
			default:
				return false;
			}

	};

	/*
		This manager tracks offsets of draggables and droppables
	*/
	$.ui.ddmanager = {
		current: null,
		droppables: { "default": [] },
		prepareOffsets: function(t, event) {

			var i, j,
				m = $.ui.ddmanager.droppables[t.options.scope] || [],
				type = event ? event.type : null, // workaround for #2317
				list = (t.currentItem || t.element).find(":data(ui-droppable)").addBack();

			droppablesLoop: for (i = 0; i < m.length; i++) {

				//No disabled and non-accepted
				if(m[i].options.disabled || (t && !m[i].accept.call(m[i].element[0],(t.currentItem || t.element)))) {
					continue;
				}

				// Filter out elements in the current dragged item
				for (j=0; j < list.length; j++) {
					if(list[j] === m[i].element[0]) {
						m[i].proportions.height = 0;
						continue droppablesLoop;
					}
				}

				m[i].visible = m[i].element.css("display") !== "none";
				if(!m[i].visible) {
					continue;
				}

				//Activate the droppable if used directly from draggables
				if(type === "mousedown") {
					m[i]._activate.call(m[i], event);
				}

				m[i].offset = m[i].element.offset();
				m[i].proportions = { width: m[i].element[0].offsetWidth, height: m[i].element[0].offsetHeight };

			}

		},
		drop: function(draggable, event) {

			var dropped = false;
			// Create a copy of the droppables in case the list changes during the drop (#9116)
			$.each(($.ui.ddmanager.droppables[draggable.options.scope] || []).slice(), function() {

				if(!this.options) {
					return;
				}
				if (!this.options.disabled && this.visible && $.ui.intersect(draggable, this, this.options.tolerance)) {
					dropped = this._drop.call(this, event) || dropped;
				}

				if (!this.options.disabled && this.visible && this.accept.call(this.element[0],(draggable.currentItem || draggable.element))) {
					this.isout = true;
					this.isover = false;
					this._deactivate.call(this, event);
				}

			});
			return dropped;

		},
		dragStart: function( draggable, event ) {
			//Listen for scrolling so that if the dragging causes scrolling the position of the droppables can be recalculated (see #5003)
			draggable.element.parentsUntil( "body" ).bind( "scroll.droppable", function() {
				if( !draggable.options.refreshPositions ) {
					$.ui.ddmanager.prepareOffsets( draggable, event );
				}
			});
		},
		drag: function(draggable, event) {

			//If you have a highly dynamic page, you might try this option. It renders positions every time you move the mouse.
			if(draggable.options.refreshPositions) {
				$.ui.ddmanager.prepareOffsets(draggable, event);
			}

			//Run through all droppables and check their positions based on specific tolerance options
			$.each($.ui.ddmanager.droppables[draggable.options.scope] || [], function() {

				if(this.options.disabled || this.greedyChild || !this.visible) {
					return;
				}

				var parentInstance, scope, parent,
					intersects = $.ui.intersect(draggable, this, this.options.tolerance),
					c = !intersects && this.isover ? "isout" : (intersects && !this.isover ? "isover" : null);
				if(!c) {
					return;
				}

				if (this.options.greedy) {
					// find droppable parents with same scope
					scope = this.options.scope;
					parent = this.element.parents(":data(ui-droppable)").filter(function () {
						return $.data(this, "ui-droppable").options.scope === scope;
					});

					if (parent.length) {
						parentInstance = $.data(parent[0], "ui-droppable");
						parentInstance.greedyChild = (c === "isover");
					}
				}

				// we just moved into a greedy child
				if (parentInstance && c === "isover") {
					parentInstance.isover = false;
					parentInstance.isout = true;
					parentInstance._out.call(parentInstance, event);
				}

				this[c] = true;
				this[c === "isout" ? "isover" : "isout"] = false;
				this[c === "isover" ? "_over" : "_out"].call(this, event);

				// we just moved out of a greedy child
				if (parentInstance && c === "isout") {
					parentInstance.isout = false;
					parentInstance.isover = true;
					parentInstance._over.call(parentInstance, event);
				}
			});

		},
		dragStop: function( draggable, event ) {
			draggable.element.parentsUntil( "body" ).unbind( "scroll.droppable" );
			//Call prepareOffsets one final time since IE does not fire return scroll events when overflow was caused by drag (see #5003)
			if( !draggable.options.refreshPositions ) {
				$.ui.ddmanager.prepareOffsets( draggable, event );
			}
		}
	};

	})(jQuery);

	(function( $, undefined ) {

	function num(v) {
		return parseInt(v, 10) || 0;
	}

	function isNumber(value) {
		return !isNaN(parseInt(value, 10));
	}

	$.widget("ui.resizable", $.ui.mouse, {
		version: "1.10.3",
		widgetEventPrefix: "resize",
		options: {
			alsoResize: false,
			animate: false,
			animateDuration: "slow",
			animateEasing: "swing",
			aspectRatio: false,
			autoHide: false,
			containment: false,
			ghost: false,
			grid: false,
			handles: "e,s,se",
			helper: false,
			maxHeight: null,
			maxWidth: null,
			minHeight: 10,
			minWidth: 10,
			// See #7960
			zIndex: 90,

			// callbacks
			resize: null,
			start: null,
			stop: null
		},
		_create: function() {

			var n, i, handle, axis, hname,
				that = this,
				o = this.options;
			this.element.addClass("ui-resizable");

			$.extend(this, {
				_aspectRatio: !!(o.aspectRatio),
				aspectRatio: o.aspectRatio,
				originalElement: this.element,
				_proportionallyResizeElements: [],
				_helper: o.helper || o.ghost || o.animate ? o.helper || "ui-resizable-helper" : null
			});

			//Wrap the element if it cannot hold child nodes
			if(this.element[0].nodeName.match(/canvas|textarea|input|select|button|img/i)) {

				//Create a wrapper element and set the wrapper to the new current internal element
				this.element.wrap(
					$("<div class='ui-wrapper' style='overflow: hidden;'></div>").css({
						position: this.element.css("position"),
						width: this.element.outerWidth(),
						height: this.element.outerHeight(),
						top: this.element.css("top"),
						left: this.element.css("left")
					})
				);

				//Overwrite the original this.element
				this.element = this.element.parent().data(
					"ui-resizable", this.element.data("ui-resizable")
				);

				this.elementIsWrapper = true;

				//Move margins to the wrapper
				this.element.css({ marginLeft: this.originalElement.css("marginLeft"), marginTop: this.originalElement.css("marginTop"), marginRight: this.originalElement.css("marginRight"), marginBottom: this.originalElement.css("marginBottom") });
				this.originalElement.css({ marginLeft: 0, marginTop: 0, marginRight: 0, marginBottom: 0});

				//Prevent Safari textarea resize
				this.originalResizeStyle = this.originalElement.css("resize");
				this.originalElement.css("resize", "none");

				//Push the actual element to our proportionallyResize internal array
				this._proportionallyResizeElements.push(this.originalElement.css({ position: "static", zoom: 1, display: "block" }));

				// avoid IE jump (hard set the margin)
				this.originalElement.css({ margin: this.originalElement.css("margin") });

				// fix handlers offset
				this._proportionallyResize();

			}

			this.handles = o.handles || (!$(".ui-resizable-handle", this.element).length ? "e,s,se" : { n: ".ui-resizable-n", e: ".ui-resizable-e", s: ".ui-resizable-s", w: ".ui-resizable-w", se: ".ui-resizable-se", sw: ".ui-resizable-sw", ne: ".ui-resizable-ne", nw: ".ui-resizable-nw" });
			if(this.handles.constructor === String) {

				if ( this.handles === "all") {
					this.handles = "n,e,s,w,se,sw,ne,nw";
				}

				n = this.handles.split(",");
				this.handles = {};

				for(i = 0; i < n.length; i++) {

					handle = $.trim(n[i]);
					hname = "ui-resizable-"+handle;
					axis = $("<div class='ui-resizable-handle " + hname + "'></div>");

					// Apply zIndex to all handles - see #7960
					axis.css({ zIndex: o.zIndex });

					//TODO : What's going on here?
					if ("se" === handle) {
						axis.addClass("ui-icon ui-icon-gripsmall-diagonal-se");
					}

					//Insert into internal handles object and append to element
					this.handles[handle] = ".ui-resizable-"+handle;
					this.element.append(axis);
				}

			}

			this._renderAxis = function(target) {

				var i, axis, padPos, padWrapper;

				target = target || this.element;

				for(i in this.handles) {

					if(this.handles[i].constructor === String) {
						this.handles[i] = $(this.handles[i], this.element).show();
					}

					//Apply pad to wrapper element, needed to fix axis position (textarea, inputs, scrolls)
					if (this.elementIsWrapper && this.originalElement[0].nodeName.match(/textarea|input|select|button/i)) {

						axis = $(this.handles[i], this.element);

						//Checking the correct pad and border
						padWrapper = /sw|ne|nw|se|n|s/.test(i) ? axis.outerHeight() : axis.outerWidth();

						//The padding type i have to apply...
						padPos = [ "padding",
							/ne|nw|n/.test(i) ? "Top" :
							/se|sw|s/.test(i) ? "Bottom" :
							/^e$/.test(i) ? "Right" : "Left" ].join("");

						target.css(padPos, padWrapper);

						this._proportionallyResize();

					}

					//TODO: What's that good for? There's not anything to be executed left
					if(!$(this.handles[i]).length) {
						continue;
					}
				}
			};

			//TODO: make renderAxis a prototype function
			this._renderAxis(this.element);

			this._handles = $(".ui-resizable-handle", this.element)
				.disableSelection();

			//Matching axis name
			this._handles.mouseover(function() {
				if (!that.resizing) {
					if (this.className) {
						axis = this.className.match(/ui-resizable-(se|sw|ne|nw|n|e|s|w)/i);
					}
					//Axis, default = se
					that.axis = axis && axis[1] ? axis[1] : "se";
				}
			});

			//If we want to auto hide the elements
			if (o.autoHide) {
				this._handles.hide();
				$(this.element)
					.addClass("ui-resizable-autohide")
					.mouseenter(function() {
						if (o.disabled) {
							return;
						}
						$(this).removeClass("ui-resizable-autohide");
						that._handles.show();
					})
					.mouseleave(function(){
						if (o.disabled) {
							return;
						}
						if (!that.resizing) {
							$(this).addClass("ui-resizable-autohide");
							that._handles.hide();
						}
					});
			}

			//Initialize the mouse interaction
			this._mouseInit();

		},

		_destroy: function() {

			this._mouseDestroy();

			var wrapper,
				_destroy = function(exp) {
					$(exp).removeClass("ui-resizable ui-resizable-disabled ui-resizable-resizing")
						.removeData("resizable").removeData("ui-resizable").unbind(".resizable").find(".ui-resizable-handle").remove();
				};

			//TODO: Unwrap at same DOM position
			if (this.elementIsWrapper) {
				_destroy(this.element);
				wrapper = this.element;
				this.originalElement.css({
					position: wrapper.css("position"),
					width: wrapper.outerWidth(),
					height: wrapper.outerHeight(),
					top: wrapper.css("top"),
					left: wrapper.css("left")
				}).insertAfter( wrapper );
				wrapper.remove();
			}

			this.originalElement.css("resize", this.originalResizeStyle);
			_destroy(this.originalElement);

			return this;
		},

		_mouseCapture: function(event) {
			var i, handle,
				capture = false;

			for (i in this.handles) {
				handle = $(this.handles[i])[0];
				if (handle === event.target || $.contains(handle, event.target)) {
					capture = true;
				}
			}

			return !this.options.disabled && capture;
		},

		_mouseStart: function(event) {

			var curleft, curtop, cursor,
				o = this.options,
				iniPos = this.element.position(),
				el = this.element;

			this.resizing = true;

			// bugfix for http://dev.jquery.com/ticket/1749
			if ( (/absolute/).test( el.css("position") ) ) {
				el.css({ position: "absolute", top: el.css("top"), left: el.css("left") });
			} else if (el.is(".ui-draggable")) {
				el.css({ position: "absolute", top: iniPos.top, left: iniPos.left });
			}

			this._renderProxy();

			curleft = num(this.helper.css("left"));
			curtop = num(this.helper.css("top"));

			if (o.containment) {
				curleft += $(o.containment).scrollLeft() || 0;
				curtop += $(o.containment).scrollTop() || 0;
			}

			//Store needed variables
			this.offset = this.helper.offset();
			this.position = { left: curleft, top: curtop };
			this.size = this._helper ? { width: el.outerWidth(), height: el.outerHeight() } : { width: el.width(), height: el.height() };
			this.originalSize = this._helper ? { width: el.outerWidth(), height: el.outerHeight() } : { width: el.width(), height: el.height() };
			this.originalPosition = { left: curleft, top: curtop };
			this.sizeDiff = { width: el.outerWidth() - el.width(), height: el.outerHeight() - el.height() };
			this.originalMousePosition = { left: event.pageX, top: event.pageY };

			//Aspect Ratio
			this.aspectRatio = (typeof o.aspectRatio === "number") ? o.aspectRatio : ((this.originalSize.width / this.originalSize.height) || 1);

			cursor = $(".ui-resizable-" + this.axis).css("cursor");
			$("body").css("cursor", cursor === "auto" ? this.axis + "-resize" : cursor);

			el.addClass("ui-resizable-resizing");
			this._propagate("start", event);
			return true;
		},

		_mouseDrag: function(event) {

			//Increase performance, avoid regex
			var data,
				el = this.helper, props = {},
				smp = this.originalMousePosition,
				a = this.axis,
				prevTop = this.position.top,
				prevLeft = this.position.left,
				prevWidth = this.size.width,
				prevHeight = this.size.height,
				dx = (event.pageX-smp.left)||0,
				dy = (event.pageY-smp.top)||0,
				trigger = this._change[a];

			if (!trigger) {
				return false;
			}

			// Calculate the attrs that will be change
			data = trigger.apply(this, [event, dx, dy]);

			// Put this in the mouseDrag handler since the user can start pressing shift while resizing
			this._updateVirtualBoundaries(event.shiftKey);
			if (this._aspectRatio || event.shiftKey) {
				data = this._updateRatio(data, event);
			}

			data = this._respectSize(data, event);

			this._updateCache(data);

			// plugins callbacks need to be called first
			this._propagate("resize", event);

			if (this.position.top !== prevTop) {
				props.top = this.position.top + "px";
			}
			if (this.position.left !== prevLeft) {
				props.left = this.position.left + "px";
			}
			if (this.size.width !== prevWidth) {
				props.width = this.size.width + "px";
			}
			if (this.size.height !== prevHeight) {
				props.height = this.size.height + "px";
			}
			el.css(props);

			if (!this._helper && this._proportionallyResizeElements.length) {
				this._proportionallyResize();
			}

			// Call the user callback if the element was resized
			if ( ! $.isEmptyObject(props) ) {
				this._trigger("resize", event, this.ui());
			}

			return false;
		},

		_mouseStop: function(event) {

			this.resizing = false;
			var pr, ista, soffseth, soffsetw, s, left, top,
				o = this.options, that = this;

			if(this._helper) {

				pr = this._proportionallyResizeElements;
				ista = pr.length && (/textarea/i).test(pr[0].nodeName);
				soffseth = ista && $.ui.hasScroll(pr[0], "left") /* TODO - jump height */ ? 0 : that.sizeDiff.height;
				soffsetw = ista ? 0 : that.sizeDiff.width;

				s = { width: (that.helper.width()  - soffsetw), height: (that.helper.height() - soffseth) };
				left = (parseInt(that.element.css("left"), 10) + (that.position.left - that.originalPosition.left)) || null;
				top = (parseInt(that.element.css("top"), 10) + (that.position.top - that.originalPosition.top)) || null;

				if (!o.animate) {
					this.element.css($.extend(s, { top: top, left: left }));
				}

				that.helper.height(that.size.height);
				that.helper.width(that.size.width);

				if (this._helper && !o.animate) {
					this._proportionallyResize();
				}
			}

			$("body").css("cursor", "auto");

			this.element.removeClass("ui-resizable-resizing");

			this._propagate("stop", event);

			if (this._helper) {
				this.helper.remove();
			}

			return false;

		},

		_updateVirtualBoundaries: function(forceAspectRatio) {
			var pMinWidth, pMaxWidth, pMinHeight, pMaxHeight, b,
				o = this.options;

			b = {
				minWidth: isNumber(o.minWidth) ? o.minWidth : 0,
				maxWidth: isNumber(o.maxWidth) ? o.maxWidth : Infinity,
				minHeight: isNumber(o.minHeight) ? o.minHeight : 0,
				maxHeight: isNumber(o.maxHeight) ? o.maxHeight : Infinity
			};

			if(this._aspectRatio || forceAspectRatio) {
				// We want to create an enclosing box whose aspect ration is the requested one
				// First, compute the "projected" size for each dimension based on the aspect ratio and other dimension
				pMinWidth = b.minHeight * this.aspectRatio;
				pMinHeight = b.minWidth / this.aspectRatio;
				pMaxWidth = b.maxHeight * this.aspectRatio;
				pMaxHeight = b.maxWidth / this.aspectRatio;

				if(pMinWidth > b.minWidth) {
					b.minWidth = pMinWidth;
				}
				if(pMinHeight > b.minHeight) {
					b.minHeight = pMinHeight;
				}
				if(pMaxWidth < b.maxWidth) {
					b.maxWidth = pMaxWidth;
				}
				if(pMaxHeight < b.maxHeight) {
					b.maxHeight = pMaxHeight;
				}
			}
			this._vBoundaries = b;
		},

		_updateCache: function(data) {
			this.offset = this.helper.offset();
			if (isNumber(data.left)) {
				this.position.left = data.left;
			}
			if (isNumber(data.top)) {
				this.position.top = data.top;
			}
			if (isNumber(data.height)) {
				this.size.height = data.height;
			}
			if (isNumber(data.width)) {
				this.size.width = data.width;
			}
		},

		_updateRatio: function( data ) {

			var cpos = this.position,
				csize = this.size,
				a = this.axis;

			if (isNumber(data.height)) {
				data.width = (data.height * this.aspectRatio);
			} else if (isNumber(data.width)) {
				data.height = (data.width / this.aspectRatio);
			}

			if (a === "sw") {
				data.left = cpos.left + (csize.width - data.width);
				data.top = null;
			}
			if (a === "nw") {
				data.top = cpos.top + (csize.height - data.height);
				data.left = cpos.left + (csize.width - data.width);
			}

			return data;
		},

		_respectSize: function( data ) {

			var o = this._vBoundaries,
				a = this.axis,
				ismaxw = isNumber(data.width) && o.maxWidth && (o.maxWidth < data.width), ismaxh = isNumber(data.height) && o.maxHeight && (o.maxHeight < data.height),
				isminw = isNumber(data.width) && o.minWidth && (o.minWidth > data.width), isminh = isNumber(data.height) && o.minHeight && (o.minHeight > data.height),
				dw = this.originalPosition.left + this.originalSize.width,
				dh = this.position.top + this.size.height,
				cw = /sw|nw|w/.test(a), ch = /nw|ne|n/.test(a);
			if (isminw) {
				data.width = o.minWidth;
			}
			if (isminh) {
				data.height = o.minHeight;
			}
			if (ismaxw) {
				data.width = o.maxWidth;
			}
			if (ismaxh) {
				data.height = o.maxHeight;
			}

			if (isminw && cw) {
				data.left = dw - o.minWidth;
			}
			if (ismaxw && cw) {
				data.left = dw - o.maxWidth;
			}
			if (isminh && ch) {
				data.top = dh - o.minHeight;
			}
			if (ismaxh && ch) {
				data.top = dh - o.maxHeight;
			}

			// fixing jump error on top/left - bug #2330
			if (!data.width && !data.height && !data.left && data.top) {
				data.top = null;
			} else if (!data.width && !data.height && !data.top && data.left) {
				data.left = null;
			}

			return data;
		},

		_proportionallyResize: function() {

			if (!this._proportionallyResizeElements.length) {
				return;
			}

			var i, j, borders, paddings, prel,
				element = this.helper || this.element;

			for ( i=0; i < this._proportionallyResizeElements.length; i++) {

				prel = this._proportionallyResizeElements[i];

				if (!this.borderDif) {
					this.borderDif = [];
					borders = [prel.css("borderTopWidth"), prel.css("borderRightWidth"), prel.css("borderBottomWidth"), prel.css("borderLeftWidth")];
					paddings = [prel.css("paddingTop"), prel.css("paddingRight"), prel.css("paddingBottom"), prel.css("paddingLeft")];

					for ( j = 0; j < borders.length; j++ ) {
						this.borderDif[ j ] = ( parseInt( borders[ j ], 10 ) || 0 ) + ( parseInt( paddings[ j ], 10 ) || 0 );
					}
				}

				prel.css({
					height: (element.height() - this.borderDif[0] - this.borderDif[2]) || 0,
					width: (element.width() - this.borderDif[1] - this.borderDif[3]) || 0
				});

			}

		},

		_renderProxy: function() {

			var el = this.element, o = this.options;
			this.elementOffset = el.offset();

			if(this._helper) {

				this.helper = this.helper || $("<div style='overflow:hidden;'></div>");

				this.helper.addClass(this._helper).css({
					width: this.element.outerWidth() - 1,
					height: this.element.outerHeight() - 1,
					position: "absolute",
					left: this.elementOffset.left +"px",
					top: this.elementOffset.top +"px",
					zIndex: ++o.zIndex //TODO: Don't modify option
				});

				this.helper
					.appendTo("body")
					.disableSelection();

			} else {
				this.helper = this.element;
			}

		},

		_change: {
			e: function(event, dx) {
				return { width: this.originalSize.width + dx };
			},
			w: function(event, dx) {
				var cs = this.originalSize, sp = this.originalPosition;
				return { left: sp.left + dx, width: cs.width - dx };
			},
			n: function(event, dx, dy) {
				var cs = this.originalSize, sp = this.originalPosition;
				return { top: sp.top + dy, height: cs.height - dy };
			},
			s: function(event, dx, dy) {
				return { height: this.originalSize.height + dy };
			},
			se: function(event, dx, dy) {
				return $.extend(this._change.s.apply(this, arguments), this._change.e.apply(this, [event, dx, dy]));
			},
			sw: function(event, dx, dy) {
				return $.extend(this._change.s.apply(this, arguments), this._change.w.apply(this, [event, dx, dy]));
			},
			ne: function(event, dx, dy) {
				return $.extend(this._change.n.apply(this, arguments), this._change.e.apply(this, [event, dx, dy]));
			},
			nw: function(event, dx, dy) {
				return $.extend(this._change.n.apply(this, arguments), this._change.w.apply(this, [event, dx, dy]));
			}
		},

		_propagate: function(n, event) {
			$.ui.plugin.call(this, n, [event, this.ui()]);
			(n !== "resize" && this._trigger(n, event, this.ui()));
		},

		plugins: {},

		ui: function() {
			return {
				originalElement: this.originalElement,
				element: this.element,
				helper: this.helper,
				position: this.position,
				size: this.size,
				originalSize: this.originalSize,
				originalPosition: this.originalPosition
			};
		}

	});

	/*
	 * Resizable Extensions
	 */

	$.ui.plugin.add("resizable", "animate", {

		stop: function( event ) {
			var that = $(this).data("ui-resizable"),
				o = that.options,
				pr = that._proportionallyResizeElements,
				ista = pr.length && (/textarea/i).test(pr[0].nodeName),
				soffseth = ista && $.ui.hasScroll(pr[0], "left") /* TODO - jump height */ ? 0 : that.sizeDiff.height,
				soffsetw = ista ? 0 : that.sizeDiff.width,
				style = { width: (that.size.width - soffsetw), height: (that.size.height - soffseth) },
				left = (parseInt(that.element.css("left"), 10) + (that.position.left - that.originalPosition.left)) || null,
				top = (parseInt(that.element.css("top"), 10) + (that.position.top - that.originalPosition.top)) || null;

			that.element.animate(
				$.extend(style, top && left ? { top: top, left: left } : {}), {
					duration: o.animateDuration,
					easing: o.animateEasing,
					step: function() {

						var data = {
							width: parseInt(that.element.css("width"), 10),
							height: parseInt(that.element.css("height"), 10),
							top: parseInt(that.element.css("top"), 10),
							left: parseInt(that.element.css("left"), 10)
						};

						if (pr && pr.length) {
							$(pr[0]).css({ width: data.width, height: data.height });
						}

						// propagating resize, and updating values for each animation step
						that._updateCache(data);
						that._propagate("resize", event);

					}
				}
			);
		}

	});

	$.ui.plugin.add("resizable", "containment", {

		start: function() {
			var element, p, co, ch, cw, width, height,
				that = $(this).data("ui-resizable"),
				o = that.options,
				el = that.element,
				oc = o.containment,
				ce = (oc instanceof $) ? oc.get(0) : (/parent/.test(oc)) ? el.parent().get(0) : oc;

			if (!ce) {
				return;
			}

			that.containerElement = $(ce);

			if (/document/.test(oc) || oc === document) {
				that.containerOffset = { left: 0, top: 0 };
				that.containerPosition = { left: 0, top: 0 };

				that.parentData = {
					element: $(document), left: 0, top: 0,
					width: $(document).width(), height: $(document).height() || document.body.parentNode.scrollHeight
				};
			}

			// i'm a node, so compute top, left, right, bottom
			else {
				element = $(ce);
				p = [];
				$([ "Top", "Right", "Left", "Bottom" ]).each(function(i, name) { p[i] = num(element.css("padding" + name)); });

				that.containerOffset = element.offset();
				that.containerPosition = element.position();
				that.containerSize = { height: (element.innerHeight() - p[3]), width: (element.innerWidth() - p[1]) };

				co = that.containerOffset;
				ch = that.containerSize.height;
				cw = that.containerSize.width;
				width = ($.ui.hasScroll(ce, "left") ? ce.scrollWidth : cw );
				height = ($.ui.hasScroll(ce) ? ce.scrollHeight : ch);

				that.parentData = {
					element: ce, left: co.left, top: co.top, width: width, height: height
				};
			}
		},

		resize: function( event ) {
			var woset, hoset, isParent, isOffsetRelative,
				that = $(this).data("ui-resizable"),
				o = that.options,
				co = that.containerOffset, cp = that.position,
				pRatio = that._aspectRatio || event.shiftKey,
				cop = { top:0, left:0 }, ce = that.containerElement;

			if (ce[0] !== document && (/static/).test(ce.css("position"))) {
				cop = co;
			}

			if (cp.left < (that._helper ? co.left : 0)) {
				that.size.width = that.size.width + (that._helper ? (that.position.left - co.left) : (that.position.left - cop.left));
				if (pRatio) {
					that.size.height = that.size.width / that.aspectRatio;
				}
				that.position.left = o.helper ? co.left : 0;
			}

			if (cp.top < (that._helper ? co.top : 0)) {
				that.size.height = that.size.height + (that._helper ? (that.position.top - co.top) : that.position.top);
				if (pRatio) {
					that.size.width = that.size.height * that.aspectRatio;
				}
				that.position.top = that._helper ? co.top : 0;
			}

			that.offset.left = that.parentData.left+that.position.left;
			that.offset.top = that.parentData.top+that.position.top;

			woset = Math.abs( (that._helper ? that.offset.left - cop.left : (that.offset.left - cop.left)) + that.sizeDiff.width );
			hoset = Math.abs( (that._helper ? that.offset.top - cop.top : (that.offset.top - co.top)) + that.sizeDiff.height );

			isParent = that.containerElement.get(0) === that.element.parent().get(0);
			isOffsetRelative = /relative|absolute/.test(that.containerElement.css("position"));

			if(isParent && isOffsetRelative) {
				woset -= that.parentData.left;
			}

			if (woset + that.size.width >= that.parentData.width) {
				that.size.width = that.parentData.width - woset;
				if (pRatio) {
					that.size.height = that.size.width / that.aspectRatio;
				}
			}

			if (hoset + that.size.height >= that.parentData.height) {
				that.size.height = that.parentData.height - hoset;
				if (pRatio) {
					that.size.width = that.size.height * that.aspectRatio;
				}
			}
		},

		stop: function(){
			var that = $(this).data("ui-resizable"),
				o = that.options,
				co = that.containerOffset,
				cop = that.containerPosition,
				ce = that.containerElement,
				helper = $(that.helper),
				ho = helper.offset(),
				w = helper.outerWidth() - that.sizeDiff.width,
				h = helper.outerHeight() - that.sizeDiff.height;

			if (that._helper && !o.animate && (/relative/).test(ce.css("position"))) {
				$(this).css({ left: ho.left - cop.left - co.left, width: w, height: h });
			}

			if (that._helper && !o.animate && (/static/).test(ce.css("position"))) {
				$(this).css({ left: ho.left - cop.left - co.left, width: w, height: h });
			}

		}
	});

	$.ui.plugin.add("resizable", "alsoResize", {

		start: function () {
			var that = $(this).data("ui-resizable"),
				o = that.options,
				_store = function (exp) {
					$(exp).each(function() {
						var el = $(this);
						el.data("ui-resizable-alsoresize", {
							width: parseInt(el.width(), 10), height: parseInt(el.height(), 10),
							left: parseInt(el.css("left"), 10), top: parseInt(el.css("top"), 10)
						});
					});
				};

			if (typeof(o.alsoResize) === "object" && !o.alsoResize.parentNode) {
				if (o.alsoResize.length) { o.alsoResize = o.alsoResize[0]; _store(o.alsoResize); }
				else { $.each(o.alsoResize, function (exp) { _store(exp); }); }
			}else{
				_store(o.alsoResize);
			}
		},

		resize: function (event, ui) {
			var that = $(this).data("ui-resizable"),
				o = that.options,
				os = that.originalSize,
				op = that.originalPosition,
				delta = {
					height: (that.size.height - os.height) || 0, width: (that.size.width - os.width) || 0,
					top: (that.position.top - op.top) || 0, left: (that.position.left - op.left) || 0
				},

				_alsoResize = function (exp, c) {
					$(exp).each(function() {
						var el = $(this), start = $(this).data("ui-resizable-alsoresize"), style = {},
							css = c && c.length ? c : el.parents(ui.originalElement[0]).length ? ["width", "height"] : ["width", "height", "top", "left"];

						$.each(css, function (i, prop) {
							var sum = (start[prop]||0) + (delta[prop]||0);
							if (sum && sum >= 0) {
								style[prop] = sum || null;
							}
						});

						el.css(style);
					});
				};

			if (typeof(o.alsoResize) === "object" && !o.alsoResize.nodeType) {
				$.each(o.alsoResize, function (exp, c) { _alsoResize(exp, c); });
			}else{
				_alsoResize(o.alsoResize);
			}
		},

		stop: function () {
			$(this).removeData("resizable-alsoresize");
		}
	});

	$.ui.plugin.add("resizable", "ghost", {

		start: function() {

			var that = $(this).data("ui-resizable"), o = that.options, cs = that.size;

			that.ghost = that.originalElement.clone();
			that.ghost
				.css({ opacity: 0.25, display: "block", position: "relative", height: cs.height, width: cs.width, margin: 0, left: 0, top: 0 })
				.addClass("ui-resizable-ghost")
				.addClass(typeof o.ghost === "string" ? o.ghost : "");

			that.ghost.appendTo(that.helper);

		},

		resize: function(){
			var that = $(this).data("ui-resizable");
			if (that.ghost) {
				that.ghost.css({ position: "relative", height: that.size.height, width: that.size.width });
			}
		},

		stop: function() {
			var that = $(this).data("ui-resizable");
			if (that.ghost && that.helper) {
				that.helper.get(0).removeChild(that.ghost.get(0));
			}
		}

	});

	$.ui.plugin.add("resizable", "grid", {

		resize: function() {
			var that = $(this).data("ui-resizable"),
				o = that.options,
				cs = that.size,
				os = that.originalSize,
				op = that.originalPosition,
				a = that.axis,
				grid = typeof o.grid === "number" ? [o.grid, o.grid] : o.grid,
				gridX = (grid[0]||1),
				gridY = (grid[1]||1),
				ox = Math.round((cs.width - os.width) / gridX) * gridX,
				oy = Math.round((cs.height - os.height) / gridY) * gridY,
				newWidth = os.width + ox,
				newHeight = os.height + oy,
				isMaxWidth = o.maxWidth && (o.maxWidth < newWidth),
				isMaxHeight = o.maxHeight && (o.maxHeight < newHeight),
				isMinWidth = o.minWidth && (o.minWidth > newWidth),
				isMinHeight = o.minHeight && (o.minHeight > newHeight);

			o.grid = grid;

			if (isMinWidth) {
				newWidth = newWidth + gridX;
			}
			if (isMinHeight) {
				newHeight = newHeight + gridY;
			}
			if (isMaxWidth) {
				newWidth = newWidth - gridX;
			}
			if (isMaxHeight) {
				newHeight = newHeight - gridY;
			}

			if (/^(se|s|e)$/.test(a)) {
				that.size.width = newWidth;
				that.size.height = newHeight;
			} else if (/^(ne)$/.test(a)) {
				that.size.width = newWidth;
				that.size.height = newHeight;
				that.position.top = op.top - oy;
			} else if (/^(sw)$/.test(a)) {
				that.size.width = newWidth;
				that.size.height = newHeight;
				that.position.left = op.left - ox;
			} else {
				that.size.width = newWidth;
				that.size.height = newHeight;
				that.position.top = op.top - oy;
				that.position.left = op.left - ox;
			}
		}

	});

	})(jQuery);

	(function( $, undefined ) {

	$.widget("ui.selectable", $.ui.mouse, {
		version: "1.10.3",
		options: {
			appendTo: "body",
			autoRefresh: true,
			distance: 0,
			filter: "*",
			tolerance: "touch",

			// callbacks
			selected: null,
			selecting: null,
			start: null,
			stop: null,
			unselected: null,
			unselecting: null
		},
		_create: function() {
			var selectees,
				that = this;

			this.element.addClass("ui-selectable");

			this.dragged = false;

			// cache selectee children based on filter
			this.refresh = function() {
				selectees = $(that.options.filter, that.element[0]);
				selectees.addClass("ui-selectee");
				selectees.each(function() {
					var $this = $(this),
						pos = $this.offset();
					$.data(this, "selectable-item", {
						element: this,
						$element: $this,
						left: pos.left,
						top: pos.top,
						right: pos.left + $this.outerWidth(),
						bottom: pos.top + $this.outerHeight(),
						startselected: false,
						selected: $this.hasClass("ui-selected"),
						selecting: $this.hasClass("ui-selecting"),
						unselecting: $this.hasClass("ui-unselecting")
					});
				});
			};
			this.refresh();

			this.selectees = selectees.addClass("ui-selectee");

			this._mouseInit();

			this.helper = $("<div class='ui-selectable-helper'></div>");
		},

		_destroy: function() {
			this.selectees
				.removeClass("ui-selectee")
				.removeData("selectable-item");
			this.element
				.removeClass("ui-selectable ui-selectable-disabled");
			this._mouseDestroy();
		},

		_mouseStart: function(event) {
			var that = this,
				options = this.options;

			this.opos = [event.pageX, event.pageY];

			if (this.options.disabled) {
				return;
			}

			this.selectees = $(options.filter, this.element[0]);

			this._trigger("start", event);

			$(options.appendTo).append(this.helper);
			// position helper (lasso)
			this.helper.css({
				"left": event.pageX,
				"top": event.pageY,
				"width": 0,
				"height": 0
			});

			if (options.autoRefresh) {
				this.refresh();
			}

			this.selectees.filter(".ui-selected").each(function() {
				var selectee = $.data(this, "selectable-item");
				selectee.startselected = true;
				if (!event.metaKey && !event.ctrlKey) {
					selectee.$element.removeClass("ui-selected");
					selectee.selected = false;
					selectee.$element.addClass("ui-unselecting");
					selectee.unselecting = true;
					// selectable UNSELECTING callback
					that._trigger("unselecting", event, {
						unselecting: selectee.element
					});
				}
			});

			$(event.target).parents().addBack().each(function() {
				var doSelect,
					selectee = $.data(this, "selectable-item");
				if (selectee) {
					doSelect = (!event.metaKey && !event.ctrlKey) || !selectee.$element.hasClass("ui-selected");
					selectee.$element
						.removeClass(doSelect ? "ui-unselecting" : "ui-selected")
						.addClass(doSelect ? "ui-selecting" : "ui-unselecting");
					selectee.unselecting = !doSelect;
					selectee.selecting = doSelect;
					selectee.selected = doSelect;
					// selectable (UN)SELECTING callback
					if (doSelect) {
						that._trigger("selecting", event, {
							selecting: selectee.element
						});
					} else {
						that._trigger("unselecting", event, {
							unselecting: selectee.element
						});
					}
					return false;
				}
			});

		},

		_mouseDrag: function(event) {

			this.dragged = true;

			if (this.options.disabled) {
				return;
			}

			var tmp,
				that = this,
				options = this.options,
				x1 = this.opos[0],
				y1 = this.opos[1],
				x2 = event.pageX,
				y2 = event.pageY;

			if (x1 > x2) { tmp = x2; x2 = x1; x1 = tmp; }
			if (y1 > y2) { tmp = y2; y2 = y1; y1 = tmp; }
			this.helper.css({left: x1, top: y1, width: x2-x1, height: y2-y1});

			this.selectees.each(function() {
				var selectee = $.data(this, "selectable-item"),
					hit = false;

				//prevent helper from being selected if appendTo: selectable
				if (!selectee || selectee.element === that.element[0]) {
					return;
				}

				if (options.tolerance === "touch") {
					hit = ( !(selectee.left > x2 || selectee.right < x1 || selectee.top > y2 || selectee.bottom < y1) );
				} else if (options.tolerance === "fit") {
					hit = (selectee.left > x1 && selectee.right < x2 && selectee.top > y1 && selectee.bottom < y2);
				}

				if (hit) {
					// SELECT
					if (selectee.selected) {
						selectee.$element.removeClass("ui-selected");
						selectee.selected = false;
					}
					if (selectee.unselecting) {
						selectee.$element.removeClass("ui-unselecting");
						selectee.unselecting = false;
					}
					if (!selectee.selecting) {
						selectee.$element.addClass("ui-selecting");
						selectee.selecting = true;
						// selectable SELECTING callback
						that._trigger("selecting", event, {
							selecting: selectee.element
						});
					}
				} else {
					// UNSELECT
					if (selectee.selecting) {
						if ((event.metaKey || event.ctrlKey) && selectee.startselected) {
							selectee.$element.removeClass("ui-selecting");
							selectee.selecting = false;
							selectee.$element.addClass("ui-selected");
							selectee.selected = true;
						} else {
							selectee.$element.removeClass("ui-selecting");
							selectee.selecting = false;
							if (selectee.startselected) {
								selectee.$element.addClass("ui-unselecting");
								selectee.unselecting = true;
							}
							// selectable UNSELECTING callback
							that._trigger("unselecting", event, {
								unselecting: selectee.element
							});
						}
					}
					if (selectee.selected) {
						if (!event.metaKey && !event.ctrlKey && !selectee.startselected) {
							selectee.$element.removeClass("ui-selected");
							selectee.selected = false;

							selectee.$element.addClass("ui-unselecting");
							selectee.unselecting = true;
							// selectable UNSELECTING callback
							that._trigger("unselecting", event, {
								unselecting: selectee.element
							});
						}
					}
				}
			});

			return false;
		},

		_mouseStop: function(event) {
			var that = this;

			this.dragged = false;

			$(".ui-unselecting", this.element[0]).each(function() {
				var selectee = $.data(this, "selectable-item");
				selectee.$element.removeClass("ui-unselecting");
				selectee.unselecting = false;
				selectee.startselected = false;
				that._trigger("unselected", event, {
					unselected: selectee.element
				});
			});
			$(".ui-selecting", this.element[0]).each(function() {
				var selectee = $.data(this, "selectable-item");
				selectee.$element.removeClass("ui-selecting").addClass("ui-selected");
				selectee.selecting = false;
				selectee.selected = true;
				selectee.startselected = true;
				that._trigger("selected", event, {
					selected: selectee.element
				});
			});
			this._trigger("stop", event);

			this.helper.remove();

			return false;
		}

	});

	})(jQuery);

	(function( $, undefined ) {

	/*jshint loopfunc: true */

	function isOverAxis( x, reference, size ) {
		return ( x > reference ) && ( x < ( reference + size ) );
	}

	function isFloating(item) {
		return (/left|right/).test(item.css("float")) || (/inline|table-cell/).test(item.css("display"));
	}

	$.widget("ui.sortable", $.ui.mouse, {
		version: "1.10.3",
		widgetEventPrefix: "sort",
		ready: false,
		options: {
			appendTo: "parent",
			axis: false,
			connectWith: false,
			containment: false,
			cursor: "auto",
			cursorAt: false,
			dropOnEmpty: true,
			forcePlaceholderSize: false,
			forceHelperSize: false,
			grid: false,
			handle: false,
			helper: "original",
			items: "> *",
			opacity: false,
			placeholder: false,
			revert: false,
			scroll: true,
			scrollSensitivity: 20,
			scrollSpeed: 20,
			scope: "default",
			tolerance: "intersect",
			zIndex: 1000,

			// callbacks
			activate: null,
			beforeStop: null,
			change: null,
			deactivate: null,
			out: null,
			over: null,
			receive: null,
			remove: null,
			sort: null,
			start: null,
			stop: null,
			update: null
		},
		_create: function() {

			var o = this.options;
			this.containerCache = {};
			this.element.addClass("ui-sortable");

			//Get the items
			this.refresh();

			//Let's determine if the items are being displayed horizontally
			this.floating = this.items.length ? o.axis === "x" || isFloating(this.items[0].item) : false;

			//Let's determine the parent's offset
			this.offset = this.element.offset();

			//Initialize mouse events for interaction
			this._mouseInit();

			//We're ready to go
			this.ready = true;

		},

		_destroy: function() {
			this.element
				.removeClass("ui-sortable ui-sortable-disabled");
			this._mouseDestroy();

			for ( var i = this.items.length - 1; i >= 0; i-- ) {
				this.items[i].item.removeData(this.widgetName + "-item");
			}

			return this;
		},

		_setOption: function(key, value){
			if ( key === "disabled" ) {
				this.options[ key ] = value;

				this.widget().toggleClass( "ui-sortable-disabled", !!value );
			} else {
				// Don't call widget base _setOption for disable as it adds ui-state-disabled class
				$.Widget.prototype._setOption.apply(this, arguments);
			}
		},

		_mouseCapture: function(event, overrideHandle) {
			var currentItem = null,
				validHandle = false,
				that = this;

			if (this.reverting) {
				return false;
			}

			if(this.options.disabled || this.options.type === "static") {
				return false;
			}

			//We have to refresh the items data once first
			this._refreshItems(event);

			//Find out if the clicked node (or one of its parents) is a actual item in this.items
			$(event.target).parents().each(function() {
				if($.data(this, that.widgetName + "-item") === that) {
					currentItem = $(this);
					return false;
				}
			});
			if($.data(event.target, that.widgetName + "-item") === that) {
				currentItem = $(event.target);
			}

			if(!currentItem) {
				return false;
			}
			if(this.options.handle && !overrideHandle) {
				$(this.options.handle, currentItem).find("*").addBack().each(function() {
					if(this === event.target) {
						validHandle = true;
					}
				});
				if(!validHandle) {
					return false;
				}
			}

			this.currentItem = currentItem;
			this._removeCurrentsFromItems();
			return true;

		},

		_mouseStart: function(event, overrideHandle, noActivation) {

			var i, body,
				o = this.options;

			this.currentContainer = this;

			//We only need to call refreshPositions, because the refreshItems call has been moved to mouseCapture
			this.refreshPositions();

			//Create and append the visible helper
			this.helper = this._createHelper(event);

			//Cache the helper size
			this._cacheHelperProportions();

			/*
			 * - Position generation -
			 * This block generates everything position related - it's the core of draggables.
			 */

			//Cache the margins of the original element
			this._cacheMargins();

			//Get the next scrolling parent
			this.scrollParent = this.helper.scrollParent();

			//The element's absolute position on the page minus margins
			this.offset = this.currentItem.offset();
			this.offset = {
				top: this.offset.top - this.margins.top,
				left: this.offset.left - this.margins.left
			};

			$.extend(this.offset, {
				click: { //Where the click happened, relative to the element
					left: event.pageX - this.offset.left,
					top: event.pageY - this.offset.top
				},
				parent: this._getParentOffset(),
				relative: this._getRelativeOffset() //This is a relative to absolute position minus the actual position calculation - only used for relative positioned helper
			});

			// Only after we got the offset, we can change the helper's position to absolute
			// TODO: Still need to figure out a way to make relative sorting possible
			this.helper.css("position", "absolute");
			this.cssPosition = this.helper.css("position");

			//Generate the original position
			this.originalPosition = this._generatePosition(event);
			this.originalPageX = event.pageX;
			this.originalPageY = event.pageY;

			//Adjust the mouse offset relative to the helper if "cursorAt" is supplied
			(o.cursorAt && this._adjustOffsetFromHelper(o.cursorAt));

			//Cache the former DOM position
			this.domPosition = { prev: this.currentItem.prev()[0], parent: this.currentItem.parent()[0] };

			//If the helper is not the original, hide the original so it's not playing any role during the drag, won't cause anything bad this way
			if(this.helper[0] !== this.currentItem[0]) {
				this.currentItem.hide();
			}

			//Create the placeholder
			this._createPlaceholder();

			//Set a containment if given in the options
			if(o.containment) {
				this._setContainment();
			}

			if( o.cursor && o.cursor !== "auto" ) { // cursor option
				body = this.document.find( "body" );

				// support: IE
				this.storedCursor = body.css( "cursor" );
				body.css( "cursor", o.cursor );

				this.storedStylesheet = $( "<style>*{ cursor: "+o.cursor+" !important; }</style>" ).appendTo( body );
			}

			if(o.opacity) { // opacity option
				if (this.helper.css("opacity")) {
					this._storedOpacity = this.helper.css("opacity");
				}
				this.helper.css("opacity", o.opacity);
			}

			if(o.zIndex) { // zIndex option
				if (this.helper.css("zIndex")) {
					this._storedZIndex = this.helper.css("zIndex");
				}
				this.helper.css("zIndex", o.zIndex);
			}

			//Prepare scrolling
			if(this.scrollParent[0] !== document && this.scrollParent[0].tagName !== "HTML") {
				this.overflowOffset = this.scrollParent.offset();
			}

			//Call callbacks
			this._trigger("start", event, this._uiHash());

			//Recache the helper size
			if(!this._preserveHelperProportions) {
				this._cacheHelperProportions();
			}


			//Post "activate" events to possible containers
			if( !noActivation ) {
				for ( i = this.containers.length - 1; i >= 0; i-- ) {
					this.containers[ i ]._trigger( "activate", event, this._uiHash( this ) );
				}
			}

			//Prepare possible droppables
			if($.ui.ddmanager) {
				$.ui.ddmanager.current = this;
			}

			if ($.ui.ddmanager && !o.dropBehaviour) {
				$.ui.ddmanager.prepareOffsets(this, event);
			}

			this.dragging = true;

			this.helper.addClass("ui-sortable-helper");
			this._mouseDrag(event); //Execute the drag once - this causes the helper not to be visible before getting its correct position
			return true;

		},

		_mouseDrag: function(event) {
			var i, item, itemElement, intersection,
				o = this.options,
				scrolled = false;

			//Compute the helpers position
			this.position = this._generatePosition(event);
			this.positionAbs = this._convertPositionTo("absolute");

			if (!this.lastPositionAbs) {
				this.lastPositionAbs = this.positionAbs;
			}

			//Do scrolling
			if(this.options.scroll) {
				if(this.scrollParent[0] !== document && this.scrollParent[0].tagName !== "HTML") {

					if((this.overflowOffset.top + this.scrollParent[0].offsetHeight) - event.pageY < o.scrollSensitivity) {
						this.scrollParent[0].scrollTop = scrolled = this.scrollParent[0].scrollTop + o.scrollSpeed;
					} else if(event.pageY - this.overflowOffset.top < o.scrollSensitivity) {
						this.scrollParent[0].scrollTop = scrolled = this.scrollParent[0].scrollTop - o.scrollSpeed;
					}

					if((this.overflowOffset.left + this.scrollParent[0].offsetWidth) - event.pageX < o.scrollSensitivity) {
						this.scrollParent[0].scrollLeft = scrolled = this.scrollParent[0].scrollLeft + o.scrollSpeed;
					} else if(event.pageX - this.overflowOffset.left < o.scrollSensitivity) {
						this.scrollParent[0].scrollLeft = scrolled = this.scrollParent[0].scrollLeft - o.scrollSpeed;
					}

				} else {

					if(event.pageY - $(document).scrollTop() < o.scrollSensitivity) {
						scrolled = $(document).scrollTop($(document).scrollTop() - o.scrollSpeed);
					} else if($(window).height() - (event.pageY - $(document).scrollTop()) < o.scrollSensitivity) {
						scrolled = $(document).scrollTop($(document).scrollTop() + o.scrollSpeed);
					}

					if(event.pageX - $(document).scrollLeft() < o.scrollSensitivity) {
						scrolled = $(document).scrollLeft($(document).scrollLeft() - o.scrollSpeed);
					} else if($(window).width() - (event.pageX - $(document).scrollLeft()) < o.scrollSensitivity) {
						scrolled = $(document).scrollLeft($(document).scrollLeft() + o.scrollSpeed);
					}

				}

				if(scrolled !== false && $.ui.ddmanager && !o.dropBehaviour) {
					$.ui.ddmanager.prepareOffsets(this, event);
				}
			}

			//Regenerate the absolute position used for position checks
			this.positionAbs = this._convertPositionTo("absolute");

			//Set the helper position
			if(!this.options.axis || this.options.axis !== "y") {
				this.helper[0].style.left = this.position.left+"px";
			}
			if(!this.options.axis || this.options.axis !== "x") {
				this.helper[0].style.top = this.position.top+"px";
			}

			//Rearrange
			for (i = this.items.length - 1; i >= 0; i--) {

				//Cache variables and intersection, continue if no intersection
				item = this.items[i];
				itemElement = item.item[0];
				intersection = this._intersectsWithPointer(item);
				if (!intersection) {
					continue;
				}

				// Only put the placeholder inside the current Container, skip all
				// items form other containers. This works because when moving
				// an item from one container to another the
				// currentContainer is switched before the placeholder is moved.
				//
				// Without this moving items in "sub-sortables" can cause the placeholder to jitter
				// beetween the outer and inner container.
				if (item.instance !== this.currentContainer) {
					continue;
				}

				// cannot intersect with itself
				// no useless actions that have been done before
				// no action if the item moved is the parent of the item checked
				if (itemElement !== this.currentItem[0] &&
					this.placeholder[intersection === 1 ? "next" : "prev"]()[0] !== itemElement &&
					!$.contains(this.placeholder[0], itemElement) &&
					(this.options.type === "semi-dynamic" ? !$.contains(this.element[0], itemElement) : true)
				) {

					this.direction = intersection === 1 ? "down" : "up";

					if (this.options.tolerance === "pointer" || this._intersectsWithSides(item)) {
						this._rearrange(event, item);
					} else {
						break;
					}

					this._trigger("change", event, this._uiHash());
					break;
				}
			}

			//Post events to containers
			this._contactContainers(event);

			//Interconnect with droppables
			if($.ui.ddmanager) {
				$.ui.ddmanager.drag(this, event);
			}

			//Call callbacks
			this._trigger("sort", event, this._uiHash());

			this.lastPositionAbs = this.positionAbs;
			return false;

		},

		_mouseStop: function(event, noPropagation) {

			if(!event) {
				return;
			}

			//If we are using droppables, inform the manager about the drop
			if ($.ui.ddmanager && !this.options.dropBehaviour) {
				$.ui.ddmanager.drop(this, event);
			}

			if(this.options.revert) {
				var that = this,
					cur = this.placeholder.offset(),
					axis = this.options.axis,
					animation = {};

				if ( !axis || axis === "x" ) {
					animation.left = cur.left - this.offset.parent.left - this.margins.left + (this.offsetParent[0] === document.body ? 0 : this.offsetParent[0].scrollLeft);
				}
				if ( !axis || axis === "y" ) {
					animation.top = cur.top - this.offset.parent.top - this.margins.top + (this.offsetParent[0] === document.body ? 0 : this.offsetParent[0].scrollTop);
				}
				this.reverting = true;
				$(this.helper).animate( animation, parseInt(this.options.revert, 10) || 500, function() {
					that._clear(event);
				});
			} else {
				this._clear(event, noPropagation);
			}

			return false;

		},

		cancel: function() {

			if(this.dragging) {

				this._mouseUp({ target: null });

				if(this.options.helper === "original") {
					this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper");
				} else {
					this.currentItem.show();
				}

				//Post deactivating events to containers
				for (var i = this.containers.length - 1; i >= 0; i--){
					this.containers[i]._trigger("deactivate", null, this._uiHash(this));
					if(this.containers[i].containerCache.over) {
						this.containers[i]._trigger("out", null, this._uiHash(this));
						this.containers[i].containerCache.over = 0;
					}
				}

			}

			if (this.placeholder) {
				//$(this.placeholder[0]).remove(); would have been the jQuery way - unfortunately, it unbinds ALL events from the original node!
				if(this.placeholder[0].parentNode) {
					this.placeholder[0].parentNode.removeChild(this.placeholder[0]);
				}
				if(this.options.helper !== "original" && this.helper && this.helper[0].parentNode) {
					this.helper.remove();
				}

				$.extend(this, {
					helper: null,
					dragging: false,
					reverting: false,
					_noFinalSort: null
				});

				if(this.domPosition.prev) {
					$(this.domPosition.prev).after(this.currentItem);
				} else {
					$(this.domPosition.parent).prepend(this.currentItem);
				}
			}

			return this;

		},

		serialize: function(o) {

			var items = this._getItemsAsjQuery(o && o.connected),
				str = [];
			o = o || {};

			$(items).each(function() {
				var res = ($(o.item || this).attr(o.attribute || "id") || "").match(o.expression || (/(.+)[\-=_](.+)/));
				if (res) {
					str.push((o.key || res[1]+"[]")+"="+(o.key && o.expression ? res[1] : res[2]));
				}
			});

			if(!str.length && o.key) {
				str.push(o.key + "=");
			}

			return str.join("&");

		},

		toArray: function(o) {

			var items = this._getItemsAsjQuery(o && o.connected),
				ret = [];

			o = o || {};

			items.each(function() { ret.push($(o.item || this).attr(o.attribute || "id") || ""); });
			return ret;

		},

		/* Be careful with the following core functions */
		_intersectsWith: function(item) {

			var x1 = this.positionAbs.left,
				x2 = x1 + this.helperProportions.width,
				y1 = this.positionAbs.top,
				y2 = y1 + this.helperProportions.height,
				l = item.left,
				r = l + item.width,
				t = item.top,
				b = t + item.height,
				dyClick = this.offset.click.top,
				dxClick = this.offset.click.left,
				isOverElementHeight = ( this.options.axis === "x" ) || ( ( y1 + dyClick ) > t && ( y1 + dyClick ) < b ),
				isOverElementWidth = ( this.options.axis === "y" ) || ( ( x1 + dxClick ) > l && ( x1 + dxClick ) < r ),
				isOverElement = isOverElementHeight && isOverElementWidth;

			if ( this.options.tolerance === "pointer" ||
				this.options.forcePointerForContainers ||
				(this.options.tolerance !== "pointer" && this.helperProportions[this.floating ? "width" : "height"] > item[this.floating ? "width" : "height"])
			) {
				return isOverElement;
			} else {

				return (l < x1 + (this.helperProportions.width / 2) && // Right Half
					x2 - (this.helperProportions.width / 2) < r && // Left Half
					t < y1 + (this.helperProportions.height / 2) && // Bottom Half
					y2 - (this.helperProportions.height / 2) < b ); // Top Half

			}
		},

		_intersectsWithPointer: function(item) {

			var isOverElementHeight = (this.options.axis === "x") || isOverAxis(this.positionAbs.top + this.offset.click.top, item.top, item.height),
				isOverElementWidth = (this.options.axis === "y") || isOverAxis(this.positionAbs.left + this.offset.click.left, item.left, item.width),
				isOverElement = isOverElementHeight && isOverElementWidth,
				verticalDirection = this._getDragVerticalDirection(),
				horizontalDirection = this._getDragHorizontalDirection();

			if (!isOverElement) {
				return false;
			}

			return this.floating ?
				( ((horizontalDirection && horizontalDirection === "right") || verticalDirection === "down") ? 2 : 1 )
				: ( verticalDirection && (verticalDirection === "down" ? 2 : 1) );

		},

		_intersectsWithSides: function(item) {

			var isOverBottomHalf = isOverAxis(this.positionAbs.top + this.offset.click.top, item.top + (item.height/2), item.height),
				isOverRightHalf = isOverAxis(this.positionAbs.left + this.offset.click.left, item.left + (item.width/2), item.width),
				verticalDirection = this._getDragVerticalDirection(),
				horizontalDirection = this._getDragHorizontalDirection();

			if (this.floating && horizontalDirection) {
				return ((horizontalDirection === "right" && isOverRightHalf) || (horizontalDirection === "left" && !isOverRightHalf));
			} else {
				return verticalDirection && ((verticalDirection === "down" && isOverBottomHalf) || (verticalDirection === "up" && !isOverBottomHalf));
			}

		},

		_getDragVerticalDirection: function() {
			var delta = this.positionAbs.top - this.lastPositionAbs.top;
			return delta !== 0 && (delta > 0 ? "down" : "up");
		},

		_getDragHorizontalDirection: function() {
			var delta = this.positionAbs.left - this.lastPositionAbs.left;
			return delta !== 0 && (delta > 0 ? "right" : "left");
		},

		refresh: function(event) {
			this._refreshItems(event);
			this.refreshPositions();
			return this;
		},

		_connectWith: function() {
			var options = this.options;
			return options.connectWith.constructor === String ? [options.connectWith] : options.connectWith;
		},

		_getItemsAsjQuery: function(connected) {

			var i, j, cur, inst,
				items = [],
				queries = [],
				connectWith = this._connectWith();

			if(connectWith && connected) {
				for (i = connectWith.length - 1; i >= 0; i--){
					cur = $(connectWith[i]);
					for ( j = cur.length - 1; j >= 0; j--){
						inst = $.data(cur[j], this.widgetFullName);
						if(inst && inst !== this && !inst.options.disabled) {
							queries.push([$.isFunction(inst.options.items) ? inst.options.items.call(inst.element) : $(inst.options.items, inst.element).not(".ui-sortable-helper").not(".ui-sortable-placeholder"), inst]);
						}
					}
				}
			}

			queries.push([$.isFunction(this.options.items) ? this.options.items.call(this.element, null, { options: this.options, item: this.currentItem }) : $(this.options.items, this.element).not(".ui-sortable-helper").not(".ui-sortable-placeholder"), this]);

			for (i = queries.length - 1; i >= 0; i--){
				queries[i][0].each(function() {
					items.push(this);
				});
			}

			return $(items);

		},

		_removeCurrentsFromItems: function() {

			var list = this.currentItem.find(":data(" + this.widgetName + "-item)");

			this.items = $.grep(this.items, function (item) {
				for (var j=0; j < list.length; j++) {
					if(list[j] === item.item[0]) {
						return false;
					}
				}
				return true;
			});

		},

		_refreshItems: function(event) {

			this.items = [];
			this.containers = [this];

			var i, j, cur, inst, targetData, _queries, item, queriesLength,
				items = this.items,
				queries = [[$.isFunction(this.options.items) ? this.options.items.call(this.element[0], event, { item: this.currentItem }) : $(this.options.items, this.element), this]],
				connectWith = this._connectWith();

			if(connectWith && this.ready) { //Shouldn't be run the first time through due to massive slow-down
				for (i = connectWith.length - 1; i >= 0; i--){
					cur = $(connectWith[i]);
					for (j = cur.length - 1; j >= 0; j--){
						inst = $.data(cur[j], this.widgetFullName);
						if(inst && inst !== this && !inst.options.disabled) {
							queries.push([$.isFunction(inst.options.items) ? inst.options.items.call(inst.element[0], event, { item: this.currentItem }) : $(inst.options.items, inst.element), inst]);
							this.containers.push(inst);
						}
					}
				}
			}

			for (i = queries.length - 1; i >= 0; i--) {
				targetData = queries[i][1];
				_queries = queries[i][0];

				for (j=0, queriesLength = _queries.length; j < queriesLength; j++) {
					item = $(_queries[j]);

					item.data(this.widgetName + "-item", targetData); // Data for target checking (mouse manager)

					items.push({
						item: item,
						instance: targetData,
						width: 0, height: 0,
						left: 0, top: 0
					});
				}
			}

		},

		refreshPositions: function(fast) {

			//This has to be redone because due to the item being moved out/into the offsetParent, the offsetParent's position will change
			if(this.offsetParent && this.helper) {
				this.offset.parent = this._getParentOffset();
			}

			var i, item, t, p;

			for (i = this.items.length - 1; i >= 0; i--){
				item = this.items[i];

				//We ignore calculating positions of all connected containers when we're not over them
				if(item.instance !== this.currentContainer && this.currentContainer && item.item[0] !== this.currentItem[0]) {
					continue;
				}

				t = this.options.toleranceElement ? $(this.options.toleranceElement, item.item) : item.item;

				if (!fast) {
					item.width = t.outerWidth();
					item.height = t.outerHeight();
				}

				p = t.offset();
				item.left = p.left;
				item.top = p.top;
			}

			if(this.options.custom && this.options.custom.refreshContainers) {
				this.options.custom.refreshContainers.call(this);
			} else {
				for (i = this.containers.length - 1; i >= 0; i--){
					p = this.containers[i].element.offset();
					this.containers[i].containerCache.left = p.left;
					this.containers[i].containerCache.top = p.top;
					this.containers[i].containerCache.width	= this.containers[i].element.outerWidth();
					this.containers[i].containerCache.height = this.containers[i].element.outerHeight();
				}
			}

			return this;
		},

		_createPlaceholder: function(that) {
			that = that || this;
			var className,
				o = that.options;

			if(!o.placeholder || o.placeholder.constructor === String) {
				className = o.placeholder;
				o.placeholder = {
					element: function() {

						var nodeName = that.currentItem[0].nodeName.toLowerCase(),
							element = $( "<" + nodeName + ">", that.document[0] )
								.addClass(className || that.currentItem[0].className+" ui-sortable-placeholder")
								.removeClass("ui-sortable-helper");

						if ( nodeName === "tr" ) {
							that.currentItem.children().each(function() {
								$( "<td>&#160;</td>", that.document[0] )
									.attr( "colspan", $( this ).attr( "colspan" ) || 1 )
									.appendTo( element );
							});
						} else if ( nodeName === "img" ) {
							element.attr( "src", that.currentItem.attr( "src" ) );
						}

						if ( !className ) {
							element.css( "visibility", "hidden" );
						}

						return element;
					},
					update: function(container, p) {

						// 1. If a className is set as 'placeholder option, we don't force sizes - the class is responsible for that
						// 2. The option 'forcePlaceholderSize can be enabled to force it even if a class name is specified
						if(className && !o.forcePlaceholderSize) {
							return;
						}

						//If the element doesn't have a actual height by itself (without styles coming from a stylesheet), it receives the inline height from the dragged item
						if(!p.height()) { p.height(that.currentItem.innerHeight() - parseInt(that.currentItem.css("paddingTop")||0, 10) - parseInt(that.currentItem.css("paddingBottom")||0, 10)); }
						if(!p.width()) { p.width(that.currentItem.innerWidth() - parseInt(that.currentItem.css("paddingLeft")||0, 10) - parseInt(that.currentItem.css("paddingRight")||0, 10)); }
					}
				};
			}

			//Create the placeholder
			that.placeholder = $(o.placeholder.element.call(that.element, that.currentItem));

			//Append it after the actual current item
			that.currentItem.after(that.placeholder);

			//Update the size of the placeholder (TODO: Logic to fuzzy, see line 316/317)
			o.placeholder.update(that, that.placeholder);

		},

		_contactContainers: function(event) {
			var i, j, dist, itemWithLeastDistance, posProperty, sizeProperty, base, cur, nearBottom, floating,
				innermostContainer = null,
				innermostIndex = null;

			// get innermost container that intersects with item
			for (i = this.containers.length - 1; i >= 0; i--) {

				// never consider a container that's located within the item itself
				if($.contains(this.currentItem[0], this.containers[i].element[0])) {
					continue;
				}

				if(this._intersectsWith(this.containers[i].containerCache)) {

					// if we've already found a container and it's more "inner" than this, then continue
					if(innermostContainer && $.contains(this.containers[i].element[0], innermostContainer.element[0])) {
						continue;
					}

					innermostContainer = this.containers[i];
					innermostIndex = i;

				} else {
					// container doesn't intersect. trigger "out" event if necessary
					if(this.containers[i].containerCache.over) {
						this.containers[i]._trigger("out", event, this._uiHash(this));
						this.containers[i].containerCache.over = 0;
					}
				}

			}

			// if no intersecting containers found, return
			if(!innermostContainer) {
				return;
			}

			// move the item into the container if it's not there already
			if(this.containers.length === 1) {
				if (!this.containers[innermostIndex].containerCache.over) {
					this.containers[innermostIndex]._trigger("over", event, this._uiHash(this));
					this.containers[innermostIndex].containerCache.over = 1;
				}
			} else {

				//When entering a new container, we will find the item with the least distance and append our item near it
				dist = 10000;
				itemWithLeastDistance = null;
				floating = innermostContainer.floating || isFloating(this.currentItem);
				posProperty = floating ? "left" : "top";
				sizeProperty = floating ? "width" : "height";
				base = this.positionAbs[posProperty] + this.offset.click[posProperty];
				for (j = this.items.length - 1; j >= 0; j--) {
					if(!$.contains(this.containers[innermostIndex].element[0], this.items[j].item[0])) {
						continue;
					}
					if(this.items[j].item[0] === this.currentItem[0]) {
						continue;
					}
					if (floating && !isOverAxis(this.positionAbs.top + this.offset.click.top, this.items[j].top, this.items[j].height)) {
						continue;
					}
					cur = this.items[j].item.offset()[posProperty];
					nearBottom = false;
					if(Math.abs(cur - base) > Math.abs(cur + this.items[j][sizeProperty] - base)){
						nearBottom = true;
						cur += this.items[j][sizeProperty];
					}

					if(Math.abs(cur - base) < dist) {
						dist = Math.abs(cur - base); itemWithLeastDistance = this.items[j];
						this.direction = nearBottom ? "up": "down";
					}
				}

				//Check if dropOnEmpty is enabled
				if(!itemWithLeastDistance && !this.options.dropOnEmpty) {
					return;
				}

				if(this.currentContainer === this.containers[innermostIndex]) {
					return;
				}

				itemWithLeastDistance ? this._rearrange(event, itemWithLeastDistance, null, true) : this._rearrange(event, null, this.containers[innermostIndex].element, true);
				this._trigger("change", event, this._uiHash());
				this.containers[innermostIndex]._trigger("change", event, this._uiHash(this));
				this.currentContainer = this.containers[innermostIndex];

				//Update the placeholder
				this.options.placeholder.update(this.currentContainer, this.placeholder);

				this.containers[innermostIndex]._trigger("over", event, this._uiHash(this));
				this.containers[innermostIndex].containerCache.over = 1;
			}


		},

		_createHelper: function(event) {

			var o = this.options,
				helper = $.isFunction(o.helper) ? $(o.helper.apply(this.element[0], [event, this.currentItem])) : (o.helper === "clone" ? this.currentItem.clone() : this.currentItem);

			//Add the helper to the DOM if that didn't happen already
			if(!helper.parents("body").length) {
				$(o.appendTo !== "parent" ? o.appendTo : this.currentItem[0].parentNode)[0].appendChild(helper[0]);
			}

			if(helper[0] === this.currentItem[0]) {
				this._storedCSS = { width: this.currentItem[0].style.width, height: this.currentItem[0].style.height, position: this.currentItem.css("position"), top: this.currentItem.css("top"), left: this.currentItem.css("left") };
			}

			if(!helper[0].style.width || o.forceHelperSize) {
				helper.width(this.currentItem.width());
			}
			if(!helper[0].style.height || o.forceHelperSize) {
				helper.height(this.currentItem.height());
			}

			return helper;

		},

		_adjustOffsetFromHelper: function(obj) {
			if (typeof obj === "string") {
				obj = obj.split(" ");
			}
			if ($.isArray(obj)) {
				obj = {left: +obj[0], top: +obj[1] || 0};
			}
			if ("left" in obj) {
				this.offset.click.left = obj.left + this.margins.left;
			}
			if ("right" in obj) {
				this.offset.click.left = this.helperProportions.width - obj.right + this.margins.left;
			}
			if ("top" in obj) {
				this.offset.click.top = obj.top + this.margins.top;
			}
			if ("bottom" in obj) {
				this.offset.click.top = this.helperProportions.height - obj.bottom + this.margins.top;
			}
		},

		_getParentOffset: function() {


			//Get the offsetParent and cache its position
			this.offsetParent = this.helper.offsetParent();
			var po = this.offsetParent.offset();

			// This is a special case where we need to modify a offset calculated on start, since the following happened:
			// 1. The position of the helper is absolute, so it's position is calculated based on the next positioned parent
			// 2. The actual offset parent is a child of the scroll parent, and the scroll parent isn't the document, which means that
			//    the scroll is included in the initial calculation of the offset of the parent, and never recalculated upon drag
			if(this.cssPosition === "absolute" && this.scrollParent[0] !== document && $.contains(this.scrollParent[0], this.offsetParent[0])) {
				po.left += this.scrollParent.scrollLeft();
				po.top += this.scrollParent.scrollTop();
			}

			// This needs to be actually done for all browsers, since pageX/pageY includes this information
			// with an ugly IE fix
			if( this.offsetParent[0] === document.body || (this.offsetParent[0].tagName && this.offsetParent[0].tagName.toLowerCase() === "html" && $.ui.ie)) {
				po = { top: 0, left: 0 };
			}

			return {
				top: po.top + (parseInt(this.offsetParent.css("borderTopWidth"),10) || 0),
				left: po.left + (parseInt(this.offsetParent.css("borderLeftWidth"),10) || 0)
			};

		},

		_getRelativeOffset: function() {

			if(this.cssPosition === "relative") {
				var p = this.currentItem.position();
				return {
					top: p.top - (parseInt(this.helper.css("top"),10) || 0) + this.scrollParent.scrollTop(),
					left: p.left - (parseInt(this.helper.css("left"),10) || 0) + this.scrollParent.scrollLeft()
				};
			} else {
				return { top: 0, left: 0 };
			}

		},

		_cacheMargins: function() {
			this.margins = {
				left: (parseInt(this.currentItem.css("marginLeft"),10) || 0),
				top: (parseInt(this.currentItem.css("marginTop"),10) || 0)
			};
		},

		_cacheHelperProportions: function() {
			this.helperProportions = {
				width: this.helper.outerWidth(),
				height: this.helper.outerHeight()
			};
		},

		_setContainment: function() {

			var ce, co, over,
				o = this.options;
			if(o.containment === "parent") {
				o.containment = this.helper[0].parentNode;
			}
			if(o.containment === "document" || o.containment === "window") {
				this.containment = [
					0 - this.offset.relative.left - this.offset.parent.left,
					0 - this.offset.relative.top - this.offset.parent.top,
					$(o.containment === "document" ? document : window).width() - this.helperProportions.width - this.margins.left,
					($(o.containment === "document" ? document : window).height() || document.body.parentNode.scrollHeight) - this.helperProportions.height - this.margins.top
				];
			}

			if(!(/^(document|window|parent)$/).test(o.containment)) {
				ce = $(o.containment)[0];
				co = $(o.containment).offset();
				over = ($(ce).css("overflow") !== "hidden");

				this.containment = [
					co.left + (parseInt($(ce).css("borderLeftWidth"),10) || 0) + (parseInt($(ce).css("paddingLeft"),10) || 0) - this.margins.left,
					co.top + (parseInt($(ce).css("borderTopWidth"),10) || 0) + (parseInt($(ce).css("paddingTop"),10) || 0) - this.margins.top,
					co.left+(over ? Math.max(ce.scrollWidth,ce.offsetWidth) : ce.offsetWidth) - (parseInt($(ce).css("borderLeftWidth"),10) || 0) - (parseInt($(ce).css("paddingRight"),10) || 0) - this.helperProportions.width - this.margins.left,
					co.top+(over ? Math.max(ce.scrollHeight,ce.offsetHeight) : ce.offsetHeight) - (parseInt($(ce).css("borderTopWidth"),10) || 0) - (parseInt($(ce).css("paddingBottom"),10) || 0) - this.helperProportions.height - this.margins.top
				];
			}

		},

		_convertPositionTo: function(d, pos) {

			if(!pos) {
				pos = this.position;
			}
			var mod = d === "absolute" ? 1 : -1,
				scroll = this.cssPosition === "absolute" && !(this.scrollParent[0] !== document && $.contains(this.scrollParent[0], this.offsetParent[0])) ? this.offsetParent : this.scrollParent,
				scrollIsRootNode = (/(html|body)/i).test(scroll[0].tagName);

			return {
				top: (
					pos.top	+																// The absolute mouse position
					this.offset.relative.top * mod +										// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.top * mod -											// The offsetParent's offset without borders (offset + border)
					( ( this.cssPosition === "fixed" ? -this.scrollParent.scrollTop() : ( scrollIsRootNode ? 0 : scroll.scrollTop() ) ) * mod)
				),
				left: (
					pos.left +																// The absolute mouse position
					this.offset.relative.left * mod +										// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.left * mod	-										// The offsetParent's offset without borders (offset + border)
					( ( this.cssPosition === "fixed" ? -this.scrollParent.scrollLeft() : scrollIsRootNode ? 0 : scroll.scrollLeft() ) * mod)
				)
			};

		},

		_generatePosition: function(event) {

			var top, left,
				o = this.options,
				pageX = event.pageX,
				pageY = event.pageY,
				scroll = this.cssPosition === "absolute" && !(this.scrollParent[0] !== document && $.contains(this.scrollParent[0], this.offsetParent[0])) ? this.offsetParent : this.scrollParent, scrollIsRootNode = (/(html|body)/i).test(scroll[0].tagName);

			// This is another very weird special case that only happens for relative elements:
			// 1. If the css position is relative
			// 2. and the scroll parent is the document or similar to the offset parent
			// we have to refresh the relative offset during the scroll so there are no jumps
			if(this.cssPosition === "relative" && !(this.scrollParent[0] !== document && this.scrollParent[0] !== this.offsetParent[0])) {
				this.offset.relative = this._getRelativeOffset();
			}

			/*
			 * - Position constraining -
			 * Constrain the position to a mix of grid, containment.
			 */

			if(this.originalPosition) { //If we are not dragging yet, we won't check for options

				if(this.containment) {
					if(event.pageX - this.offset.click.left < this.containment[0]) {
						pageX = this.containment[0] + this.offset.click.left;
					}
					if(event.pageY - this.offset.click.top < this.containment[1]) {
						pageY = this.containment[1] + this.offset.click.top;
					}
					if(event.pageX - this.offset.click.left > this.containment[2]) {
						pageX = this.containment[2] + this.offset.click.left;
					}
					if(event.pageY - this.offset.click.top > this.containment[3]) {
						pageY = this.containment[3] + this.offset.click.top;
					}
				}

				if(o.grid) {
					top = this.originalPageY + Math.round((pageY - this.originalPageY) / o.grid[1]) * o.grid[1];
					pageY = this.containment ? ( (top - this.offset.click.top >= this.containment[1] && top - this.offset.click.top <= this.containment[3]) ? top : ((top - this.offset.click.top >= this.containment[1]) ? top - o.grid[1] : top + o.grid[1])) : top;

					left = this.originalPageX + Math.round((pageX - this.originalPageX) / o.grid[0]) * o.grid[0];
					pageX = this.containment ? ( (left - this.offset.click.left >= this.containment[0] && left - this.offset.click.left <= this.containment[2]) ? left : ((left - this.offset.click.left >= this.containment[0]) ? left - o.grid[0] : left + o.grid[0])) : left;
				}

			}

			return {
				top: (
					pageY -																// The absolute mouse position
					this.offset.click.top -													// Click offset (relative to the element)
					this.offset.relative.top	-											// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.top +												// The offsetParent's offset without borders (offset + border)
					( ( this.cssPosition === "fixed" ? -this.scrollParent.scrollTop() : ( scrollIsRootNode ? 0 : scroll.scrollTop() ) ))
				),
				left: (
					pageX -																// The absolute mouse position
					this.offset.click.left -												// Click offset (relative to the element)
					this.offset.relative.left	-											// Only for relative positioned nodes: Relative offset from element to offset parent
					this.offset.parent.left +												// The offsetParent's offset without borders (offset + border)
					( ( this.cssPosition === "fixed" ? -this.scrollParent.scrollLeft() : scrollIsRootNode ? 0 : scroll.scrollLeft() ))
				)
			};

		},

		_rearrange: function(event, i, a, hardRefresh) {

			a ? a[0].appendChild(this.placeholder[0]) : i.item[0].parentNode.insertBefore(this.placeholder[0], (this.direction === "down" ? i.item[0] : i.item[0].nextSibling));

			//Various things done here to improve the performance:
			// 1. we create a setTimeout, that calls refreshPositions
			// 2. on the instance, we have a counter variable, that get's higher after every append
			// 3. on the local scope, we copy the counter variable, and check in the timeout, if it's still the same
			// 4. this lets only the last addition to the timeout stack through
			this.counter = this.counter ? ++this.counter : 1;
			var counter = this.counter;

			this._delay(function() {
				if(counter === this.counter) {
					this.refreshPositions(!hardRefresh); //Precompute after each DOM insertion, NOT on mousemove
				}
			});

		},

		_clear: function(event, noPropagation) {

			this.reverting = false;
			// We delay all events that have to be triggered to after the point where the placeholder has been removed and
			// everything else normalized again
			var i,
				delayedTriggers = [];

			// We first have to update the dom position of the actual currentItem
			// Note: don't do it if the current item is already removed (by a user), or it gets reappended (see #4088)
			if(!this._noFinalSort && this.currentItem.parent().length) {
				this.placeholder.before(this.currentItem);
			}
			this._noFinalSort = null;

			if(this.helper[0] === this.currentItem[0]) {
				for(i in this._storedCSS) {
					if(this._storedCSS[i] === "auto" || this._storedCSS[i] === "static") {
						this._storedCSS[i] = "";
					}
				}
				this.currentItem.css(this._storedCSS).removeClass("ui-sortable-helper");
			} else {
				this.currentItem.show();
			}

			if(this.fromOutside && !noPropagation) {
				delayedTriggers.push(function(event) { this._trigger("receive", event, this._uiHash(this.fromOutside)); });
			}
			if((this.fromOutside || this.domPosition.prev !== this.currentItem.prev().not(".ui-sortable-helper")[0] || this.domPosition.parent !== this.currentItem.parent()[0]) && !noPropagation) {
				delayedTriggers.push(function(event) { this._trigger("update", event, this._uiHash()); }); //Trigger update callback if the DOM position has changed
			}

			// Check if the items Container has Changed and trigger appropriate
			// events.
			if (this !== this.currentContainer) {
				if(!noPropagation) {
					delayedTriggers.push(function(event) { this._trigger("remove", event, this._uiHash()); });
					delayedTriggers.push((function(c) { return function(event) { c._trigger("receive", event, this._uiHash(this)); };  }).call(this, this.currentContainer));
					delayedTriggers.push((function(c) { return function(event) { c._trigger("update", event, this._uiHash(this));  }; }).call(this, this.currentContainer));
				}
			}


			//Post events to containers
			for (i = this.containers.length - 1; i >= 0; i--){
				if(!noPropagation) {
					delayedTriggers.push((function(c) { return function(event) { c._trigger("deactivate", event, this._uiHash(this)); };  }).call(this, this.containers[i]));
				}
				if(this.containers[i].containerCache.over) {
					delayedTriggers.push((function(c) { return function(event) { c._trigger("out", event, this._uiHash(this)); };  }).call(this, this.containers[i]));
					this.containers[i].containerCache.over = 0;
				}
			}

			//Do what was originally in plugins
			if ( this.storedCursor ) {
				this.document.find( "body" ).css( "cursor", this.storedCursor );
				this.storedStylesheet.remove();
			}
			if(this._storedOpacity) {
				this.helper.css("opacity", this._storedOpacity);
			}
			if(this._storedZIndex) {
				this.helper.css("zIndex", this._storedZIndex === "auto" ? "" : this._storedZIndex);
			}

			this.dragging = false;
			if(this.cancelHelperRemoval) {
				if(!noPropagation) {
					this._trigger("beforeStop", event, this._uiHash());
					for (i=0; i < delayedTriggers.length; i++) {
						delayedTriggers[i].call(this, event);
					} //Trigger all delayed events
					this._trigger("stop", event, this._uiHash());
				}

				this.fromOutside = false;
				return false;
			}

			if(!noPropagation) {
				this._trigger("beforeStop", event, this._uiHash());
			}

			//$(this.placeholder[0]).remove(); would have been the jQuery way - unfortunately, it unbinds ALL events from the original node!
			this.placeholder[0].parentNode.removeChild(this.placeholder[0]);

			if(this.helper[0] !== this.currentItem[0]) {
				this.helper.remove();
			}
			this.helper = null;

			if(!noPropagation) {
				for (i=0; i < delayedTriggers.length; i++) {
					delayedTriggers[i].call(this, event);
				} //Trigger all delayed events
				this._trigger("stop", event, this._uiHash());
			}

			this.fromOutside = false;
			return true;

		},

		_trigger: function() {
			if ($.Widget.prototype._trigger.apply(this, arguments) === false) {
				this.cancel();
			}
		},

		_uiHash: function(_inst) {
			var inst = _inst || this;
			return {
				helper: inst.helper,
				placeholder: inst.placeholder || $([]),
				position: inst.position,
				originalPosition: inst.originalPosition,
				offset: inst.positionAbs,
				item: inst.currentItem,
				sender: _inst ? _inst.element : null
			};
		}

	});

	})(jQuery);

	(function($, undefined) {

	var dataSpace = "ui-effects-";

	$.effects = {
		effect: {}
	};

	/*!
	 * jQuery Color Animations v2.1.2
	 * https://github.com/jquery/jquery-color
	 *
	 * Copyright 2013 jQuery Foundation and other contributors
	 * Released under the MIT license.
	 * http://jquery.org/license
	 *
	 * Date: Wed Jan 16 08:47:09 2013 -0600
	 */
	(function( jQuery, undefined ) {

		var stepHooks = "backgroundColor borderBottomColor borderLeftColor borderRightColor borderTopColor color columnRuleColor outlineColor textDecorationColor textEmphasisColor",

		// plusequals test for += 100 -= 100
		rplusequals = /^([\-+])=\s*(\d+\.?\d*)/,
		// a set of RE's that can match strings and generate color tuples.
		stringParsers = [{
				re: /rgba?\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*(?:,\s*(\d?(?:\.\d+)?)\s*)?\)/,
				parse: function( execResult ) {
					return [
						execResult[ 1 ],
						execResult[ 2 ],
						execResult[ 3 ],
						execResult[ 4 ]
					];
				}
			}, {
				re: /rgba?\(\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*(?:,\s*(\d?(?:\.\d+)?)\s*)?\)/,
				parse: function( execResult ) {
					return [
						execResult[ 1 ] * 2.55,
						execResult[ 2 ] * 2.55,
						execResult[ 3 ] * 2.55,
						execResult[ 4 ]
					];
				}
			}, {
				// this regex ignores A-F because it's compared against an already lowercased string
				re: /#([a-f0-9]{2})([a-f0-9]{2})([a-f0-9]{2})/,
				parse: function( execResult ) {
					return [
						parseInt( execResult[ 1 ], 16 ),
						parseInt( execResult[ 2 ], 16 ),
						parseInt( execResult[ 3 ], 16 )
					];
				}
			}, {
				// this regex ignores A-F because it's compared against an already lowercased string
				re: /#([a-f0-9])([a-f0-9])([a-f0-9])/,
				parse: function( execResult ) {
					return [
						parseInt( execResult[ 1 ] + execResult[ 1 ], 16 ),
						parseInt( execResult[ 2 ] + execResult[ 2 ], 16 ),
						parseInt( execResult[ 3 ] + execResult[ 3 ], 16 )
					];
				}
			}, {
				re: /hsla?\(\s*(\d+(?:\.\d+)?)\s*,\s*(\d+(?:\.\d+)?)\%\s*,\s*(\d+(?:\.\d+)?)\%\s*(?:,\s*(\d?(?:\.\d+)?)\s*)?\)/,
				space: "hsla",
				parse: function( execResult ) {
					return [
						execResult[ 1 ],
						execResult[ 2 ] / 100,
						execResult[ 3 ] / 100,
						execResult[ 4 ]
					];
				}
			}],

		// jQuery.Color( )
		color = jQuery.Color = function( color, green, blue, alpha ) {
			return new jQuery.Color.fn.parse( color, green, blue, alpha );
		},
		spaces = {
			rgba: {
				props: {
					red: {
						idx: 0,
						type: "byte"
					},
					green: {
						idx: 1,
						type: "byte"
					},
					blue: {
						idx: 2,
						type: "byte"
					}
				}
			},

			hsla: {
				props: {
					hue: {
						idx: 0,
						type: "degrees"
					},
					saturation: {
						idx: 1,
						type: "percent"
					},
					lightness: {
						idx: 2,
						type: "percent"
					}
				}
			}
		},
		propTypes = {
			"byte": {
				floor: true,
				max: 255
			},
			"percent": {
				max: 1
			},
			"degrees": {
				mod: 360,
				floor: true
			}
		},
		support = color.support = {},

		// element for support tests
		supportElem = jQuery( "<p>" )[ 0 ],

		// colors = jQuery.Color.names
		colors,

		// local aliases of functions called often
		each = jQuery.each;

	// determine rgba support immediately
	supportElem.style.cssText = "background-color:rgba(1,1,1,.5)";
	support.rgba = supportElem.style.backgroundColor.indexOf( "rgba" ) > -1;

	// define cache name and alpha properties
	// for rgba and hsla spaces
	each( spaces, function( spaceName, space ) {
		space.cache = "_" + spaceName;
		space.props.alpha = {
			idx: 3,
			type: "percent",
			def: 1
		};
	});

	function clamp( value, prop, allowEmpty ) {
		var type = propTypes[ prop.type ] || {};

		if ( value == null ) {
			return (allowEmpty || !prop.def) ? null : prop.def;
		}

		// ~~ is an short way of doing floor for positive numbers
		value = type.floor ? ~~value : parseFloat( value );

		// IE will pass in empty strings as value for alpha,
		// which will hit this case
		if ( isNaN( value ) ) {
			return prop.def;
		}

		if ( type.mod ) {
			// we add mod before modding to make sure that negatives values
			// get converted properly: -10 -> 350
			return (value + type.mod) % type.mod;
		}

		// for now all property types without mod have min and max
		return 0 > value ? 0 : type.max < value ? type.max : value;
	}

	function stringParse( string ) {
		var inst = color(),
			rgba = inst._rgba = [];

		string = string.toLowerCase();

		each( stringParsers, function( i, parser ) {
			var parsed,
				match = parser.re.exec( string ),
				values = match && parser.parse( match ),
				spaceName = parser.space || "rgba";

			if ( values ) {
				parsed = inst[ spaceName ]( values );

				// if this was an rgba parse the assignment might happen twice
				// oh well....
				inst[ spaces[ spaceName ].cache ] = parsed[ spaces[ spaceName ].cache ];
				rgba = inst._rgba = parsed._rgba;

				// exit each( stringParsers ) here because we matched
				return false;
			}
		});

		// Found a stringParser that handled it
		if ( rgba.length ) {

			// if this came from a parsed string, force "transparent" when alpha is 0
			// chrome, (and maybe others) return "transparent" as rgba(0,0,0,0)
			if ( rgba.join() === "0,0,0,0" ) {
				jQuery.extend( rgba, colors.transparent );
			}
			return inst;
		}

		// named colors
		return colors[ string ];
	}

	color.fn = jQuery.extend( color.prototype, {
		parse: function( red, green, blue, alpha ) {
			if ( red === undefined ) {
				this._rgba = [ null, null, null, null ];
				return this;
			}
			if ( red.jquery || red.nodeType ) {
				red = jQuery( red ).css( green );
				green = undefined;
			}

			var inst = this,
				type = jQuery.type( red ),
				rgba = this._rgba = [];

			// more than 1 argument specified - assume ( red, green, blue, alpha )
			if ( green !== undefined ) {
				red = [ red, green, blue, alpha ];
				type = "array";
			}

			if ( type === "string" ) {
				return this.parse( stringParse( red ) || colors._default );
			}

			if ( type === "array" ) {
				each( spaces.rgba.props, function( key, prop ) {
					rgba[ prop.idx ] = clamp( red[ prop.idx ], prop );
				});
				return this;
			}

			if ( type === "object" ) {
				if ( red instanceof color ) {
					each( spaces, function( spaceName, space ) {
						if ( red[ space.cache ] ) {
							inst[ space.cache ] = red[ space.cache ].slice();
						}
					});
				} else {
					each( spaces, function( spaceName, space ) {
						var cache = space.cache;
						each( space.props, function( key, prop ) {

							// if the cache doesn't exist, and we know how to convert
							if ( !inst[ cache ] && space.to ) {

								// if the value was null, we don't need to copy it
								// if the key was alpha, we don't need to copy it either
								if ( key === "alpha" || red[ key ] == null ) {
									return;
								}
								inst[ cache ] = space.to( inst._rgba );
							}

							// this is the only case where we allow nulls for ALL properties.
							// call clamp with alwaysAllowEmpty
							inst[ cache ][ prop.idx ] = clamp( red[ key ], prop, true );
						});

						// everything defined but alpha?
						if ( inst[ cache ] && jQuery.inArray( null, inst[ cache ].slice( 0, 3 ) ) < 0 ) {
							// use the default of 1
							inst[ cache ][ 3 ] = 1;
							if ( space.from ) {
								inst._rgba = space.from( inst[ cache ] );
							}
						}
					});
				}
				return this;
			}
		},
		is: function( compare ) {
			var is = color( compare ),
				same = true,
				inst = this;

			each( spaces, function( _, space ) {
				var localCache,
					isCache = is[ space.cache ];
				if (isCache) {
					localCache = inst[ space.cache ] || space.to && space.to( inst._rgba ) || [];
					each( space.props, function( _, prop ) {
						if ( isCache[ prop.idx ] != null ) {
							same = ( isCache[ prop.idx ] === localCache[ prop.idx ] );
							return same;
						}
					});
				}
				return same;
			});
			return same;
		},
		_space: function() {
			var used = [],
				inst = this;
			each( spaces, function( spaceName, space ) {
				if ( inst[ space.cache ] ) {
					used.push( spaceName );
				}
			});
			return used.pop();
		},
		transition: function( other, distance ) {
			var end = color( other ),
				spaceName = end._space(),
				space = spaces[ spaceName ],
				startColor = this.alpha() === 0 ? color( "transparent" ) : this,
				start = startColor[ space.cache ] || space.to( startColor._rgba ),
				result = start.slice();

			end = end[ space.cache ];
			each( space.props, function( key, prop ) {
				var index = prop.idx,
					startValue = start[ index ],
					endValue = end[ index ],
					type = propTypes[ prop.type ] || {};

				// if null, don't override start value
				if ( endValue === null ) {
					return;
				}
				// if null - use end
				if ( startValue === null ) {
					result[ index ] = endValue;
				} else {
					if ( type.mod ) {
						if ( endValue - startValue > type.mod / 2 ) {
							startValue += type.mod;
						} else if ( startValue - endValue > type.mod / 2 ) {
							startValue -= type.mod;
						}
					}
					result[ index ] = clamp( ( endValue - startValue ) * distance + startValue, prop );
				}
			});
			return this[ spaceName ]( result );
		},
		blend: function( opaque ) {
			// if we are already opaque - return ourself
			if ( this._rgba[ 3 ] === 1 ) {
				return this;
			}

			var rgb = this._rgba.slice(),
				a = rgb.pop(),
				blend = color( opaque )._rgba;

			return color( jQuery.map( rgb, function( v, i ) {
				return ( 1 - a ) * blend[ i ] + a * v;
			}));
		},
		toRgbaString: function() {
			var prefix = "rgba(",
				rgba = jQuery.map( this._rgba, function( v, i ) {
					return v == null ? ( i > 2 ? 1 : 0 ) : v;
				});

			if ( rgba[ 3 ] === 1 ) {
				rgba.pop();
				prefix = "rgb(";
			}

			return prefix + rgba.join() + ")";
		},
		toHslaString: function() {
			var prefix = "hsla(",
				hsla = jQuery.map( this.hsla(), function( v, i ) {
					if ( v == null ) {
						v = i > 2 ? 1 : 0;
					}

					// catch 1 and 2
					if ( i && i < 3 ) {
						v = Math.round( v * 100 ) + "%";
					}
					return v;
				});

			if ( hsla[ 3 ] === 1 ) {
				hsla.pop();
				prefix = "hsl(";
			}
			return prefix + hsla.join() + ")";
		},
		toHexString: function( includeAlpha ) {
			var rgba = this._rgba.slice(),
				alpha = rgba.pop();

			if ( includeAlpha ) {
				rgba.push( ~~( alpha * 255 ) );
			}

			return "#" + jQuery.map( rgba, function( v ) {

				// default to 0 when nulls exist
				v = ( v || 0 ).toString( 16 );
				return v.length === 1 ? "0" + v : v;
			}).join("");
		},
		toString: function() {
			return this._rgba[ 3 ] === 0 ? "transparent" : this.toRgbaString();
		}
	});
	color.fn.parse.prototype = color.fn;

	// hsla conversions adapted from:
	// https://code.google.com/p/maashaack/source/browse/packages/graphics/trunk/src/graphics/colors/HUE2RGB.as?r=5021

	function hue2rgb( p, q, h ) {
		h = ( h + 1 ) % 1;
		if ( h * 6 < 1 ) {
			return p + (q - p) * h * 6;
		}
		if ( h * 2 < 1) {
			return q;
		}
		if ( h * 3 < 2 ) {
			return p + (q - p) * ((2/3) - h) * 6;
		}
		return p;
	}

	spaces.hsla.to = function ( rgba ) {
		if ( rgba[ 0 ] == null || rgba[ 1 ] == null || rgba[ 2 ] == null ) {
			return [ null, null, null, rgba[ 3 ] ];
		}
		var r = rgba[ 0 ] / 255,
			g = rgba[ 1 ] / 255,
			b = rgba[ 2 ] / 255,
			a = rgba[ 3 ],
			max = Math.max( r, g, b ),
			min = Math.min( r, g, b ),
			diff = max - min,
			add = max + min,
			l = add * 0.5,
			h, s;

		if ( min === max ) {
			h = 0;
		} else if ( r === max ) {
			h = ( 60 * ( g - b ) / diff ) + 360;
		} else if ( g === max ) {
			h = ( 60 * ( b - r ) / diff ) + 120;
		} else {
			h = ( 60 * ( r - g ) / diff ) + 240;
		}

		// chroma (diff) == 0 means greyscale which, by definition, saturation = 0%
		// otherwise, saturation is based on the ratio of chroma (diff) to lightness (add)
		if ( diff === 0 ) {
			s = 0;
		} else if ( l <= 0.5 ) {
			s = diff / add;
		} else {
			s = diff / ( 2 - add );
		}
		return [ Math.round(h) % 360, s, l, a == null ? 1 : a ];
	};

	spaces.hsla.from = function ( hsla ) {
		if ( hsla[ 0 ] == null || hsla[ 1 ] == null || hsla[ 2 ] == null ) {
			return [ null, null, null, hsla[ 3 ] ];
		}
		var h = hsla[ 0 ] / 360,
			s = hsla[ 1 ],
			l = hsla[ 2 ],
			a = hsla[ 3 ],
			q = l <= 0.5 ? l * ( 1 + s ) : l + s - l * s,
			p = 2 * l - q;

		return [
			Math.round( hue2rgb( p, q, h + ( 1 / 3 ) ) * 255 ),
			Math.round( hue2rgb( p, q, h ) * 255 ),
			Math.round( hue2rgb( p, q, h - ( 1 / 3 ) ) * 255 ),
			a
		];
	};


	each( spaces, function( spaceName, space ) {
		var props = space.props,
			cache = space.cache,
			to = space.to,
			from = space.from;

		// makes rgba() and hsla()
		color.fn[ spaceName ] = function( value ) {

			// generate a cache for this space if it doesn't exist
			if ( to && !this[ cache ] ) {
				this[ cache ] = to( this._rgba );
			}
			if ( value === undefined ) {
				return this[ cache ].slice();
			}

			var ret,
				type = jQuery.type( value ),
				arr = ( type === "array" || type === "object" ) ? value : arguments,
				local = this[ cache ].slice();

			each( props, function( key, prop ) {
				var val = arr[ type === "object" ? key : prop.idx ];
				if ( val == null ) {
					val = local[ prop.idx ];
				}
				local[ prop.idx ] = clamp( val, prop );
			});

			if ( from ) {
				ret = color( from( local ) );
				ret[ cache ] = local;
				return ret;
			} else {
				return color( local );
			}
		};

		// makes red() green() blue() alpha() hue() saturation() lightness()
		each( props, function( key, prop ) {
			// alpha is included in more than one space
			if ( color.fn[ key ] ) {
				return;
			}
			color.fn[ key ] = function( value ) {
				var vtype = jQuery.type( value ),
					fn = ( key === "alpha" ? ( this._hsla ? "hsla" : "rgba" ) : spaceName ),
					local = this[ fn ](),
					cur = local[ prop.idx ],
					match;

				if ( vtype === "undefined" ) {
					return cur;
				}

				if ( vtype === "function" ) {
					value = value.call( this, cur );
					vtype = jQuery.type( value );
				}
				if ( value == null && prop.empty ) {
					return this;
				}
				if ( vtype === "string" ) {
					match = rplusequals.exec( value );
					if ( match ) {
						value = cur + parseFloat( match[ 2 ] ) * ( match[ 1 ] === "+" ? 1 : -1 );
					}
				}
				local[ prop.idx ] = value;
				return this[ fn ]( local );
			};
		});
	});

	// add cssHook and .fx.step function for each named hook.
	// accept a space separated string of properties
	color.hook = function( hook ) {
		var hooks = hook.split( " " );
		each( hooks, function( i, hook ) {
			jQuery.cssHooks[ hook ] = {
				set: function( elem, value ) {
					var parsed, curElem,
						backgroundColor = "";

					if ( value !== "transparent" && ( jQuery.type( value ) !== "string" || ( parsed = stringParse( value ) ) ) ) {
						value = color( parsed || value );
						if ( !support.rgba && value._rgba[ 3 ] !== 1 ) {
							curElem = hook === "backgroundColor" ? elem.parentNode : elem;
							while (
								(backgroundColor === "" || backgroundColor === "transparent") &&
								curElem && curElem.style
							) {
								try {
									backgroundColor = jQuery.css( curElem, "backgroundColor" );
									curElem = curElem.parentNode;
								} catch ( e ) {
								}
							}

							value = value.blend( backgroundColor && backgroundColor !== "transparent" ?
								backgroundColor :
								"_default" );
						}

						value = value.toRgbaString();
					}
					try {
						elem.style[ hook ] = value;
					} catch( e ) {
						// wrapped to prevent IE from throwing errors on "invalid" values like 'auto' or 'inherit'
					}
				}
			};
			jQuery.fx.step[ hook ] = function( fx ) {
				if ( !fx.colorInit ) {
					fx.start = color( fx.elem, hook );
					fx.end = color( fx.end );
					fx.colorInit = true;
				}
				jQuery.cssHooks[ hook ].set( fx.elem, fx.start.transition( fx.end, fx.pos ) );
			};
		});

	};

	color.hook( stepHooks );

	jQuery.cssHooks.borderColor = {
		expand: function( value ) {
			var expanded = {};

			each( [ "Top", "Right", "Bottom", "Left" ], function( i, part ) {
				expanded[ "border" + part + "Color" ] = value;
			});
			return expanded;
		}
	};

	// Basic color names only.
	// Usage of any of the other color names requires adding yourself or including
	// jquery.color.svg-names.js.
	colors = jQuery.Color.names = {
		// 4.1. Basic color keywords
		aqua: "#00ffff",
		black: "#000000",
		blue: "#0000ff",
		fuchsia: "#ff00ff",
		gray: "#808080",
		green: "#008000",
		lime: "#00ff00",
		maroon: "#800000",
		navy: "#000080",
		olive: "#808000",
		purple: "#800080",
		red: "#ff0000",
		silver: "#c0c0c0",
		teal: "#008080",
		white: "#ffffff",
		yellow: "#ffff00",

		// 4.2.3. "transparent" color keyword
		transparent: [ null, null, null, 0 ],

		_default: "#ffffff"
	};

	})( jQuery );


	/******************************************************************************/
	/****************************** CLASS ANIMATIONS ******************************/
	/******************************************************************************/
	(function() {

	var classAnimationActions = [ "add", "remove", "toggle" ],
		shorthandStyles = {
			border: 1,
			borderBottom: 1,
			borderColor: 1,
			borderLeft: 1,
			borderRight: 1,
			borderTop: 1,
			borderWidth: 1,
			margin: 1,
			padding: 1
		};

	$.each([ "borderLeftStyle", "borderRightStyle", "borderBottomStyle", "borderTopStyle" ], function( _, prop ) {
		$.fx.step[ prop ] = function( fx ) {
			if ( fx.end !== "none" && !fx.setAttr || fx.pos === 1 && !fx.setAttr ) {
				jQuery.style( fx.elem, prop, fx.end );
				fx.setAttr = true;
			}
		};
	});

	function getElementStyles( elem ) {
		var key, len,
			style = elem.ownerDocument.defaultView ?
				elem.ownerDocument.defaultView.getComputedStyle( elem, null ) :
				elem.currentStyle,
			styles = {};

		if ( style && style.length && style[ 0 ] && style[ style[ 0 ] ] ) {
			len = style.length;
			while ( len-- ) {
				key = style[ len ];
				if ( typeof style[ key ] === "string" ) {
					styles[ $.camelCase( key ) ] = style[ key ];
				}
			}
		// support: Opera, IE <9
		} else {
			for ( key in style ) {
				if ( typeof style[ key ] === "string" ) {
					styles[ key ] = style[ key ];
				}
			}
		}

		return styles;
	}


	function styleDifference( oldStyle, newStyle ) {
		var diff = {},
			name, value;

		for ( name in newStyle ) {
			value = newStyle[ name ];
			if ( oldStyle[ name ] !== value ) {
				if ( !shorthandStyles[ name ] ) {
					if ( $.fx.step[ name ] || !isNaN( parseFloat( value ) ) ) {
						diff[ name ] = value;
					}
				}
			}
		}

		return diff;
	}

	// support: jQuery <1.8
	if ( !$.fn.addBack ) {
		$.fn.addBack = function( selector ) {
			return this.add( selector == null ?
				this.prevObject : this.prevObject.filter( selector )
			);
		};
	}

	$.effects.animateClass = function( value, duration, easing, callback ) {
		var o = $.speed( duration, easing, callback );

		return this.queue( function() {
			var animated = $( this ),
				baseClass = animated.attr( "class" ) || "",
				applyClassChange,
				allAnimations = o.children ? animated.find( "*" ).addBack() : animated;

			// map the animated objects to store the original styles.
			allAnimations = allAnimations.map(function() {
				var el = $( this );
				return {
					el: el,
					start: getElementStyles( this )
				};
			});

			// apply class change
			applyClassChange = function() {
				$.each( classAnimationActions, function(i, action) {
					if ( value[ action ] ) {
						animated[ action + "Class" ]( value[ action ] );
					}
				});
			};
			applyClassChange();

			// map all animated objects again - calculate new styles and diff
			allAnimations = allAnimations.map(function() {
				this.end = getElementStyles( this.el[ 0 ] );
				this.diff = styleDifference( this.start, this.end );
				return this;
			});

			// apply original class
			animated.attr( "class", baseClass );

			// map all animated objects again - this time collecting a promise
			allAnimations = allAnimations.map(function() {
				var styleInfo = this,
					dfd = $.Deferred(),
					opts = $.extend({}, o, {
						queue: false,
						complete: function() {
							dfd.resolve( styleInfo );
						}
					});

				this.el.animate( this.diff, opts );
				return dfd.promise();
			});

			// once all animations have completed:
			$.when.apply( $, allAnimations.get() ).done(function() {

				// set the final class
				applyClassChange();

				// for each animated element,
				// clear all css properties that were animated
				$.each( arguments, function() {
					var el = this.el;
					$.each( this.diff, function(key) {
						el.css( key, "" );
					});
				});

				// this is guarnteed to be there if you use jQuery.speed()
				// it also handles dequeuing the next anim...
				o.complete.call( animated[ 0 ] );
			});
		});
	};

	$.fn.extend({
		addClass: (function( orig ) {
			return function( classNames, speed, easing, callback ) {
				return speed ?
					$.effects.animateClass.call( this,
						{ add: classNames }, speed, easing, callback ) :
					orig.apply( this, arguments );
			};
		})( $.fn.addClass ),

		removeClass: (function( orig ) {
			return function( classNames, speed, easing, callback ) {
				return arguments.length > 1 ?
					$.effects.animateClass.call( this,
						{ remove: classNames }, speed, easing, callback ) :
					orig.apply( this, arguments );
			};
		})( $.fn.removeClass ),

		toggleClass: (function( orig ) {
			return function( classNames, force, speed, easing, callback ) {
				if ( typeof force === "boolean" || force === undefined ) {
					if ( !speed ) {
						// without speed parameter
						return orig.apply( this, arguments );
					} else {
						return $.effects.animateClass.call( this,
							(force ? { add: classNames } : { remove: classNames }),
							speed, easing, callback );
					}
				} else {
					// without force parameter
					return $.effects.animateClass.call( this,
						{ toggle: classNames }, force, speed, easing );
				}
			};
		})( $.fn.toggleClass ),

		switchClass: function( remove, add, speed, easing, callback) {
			return $.effects.animateClass.call( this, {
				add: add,
				remove: remove
			}, speed, easing, callback );
		}
	});

	})();

	/******************************************************************************/
	/*********************************** EFFECTS **********************************/
	/******************************************************************************/

	(function() {

	$.extend( $.effects, {
		version: "1.10.3",

		// Saves a set of properties in a data storage
		save: function( element, set ) {
			for( var i=0; i < set.length; i++ ) {
				if ( set[ i ] !== null ) {
					element.data( dataSpace + set[ i ], element[ 0 ].style[ set[ i ] ] );
				}
			}
		},

		// Restores a set of previously saved properties from a data storage
		restore: function( element, set ) {
			var val, i;
			for( i=0; i < set.length; i++ ) {
				if ( set[ i ] !== null ) {
					val = element.data( dataSpace + set[ i ] );
					// support: jQuery 1.6.2
					// http://bugs.jquery.com/ticket/9917
					// jQuery 1.6.2 incorrectly returns undefined for any falsy value.
					// We can't differentiate between "" and 0 here, so we just assume
					// empty string since it's likely to be a more common value...
					if ( val === undefined ) {
						val = "";
					}
					element.css( set[ i ], val );
				}
			}
		},

		setMode: function( el, mode ) {
			if (mode === "toggle") {
				mode = el.is( ":hidden" ) ? "show" : "hide";
			}
			return mode;
		},

		// Translates a [top,left] array into a baseline value
		// this should be a little more flexible in the future to handle a string & hash
		getBaseline: function( origin, original ) {
			var y, x;
			switch ( origin[ 0 ] ) {
				case "top": y = 0; break;
				case "middle": y = 0.5; break;
				case "bottom": y = 1; break;
				default: y = origin[ 0 ] / original.height;
			}
			switch ( origin[ 1 ] ) {
				case "left": x = 0; break;
				case "center": x = 0.5; break;
				case "right": x = 1; break;
				default: x = origin[ 1 ] / original.width;
			}
			return {
				x: x,
				y: y
			};
		},

		// Wraps the element around a wrapper that copies position properties
		createWrapper: function( element ) {

			// if the element is already wrapped, return it
			if ( element.parent().is( ".ui-effects-wrapper" )) {
				return element.parent();
			}

			// wrap the element
			var props = {
					width: element.outerWidth(true),
					height: element.outerHeight(true),
					"float": element.css( "float" )
				},
				wrapper = $( "<div></div>" )
					.addClass( "ui-effects-wrapper" )
					.css({
						fontSize: "100%",
						background: "transparent",
						border: "none",
						margin: 0,
						padding: 0
					}),
				// Store the size in case width/height are defined in % - Fixes #5245
				size = {
					width: element.width(),
					height: element.height()
				},
				active = document.activeElement;

			// support: Firefox
			// Firefox incorrectly exposes anonymous content
			// https://bugzilla.mozilla.org/show_bug.cgi?id=561664
			try {
				active.id;
			} catch( e ) {
				active = document.body;
			}

			element.wrap( wrapper );

			// Fixes #7595 - Elements lose focus when wrapped.
			if ( element[ 0 ] === active || $.contains( element[ 0 ], active ) ) {
				$( active ).focus();
			}

			wrapper = element.parent(); //Hotfix for jQuery 1.4 since some change in wrap() seems to actually lose the reference to the wrapped element

			// transfer positioning properties to the wrapper
			if ( element.css( "position" ) === "static" ) {
				wrapper.css({ position: "relative" });
				element.css({ position: "relative" });
			} else {
				$.extend( props, {
					position: element.css( "position" ),
					zIndex: element.css( "z-index" )
				});
				$.each([ "top", "left", "bottom", "right" ], function(i, pos) {
					props[ pos ] = element.css( pos );
					if ( isNaN( parseInt( props[ pos ], 10 ) ) ) {
						props[ pos ] = "auto";
					}
				});
				element.css({
					position: "relative",
					top: 0,
					left: 0,
					right: "auto",
					bottom: "auto"
				});
			}
			element.css(size);

			return wrapper.css( props ).show();
		},

		removeWrapper: function( element ) {
			var active = document.activeElement;

			if ( element.parent().is( ".ui-effects-wrapper" ) ) {
				element.parent().replaceWith( element );

				// Fixes #7595 - Elements lose focus when wrapped.
				if ( element[ 0 ] === active || $.contains( element[ 0 ], active ) ) {
					$( active ).focus();
				}
			}


			return element;
		},

		setTransition: function( element, list, factor, value ) {
			value = value || {};
			$.each( list, function( i, x ) {
				var unit = element.cssUnit( x );
				if ( unit[ 0 ] > 0 ) {
					value[ x ] = unit[ 0 ] * factor + unit[ 1 ];
				}
			});
			return value;
		}
	});

	// return an effect options object for the given parameters:
	function _normalizeArguments( effect, options, speed, callback ) {

		// allow passing all options as the first parameter
		if ( $.isPlainObject( effect ) ) {
			options = effect;
			effect = effect.effect;
		}

		// convert to an object
		effect = { effect: effect };

		// catch (effect, null, ...)
		if ( options == null ) {
			options = {};
		}

		// catch (effect, callback)
		if ( $.isFunction( options ) ) {
			callback = options;
			speed = null;
			options = {};
		}

		// catch (effect, speed, ?)
		if ( typeof options === "number" || $.fx.speeds[ options ] ) {
			callback = speed;
			speed = options;
			options = {};
		}

		// catch (effect, options, callback)
		if ( $.isFunction( speed ) ) {
			callback = speed;
			speed = null;
		}

		// add options to effect
		if ( options ) {
			$.extend( effect, options );
		}

		speed = speed || options.duration;
		effect.duration = $.fx.off ? 0 :
			typeof speed === "number" ? speed :
			speed in $.fx.speeds ? $.fx.speeds[ speed ] :
			$.fx.speeds._default;

		effect.complete = callback || options.complete;

		return effect;
	}

	function standardAnimationOption( option ) {
		// Valid standard speeds (nothing, number, named speed)
		if ( !option || typeof option === "number" || $.fx.speeds[ option ] ) {
			return true;
		}

		// Invalid strings - treat as "normal" speed
		if ( typeof option === "string" && !$.effects.effect[ option ] ) {
			return true;
		}

		// Complete callback
		if ( $.isFunction( option ) ) {
			return true;
		}

		// Options hash (but not naming an effect)
		if ( typeof option === "object" && !option.effect ) {
			return true;
		}

		// Didn't match any standard API
		return false;
	}

	$.fn.extend({
		effect: function( /* effect, options, speed, callback */ ) {
			var args = _normalizeArguments.apply( this, arguments ),
				mode = args.mode,
				queue = args.queue,
				effectMethod = $.effects.effect[ args.effect ];

			if ( $.fx.off || !effectMethod ) {
				// delegate to the original method (e.g., .show()) if possible
				if ( mode ) {
					return this[ mode ]( args.duration, args.complete );
				} else {
					return this.each( function() {
						if ( args.complete ) {
							args.complete.call( this );
						}
					});
				}
			}

			function run( next ) {
				var elem = $( this ),
					complete = args.complete,
					mode = args.mode;

				function done() {
					if ( $.isFunction( complete ) ) {
						complete.call( elem[0] );
					}
					if ( $.isFunction( next ) ) {
						next();
					}
				}

				// If the element already has the correct final state, delegate to
				// the core methods so the internal tracking of "olddisplay" works.
				if ( elem.is( ":hidden" ) ? mode === "hide" : mode === "show" ) {
					elem[ mode ]();
					done();
				} else {
					effectMethod.call( elem[0], args, done );
				}
			}

			return queue === false ? this.each( run ) : this.queue( queue || "fx", run );
		},

		show: (function( orig ) {
			return function( option ) {
				if ( standardAnimationOption( option ) ) {
					return orig.apply( this, arguments );
				} else {
					var args = _normalizeArguments.apply( this, arguments );
					args.mode = "show";
					return this.effect.call( this, args );
				}
			};
		})( $.fn.show ),

		hide: (function( orig ) {
			return function( option ) {
				if ( standardAnimationOption( option ) ) {
					return orig.apply( this, arguments );
				} else {
					var args = _normalizeArguments.apply( this, arguments );
					args.mode = "hide";
					return this.effect.call( this, args );
				}
			};
		})( $.fn.hide ),

		toggle: (function( orig ) {
			return function( option ) {
				if ( standardAnimationOption( option ) || typeof option === "boolean" ) {
					return orig.apply( this, arguments );
				} else {
					var args = _normalizeArguments.apply( this, arguments );
					args.mode = "toggle";
					return this.effect.call( this, args );
				}
			};
		})( $.fn.toggle ),

		// helper functions
		cssUnit: function(key) {
			var style = this.css( key ),
				val = [];

			$.each( [ "em", "px", "%", "pt" ], function( i, unit ) {
				if ( style.indexOf( unit ) > 0 ) {
					val = [ parseFloat( style ), unit ];
				}
			});
			return val;
		}
	});

	})();

	/******************************************************************************/
	/*********************************** EASING ***********************************/
	/******************************************************************************/

	(function() {

	// based on easing equations from Robert Penner (http://www.robertpenner.com/easing)

	var baseEasings = {};

	$.each( [ "Quad", "Cubic", "Quart", "Quint", "Expo" ], function( i, name ) {
		baseEasings[ name ] = function( p ) {
			return Math.pow( p, i + 2 );
		};
	});

	$.extend( baseEasings, {
		Sine: function ( p ) {
			return 1 - Math.cos( p * Math.PI / 2 );
		},
		Circ: function ( p ) {
			return 1 - Math.sqrt( 1 - p * p );
		},
		Elastic: function( p ) {
			return p === 0 || p === 1 ? p :
				-Math.pow( 2, 8 * (p - 1) ) * Math.sin( ( (p - 1) * 80 - 7.5 ) * Math.PI / 15 );
		},
		Back: function( p ) {
			return p * p * ( 3 * p - 2 );
		},
		Bounce: function ( p ) {
			var pow2,
				bounce = 4;

			while ( p < ( ( pow2 = Math.pow( 2, --bounce ) ) - 1 ) / 11 ) {}
			return 1 / Math.pow( 4, 3 - bounce ) - 7.5625 * Math.pow( ( pow2 * 3 - 2 ) / 22 - p, 2 );
		}
	});

	$.each( baseEasings, function( name, easeIn ) {
		$.easing[ "easeIn" + name ] = easeIn;
		$.easing[ "easeOut" + name ] = function( p ) {
			return 1 - easeIn( 1 - p );
		};
		$.easing[ "easeInOut" + name ] = function( p ) {
			return p < 0.5 ?
				easeIn( p * 2 ) / 2 :
				1 - easeIn( p * -2 + 2 ) / 2;
		};
	});

	})();

	})(jQuery);

	(function( $, undefined ) {

	var uid = 0,
		hideProps = {},
		showProps = {};

	hideProps.height = hideProps.paddingTop = hideProps.paddingBottom =
		hideProps.borderTopWidth = hideProps.borderBottomWidth = "hide";
	showProps.height = showProps.paddingTop = showProps.paddingBottom =
		showProps.borderTopWidth = showProps.borderBottomWidth = "show";

	$.widget( "ui.accordion", {
		version: "1.10.3",
		options: {
			active: 0,
			animate: {},
			collapsible: false,
			event: "click",
			header: "> li > :first-child,> :not(li):even",
			heightStyle: "auto",
			icons: {
				activeHeader: "ui-icon-triangle-1-s",
				header: "ui-icon-triangle-1-e"
			},

			// callbacks
			activate: null,
			beforeActivate: null
		},

		_create: function() {
			var options = this.options;
			this.prevShow = this.prevHide = $();
			this.element.addClass( "ui-accordion ui-widget ui-helper-reset" )
				// ARIA
				.attr( "role", "tablist" );

			// don't allow collapsible: false and active: false / null
			if ( !options.collapsible && (options.active === false || options.active == null) ) {
				options.active = 0;
			}

			this._processPanels();
			// handle negative values
			if ( options.active < 0 ) {
				options.active += this.headers.length;
			}
			this._refresh();
		},

		_getCreateEventData: function() {
			return {
				header: this.active,
				panel: !this.active.length ? $() : this.active.next(),
				content: !this.active.length ? $() : this.active.next()
			};
		},

		_createIcons: function() {
			var icons = this.options.icons;
			if ( icons ) {
				$( "<span>" )
					.addClass( "ui-accordion-header-icon ui-icon " + icons.header )
					.prependTo( this.headers );
				this.active.children( ".ui-accordion-header-icon" )
					.removeClass( icons.header )
					.addClass( icons.activeHeader );
				this.headers.addClass( "ui-accordion-icons" );
			}
		},

		_destroyIcons: function() {
			this.headers
				.removeClass( "ui-accordion-icons" )
				.children( ".ui-accordion-header-icon" )
					.remove();
		},

		_destroy: function() {
			var contents;

			// clean up main element
			this.element
				.removeClass( "ui-accordion ui-widget ui-helper-reset" )
				.removeAttr( "role" );

			// clean up headers
			this.headers
				.removeClass( "ui-accordion-header ui-accordion-header-active ui-helper-reset ui-state-default ui-corner-all ui-state-active ui-state-disabled ui-corner-top" )
				.removeAttr( "role" )
				.removeAttr( "aria-selected" )
				.removeAttr( "aria-controls" )
				.removeAttr( "tabIndex" )
				.each(function() {
					if ( /^ui-accordion/.test( this.id ) ) {
						this.removeAttribute( "id" );
					}
				});
			this._destroyIcons();

			// clean up content panels
			contents = this.headers.next()
				.css( "display", "" )
				.removeAttr( "role" )
				.removeAttr( "aria-expanded" )
				.removeAttr( "aria-hidden" )
				.removeAttr( "aria-labelledby" )
				.removeClass( "ui-helper-reset ui-widget-content ui-corner-bottom ui-accordion-content ui-accordion-content-active ui-state-disabled" )
				.each(function() {
					if ( /^ui-accordion/.test( this.id ) ) {
						this.removeAttribute( "id" );
					}
				});
			if ( this.options.heightStyle !== "content" ) {
				contents.css( "height", "" );
			}
		},

		_setOption: function( key, value ) {
			if ( key === "active" ) {
				// _activate() will handle invalid values and update this.options
				this._activate( value );
				return;
			}

			if ( key === "event" ) {
				if ( this.options.event ) {
					this._off( this.headers, this.options.event );
				}
				this._setupEvents( value );
			}

			this._super( key, value );

			// setting collapsible: false while collapsed; open first panel
			if ( key === "collapsible" && !value && this.options.active === false ) {
				this._activate( 0 );
			}

			if ( key === "icons" ) {
				this._destroyIcons();
				if ( value ) {
					this._createIcons();
				}
			}

			// #5332 - opacity doesn't cascade to positioned elements in IE
			// so we need to add the disabled class to the headers and panels
			if ( key === "disabled" ) {
				this.headers.add( this.headers.next() )
					.toggleClass( "ui-state-disabled", !!value );
			}
		},

		_keydown: function( event ) {
			/*jshint maxcomplexity:15*/
			if ( event.altKey || event.ctrlKey ) {
				return;
			}

			var keyCode = $.ui.keyCode,
				length = this.headers.length,
				currentIndex = this.headers.index( event.target ),
				toFocus = false;

			switch ( event.keyCode ) {
				case keyCode.RIGHT:
				case keyCode.DOWN:
					toFocus = this.headers[ ( currentIndex + 1 ) % length ];
					break;
				case keyCode.LEFT:
				case keyCode.UP:
					toFocus = this.headers[ ( currentIndex - 1 + length ) % length ];
					break;
				case keyCode.SPACE:
				case keyCode.ENTER:
					this._eventHandler( event );
					break;
				case keyCode.HOME:
					toFocus = this.headers[ 0 ];
					break;
				case keyCode.END:
					toFocus = this.headers[ length - 1 ];
					break;
			}

			if ( toFocus ) {
				$( event.target ).attr( "tabIndex", -1 );
				$( toFocus ).attr( "tabIndex", 0 );
				toFocus.focus();
				event.preventDefault();
			}
		},

		_panelKeyDown : function( event ) {
			if ( event.keyCode === $.ui.keyCode.UP && event.ctrlKey ) {
				$( event.currentTarget ).prev().focus();
			}
		},

		refresh: function() {
			var options = this.options;
			this._processPanels();

			// was collapsed or no panel
			if ( ( options.active === false && options.collapsible === true ) || !this.headers.length ) {
				options.active = false;
				this.active = $();
			// active false only when collapsible is true
			} else if ( options.active === false ) {
				this._activate( 0 );
			// was active, but active panel is gone
			} else if ( this.active.length && !$.contains( this.element[ 0 ], this.active[ 0 ] ) ) {
				// all remaining panel are disabled
				if ( this.headers.length === this.headers.find(".ui-state-disabled").length ) {
					options.active = false;
					this.active = $();
				// activate previous panel
				} else {
					this._activate( Math.max( 0, options.active - 1 ) );
				}
			// was active, active panel still exists
			} else {
				// make sure active index is correct
				options.active = this.headers.index( this.active );
			}

			this._destroyIcons();

			this._refresh();
		},

		_processPanels: function() {
			this.headers = this.element.find( this.options.header )
				.addClass( "ui-accordion-header ui-helper-reset ui-state-default ui-corner-all" );

			this.headers.next()
				.addClass( "ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom" )
				.filter(":not(.ui-accordion-content-active)")
				.hide();
		},

		_refresh: function() {
			var maxHeight,
				options = this.options,
				heightStyle = options.heightStyle,
				parent = this.element.parent(),
				accordionId = this.accordionId = "ui-accordion-" +
					(this.element.attr( "id" ) || ++uid);

			this.active = this._findActive( options.active )
				.addClass( "ui-accordion-header-active ui-state-active ui-corner-top" )
				.removeClass( "ui-corner-all" );
			this.active.next()
				.addClass( "ui-accordion-content-active" )
				.show();

			this.headers
				.attr( "role", "tab" )
				.each(function( i ) {
					var header = $( this ),
						headerId = header.attr( "id" ),
						panel = header.next(),
						panelId = panel.attr( "id" );
					if ( !headerId ) {
						headerId = accordionId + "-header-" + i;
						header.attr( "id", headerId );
					}
					if ( !panelId ) {
						panelId = accordionId + "-panel-" + i;
						panel.attr( "id", panelId );
					}
					header.attr( "aria-controls", panelId );
					panel.attr( "aria-labelledby", headerId );
				})
				.next()
					.attr( "role", "tabpanel" );

			this.headers
				.not( this.active )
				.attr({
					"aria-selected": "false",
					tabIndex: -1
				})
				.next()
					.attr({
						"aria-expanded": "false",
						"aria-hidden": "true"
					})
					.hide();

			// make sure at least one header is in the tab order
			if ( !this.active.length ) {
				this.headers.eq( 0 ).attr( "tabIndex", 0 );
			} else {
				this.active.attr({
					"aria-selected": "true",
					tabIndex: 0
				})
				.next()
					.attr({
						"aria-expanded": "true",
						"aria-hidden": "false"
					});
			}

			this._createIcons();

			this._setupEvents( options.event );

			if ( heightStyle === "fill" ) {
				maxHeight = parent.height();
				this.element.siblings( ":visible" ).each(function() {
					var elem = $( this ),
						position = elem.css( "position" );

					if ( position === "absolute" || position === "fixed" ) {
						return;
					}
					maxHeight -= elem.outerHeight( true );
				});

				this.headers.each(function() {
					maxHeight -= $( this ).outerHeight( true );
				});

				this.headers.next()
					.each(function() {
						$( this ).height( Math.max( 0, maxHeight -
							$( this ).innerHeight() + $( this ).height() ) );
					})
					.css( "overflow", "auto" );
			} else if ( heightStyle === "auto" ) {
				maxHeight = 0;
				this.headers.next()
					.each(function() {
						maxHeight = Math.max( maxHeight, $( this ).css( "height", "" ).height() );
					})
					.height( maxHeight );
			}
		},

		_activate: function( index ) {
			var active = this._findActive( index )[ 0 ];

			// trying to activate the already active panel
			if ( active === this.active[ 0 ] ) {
				return;
			}

			// trying to collapse, simulate a click on the currently active header
			active = active || this.active[ 0 ];

			this._eventHandler({
				target: active,
				currentTarget: active,
				preventDefault: $.noop
			});
		},

		_findActive: function( selector ) {
			return typeof selector === "number" ? this.headers.eq( selector ) : $();
		},

		_setupEvents: function( event ) {
			var events = {
				keydown: "_keydown"
			};
			if ( event ) {
				$.each( event.split(" "), function( index, eventName ) {
					events[ eventName ] = "_eventHandler";
				});
			}

			this._off( this.headers.add( this.headers.next() ) );
			this._on( this.headers, events );
			this._on( this.headers.next(), { keydown: "_panelKeyDown" });
			this._hoverable( this.headers );
			this._focusable( this.headers );
		},

		_eventHandler: function( event ) {
			var options = this.options,
				active = this.active,
				clicked = $( event.currentTarget ),
				clickedIsActive = clicked[ 0 ] === active[ 0 ],
				collapsing = clickedIsActive && options.collapsible,
				toShow = collapsing ? $() : clicked.next(),
				toHide = active.next(),
				eventData = {
					oldHeader: active,
					oldPanel: toHide,
					newHeader: collapsing ? $() : clicked,
					newPanel: toShow
				};

			event.preventDefault();

			if (
					// click on active header, but not collapsible
					( clickedIsActive && !options.collapsible ) ||
					// allow canceling activation
					( this._trigger( "beforeActivate", event, eventData ) === false ) ) {
				return;
			}

			options.active = collapsing ? false : this.headers.index( clicked );

			// when the call to ._toggle() comes after the class changes
			// it causes a very odd bug in IE 8 (see #6720)
			this.active = clickedIsActive ? $() : clicked;
			this._toggle( eventData );

			// switch classes
			// corner classes on the previously active header stay after the animation
			active.removeClass( "ui-accordion-header-active ui-state-active" );
			if ( options.icons ) {
				active.children( ".ui-accordion-header-icon" )
					.removeClass( options.icons.activeHeader )
					.addClass( options.icons.header );
			}

			if ( !clickedIsActive ) {
				clicked
					.removeClass( "ui-corner-all" )
					.addClass( "ui-accordion-header-active ui-state-active ui-corner-top" );
				if ( options.icons ) {
					clicked.children( ".ui-accordion-header-icon" )
						.removeClass( options.icons.header )
						.addClass( options.icons.activeHeader );
				}

				clicked
					.next()
					.addClass( "ui-accordion-content-active" );
			}
		},

		_toggle: function( data ) {
			var toShow = data.newPanel,
				toHide = this.prevShow.length ? this.prevShow : data.oldPanel;

			// handle activating a panel during the animation for another activation
			this.prevShow.add( this.prevHide ).stop( true, true );
			this.prevShow = toShow;
			this.prevHide = toHide;

			if ( this.options.animate ) {
				this._animate( toShow, toHide, data );
			} else {
				toHide.hide();
				toShow.show();
				this._toggleComplete( data );
			}

			toHide.attr({
				"aria-expanded": "false",
				"aria-hidden": "true"
			});
			toHide.prev().attr( "aria-selected", "false" );
			// if we're switching panels, remove the old header from the tab order
			// if we're opening from collapsed state, remove the previous header from the tab order
			// if we're collapsing, then keep the collapsing header in the tab order
			if ( toShow.length && toHide.length ) {
				toHide.prev().attr( "tabIndex", -1 );
			} else if ( toShow.length ) {
				this.headers.filter(function() {
					return $( this ).attr( "tabIndex" ) === 0;
				})
				.attr( "tabIndex", -1 );
			}

			toShow
				.attr({
					"aria-expanded": "true",
					"aria-hidden": "false"
				})
				.prev()
					.attr({
						"aria-selected": "true",
						tabIndex: 0
					});
		},

		_animate: function( toShow, toHide, data ) {
			var total, easing, duration,
				that = this,
				adjust = 0,
				down = toShow.length &&
					( !toHide.length || ( toShow.index() < toHide.index() ) ),
				animate = this.options.animate || {},
				options = down && animate.down || animate,
				complete = function() {
					that._toggleComplete( data );
				};

			if ( typeof options === "number" ) {
				duration = options;
			}
			if ( typeof options === "string" ) {
				easing = options;
			}
			// fall back from options to animation in case of partial down settings
			easing = easing || options.easing || animate.easing;
			duration = duration || options.duration || animate.duration;

			if ( !toHide.length ) {
				return toShow.animate( showProps, duration, easing, complete );
			}
			if ( !toShow.length ) {
				return toHide.animate( hideProps, duration, easing, complete );
			}

			total = toShow.show().outerHeight();
			toHide.animate( hideProps, {
				duration: duration,
				easing: easing,
				step: function( now, fx ) {
					fx.now = Math.round( now );
				}
			});
			toShow
				.hide()
				.animate( showProps, {
					duration: duration,
					easing: easing,
					complete: complete,
					step: function( now, fx ) {
						fx.now = Math.round( now );
						if ( fx.prop !== "height" ) {
							adjust += fx.now;
						} else if ( that.options.heightStyle !== "content" ) {
							fx.now = Math.round( total - toHide.outerHeight() - adjust );
							adjust = 0;
						}
					}
				});
		},

		_toggleComplete: function( data ) {
			var toHide = data.oldPanel;

			toHide
				.removeClass( "ui-accordion-content-active" )
				.prev()
					.removeClass( "ui-corner-top" )
					.addClass( "ui-corner-all" );

			// Work around for rendering bug in IE (#5421)
			if ( toHide.length ) {
				toHide.parent()[0].className = toHide.parent()[0].className;
			}

			this._trigger( "activate", null, data );
		}
	});

	})( jQuery );

	(function( $, undefined ) {

	// used to prevent race conditions with remote data sources
	var requestIndex = 0;

	$.widget( "ui.autocomplete", {
		version: "1.10.3",
		defaultElement: "<input>",
		options: {
			appendTo: null,
			autoFocus: false,
			delay: 300,
			minLength: 1,
			position: {
				my: "left top",
				at: "left bottom",
				collision: "none"
			},
			source: null,

			// callbacks
			change: null,
			close: null,
			focus: null,
			open: null,
			response: null,
			search: null,
			select: null
		},

		pending: 0,

		_create: function() {
			// Some browsers only repeat keydown events, not keypress events,
			// so we use the suppressKeyPress flag to determine if we've already
			// handled the keydown event. #7269
			// Unfortunately the code for & in keypress is the same as the up arrow,
			// so we use the suppressKeyPressRepeat flag to avoid handling keypress
			// events when we know the keydown event was used to modify the
			// search term. #7799
			var suppressKeyPress, suppressKeyPressRepeat, suppressInput,
				nodeName = this.element[0].nodeName.toLowerCase(),
				isTextarea = nodeName === "textarea",
				isInput = nodeName === "input";

			this.isMultiLine =
				// Textareas are always multi-line
				isTextarea ? true :
				// Inputs are always single-line, even if inside a contentEditable element
				// IE also treats inputs as contentEditable
				isInput ? false :
				// All other element types are determined by whether or not they're contentEditable
				this.element.prop( "isContentEditable" );

			this.valueMethod = this.element[ isTextarea || isInput ? "val" : "text" ];
			this.isNewMenu = true;

			this.element
				.addClass( "ui-autocomplete-input" )
				.attr( "autocomplete", "off" );

			this._on( this.element, {
				keydown: function( event ) {
					/*jshint maxcomplexity:15*/
					if ( this.element.prop( "readOnly" ) ) {
						suppressKeyPress = true;
						suppressInput = true;
						suppressKeyPressRepeat = true;
						return;
					}

					suppressKeyPress = false;
					suppressInput = false;
					suppressKeyPressRepeat = false;
					var keyCode = $.ui.keyCode;
					switch( event.keyCode ) {
					case keyCode.PAGE_UP:
						suppressKeyPress = true;
						this._move( "previousPage", event );
						break;
					case keyCode.PAGE_DOWN:
						suppressKeyPress = true;
						this._move( "nextPage", event );
						break;
					case keyCode.UP:
						suppressKeyPress = true;
						this._keyEvent( "previous", event );
						break;
					case keyCode.DOWN:
						suppressKeyPress = true;
						this._keyEvent( "next", event );
						break;
					case keyCode.ENTER:
					case keyCode.NUMPAD_ENTER:
						// when menu is open and has focus
						if ( this.menu.active ) {
							// #6055 - Opera still allows the keypress to occur
							// which causes forms to submit
							suppressKeyPress = true;
							event.preventDefault();
							this.menu.select( event );
						}
						break;
					case keyCode.TAB:
						if ( this.menu.active ) {
							this.menu.select( event );
						}
						break;
					case keyCode.ESCAPE:
						if ( this.menu.element.is( ":visible" ) ) {
							this._value( this.term );
							this.close( event );
							// Different browsers have different default behavior for escape
							// Single press can mean undo or clear
							// Double press in IE means clear the whole form
							event.preventDefault();
						}
						break;
					default:
						suppressKeyPressRepeat = true;
						// search timeout should be triggered before the input value is changed
						this._searchTimeout( event );
						break;
					}
				},
				keypress: function( event ) {
					if ( suppressKeyPress ) {
						suppressKeyPress = false;
						if ( !this.isMultiLine || this.menu.element.is( ":visible" ) ) {
							event.preventDefault();
						}
						return;
					}
					if ( suppressKeyPressRepeat ) {
						return;
					}

					// replicate some key handlers to allow them to repeat in Firefox and Opera
					var keyCode = $.ui.keyCode;
					switch( event.keyCode ) {
					case keyCode.PAGE_UP:
						this._move( "previousPage", event );
						break;
					case keyCode.PAGE_DOWN:
						this._move( "nextPage", event );
						break;
					case keyCode.UP:
						this._keyEvent( "previous", event );
						break;
					case keyCode.DOWN:
						this._keyEvent( "next", event );
						break;
					}
				},
				input: function( event ) {
					if ( suppressInput ) {
						suppressInput = false;
						event.preventDefault();
						return;
					}
					this._searchTimeout( event );
				},
				focus: function() {
					this.selectedItem = null;
					this.previous = this._value();
				},
				blur: function( event ) {
					if ( this.cancelBlur ) {
						delete this.cancelBlur;
						return;
					}

					clearTimeout( this.searching );
					this.close( event );
					this._change( event );
				}
			});

			this._initSource();
			this.menu = $( "<ul>" )
				.addClass( "ui-autocomplete ui-front" )
				.appendTo( this._appendTo() )
				.menu({
					// disable ARIA support, the live region takes care of that
					role: null
				})
				.hide()
				.data( "ui-menu" );

			this._on( this.menu.element, {
				mousedown: function( event ) {
					// prevent moving focus out of the text field
					event.preventDefault();

					// IE doesn't prevent moving focus even with event.preventDefault()
					// so we set a flag to know when we should ignore the blur event
					this.cancelBlur = true;
					this._delay(function() {
						delete this.cancelBlur;
					});

					// clicking on the scrollbar causes focus to shift to the body
					// but we can't detect a mouseup or a click immediately afterward
					// so we have to track the next mousedown and close the menu if
					// the user clicks somewhere outside of the autocomplete
					var menuElement = this.menu.element[ 0 ];
					if ( !$( event.target ).closest( ".ui-menu-item" ).length ) {
						this._delay(function() {
							var that = this;
							this.document.one( "mousedown", function( event ) {
								if ( event.target !== that.element[ 0 ] &&
										event.target !== menuElement &&
										!$.contains( menuElement, event.target ) ) {
									that.close();
								}
							});
						});
					}
				},
				menufocus: function( event, ui ) {
					// support: Firefox
					// Prevent accidental activation of menu items in Firefox (#7024 #9118)
					if ( this.isNewMenu ) {
						this.isNewMenu = false;
						if ( event.originalEvent && /^mouse/.test( event.originalEvent.type ) ) {
							this.menu.blur();

							this.document.one( "mousemove", function() {
								$( event.target ).trigger( event.originalEvent );
							});

							return;
						}
					}

					var item = ui.item.data( "ui-autocomplete-item" );
					if ( false !== this._trigger( "focus", event, { item: item } ) ) {
						// use value to match what will end up in the input, if it was a key event
						if ( event.originalEvent && /^key/.test( event.originalEvent.type ) ) {
							this._value( item.value );
						}
					} else {
						// Normally the input is populated with the item's value as the
						// menu is navigated, causing screen readers to notice a change and
						// announce the item. Since the focus event was canceled, this doesn't
						// happen, so we update the live region so that screen readers can
						// still notice the change and announce it.
						this.liveRegion.text( item.value );
					}
				},
				menuselect: function( event, ui ) {
					var item = ui.item.data( "ui-autocomplete-item" ),
						previous = this.previous;

					// only trigger when focus was lost (click on menu)
					if ( this.element[0] !== this.document[0].activeElement ) {
						this.element.focus();
						this.previous = previous;
						// #6109 - IE triggers two focus events and the second
						// is asynchronous, so we need to reset the previous
						// term synchronously and asynchronously :-(
						this._delay(function() {
							this.previous = previous;
							this.selectedItem = item;
						});
					}

					if ( false !== this._trigger( "select", event, { item: item } ) ) {
						this._value( item.value );
					}
					// reset the term after the select event
					// this allows custom select handling to work properly
					this.term = this._value();

					this.close( event );
					this.selectedItem = item;
				}
			});

			this.liveRegion = $( "<span>", {
					role: "status",
					"aria-live": "polite"
				})
				.addClass( "ui-helper-hidden-accessible" )
				.insertBefore( this.element );

			// turning off autocomplete prevents the browser from remembering the
			// value when navigating through history, so we re-enable autocomplete
			// if the page is unloaded before the widget is destroyed. #7790
			this._on( this.window, {
				beforeunload: function() {
					this.element.removeAttr( "autocomplete" );
				}
			});
		},

		_destroy: function() {
			clearTimeout( this.searching );
			this.element
				.removeClass( "ui-autocomplete-input" )
				.removeAttr( "autocomplete" );
			this.menu.element.remove();
			this.liveRegion.remove();
		},

		_setOption: function( key, value ) {
			this._super( key, value );
			if ( key === "source" ) {
				this._initSource();
			}
			if ( key === "appendTo" ) {
				this.menu.element.appendTo( this._appendTo() );
			}
			if ( key === "disabled" && value && this.xhr ) {
				this.xhr.abort();
			}
		},

		_appendTo: function() {
			var element = this.options.appendTo;

			if ( element ) {
				element = element.jquery || element.nodeType ?
					$( element ) :
					this.document.find( element ).eq( 0 );
			}

			if ( !element ) {
				element = this.element.closest( ".ui-front" );
			}

			if ( !element.length ) {
				element = this.document[0].body;
			}

			return element;
		},

		_initSource: function() {
			var array, url,
				that = this;
			if ( $.isArray(this.options.source) ) {
				array = this.options.source;
				this.source = function( request, response ) {
					response( $.ui.autocomplete.filter( array, request.term ) );
				};
			} else if ( typeof this.options.source === "string" ) {
				url = this.options.source;
				this.source = function( request, response ) {
					if ( that.xhr ) {
						that.xhr.abort();
					}
					that.xhr = $.ajax({
						url: url,
						data: request,
						dataType: "json",
						success: function( data ) {
							response( data );
						},
						error: function() {
							response( [] );
						}
					});
				};
			} else {
				this.source = this.options.source;
			}
		},

		_searchTimeout: function( event ) {
			clearTimeout( this.searching );
			this.searching = this._delay(function() {
				// only search if the value has changed
				if ( this.term !== this._value() ) {
					this.selectedItem = null;
					this.search( null, event );
				}
			}, this.options.delay );
		},

		search: function( value, event ) {
			value = value != null ? value : this._value();

			// always save the actual value, not the one passed as an argument
			this.term = this._value();

			if ( value.length < this.options.minLength ) {
				return this.close( event );
			}

			if ( this._trigger( "search", event ) === false ) {
				return;
			}

			return this._search( value );
		},

		_search: function( value ) {
			this.pending++;
			this.element.addClass( "ui-autocomplete-loading" );
			this.cancelSearch = false;

			this.source( { term: value }, this._response() );
		},

		_response: function() {
			var that = this,
				index = ++requestIndex;

			return function( content ) {
				if ( index === requestIndex ) {
					that.__response( content );
				}

				that.pending--;
				if ( !that.pending ) {
					that.element.removeClass( "ui-autocomplete-loading" );
				}
			};
		},

		__response: function( content ) {
			if ( content ) {
				content = this._normalize( content );
			}
			this._trigger( "response", null, { content: content } );
			if ( !this.options.disabled && content && content.length && !this.cancelSearch ) {
				this._suggest( content );
				this._trigger( "open" );
			} else {
				// use ._close() instead of .close() so we don't cancel future searches
				this._close();
			}
		},

		close: function( event ) {
			this.cancelSearch = true;
			this._close( event );
		},

		_close: function( event ) {
			if ( this.menu.element.is( ":visible" ) ) {
				this.menu.element.hide();
				this.menu.blur();
				this.isNewMenu = true;
				this._trigger( "close", event );
			}
		},

		_change: function( event ) {
			if ( this.previous !== this._value() ) {
				this._trigger( "change", event, { item: this.selectedItem } );
			}
		},

		_normalize: function( items ) {
			// assume all items have the right format when the first item is complete
			if ( items.length && items[0].label && items[0].value ) {
				return items;
			}
			return $.map( items, function( item ) {
				if ( typeof item === "string" ) {
					return {
						label: item,
						value: item
					};
				}
				return $.extend({
					label: item.label || item.value,
					value: item.value || item.label
				}, item );
			});
		},

		_suggest: function( items ) {
			var ul = this.menu.element.empty();
			this._renderMenu( ul, items );
			this.isNewMenu = true;
			this.menu.refresh();

			// size and position menu
			ul.show();
			this._resizeMenu();
			ul.position( $.extend({
				of: this.element
			}, this.options.position ));

			if ( this.options.autoFocus ) {
				this.menu.next();
			}
		},

		_resizeMenu: function() {
			var ul = this.menu.element;
			ul.outerWidth( Math.max(
				// Firefox wraps long text (possibly a rounding bug)
				// so we add 1px to avoid the wrapping (#7513)
				ul.width( "" ).outerWidth() + 1,
				this.element.outerWidth()
			) );
		},

		_renderMenu: function( ul, items ) {
			var that = this;
			$.each( items, function( index, item ) {
				that._renderItemData( ul, item );
			});
		},

		_renderItemData: function( ul, item ) {
			return this._renderItem( ul, item ).data( "ui-autocomplete-item", item );
		},

		_renderItem: function( ul, item ) {
			return $( "<li>" )
				.append( $( "<a>" ).text( item.label ) )
				.appendTo( ul );
		},

		_move: function( direction, event ) {
			if ( !this.menu.element.is( ":visible" ) ) {
				this.search( null, event );
				return;
			}
			if ( this.menu.isFirstItem() && /^previous/.test( direction ) ||
					this.menu.isLastItem() && /^next/.test( direction ) ) {
				this._value( this.term );
				this.menu.blur();
				return;
			}
			this.menu[ direction ]( event );
		},

		widget: function() {
			return this.menu.element;
		},

		_value: function() {
			return this.valueMethod.apply( this.element, arguments );
		},

		_keyEvent: function( keyEvent, event ) {
			if ( !this.isMultiLine || this.menu.element.is( ":visible" ) ) {
				this._move( keyEvent, event );

				// prevents moving cursor to beginning/end of the text field in some browsers
				event.preventDefault();
			}
		}
	});

	$.extend( $.ui.autocomplete, {
		escapeRegex: function( value ) {
			return value.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, "\\$&");
		},
		filter: function(array, term) {
			var matcher = new RegExp( $.ui.autocomplete.escapeRegex(term), "i" );
			return $.grep( array, function(value) {
				return matcher.test( value.label || value.value || value );
			});
		}
	});


	// live region extension, adding a `messages` option
	// NOTE: This is an experimental API. We are still investigating
	// a full solution for string manipulation and internationalization.
	$.widget( "ui.autocomplete", $.ui.autocomplete, {
		options: {
			messages: {
				noResults: "No search results.",
				results: function( amount ) {
					return amount + ( amount > 1 ? " results are" : " result is" ) +
						" available, use up and down arrow keys to navigate.";
				}
			}
		},

		__response: function( content ) {
			var message;
			this._superApply( arguments );
			if ( this.options.disabled || this.cancelSearch ) {
				return;
			}
			if ( content && content.length ) {
				message = this.options.messages.results( content.length );
			} else {
				message = this.options.messages.noResults;
			}
			this.liveRegion.text( message );
		}
	});

	}( jQuery ));

	(function( $, undefined ) {

	var lastActive, startXPos, startYPos, clickDragged,
		baseClasses = "ui-button ui-widget ui-state-default ui-corner-all",
		stateClasses = "ui-state-hover ui-state-active ",
		typeClasses = "ui-button-icons-only ui-button-icon-only ui-button-text-icons ui-button-text-icon-primary ui-button-text-icon-secondary ui-button-text-only",
		formResetHandler = function() {
			var form = $( this );
			setTimeout(function() {
				form.find( ":ui-button" ).button( "refresh" );
			}, 1 );
		},
		radioGroup = function( radio ) {
			var name = radio.name,
				form = radio.form,
				radios = $( [] );
			if ( name ) {
				name = name.replace( /'/g, "\\'" );
				if ( form ) {
					radios = $( form ).find( "[name='" + name + "']" );
				} else {
					radios = $( "[name='" + name + "']", radio.ownerDocument )
						.filter(function() {
							return !this.form;
						});
				}
			}
			return radios;
		};

	$.widget( "ui.button", {
		version: "1.10.3",
		defaultElement: "<button>",
		options: {
			disabled: null,
			text: true,
			label: null,
			icons: {
				primary: null,
				secondary: null
			}
		},
		_create: function() {
			this.element.closest( "form" )
				.unbind( "reset" + this.eventNamespace )
				.bind( "reset" + this.eventNamespace, formResetHandler );

			if ( typeof this.options.disabled !== "boolean" ) {
				this.options.disabled = !!this.element.prop( "disabled" );
			} else {
				this.element.prop( "disabled", this.options.disabled );
			}

			this._determineButtonType();
			this.hasTitle = !!this.buttonElement.attr( "title" );

			var that = this,
				options = this.options,
				toggleButton = this.type === "checkbox" || this.type === "radio",
				activeClass = !toggleButton ? "ui-state-active" : "",
				focusClass = "ui-state-focus";

			if ( options.label === null ) {
				options.label = (this.type === "input" ? this.buttonElement.val() : this.buttonElement.html());
			}

			this._hoverable( this.buttonElement );

			this.buttonElement
				.addClass( baseClasses )
				.attr( "role", "button" )
				.bind( "mouseenter" + this.eventNamespace, function() {
					if ( options.disabled ) {
						return;
					}
					if ( this === lastActive ) {
						$( this ).addClass( "ui-state-active" );
					}
				})
				.bind( "mouseleave" + this.eventNamespace, function() {
					if ( options.disabled ) {
						return;
					}
					$( this ).removeClass( activeClass );
				})
				.bind( "click" + this.eventNamespace, function( event ) {
					if ( options.disabled ) {
						event.preventDefault();
						event.stopImmediatePropagation();
					}
				});

			this.element
				.bind( "focus" + this.eventNamespace, function() {
					// no need to check disabled, focus won't be triggered anyway
					that.buttonElement.addClass( focusClass );
				})
				.bind( "blur" + this.eventNamespace, function() {
					that.buttonElement.removeClass( focusClass );
				});

			if ( toggleButton ) {
				this.element.bind( "change" + this.eventNamespace, function() {
					if ( clickDragged ) {
						return;
					}
					that.refresh();
				});
				// if mouse moves between mousedown and mouseup (drag) set clickDragged flag
				// prevents issue where button state changes but checkbox/radio checked state
				// does not in Firefox (see ticket #6970)
				this.buttonElement
					.bind( "mousedown" + this.eventNamespace, function( event ) {
						if ( options.disabled ) {
							return;
						}
						clickDragged = false;
						startXPos = event.pageX;
						startYPos = event.pageY;
					})
					.bind( "mouseup" + this.eventNamespace, function( event ) {
						if ( options.disabled ) {
							return;
						}
						if ( startXPos !== event.pageX || startYPos !== event.pageY ) {
							clickDragged = true;
						}
				});
			}

			if ( this.type === "checkbox" ) {
				this.buttonElement.bind( "click" + this.eventNamespace, function() {
					if ( options.disabled || clickDragged ) {
						return false;
					}
				});
			} else if ( this.type === "radio" ) {
				this.buttonElement.bind( "click" + this.eventNamespace, function() {
					if ( options.disabled || clickDragged ) {
						return false;
					}
					$( this ).addClass( "ui-state-active" );
					that.buttonElement.attr( "aria-pressed", "true" );

					var radio = that.element[ 0 ];
					radioGroup( radio )
						.not( radio )
						.map(function() {
							return $( this ).button( "widget" )[ 0 ];
						})
						.removeClass( "ui-state-active" )
						.attr( "aria-pressed", "false" );
				});
			} else {
				this.buttonElement
					.bind( "mousedown" + this.eventNamespace, function() {
						if ( options.disabled ) {
							return false;
						}
						$( this ).addClass( "ui-state-active" );
						lastActive = this;
						that.document.one( "mouseup", function() {
							lastActive = null;
						});
					})
					.bind( "mouseup" + this.eventNamespace, function() {
						if ( options.disabled ) {
							return false;
						}
						$( this ).removeClass( "ui-state-active" );
					})
					.bind( "keydown" + this.eventNamespace, function(event) {
						if ( options.disabled ) {
							return false;
						}
						if ( event.keyCode === $.ui.keyCode.SPACE || event.keyCode === $.ui.keyCode.ENTER ) {
							$( this ).addClass( "ui-state-active" );
						}
					})
					// see #8559, we bind to blur here in case the button element loses
					// focus between keydown and keyup, it would be left in an "active" state
					.bind( "keyup" + this.eventNamespace + " blur" + this.eventNamespace, function() {
						$( this ).removeClass( "ui-state-active" );
					});

				if ( this.buttonElement.is("a") ) {
					this.buttonElement.keyup(function(event) {
						if ( event.keyCode === $.ui.keyCode.SPACE ) {
							// TODO pass through original event correctly (just as 2nd argument doesn't work)
							$( this ).click();
						}
					});
				}
			}

			// TODO: pull out $.Widget's handling for the disabled option into
			// $.Widget.prototype._setOptionDisabled so it's easy to proxy and can
			// be overridden by individual plugins
			this._setOption( "disabled", options.disabled );
			this._resetButton();
		},

		_determineButtonType: function() {
			var ancestor, labelSelector, checked;

			if ( this.element.is("[type=checkbox]") ) {
				this.type = "checkbox";
			} else if ( this.element.is("[type=radio]") ) {
				this.type = "radio";
			} else if ( this.element.is("input") ) {
				this.type = "input";
			} else {
				this.type = "button";
			}

			if ( this.type === "checkbox" || this.type === "radio" ) {
				// we don't search against the document in case the element
				// is disconnected from the DOM
				ancestor = this.element.parents().last();
				labelSelector = "label[for='" + this.element.attr("id") + "']";
				this.buttonElement = ancestor.find( labelSelector );
				if ( !this.buttonElement.length ) {
					ancestor = ancestor.length ? ancestor.siblings() : this.element.siblings();
					this.buttonElement = ancestor.filter( labelSelector );
					if ( !this.buttonElement.length ) {
						this.buttonElement = ancestor.find( labelSelector );
					}
				}
				this.element.addClass( "ui-helper-hidden-accessible" );

				checked = this.element.is( ":checked" );
				if ( checked ) {
					this.buttonElement.addClass( "ui-state-active" );
				}
				this.buttonElement.prop( "aria-pressed", checked );
			} else {
				this.buttonElement = this.element;
			}
		},

		widget: function() {
			return this.buttonElement;
		},

		_destroy: function() {
			this.element
				.removeClass( "ui-helper-hidden-accessible" );
			this.buttonElement
				.removeClass( baseClasses + " " + stateClasses + " " + typeClasses )
				.removeAttr( "role" )
				.removeAttr( "aria-pressed" )
				.html( this.buttonElement.find(".ui-button-text").html() );

			if ( !this.hasTitle ) {
				this.buttonElement.removeAttr( "title" );
			}
		},

		_setOption: function( key, value ) {
			this._super( key, value );
			if ( key === "disabled" ) {
				if ( value ) {
					this.element.prop( "disabled", true );
				} else {
					this.element.prop( "disabled", false );
				}
				return;
			}
			this._resetButton();
		},

		refresh: function() {
			//See #8237 & #8828
			var isDisabled = this.element.is( "input, button" ) ? this.element.is( ":disabled" ) : this.element.hasClass( "ui-button-disabled" );

			if ( isDisabled !== this.options.disabled ) {
				this._setOption( "disabled", isDisabled );
			}
			if ( this.type === "radio" ) {
				radioGroup( this.element[0] ).each(function() {
					if ( $( this ).is( ":checked" ) ) {
						$( this ).button( "widget" )
							.addClass( "ui-state-active" )
							.attr( "aria-pressed", "true" );
					} else {
						$( this ).button( "widget" )
							.removeClass( "ui-state-active" )
							.attr( "aria-pressed", "false" );
					}
				});
			} else if ( this.type === "checkbox" ) {
				if ( this.element.is( ":checked" ) ) {
					this.buttonElement
						.addClass( "ui-state-active" )
						.attr( "aria-pressed", "true" );
				} else {
					this.buttonElement
						.removeClass( "ui-state-active" )
						.attr( "aria-pressed", "false" );
				}
			}
		},

		_resetButton: function() {
			if ( this.type === "input" ) {
				if ( this.options.label ) {
					this.element.val( this.options.label );
				}
				return;
			}
			var buttonElement = this.buttonElement.removeClass( typeClasses ),
				buttonText = $( "<span></span>", this.document[0] )
					.addClass( "ui-button-text" )
					.html( this.options.label )
					.appendTo( buttonElement.empty() )
					.text(),
				icons = this.options.icons,
				multipleIcons = icons.primary && icons.secondary,
				buttonClasses = [];

			if ( icons.primary || icons.secondary ) {
				if ( this.options.text ) {
					buttonClasses.push( "ui-button-text-icon" + ( multipleIcons ? "s" : ( icons.primary ? "-primary" : "-secondary" ) ) );
				}

				if ( icons.primary ) {
					buttonElement.prepend( "<span class='ui-button-icon-primary ui-icon " + icons.primary + "'></span>" );
				}

				if ( icons.secondary ) {
					buttonElement.append( "<span class='ui-button-icon-secondary ui-icon " + icons.secondary + "'></span>" );
				}

				if ( !this.options.text ) {
					buttonClasses.push( multipleIcons ? "ui-button-icons-only" : "ui-button-icon-only" );

					if ( !this.hasTitle ) {
						buttonElement.attr( "title", $.trim( buttonText ) );
					}
				}
			} else {
				buttonClasses.push( "ui-button-text-only" );
			}
			buttonElement.addClass( buttonClasses.join( " " ) );
		}
	});

	$.widget( "ui.buttonset", {
		version: "1.10.3",
		options: {
			items: "button, input[type=button], input[type=submit], input[type=reset], input[type=checkbox], input[type=radio], a, :data(ui-button)"
		},

		_create: function() {
			this.element.addClass( "ui-buttonset" );
		},

		_init: function() {
			this.refresh();
		},

		_setOption: function( key, value ) {
			if ( key === "disabled" ) {
				this.buttons.button( "option", key, value );
			}

			this._super( key, value );
		},

		refresh: function() {
			var rtl = this.element.css( "direction" ) === "rtl";

			this.buttons = this.element.find( this.options.items )
				.filter( ":ui-button" )
					.button( "refresh" )
				.end()
				.not( ":ui-button" )
					.button()
				.end()
				.map(function() {
					return $( this ).button( "widget" )[ 0 ];
				})
					.removeClass( "ui-corner-all ui-corner-left ui-corner-right" )
					.filter( ":first" )
						.addClass( rtl ? "ui-corner-right" : "ui-corner-left" )
					.end()
					.filter( ":last" )
						.addClass( rtl ? "ui-corner-left" : "ui-corner-right" )
					.end()
				.end();
		},

		_destroy: function() {
			this.element.removeClass( "ui-buttonset" );
			this.buttons
				.map(function() {
					return $( this ).button( "widget" )[ 0 ];
				})
					.removeClass( "ui-corner-left ui-corner-right" )
				.end()
				.button( "destroy" );
		}
	});

	}( jQuery ) );

	(function( $, undefined ) {

	$.extend($.ui, { datepicker: { version: "1.10.3" } });

	var PROP_NAME = "datepicker",
		instActive;

	/* Date picker manager.
	   Use the singleton instance of this class, $.datepicker, to interact with the date picker.
	   Settings for (groups of) date pickers are maintained in an instance object,
	   allowing multiple different settings on the same page. */

	function Datepicker() {
		this._curInst = null; // The current instance in use
		this._keyEvent = false; // If the last event was a key event
		this._disabledInputs = []; // List of date picker inputs that have been disabled
		this._datepickerShowing = false; // True if the popup picker is showing , false if not
		this._inDialog = false; // True if showing within a "dialog", false if not
		this._mainDivId = "ui-datepicker-div"; // The ID of the main datepicker division
		this._inlineClass = "ui-datepicker-inline"; // The name of the inline marker class
		this._appendClass = "ui-datepicker-append"; // The name of the append marker class
		this._triggerClass = "ui-datepicker-trigger"; // The name of the trigger marker class
		this._dialogClass = "ui-datepicker-dialog"; // The name of the dialog marker class
		this._disableClass = "ui-datepicker-disabled"; // The name of the disabled covering marker class
		this._unselectableClass = "ui-datepicker-unselectable"; // The name of the unselectable cell marker class
		this._currentClass = "ui-datepicker-current-day"; // The name of the current day marker class
		this._dayOverClass = "ui-datepicker-days-cell-over"; // The name of the day hover marker class
		this.regional = []; // Available regional settings, indexed by language code
		this.regional[""] = { // Default regional settings
			closeText: "Done", // Display text for close link
			prevText: "Prev", // Display text for previous month link
			nextText: "Next", // Display text for next month link
			currentText: "Today", // Display text for current month link
			monthNames: ["January","February","March","April","May","June",
				"July","August","September","October","November","December"], // Names of months for drop-down and formatting
			monthNamesShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"], // For formatting
			dayNames: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], // For formatting
			dayNamesShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"], // For formatting
			dayNamesMin: ["Su","Mo","Tu","We","Th","Fr","Sa"], // Column headings for days starting at Sunday
			weekHeader: "Wk", // Column header for week of the year
			dateFormat: "mm/dd/yy", // See format options on parseDate
			firstDay: 0, // The first day of the week, Sun = 0, Mon = 1, ...
			isRTL: false, // True if right-to-left language, false if left-to-right
			showMonthAfterYear: false, // True if the year select precedes month, false for month then year
			yearSuffix: "" // Additional text to append to the year in the month headers
		};
		this._defaults = { // Global defaults for all the date picker instances
			showOn: "focus", // "focus" for popup on focus,
				// "button" for trigger button, or "both" for either
			showAnim: "fadeIn", // Name of jQuery animation for popup
			showOptions: {}, // Options for enhanced animations
			defaultDate: null, // Used when field is blank: actual date,
				// +/-number for offset from today, null for today
			appendText: "", // Display text following the input box, e.g. showing the format
			buttonText: "...", // Text for trigger button
			buttonImage: "", // URL for trigger button image
			buttonImageOnly: false, // True if the image appears alone, false if it appears on a button
			hideIfNoPrevNext: false, // True to hide next/previous month links
				// if not applicable, false to just disable them
			navigationAsDateFormat: false, // True if date formatting applied to prev/today/next links
			gotoCurrent: false, // True if today link goes back to current selection instead
			changeMonth: false, // True if month can be selected directly, false if only prev/next
			changeYear: false, // True if year can be selected directly, false if only prev/next
			yearRange: "c-10:c+10", // Range of years to display in drop-down,
				// either relative to today's year (-nn:+nn), relative to currently displayed year
				// (c-nn:c+nn), absolute (nnnn:nnnn), or a combination of the above (nnnn:-n)
			showOtherMonths: false, // True to show dates in other months, false to leave blank
			selectOtherMonths: false, // True to allow selection of dates in other months, false for unselectable
			showWeek: false, // True to show week of the year, false to not show it
			calculateWeek: this.iso8601Week, // How to calculate the week of the year,
				// takes a Date and returns the number of the week for it
			shortYearCutoff: "+10", // Short year values < this are in the current century,
				// > this are in the previous century,
				// string value starting with "+" for current year + value
			minDate: null, // The earliest selectable date, or null for no limit
			maxDate: null, // The latest selectable date, or null for no limit
			duration: "fast", // Duration of display/closure
			beforeShowDay: null, // Function that takes a date and returns an array with
				// [0] = true if selectable, false if not, [1] = custom CSS class name(s) or "",
				// [2] = cell title (optional), e.g. $.datepicker.noWeekends
			beforeShow: null, // Function that takes an input field and
				// returns a set of custom settings for the date picker
			onSelect: null, // Define a callback function when a date is selected
			onChangeMonthYear: null, // Define a callback function when the month or year is changed
			onClose: null, // Define a callback function when the datepicker is closed
			numberOfMonths: 1, // Number of months to show at a time
			showCurrentAtPos: 0, // The position in multipe months at which to show the current month (starting at 0)
			stepMonths: 1, // Number of months to step back/forward
			stepBigMonths: 12, // Number of months to step back/forward for the big links
			altField: "", // Selector for an alternate field to store selected dates into
			altFormat: "", // The date format to use for the alternate field
			constrainInput: true, // The input is constrained by the current date format
			showButtonPanel: false, // True to show button panel, false to not show it
			autoSize: false, // True to size the input for the date format, false to leave as is
			disabled: false // The initial disabled state
		};
		$.extend(this._defaults, this.regional[""]);
		this.dpDiv = bindHover($("<div id='" + this._mainDivId + "' class='ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all'></div>"));
	}

	$.extend(Datepicker.prototype, {
		/* Class name added to elements to indicate already configured with a date picker. */
		markerClassName: "hasDatepicker",

		//Keep track of the maximum number of rows displayed (see #7043)
		maxRows: 4,

		// TODO rename to "widget" when switching to widget factory
		_widgetDatepicker: function() {
			return this.dpDiv;
		},

		/* Override the default settings for all instances of the date picker.
		 * @param  settings  object - the new settings to use as defaults (anonymous object)
		 * @return the manager object
		 */
		setDefaults: function(settings) {
			extendRemove(this._defaults, settings || {});
			return this;
		},

		/* Attach the date picker to a jQuery selection.
		 * @param  target	element - the target input field or division or span
		 * @param  settings  object - the new settings to use for this date picker instance (anonymous)
		 */
		_attachDatepicker: function(target, settings) {
			var nodeName, inline, inst;
			nodeName = target.nodeName.toLowerCase();
			inline = (nodeName === "div" || nodeName === "span");
			if (!target.id) {
				this.uuid += 1;
				target.id = "dp" + this.uuid;
			}
			inst = this._newInst($(target), inline);
			inst.settings = $.extend({}, settings || {});
			if (nodeName === "input") {
				this._connectDatepicker(target, inst);
			} else if (inline) {
				this._inlineDatepicker(target, inst);
			}
		},

		/* Create a new instance object. */
		_newInst: function(target, inline) {
			var id = target[0].id.replace(/([^A-Za-z0-9_\-])/g, "\\\\$1"); // escape jQuery meta chars
			return {id: id, input: target, // associated target
				selectedDay: 0, selectedMonth: 0, selectedYear: 0, // current selection
				drawMonth: 0, drawYear: 0, // month being drawn
				inline: inline, // is datepicker inline or not
				dpDiv: (!inline ? this.dpDiv : // presentation div
				bindHover($("<div class='" + this._inlineClass + " ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all'></div>")))};
		},

		/* Attach the date picker to an input field. */
		_connectDatepicker: function(target, inst) {
			var input = $(target);
			inst.append = $([]);
			inst.trigger = $([]);
			if (input.hasClass(this.markerClassName)) {
				return;
			}
			this._attachments(input, inst);
			input.addClass(this.markerClassName).keydown(this._doKeyDown).
				keypress(this._doKeyPress).keyup(this._doKeyUp);
			this._autoSize(inst);
			$.data(target, PROP_NAME, inst);
			//If disabled option is true, disable the datepicker once it has been attached to the input (see ticket #5665)
			if( inst.settings.disabled ) {
				this._disableDatepicker( target );
			}
		},

		/* Make attachments based on settings. */
		_attachments: function(input, inst) {
			var showOn, buttonText, buttonImage,
				appendText = this._get(inst, "appendText"),
				isRTL = this._get(inst, "isRTL");

			if (inst.append) {
				inst.append.remove();
			}
			if (appendText) {
				inst.append = $("<span class='" + this._appendClass + "'>" + appendText + "</span>");
				input[isRTL ? "before" : "after"](inst.append);
			}

			input.unbind("focus", this._showDatepicker);

			if (inst.trigger) {
				inst.trigger.remove();
			}

			showOn = this._get(inst, "showOn");
			if (showOn === "focus" || showOn === "both") { // pop-up date picker when in the marked field
				input.focus(this._showDatepicker);
			}
			if (showOn === "button" || showOn === "both") { // pop-up date picker when button clicked
				buttonText = this._get(inst, "buttonText");
				buttonImage = this._get(inst, "buttonImage");
				inst.trigger = $(this._get(inst, "buttonImageOnly") ?
					$("<img/>").addClass(this._triggerClass).
						attr({ src: buttonImage, alt: buttonText, title: buttonText }) :
					$("<button type='button'></button>").addClass(this._triggerClass).
						html(!buttonImage ? buttonText : $("<img/>").attr(
						{ src:buttonImage, alt:buttonText, title:buttonText })));
				input[isRTL ? "before" : "after"](inst.trigger);
				inst.trigger.click(function() {
					if ($.datepicker._datepickerShowing && $.datepicker._lastInput === input[0]) {
						$.datepicker._hideDatepicker();
					} else if ($.datepicker._datepickerShowing && $.datepicker._lastInput !== input[0]) {
						$.datepicker._hideDatepicker();
						$.datepicker._showDatepicker(input[0]);
					} else {
						$.datepicker._showDatepicker(input[0]);
					}
					return false;
				});
			}
		},

		/* Apply the maximum length for the date format. */
		_autoSize: function(inst) {
			if (this._get(inst, "autoSize") && !inst.inline) {
				var findMax, max, maxI, i,
					date = new Date(2009, 12 - 1, 20), // Ensure double digits
					dateFormat = this._get(inst, "dateFormat");

				if (dateFormat.match(/[DM]/)) {
					findMax = function(names) {
						max = 0;
						maxI = 0;
						for (i = 0; i < names.length; i++) {
							if (names[i].length > max) {
								max = names[i].length;
								maxI = i;
							}
						}
						return maxI;
					};
					date.setMonth(findMax(this._get(inst, (dateFormat.match(/MM/) ?
						"monthNames" : "monthNamesShort"))));
					date.setDate(findMax(this._get(inst, (dateFormat.match(/DD/) ?
						"dayNames" : "dayNamesShort"))) + 20 - date.getDay());
				}
				inst.input.attr("size", this._formatDate(inst, date).length);
			}
		},

		/* Attach an inline date picker to a div. */
		_inlineDatepicker: function(target, inst) {
			var divSpan = $(target);
			if (divSpan.hasClass(this.markerClassName)) {
				return;
			}
			divSpan.addClass(this.markerClassName).append(inst.dpDiv);
			$.data(target, PROP_NAME, inst);
			this._setDate(inst, this._getDefaultDate(inst), true);
			this._updateDatepicker(inst);
			this._updateAlternate(inst);
			//If disabled option is true, disable the datepicker before showing it (see ticket #5665)
			if( inst.settings.disabled ) {
				this._disableDatepicker( target );
			}
			// Set display:block in place of inst.dpDiv.show() which won't work on disconnected elements
			// http://bugs.jqueryui.com/ticket/7552 - A Datepicker created on a detached div has zero height
			inst.dpDiv.css( "display", "block" );
		},

		/* Pop-up the date picker in a "dialog" box.
		 * @param  input element - ignored
		 * @param  date	string or Date - the initial date to display
		 * @param  onSelect  function - the function to call when a date is selected
		 * @param  settings  object - update the dialog date picker instance's settings (anonymous object)
		 * @param  pos int[2] - coordinates for the dialog's position within the screen or
		 *					event - with x/y coordinates or
		 *					leave empty for default (screen centre)
		 * @return the manager object
		 */
		_dialogDatepicker: function(input, date, onSelect, settings, pos) {
			var id, browserWidth, browserHeight, scrollX, scrollY,
				inst = this._dialogInst; // internal instance

			if (!inst) {
				this.uuid += 1;
				id = "dp" + this.uuid;
				this._dialogInput = $("<input type='text' id='" + id +
					"' style='position: absolute; top: -100px; width: 0px;'/>");
				this._dialogInput.keydown(this._doKeyDown);
				$("body").append(this._dialogInput);
				inst = this._dialogInst = this._newInst(this._dialogInput, false);
				inst.settings = {};
				$.data(this._dialogInput[0], PROP_NAME, inst);
			}
			extendRemove(inst.settings, settings || {});
			date = (date && date.constructor === Date ? this._formatDate(inst, date) : date);
			this._dialogInput.val(date);

			this._pos = (pos ? (pos.length ? pos : [pos.pageX, pos.pageY]) : null);
			if (!this._pos) {
				browserWidth = document.documentElement.clientWidth;
				browserHeight = document.documentElement.clientHeight;
				scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
				scrollY = document.documentElement.scrollTop || document.body.scrollTop;
				this._pos = // should use actual width/height below
					[(browserWidth / 2) - 100 + scrollX, (browserHeight / 2) - 150 + scrollY];
			}

			// move input on screen for focus, but hidden behind dialog
			this._dialogInput.css("left", (this._pos[0] + 20) + "px").css("top", this._pos[1] + "px");
			inst.settings.onSelect = onSelect;
			this._inDialog = true;
			this.dpDiv.addClass(this._dialogClass);
			this._showDatepicker(this._dialogInput[0]);
			if ($.blockUI) {
				$.blockUI(this.dpDiv);
			}
			$.data(this._dialogInput[0], PROP_NAME, inst);
			return this;
		},

		/* Detach a datepicker from its control.
		 * @param  target	element - the target input field or division or span
		 */
		_destroyDatepicker: function(target) {
			var nodeName,
				$target = $(target),
				inst = $.data(target, PROP_NAME);

			if (!$target.hasClass(this.markerClassName)) {
				return;
			}

			nodeName = target.nodeName.toLowerCase();
			$.removeData(target, PROP_NAME);
			if (nodeName === "input") {
				inst.append.remove();
				inst.trigger.remove();
				$target.removeClass(this.markerClassName).
					unbind("focus", this._showDatepicker).
					unbind("keydown", this._doKeyDown).
					unbind("keypress", this._doKeyPress).
					unbind("keyup", this._doKeyUp);
			} else if (nodeName === "div" || nodeName === "span") {
				$target.removeClass(this.markerClassName).empty();
			}
		},

		/* Enable the date picker to a jQuery selection.
		 * @param  target	element - the target input field or division or span
		 */
		_enableDatepicker: function(target) {
			var nodeName, inline,
				$target = $(target),
				inst = $.data(target, PROP_NAME);

			if (!$target.hasClass(this.markerClassName)) {
				return;
			}

			nodeName = target.nodeName.toLowerCase();
			if (nodeName === "input") {
				target.disabled = false;
				inst.trigger.filter("button").
					each(function() { this.disabled = false; }).end().
					filter("img").css({opacity: "1.0", cursor: ""});
			} else if (nodeName === "div" || nodeName === "span") {
				inline = $target.children("." + this._inlineClass);
				inline.children().removeClass("ui-state-disabled");
				inline.find("select.ui-datepicker-month, select.ui-datepicker-year").
					prop("disabled", false);
			}
			this._disabledInputs = $.map(this._disabledInputs,
				function(value) { return (value === target ? null : value); }); // delete entry
		},

		/* Disable the date picker to a jQuery selection.
		 * @param  target	element - the target input field or division or span
		 */
		_disableDatepicker: function(target) {
			var nodeName, inline,
				$target = $(target),
				inst = $.data(target, PROP_NAME);

			if (!$target.hasClass(this.markerClassName)) {
				return;
			}

			nodeName = target.nodeName.toLowerCase();
			if (nodeName === "input") {
				target.disabled = true;
				inst.trigger.filter("button").
					each(function() { this.disabled = true; }).end().
					filter("img").css({opacity: "0.5", cursor: "default"});
			} else if (nodeName === "div" || nodeName === "span") {
				inline = $target.children("." + this._inlineClass);
				inline.children().addClass("ui-state-disabled");
				inline.find("select.ui-datepicker-month, select.ui-datepicker-year").
					prop("disabled", true);
			}
			this._disabledInputs = $.map(this._disabledInputs,
				function(value) { return (value === target ? null : value); }); // delete entry
			this._disabledInputs[this._disabledInputs.length] = target;
		},

		/* Is the first field in a jQuery collection disabled as a datepicker?
		 * @param  target	element - the target input field or division or span
		 * @return boolean - true if disabled, false if enabled
		 */
		_isDisabledDatepicker: function(target) {
			if (!target) {
				return false;
			}
			for (var i = 0; i < this._disabledInputs.length; i++) {
				if (this._disabledInputs[i] === target) {
					return true;
				}
			}
			return false;
		},

		/* Retrieve the instance data for the target control.
		 * @param  target  element - the target input field or division or span
		 * @return  object - the associated instance data
		 * @throws  error if a jQuery problem getting data
		 */
		_getInst: function(target) {
			try {
				return $.data(target, PROP_NAME);
			}
			catch (err) {
				throw "Missing instance data for this datepicker";
			}
		},

		/* Update or retrieve the settings for a date picker attached to an input field or division.
		 * @param  target  element - the target input field or division or span
		 * @param  name	object - the new settings to update or
		 *				string - the name of the setting to change or retrieve,
		 *				when retrieving also "all" for all instance settings or
		 *				"defaults" for all global defaults
		 * @param  value   any - the new value for the setting
		 *				(omit if above is an object or to retrieve a value)
		 */
		_optionDatepicker: function(target, name, value) {
			var settings, date, minDate, maxDate,
				inst = this._getInst(target);

			if (arguments.length === 2 && typeof name === "string") {
				return (name === "defaults" ? $.extend({}, $.datepicker._defaults) :
					(inst ? (name === "all" ? $.extend({}, inst.settings) :
					this._get(inst, name)) : null));
			}

			settings = name || {};
			if (typeof name === "string") {
				settings = {};
				settings[name] = value;
			}

			if (inst) {
				if (this._curInst === inst) {
					this._hideDatepicker();
				}

				date = this._getDateDatepicker(target, true);
				minDate = this._getMinMaxDate(inst, "min");
				maxDate = this._getMinMaxDate(inst, "max");
				extendRemove(inst.settings, settings);
				// reformat the old minDate/maxDate values if dateFormat changes and a new minDate/maxDate isn't provided
				if (minDate !== null && settings.dateFormat !== undefined && settings.minDate === undefined) {
					inst.settings.minDate = this._formatDate(inst, minDate);
				}
				if (maxDate !== null && settings.dateFormat !== undefined && settings.maxDate === undefined) {
					inst.settings.maxDate = this._formatDate(inst, maxDate);
				}
				if ( "disabled" in settings ) {
					if ( settings.disabled ) {
						this._disableDatepicker(target);
					} else {
						this._enableDatepicker(target);
					}
				}
				this._attachments($(target), inst);
				this._autoSize(inst);
				this._setDate(inst, date);
				this._updateAlternate(inst);
				this._updateDatepicker(inst);
			}
		},

		// change method deprecated
		_changeDatepicker: function(target, name, value) {
			this._optionDatepicker(target, name, value);
		},

		/* Redraw the date picker attached to an input field or division.
		 * @param  target  element - the target input field or division or span
		 */
		_refreshDatepicker: function(target) {
			var inst = this._getInst(target);
			if (inst) {
				this._updateDatepicker(inst);
			}
		},

		/* Set the dates for a jQuery selection.
		 * @param  target element - the target input field or division or span
		 * @param  date	Date - the new date
		 */
		_setDateDatepicker: function(target, date) {
			var inst = this._getInst(target);
			if (inst) {
				this._setDate(inst, date);
				this._updateDatepicker(inst);
				this._updateAlternate(inst);
			}
		},

		/* Get the date(s) for the first entry in a jQuery selection.
		 * @param  target element - the target input field or division or span
		 * @param  noDefault boolean - true if no default date is to be used
		 * @return Date - the current date
		 */
		_getDateDatepicker: function(target, noDefault) {
			var inst = this._getInst(target);
			if (inst && !inst.inline) {
				this._setDateFromField(inst, noDefault);
			}
			return (inst ? this._getDate(inst) : null);
		},

		/* Handle keystrokes. */
		_doKeyDown: function(event) {
			var onSelect, dateStr, sel,
				inst = $.datepicker._getInst(event.target),
				handled = true,
				isRTL = inst.dpDiv.is(".ui-datepicker-rtl");

			inst._keyEvent = true;
			if ($.datepicker._datepickerShowing) {
				switch (event.keyCode) {
					case 9: $.datepicker._hideDatepicker();
							handled = false;
							break; // hide on tab out
					case 13: sel = $("td." + $.datepicker._dayOverClass + ":not(." +
										$.datepicker._currentClass + ")", inst.dpDiv);
							if (sel[0]) {
								$.datepicker._selectDay(event.target, inst.selectedMonth, inst.selectedYear, sel[0]);
							}

							onSelect = $.datepicker._get(inst, "onSelect");
							if (onSelect) {
								dateStr = $.datepicker._formatDate(inst);

								// trigger custom callback
								onSelect.apply((inst.input ? inst.input[0] : null), [dateStr, inst]);
							} else {
								$.datepicker._hideDatepicker();
							}

							return false; // don't submit the form
					case 27: $.datepicker._hideDatepicker();
							break; // hide on escape
					case 33: $.datepicker._adjustDate(event.target, (event.ctrlKey ?
								-$.datepicker._get(inst, "stepBigMonths") :
								-$.datepicker._get(inst, "stepMonths")), "M");
							break; // previous month/year on page up/+ ctrl
					case 34: $.datepicker._adjustDate(event.target, (event.ctrlKey ?
								+$.datepicker._get(inst, "stepBigMonths") :
								+$.datepicker._get(inst, "stepMonths")), "M");
							break; // next month/year on page down/+ ctrl
					case 35: if (event.ctrlKey || event.metaKey) {
								$.datepicker._clearDate(event.target);
							}
							handled = event.ctrlKey || event.metaKey;
							break; // clear on ctrl or command +end
					case 36: if (event.ctrlKey || event.metaKey) {
								$.datepicker._gotoToday(event.target);
							}
							handled = event.ctrlKey || event.metaKey;
							break; // current on ctrl or command +home
					case 37: if (event.ctrlKey || event.metaKey) {
								$.datepicker._adjustDate(event.target, (isRTL ? +1 : -1), "D");
							}
							handled = event.ctrlKey || event.metaKey;
							// -1 day on ctrl or command +left
							if (event.originalEvent.altKey) {
								$.datepicker._adjustDate(event.target, (event.ctrlKey ?
									-$.datepicker._get(inst, "stepBigMonths") :
									-$.datepicker._get(inst, "stepMonths")), "M");
							}
							// next month/year on alt +left on Mac
							break;
					case 38: if (event.ctrlKey || event.metaKey) {
								$.datepicker._adjustDate(event.target, -7, "D");
							}
							handled = event.ctrlKey || event.metaKey;
							break; // -1 week on ctrl or command +up
					case 39: if (event.ctrlKey || event.metaKey) {
								$.datepicker._adjustDate(event.target, (isRTL ? -1 : +1), "D");
							}
							handled = event.ctrlKey || event.metaKey;
							// +1 day on ctrl or command +right
							if (event.originalEvent.altKey) {
								$.datepicker._adjustDate(event.target, (event.ctrlKey ?
									+$.datepicker._get(inst, "stepBigMonths") :
									+$.datepicker._get(inst, "stepMonths")), "M");
							}
							// next month/year on alt +right
							break;
					case 40: if (event.ctrlKey || event.metaKey) {
								$.datepicker._adjustDate(event.target, +7, "D");
							}
							handled = event.ctrlKey || event.metaKey;
							break; // +1 week on ctrl or command +down
					default: handled = false;
				}
			} else if (event.keyCode === 36 && event.ctrlKey) { // display the date picker on ctrl+home
				$.datepicker._showDatepicker(this);
			} else {
				handled = false;
			}

			if (handled) {
				event.preventDefault();
				event.stopPropagation();
			}
		},

		/* Filter entered characters - based on date format. */
		_doKeyPress: function(event) {
			var chars, chr,
				inst = $.datepicker._getInst(event.target);

			if ($.datepicker._get(inst, "constrainInput")) {
				chars = $.datepicker._possibleChars($.datepicker._get(inst, "dateFormat"));
				chr = String.fromCharCode(event.charCode == null ? event.keyCode : event.charCode);
				return event.ctrlKey || event.metaKey || (chr < " " || !chars || chars.indexOf(chr) > -1);
			}
		},

		/* Synchronise manual entry and field/alternate field. */
		_doKeyUp: function(event) {
			var date,
				inst = $.datepicker._getInst(event.target);

			if (inst.input.val() !== inst.lastVal) {
				try {
					date = $.datepicker.parseDate($.datepicker._get(inst, "dateFormat"),
						(inst.input ? inst.input.val() : null),
						$.datepicker._getFormatConfig(inst));

					if (date) { // only if valid
						$.datepicker._setDateFromField(inst);
						$.datepicker._updateAlternate(inst);
						$.datepicker._updateDatepicker(inst);
					}
				}
				catch (err) {
				}
			}
			return true;
		},

		/* Pop-up the date picker for a given input field.
		 * If false returned from beforeShow event handler do not show.
		 * @param  input  element - the input field attached to the date picker or
		 *					event - if triggered by focus
		 */
		_showDatepicker: function(input) {
			input = input.target || input;
			if (input.nodeName.toLowerCase() !== "input") { // find from button/image trigger
				input = $("input", input.parentNode)[0];
			}

			if ($.datepicker._isDisabledDatepicker(input) || $.datepicker._lastInput === input) { // already here
				return;
			}

			var inst, beforeShow, beforeShowSettings, isFixed,
				offset, showAnim, duration;

			inst = $.datepicker._getInst(input);
			if ($.datepicker._curInst && $.datepicker._curInst !== inst) {
				$.datepicker._curInst.dpDiv.stop(true, true);
				if ( inst && $.datepicker._datepickerShowing ) {
					$.datepicker._hideDatepicker( $.datepicker._curInst.input[0] );
				}
			}

			beforeShow = $.datepicker._get(inst, "beforeShow");
			beforeShowSettings = beforeShow ? beforeShow.apply(input, [input, inst]) : {};
			if(beforeShowSettings === false){
				return;
			}
			extendRemove(inst.settings, beforeShowSettings);

			inst.lastVal = null;
			$.datepicker._lastInput = input;
			$.datepicker._setDateFromField(inst);

			if ($.datepicker._inDialog) { // hide cursor
				input.value = "";
			}
			if (!$.datepicker._pos) { // position below input
				$.datepicker._pos = $.datepicker._findPos(input);
				$.datepicker._pos[1] += input.offsetHeight; // add the height
			}

			isFixed = false;
			$(input).parents().each(function() {
				isFixed |= $(this).css("position") === "fixed";
				return !isFixed;
			});

			offset = {left: $.datepicker._pos[0], top: $.datepicker._pos[1]};
			$.datepicker._pos = null;
			//to avoid flashes on Firefox
			inst.dpDiv.empty();
			// determine sizing offscreen
			inst.dpDiv.css({position: "absolute", display: "block", top: "-1000px"});
			$.datepicker._updateDatepicker(inst);
			// fix width for dynamic number of date pickers
			// and adjust position before showing
			offset = $.datepicker._checkOffset(inst, offset, isFixed);
			inst.dpDiv.css({position: ($.datepicker._inDialog && $.blockUI ?
				"static" : (isFixed ? "fixed" : "absolute")), display: "none",
				left: offset.left + "px", top: offset.top + "px"});

			if (!inst.inline) {
				showAnim = $.datepicker._get(inst, "showAnim");
				duration = $.datepicker._get(inst, "duration");
				inst.dpDiv.zIndex($(input).zIndex()+1);
				$.datepicker._datepickerShowing = true;

				if ( $.effects && $.effects.effect[ showAnim ] ) {
					inst.dpDiv.show(showAnim, $.datepicker._get(inst, "showOptions"), duration);
				} else {
					inst.dpDiv[showAnim || "show"](showAnim ? duration : null);
				}

				if ( $.datepicker._shouldFocusInput( inst ) ) {
					inst.input.focus();
				}

				$.datepicker._curInst = inst;
			}
		},

		/* Generate the date picker content. */
		_updateDatepicker: function(inst) {
			this.maxRows = 4; //Reset the max number of rows being displayed (see #7043)
			instActive = inst; // for delegate hover events
			inst.dpDiv.empty().append(this._generateHTML(inst));
			this._attachHandlers(inst);
			inst.dpDiv.find("." + this._dayOverClass + " a").mouseover();

			var origyearshtml,
				numMonths = this._getNumberOfMonths(inst),
				cols = numMonths[1],
				width = 17;

			inst.dpDiv.removeClass("ui-datepicker-multi-2 ui-datepicker-multi-3 ui-datepicker-multi-4").width("");
			if (cols > 1) {
				inst.dpDiv.addClass("ui-datepicker-multi-" + cols).css("width", (width * cols) + "em");
			}
			inst.dpDiv[(numMonths[0] !== 1 || numMonths[1] !== 1 ? "add" : "remove") +
				"Class"]("ui-datepicker-multi");
			inst.dpDiv[(this._get(inst, "isRTL") ? "add" : "remove") +
				"Class"]("ui-datepicker-rtl");

			if (inst === $.datepicker._curInst && $.datepicker._datepickerShowing && $.datepicker._shouldFocusInput( inst ) ) {
				inst.input.focus();
			}

			// deffered render of the years select (to avoid flashes on Firefox)
			if( inst.yearshtml ){
				origyearshtml = inst.yearshtml;
				setTimeout(function(){
					//assure that inst.yearshtml didn't change.
					if( origyearshtml === inst.yearshtml && inst.yearshtml ){
						inst.dpDiv.find("select.ui-datepicker-year:first").replaceWith(inst.yearshtml);
					}
					origyearshtml = inst.yearshtml = null;
				}, 0);
			}
		},

		// #6694 - don't focus the input if it's already focused
		// this breaks the change event in IE
		// Support: IE and jQuery <1.9
		_shouldFocusInput: function( inst ) {
			return inst.input && inst.input.is( ":visible" ) && !inst.input.is( ":disabled" ) && !inst.input.is( ":focus" );
		},

		/* Check positioning to remain on screen. */
		_checkOffset: function(inst, offset, isFixed) {
			var dpWidth = inst.dpDiv.outerWidth(),
				dpHeight = inst.dpDiv.outerHeight(),
				inputWidth = inst.input ? inst.input.outerWidth() : 0,
				inputHeight = inst.input ? inst.input.outerHeight() : 0,
				viewWidth = document.documentElement.clientWidth + (isFixed ? 0 : $(document).scrollLeft()),
				viewHeight = document.documentElement.clientHeight + (isFixed ? 0 : $(document).scrollTop());

			offset.left -= (this._get(inst, "isRTL") ? (dpWidth - inputWidth) : 0);
			offset.left -= (isFixed && offset.left === inst.input.offset().left) ? $(document).scrollLeft() : 0;
			offset.top -= (isFixed && offset.top === (inst.input.offset().top + inputHeight)) ? $(document).scrollTop() : 0;

			// now check if datepicker is showing outside window viewport - move to a better place if so.
			offset.left -= Math.min(offset.left, (offset.left + dpWidth > viewWidth && viewWidth > dpWidth) ?
				Math.abs(offset.left + dpWidth - viewWidth) : 0);
			offset.top -= Math.min(offset.top, (offset.top + dpHeight > viewHeight && viewHeight > dpHeight) ?
				Math.abs(dpHeight + inputHeight) : 0);

			return offset;
		},

		/* Find an object's position on the screen. */
		_findPos: function(obj) {
			var position,
				inst = this._getInst(obj),
				isRTL = this._get(inst, "isRTL");

			while (obj && (obj.type === "hidden" || obj.nodeType !== 1 || $.expr.filters.hidden(obj))) {
				obj = obj[isRTL ? "previousSibling" : "nextSibling"];
			}

			position = $(obj).offset();
			return [position.left, position.top];
		},

		/* Hide the date picker from view.
		 * @param  input  element - the input field attached to the date picker
		 */
		_hideDatepicker: function(input) {
			var showAnim, duration, postProcess, onClose,
				inst = this._curInst;

			if (!inst || (input && inst !== $.data(input, PROP_NAME))) {
				return;
			}

			if (this._datepickerShowing) {
				showAnim = this._get(inst, "showAnim");
				duration = this._get(inst, "duration");
				postProcess = function() {
					$.datepicker._tidyDialog(inst);
				};

				// DEPRECATED: after BC for 1.8.x $.effects[ showAnim ] is not needed
				if ( $.effects && ( $.effects.effect[ showAnim ] || $.effects[ showAnim ] ) ) {
					inst.dpDiv.hide(showAnim, $.datepicker._get(inst, "showOptions"), duration, postProcess);
				} else {
					inst.dpDiv[(showAnim === "slideDown" ? "slideUp" :
						(showAnim === "fadeIn" ? "fadeOut" : "hide"))]((showAnim ? duration : null), postProcess);
				}

				if (!showAnim) {
					postProcess();
				}
				this._datepickerShowing = false;

				onClose = this._get(inst, "onClose");
				if (onClose) {
					onClose.apply((inst.input ? inst.input[0] : null), [(inst.input ? inst.input.val() : ""), inst]);
				}

				this._lastInput = null;
				if (this._inDialog) {
					this._dialogInput.css({ position: "absolute", left: "0", top: "-100px" });
					if ($.blockUI) {
						$.unblockUI();
						$("body").append(this.dpDiv);
					}
				}
				this._inDialog = false;
			}
		},

		/* Tidy up after a dialog display. */
		_tidyDialog: function(inst) {
			inst.dpDiv.removeClass(this._dialogClass).unbind(".ui-datepicker-calendar");
		},

		/* Close date picker if clicked elsewhere. */
		_checkExternalClick: function(event) {
			if (!$.datepicker._curInst) {
				return;
			}

			var $target = $(event.target),
				inst = $.datepicker._getInst($target[0]);

			if ( ( ( $target[0].id !== $.datepicker._mainDivId &&
					$target.parents("#" + $.datepicker._mainDivId).length === 0 &&
					!$target.hasClass($.datepicker.markerClassName) &&
					!$target.closest("." + $.datepicker._triggerClass).length &&
					$.datepicker._datepickerShowing && !($.datepicker._inDialog && $.blockUI) ) ) ||
				( $target.hasClass($.datepicker.markerClassName) && $.datepicker._curInst !== inst ) ) {
					$.datepicker._hideDatepicker();
			}
		},

		/* Adjust one of the date sub-fields. */
		_adjustDate: function(id, offset, period) {
			var target = $(id),
				inst = this._getInst(target[0]);

			if (this._isDisabledDatepicker(target[0])) {
				return;
			}
			this._adjustInstDate(inst, offset +
				(period === "M" ? this._get(inst, "showCurrentAtPos") : 0), // undo positioning
				period);
			this._updateDatepicker(inst);
		},

		/* Action for current link. */
		_gotoToday: function(id) {
			var date,
				target = $(id),
				inst = this._getInst(target[0]);

			if (this._get(inst, "gotoCurrent") && inst.currentDay) {
				inst.selectedDay = inst.currentDay;
				inst.drawMonth = inst.selectedMonth = inst.currentMonth;
				inst.drawYear = inst.selectedYear = inst.currentYear;
			} else {
				date = new Date();
				inst.selectedDay = date.getDate();
				inst.drawMonth = inst.selectedMonth = date.getMonth();
				inst.drawYear = inst.selectedYear = date.getFullYear();
			}
			this._notifyChange(inst);
			this._adjustDate(target);
		},

		/* Action for selecting a new month/year. */
		_selectMonthYear: function(id, select, period) {
			var target = $(id),
				inst = this._getInst(target[0]);

			inst["selected" + (period === "M" ? "Month" : "Year")] =
			inst["draw" + (period === "M" ? "Month" : "Year")] =
				parseInt(select.options[select.selectedIndex].value,10);

			this._notifyChange(inst);
			this._adjustDate(target);
		},

		/* Action for selecting a day. */
		_selectDay: function(id, month, year, td) {
			var inst,
				target = $(id);

			if ($(td).hasClass(this._unselectableClass) || this._isDisabledDatepicker(target[0])) {
				return;
			}

			inst = this._getInst(target[0]);
			inst.selectedDay = inst.currentDay = $("a", td).html();
			inst.selectedMonth = inst.currentMonth = month;
			inst.selectedYear = inst.currentYear = year;
			this._selectDate(id, this._formatDate(inst,
				inst.currentDay, inst.currentMonth, inst.currentYear));
		},

		/* Erase the input field and hide the date picker. */
		_clearDate: function(id) {
			var target = $(id);
			this._selectDate(target, "");
		},

		/* Update the input field with the selected date. */
		_selectDate: function(id, dateStr) {
			var onSelect,
				target = $(id),
				inst = this._getInst(target[0]);

			dateStr = (dateStr != null ? dateStr : this._formatDate(inst));
			if (inst.input) {
				inst.input.val(dateStr);
			}
			this._updateAlternate(inst);

			onSelect = this._get(inst, "onSelect");
			if (onSelect) {
				onSelect.apply((inst.input ? inst.input[0] : null), [dateStr, inst]);  // trigger custom callback
			} else if (inst.input) {
				inst.input.trigger("change"); // fire the change event
			}

			if (inst.inline){
				this._updateDatepicker(inst);
			} else {
				this._hideDatepicker();
				this._lastInput = inst.input[0];
				if (typeof(inst.input[0]) !== "object") {
					inst.input.focus(); // restore focus
				}
				this._lastInput = null;
			}
		},

		/* Update any alternate field to synchronise with the main field. */
		_updateAlternate: function(inst) {
			var altFormat, date, dateStr,
				altField = this._get(inst, "altField");

			if (altField) { // update alternate field too
				altFormat = this._get(inst, "altFormat") || this._get(inst, "dateFormat");
				date = this._getDate(inst);
				dateStr = this.formatDate(altFormat, date, this._getFormatConfig(inst));
				$(altField).each(function() { $(this).val(dateStr); });
			}
		},

		/* Set as beforeShowDay function to prevent selection of weekends.
		 * @param  date  Date - the date to customise
		 * @return [boolean, string] - is this date selectable?, what is its CSS class?
		 */
		noWeekends: function(date) {
			var day = date.getDay();
			return [(day > 0 && day < 6), ""];
		},

		/* Set as calculateWeek to determine the week of the year based on the ISO 8601 definition.
		 * @param  date  Date - the date to get the week for
		 * @return  number - the number of the week within the year that contains this date
		 */
		iso8601Week: function(date) {
			var time,
				checkDate = new Date(date.getTime());

			// Find Thursday of this week starting on Monday
			checkDate.setDate(checkDate.getDate() + 4 - (checkDate.getDay() || 7));

			time = checkDate.getTime();
			checkDate.setMonth(0); // Compare with Jan 1
			checkDate.setDate(1);
			return Math.floor(Math.round((time - checkDate) / 86400000) / 7) + 1;
		},

		/* Parse a string value into a date object.
		 * See formatDate below for the possible formats.
		 *
		 * @param  format string - the expected format of the date
		 * @param  value string - the date in the above format
		 * @param  settings Object - attributes include:
		 *					shortYearCutoff  number - the cutoff year for determining the century (optional)
		 *					dayNamesShort	string[7] - abbreviated names of the days from Sunday (optional)
		 *					dayNames		string[7] - names of the days from Sunday (optional)
		 *					monthNamesShort string[12] - abbreviated names of the months (optional)
		 *					monthNames		string[12] - names of the months (optional)
		 * @return  Date - the extracted date value or null if value is blank
		 */
		parseDate: function (format, value, settings) {
			if (format == null || value == null) {
				throw "Invalid arguments";
			}

			value = (typeof value === "object" ? value.toString() : value + "");
			if (value === "") {
				return null;
			}

			var iFormat, dim, extra,
				iValue = 0,
				shortYearCutoffTemp = (settings ? settings.shortYearCutoff : null) || this._defaults.shortYearCutoff,
				shortYearCutoff = (typeof shortYearCutoffTemp !== "string" ? shortYearCutoffTemp :
					new Date().getFullYear() % 100 + parseInt(shortYearCutoffTemp, 10)),
				dayNamesShort = (settings ? settings.dayNamesShort : null) || this._defaults.dayNamesShort,
				dayNames = (settings ? settings.dayNames : null) || this._defaults.dayNames,
				monthNamesShort = (settings ? settings.monthNamesShort : null) || this._defaults.monthNamesShort,
				monthNames = (settings ? settings.monthNames : null) || this._defaults.monthNames,
				year = -1,
				month = -1,
				day = -1,
				doy = -1,
				literal = false,
				date,
				// Check whether a format character is doubled
				lookAhead = function(match) {
					var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) === match);
					if (matches) {
						iFormat++;
					}
					return matches;
				},
				// Extract a number from the string value
				getNumber = function(match) {
					var isDoubled = lookAhead(match),
						size = (match === "@" ? 14 : (match === "!" ? 20 :
						(match === "y" && isDoubled ? 4 : (match === "o" ? 3 : 2)))),
						digits = new RegExp("^\\d{1," + size + "}"),
						num = value.substring(iValue).match(digits);
					if (!num) {
						throw "Missing number at position " + iValue;
					}
					iValue += num[0].length;
					return parseInt(num[0], 10);
				},
				// Extract a name from the string value and convert to an index
				getName = function(match, shortNames, longNames) {
					var index = -1,
						names = $.map(lookAhead(match) ? longNames : shortNames, function (v, k) {
							return [ [k, v] ];
						}).sort(function (a, b) {
							return -(a[1].length - b[1].length);
						});

					$.each(names, function (i, pair) {
						var name = pair[1];
						if (value.substr(iValue, name.length).toLowerCase() === name.toLowerCase()) {
							index = pair[0];
							iValue += name.length;
							return false;
						}
					});
					if (index !== -1) {
						return index + 1;
					} else {
						throw "Unknown name at position " + iValue;
					}
				},
				// Confirm that a literal character matches the string value
				checkLiteral = function() {
					if (value.charAt(iValue) !== format.charAt(iFormat)) {
						throw "Unexpected literal at position " + iValue;
					}
					iValue++;
				};

			for (iFormat = 0; iFormat < format.length; iFormat++) {
				if (literal) {
					if (format.charAt(iFormat) === "'" && !lookAhead("'")) {
						literal = false;
					} else {
						checkLiteral();
					}
				} else {
					switch (format.charAt(iFormat)) {
						case "d":
							day = getNumber("d");
							break;
						case "D":
							getName("D", dayNamesShort, dayNames);
							break;
						case "o":
							doy = getNumber("o");
							break;
						case "m":
							month = getNumber("m");
							break;
						case "M":
							month = getName("M", monthNamesShort, monthNames);
							break;
						case "y":
							year = getNumber("y");
							break;
						case "@":
							date = new Date(getNumber("@"));
							year = date.getFullYear();
							month = date.getMonth() + 1;
							day = date.getDate();
							break;
						case "!":
							date = new Date((getNumber("!") - this._ticksTo1970) / 10000);
							year = date.getFullYear();
							month = date.getMonth() + 1;
							day = date.getDate();
							break;
						case "'":
							if (lookAhead("'")){
								checkLiteral();
							} else {
								literal = true;
							}
							break;
						default:
							checkLiteral();
					}
				}
			}

			if (iValue < value.length){
				extra = value.substr(iValue);
				if (!/^\s+/.test(extra)) {
					throw "Extra/unparsed characters found in date: " + extra;
				}
			}

			if (year === -1) {
				year = new Date().getFullYear();
			} else if (year < 100) {
				year += new Date().getFullYear() - new Date().getFullYear() % 100 +
					(year <= shortYearCutoff ? 0 : -100);
			}

			if (doy > -1) {
				month = 1;
				day = doy;
				do {
					dim = this._getDaysInMonth(year, month - 1);
					if (day <= dim) {
						break;
					}
					month++;
					day -= dim;
				} while (true);
			}

			date = this._daylightSavingAdjust(new Date(year, month - 1, day));
			if (date.getFullYear() !== year || date.getMonth() + 1 !== month || date.getDate() !== day) {
				throw "Invalid date"; // E.g. 31/02/00
			}
			return date;
		},

		/* Standard date formats. */
		ATOM: "yy-mm-dd", // RFC 3339 (ISO 8601)
		COOKIE: "D, dd M yy",
		ISO_8601: "yy-mm-dd",
		RFC_822: "D, d M y",
		RFC_850: "DD, dd-M-y",
		RFC_1036: "D, d M y",
		RFC_1123: "D, d M yy",
		RFC_2822: "D, d M yy",
		RSS: "D, d M y", // RFC 822
		TICKS: "!",
		TIMESTAMP: "@",
		W3C: "yy-mm-dd", // ISO 8601

		_ticksTo1970: (((1970 - 1) * 365 + Math.floor(1970 / 4) - Math.floor(1970 / 100) +
			Math.floor(1970 / 400)) * 24 * 60 * 60 * 10000000),

		/* Format a date object into a string value.
		 * The format can be combinations of the following:
		 * d  - day of month (no leading zero)
		 * dd - day of month (two digit)
		 * o  - day of year (no leading zeros)
		 * oo - day of year (three digit)
		 * D  - day name short
		 * DD - day name long
		 * m  - month of year (no leading zero)
		 * mm - month of year (two digit)
		 * M  - month name short
		 * MM - month name long
		 * y  - year (two digit)
		 * yy - year (four digit)
		 * @ - Unix timestamp (ms since 01/01/1970)
		 * ! - Windows ticks (100ns since 01/01/0001)
		 * "..." - literal text
		 * '' - single quote
		 *
		 * @param  format string - the desired format of the date
		 * @param  date Date - the date value to format
		 * @param  settings Object - attributes include:
		 *					dayNamesShort	string[7] - abbreviated names of the days from Sunday (optional)
		 *					dayNames		string[7] - names of the days from Sunday (optional)
		 *					monthNamesShort string[12] - abbreviated names of the months (optional)
		 *					monthNames		string[12] - names of the months (optional)
		 * @return  string - the date in the above format
		 */
		formatDate: function (format, date, settings) {
			if (!date) {
				return "";
			}

			var iFormat,
				dayNamesShort = (settings ? settings.dayNamesShort : null) || this._defaults.dayNamesShort,
				dayNames = (settings ? settings.dayNames : null) || this._defaults.dayNames,
				monthNamesShort = (settings ? settings.monthNamesShort : null) || this._defaults.monthNamesShort,
				monthNames = (settings ? settings.monthNames : null) || this._defaults.monthNames,
				// Check whether a format character is doubled
				lookAhead = function(match) {
					var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) === match);
					if (matches) {
						iFormat++;
					}
					return matches;
				},
				// Format a number, with leading zero if necessary
				formatNumber = function(match, value, len) {
					var num = "" + value;
					if (lookAhead(match)) {
						while (num.length < len) {
							num = "0" + num;
						}
					}
					return num;
				},
				// Format a name, short or long as requested
				formatName = function(match, value, shortNames, longNames) {
					return (lookAhead(match) ? longNames[value] : shortNames[value]);
				},
				output = "",
				literal = false;

			if (date) {
				for (iFormat = 0; iFormat < format.length; iFormat++) {
					if (literal) {
						if (format.charAt(iFormat) === "'" && !lookAhead("'")) {
							literal = false;
						} else {
							output += format.charAt(iFormat);
						}
					} else {
						switch (format.charAt(iFormat)) {
							case "d":
								output += formatNumber("d", date.getDate(), 2);
								break;
							case "D":
								output += formatName("D", date.getDay(), dayNamesShort, dayNames);
								break;
							case "o":
								output += formatNumber("o",
									Math.round((new Date(date.getFullYear(), date.getMonth(), date.getDate()).getTime() - new Date(date.getFullYear(), 0, 0).getTime()) / 86400000), 3);
								break;
							case "m":
								output += formatNumber("m", date.getMonth() + 1, 2);
								break;
							case "M":
								output += formatName("M", date.getMonth(), monthNamesShort, monthNames);
								break;
							case "y":
								output += (lookAhead("y") ? date.getFullYear() :
									(date.getYear() % 100 < 10 ? "0" : "") + date.getYear() % 100);
								break;
							case "@":
								output += date.getTime();
								break;
							case "!":
								output += date.getTime() * 10000 + this._ticksTo1970;
								break;
							case "'":
								if (lookAhead("'")) {
									output += "'";
								} else {
									literal = true;
								}
								break;
							default:
								output += format.charAt(iFormat);
						}
					}
				}
			}
			return output;
		},

		/* Extract all possible characters from the date format. */
		_possibleChars: function (format) {
			var iFormat,
				chars = "",
				literal = false,
				// Check whether a format character is doubled
				lookAhead = function(match) {
					var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) === match);
					if (matches) {
						iFormat++;
					}
					return matches;
				};

			for (iFormat = 0; iFormat < format.length; iFormat++) {
				if (literal) {
					if (format.charAt(iFormat) === "'" && !lookAhead("'")) {
						literal = false;
					} else {
						chars += format.charAt(iFormat);
					}
				} else {
					switch (format.charAt(iFormat)) {
						case "d": case "m": case "y": case "@":
							chars += "0123456789";
							break;
						case "D": case "M":
							return null; // Accept anything
						case "'":
							if (lookAhead("'")) {
								chars += "'";
							} else {
								literal = true;
							}
							break;
						default:
							chars += format.charAt(iFormat);
					}
				}
			}
			return chars;
		},

		/* Get a setting value, defaulting if necessary. */
		_get: function(inst, name) {
			return inst.settings[name] !== undefined ?
				inst.settings[name] : this._defaults[name];
		},

		/* Parse existing date and initialise date picker. */
		_setDateFromField: function(inst, noDefault) {
			if (inst.input.val() === inst.lastVal) {
				return;
			}

			var dateFormat = this._get(inst, "dateFormat"),
				dates = inst.lastVal = inst.input ? inst.input.val() : null,
				defaultDate = this._getDefaultDate(inst),
				date = defaultDate,
				settings = this._getFormatConfig(inst);

			try {
				date = this.parseDate(dateFormat, dates, settings) || defaultDate;
			} catch (event) {
				dates = (noDefault ? "" : dates);
			}
			inst.selectedDay = date.getDate();
			inst.drawMonth = inst.selectedMonth = date.getMonth();
			inst.drawYear = inst.selectedYear = date.getFullYear();
			inst.currentDay = (dates ? date.getDate() : 0);
			inst.currentMonth = (dates ? date.getMonth() : 0);
			inst.currentYear = (dates ? date.getFullYear() : 0);
			this._adjustInstDate(inst);
		},

		/* Retrieve the default date shown on opening. */
		_getDefaultDate: function(inst) {
			return this._restrictMinMax(inst,
				this._determineDate(inst, this._get(inst, "defaultDate"), new Date()));
		},

		/* A date may be specified as an exact value or a relative one. */
		_determineDate: function(inst, date, defaultDate) {
			var offsetNumeric = function(offset) {
					var date = new Date();
					date.setDate(date.getDate() + offset);
					return date;
				},
				offsetString = function(offset) {
					try {
						return $.datepicker.parseDate($.datepicker._get(inst, "dateFormat"),
							offset, $.datepicker._getFormatConfig(inst));
					}
					catch (e) {
						// Ignore
					}

					var date = (offset.toLowerCase().match(/^c/) ?
						$.datepicker._getDate(inst) : null) || new Date(),
						year = date.getFullYear(),
						month = date.getMonth(),
						day = date.getDate(),
						pattern = /([+\-]?[0-9]+)\s*(d|D|w|W|m|M|y|Y)?/g,
						matches = pattern.exec(offset);

					while (matches) {
						switch (matches[2] || "d") {
							case "d" : case "D" :
								day += parseInt(matches[1],10); break;
							case "w" : case "W" :
								day += parseInt(matches[1],10) * 7; break;
							case "m" : case "M" :
								month += parseInt(matches[1],10);
								day = Math.min(day, $.datepicker._getDaysInMonth(year, month));
								break;
							case "y": case "Y" :
								year += parseInt(matches[1],10);
								day = Math.min(day, $.datepicker._getDaysInMonth(year, month));
								break;
						}
						matches = pattern.exec(offset);
					}
					return new Date(year, month, day);
				},
				newDate = (date == null || date === "" ? defaultDate : (typeof date === "string" ? offsetString(date) :
					(typeof date === "number" ? (isNaN(date) ? defaultDate : offsetNumeric(date)) : new Date(date.getTime()))));

			newDate = (newDate && newDate.toString() === "Invalid Date" ? defaultDate : newDate);
			if (newDate) {
				newDate.setHours(0);
				newDate.setMinutes(0);
				newDate.setSeconds(0);
				newDate.setMilliseconds(0);
			}
			return this._daylightSavingAdjust(newDate);
		},

		/* Handle switch to/from daylight saving.
		 * Hours may be non-zero on daylight saving cut-over:
		 * > 12 when midnight changeover, but then cannot generate
		 * midnight datetime, so jump to 1AM, otherwise reset.
		 * @param  date  (Date) the date to check
		 * @return  (Date) the corrected date
		 */
		_daylightSavingAdjust: function(date) {
			if (!date) {
				return null;
			}
			date.setHours(date.getHours() > 12 ? date.getHours() + 2 : 0);
			return date;
		},

		/* Set the date(s) directly. */
		_setDate: function(inst, date, noChange) {
			var clear = !date,
				origMonth = inst.selectedMonth,
				origYear = inst.selectedYear,
				newDate = this._restrictMinMax(inst, this._determineDate(inst, date, new Date()));

			inst.selectedDay = inst.currentDay = newDate.getDate();
			inst.drawMonth = inst.selectedMonth = inst.currentMonth = newDate.getMonth();
			inst.drawYear = inst.selectedYear = inst.currentYear = newDate.getFullYear();
			if ((origMonth !== inst.selectedMonth || origYear !== inst.selectedYear) && !noChange) {
				this._notifyChange(inst);
			}
			this._adjustInstDate(inst);
			if (inst.input) {
				inst.input.val(clear ? "" : this._formatDate(inst));
			}
		},

		/* Retrieve the date(s) directly. */
		_getDate: function(inst) {
			var startDate = (!inst.currentYear || (inst.input && inst.input.val() === "") ? null :
				this._daylightSavingAdjust(new Date(
				inst.currentYear, inst.currentMonth, inst.currentDay)));
				return startDate;
		},

		/* Attach the onxxx handlers.  These are declared statically so
		 * they work with static code transformers like Caja.
		 */
		_attachHandlers: function(inst) {
			var stepMonths = this._get(inst, "stepMonths"),
				id = "#" + inst.id.replace( /\\\\/g, "\\" );
			inst.dpDiv.find("[data-handler]").map(function () {
				var handler = {
					prev: function () {
						$.datepicker._adjustDate(id, -stepMonths, "M");
					},
					next: function () {
						$.datepicker._adjustDate(id, +stepMonths, "M");
					},
					hide: function () {
						$.datepicker._hideDatepicker();
					},
					today: function () {
						$.datepicker._gotoToday(id);
					},
					selectDay: function () {
						$.datepicker._selectDay(id, +this.getAttribute("data-month"), +this.getAttribute("data-year"), this);
						return false;
					},
					selectMonth: function () {
						$.datepicker._selectMonthYear(id, this, "M");
						return false;
					},
					selectYear: function () {
						$.datepicker._selectMonthYear(id, this, "Y");
						return false;
					}
				};
				$(this).bind(this.getAttribute("data-event"), handler[this.getAttribute("data-handler")]);
			});
		},

		/* Generate the HTML for the current state of the date picker. */
		_generateHTML: function(inst) {
			var maxDraw, prevText, prev, nextText, next, currentText, gotoDate,
				controls, buttonPanel, firstDay, showWeek, dayNames, dayNamesMin,
				monthNames, monthNamesShort, beforeShowDay, showOtherMonths,
				selectOtherMonths, defaultDate, html, dow, row, group, col, selectedDate,
				cornerClass, calender, thead, day, daysInMonth, leadDays, curRows, numRows,
				printDate, dRow, tbody, daySettings, otherMonth, unselectable,
				tempDate = new Date(),
				today = this._daylightSavingAdjust(
					new Date(tempDate.getFullYear(), tempDate.getMonth(), tempDate.getDate())), // clear time
				isRTL = this._get(inst, "isRTL"),
				showButtonPanel = this._get(inst, "showButtonPanel"),
				hideIfNoPrevNext = this._get(inst, "hideIfNoPrevNext"),
				navigationAsDateFormat = this._get(inst, "navigationAsDateFormat"),
				numMonths = this._getNumberOfMonths(inst),
				showCurrentAtPos = this._get(inst, "showCurrentAtPos"),
				stepMonths = this._get(inst, "stepMonths"),
				isMultiMonth = (numMonths[0] !== 1 || numMonths[1] !== 1),
				currentDate = this._daylightSavingAdjust((!inst.currentDay ? new Date(9999, 9, 9) :
					new Date(inst.currentYear, inst.currentMonth, inst.currentDay))),
				minDate = this._getMinMaxDate(inst, "min"),
				maxDate = this._getMinMaxDate(inst, "max"),
				drawMonth = inst.drawMonth - showCurrentAtPos,
				drawYear = inst.drawYear;

			if (drawMonth < 0) {
				drawMonth += 12;
				drawYear--;
			}
			if (maxDate) {
				maxDraw = this._daylightSavingAdjust(new Date(maxDate.getFullYear(),
					maxDate.getMonth() - (numMonths[0] * numMonths[1]) + 1, maxDate.getDate()));
				maxDraw = (minDate && maxDraw < minDate ? minDate : maxDraw);
				while (this._daylightSavingAdjust(new Date(drawYear, drawMonth, 1)) > maxDraw) {
					drawMonth--;
					if (drawMonth < 0) {
						drawMonth = 11;
						drawYear--;
					}
				}
			}
			inst.drawMonth = drawMonth;
			inst.drawYear = drawYear;

			prevText = this._get(inst, "prevText");
			prevText = (!navigationAsDateFormat ? prevText : this.formatDate(prevText,
				this._daylightSavingAdjust(new Date(drawYear, drawMonth - stepMonths, 1)),
				this._getFormatConfig(inst)));

			prev = (this._canAdjustMonth(inst, -1, drawYear, drawMonth) ?
				"<a class='ui-datepicker-prev ui-corner-all' data-handler='prev' data-event='click'" +
				" title='" + prevText + "'><span class='ui-icon ui-icon-circle-triangle-" + ( isRTL ? "e" : "w") + "'>" + prevText + "</span></a>" :
				(hideIfNoPrevNext ? "" : "<a class='ui-datepicker-prev ui-corner-all ui-state-disabled' title='"+ prevText +"'><span class='ui-icon ui-icon-circle-triangle-" + ( isRTL ? "e" : "w") + "'>" + prevText + "</span></a>"));

			nextText = this._get(inst, "nextText");
			nextText = (!navigationAsDateFormat ? nextText : this.formatDate(nextText,
				this._daylightSavingAdjust(new Date(drawYear, drawMonth + stepMonths, 1)),
				this._getFormatConfig(inst)));

			next = (this._canAdjustMonth(inst, +1, drawYear, drawMonth) ?
				"<a class='ui-datepicker-next ui-corner-all' data-handler='next' data-event='click'" +
				" title='" + nextText + "'><span class='ui-icon ui-icon-circle-triangle-" + ( isRTL ? "w" : "e") + "'>" + nextText + "</span></a>" :
				(hideIfNoPrevNext ? "" : "<a class='ui-datepicker-next ui-corner-all ui-state-disabled' title='"+ nextText + "'><span class='ui-icon ui-icon-circle-triangle-" + ( isRTL ? "w" : "e") + "'>" + nextText + "</span></a>"));

			currentText = this._get(inst, "currentText");
			gotoDate = (this._get(inst, "gotoCurrent") && inst.currentDay ? currentDate : today);
			currentText = (!navigationAsDateFormat ? currentText :
				this.formatDate(currentText, gotoDate, this._getFormatConfig(inst)));

			controls = (!inst.inline ? "<button type='button' class='ui-datepicker-close ui-state-default ui-priority-primary ui-corner-all' data-handler='hide' data-event='click'>" +
				this._get(inst, "closeText") + "</button>" : "");

			buttonPanel = (showButtonPanel) ? "<div class='ui-datepicker-buttonpane ui-widget-content'>" + (isRTL ? controls : "") +
				(this._isInRange(inst, gotoDate) ? "<button type='button' class='ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all' data-handler='today' data-event='click'" +
				">" + currentText + "</button>" : "") + (isRTL ? "" : controls) + "</div>" : "";

			firstDay = parseInt(this._get(inst, "firstDay"),10);
			firstDay = (isNaN(firstDay) ? 0 : firstDay);

			showWeek = this._get(inst, "showWeek");
			dayNames = this._get(inst, "dayNames");
			dayNamesMin = this._get(inst, "dayNamesMin");
			monthNames = this._get(inst, "monthNames");
			monthNamesShort = this._get(inst, "monthNamesShort");
			beforeShowDay = this._get(inst, "beforeShowDay");
			showOtherMonths = this._get(inst, "showOtherMonths");
			selectOtherMonths = this._get(inst, "selectOtherMonths");
			defaultDate = this._getDefaultDate(inst);
			html = "";
			dow;
			for (row = 0; row < numMonths[0]; row++) {
				group = "";
				this.maxRows = 4;
				for (col = 0; col < numMonths[1]; col++) {
					selectedDate = this._daylightSavingAdjust(new Date(drawYear, drawMonth, inst.selectedDay));
					cornerClass = " ui-corner-all";
					calender = "";
					if (isMultiMonth) {
						calender += "<div class='ui-datepicker-group";
						if (numMonths[1] > 1) {
							switch (col) {
								case 0: calender += " ui-datepicker-group-first";
									cornerClass = " ui-corner-" + (isRTL ? "right" : "left"); break;
								case numMonths[1]-1: calender += " ui-datepicker-group-last";
									cornerClass = " ui-corner-" + (isRTL ? "left" : "right"); break;
								default: calender += " ui-datepicker-group-middle"; cornerClass = ""; break;
							}
						}
						calender += "'>";
					}
					calender += "<div class='ui-datepicker-header ui-widget-header ui-helper-clearfix" + cornerClass + "'>" +
						(/all|left/.test(cornerClass) && row === 0 ? (isRTL ? next : prev) : "") +
						(/all|right/.test(cornerClass) && row === 0 ? (isRTL ? prev : next) : "") +
						this._generateMonthYearHeader(inst, drawMonth, drawYear, minDate, maxDate,
						row > 0 || col > 0, monthNames, monthNamesShort) + // draw month headers
						"</div><table class='ui-datepicker-calendar'><thead>" +
						"<tr>";
					thead = (showWeek ? "<th class='ui-datepicker-week-col'>" + this._get(inst, "weekHeader") + "</th>" : "");
					for (dow = 0; dow < 7; dow++) { // days of the week
						day = (dow + firstDay) % 7;
						thead += "<th" + ((dow + firstDay + 6) % 7 >= 5 ? " class='ui-datepicker-week-end'" : "") + ">" +
							"<span title='" + dayNames[day] + "'>" + dayNamesMin[day] + "</span></th>";
					}
					calender += thead + "</tr></thead><tbody>";
					daysInMonth = this._getDaysInMonth(drawYear, drawMonth);
					if (drawYear === inst.selectedYear && drawMonth === inst.selectedMonth) {
						inst.selectedDay = Math.min(inst.selectedDay, daysInMonth);
					}
					leadDays = (this._getFirstDayOfMonth(drawYear, drawMonth) - firstDay + 7) % 7;
					curRows = Math.ceil((leadDays + daysInMonth) / 7); // calculate the number of rows to generate
					numRows = (isMultiMonth ? this.maxRows > curRows ? this.maxRows : curRows : curRows); //If multiple months, use the higher number of rows (see #7043)
					this.maxRows = numRows;
					printDate = this._daylightSavingAdjust(new Date(drawYear, drawMonth, 1 - leadDays));
					for (dRow = 0; dRow < numRows; dRow++) { // create date picker rows
						calender += "<tr>";
						tbody = (!showWeek ? "" : "<td class='ui-datepicker-week-col'>" +
							this._get(inst, "calculateWeek")(printDate) + "</td>");
						for (dow = 0; dow < 7; dow++) { // create date picker days
							daySettings = (beforeShowDay ?
								beforeShowDay.apply((inst.input ? inst.input[0] : null), [printDate]) : [true, ""]);
							otherMonth = (printDate.getMonth() !== drawMonth);
							unselectable = (otherMonth && !selectOtherMonths) || !daySettings[0] ||
								(minDate && printDate < minDate) || (maxDate && printDate > maxDate);
							tbody += "<td class='" +
								((dow + firstDay + 6) % 7 >= 5 ? " ui-datepicker-week-end" : "") + // highlight weekends
								(otherMonth ? " ui-datepicker-other-month" : "") + // highlight days from other months
								((printDate.getTime() === selectedDate.getTime() && drawMonth === inst.selectedMonth && inst._keyEvent) || // user pressed key
								(defaultDate.getTime() === printDate.getTime() && defaultDate.getTime() === selectedDate.getTime()) ?
								// or defaultDate is current printedDate and defaultDate is selectedDate
								" " + this._dayOverClass : "") + // highlight selected day
								(unselectable ? " " + this._unselectableClass + " ui-state-disabled": "") +  // highlight unselectable days
								(otherMonth && !showOtherMonths ? "" : " " + daySettings[1] + // highlight custom dates
								(printDate.getTime() === currentDate.getTime() ? " " + this._currentClass : "") + // highlight selected day
								(printDate.getTime() === today.getTime() ? " ui-datepicker-today" : "")) + "'" + // highlight today (if different)
								((!otherMonth || showOtherMonths) && daySettings[2] ? " title='" + daySettings[2].replace(/'/g, "&#39;") + "'" : "") + // cell title
								(unselectable ? "" : " data-handler='selectDay' data-event='click' data-month='" + printDate.getMonth() + "' data-year='" + printDate.getFullYear() + "'") + ">" + // actions
								(otherMonth && !showOtherMonths ? "&#xa0;" : // display for other months
								(unselectable ? "<span class='ui-state-default'>" + printDate.getDate() + "</span>" : "<a class='ui-state-default" +
								(printDate.getTime() === today.getTime() ? " ui-state-highlight" : "") +
								(printDate.getTime() === currentDate.getTime() ? " ui-state-active" : "") + // highlight selected day
								(otherMonth ? " ui-priority-secondary" : "") + // distinguish dates from other months
								"' href='#'>" + printDate.getDate() + "</a>")) + "</td>"; // display selectable date
							printDate.setDate(printDate.getDate() + 1);
							printDate = this._daylightSavingAdjust(printDate);
						}
						calender += tbody + "</tr>";
					}
					drawMonth++;
					if (drawMonth > 11) {
						drawMonth = 0;
						drawYear++;
					}
					calender += "</tbody></table>" + (isMultiMonth ? "</div>" +
								((numMonths[0] > 0 && col === numMonths[1]-1) ? "<div class='ui-datepicker-row-break'></div>" : "") : "");
					group += calender;
				}
				html += group;
			}
			html += buttonPanel;
			inst._keyEvent = false;
			return html;
		},

		/* Generate the month and year header. */
		_generateMonthYearHeader: function(inst, drawMonth, drawYear, minDate, maxDate,
				secondary, monthNames, monthNamesShort) {

			var inMinYear, inMaxYear, month, years, thisYear, determineYear, year, endYear,
				changeMonth = this._get(inst, "changeMonth"),
				changeYear = this._get(inst, "changeYear"),
				showMonthAfterYear = this._get(inst, "showMonthAfterYear"),
				html = "<div class='ui-datepicker-title'>",
				monthHtml = "";

			// month selection
			if (secondary || !changeMonth) {
				monthHtml += "<span class='ui-datepicker-month'>" + monthNames[drawMonth] + "</span>";
			} else {
				inMinYear = (minDate && minDate.getFullYear() === drawYear);
				inMaxYear = (maxDate && maxDate.getFullYear() === drawYear);
				monthHtml += "<select class='ui-datepicker-month' data-handler='selectMonth' data-event='change'>";
				for ( month = 0; month < 12; month++) {
					if ((!inMinYear || month >= minDate.getMonth()) && (!inMaxYear || month <= maxDate.getMonth())) {
						monthHtml += "<option value='" + month + "'" +
							(month === drawMonth ? " selected='selected'" : "") +
							">" + monthNamesShort[month] + "</option>";
					}
				}
				monthHtml += "</select>";
			}

			if (!showMonthAfterYear) {
				html += monthHtml + (secondary || !(changeMonth && changeYear) ? "&#xa0;" : "");
			}

			// year selection
			if ( !inst.yearshtml ) {
				inst.yearshtml = "";
				if (secondary || !changeYear) {
					html += "<span class='ui-datepicker-year'>" + drawYear + "</span>";
				} else {
					// determine range of years to display
					years = this._get(inst, "yearRange").split(":");
					thisYear = new Date().getFullYear();
					determineYear = function(value) {
						var year = (value.match(/c[+\-].*/) ? drawYear + parseInt(value.substring(1), 10) :
							(value.match(/[+\-].*/) ? thisYear + parseInt(value, 10) :
							parseInt(value, 10)));
						return (isNaN(year) ? thisYear : year);
					};
					year = determineYear(years[0]);
					endYear = Math.max(year, determineYear(years[1] || ""));
					year = (minDate ? Math.max(year, minDate.getFullYear()) : year);
					endYear = (maxDate ? Math.min(endYear, maxDate.getFullYear()) : endYear);
					inst.yearshtml += "<select class='ui-datepicker-year' data-handler='selectYear' data-event='change'>";
					for (; year <= endYear; year++) {
						inst.yearshtml += "<option value='" + year + "'" +
							(year === drawYear ? " selected='selected'" : "") +
							">" + year + "</option>";
					}
					inst.yearshtml += "</select>";

					html += inst.yearshtml;
					inst.yearshtml = null;
				}
			}

			html += this._get(inst, "yearSuffix");
			if (showMonthAfterYear) {
				html += (secondary || !(changeMonth && changeYear) ? "&#xa0;" : "") + monthHtml;
			}
			html += "</div>"; // Close datepicker_header
			return html;
		},

		/* Adjust one of the date sub-fields. */
		_adjustInstDate: function(inst, offset, period) {
			var year = inst.drawYear + (period === "Y" ? offset : 0),
				month = inst.drawMonth + (period === "M" ? offset : 0),
				day = Math.min(inst.selectedDay, this._getDaysInMonth(year, month)) + (period === "D" ? offset : 0),
				date = this._restrictMinMax(inst, this._daylightSavingAdjust(new Date(year, month, day)));

			inst.selectedDay = date.getDate();
			inst.drawMonth = inst.selectedMonth = date.getMonth();
			inst.drawYear = inst.selectedYear = date.getFullYear();
			if (period === "M" || period === "Y") {
				this._notifyChange(inst);
			}
		},

		/* Ensure a date is within any min/max bounds. */
		_restrictMinMax: function(inst, date) {
			var minDate = this._getMinMaxDate(inst, "min"),
				maxDate = this._getMinMaxDate(inst, "max"),
				newDate = (minDate && date < minDate ? minDate : date);
			return (maxDate && newDate > maxDate ? maxDate : newDate);
		},

		/* Notify change of month/year. */
		_notifyChange: function(inst) {
			var onChange = this._get(inst, "onChangeMonthYear");
			if (onChange) {
				onChange.apply((inst.input ? inst.input[0] : null),
					[inst.selectedYear, inst.selectedMonth + 1, inst]);
			}
		},

		/* Determine the number of months to show. */
		_getNumberOfMonths: function(inst) {
			var numMonths = this._get(inst, "numberOfMonths");
			return (numMonths == null ? [1, 1] : (typeof numMonths === "number" ? [1, numMonths] : numMonths));
		},

		/* Determine the current maximum date - ensure no time components are set. */
		_getMinMaxDate: function(inst, minMax) {
			return this._determineDate(inst, this._get(inst, minMax + "Date"), null);
		},

		/* Find the number of days in a given month. */
		_getDaysInMonth: function(year, month) {
			return 32 - this._daylightSavingAdjust(new Date(year, month, 32)).getDate();
		},

		/* Find the day of the week of the first of a month. */
		_getFirstDayOfMonth: function(year, month) {
			return new Date(year, month, 1).getDay();
		},

		/* Determines if we should allow a "next/prev" month display change. */
		_canAdjustMonth: function(inst, offset, curYear, curMonth) {
			var numMonths = this._getNumberOfMonths(inst),
				date = this._daylightSavingAdjust(new Date(curYear,
				curMonth + (offset < 0 ? offset : numMonths[0] * numMonths[1]), 1));

			if (offset < 0) {
				date.setDate(this._getDaysInMonth(date.getFullYear(), date.getMonth()));
			}
			return this._isInRange(inst, date);
		},

		/* Is the given date in the accepted range? */
		_isInRange: function(inst, date) {
			var yearSplit, currentYear,
				minDate = this._getMinMaxDate(inst, "min"),
				maxDate = this._getMinMaxDate(inst, "max"),
				minYear = null,
				maxYear = null,
				years = this._get(inst, "yearRange");
				if (years){
					yearSplit = years.split(":");
					currentYear = new Date().getFullYear();
					minYear = parseInt(yearSplit[0], 10);
					maxYear = parseInt(yearSplit[1], 10);
					if ( yearSplit[0].match(/[+\-].*/) ) {
						minYear += currentYear;
					}
					if ( yearSplit[1].match(/[+\-].*/) ) {
						maxYear += currentYear;
					}
				}

			return ((!minDate || date.getTime() >= minDate.getTime()) &&
				(!maxDate || date.getTime() <= maxDate.getTime()) &&
				(!minYear || date.getFullYear() >= minYear) &&
				(!maxYear || date.getFullYear() <= maxYear));
		},

		/* Provide the configuration settings for formatting/parsing. */
		_getFormatConfig: function(inst) {
			var shortYearCutoff = this._get(inst, "shortYearCutoff");
			shortYearCutoff = (typeof shortYearCutoff !== "string" ? shortYearCutoff :
				new Date().getFullYear() % 100 + parseInt(shortYearCutoff, 10));
			return {shortYearCutoff: shortYearCutoff,
				dayNamesShort: this._get(inst, "dayNamesShort"), dayNames: this._get(inst, "dayNames"),
				monthNamesShort: this._get(inst, "monthNamesShort"), monthNames: this._get(inst, "monthNames")};
		},

		/* Format the given date for display. */
		_formatDate: function(inst, day, month, year) {
			if (!day) {
				inst.currentDay = inst.selectedDay;
				inst.currentMonth = inst.selectedMonth;
				inst.currentYear = inst.selectedYear;
			}
			var date = (day ? (typeof day === "object" ? day :
				this._daylightSavingAdjust(new Date(year, month, day))) :
				this._daylightSavingAdjust(new Date(inst.currentYear, inst.currentMonth, inst.currentDay)));
			return this.formatDate(this._get(inst, "dateFormat"), date, this._getFormatConfig(inst));
		}
	});

	/*
	 * Bind hover events for datepicker elements.
	 * Done via delegate so the binding only occurs once in the lifetime of the parent div.
	 * Global instActive, set by _updateDatepicker allows the handlers to find their way back to the active picker.
	 */
	function bindHover(dpDiv) {
		var selector = "button, .ui-datepicker-prev, .ui-datepicker-next, .ui-datepicker-calendar td a";
		return dpDiv.delegate(selector, "mouseout", function() {
				$(this).removeClass("ui-state-hover");
				if (this.className.indexOf("ui-datepicker-prev") !== -1) {
					$(this).removeClass("ui-datepicker-prev-hover");
				}
				if (this.className.indexOf("ui-datepicker-next") !== -1) {
					$(this).removeClass("ui-datepicker-next-hover");
				}
			})
			.delegate(selector, "mouseover", function(){
				if (!$.datepicker._isDisabledDatepicker( instActive.inline ? dpDiv.parent()[0] : instActive.input[0])) {
					$(this).parents(".ui-datepicker-calendar").find("a").removeClass("ui-state-hover");
					$(this).addClass("ui-state-hover");
					if (this.className.indexOf("ui-datepicker-prev") !== -1) {
						$(this).addClass("ui-datepicker-prev-hover");
					}
					if (this.className.indexOf("ui-datepicker-next") !== -1) {
						$(this).addClass("ui-datepicker-next-hover");
					}
				}
			});
	}

	/* jQuery extend now ignores nulls! */
	function extendRemove(target, props) {
		$.extend(target, props);
		for (var name in props) {
			if (props[name] == null) {
				target[name] = props[name];
			}
		}
		return target;
	}

	/* Invoke the datepicker functionality.
	   @param  options  string - a command, optionally followed by additional parameters or
						Object - settings for attaching new datepicker functionality
	   @return  jQuery object */
	$.fn.datepicker = function(options){

		/* Verify an empty collection wasn't passed - Fixes #6976 */
		if ( !this.length ) {
			return this;
		}

		/* Initialise the date picker. */
		if (!$.datepicker.initialized) {
			$(document).mousedown($.datepicker._checkExternalClick);
			$.datepicker.initialized = true;
		}

		/* Append datepicker main container to body if not exist. */
		if ($("#"+$.datepicker._mainDivId).length === 0) {
			$("body").append($.datepicker.dpDiv);
		}

		var otherArgs = Array.prototype.slice.call(arguments, 1);
		if (typeof options === "string" && (options === "isDisabled" || options === "getDate" || options === "widget")) {
			return $.datepicker["_" + options + "Datepicker"].
				apply($.datepicker, [this[0]].concat(otherArgs));
		}
		if (options === "option" && arguments.length === 2 && typeof arguments[1] === "string") {
			return $.datepicker["_" + options + "Datepicker"].
				apply($.datepicker, [this[0]].concat(otherArgs));
		}
		return this.each(function() {
			typeof options === "string" ?
				$.datepicker["_" + options + "Datepicker"].
					apply($.datepicker, [this].concat(otherArgs)) :
				$.datepicker._attachDatepicker(this, options);
		});
	};

	$.datepicker = new Datepicker(); // singleton instance
	$.datepicker.initialized = false;
	$.datepicker.uuid = new Date().getTime();
	$.datepicker.version = "1.10.3";

	})(jQuery);

	(function( $, undefined ) {

	var sizeRelatedOptions = {
			buttons: true,
			height: true,
			maxHeight: true,
			maxWidth: true,
			minHeight: true,
			minWidth: true,
			width: true
		},
		resizableRelatedOptions = {
			maxHeight: true,
			maxWidth: true,
			minHeight: true,
			minWidth: true
		};

	$.widget( "ui.dialog", {
		version: "1.10.3",
		options: {
			appendTo: "body",
			autoOpen: true,
			buttons: [],
			closeOnEscape: true,
			closeText: "close",
			dialogClass: "",
			draggable: true,
			hide: null,
			height: "auto",
			maxHeight: null,
			maxWidth: null,
			minHeight: 150,
			minWidth: 150,
			modal: false,
			position: {
				my: "center",
				at: "center",
				of: window,
				collision: "fit",
				// Ensure the titlebar is always visible
				using: function( pos ) {
					var topOffset = $( this ).css( pos ).offset().top;
					if ( topOffset < 0 ) {
						$( this ).css( "top", pos.top - topOffset );
					}
				}
			},
			resizable: true,
			show: null,
			title: null,
			width: 300,

			// callbacks
			beforeClose: null,
			close: null,
			drag: null,
			dragStart: null,
			dragStop: null,
			focus: null,
			open: null,
			resize: null,
			resizeStart: null,
			resizeStop: null
		},

		_create: function() {
			this.originalCss = {
				display: this.element[0].style.display,
				width: this.element[0].style.width,
				minHeight: this.element[0].style.minHeight,
				maxHeight: this.element[0].style.maxHeight,
				height: this.element[0].style.height
			};
			this.originalPosition = {
				parent: this.element.parent(),
				index: this.element.parent().children().index( this.element )
			};
			this.originalTitle = this.element.attr("title");
			this.options.title = this.options.title || this.originalTitle;

			this._createWrapper();

			this.element
				.show()
				.removeAttr("title")
				.addClass("ui-dialog-content ui-widget-content")
				.appendTo( this.uiDialog );

			this._createTitlebar();
			this._createButtonPane();

			if ( this.options.draggable && $.fn.draggable ) {
				this._makeDraggable();
			}
			if ( this.options.resizable && $.fn.resizable ) {
				this._makeResizable();
			}

			this._isOpen = false;
		},

		_init: function() {
			if ( this.options.autoOpen ) {
				this.open();
			}
		},

		_appendTo: function() {
			var element = this.options.appendTo;
			if ( element && (element.jquery || element.nodeType) ) {
				return $( element );
			}
			return this.document.find( element || "body" ).eq( 0 );
		},

		_destroy: function() {
			var next,
				originalPosition = this.originalPosition;

			this._destroyOverlay();

			this.element
				.removeUniqueId()
				.removeClass("ui-dialog-content ui-widget-content")
				.css( this.originalCss )
				// Without detaching first, the following becomes really slow
				.detach();

			this.uiDialog.stop( true, true ).remove();

			if ( this.originalTitle ) {
				this.element.attr( "title", this.originalTitle );
			}

			next = originalPosition.parent.children().eq( originalPosition.index );
			// Don't try to place the dialog next to itself (#8613)
			if ( next.length && next[0] !== this.element[0] ) {
				next.before( this.element );
			} else {
				originalPosition.parent.append( this.element );
			}
		},

		widget: function() {
			return this.uiDialog;
		},

		disable: $.noop,
		enable: $.noop,

		close: function( event ) {
			var that = this;

			if ( !this._isOpen || this._trigger( "beforeClose", event ) === false ) {
				return;
			}

			this._isOpen = false;
			this._destroyOverlay();

			if ( !this.opener.filter(":focusable").focus().length ) {
				// Hiding a focused element doesn't trigger blur in WebKit
				// so in case we have nothing to focus on, explicitly blur the active element
				// https://bugs.webkit.org/show_bug.cgi?id=47182
				$( this.document[0].activeElement ).blur();
			}

			this._hide( this.uiDialog, this.options.hide, function() {
				that._trigger( "close", event );
			});
		},

		isOpen: function() {
			return this._isOpen;
		},

		moveToTop: function() {
			this._moveToTop();
		},

		_moveToTop: function( event, silent ) {
			var moved = !!this.uiDialog.nextAll(":visible").insertBefore( this.uiDialog ).length;
			if ( moved && !silent ) {
				this._trigger( "focus", event );
			}
			return moved;
		},

		open: function() {
			var that = this;
			if ( this._isOpen ) {
				if ( this._moveToTop() ) {
					this._focusTabbable();
				}
				return;
			}

			this._isOpen = true;
			this.opener = $( this.document[0].activeElement );

			this._size();
			this._position();
			this._createOverlay();
			this._moveToTop( null, true );
			this._show( this.uiDialog, this.options.show, function() {
				that._focusTabbable();
				that._trigger("focus");
			});

			this._trigger("open");
		},

		_focusTabbable: function() {
			// Set focus to the first match:
			// 1. First element inside the dialog matching [autofocus]
			// 2. Tabbable element inside the content element
			// 3. Tabbable element inside the buttonpane
			// 4. The close button
			// 5. The dialog itself
			var hasFocus = this.element.find("[autofocus]");
			if ( !hasFocus.length ) {
				hasFocus = this.element.find(":tabbable");
			}
			if ( !hasFocus.length ) {
				hasFocus = this.uiDialogButtonPane.find(":tabbable");
			}
			if ( !hasFocus.length ) {
				hasFocus = this.uiDialogTitlebarClose.filter(":tabbable");
			}
			if ( !hasFocus.length ) {
				hasFocus = this.uiDialog;
			}
			hasFocus.eq( 0 ).focus();
		},

		_keepFocus: function( event ) {
			function checkFocus() {
				var activeElement = this.document[0].activeElement,
					isActive = this.uiDialog[0] === activeElement ||
						$.contains( this.uiDialog[0], activeElement );
				if ( !isActive ) {
					this._focusTabbable();
				}
			}
			event.preventDefault();
			checkFocus.call( this );
			// support: IE
			// IE <= 8 doesn't prevent moving focus even with event.preventDefault()
			// so we check again later
			this._delay( checkFocus );
		},

		_createWrapper: function() {
			this.uiDialog = $("<div>")
				.addClass( "ui-dialog ui-widget ui-widget-content ui-corner-all ui-front " +
					this.options.dialogClass )
				.hide()
				.attr({
					// Setting tabIndex makes the div focusable
					tabIndex: -1,
					role: "dialog"
				})
				.appendTo( this._appendTo() );

			this._on( this.uiDialog, {
				keydown: function( event ) {
					if ( this.options.closeOnEscape && !event.isDefaultPrevented() && event.keyCode &&
							event.keyCode === $.ui.keyCode.ESCAPE ) {
						event.preventDefault();
						this.close( event );
						return;
					}

					// prevent tabbing out of dialogs
					if ( event.keyCode !== $.ui.keyCode.TAB ) {
						return;
					}
					var tabbables = this.uiDialog.find(":tabbable"),
						first = tabbables.filter(":first"),
						last  = tabbables.filter(":last");

					if ( ( event.target === last[0] || event.target === this.uiDialog[0] ) && !event.shiftKey ) {
						first.focus( 1 );
						event.preventDefault();
					} else if ( ( event.target === first[0] || event.target === this.uiDialog[0] ) && event.shiftKey ) {
						last.focus( 1 );
						event.preventDefault();
					}
				},
				mousedown: function( event ) {
					if ( this._moveToTop( event ) ) {
						this._focusTabbable();
					}
				}
			});

			// We assume that any existing aria-describedby attribute means
			// that the dialog content is marked up properly
			// otherwise we brute force the content as the description
			if ( !this.element.find("[aria-describedby]").length ) {
				this.uiDialog.attr({
					"aria-describedby": this.element.uniqueId().attr("id")
				});
			}
		},

		_createTitlebar: function() {
			var uiDialogTitle;

			this.uiDialogTitlebar = $("<div>")
				.addClass("ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix")
				.prependTo( this.uiDialog );
			this._on( this.uiDialogTitlebar, {
				mousedown: function( event ) {
					// Don't prevent click on close button (#8838)
					// Focusing a dialog that is partially scrolled out of view
					// causes the browser to scroll it into view, preventing the click event
					if ( !$( event.target ).closest(".ui-dialog-titlebar-close") ) {
						// Dialog isn't getting focus when dragging (#8063)
						this.uiDialog.focus();
					}
				}
			});

			this.uiDialogTitlebarClose = $("<button></button>")
				.button({
					label: this.options.closeText,
					icons: {
						primary: "ui-icon-closethick"
					},
					text: false
				})
				.addClass("ui-dialog-titlebar-close")
				.appendTo( this.uiDialogTitlebar );
			this._on( this.uiDialogTitlebarClose, {
				click: function( event ) {
					event.preventDefault();
					this.close( event );
				}
			});

			uiDialogTitle = $("<span>")
				.uniqueId()
				.addClass("ui-dialog-title")
				.prependTo( this.uiDialogTitlebar );
			this._title( uiDialogTitle );

			this.uiDialog.attr({
				"aria-labelledby": uiDialogTitle.attr("id")
			});
		},

		_title: function( title ) {
			if ( !this.options.title ) {
				title.html("&#160;");
			}
			title.text( this.options.title );
		},

		_createButtonPane: function() {
			this.uiDialogButtonPane = $("<div>")
				.addClass("ui-dialog-buttonpane ui-widget-content ui-helper-clearfix");

			this.uiButtonSet = $("<div>")
				.addClass("ui-dialog-buttonset")
				.appendTo( this.uiDialogButtonPane );

			this._createButtons();
		},

		_createButtons: function() {
			var that = this,
				buttons = this.options.buttons;

			// if we already have a button pane, remove it
			this.uiDialogButtonPane.remove();
			this.uiButtonSet.empty();

			if ( $.isEmptyObject( buttons ) || ($.isArray( buttons ) && !buttons.length) ) {
				this.uiDialog.removeClass("ui-dialog-buttons");
				return;
			}

			$.each( buttons, function( name, props ) {
				var click, buttonOptions;
				props = $.isFunction( props ) ?
					{ click: props, text: name } :
					props;
				// Default to a non-submitting button
				props = $.extend( { type: "button" }, props );
				// Change the context for the click callback to be the main element
				click = props.click;
				props.click = function() {
					click.apply( that.element[0], arguments );
				};
				buttonOptions = {
					icons: props.icons,
					text: props.showText
				};
				delete props.icons;
				delete props.showText;
				$( "<button></button>", props )
					.button( buttonOptions )
					.appendTo( that.uiButtonSet );
			});
			this.uiDialog.addClass("ui-dialog-buttons");
			this.uiDialogButtonPane.appendTo( this.uiDialog );
		},

		_makeDraggable: function() {
			var that = this,
				options = this.options;

			function filteredUi( ui ) {
				return {
					position: ui.position,
					offset: ui.offset
				};
			}

			this.uiDialog.draggable({
				cancel: ".ui-dialog-content, .ui-dialog-titlebar-close",
				handle: ".ui-dialog-titlebar",
				containment: "document",
				start: function( event, ui ) {
					$( this ).addClass("ui-dialog-dragging");
					that._blockFrames();
					that._trigger( "dragStart", event, filteredUi( ui ) );
				},
				drag: function( event, ui ) {
					that._trigger( "drag", event, filteredUi( ui ) );
				},
				stop: function( event, ui ) {
					options.position = [
						ui.position.left - that.document.scrollLeft(),
						ui.position.top - that.document.scrollTop()
					];
					$( this ).removeClass("ui-dialog-dragging");
					that._unblockFrames();
					that._trigger( "dragStop", event, filteredUi( ui ) );
				}
			});
		},

		_makeResizable: function() {
			var that = this,
				options = this.options,
				handles = options.resizable,
				// .ui-resizable has position: relative defined in the stylesheet
				// but dialogs have to use absolute or fixed positioning
				position = this.uiDialog.css("position"),
				resizeHandles = typeof handles === "string" ?
					handles	:
					"n,e,s,w,se,sw,ne,nw";

			function filteredUi( ui ) {
				return {
					originalPosition: ui.originalPosition,
					originalSize: ui.originalSize,
					position: ui.position,
					size: ui.size
				};
			}

			this.uiDialog.resizable({
				cancel: ".ui-dialog-content",
				containment: "document",
				alsoResize: this.element,
				maxWidth: options.maxWidth,
				maxHeight: options.maxHeight,
				minWidth: options.minWidth,
				minHeight: this._minHeight(),
				handles: resizeHandles,
				start: function( event, ui ) {
					$( this ).addClass("ui-dialog-resizing");
					that._blockFrames();
					that._trigger( "resizeStart", event, filteredUi( ui ) );
				},
				resize: function( event, ui ) {
					that._trigger( "resize", event, filteredUi( ui ) );
				},
				stop: function( event, ui ) {
					options.height = $( this ).height();
					options.width = $( this ).width();
					$( this ).removeClass("ui-dialog-resizing");
					that._unblockFrames();
					that._trigger( "resizeStop", event, filteredUi( ui ) );
				}
			})
			.css( "position", position );
		},

		_minHeight: function() {
			var options = this.options;

			return options.height === "auto" ?
				options.minHeight :
				Math.min( options.minHeight, options.height );
		},

		_position: function() {
			// Need to show the dialog to get the actual offset in the position plugin
			var isVisible = this.uiDialog.is(":visible");
			if ( !isVisible ) {
				this.uiDialog.show();
			}
			this.uiDialog.position( this.options.position );
			if ( !isVisible ) {
				this.uiDialog.hide();
			}
		},

		_setOptions: function( options ) {
			var that = this,
				resize = false,
				resizableOptions = {};

			$.each( options, function( key, value ) {
				that._setOption( key, value );

				if ( key in sizeRelatedOptions ) {
					resize = true;
				}
				if ( key in resizableRelatedOptions ) {
					resizableOptions[ key ] = value;
				}
			});

			if ( resize ) {
				this._size();
				this._position();
			}
			if ( this.uiDialog.is(":data(ui-resizable)") ) {
				this.uiDialog.resizable( "option", resizableOptions );
			}
		},

		_setOption: function( key, value ) {
			/*jshint maxcomplexity:15*/
			var isDraggable, isResizable,
				uiDialog = this.uiDialog;

			if ( key === "dialogClass" ) {
				uiDialog
					.removeClass( this.options.dialogClass )
					.addClass( value );
			}

			if ( key === "disabled" ) {
				return;
			}

			this._super( key, value );

			if ( key === "appendTo" ) {
				this.uiDialog.appendTo( this._appendTo() );
			}

			if ( key === "buttons" ) {
				this._createButtons();
			}

			if ( key === "closeText" ) {
				this.uiDialogTitlebarClose.button({
					// Ensure that we always pass a string
					label: "" + value
				});
			}

			if ( key === "draggable" ) {
				isDraggable = uiDialog.is(":data(ui-draggable)");
				if ( isDraggable && !value ) {
					uiDialog.draggable("destroy");
				}

				if ( !isDraggable && value ) {
					this._makeDraggable();
				}
			}

			if ( key === "position" ) {
				this._position();
			}

			if ( key === "resizable" ) {
				// currently resizable, becoming non-resizable
				isResizable = uiDialog.is(":data(ui-resizable)");
				if ( isResizable && !value ) {
					uiDialog.resizable("destroy");
				}

				// currently resizable, changing handles
				if ( isResizable && typeof value === "string" ) {
					uiDialog.resizable( "option", "handles", value );
				}

				// currently non-resizable, becoming resizable
				if ( !isResizable && value !== false ) {
					this._makeResizable();
				}
			}

			if ( key === "title" ) {
				this._title( this.uiDialogTitlebar.find(".ui-dialog-title") );
			}
		},

		_size: function() {
			// If the user has resized the dialog, the .ui-dialog and .ui-dialog-content
			// divs will both have width and height set, so we need to reset them
			var nonContentHeight, minContentHeight, maxContentHeight,
				options = this.options;

			// Reset content sizing
			this.element.show().css({
				width: "auto",
				minHeight: 0,
				maxHeight: "none",
				height: 0
			});

			if ( options.minWidth > options.width ) {
				options.width = options.minWidth;
			}

			// reset wrapper sizing
			// determine the height of all the non-content elements
			nonContentHeight = this.uiDialog.css({
					height: "auto",
					width: options.width
				})
				.outerHeight();
			minContentHeight = Math.max( 0, options.minHeight - nonContentHeight );
			maxContentHeight = typeof options.maxHeight === "number" ?
				Math.max( 0, options.maxHeight - nonContentHeight ) :
				"none";

			if ( options.height === "auto" ) {
				this.element.css({
					minHeight: minContentHeight,
					maxHeight: maxContentHeight,
					height: "auto"
				});
			} else {
				this.element.height( Math.max( 0, options.height - nonContentHeight ) );
			}

			if (this.uiDialog.is(":data(ui-resizable)") ) {
				this.uiDialog.resizable( "option", "minHeight", this._minHeight() );
			}
		},

		_blockFrames: function() {
			this.iframeBlocks = this.document.find( "iframe" ).map(function() {
				var iframe = $( this );

				return $( "<div>" )
					.css({
						position: "absolute",
						width: iframe.outerWidth(),
						height: iframe.outerHeight()
					})
					.appendTo( iframe.parent() )
					.offset( iframe.offset() )[0];
			});
		},

		_unblockFrames: function() {
			if ( this.iframeBlocks ) {
				this.iframeBlocks.remove();
				delete this.iframeBlocks;
			}
		},

		_allowInteraction: function( event ) {
			if ( $( event.target ).closest(".ui-dialog").length ) {
				return true;
			}

			// TODO: Remove hack when datepicker implements
			// the .ui-front logic (#8989)
			return !!$( event.target ).closest(".ui-datepicker").length;
		},

		_createOverlay: function() {
			if ( !this.options.modal ) {
				return;
			}

			var that = this,
				widgetFullName = this.widgetFullName;
			if ( !$.ui.dialog.overlayInstances ) {
				// Prevent use of anchors and inputs.
				// We use a delay in case the overlay is created from an
				// event that we're going to be cancelling. (#2804)
				this._delay(function() {
					// Handle .dialog().dialog("close") (#4065)
					if ( $.ui.dialog.overlayInstances ) {
						this.document.bind( "focusin.dialog", function( event ) {
							if ( !that._allowInteraction( event ) ) {
								event.preventDefault();
								$(".ui-dialog:visible:last .ui-dialog-content")
									.data( widgetFullName )._focusTabbable();
							}
						});
					}
				});
			}

			this.overlay = $("<div>")
				.addClass("ui-widget-overlay ui-front")
				.appendTo( this._appendTo() );
			this._on( this.overlay, {
				mousedown: "_keepFocus"
			});
			$.ui.dialog.overlayInstances++;
		},

		_destroyOverlay: function() {
			if ( !this.options.modal ) {
				return;
			}

			if ( this.overlay ) {
				$.ui.dialog.overlayInstances--;

				if ( !$.ui.dialog.overlayInstances ) {
					this.document.unbind( "focusin.dialog" );
				}
				this.overlay.remove();
				this.overlay = null;
			}
		}
	});

	$.ui.dialog.overlayInstances = 0;

	// DEPRECATED
	if ( $.uiBackCompat !== false ) {
		// position option with array notation
		// just override with old implementation
		$.widget( "ui.dialog", $.ui.dialog, {
			_position: function() {
				var position = this.options.position,
					myAt = [],
					offset = [ 0, 0 ],
					isVisible;

				if ( position ) {
					if ( typeof position === "string" || (typeof position === "object" && "0" in position ) ) {
						myAt = position.split ? position.split(" ") : [ position[0], position[1] ];
						if ( myAt.length === 1 ) {
							myAt[1] = myAt[0];
						}

						$.each( [ "left", "top" ], function( i, offsetPosition ) {
							if ( +myAt[ i ] === myAt[ i ] ) {
								offset[ i ] = myAt[ i ];
								myAt[ i ] = offsetPosition;
							}
						});

						position = {
							my: myAt[0] + (offset[0] < 0 ? offset[0] : "+" + offset[0]) + " " +
								myAt[1] + (offset[1] < 0 ? offset[1] : "+" + offset[1]),
							at: myAt.join(" ")
						};
					}

					position = $.extend( {}, $.ui.dialog.prototype.options.position, position );
				} else {
					position = $.ui.dialog.prototype.options.position;
				}

				// need to show the dialog to get the actual offset in the position plugin
				isVisible = this.uiDialog.is(":visible");
				if ( !isVisible ) {
					this.uiDialog.show();
				}
				this.uiDialog.position( position );
				if ( !isVisible ) {
					this.uiDialog.hide();
				}
			}
		});
	}

	}( jQuery ) );

	(function( $, undefined ) {

	var rvertical = /up|down|vertical/,
		rpositivemotion = /up|left|vertical|horizontal/;

	$.effects.effect.blind = function( o, done ) {
		// Create element
		var el = $( this ),
			props = [ "position", "top", "bottom", "left", "right", "height", "width" ],
			mode = $.effects.setMode( el, o.mode || "hide" ),
			direction = o.direction || "up",
			vertical = rvertical.test( direction ),
			ref = vertical ? "height" : "width",
			ref2 = vertical ? "top" : "left",
			motion = rpositivemotion.test( direction ),
			animation = {},
			show = mode === "show",
			wrapper, distance, margin;

		// if already wrapped, the wrapper's properties are my property. #6245
		if ( el.parent().is( ".ui-effects-wrapper" ) ) {
			$.effects.save( el.parent(), props );
		} else {
			$.effects.save( el, props );
		}
		el.show();
		wrapper = $.effects.createWrapper( el ).css({
			overflow: "hidden"
		});

		distance = wrapper[ ref ]();
		margin = parseFloat( wrapper.css( ref2 ) ) || 0;

		animation[ ref ] = show ? distance : 0;
		if ( !motion ) {
			el
				.css( vertical ? "bottom" : "right", 0 )
				.css( vertical ? "top" : "left", "auto" )
				.css({ position: "absolute" });

			animation[ ref2 ] = show ? margin : distance + margin;
		}

		// start at 0 if we are showing
		if ( show ) {
			wrapper.css( ref, 0 );
			if ( ! motion ) {
				wrapper.css( ref2, margin + distance );
			}
		}

		// Animate
		wrapper.animate( animation, {
			duration: o.duration,
			easing: o.easing,
			queue: false,
			complete: function() {
				if ( mode === "hide" ) {
					el.hide();
				}
				$.effects.restore( el, props );
				$.effects.removeWrapper( el );
				done();
			}
		});

	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.bounce = function( o, done ) {
		var el = $( this ),
			props = [ "position", "top", "bottom", "left", "right", "height", "width" ],

			// defaults:
			mode = $.effects.setMode( el, o.mode || "effect" ),
			hide = mode === "hide",
			show = mode === "show",
			direction = o.direction || "up",
			distance = o.distance,
			times = o.times || 5,

			// number of internal animations
			anims = times * 2 + ( show || hide ? 1 : 0 ),
			speed = o.duration / anims,
			easing = o.easing,

			// utility:
			ref = ( direction === "up" || direction === "down" ) ? "top" : "left",
			motion = ( direction === "up" || direction === "left" ),
			i,
			upAnim,
			downAnim,

			// we will need to re-assemble the queue to stack our animations in place
			queue = el.queue(),
			queuelen = queue.length;

		// Avoid touching opacity to prevent clearType and PNG issues in IE
		if ( show || hide ) {
			props.push( "opacity" );
		}

		$.effects.save( el, props );
		el.show();
		$.effects.createWrapper( el ); // Create Wrapper

		// default distance for the BIGGEST bounce is the outer Distance / 3
		if ( !distance ) {
			distance = el[ ref === "top" ? "outerHeight" : "outerWidth" ]() / 3;
		}

		if ( show ) {
			downAnim = { opacity: 1 };
			downAnim[ ref ] = 0;

			// if we are showing, force opacity 0 and set the initial position
			// then do the "first" animation
			el.css( "opacity", 0 )
				.css( ref, motion ? -distance * 2 : distance * 2 )
				.animate( downAnim, speed, easing );
		}

		// start at the smallest distance if we are hiding
		if ( hide ) {
			distance = distance / Math.pow( 2, times - 1 );
		}

		downAnim = {};
		downAnim[ ref ] = 0;
		// Bounces up/down/left/right then back to 0 -- times * 2 animations happen here
		for ( i = 0; i < times; i++ ) {
			upAnim = {};
			upAnim[ ref ] = ( motion ? "-=" : "+=" ) + distance;

			el.animate( upAnim, speed, easing )
				.animate( downAnim, speed, easing );

			distance = hide ? distance * 2 : distance / 2;
		}

		// Last Bounce when Hiding
		if ( hide ) {
			upAnim = { opacity: 0 };
			upAnim[ ref ] = ( motion ? "-=" : "+=" ) + distance;

			el.animate( upAnim, speed, easing );
		}

		el.queue(function() {
			if ( hide ) {
				el.hide();
			}
			$.effects.restore( el, props );
			$.effects.removeWrapper( el );
			done();
		});

		// inject all the animations we just queued to be first in line (after "inprogress")
		if ( queuelen > 1) {
			queue.splice.apply( queue,
				[ 1, 0 ].concat( queue.splice( queuelen, anims + 1 ) ) );
		}
		el.dequeue();

	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.clip = function( o, done ) {
		// Create element
		var el = $( this ),
			props = [ "position", "top", "bottom", "left", "right", "height", "width" ],
			mode = $.effects.setMode( el, o.mode || "hide" ),
			show = mode === "show",
			direction = o.direction || "vertical",
			vert = direction === "vertical",
			size = vert ? "height" : "width",
			position = vert ? "top" : "left",
			animation = {},
			wrapper, animate, distance;

		// Save & Show
		$.effects.save( el, props );
		el.show();

		// Create Wrapper
		wrapper = $.effects.createWrapper( el ).css({
			overflow: "hidden"
		});
		animate = ( el[0].tagName === "IMG" ) ? wrapper : el;
		distance = animate[ size ]();

		// Shift
		if ( show ) {
			animate.css( size, 0 );
			animate.css( position, distance / 2 );
		}

		// Create Animation Object:
		animation[ size ] = show ? distance : 0;
		animation[ position ] = show ? 0 : distance / 2;

		// Animate
		animate.animate( animation, {
			queue: false,
			duration: o.duration,
			easing: o.easing,
			complete: function() {
				if ( !show ) {
					el.hide();
				}
				$.effects.restore( el, props );
				$.effects.removeWrapper( el );
				done();
			}
		});

	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.drop = function( o, done ) {

		var el = $( this ),
			props = [ "position", "top", "bottom", "left", "right", "opacity", "height", "width" ],
			mode = $.effects.setMode( el, o.mode || "hide" ),
			show = mode === "show",
			direction = o.direction || "left",
			ref = ( direction === "up" || direction === "down" ) ? "top" : "left",
			motion = ( direction === "up" || direction === "left" ) ? "pos" : "neg",
			animation = {
				opacity: show ? 1 : 0
			},
			distance;

		// Adjust
		$.effects.save( el, props );
		el.show();
		$.effects.createWrapper( el );

		distance = o.distance || el[ ref === "top" ? "outerHeight": "outerWidth" ]( true ) / 2;

		if ( show ) {
			el
				.css( "opacity", 0 )
				.css( ref, motion === "pos" ? -distance : distance );
		}

		// Animation
		animation[ ref ] = ( show ?
			( motion === "pos" ? "+=" : "-=" ) :
			( motion === "pos" ? "-=" : "+=" ) ) +
			distance;

		// Animate
		el.animate( animation, {
			queue: false,
			duration: o.duration,
			easing: o.easing,
			complete: function() {
				if ( mode === "hide" ) {
					el.hide();
				}
				$.effects.restore( el, props );
				$.effects.removeWrapper( el );
				done();
			}
		});
	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.explode = function( o, done ) {

		var rows = o.pieces ? Math.round( Math.sqrt( o.pieces ) ) : 3,
			cells = rows,
			el = $( this ),
			mode = $.effects.setMode( el, o.mode || "hide" ),
			show = mode === "show",

			// show and then visibility:hidden the element before calculating offset
			offset = el.show().css( "visibility", "hidden" ).offset(),

			// width and height of a piece
			width = Math.ceil( el.outerWidth() / cells ),
			height = Math.ceil( el.outerHeight() / rows ),
			pieces = [],

			// loop
			i, j, left, top, mx, my;

		// children animate complete:
		function childComplete() {
			pieces.push( this );
			if ( pieces.length === rows * cells ) {
				animComplete();
			}
		}

		// clone the element for each row and cell.
		for( i = 0; i < rows ; i++ ) { // ===>
			top = offset.top + i * height;
			my = i - ( rows - 1 ) / 2 ;

			for( j = 0; j < cells ; j++ ) { // |||
				left = offset.left + j * width;
				mx = j - ( cells - 1 ) / 2 ;

				// Create a clone of the now hidden main element that will be absolute positioned
				// within a wrapper div off the -left and -top equal to size of our pieces
				el
					.clone()
					.appendTo( "body" )
					.wrap( "<div></div>" )
					.css({
						position: "absolute",
						visibility: "visible",
						left: -j * width,
						top: -i * height
					})

				// select the wrapper - make it overflow: hidden and absolute positioned based on
				// where the original was located +left and +top equal to the size of pieces
					.parent()
					.addClass( "ui-effects-explode" )
					.css({
						position: "absolute",
						overflow: "hidden",
						width: width,
						height: height,
						left: left + ( show ? mx * width : 0 ),
						top: top + ( show ? my * height : 0 ),
						opacity: show ? 0 : 1
					}).animate({
						left: left + ( show ? 0 : mx * width ),
						top: top + ( show ? 0 : my * height ),
						opacity: show ? 1 : 0
					}, o.duration || 500, o.easing, childComplete );
			}
		}

		function animComplete() {
			el.css({
				visibility: "visible"
			});
			$( pieces ).remove();
			if ( !show ) {
				el.hide();
			}
			done();
		}
	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.fade = function( o, done ) {
		var el = $( this ),
			mode = $.effects.setMode( el, o.mode || "toggle" );

		el.animate({
			opacity: mode
		}, {
			queue: false,
			duration: o.duration,
			easing: o.easing,
			complete: done
		});
	};

	})( jQuery );

	(function( $, undefined ) {

	$.effects.effect.fold = function( o, done ) {

		// Create element
		var el = $( this ),
			props = [ "position", "top", "bottom", "left", "right", "height", "width" ],
			mode = $.effects.setMode( el, o.mode || "hide" ),
			show = mode === "show",
			hide = mode === "hide",
			size = o.size || 15,
			percent = /([0-9]+)%/.exec( size ),
			horizFirst = !!o.horizFirst,
			widthFirst = show !== horizFirst,
			ref = widthFirst ? [ "width", "height" ] : [ "height", "width" ],
			duration = o.duration / 2,
			wrapper, distance,
			animation1 = {},
			animation2 = {};

		$.effects.save( el, props );
		el.show();

		// Create Wrapper
		wrapper = $.effects.createWrapper( el ).css({
			overflow: "hidden"
		});
		distance = widthFirst ?
			[ wrapper.width(), wrapper.height() ] :
			[ wrapper.height(), wrapper.width() ];

		if ( percent ) {
			size = parseInt( percent[ 1 ], 10 ) / 100 * distance[ hide ? 0 : 1 ];
		}
		if ( show ) {
			wrapper.css( horizFirst ? {
				height: 0,
				width: size
			} : {
				height: size,
				width: 0
			});
		}

		// Animation
		animation1[ ref[ 0 ] ] = show ? distance[ 0 ] : size;
		animation2[ ref[ 1 ] ] = show ? distance[ 1 ] : 0;

		// Animate
		wrapper
			.animate( animation1, duration, o.easing )
			.animate( animation2, duration, o.easing, function() {
				if ( hide ) {
					el.hide();
				}
				$.effects.restore( el, props );
				$.effects.removeWrapper( el );
				done();
			});

	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.highlight = function( o, done ) {
		var elem = $( this ),
			props = [ "backgroundImage", "backgroundColor", "opacity" ],
			mode = $.effects.setMode( elem, o.mode || "show" ),
			animation = {
				backgroundColor: elem.css( "backgroundColor" )
			};

		if (mode === "hide") {
			animation.opacity = 0;
		}

		$.effects.save( elem, props );

		elem
			.show()
			.css({
				backgroundImage: "none",
				backgroundColor: o.color || "#ffff99"
			})
			.animate( animation, {
				queue: false,
				duration: o.duration,
				easing: o.easing,
				complete: function() {
					if ( mode === "hide" ) {
						elem.hide();
					}
					$.effects.restore( elem, props );
					done();
				}
			});
	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.pulsate = function( o, done ) {
		var elem = $( this ),
			mode = $.effects.setMode( elem, o.mode || "show" ),
			show = mode === "show",
			hide = mode === "hide",
			showhide = ( show || mode === "hide" ),

			// showing or hiding leaves of the "last" animation
			anims = ( ( o.times || 5 ) * 2 ) + ( showhide ? 1 : 0 ),
			duration = o.duration / anims,
			animateTo = 0,
			queue = elem.queue(),
			queuelen = queue.length,
			i;

		if ( show || !elem.is(":visible")) {
			elem.css( "opacity", 0 ).show();
			animateTo = 1;
		}

		// anims - 1 opacity "toggles"
		for ( i = 1; i < anims; i++ ) {
			elem.animate({
				opacity: animateTo
			}, duration, o.easing );
			animateTo = 1 - animateTo;
		}

		elem.animate({
			opacity: animateTo
		}, duration, o.easing);

		elem.queue(function() {
			if ( hide ) {
				elem.hide();
			}
			done();
		});

		// We just queued up "anims" animations, we need to put them next in the queue
		if ( queuelen > 1 ) {
			queue.splice.apply( queue,
				[ 1, 0 ].concat( queue.splice( queuelen, anims + 1 ) ) );
		}
		elem.dequeue();
	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.puff = function( o, done ) {
		var elem = $( this ),
			mode = $.effects.setMode( elem, o.mode || "hide" ),
			hide = mode === "hide",
			percent = parseInt( o.percent, 10 ) || 150,
			factor = percent / 100,
			original = {
				height: elem.height(),
				width: elem.width(),
				outerHeight: elem.outerHeight(),
				outerWidth: elem.outerWidth()
			};

		$.extend( o, {
			effect: "scale",
			queue: false,
			fade: true,
			mode: mode,
			complete: done,
			percent: hide ? percent : 100,
			from: hide ?
				original :
				{
					height: original.height * factor,
					width: original.width * factor,
					outerHeight: original.outerHeight * factor,
					outerWidth: original.outerWidth * factor
				}
		});

		elem.effect( o );
	};

	$.effects.effect.scale = function( o, done ) {

		// Create element
		var el = $( this ),
			options = $.extend( true, {}, o ),
			mode = $.effects.setMode( el, o.mode || "effect" ),
			percent = parseInt( o.percent, 10 ) ||
				( parseInt( o.percent, 10 ) === 0 ? 0 : ( mode === "hide" ? 0 : 100 ) ),
			direction = o.direction || "both",
			origin = o.origin,
			original = {
				height: el.height(),
				width: el.width(),
				outerHeight: el.outerHeight(),
				outerWidth: el.outerWidth()
			},
			factor = {
				y: direction !== "horizontal" ? (percent / 100) : 1,
				x: direction !== "vertical" ? (percent / 100) : 1
			};

		// We are going to pass this effect to the size effect:
		options.effect = "size";
		options.queue = false;
		options.complete = done;

		// Set default origin and restore for show/hide
		if ( mode !== "effect" ) {
			options.origin = origin || ["middle","center"];
			options.restore = true;
		}

		options.from = o.from || ( mode === "show" ? {
			height: 0,
			width: 0,
			outerHeight: 0,
			outerWidth: 0
		} : original );
		options.to = {
			height: original.height * factor.y,
			width: original.width * factor.x,
			outerHeight: original.outerHeight * factor.y,
			outerWidth: original.outerWidth * factor.x
		};

		// Fade option to support puff
		if ( options.fade ) {
			if ( mode === "show" ) {
				options.from.opacity = 0;
				options.to.opacity = 1;
			}
			if ( mode === "hide" ) {
				options.from.opacity = 1;
				options.to.opacity = 0;
			}
		}

		// Animate
		el.effect( options );

	};

	$.effects.effect.size = function( o, done ) {

		// Create element
		var original, baseline, factor,
			el = $( this ),
			props0 = [ "position", "top", "bottom", "left", "right", "width", "height", "overflow", "opacity" ],

			// Always restore
			props1 = [ "position", "top", "bottom", "left", "right", "overflow", "opacity" ],

			// Copy for children
			props2 = [ "width", "height", "overflow" ],
			cProps = [ "fontSize" ],
			vProps = [ "borderTopWidth", "borderBottomWidth", "paddingTop", "paddingBottom" ],
			hProps = [ "borderLeftWidth", "borderRightWidth", "paddingLeft", "paddingRight" ],

			// Set options
			mode = $.effects.setMode( el, o.mode || "effect" ),
			restore = o.restore || mode !== "effect",
			scale = o.scale || "both",
			origin = o.origin || [ "middle", "center" ],
			position = el.css( "position" ),
			props = restore ? props0 : props1,
			zero = {
				height: 0,
				width: 0,
				outerHeight: 0,
				outerWidth: 0
			};

		if ( mode === "show" ) {
			el.show();
		}
		original = {
			height: el.height(),
			width: el.width(),
			outerHeight: el.outerHeight(),
			outerWidth: el.outerWidth()
		};

		if ( o.mode === "toggle" && mode === "show" ) {
			el.from = o.to || zero;
			el.to = o.from || original;
		} else {
			el.from = o.from || ( mode === "show" ? zero : original );
			el.to = o.to || ( mode === "hide" ? zero : original );
		}

		// Set scaling factor
		factor = {
			from: {
				y: el.from.height / original.height,
				x: el.from.width / original.width
			},
			to: {
				y: el.to.height / original.height,
				x: el.to.width / original.width
			}
		};

		// Scale the css box
		if ( scale === "box" || scale === "both" ) {

			// Vertical props scaling
			if ( factor.from.y !== factor.to.y ) {
				props = props.concat( vProps );
				el.from = $.effects.setTransition( el, vProps, factor.from.y, el.from );
				el.to = $.effects.setTransition( el, vProps, factor.to.y, el.to );
			}

			// Horizontal props scaling
			if ( factor.from.x !== factor.to.x ) {
				props = props.concat( hProps );
				el.from = $.effects.setTransition( el, hProps, factor.from.x, el.from );
				el.to = $.effects.setTransition( el, hProps, factor.to.x, el.to );
			}
		}

		// Scale the content
		if ( scale === "content" || scale === "both" ) {

			// Vertical props scaling
			if ( factor.from.y !== factor.to.y ) {
				props = props.concat( cProps ).concat( props2 );
				el.from = $.effects.setTransition( el, cProps, factor.from.y, el.from );
				el.to = $.effects.setTransition( el, cProps, factor.to.y, el.to );
			}
		}

		$.effects.save( el, props );
		el.show();
		$.effects.createWrapper( el );
		el.css( "overflow", "hidden" ).css( el.from );

		// Adjust
		if (origin) { // Calculate baseline shifts
			baseline = $.effects.getBaseline( origin, original );
			el.from.top = ( original.outerHeight - el.outerHeight() ) * baseline.y;
			el.from.left = ( original.outerWidth - el.outerWidth() ) * baseline.x;
			el.to.top = ( original.outerHeight - el.to.outerHeight ) * baseline.y;
			el.to.left = ( original.outerWidth - el.to.outerWidth ) * baseline.x;
		}
		el.css( el.from ); // set top & left

		// Animate
		if ( scale === "content" || scale === "both" ) { // Scale the children

			// Add margins/font-size
			vProps = vProps.concat([ "marginTop", "marginBottom" ]).concat(cProps);
			hProps = hProps.concat([ "marginLeft", "marginRight" ]);
			props2 = props0.concat(vProps).concat(hProps);

			el.find( "*[width]" ).each( function(){
				var child = $( this ),
					c_original = {
						height: child.height(),
						width: child.width(),
						outerHeight: child.outerHeight(),
						outerWidth: child.outerWidth()
					};
				if (restore) {
					$.effects.save(child, props2);
				}

				child.from = {
					height: c_original.height * factor.from.y,
					width: c_original.width * factor.from.x,
					outerHeight: c_original.outerHeight * factor.from.y,
					outerWidth: c_original.outerWidth * factor.from.x
				};
				child.to = {
					height: c_original.height * factor.to.y,
					width: c_original.width * factor.to.x,
					outerHeight: c_original.height * factor.to.y,
					outerWidth: c_original.width * factor.to.x
				};

				// Vertical props scaling
				if ( factor.from.y !== factor.to.y ) {
					child.from = $.effects.setTransition( child, vProps, factor.from.y, child.from );
					child.to = $.effects.setTransition( child, vProps, factor.to.y, child.to );
				}

				// Horizontal props scaling
				if ( factor.from.x !== factor.to.x ) {
					child.from = $.effects.setTransition( child, hProps, factor.from.x, child.from );
					child.to = $.effects.setTransition( child, hProps, factor.to.x, child.to );
				}

				// Animate children
				child.css( child.from );
				child.animate( child.to, o.duration, o.easing, function() {

					// Restore children
					if ( restore ) {
						$.effects.restore( child, props2 );
					}
				});
			});
		}

		// Animate
		el.animate( el.to, {
			queue: false,
			duration: o.duration,
			easing: o.easing,
			complete: function() {
				if ( el.to.opacity === 0 ) {
					el.css( "opacity", el.from.opacity );
				}
				if( mode === "hide" ) {
					el.hide();
				}
				$.effects.restore( el, props );
				if ( !restore ) {

					// we need to calculate our new positioning based on the scaling
					if ( position === "static" ) {
						el.css({
							position: "relative",
							top: el.to.top,
							left: el.to.left
						});
					} else {
						$.each([ "top", "left" ], function( idx, pos ) {
							el.css( pos, function( _, str ) {
								var val = parseInt( str, 10 ),
									toRef = idx ? el.to.left : el.to.top;

								// if original was "auto", recalculate the new value from wrapper
								if ( str === "auto" ) {
									return toRef + "px";
								}

								return val + toRef + "px";
							});
						});
					}
				}

				$.effects.removeWrapper( el );
				done();
			}
		});

	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.shake = function( o, done ) {

		var el = $( this ),
			props = [ "position", "top", "bottom", "left", "right", "height", "width" ],
			mode = $.effects.setMode( el, o.mode || "effect" ),
			direction = o.direction || "left",
			distance = o.distance || 20,
			times = o.times || 3,
			anims = times * 2 + 1,
			speed = Math.round(o.duration/anims),
			ref = (direction === "up" || direction === "down") ? "top" : "left",
			positiveMotion = (direction === "up" || direction === "left"),
			animation = {},
			animation1 = {},
			animation2 = {},
			i,

			// we will need to re-assemble the queue to stack our animations in place
			queue = el.queue(),
			queuelen = queue.length;

		$.effects.save( el, props );
		el.show();
		$.effects.createWrapper( el );

		// Animation
		animation[ ref ] = ( positiveMotion ? "-=" : "+=" ) + distance;
		animation1[ ref ] = ( positiveMotion ? "+=" : "-=" ) + distance * 2;
		animation2[ ref ] = ( positiveMotion ? "-=" : "+=" ) + distance * 2;

		// Animate
		el.animate( animation, speed, o.easing );

		// Shakes
		for ( i = 1; i < times; i++ ) {
			el.animate( animation1, speed, o.easing ).animate( animation2, speed, o.easing );
		}
		el
			.animate( animation1, speed, o.easing )
			.animate( animation, speed / 2, o.easing )
			.queue(function() {
				if ( mode === "hide" ) {
					el.hide();
				}
				$.effects.restore( el, props );
				$.effects.removeWrapper( el );
				done();
			});

		// inject all the animations we just queued to be first in line (after "inprogress")
		if ( queuelen > 1) {
			queue.splice.apply( queue,
				[ 1, 0 ].concat( queue.splice( queuelen, anims + 1 ) ) );
		}
		el.dequeue();

	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.slide = function( o, done ) {

		// Create element
		var el = $( this ),
			props = [ "position", "top", "bottom", "left", "right", "width", "height" ],
			mode = $.effects.setMode( el, o.mode || "show" ),
			show = mode === "show",
			direction = o.direction || "left",
			ref = (direction === "up" || direction === "down") ? "top" : "left",
			positiveMotion = (direction === "up" || direction === "left"),
			distance,
			animation = {};

		// Adjust
		$.effects.save( el, props );
		el.show();
		distance = o.distance || el[ ref === "top" ? "outerHeight" : "outerWidth" ]( true );

		$.effects.createWrapper( el ).css({
			overflow: "hidden"
		});

		if ( show ) {
			el.css( ref, positiveMotion ? (isNaN(distance) ? "-" + distance : -distance) : distance );
		}

		// Animation
		animation[ ref ] = ( show ?
			( positiveMotion ? "+=" : "-=") :
			( positiveMotion ? "-=" : "+=")) +
			distance;

		// Animate
		el.animate( animation, {
			queue: false,
			duration: o.duration,
			easing: o.easing,
			complete: function() {
				if ( mode === "hide" ) {
					el.hide();
				}
				$.effects.restore( el, props );
				$.effects.removeWrapper( el );
				done();
			}
		});
	};

	})(jQuery);

	(function( $, undefined ) {

	$.effects.effect.transfer = function( o, done ) {
		var elem = $( this ),
			target = $( o.to ),
			targetFixed = target.css( "position" ) === "fixed",
			body = $("body"),
			fixTop = targetFixed ? body.scrollTop() : 0,
			fixLeft = targetFixed ? body.scrollLeft() : 0,
			endPosition = target.offset(),
			animation = {
				top: endPosition.top - fixTop ,
				left: endPosition.left - fixLeft ,
				height: target.innerHeight(),
				width: target.innerWidth()
			},
			startPosition = elem.offset(),
			transfer = $( "<div class='ui-effects-transfer'></div>" )
				.appendTo( document.body )
				.addClass( o.className )
				.css({
					top: startPosition.top - fixTop ,
					left: startPosition.left - fixLeft ,
					height: elem.innerHeight(),
					width: elem.innerWidth(),
					position: targetFixed ? "fixed" : "absolute"
				})
				.animate( animation, o.duration, o.easing, function() {
					transfer.remove();
					done();
				});
	};

	})(jQuery);

	(function( $, undefined ) {

	$.widget( "ui.menu", {
		version: "1.10.3",
		defaultElement: "<ul>",
		delay: 300,
		options: {
			icons: {
				submenu: "ui-icon-carat-1-e"
			},
			menus: "ul",
			position: {
				my: "left top",
				at: "right top"
			},
			role: "menu",

			// callbacks
			blur: null,
			focus: null,
			select: null
		},

		_create: function() {
			this.activeMenu = this.element;
			// flag used to prevent firing of the click handler
			// as the event bubbles up through nested menus
			this.mouseHandled = false;
			this.element
				.uniqueId()
				.addClass( "ui-menu ui-widget ui-widget-content ui-corner-all" )
				.toggleClass( "ui-menu-icons", !!this.element.find( ".ui-icon" ).length )
				.attr({
					role: this.options.role,
					tabIndex: 0
				})
				// need to catch all clicks on disabled menu
				// not possible through _on
				.bind( "click" + this.eventNamespace, $.proxy(function( event ) {
					if ( this.options.disabled ) {
						event.preventDefault();
					}
				}, this ));

			if ( this.options.disabled ) {
				this.element
					.addClass( "ui-state-disabled" )
					.attr( "aria-disabled", "true" );
			}

			this._on({
				// Prevent focus from sticking to links inside menu after clicking
				// them (focus should always stay on UL during navigation).
				"mousedown .ui-menu-item > a": function( event ) {
					event.preventDefault();
				},
				"click .ui-state-disabled > a": function( event ) {
					event.preventDefault();
				},
				"click .ui-menu-item:has(a)": function( event ) {
					var target = $( event.target ).closest( ".ui-menu-item" );
					if ( !this.mouseHandled && target.not( ".ui-state-disabled" ).length ) {
						this.mouseHandled = true;

						this.select( event );
						// Open submenu on click
						if ( target.has( ".ui-menu" ).length ) {
							this.expand( event );
						} else if ( !this.element.is( ":focus" ) ) {
							// Redirect focus to the menu
							this.element.trigger( "focus", [ true ] );

							// If the active item is on the top level, let it stay active.
							// Otherwise, blur the active item since it is no longer visible.
							if ( this.active && this.active.parents( ".ui-menu" ).length === 1 ) {
								clearTimeout( this.timer );
							}
						}
					}
				},
				"mouseenter .ui-menu-item": function( event ) {
					var target = $( event.currentTarget );
					// Remove ui-state-active class from siblings of the newly focused menu item
					// to avoid a jump caused by adjacent elements both having a class with a border
					target.siblings().children( ".ui-state-active" ).removeClass( "ui-state-active" );
					this.focus( event, target );
				},
				mouseleave: "collapseAll",
				"mouseleave .ui-menu": "collapseAll",
				focus: function( event, keepActiveItem ) {
					// If there's already an active item, keep it active
					// If not, activate the first item
					var item = this.active || this.element.children( ".ui-menu-item" ).eq( 0 );

					if ( !keepActiveItem ) {
						this.focus( event, item );
					}
				},
				blur: function( event ) {
					this._delay(function() {
						if ( !$.contains( this.element[0], this.document[0].activeElement ) ) {
							this.collapseAll( event );
						}
					});
				},
				keydown: "_keydown"
			});

			this.refresh();

			// Clicks outside of a menu collapse any open menus
			this._on( this.document, {
				click: function( event ) {
					if ( !$( event.target ).closest( ".ui-menu" ).length ) {
						this.collapseAll( event );
					}

					// Reset the mouseHandled flag
					this.mouseHandled = false;
				}
			});
		},

		_destroy: function() {
			// Destroy (sub)menus
			this.element
				.removeAttr( "aria-activedescendant" )
				.find( ".ui-menu" ).addBack()
					.removeClass( "ui-menu ui-widget ui-widget-content ui-corner-all ui-menu-icons" )
					.removeAttr( "role" )
					.removeAttr( "tabIndex" )
					.removeAttr( "aria-labelledby" )
					.removeAttr( "aria-expanded" )
					.removeAttr( "aria-hidden" )
					.removeAttr( "aria-disabled" )
					.removeUniqueId()
					.show();

			// Destroy menu items
			this.element.find( ".ui-menu-item" )
				.removeClass( "ui-menu-item" )
				.removeAttr( "role" )
				.removeAttr( "aria-disabled" )
				.children( "a" )
					.removeUniqueId()
					.removeClass( "ui-corner-all ui-state-hover" )
					.removeAttr( "tabIndex" )
					.removeAttr( "role" )
					.removeAttr( "aria-haspopup" )
					.children().each( function() {
						var elem = $( this );
						if ( elem.data( "ui-menu-submenu-carat" ) ) {
							elem.remove();
						}
					});

			// Destroy menu dividers
			this.element.find( ".ui-menu-divider" ).removeClass( "ui-menu-divider ui-widget-content" );
		},

		_keydown: function( event ) {
			/*jshint maxcomplexity:20*/
			var match, prev, character, skip, regex,
				preventDefault = true;

			function escape( value ) {
				return value.replace( /[\-\[\]{}()*+?.,\\\^$|#\s]/g, "\\$&" );
			}

			switch ( event.keyCode ) {
			case $.ui.keyCode.PAGE_UP:
				this.previousPage( event );
				break;
			case $.ui.keyCode.PAGE_DOWN:
				this.nextPage( event );
				break;
			case $.ui.keyCode.HOME:
				this._move( "first", "first", event );
				break;
			case $.ui.keyCode.END:
				this._move( "last", "last", event );
				break;
			case $.ui.keyCode.UP:
				this.previous( event );
				break;
			case $.ui.keyCode.DOWN:
				this.next( event );
				break;
			case $.ui.keyCode.LEFT:
				this.collapse( event );
				break;
			case $.ui.keyCode.RIGHT:
				if ( this.active && !this.active.is( ".ui-state-disabled" ) ) {
					this.expand( event );
				}
				break;
			case $.ui.keyCode.ENTER:
			case $.ui.keyCode.SPACE:
				this._activate( event );
				break;
			case $.ui.keyCode.ESCAPE:
				this.collapse( event );
				break;
			default:
				preventDefault = false;
				prev = this.previousFilter || "";
				character = String.fromCharCode( event.keyCode );
				skip = false;

				clearTimeout( this.filterTimer );

				if ( character === prev ) {
					skip = true;
				} else {
					character = prev + character;
				}

				regex = new RegExp( "^" + escape( character ), "i" );
				match = this.activeMenu.children( ".ui-menu-item" ).filter(function() {
					return regex.test( $( this ).children( "a" ).text() );
				});
				match = skip && match.index( this.active.next() ) !== -1 ?
					this.active.nextAll( ".ui-menu-item" ) :
					match;

				// If no matches on the current filter, reset to the last character pressed
				// to move down the menu to the first item that starts with that character
				if ( !match.length ) {
					character = String.fromCharCode( event.keyCode );
					regex = new RegExp( "^" + escape( character ), "i" );
					match = this.activeMenu.children( ".ui-menu-item" ).filter(function() {
						return regex.test( $( this ).children( "a" ).text() );
					});
				}

				if ( match.length ) {
					this.focus( event, match );
					if ( match.length > 1 ) {
						this.previousFilter = character;
						this.filterTimer = this._delay(function() {
							delete this.previousFilter;
						}, 1000 );
					} else {
						delete this.previousFilter;
					}
				} else {
					delete this.previousFilter;
				}
			}

			if ( preventDefault ) {
				event.preventDefault();
			}
		},

		_activate: function( event ) {
			if ( !this.active.is( ".ui-state-disabled" ) ) {
				if ( this.active.children( "a[aria-haspopup='true']" ).length ) {
					this.expand( event );
				} else {
					this.select( event );
				}
			}
		},

		refresh: function() {
			var menus,
				icon = this.options.icons.submenu,
				submenus = this.element.find( this.options.menus );

			// Initialize nested menus
			submenus.filter( ":not(.ui-menu)" )
				.addClass( "ui-menu ui-widget ui-widget-content ui-corner-all" )
				.hide()
				.attr({
					role: this.options.role,
					"aria-hidden": "true",
					"aria-expanded": "false"
				})
				.each(function() {
					var menu = $( this ),
						item = menu.prev( "a" ),
						submenuCarat = $( "<span>" )
							.addClass( "ui-menu-icon ui-icon " + icon )
							.data( "ui-menu-submenu-carat", true );

					item
						.attr( "aria-haspopup", "true" )
						.prepend( submenuCarat );
					menu.attr( "aria-labelledby", item.attr( "id" ) );
				});

			menus = submenus.add( this.element );

			// Don't refresh list items that are already adapted
			menus.children( ":not(.ui-menu-item):has(a)" )
				.addClass( "ui-menu-item" )
				.attr( "role", "presentation" )
				.children( "a" )
					.uniqueId()
					.addClass( "ui-corner-all" )
					.attr({
						tabIndex: -1,
						role: this._itemRole()
					});

			// Initialize unlinked menu-items containing spaces and/or dashes only as dividers
			menus.children( ":not(.ui-menu-item)" ).each(function() {
				var item = $( this );
				// hyphen, em dash, en dash
				if ( !/[^\-\u2014\u2013\s]/.test( item.text() ) ) {
					item.addClass( "ui-widget-content ui-menu-divider" );
				}
			});

			// Add aria-disabled attribute to any disabled menu item
			menus.children( ".ui-state-disabled" ).attr( "aria-disabled", "true" );

			// If the active item has been removed, blur the menu
			if ( this.active && !$.contains( this.element[ 0 ], this.active[ 0 ] ) ) {
				this.blur();
			}
		},

		_itemRole: function() {
			return {
				menu: "menuitem",
				listbox: "option"
			}[ this.options.role ];
		},

		_setOption: function( key, value ) {
			if ( key === "icons" ) {
				this.element.find( ".ui-menu-icon" )
					.removeClass( this.options.icons.submenu )
					.addClass( value.submenu );
			}
			this._super( key, value );
		},

		focus: function( event, item ) {
			var nested, focused;
			this.blur( event, event && event.type === "focus" );

			this._scrollIntoView( item );

			this.active = item.first();
			focused = this.active.children( "a" ).addClass( "ui-state-focus" );
			// Only update aria-activedescendant if there's a role
			// otherwise we assume focus is managed elsewhere
			if ( this.options.role ) {
				this.element.attr( "aria-activedescendant", focused.attr( "id" ) );
			}

			// Highlight active parent menu item, if any
			this.active
				.parent()
				.closest( ".ui-menu-item" )
				.children( "a:first" )
				.addClass( "ui-state-active" );

			if ( event && event.type === "keydown" ) {
				this._close();
			} else {
				this.timer = this._delay(function() {
					this._close();
				}, this.delay );
			}

			nested = item.children( ".ui-menu" );
			if ( nested.length && ( /^mouse/.test( event.type ) ) ) {
				this._startOpening(nested);
			}
			this.activeMenu = item.parent();

			this._trigger( "focus", event, { item: item } );
		},

		_scrollIntoView: function( item ) {
			var borderTop, paddingTop, offset, scroll, elementHeight, itemHeight;
			if ( this._hasScroll() ) {
				borderTop = parseFloat( $.css( this.activeMenu[0], "borderTopWidth" ) ) || 0;
				paddingTop = parseFloat( $.css( this.activeMenu[0], "paddingTop" ) ) || 0;
				offset = item.offset().top - this.activeMenu.offset().top - borderTop - paddingTop;
				scroll = this.activeMenu.scrollTop();
				elementHeight = this.activeMenu.height();
				itemHeight = item.height();

				if ( offset < 0 ) {
					this.activeMenu.scrollTop( scroll + offset );
				} else if ( offset + itemHeight > elementHeight ) {
					this.activeMenu.scrollTop( scroll + offset - elementHeight + itemHeight );
				}
			}
		},

		blur: function( event, fromFocus ) {
			if ( !fromFocus ) {
				clearTimeout( this.timer );
			}

			if ( !this.active ) {
				return;
			}

			this.active.children( "a" ).removeClass( "ui-state-focus" );
			this.active = null;

			this._trigger( "blur", event, { item: this.active } );
		},

		_startOpening: function( submenu ) {
			clearTimeout( this.timer );

			// Don't open if already open fixes a Firefox bug that caused a .5 pixel
			// shift in the submenu position when mousing over the carat icon
			if ( submenu.attr( "aria-hidden" ) !== "true" ) {
				return;
			}

			this.timer = this._delay(function() {
				this._close();
				this._open( submenu );
			}, this.delay );
		},

		_open: function( submenu ) {
			var position = $.extend({
				of: this.active
			}, this.options.position );

			clearTimeout( this.timer );
			this.element.find( ".ui-menu" ).not( submenu.parents( ".ui-menu" ) )
				.hide()
				.attr( "aria-hidden", "true" );

			submenu
				.show()
				.removeAttr( "aria-hidden" )
				.attr( "aria-expanded", "true" )
				.position( position );
		},

		collapseAll: function( event, all ) {
			clearTimeout( this.timer );
			this.timer = this._delay(function() {
				// If we were passed an event, look for the submenu that contains the event
				var currentMenu = all ? this.element :
					$( event && event.target ).closest( this.element.find( ".ui-menu" ) );

				// If we found no valid submenu ancestor, use the main menu to close all sub menus anyway
				if ( !currentMenu.length ) {
					currentMenu = this.element;
				}

				this._close( currentMenu );

				this.blur( event );
				this.activeMenu = currentMenu;
			}, this.delay );
		},

		// With no arguments, closes the currently active menu - if nothing is active
		// it closes all menus.  If passed an argument, it will search for menus BELOW
		_close: function( startMenu ) {
			if ( !startMenu ) {
				startMenu = this.active ? this.active.parent() : this.element;
			}

			startMenu
				.find( ".ui-menu" )
					.hide()
					.attr( "aria-hidden", "true" )
					.attr( "aria-expanded", "false" )
				.end()
				.find( "a.ui-state-active" )
					.removeClass( "ui-state-active" );
		},

		collapse: function( event ) {
			var newItem = this.active &&
				this.active.parent().closest( ".ui-menu-item", this.element );
			if ( newItem && newItem.length ) {
				this._close();
				this.focus( event, newItem );
			}
		},

		expand: function( event ) {
			var newItem = this.active &&
				this.active
					.children( ".ui-menu " )
					.children( ".ui-menu-item" )
					.first();

			if ( newItem && newItem.length ) {
				this._open( newItem.parent() );

				// Delay so Firefox will not hide activedescendant change in expanding submenu from AT
				this._delay(function() {
					this.focus( event, newItem );
				});
			}
		},

		next: function( event ) {
			this._move( "next", "first", event );
		},

		previous: function( event ) {
			this._move( "prev", "last", event );
		},

		isFirstItem: function() {
			return this.active && !this.active.prevAll( ".ui-menu-item" ).length;
		},

		isLastItem: function() {
			return this.active && !this.active.nextAll( ".ui-menu-item" ).length;
		},

		_move: function( direction, filter, event ) {
			var next;
			if ( this.active ) {
				if ( direction === "first" || direction === "last" ) {
					next = this.active
						[ direction === "first" ? "prevAll" : "nextAll" ]( ".ui-menu-item" )
						.eq( -1 );
				} else {
					next = this.active
						[ direction + "All" ]( ".ui-menu-item" )
						.eq( 0 );
				}
			}
			if ( !next || !next.length || !this.active ) {
				next = this.activeMenu.children( ".ui-menu-item" )[ filter ]();
			}

			this.focus( event, next );
		},

		nextPage: function( event ) {
			var item, base, height;

			if ( !this.active ) {
				this.next( event );
				return;
			}
			if ( this.isLastItem() ) {
				return;
			}
			if ( this._hasScroll() ) {
				base = this.active.offset().top;
				height = this.element.height();
				this.active.nextAll( ".ui-menu-item" ).each(function() {
					item = $( this );
					return item.offset().top - base - height < 0;
				});

				this.focus( event, item );
			} else {
				this.focus( event, this.activeMenu.children( ".ui-menu-item" )
					[ !this.active ? "first" : "last" ]() );
			}
		},

		previousPage: function( event ) {
			var item, base, height;
			if ( !this.active ) {
				this.next( event );
				return;
			}
			if ( this.isFirstItem() ) {
				return;
			}
			if ( this._hasScroll() ) {
				base = this.active.offset().top;
				height = this.element.height();
				this.active.prevAll( ".ui-menu-item" ).each(function() {
					item = $( this );
					return item.offset().top - base + height > 0;
				});

				this.focus( event, item );
			} else {
				this.focus( event, this.activeMenu.children( ".ui-menu-item" ).first() );
			}
		},

		_hasScroll: function() {
			return this.element.outerHeight() < this.element.prop( "scrollHeight" );
		},

		select: function( event ) {
			// TODO: It should never be possible to not have an active item at this
			// point, but the tests don't trigger mouseenter before click.
			this.active = this.active || $( event.target ).closest( ".ui-menu-item" );
			var ui = { item: this.active };
			if ( !this.active.has( ".ui-menu" ).length ) {
				this.collapseAll( event, true );
			}
			this._trigger( "select", event, ui );
		}
	});

	}( jQuery ));

	(function( $, undefined ) {

	$.ui = $.ui || {};

	var cachedScrollbarWidth,
		max = Math.max,
		abs = Math.abs,
		round = Math.round,
		rhorizontal = /left|center|right/,
		rvertical = /top|center|bottom/,
		roffset = /[\+\-]\d+(\.[\d]+)?%?/,
		rposition = /^\w+/,
		rpercent = /%$/,
		_position = $.fn.position;

	function getOffsets( offsets, width, height ) {
		return [
			parseFloat( offsets[ 0 ] ) * ( rpercent.test( offsets[ 0 ] ) ? width / 100 : 1 ),
			parseFloat( offsets[ 1 ] ) * ( rpercent.test( offsets[ 1 ] ) ? height / 100 : 1 )
		];
	}

	function parseCss( element, property ) {
		return parseInt( $.css( element, property ), 10 ) || 0;
	}

	function getDimensions( elem ) {
		var raw = elem[0];
		if ( raw.nodeType === 9 ) {
			return {
				width: elem.width(),
				height: elem.height(),
				offset: { top: 0, left: 0 }
			};
		}
		if ( $.isWindow( raw ) ) {
			return {
				width: elem.width(),
				height: elem.height(),
				offset: { top: elem.scrollTop(), left: elem.scrollLeft() }
			};
		}
		if ( raw.preventDefault ) {
			return {
				width: 0,
				height: 0,
				offset: { top: raw.pageY, left: raw.pageX }
			};
		}
		return {
			width: elem.outerWidth(),
			height: elem.outerHeight(),
			offset: elem.offset()
		};
	}

	$.position = {
		scrollbarWidth: function() {
			if ( cachedScrollbarWidth !== undefined ) {
				return cachedScrollbarWidth;
			}
			var w1, w2,
				div = $( "<div style='display:block;width:50px;height:50px;overflow:hidden;'><div style='height:100px;width:auto;'></div></div>" ),
				innerDiv = div.children()[0];

			$( "body" ).append( div );
			w1 = innerDiv.offsetWidth;
			div.css( "overflow", "scroll" );

			w2 = innerDiv.offsetWidth;

			if ( w1 === w2 ) {
				w2 = div[0].clientWidth;
			}

			div.remove();

			return (cachedScrollbarWidth = w1 - w2);
		},
		getScrollInfo: function( within ) {
			var overflowX = within.isWindow ? "" : within.element.css( "overflow-x" ),
				overflowY = within.isWindow ? "" : within.element.css( "overflow-y" ),
				hasOverflowX = overflowX === "scroll" ||
					( overflowX === "auto" && within.width < within.element[0].scrollWidth ),
				hasOverflowY = overflowY === "scroll" ||
					( overflowY === "auto" && within.height < within.element[0].scrollHeight );
			return {
				width: hasOverflowY ? $.position.scrollbarWidth() : 0,
				height: hasOverflowX ? $.position.scrollbarWidth() : 0
			};
		},
		getWithinInfo: function( element ) {
			var withinElement = $( element || window ),
				isWindow = $.isWindow( withinElement[0] );
			return {
				element: withinElement,
				isWindow: isWindow,
				offset: withinElement.offset() || { left: 0, top: 0 },
				scrollLeft: withinElement.scrollLeft(),
				scrollTop: withinElement.scrollTop(),
				width: isWindow ? withinElement.width() : withinElement.outerWidth(),
				height: isWindow ? withinElement.height() : withinElement.outerHeight()
			};
		}
	};

	$.fn.position = function( options ) {
		if ( !options || !options.of ) {
			return _position.apply( this, arguments );
		}

		// make a copy, we don't want to modify arguments
		options = $.extend( {}, options );

		var atOffset, targetWidth, targetHeight, targetOffset, basePosition, dimensions,
			target = $( options.of ),
			within = $.position.getWithinInfo( options.within ),
			scrollInfo = $.position.getScrollInfo( within ),
			collision = ( options.collision || "flip" ).split( " " ),
			offsets = {};

		dimensions = getDimensions( target );
		if ( target[0].preventDefault ) {
			// force left top to allow flipping
			options.at = "left top";
		}
		targetWidth = dimensions.width;
		targetHeight = dimensions.height;
		targetOffset = dimensions.offset;
		// clone to reuse original targetOffset later
		basePosition = $.extend( {}, targetOffset );

		// force my and at to have valid horizontal and vertical positions
		// if a value is missing or invalid, it will be converted to center
		$.each( [ "my", "at" ], function() {
			var pos = ( options[ this ] || "" ).split( " " ),
				horizontalOffset,
				verticalOffset;

			if ( pos.length === 1) {
				pos = rhorizontal.test( pos[ 0 ] ) ?
					pos.concat( [ "center" ] ) :
					rvertical.test( pos[ 0 ] ) ?
						[ "center" ].concat( pos ) :
						[ "center", "center" ];
			}
			pos[ 0 ] = rhorizontal.test( pos[ 0 ] ) ? pos[ 0 ] : "center";
			pos[ 1 ] = rvertical.test( pos[ 1 ] ) ? pos[ 1 ] : "center";

			// calculate offsets
			horizontalOffset = roffset.exec( pos[ 0 ] );
			verticalOffset = roffset.exec( pos[ 1 ] );
			offsets[ this ] = [
				horizontalOffset ? horizontalOffset[ 0 ] : 0,
				verticalOffset ? verticalOffset[ 0 ] : 0
			];

			// reduce to just the positions without the offsets
			options[ this ] = [
				rposition.exec( pos[ 0 ] )[ 0 ],
				rposition.exec( pos[ 1 ] )[ 0 ]
			];
		});

		// normalize collision option
		if ( collision.length === 1 ) {
			collision[ 1 ] = collision[ 0 ];
		}

		if ( options.at[ 0 ] === "right" ) {
			basePosition.left += targetWidth;
		} else if ( options.at[ 0 ] === "center" ) {
			basePosition.left += targetWidth / 2;
		}

		if ( options.at[ 1 ] === "bottom" ) {
			basePosition.top += targetHeight;
		} else if ( options.at[ 1 ] === "center" ) {
			basePosition.top += targetHeight / 2;
		}

		atOffset = getOffsets( offsets.at, targetWidth, targetHeight );
		basePosition.left += atOffset[ 0 ];
		basePosition.top += atOffset[ 1 ];

		return this.each(function() {
			var collisionPosition, using,
				elem = $( this ),
				elemWidth = elem.outerWidth(),
				elemHeight = elem.outerHeight(),
				marginLeft = parseCss( this, "marginLeft" ),
				marginTop = parseCss( this, "marginTop" ),
				collisionWidth = elemWidth + marginLeft + parseCss( this, "marginRight" ) + scrollInfo.width,
				collisionHeight = elemHeight + marginTop + parseCss( this, "marginBottom" ) + scrollInfo.height,
				position = $.extend( {}, basePosition ),
				myOffset = getOffsets( offsets.my, elem.outerWidth(), elem.outerHeight() );

			if ( options.my[ 0 ] === "right" ) {
				position.left -= elemWidth;
			} else if ( options.my[ 0 ] === "center" ) {
				position.left -= elemWidth / 2;
			}

			if ( options.my[ 1 ] === "bottom" ) {
				position.top -= elemHeight;
			} else if ( options.my[ 1 ] === "center" ) {
				position.top -= elemHeight / 2;
			}

			position.left += myOffset[ 0 ];
			position.top += myOffset[ 1 ];

			// if the browser doesn't support fractions, then round for consistent results
			if ( !$.support.offsetFractions ) {
				position.left = round( position.left );
				position.top = round( position.top );
			}

			collisionPosition = {
				marginLeft: marginLeft,
				marginTop: marginTop
			};

			$.each( [ "left", "top" ], function( i, dir ) {
				if ( $.ui.position[ collision[ i ] ] ) {
					$.ui.position[ collision[ i ] ][ dir ]( position, {
						targetWidth: targetWidth,
						targetHeight: targetHeight,
						elemWidth: elemWidth,
						elemHeight: elemHeight,
						collisionPosition: collisionPosition,
						collisionWidth: collisionWidth,
						collisionHeight: collisionHeight,
						offset: [ atOffset[ 0 ] + myOffset[ 0 ], atOffset [ 1 ] + myOffset[ 1 ] ],
						my: options.my,
						at: options.at,
						within: within,
						elem : elem
					});
				}
			});

			if ( options.using ) {
				// adds feedback as second argument to using callback, if present
				using = function( props ) {
					var left = targetOffset.left - position.left,
						right = left + targetWidth - elemWidth,
						top = targetOffset.top - position.top,
						bottom = top + targetHeight - elemHeight,
						feedback = {
							target: {
								element: target,
								left: targetOffset.left,
								top: targetOffset.top,
								width: targetWidth,
								height: targetHeight
							},
							element: {
								element: elem,
								left: position.left,
								top: position.top,
								width: elemWidth,
								height: elemHeight
							},
							horizontal: right < 0 ? "left" : left > 0 ? "right" : "center",
							vertical: bottom < 0 ? "top" : top > 0 ? "bottom" : "middle"
						};
					if ( targetWidth < elemWidth && abs( left + right ) < targetWidth ) {
						feedback.horizontal = "center";
					}
					if ( targetHeight < elemHeight && abs( top + bottom ) < targetHeight ) {
						feedback.vertical = "middle";
					}
					if ( max( abs( left ), abs( right ) ) > max( abs( top ), abs( bottom ) ) ) {
						feedback.important = "horizontal";
					} else {
						feedback.important = "vertical";
					}
					options.using.call( this, props, feedback );
				};
			}

			elem.offset( $.extend( position, { using: using } ) );
		});
	};

	$.ui.position = {
		fit: {
			left: function( position, data ) {
				var within = data.within,
					withinOffset = within.isWindow ? within.scrollLeft : within.offset.left,
					outerWidth = within.width,
					collisionPosLeft = position.left - data.collisionPosition.marginLeft,
					overLeft = withinOffset - collisionPosLeft,
					overRight = collisionPosLeft + data.collisionWidth - outerWidth - withinOffset,
					newOverRight;

				// element is wider than within
				if ( data.collisionWidth > outerWidth ) {
					// element is initially over the left side of within
					if ( overLeft > 0 && overRight <= 0 ) {
						newOverRight = position.left + overLeft + data.collisionWidth - outerWidth - withinOffset;
						position.left += overLeft - newOverRight;
					// element is initially over right side of within
					} else if ( overRight > 0 && overLeft <= 0 ) {
						position.left = withinOffset;
					// element is initially over both left and right sides of within
					} else {
						if ( overLeft > overRight ) {
							position.left = withinOffset + outerWidth - data.collisionWidth;
						} else {
							position.left = withinOffset;
						}
					}
				// too far left -> align with left edge
				} else if ( overLeft > 0 ) {
					position.left += overLeft;
				// too far right -> align with right edge
				} else if ( overRight > 0 ) {
					position.left -= overRight;
				// adjust based on position and margin
				} else {
					position.left = max( position.left - collisionPosLeft, position.left );
				}
			},
			top: function( position, data ) {
				var within = data.within,
					withinOffset = within.isWindow ? within.scrollTop : within.offset.top,
					outerHeight = data.within.height,
					collisionPosTop = position.top - data.collisionPosition.marginTop,
					overTop = withinOffset - collisionPosTop,
					overBottom = collisionPosTop + data.collisionHeight - outerHeight - withinOffset,
					newOverBottom;

				// element is taller than within
				if ( data.collisionHeight > outerHeight ) {
					// element is initially over the top of within
					if ( overTop > 0 && overBottom <= 0 ) {
						newOverBottom = position.top + overTop + data.collisionHeight - outerHeight - withinOffset;
						position.top += overTop - newOverBottom;
					// element is initially over bottom of within
					} else if ( overBottom > 0 && overTop <= 0 ) {
						position.top = withinOffset;
					// element is initially over both top and bottom of within
					} else {
						if ( overTop > overBottom ) {
							position.top = withinOffset + outerHeight - data.collisionHeight;
						} else {
							position.top = withinOffset;
						}
					}
				// too far up -> align with top
				} else if ( overTop > 0 ) {
					position.top += overTop;
				// too far down -> align with bottom edge
				} else if ( overBottom > 0 ) {
					position.top -= overBottom;
				// adjust based on position and margin
				} else {
					position.top = max( position.top - collisionPosTop, position.top );
				}
			}
		},
		flip: {
			left: function( position, data ) {
				var within = data.within,
					withinOffset = within.offset.left + within.scrollLeft,
					outerWidth = within.width,
					offsetLeft = within.isWindow ? within.scrollLeft : within.offset.left,
					collisionPosLeft = position.left - data.collisionPosition.marginLeft,
					overLeft = collisionPosLeft - offsetLeft,
					overRight = collisionPosLeft + data.collisionWidth - outerWidth - offsetLeft,
					myOffset = data.my[ 0 ] === "left" ?
						-data.elemWidth :
						data.my[ 0 ] === "right" ?
							data.elemWidth :
							0,
					atOffset = data.at[ 0 ] === "left" ?
						data.targetWidth :
						data.at[ 0 ] === "right" ?
							-data.targetWidth :
							0,
					offset = -2 * data.offset[ 0 ],
					newOverRight,
					newOverLeft;

				if ( overLeft < 0 ) {
					newOverRight = position.left + myOffset + atOffset + offset + data.collisionWidth - outerWidth - withinOffset;
					if ( newOverRight < 0 || newOverRight < abs( overLeft ) ) {
						position.left += myOffset + atOffset + offset;
					}
				}
				else if ( overRight > 0 ) {
					newOverLeft = position.left - data.collisionPosition.marginLeft + myOffset + atOffset + offset - offsetLeft;
					if ( newOverLeft > 0 || abs( newOverLeft ) < overRight ) {
						position.left += myOffset + atOffset + offset;
					}
				}
			},
			top: function( position, data ) {
				var within = data.within,
					withinOffset = within.offset.top + within.scrollTop,
					outerHeight = within.height,
					offsetTop = within.isWindow ? within.scrollTop : within.offset.top,
					collisionPosTop = position.top - data.collisionPosition.marginTop,
					overTop = collisionPosTop - offsetTop,
					overBottom = collisionPosTop + data.collisionHeight - outerHeight - offsetTop,
					top = data.my[ 1 ] === "top",
					myOffset = top ?
						-data.elemHeight :
						data.my[ 1 ] === "bottom" ?
							data.elemHeight :
							0,
					atOffset = data.at[ 1 ] === "top" ?
						data.targetHeight :
						data.at[ 1 ] === "bottom" ?
							-data.targetHeight :
							0,
					offset = -2 * data.offset[ 1 ],
					newOverTop,
					newOverBottom;
				if ( overTop < 0 ) {
					newOverBottom = position.top + myOffset + atOffset + offset + data.collisionHeight - outerHeight - withinOffset;
					if ( ( position.top + myOffset + atOffset + offset) > overTop && ( newOverBottom < 0 || newOverBottom < abs( overTop ) ) ) {
						position.top += myOffset + atOffset + offset;
					}
				}
				else if ( overBottom > 0 ) {
					newOverTop = position.top -  data.collisionPosition.marginTop + myOffset + atOffset + offset - offsetTop;
					if ( ( position.top + myOffset + atOffset + offset) > overBottom && ( newOverTop > 0 || abs( newOverTop ) < overBottom ) ) {
						position.top += myOffset + atOffset + offset;
					}
				}
			}
		},
		flipfit: {
			left: function() {
				$.ui.position.flip.left.apply( this, arguments );
				$.ui.position.fit.left.apply( this, arguments );
			},
			top: function() {
				$.ui.position.flip.top.apply( this, arguments );
				$.ui.position.fit.top.apply( this, arguments );
			}
		}
	};

	// fraction support test
	(function () {
		var testElement, testElementParent, testElementStyle, offsetLeft, i,
			body = document.getElementsByTagName( "body" )[ 0 ],
			div = document.createElement( "div" );

		//Create a "fake body" for testing based on method used in jQuery.support
		testElement = document.createElement( body ? "div" : "body" );
		testElementStyle = {
			visibility: "hidden",
			width: 0,
			height: 0,
			border: 0,
			margin: 0,
			background: "none"
		};
		if ( body ) {
			$.extend( testElementStyle, {
				position: "absolute",
				left: "-1000px",
				top: "-1000px"
			});
		}
		for ( i in testElementStyle ) {
			testElement.style[ i ] = testElementStyle[ i ];
		}
		testElement.appendChild( div );
		testElementParent = body || document.documentElement;
		testElementParent.insertBefore( testElement, testElementParent.firstChild );

		div.style.cssText = "position: absolute; left: 10.7432222px;";

		offsetLeft = $( div ).offset().left;
		$.support.offsetFractions = offsetLeft > 10 && offsetLeft < 11;

		testElement.innerHTML = "";
		testElementParent.removeChild( testElement );
	})();

	}( jQuery ) );

	(function( $, undefined ) {

	$.widget( "ui.progressbar", {
		version: "1.10.3",
		options: {
			max: 100,
			value: 0,

			change: null,
			complete: null
		},

		min: 0,

		_create: function() {
			// Constrain initial value
			this.oldValue = this.options.value = this._constrainedValue();

			this.element
				.addClass( "ui-progressbar ui-widget ui-widget-content ui-corner-all" )
				.attr({
					// Only set static values, aria-valuenow and aria-valuemax are
					// set inside _refreshValue()
					role: "progressbar",
					"aria-valuemin": this.min
				});

			this.valueDiv = $( "<div class='ui-progressbar-value ui-widget-header ui-corner-left'></div>" )
				.appendTo( this.element );

			this._refreshValue();
		},

		_destroy: function() {
			this.element
				.removeClass( "ui-progressbar ui-widget ui-widget-content ui-corner-all" )
				.removeAttr( "role" )
				.removeAttr( "aria-valuemin" )
				.removeAttr( "aria-valuemax" )
				.removeAttr( "aria-valuenow" );

			this.valueDiv.remove();
		},

		value: function( newValue ) {
			if ( newValue === undefined ) {
				return this.options.value;
			}

			this.options.value = this._constrainedValue( newValue );
			this._refreshValue();
		},

		_constrainedValue: function( newValue ) {
			if ( newValue === undefined ) {
				newValue = this.options.value;
			}

			this.indeterminate = newValue === false;

			// sanitize value
			if ( typeof newValue !== "number" ) {
				newValue = 0;
			}

			return this.indeterminate ? false :
				Math.min( this.options.max, Math.max( this.min, newValue ) );
		},

		_setOptions: function( options ) {
			// Ensure "value" option is set after other values (like max)
			var value = options.value;
			delete options.value;

			this._super( options );

			this.options.value = this._constrainedValue( value );
			this._refreshValue();
		},

		_setOption: function( key, value ) {
			if ( key === "max" ) {
				// Don't allow a max less than min
				value = Math.max( this.min, value );
			}

			this._super( key, value );
		},

		_percentage: function() {
			return this.indeterminate ? 100 : 100 * ( this.options.value - this.min ) / ( this.options.max - this.min );
		},

		_refreshValue: function() {
			var value = this.options.value,
				percentage = this._percentage();

			this.valueDiv
				.toggle( this.indeterminate || value > this.min )
				.toggleClass( "ui-corner-right", value === this.options.max )
				.width( percentage.toFixed(0) + "%" );

			this.element.toggleClass( "ui-progressbar-indeterminate", this.indeterminate );

			if ( this.indeterminate ) {
				this.element.removeAttr( "aria-valuenow" );
				if ( !this.overlayDiv ) {
					this.overlayDiv = $( "<div class='ui-progressbar-overlay'></div>" ).appendTo( this.valueDiv );
				}
			} else {
				this.element.attr({
					"aria-valuemax": this.options.max,
					"aria-valuenow": value
				});
				if ( this.overlayDiv ) {
					this.overlayDiv.remove();
					this.overlayDiv = null;
				}
			}

			if ( this.oldValue !== value ) {
				this.oldValue = value;
				this._trigger( "change" );
			}
			if ( value === this.options.max ) {
				this._trigger( "complete" );
			}
		}
	});

	})( jQuery );

	(function( $, undefined ) {

	// number of pages in a slider
	// (how many times can you page up/down to go through the whole range)
	var numPages = 5;

	$.widget( "ui.slider", $.ui.mouse, {
		version: "1.10.3",
		widgetEventPrefix: "slide",

		options: {
			animate: false,
			distance: 0,
			max: 100,
			min: 0,
			orientation: "horizontal",
			range: false,
			step: 1,
			value: 0,
			values: null,

			// callbacks
			change: null,
			slide: null,
			start: null,
			stop: null
		},

		_create: function() {
			this._keySliding = false;
			this._mouseSliding = false;
			this._animateOff = true;
			this._handleIndex = null;
			this._detectOrientation();
			this._mouseInit();

			this.element
				.addClass( "ui-slider" +
					" ui-slider-" + this.orientation +
					" ui-widget" +
					" ui-widget-content" +
					" ui-corner-all");

			this._refresh();
			this._setOption( "disabled", this.options.disabled );

			this._animateOff = false;
		},

		_refresh: function() {
			this._createRange();
			this._createHandles();
			this._setupEvents();
			this._refreshValue();
		},

		_createHandles: function() {
			var i, handleCount,
				options = this.options,
				existingHandles = this.element.find( ".ui-slider-handle" ).addClass( "ui-state-default ui-corner-all" ),
				handle = "<a class='ui-slider-handle ui-state-default ui-corner-all' href='#'></a>",
				handles = [];

			handleCount = ( options.values && options.values.length ) || 1;

			if ( existingHandles.length > handleCount ) {
				existingHandles.slice( handleCount ).remove();
				existingHandles = existingHandles.slice( 0, handleCount );
			}

			for ( i = existingHandles.length; i < handleCount; i++ ) {
				handles.push( handle );
			}

			this.handles = existingHandles.add( $( handles.join( "" ) ).appendTo( this.element ) );

			this.handle = this.handles.eq( 0 );

			this.handles.each(function( i ) {
				$( this ).data( "ui-slider-handle-index", i );
			});
		},

		_createRange: function() {
			var options = this.options,
				classes = "";

			if ( options.range ) {
				if ( options.range === true ) {
					if ( !options.values ) {
						options.values = [ this._valueMin(), this._valueMin() ];
					} else if ( options.values.length && options.values.length !== 2 ) {
						options.values = [ options.values[0], options.values[0] ];
					} else if ( $.isArray( options.values ) ) {
						options.values = options.values.slice(0);
					}
				}

				if ( !this.range || !this.range.length ) {
					this.range = $( "<div></div>" )
						.appendTo( this.element );

					classes = "ui-slider-range" +
					// note: this isn't the most fittingly semantic framework class for this element,
					// but worked best visually with a variety of themes
					" ui-widget-header ui-corner-all";
				} else {
					this.range.removeClass( "ui-slider-range-min ui-slider-range-max" )
						// Handle range switching from true to min/max
						.css({
							"left": "",
							"bottom": ""
						});
				}

				this.range.addClass( classes +
					( ( options.range === "min" || options.range === "max" ) ? " ui-slider-range-" + options.range : "" ) );
			} else {
				this.range = $([]);
			}
		},

		_setupEvents: function() {
			var elements = this.handles.add( this.range ).filter( "a" );
			this._off( elements );
			this._on( elements, this._handleEvents );
			this._hoverable( elements );
			this._focusable( elements );
		},

		_destroy: function() {
			this.handles.remove();
			this.range.remove();

			this.element
				.removeClass( "ui-slider" +
					" ui-slider-horizontal" +
					" ui-slider-vertical" +
					" ui-widget" +
					" ui-widget-content" +
					" ui-corner-all" );

			this._mouseDestroy();
		},

		_mouseCapture: function( event ) {
			var position, normValue, distance, closestHandle, index, allowed, offset, mouseOverHandle,
				that = this,
				o = this.options;

			if ( o.disabled ) {
				return false;
			}

			this.elementSize = {
				width: this.element.outerWidth(),
				height: this.element.outerHeight()
			};
			this.elementOffset = this.element.offset();

			position = { x: event.pageX, y: event.pageY };
			normValue = this._normValueFromMouse( position );
			distance = this._valueMax() - this._valueMin() + 1;
			this.handles.each(function( i ) {
				var thisDistance = Math.abs( normValue - that.values(i) );
				if (( distance > thisDistance ) ||
					( distance === thisDistance &&
						(i === that._lastChangedValue || that.values(i) === o.min ))) {
					distance = thisDistance;
					closestHandle = $( this );
					index = i;
				}
			});

			allowed = this._start( event, index );
			if ( allowed === false ) {
				return false;
			}
			this._mouseSliding = true;

			this._handleIndex = index;

			closestHandle
				.addClass( "ui-state-active" )
				.focus();

			offset = closestHandle.offset();
			mouseOverHandle = !$( event.target ).parents().addBack().is( ".ui-slider-handle" );
			this._clickOffset = mouseOverHandle ? { left: 0, top: 0 } : {
				left: event.pageX - offset.left - ( closestHandle.width() / 2 ),
				top: event.pageY - offset.top -
					( closestHandle.height() / 2 ) -
					( parseInt( closestHandle.css("borderTopWidth"), 10 ) || 0 ) -
					( parseInt( closestHandle.css("borderBottomWidth"), 10 ) || 0) +
					( parseInt( closestHandle.css("marginTop"), 10 ) || 0)
			};

			if ( !this.handles.hasClass( "ui-state-hover" ) ) {
				this._slide( event, index, normValue );
			}
			this._animateOff = true;
			return true;
		},

		_mouseStart: function() {
			return true;
		},

		_mouseDrag: function( event ) {
			var position = { x: event.pageX, y: event.pageY },
				normValue = this._normValueFromMouse( position );

			this._slide( event, this._handleIndex, normValue );

			return false;
		},

		_mouseStop: function( event ) {
			this.handles.removeClass( "ui-state-active" );
			this._mouseSliding = false;

			this._stop( event, this._handleIndex );
			this._change( event, this._handleIndex );

			this._handleIndex = null;
			this._clickOffset = null;
			this._animateOff = false;

			return false;
		},

		_detectOrientation: function() {
			this.orientation = ( this.options.orientation === "vertical" ) ? "vertical" : "horizontal";
		},

		_normValueFromMouse: function( position ) {
			var pixelTotal,
				pixelMouse,
				percentMouse,
				valueTotal,
				valueMouse;

			if ( this.orientation === "horizontal" ) {
				pixelTotal = this.elementSize.width;
				pixelMouse = position.x - this.elementOffset.left - ( this._clickOffset ? this._clickOffset.left : 0 );
			} else {
				pixelTotal = this.elementSize.height;
				pixelMouse = position.y - this.elementOffset.top - ( this._clickOffset ? this._clickOffset.top : 0 );
			}

			percentMouse = ( pixelMouse / pixelTotal );
			if ( percentMouse > 1 ) {
				percentMouse = 1;
			}
			if ( percentMouse < 0 ) {
				percentMouse = 0;
			}
			if ( this.orientation === "vertical" ) {
				percentMouse = 1 - percentMouse;
			}

			valueTotal = this._valueMax() - this._valueMin();
			valueMouse = this._valueMin() + percentMouse * valueTotal;

			return this._trimAlignValue( valueMouse );
		},

		_start: function( event, index ) {
			var uiHash = {
				handle: this.handles[ index ],
				value: this.value()
			};
			if ( this.options.values && this.options.values.length ) {
				uiHash.value = this.values( index );
				uiHash.values = this.values();
			}
			return this._trigger( "start", event, uiHash );
		},

		_slide: function( event, index, newVal ) {
			var otherVal,
				newValues,
				allowed;

			if ( this.options.values && this.options.values.length ) {
				otherVal = this.values( index ? 0 : 1 );

				if ( ( this.options.values.length === 2 && this.options.range === true ) &&
						( ( index === 0 && newVal > otherVal) || ( index === 1 && newVal < otherVal ) )
					) {
					newVal = otherVal;
				}

				if ( newVal !== this.values( index ) ) {
					newValues = this.values();
					newValues[ index ] = newVal;
					// A slide can be canceled by returning false from the slide callback
					allowed = this._trigger( "slide", event, {
						handle: this.handles[ index ],
						value: newVal,
						values: newValues
					} );
					otherVal = this.values( index ? 0 : 1 );
					if ( allowed !== false ) {
						this.values( index, newVal, true );
					}
				}
			} else {
				if ( newVal !== this.value() ) {
					// A slide can be canceled by returning false from the slide callback
					allowed = this._trigger( "slide", event, {
						handle: this.handles[ index ],
						value: newVal
					} );
					if ( allowed !== false ) {
						this.value( newVal );
					}
				}
			}
		},

		_stop: function( event, index ) {
			var uiHash = {
				handle: this.handles[ index ],
				value: this.value()
			};
			if ( this.options.values && this.options.values.length ) {
				uiHash.value = this.values( index );
				uiHash.values = this.values();
			}

			this._trigger( "stop", event, uiHash );
		},

		_change: function( event, index ) {
			if ( !this._keySliding && !this._mouseSliding ) {
				var uiHash = {
					handle: this.handles[ index ],
					value: this.value()
				};
				if ( this.options.values && this.options.values.length ) {
					uiHash.value = this.values( index );
					uiHash.values = this.values();
				}

				//store the last changed value index for reference when handles overlap
				this._lastChangedValue = index;

				this._trigger( "change", event, uiHash );
			}
		},

		value: function( newValue ) {
			if ( arguments.length ) {
				this.options.value = this._trimAlignValue( newValue );
				this._refreshValue();
				this._change( null, 0 );
				return;
			}

			return this._value();
		},

		values: function( index, newValue ) {
			var vals,
				newValues,
				i;

			if ( arguments.length > 1 ) {
				this.options.values[ index ] = this._trimAlignValue( newValue );
				this._refreshValue();
				this._change( null, index );
				return;
			}

			if ( arguments.length ) {
				if ( $.isArray( arguments[ 0 ] ) ) {
					vals = this.options.values;
					newValues = arguments[ 0 ];
					for ( i = 0; i < vals.length; i += 1 ) {
						vals[ i ] = this._trimAlignValue( newValues[ i ] );
						this._change( null, i );
					}
					this._refreshValue();
				} else {
					if ( this.options.values && this.options.values.length ) {
						return this._values( index );
					} else {
						return this.value();
					}
				}
			} else {
				return this._values();
			}
		},

		_setOption: function( key, value ) {
			var i,
				valsLength = 0;

			if ( key === "range" && this.options.range === true ) {
				if ( value === "min" ) {
					this.options.value = this._values( 0 );
					this.options.values = null;
				} else if ( value === "max" ) {
					this.options.value = this._values( this.options.values.length-1 );
					this.options.values = null;
				}
			}

			if ( $.isArray( this.options.values ) ) {
				valsLength = this.options.values.length;
			}

			$.Widget.prototype._setOption.apply( this, arguments );

			switch ( key ) {
				case "orientation":
					this._detectOrientation();
					this.element
						.removeClass( "ui-slider-horizontal ui-slider-vertical" )
						.addClass( "ui-slider-" + this.orientation );
					this._refreshValue();
					break;
				case "value":
					this._animateOff = true;
					this._refreshValue();
					this._change( null, 0 );
					this._animateOff = false;
					break;
				case "values":
					this._animateOff = true;
					this._refreshValue();
					for ( i = 0; i < valsLength; i += 1 ) {
						this._change( null, i );
					}
					this._animateOff = false;
					break;
				case "min":
				case "max":
					this._animateOff = true;
					this._refreshValue();
					this._animateOff = false;
					break;
				case "range":
					this._animateOff = true;
					this._refresh();
					this._animateOff = false;
					break;
			}
		},

		//internal value getter
		// _value() returns value trimmed by min and max, aligned by step
		_value: function() {
			var val = this.options.value;
			val = this._trimAlignValue( val );

			return val;
		},

		//internal values getter
		// _values() returns array of values trimmed by min and max, aligned by step
		// _values( index ) returns single value trimmed by min and max, aligned by step
		_values: function( index ) {
			var val,
				vals,
				i;

			if ( arguments.length ) {
				val = this.options.values[ index ];
				val = this._trimAlignValue( val );

				return val;
			} else if ( this.options.values && this.options.values.length ) {
				// .slice() creates a copy of the array
				// this copy gets trimmed by min and max and then returned
				vals = this.options.values.slice();
				for ( i = 0; i < vals.length; i+= 1) {
					vals[ i ] = this._trimAlignValue( vals[ i ] );
				}

				return vals;
			} else {
				return [];
			}
		},

		// returns the step-aligned value that val is closest to, between (inclusive) min and max
		_trimAlignValue: function( val ) {
			if ( val <= this._valueMin() ) {
				return this._valueMin();
			}
			if ( val >= this._valueMax() ) {
				return this._valueMax();
			}
			var step = ( this.options.step > 0 ) ? this.options.step : 1,
				valModStep = (val - this._valueMin()) % step,
				alignValue = val - valModStep;

			if ( Math.abs(valModStep) * 2 >= step ) {
				alignValue += ( valModStep > 0 ) ? step : ( -step );
			}

			// Since JavaScript has problems with large floats, round
			// the final value to 5 digits after the decimal point (see #4124)
			return parseFloat( alignValue.toFixed(5) );
		},

		_valueMin: function() {
			return this.options.min;
		},

		_valueMax: function() {
			return this.options.max;
		},

		_refreshValue: function() {
			var lastValPercent, valPercent, value, valueMin, valueMax,
				oRange = this.options.range,
				o = this.options,
				that = this,
				animate = ( !this._animateOff ) ? o.animate : false,
				_set = {};

			if ( this.options.values && this.options.values.length ) {
				this.handles.each(function( i ) {
					valPercent = ( that.values(i) - that._valueMin() ) / ( that._valueMax() - that._valueMin() ) * 100;
					_set[ that.orientation === "horizontal" ? "left" : "bottom" ] = valPercent + "%";
					$( this ).stop( 1, 1 )[ animate ? "animate" : "css" ]( _set, o.animate );
					if ( that.options.range === true ) {
						if ( that.orientation === "horizontal" ) {
							if ( i === 0 ) {
								that.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { left: valPercent + "%" }, o.animate );
							}
							if ( i === 1 ) {
								that.range[ animate ? "animate" : "css" ]( { width: ( valPercent - lastValPercent ) + "%" }, { queue: false, duration: o.animate } );
							}
						} else {
							if ( i === 0 ) {
								that.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { bottom: ( valPercent ) + "%" }, o.animate );
							}
							if ( i === 1 ) {
								that.range[ animate ? "animate" : "css" ]( { height: ( valPercent - lastValPercent ) + "%" }, { queue: false, duration: o.animate } );
							}
						}
					}
					lastValPercent = valPercent;
				});
			} else {
				value = this.value();
				valueMin = this._valueMin();
				valueMax = this._valueMax();
				valPercent = ( valueMax !== valueMin ) ?
						( value - valueMin ) / ( valueMax - valueMin ) * 100 :
						0;
				_set[ this.orientation === "horizontal" ? "left" : "bottom" ] = valPercent + "%";
				this.handle.stop( 1, 1 )[ animate ? "animate" : "css" ]( _set, o.animate );

				if ( oRange === "min" && this.orientation === "horizontal" ) {
					this.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { width: valPercent + "%" }, o.animate );
				}
				if ( oRange === "max" && this.orientation === "horizontal" ) {
					this.range[ animate ? "animate" : "css" ]( { width: ( 100 - valPercent ) + "%" }, { queue: false, duration: o.animate } );
				}
				if ( oRange === "min" && this.orientation === "vertical" ) {
					this.range.stop( 1, 1 )[ animate ? "animate" : "css" ]( { height: valPercent + "%" }, o.animate );
				}
				if ( oRange === "max" && this.orientation === "vertical" ) {
					this.range[ animate ? "animate" : "css" ]( { height: ( 100 - valPercent ) + "%" }, { queue: false, duration: o.animate } );
				}
			}
		},

		_handleEvents: {
			keydown: function( event ) {
				/*jshint maxcomplexity:25*/
				var allowed, curVal, newVal, step,
					index = $( event.target ).data( "ui-slider-handle-index" );

				switch ( event.keyCode ) {
					case $.ui.keyCode.HOME:
					case $.ui.keyCode.END:
					case $.ui.keyCode.PAGE_UP:
					case $.ui.keyCode.PAGE_DOWN:
					case $.ui.keyCode.UP:
					case $.ui.keyCode.RIGHT:
					case $.ui.keyCode.DOWN:
					case $.ui.keyCode.LEFT:
						event.preventDefault();
						if ( !this._keySliding ) {
							this._keySliding = true;
							$( event.target ).addClass( "ui-state-active" );
							allowed = this._start( event, index );
							if ( allowed === false ) {
								return;
							}
						}
						break;
				}

				step = this.options.step;
				if ( this.options.values && this.options.values.length ) {
					curVal = newVal = this.values( index );
				} else {
					curVal = newVal = this.value();
				}

				switch ( event.keyCode ) {
					case $.ui.keyCode.HOME:
						newVal = this._valueMin();
						break;
					case $.ui.keyCode.END:
						newVal = this._valueMax();
						break;
					case $.ui.keyCode.PAGE_UP:
						newVal = this._trimAlignValue( curVal + ( (this._valueMax() - this._valueMin()) / numPages ) );
						break;
					case $.ui.keyCode.PAGE_DOWN:
						newVal = this._trimAlignValue( curVal - ( (this._valueMax() - this._valueMin()) / numPages ) );
						break;
					case $.ui.keyCode.UP:
					case $.ui.keyCode.RIGHT:
						if ( curVal === this._valueMax() ) {
							return;
						}
						newVal = this._trimAlignValue( curVal + step );
						break;
					case $.ui.keyCode.DOWN:
					case $.ui.keyCode.LEFT:
						if ( curVal === this._valueMin() ) {
							return;
						}
						newVal = this._trimAlignValue( curVal - step );
						break;
				}

				this._slide( event, index, newVal );
			},
			click: function( event ) {
				event.preventDefault();
			},
			keyup: function( event ) {
				var index = $( event.target ).data( "ui-slider-handle-index" );

				if ( this._keySliding ) {
					this._keySliding = false;
					this._stop( event, index );
					this._change( event, index );
					$( event.target ).removeClass( "ui-state-active" );
				}
			}
		}

	});

	}(jQuery));

	(function( $ ) {

	function modifier( fn ) {
		return function() {
			var previous = this.element.val();
			fn.apply( this, arguments );
			this._refresh();
			if ( previous !== this.element.val() ) {
				this._trigger( "change" );
			}
		};
	}

	$.widget( "ui.spinner", {
		version: "1.10.3",
		defaultElement: "<input>",
		widgetEventPrefix: "spin",
		options: {
			culture: null,
			icons: {
				down: "ui-icon-triangle-1-s",
				up: "ui-icon-triangle-1-n"
			},
			incremental: true,
			max: null,
			min: null,
			numberFormat: null,
			page: 10,
			step: 1,

			change: null,
			spin: null,
			start: null,
			stop: null
		},

		_create: function() {
			// handle string values that need to be parsed
			this._setOption( "max", this.options.max );
			this._setOption( "min", this.options.min );
			this._setOption( "step", this.options.step );

			// format the value, but don't constrain
			this._value( this.element.val(), true );

			this._draw();
			this._on( this._events );
			this._refresh();

			// turning off autocomplete prevents the browser from remembering the
			// value when navigating through history, so we re-enable autocomplete
			// if the page is unloaded before the widget is destroyed. #7790
			this._on( this.window, {
				beforeunload: function() {
					this.element.removeAttr( "autocomplete" );
				}
			});
		},

		_getCreateOptions: function() {
			var options = {},
				element = this.element;

			$.each( [ "min", "max", "step" ], function( i, option ) {
				var value = element.attr( option );
				if ( value !== undefined && value.length ) {
					options[ option ] = value;
				}
			});

			return options;
		},

		_events: {
			keydown: function( event ) {
				if ( this._start( event ) && this._keydown( event ) ) {
					event.preventDefault();
				}
			},
			keyup: "_stop",
			focus: function() {
				this.previous = this.element.val();
			},
			blur: function( event ) {
				if ( this.cancelBlur ) {
					delete this.cancelBlur;
					return;
				}

				this._stop();
				this._refresh();
				if ( this.previous !== this.element.val() ) {
					this._trigger( "change", event );
				}
			},
			mousewheel: function( event, delta ) {
				if ( !delta ) {
					return;
				}
				if ( !this.spinning && !this._start( event ) ) {
					return false;
				}

				this._spin( (delta > 0 ? 1 : -1) * this.options.step, event );
				clearTimeout( this.mousewheelTimer );
				this.mousewheelTimer = this._delay(function() {
					if ( this.spinning ) {
						this._stop( event );
					}
				}, 100 );
				event.preventDefault();
			},
			"mousedown .ui-spinner-button": function( event ) {
				var previous;

				// We never want the buttons to have focus; whenever the user is
				// interacting with the spinner, the focus should be on the input.
				// If the input is focused then this.previous is properly set from
				// when the input first received focus. If the input is not focused
				// then we need to set this.previous based on the value before spinning.
				previous = this.element[0] === this.document[0].activeElement ?
					this.previous : this.element.val();
				function checkFocus() {
					var isActive = this.element[0] === this.document[0].activeElement;
					if ( !isActive ) {
						this.element.focus();
						this.previous = previous;
						// support: IE
						// IE sets focus asynchronously, so we need to check if focus
						// moved off of the input because the user clicked on the button.
						this._delay(function() {
							this.previous = previous;
						});
					}
				}

				// ensure focus is on (or stays on) the text field
				event.preventDefault();
				checkFocus.call( this );

				// support: IE
				// IE doesn't prevent moving focus even with event.preventDefault()
				// so we set a flag to know when we should ignore the blur event
				// and check (again) if focus moved off of the input.
				this.cancelBlur = true;
				this._delay(function() {
					delete this.cancelBlur;
					checkFocus.call( this );
				});

				if ( this._start( event ) === false ) {
					return;
				}

				this._repeat( null, $( event.currentTarget ).hasClass( "ui-spinner-up" ) ? 1 : -1, event );
			},
			"mouseup .ui-spinner-button": "_stop",
			"mouseenter .ui-spinner-button": function( event ) {
				// button will add ui-state-active if mouse was down while mouseleave and kept down
				if ( !$( event.currentTarget ).hasClass( "ui-state-active" ) ) {
					return;
				}

				if ( this._start( event ) === false ) {
					return false;
				}
				this._repeat( null, $( event.currentTarget ).hasClass( "ui-spinner-up" ) ? 1 : -1, event );
			},
			// TODO: do we really want to consider this a stop?
			// shouldn't we just stop the repeater and wait until mouseup before
			// we trigger the stop event?
			"mouseleave .ui-spinner-button": "_stop"
		},

		_draw: function() {
			var uiSpinner = this.uiSpinner = this.element
				.addClass( "ui-spinner-input" )
				.attr( "autocomplete", "off" )
				.wrap( this._uiSpinnerHtml() )
				.parent()
					// add buttons
					.append( this._buttonHtml() );

			this.element.attr( "role", "spinbutton" );

			// button bindings
			this.buttons = uiSpinner.find( ".ui-spinner-button" )
				.attr( "tabIndex", -1 )
				.button()
				.removeClass( "ui-corner-all" );

			// IE 6 doesn't understand height: 50% for the buttons
			// unless the wrapper has an explicit height
			if ( this.buttons.height() > Math.ceil( uiSpinner.height() * 0.5 ) &&
					uiSpinner.height() > 0 ) {
				uiSpinner.height( uiSpinner.height() );
			}

			// disable spinner if element was already disabled
			if ( this.options.disabled ) {
				this.disable();
			}
		},

		_keydown: function( event ) {
			var options = this.options,
				keyCode = $.ui.keyCode;

			switch ( event.keyCode ) {
			case keyCode.UP:
				this._repeat( null, 1, event );
				return true;
			case keyCode.DOWN:
				this._repeat( null, -1, event );
				return true;
			case keyCode.PAGE_UP:
				this._repeat( null, options.page, event );
				return true;
			case keyCode.PAGE_DOWN:
				this._repeat( null, -options.page, event );
				return true;
			}

			return false;
		},

		_uiSpinnerHtml: function() {
			return "<span class='ui-spinner ui-widget ui-widget-content ui-corner-all'></span>";
		},

		_buttonHtml: function() {
			return "" +
				"<a class='ui-spinner-button ui-spinner-up ui-corner-tr'>" +
					"<span class='ui-icon " + this.options.icons.up + "'>&#9650;</span>" +
				"</a>" +
				"<a class='ui-spinner-button ui-spinner-down ui-corner-br'>" +
					"<span class='ui-icon " + this.options.icons.down + "'>&#9660;</span>" +
				"</a>";
		},

		_start: function( event ) {
			if ( !this.spinning && this._trigger( "start", event ) === false ) {
				return false;
			}

			if ( !this.counter ) {
				this.counter = 1;
			}
			this.spinning = true;
			return true;
		},

		_repeat: function( i, steps, event ) {
			i = i || 500;

			clearTimeout( this.timer );
			this.timer = this._delay(function() {
				this._repeat( 40, steps, event );
			}, i );

			this._spin( steps * this.options.step, event );
		},

		_spin: function( step, event ) {
			var value = this.value() || 0;

			if ( !this.counter ) {
				this.counter = 1;
			}

			value = this._adjustValue( value + step * this._increment( this.counter ) );

			if ( !this.spinning || this._trigger( "spin", event, { value: value } ) !== false) {
				this._value( value );
				this.counter++;
			}
		},

		_increment: function( i ) {
			var incremental = this.options.incremental;

			if ( incremental ) {
				return $.isFunction( incremental ) ?
					incremental( i ) :
					Math.floor( i*i*i/50000 - i*i/500 + 17*i/200 + 1 );
			}

			return 1;
		},

		_precision: function() {
			var precision = this._precisionOf( this.options.step );
			if ( this.options.min !== null ) {
				precision = Math.max( precision, this._precisionOf( this.options.min ) );
			}
			return precision;
		},

		_precisionOf: function( num ) {
			var str = num.toString(),
				decimal = str.indexOf( "." );
			return decimal === -1 ? 0 : str.length - decimal - 1;
		},

		_adjustValue: function( value ) {
			var base, aboveMin,
				options = this.options;

			// make sure we're at a valid step
			// - find out where we are relative to the base (min or 0)
			base = options.min !== null ? options.min : 0;
			aboveMin = value - base;
			// - round to the nearest step
			aboveMin = Math.round(aboveMin / options.step) * options.step;
			// - rounding is based on 0, so adjust back to our base
			value = base + aboveMin;

			// fix precision from bad JS floating point math
			value = parseFloat( value.toFixed( this._precision() ) );

			// clamp the value
			if ( options.max !== null && value > options.max) {
				return options.max;
			}
			if ( options.min !== null && value < options.min ) {
				return options.min;
			}

			return value;
		},

		_stop: function( event ) {
			if ( !this.spinning ) {
				return;
			}

			clearTimeout( this.timer );
			clearTimeout( this.mousewheelTimer );
			this.counter = 0;
			this.spinning = false;
			this._trigger( "stop", event );
		},

		_setOption: function( key, value ) {
			if ( key === "culture" || key === "numberFormat" ) {
				var prevValue = this._parse( this.element.val() );
				this.options[ key ] = value;
				this.element.val( this._format( prevValue ) );
				return;
			}

			if ( key === "max" || key === "min" || key === "step" ) {
				if ( typeof value === "string" ) {
					value = this._parse( value );
				}
			}
			if ( key === "icons" ) {
				this.buttons.first().find( ".ui-icon" )
					.removeClass( this.options.icons.up )
					.addClass( value.up );
				this.buttons.last().find( ".ui-icon" )
					.removeClass( this.options.icons.down )
					.addClass( value.down );
			}

			this._super( key, value );

			if ( key === "disabled" ) {
				if ( value ) {
					this.element.prop( "disabled", true );
					this.buttons.button( "disable" );
				} else {
					this.element.prop( "disabled", false );
					this.buttons.button( "enable" );
				}
			}
		},

		_setOptions: modifier(function( options ) {
			this._super( options );
			this._value( this.element.val() );
		}),

		_parse: function( val ) {
			if ( typeof val === "string" && val !== "" ) {
				val = window.Globalize && this.options.numberFormat ?
					Globalize.parseFloat( val, 10, this.options.culture ) : +val;
			}
			return val === "" || isNaN( val ) ? null : val;
		},

		_format: function( value ) {
			if ( value === "" ) {
				return "";
			}
			return window.Globalize && this.options.numberFormat ?
				Globalize.format( value, this.options.numberFormat, this.options.culture ) :
				value;
		},

		_refresh: function() {
			this.element.attr({
				"aria-valuemin": this.options.min,
				"aria-valuemax": this.options.max,
				// TODO: what should we do with values that can't be parsed?
				"aria-valuenow": this._parse( this.element.val() )
			});
		},

		// update the value without triggering change
		_value: function( value, allowAny ) {
			var parsed;
			if ( value !== "" ) {
				parsed = this._parse( value );
				if ( parsed !== null ) {
					if ( !allowAny ) {
						parsed = this._adjustValue( parsed );
					}
					value = this._format( parsed );
				}
			}
			this.element.val( value );
			this._refresh();
		},

		_destroy: function() {
			this.element
				.removeClass( "ui-spinner-input" )
				.prop( "disabled", false )
				.removeAttr( "autocomplete" )
				.removeAttr( "role" )
				.removeAttr( "aria-valuemin" )
				.removeAttr( "aria-valuemax" )
				.removeAttr( "aria-valuenow" );
			this.uiSpinner.replaceWith( this.element );
		},

		stepUp: modifier(function( steps ) {
			this._stepUp( steps );
		}),
		_stepUp: function( steps ) {
			if ( this._start() ) {
				this._spin( (steps || 1) * this.options.step );
				this._stop();
			}
		},

		stepDown: modifier(function( steps ) {
			this._stepDown( steps );
		}),
		_stepDown: function( steps ) {
			if ( this._start() ) {
				this._spin( (steps || 1) * -this.options.step );
				this._stop();
			}
		},

		pageUp: modifier(function( pages ) {
			this._stepUp( (pages || 1) * this.options.page );
		}),

		pageDown: modifier(function( pages ) {
			this._stepDown( (pages || 1) * this.options.page );
		}),

		value: function( newVal ) {
			if ( !arguments.length ) {
				return this._parse( this.element.val() );
			}
			modifier( this._value ).call( this, newVal );
		},

		widget: function() {
			return this.uiSpinner;
		}
	});

	}( jQuery ) );

	(function( $, undefined ) {

	var tabId = 0,
		rhash = /#.*$/;

	function getNextTabId() {
		return ++tabId;
	}

	function isLocal( anchor ) {
		return anchor.hash.length > 1 &&
			decodeURIComponent( anchor.href.replace( rhash, "" ) ) ===
				decodeURIComponent( location.href.replace( rhash, "" ) );
	}

	$.widget( "ui.tabs", {
		version: "1.10.3",
		delay: 300,
		options: {
			active: null,
			collapsible: false,
			event: "click",
			heightStyle: "content",
			hide: null,
			show: null,

			// callbacks
			activate: null,
			beforeActivate: null,
			beforeLoad: null,
			load: null
		},

		_create: function() {
			var that = this,
				options = this.options;

			this.running = false;

			this.element
				.addClass( "ui-tabs ui-widget ui-widget-content ui-corner-all" )
				.toggleClass( "ui-tabs-collapsible", options.collapsible )
				// Prevent users from focusing disabled tabs via click
				.delegate( ".ui-tabs-nav > li", "mousedown" + this.eventNamespace, function( event ) {
					if ( $( this ).is( ".ui-state-disabled" ) ) {
						event.preventDefault();
					}
				})
				// support: IE <9
				// Preventing the default action in mousedown doesn't prevent IE
				// from focusing the element, so if the anchor gets focused, blur.
				// We don't have to worry about focusing the previously focused
				// element since clicking on a non-focusable element should focus
				// the body anyway.
				.delegate( ".ui-tabs-anchor", "focus" + this.eventNamespace, function() {
					if ( $( this ).closest( "li" ).is( ".ui-state-disabled" ) ) {
						this.blur();
					}
				});

			this._processTabs();
			options.active = this._initialActive();

			// Take disabling tabs via class attribute from HTML
			// into account and update option properly.
			if ( $.isArray( options.disabled ) ) {
				options.disabled = $.unique( options.disabled.concat(
					$.map( this.tabs.filter( ".ui-state-disabled" ), function( li ) {
						return that.tabs.index( li );
					})
				) ).sort();
			}

			// check for length avoids error when initializing empty list
			if ( this.options.active !== false && this.anchors.length ) {
				this.active = this._findActive( options.active );
			} else {
				this.active = $();
			}

			this._refresh();

			if ( this.active.length ) {
				this.load( options.active );
			}
		},

		_initialActive: function() {
			var active = this.options.active,
				collapsible = this.options.collapsible,
				locationHash = location.hash.substring( 1 );

			if ( active === null ) {
				// check the fragment identifier in the URL
				if ( locationHash ) {
					this.tabs.each(function( i, tab ) {
						if ( $( tab ).attr( "aria-controls" ) === locationHash ) {
							active = i;
							return false;
						}
					});
				}

				// check for a tab marked active via a class
				if ( active === null ) {
					active = this.tabs.index( this.tabs.filter( ".ui-tabs-active" ) );
				}

				// no active tab, set to false
				if ( active === null || active === -1 ) {
					active = this.tabs.length ? 0 : false;
				}
			}

			// handle numbers: negative, out of range
			if ( active !== false ) {
				active = this.tabs.index( this.tabs.eq( active ) );
				if ( active === -1 ) {
					active = collapsible ? false : 0;
				}
			}

			// don't allow collapsible: false and active: false
			if ( !collapsible && active === false && this.anchors.length ) {
				active = 0;
			}

			return active;
		},

		_getCreateEventData: function() {
			return {
				tab: this.active,
				panel: !this.active.length ? $() : this._getPanelForTab( this.active )
			};
		},

		_tabKeydown: function( event ) {
			/*jshint maxcomplexity:15*/
			var focusedTab = $( this.document[0].activeElement ).closest( "li" ),
				selectedIndex = this.tabs.index( focusedTab ),
				goingForward = true;

			if ( this._handlePageNav( event ) ) {
				return;
			}

			switch ( event.keyCode ) {
				case $.ui.keyCode.RIGHT:
				case $.ui.keyCode.DOWN:
					selectedIndex++;
					break;
				case $.ui.keyCode.UP:
				case $.ui.keyCode.LEFT:
					goingForward = false;
					selectedIndex--;
					break;
				case $.ui.keyCode.END:
					selectedIndex = this.anchors.length - 1;
					break;
				case $.ui.keyCode.HOME:
					selectedIndex = 0;
					break;
				case $.ui.keyCode.SPACE:
					// Activate only, no collapsing
					event.preventDefault();
					clearTimeout( this.activating );
					this._activate( selectedIndex );
					return;
				case $.ui.keyCode.ENTER:
					// Toggle (cancel delayed activation, allow collapsing)
					event.preventDefault();
					clearTimeout( this.activating );
					// Determine if we should collapse or activate
					this._activate( selectedIndex === this.options.active ? false : selectedIndex );
					return;
				default:
					return;
			}

			// Focus the appropriate tab, based on which key was pressed
			event.preventDefault();
			clearTimeout( this.activating );
			selectedIndex = this._focusNextTab( selectedIndex, goingForward );

			// Navigating with control key will prevent automatic activation
			if ( !event.ctrlKey ) {
				// Update aria-selected immediately so that AT think the tab is already selected.
				// Otherwise AT may confuse the user by stating that they need to activate the tab,
				// but the tab will already be activated by the time the announcement finishes.
				focusedTab.attr( "aria-selected", "false" );
				this.tabs.eq( selectedIndex ).attr( "aria-selected", "true" );

				this.activating = this._delay(function() {
					this.option( "active", selectedIndex );
				}, this.delay );
			}
		},

		_panelKeydown: function( event ) {
			if ( this._handlePageNav( event ) ) {
				return;
			}

			// Ctrl+up moves focus to the current tab
			if ( event.ctrlKey && event.keyCode === $.ui.keyCode.UP ) {
				event.preventDefault();
				this.active.focus();
			}
		},

		// Alt+page up/down moves focus to the previous/next tab (and activates)
		_handlePageNav: function( event ) {
			if ( event.altKey && event.keyCode === $.ui.keyCode.PAGE_UP ) {
				this._activate( this._focusNextTab( this.options.active - 1, false ) );
				return true;
			}
			if ( event.altKey && event.keyCode === $.ui.keyCode.PAGE_DOWN ) {
				this._activate( this._focusNextTab( this.options.active + 1, true ) );
				return true;
			}
		},

		_findNextTab: function( index, goingForward ) {
			var lastTabIndex = this.tabs.length - 1;

			function constrain() {
				if ( index > lastTabIndex ) {
					index = 0;
				}
				if ( index < 0 ) {
					index = lastTabIndex;
				}
				return index;
			}

			while ( $.inArray( constrain(), this.options.disabled ) !== -1 ) {
				index = goingForward ? index + 1 : index - 1;
			}

			return index;
		},

		_focusNextTab: function( index, goingForward ) {
			index = this._findNextTab( index, goingForward );
			this.tabs.eq( index ).focus();
			return index;
		},

		_setOption: function( key, value ) {
			if ( key === "active" ) {
				// _activate() will handle invalid values and update this.options
				this._activate( value );
				return;
			}

			if ( key === "disabled" ) {
				// don't use the widget factory's disabled handling
				this._setupDisabled( value );
				return;
			}

			this._super( key, value);

			if ( key === "collapsible" ) {
				this.element.toggleClass( "ui-tabs-collapsible", value );
				// Setting collapsible: false while collapsed; open first panel
				if ( !value && this.options.active === false ) {
					this._activate( 0 );
				}
			}

			if ( key === "event" ) {
				this._setupEvents( value );
			}

			if ( key === "heightStyle" ) {
				this._setupHeightStyle( value );
			}
		},

		_tabId: function( tab ) {
			return tab.attr( "aria-controls" ) || "ui-tabs-" + getNextTabId();
		},

		_sanitizeSelector: function( hash ) {
			return hash ? hash.replace( /[!"$%&'()*+,.\/:;<=>?@\[\]\^`{|}~]/g, "\\$&" ) : "";
		},

		refresh: function() {
			var options = this.options,
				lis = this.tablist.children( ":has(a[href])" );

			// get disabled tabs from class attribute from HTML
			// this will get converted to a boolean if needed in _refresh()
			options.disabled = $.map( lis.filter( ".ui-state-disabled" ), function( tab ) {
				return lis.index( tab );
			});

			this._processTabs();

			// was collapsed or no tabs
			if ( options.active === false || !this.anchors.length ) {
				options.active = false;
				this.active = $();
			// was active, but active tab is gone
			} else if ( this.active.length && !$.contains( this.tablist[ 0 ], this.active[ 0 ] ) ) {
				// all remaining tabs are disabled
				if ( this.tabs.length === options.disabled.length ) {
					options.active = false;
					this.active = $();
				// activate previous tab
				} else {
					this._activate( this._findNextTab( Math.max( 0, options.active - 1 ), false ) );
				}
			// was active, active tab still exists
			} else {
				// make sure active index is correct
				options.active = this.tabs.index( this.active );
			}

			this._refresh();
		},

		_refresh: function() {
			this._setupDisabled( this.options.disabled );
			this._setupEvents( this.options.event );
			this._setupHeightStyle( this.options.heightStyle );

			this.tabs.not( this.active ).attr({
				"aria-selected": "false",
				tabIndex: -1
			});
			this.panels.not( this._getPanelForTab( this.active ) )
				.hide()
				.attr({
					"aria-expanded": "false",
					"aria-hidden": "true"
				});

			// Make sure one tab is in the tab order
			if ( !this.active.length ) {
				this.tabs.eq( 0 ).attr( "tabIndex", 0 );
			} else {
				this.active
					.addClass( "ui-tabs-active ui-state-active" )
					.attr({
						"aria-selected": "true",
						tabIndex: 0
					});
				this._getPanelForTab( this.active )
					.show()
					.attr({
						"aria-expanded": "true",
						"aria-hidden": "false"
					});
			}
		},

		_processTabs: function() {
			var that = this;

			this.tablist = this._getList()
				.addClass( "ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" )
				.attr( "role", "tablist" );

			this.tabs = this.tablist.find( "> li:has(a[href])" )
				.addClass( "ui-state-default ui-corner-top" )
				.attr({
					role: "tab",
					tabIndex: -1
				});

			this.anchors = this.tabs.map(function() {
					return $( "a", this )[ 0 ];
				})
				.addClass( "ui-tabs-anchor" )
				.attr({
					role: "presentation",
					tabIndex: -1
				});

			this.panels = $();

			this.anchors.each(function( i, anchor ) {
				var selector, panel, panelId,
					anchorId = $( anchor ).uniqueId().attr( "id" ),
					tab = $( anchor ).closest( "li" ),
					originalAriaControls = tab.attr( "aria-controls" );

				// inline tab
				if ( isLocal( anchor ) ) {
					selector = anchor.hash;
					panel = that.element.find( that._sanitizeSelector( selector ) );
				// remote tab
				} else {
					panelId = that._tabId( tab );
					selector = "#" + panelId;
					panel = that.element.find( selector );
					if ( !panel.length ) {
						panel = that._createPanel( panelId );
						panel.insertAfter( that.panels[ i - 1 ] || that.tablist );
					}
					panel.attr( "aria-live", "polite" );
				}

				if ( panel.length) {
					that.panels = that.panels.add( panel );
				}
				if ( originalAriaControls ) {
					tab.data( "ui-tabs-aria-controls", originalAriaControls );
				}
				tab.attr({
					"aria-controls": selector.substring( 1 ),
					"aria-labelledby": anchorId
				});
				panel.attr( "aria-labelledby", anchorId );
			});

			this.panels
				.addClass( "ui-tabs-panel ui-widget-content ui-corner-bottom" )
				.attr( "role", "tabpanel" );
		},

		// allow overriding how to find the list for rare usage scenarios (#7715)
		_getList: function() {
			return this.element.find( "ol,ul" ).eq( 0 );
		},

		_createPanel: function( id ) {
			return $( "<div>" )
				.attr( "id", id )
				.addClass( "ui-tabs-panel ui-widget-content ui-corner-bottom" )
				.data( "ui-tabs-destroy", true );
		},

		_setupDisabled: function( disabled ) {
			if ( $.isArray( disabled ) ) {
				if ( !disabled.length ) {
					disabled = false;
				} else if ( disabled.length === this.anchors.length ) {
					disabled = true;
				}
			}

			// disable tabs
			for ( var i = 0, li; ( li = this.tabs[ i ] ); i++ ) {
				if ( disabled === true || $.inArray( i, disabled ) !== -1 ) {
					$( li )
						.addClass( "ui-state-disabled" )
						.attr( "aria-disabled", "true" );
				} else {
					$( li )
						.removeClass( "ui-state-disabled" )
						.removeAttr( "aria-disabled" );
				}
			}

			this.options.disabled = disabled;
		},

		_setupEvents: function( event ) {
			var events = {
				click: function( event ) {
					event.preventDefault();
				}
			};
			if ( event ) {
				$.each( event.split(" "), function( index, eventName ) {
					events[ eventName ] = "_eventHandler";
				});
			}

			this._off( this.anchors.add( this.tabs ).add( this.panels ) );
			this._on( this.anchors, events );
			this._on( this.tabs, { keydown: "_tabKeydown" } );
			this._on( this.panels, { keydown: "_panelKeydown" } );

			this._focusable( this.tabs );
			this._hoverable( this.tabs );
		},

		_setupHeightStyle: function( heightStyle ) {
			var maxHeight,
				parent = this.element.parent();

			if ( heightStyle === "fill" ) {
				maxHeight = parent.height();
				maxHeight -= this.element.outerHeight() - this.element.height();

				this.element.siblings( ":visible" ).each(function() {
					var elem = $( this ),
						position = elem.css( "position" );

					if ( position === "absolute" || position === "fixed" ) {
						return;
					}
					maxHeight -= elem.outerHeight( true );
				});

				this.element.children().not( this.panels ).each(function() {
					maxHeight -= $( this ).outerHeight( true );
				});

				this.panels.each(function() {
					$( this ).height( Math.max( 0, maxHeight -
						$( this ).innerHeight() + $( this ).height() ) );
				})
				.css( "overflow", "auto" );
			} else if ( heightStyle === "auto" ) {
				maxHeight = 0;
				this.panels.each(function() {
					maxHeight = Math.max( maxHeight, $( this ).height( "" ).height() );
				}).height( maxHeight );
			}
		},

		_eventHandler: function( event ) {
			var options = this.options,
				active = this.active,
				anchor = $( event.currentTarget ),
				tab = anchor.closest( "li" ),
				clickedIsActive = tab[ 0 ] === active[ 0 ],
				collapsing = clickedIsActive && options.collapsible,
				toShow = collapsing ? $() : this._getPanelForTab( tab ),
				toHide = !active.length ? $() : this._getPanelForTab( active ),
				eventData = {
					oldTab: active,
					oldPanel: toHide,
					newTab: collapsing ? $() : tab,
					newPanel: toShow
				};

			event.preventDefault();

			if ( tab.hasClass( "ui-state-disabled" ) ||
					// tab is already loading
					tab.hasClass( "ui-tabs-loading" ) ||
					// can't switch durning an animation
					this.running ||
					// click on active header, but not collapsible
					( clickedIsActive && !options.collapsible ) ||
					// allow canceling activation
					( this._trigger( "beforeActivate", event, eventData ) === false ) ) {
				return;
			}

			options.active = collapsing ? false : this.tabs.index( tab );

			this.active = clickedIsActive ? $() : tab;
			if ( this.xhr ) {
				this.xhr.abort();
			}

			if ( !toHide.length && !toShow.length ) {
				$.error( "jQuery UI Tabs: Mismatching fragment identifier." );
			}

			if ( toShow.length ) {
				this.load( this.tabs.index( tab ), event );
			}
			this._toggle( event, eventData );
		},

		// handles show/hide for selecting tabs
		_toggle: function( event, eventData ) {
			var that = this,
				toShow = eventData.newPanel,
				toHide = eventData.oldPanel;

			this.running = true;

			function complete() {
				that.running = false;
				that._trigger( "activate", event, eventData );
			}

			function show() {
				eventData.newTab.closest( "li" ).addClass( "ui-tabs-active ui-state-active" );

				if ( toShow.length && that.options.show ) {
					that._show( toShow, that.options.show, complete );
				} else {
					toShow.show();
					complete();
				}
			}

			// start out by hiding, then showing, then completing
			if ( toHide.length && this.options.hide ) {
				this._hide( toHide, this.options.hide, function() {
					eventData.oldTab.closest( "li" ).removeClass( "ui-tabs-active ui-state-active" );
					show();
				});
			} else {
				eventData.oldTab.closest( "li" ).removeClass( "ui-tabs-active ui-state-active" );
				toHide.hide();
				show();
			}

			toHide.attr({
				"aria-expanded": "false",
				"aria-hidden": "true"
			});
			eventData.oldTab.attr( "aria-selected", "false" );
			// If we're switching tabs, remove the old tab from the tab order.
			// If we're opening from collapsed state, remove the previous tab from the tab order.
			// If we're collapsing, then keep the collapsing tab in the tab order.
			if ( toShow.length && toHide.length ) {
				eventData.oldTab.attr( "tabIndex", -1 );
			} else if ( toShow.length ) {
				this.tabs.filter(function() {
					return $( this ).attr( "tabIndex" ) === 0;
				})
				.attr( "tabIndex", -1 );
			}

			toShow.attr({
				"aria-expanded": "true",
				"aria-hidden": "false"
			});
			eventData.newTab.attr({
				"aria-selected": "true",
				tabIndex: 0
			});
		},

		_activate: function( index ) {
			var anchor,
				active = this._findActive( index );

			// trying to activate the already active panel
			if ( active[ 0 ] === this.active[ 0 ] ) {
				return;
			}

			// trying to collapse, simulate a click on the current active header
			if ( !active.length ) {
				active = this.active;
			}

			anchor = active.find( ".ui-tabs-anchor" )[ 0 ];
			this._eventHandler({
				target: anchor,
				currentTarget: anchor,
				preventDefault: $.noop
			});
		},

		_findActive: function( index ) {
			return index === false ? $() : this.tabs.eq( index );
		},

		_getIndex: function( index ) {
			// meta-function to give users option to provide a href string instead of a numerical index.
			if ( typeof index === "string" ) {
				index = this.anchors.index( this.anchors.filter( "[href$='" + index + "']" ) );
			}

			return index;
		},

		_destroy: function() {
			if ( this.xhr ) {
				this.xhr.abort();
			}

			this.element.removeClass( "ui-tabs ui-widget ui-widget-content ui-corner-all ui-tabs-collapsible" );

			this.tablist
				.removeClass( "ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" )
				.removeAttr( "role" );

			this.anchors
				.removeClass( "ui-tabs-anchor" )
				.removeAttr( "role" )
				.removeAttr( "tabIndex" )
				.removeUniqueId();

			this.tabs.add( this.panels ).each(function() {
				if ( $.data( this, "ui-tabs-destroy" ) ) {
					$( this ).remove();
				} else {
					$( this )
						.removeClass( "ui-state-default ui-state-active ui-state-disabled " +
							"ui-corner-top ui-corner-bottom ui-widget-content ui-tabs-active ui-tabs-panel" )
						.removeAttr( "tabIndex" )
						.removeAttr( "aria-live" )
						.removeAttr( "aria-busy" )
						.removeAttr( "aria-selected" )
						.removeAttr( "aria-labelledby" )
						.removeAttr( "aria-hidden" )
						.removeAttr( "aria-expanded" )
						.removeAttr( "role" );
				}
			});

			this.tabs.each(function() {
				var li = $( this ),
					prev = li.data( "ui-tabs-aria-controls" );
				if ( prev ) {
					li
						.attr( "aria-controls", prev )
						.removeData( "ui-tabs-aria-controls" );
				} else {
					li.removeAttr( "aria-controls" );
				}
			});

			this.panels.show();

			if ( this.options.heightStyle !== "content" ) {
				this.panels.css( "height", "" );
			}
		},

		enable: function( index ) {
			var disabled = this.options.disabled;
			if ( disabled === false ) {
				return;
			}

			if ( index === undefined ) {
				disabled = false;
			} else {
				index = this._getIndex( index );
				if ( $.isArray( disabled ) ) {
					disabled = $.map( disabled, function( num ) {
						return num !== index ? num : null;
					});
				} else {
					disabled = $.map( this.tabs, function( li, num ) {
						return num !== index ? num : null;
					});
				}
			}
			this._setupDisabled( disabled );
		},

		disable: function( index ) {
			var disabled = this.options.disabled;
			if ( disabled === true ) {
				return;
			}

			if ( index === undefined ) {
				disabled = true;
			} else {
				index = this._getIndex( index );
				if ( $.inArray( index, disabled ) !== -1 ) {
					return;
				}
				if ( $.isArray( disabled ) ) {
					disabled = $.merge( [ index ], disabled ).sort();
				} else {
					disabled = [ index ];
				}
			}
			this._setupDisabled( disabled );
		},

		load: function( index, event ) {
			index = this._getIndex( index );
			var that = this,
				tab = this.tabs.eq( index ),
				anchor = tab.find( ".ui-tabs-anchor" ),
				panel = this._getPanelForTab( tab ),
				eventData = {
					tab: tab,
					panel: panel
				};

			// not remote
			if ( isLocal( anchor[ 0 ] ) ) {
				return;
			}

			this.xhr = $.ajax( this._ajaxSettings( anchor, event, eventData ) );

			// support: jQuery <1.8
			// jQuery <1.8 returns false if the request is canceled in beforeSend,
			// but as of 1.8, $.ajax() always returns a jqXHR object.
			if ( this.xhr && this.xhr.statusText !== "canceled" ) {
				tab.addClass( "ui-tabs-loading" );
				panel.attr( "aria-busy", "true" );

				this.xhr
					.success(function( response ) {
						// support: jQuery <1.8
						// http://bugs.jquery.com/ticket/11778
						setTimeout(function() {
							panel.html( response );
							that._trigger( "load", event, eventData );
						}, 1 );
					})
					.complete(function( jqXHR, status ) {
						// support: jQuery <1.8
						// http://bugs.jquery.com/ticket/11778
						setTimeout(function() {
							if ( status === "abort" ) {
								that.panels.stop( false, true );
							}

							tab.removeClass( "ui-tabs-loading" );
							panel.removeAttr( "aria-busy" );

							if ( jqXHR === that.xhr ) {
								delete that.xhr;
							}
						}, 1 );
					});
			}
		},

		_ajaxSettings: function( anchor, event, eventData ) {
			var that = this;
			return {
				url: anchor.attr( "href" ),
				beforeSend: function( jqXHR, settings ) {
					return that._trigger( "beforeLoad", event,
						$.extend( { jqXHR : jqXHR, ajaxSettings: settings }, eventData ) );
				}
			};
		},

		_getPanelForTab: function( tab ) {
			var id = $( tab ).attr( "aria-controls" );
			return this.element.find( this._sanitizeSelector( "#" + id ) );
		}
	});

	})( jQuery );

	(function( $ ) {

	var increments = 0;

	function addDescribedBy( elem, id ) {
		var describedby = (elem.attr( "aria-describedby" ) || "").split( /\s+/ );
		describedby.push( id );
		elem
			.data( "ui-tooltip-id", id )
			.attr( "aria-describedby", $.trim( describedby.join( " " ) ) );
	}

	function removeDescribedBy( elem ) {
		var id = elem.data( "ui-tooltip-id" ),
			describedby = (elem.attr( "aria-describedby" ) || "").split( /\s+/ ),
			index = $.inArray( id, describedby );
		if ( index !== -1 ) {
			describedby.splice( index, 1 );
		}

		elem.removeData( "ui-tooltip-id" );
		describedby = $.trim( describedby.join( " " ) );
		if ( describedby ) {
			elem.attr( "aria-describedby", describedby );
		} else {
			elem.removeAttr( "aria-describedby" );
		}
	}

	$.widget( "ui.tooltip", {
		version: "1.10.3",
		options: {
			content: function() {
				// support: IE<9, Opera in jQuery <1.7
				// .text() can't accept undefined, so coerce to a string
				var title = $( this ).attr( "title" ) || "";
				// Escape title, since we're going from an attribute to raw HTML
				return $( "<a>" ).text( title ).html();
			},
			hide: true,
			// Disabled elements have inconsistent behavior across browsers (#8661)
			items: "[title]:not([disabled])",
			position: {
				my: "left top+15",
				at: "left bottom",
				collision: "flipfit flip"
			},
			show: true,
			tooltipClass: null,
			track: false,

			// callbacks
			close: null,
			open: null
		},

		_create: function() {
			this._on({
				mouseover: "open",
				focusin: "open"
			});

			// IDs of generated tooltips, needed for destroy
			this.tooltips = {};
			// IDs of parent tooltips where we removed the title attribute
			this.parents = {};

			if ( this.options.disabled ) {
				this._disable();
			}
		},

		_setOption: function( key, value ) {
			var that = this;

			if ( key === "disabled" ) {
				this[ value ? "_disable" : "_enable" ]();
				this.options[ key ] = value;
				// disable element style changes
				return;
			}

			this._super( key, value );

			if ( key === "content" ) {
				$.each( this.tooltips, function( id, element ) {
					that._updateContent( element );
				});
			}
		},

		_disable: function() {
			var that = this;

			// close open tooltips
			$.each( this.tooltips, function( id, element ) {
				var event = $.Event( "blur" );
				event.target = event.currentTarget = element[0];
				that.close( event, true );
			});

			// remove title attributes to prevent native tooltips
			this.element.find( this.options.items ).addBack().each(function() {
				var element = $( this );
				if ( element.is( "[title]" ) ) {
					element
						.data( "ui-tooltip-title", element.attr( "title" ) )
						.attr( "title", "" );
				}
			});
		},

		_enable: function() {
			// restore title attributes
			this.element.find( this.options.items ).addBack().each(function() {
				var element = $( this );
				if ( element.data( "ui-tooltip-title" ) ) {
					element.attr( "title", element.data( "ui-tooltip-title" ) );
				}
			});
		},

		open: function( event ) {
			var that = this,
				target = $( event ? event.target : this.element )
					// we need closest here due to mouseover bubbling,
					// but always pointing at the same event target
					.closest( this.options.items );

			// No element to show a tooltip for or the tooltip is already open
			if ( !target.length || target.data( "ui-tooltip-id" ) ) {
				return;
			}

			if ( target.attr( "title" ) ) {
				target.data( "ui-tooltip-title", target.attr( "title" ) );
			}

			target.data( "ui-tooltip-open", true );

			// kill parent tooltips, custom or native, for hover
			if ( event && event.type === "mouseover" ) {
				target.parents().each(function() {
					var parent = $( this ),
						blurEvent;
					if ( parent.data( "ui-tooltip-open" ) ) {
						blurEvent = $.Event( "blur" );
						blurEvent.target = blurEvent.currentTarget = this;
						that.close( blurEvent, true );
					}
					if ( parent.attr( "title" ) ) {
						parent.uniqueId();
						that.parents[ this.id ] = {
							element: this,
							title: parent.attr( "title" )
						};
						parent.attr( "title", "" );
					}
				});
			}

			this._updateContent( target, event );
		},

		_updateContent: function( target, event ) {
			var content,
				contentOption = this.options.content,
				that = this,
				eventType = event ? event.type : null;

			if ( typeof contentOption === "string" ) {
				return this._open( event, target, contentOption );
			}

			content = contentOption.call( target[0], function( response ) {
				// ignore async response if tooltip was closed already
				if ( !target.data( "ui-tooltip-open" ) ) {
					return;
				}
				// IE may instantly serve a cached response for ajax requests
				// delay this call to _open so the other call to _open runs first
				that._delay(function() {
					// jQuery creates a special event for focusin when it doesn't
					// exist natively. To improve performance, the native event
					// object is reused and the type is changed. Therefore, we can't
					// rely on the type being correct after the event finished
					// bubbling, so we set it back to the previous value. (#8740)
					if ( event ) {
						event.type = eventType;
					}
					this._open( event, target, response );
				});
			});
			if ( content ) {
				this._open( event, target, content );
			}
		},

		_open: function( event, target, content ) {
			var tooltip, events, delayedShow,
				positionOption = $.extend( {}, this.options.position );

			if ( !content ) {
				return;
			}

			// Content can be updated multiple times. If the tooltip already
			// exists, then just update the content and bail.
			tooltip = this._find( target );
			if ( tooltip.length ) {
				tooltip.find( ".ui-tooltip-content" ).html( content );
				return;
			}

			// if we have a title, clear it to prevent the native tooltip
			// we have to check first to avoid defining a title if none exists
			// (we don't want to cause an element to start matching [title])
			//
			// We use removeAttr only for key events, to allow IE to export the correct
			// accessible attributes. For mouse events, set to empty string to avoid
			// native tooltip showing up (happens only when removing inside mouseover).
			if ( target.is( "[title]" ) ) {
				if ( event && event.type === "mouseover" ) {
					target.attr( "title", "" );
				} else {
					target.removeAttr( "title" );
				}
			}

			tooltip = this._tooltip( target );
			addDescribedBy( target, tooltip.attr( "id" ) );
			tooltip.find( ".ui-tooltip-content" ).html( content );

			function position( event ) {
				positionOption.of = event;
				if ( tooltip.is( ":hidden" ) ) {
					return;
				}
				tooltip.position( positionOption );
			}
			if ( this.options.track && event && /^mouse/.test( event.type ) ) {
				this._on( this.document, {
					mousemove: position
				});
				// trigger once to override element-relative positioning
				position( event );
			} else {
				tooltip.position( $.extend({
					of: target
				}, this.options.position ) );
			}

			tooltip.hide();

			this._show( tooltip, this.options.show );
			// Handle tracking tooltips that are shown with a delay (#8644). As soon
			// as the tooltip is visible, position the tooltip using the most recent
			// event.
			if ( this.options.show && this.options.show.delay ) {
				delayedShow = this.delayedShow = setInterval(function() {
					if ( tooltip.is( ":visible" ) ) {
						position( positionOption.of );
						clearInterval( delayedShow );
					}
				}, $.fx.interval );
			}

			this._trigger( "open", event, { tooltip: tooltip } );

			events = {
				keyup: function( event ) {
					if ( event.keyCode === $.ui.keyCode.ESCAPE ) {
						var fakeEvent = $.Event(event);
						fakeEvent.currentTarget = target[0];
						this.close( fakeEvent, true );
					}
				},
				remove: function() {
					this._removeTooltip( tooltip );
				}
			};
			if ( !event || event.type === "mouseover" ) {
				events.mouseleave = "close";
			}
			if ( !event || event.type === "focusin" ) {
				events.focusout = "close";
			}
			this._on( true, target, events );
		},

		close: function( event ) {
			var that = this,
				target = $( event ? event.currentTarget : this.element ),
				tooltip = this._find( target );

			// disabling closes the tooltip, so we need to track when we're closing
			// to avoid an infinite loop in case the tooltip becomes disabled on close
			if ( this.closing ) {
				return;
			}

			// Clear the interval for delayed tracking tooltips
			clearInterval( this.delayedShow );

			// only set title if we had one before (see comment in _open())
			if ( target.data( "ui-tooltip-title" ) ) {
				target.attr( "title", target.data( "ui-tooltip-title" ) );
			}

			removeDescribedBy( target );

			tooltip.stop( true );
			this._hide( tooltip, this.options.hide, function() {
				that._removeTooltip( $( this ) );
			});

			target.removeData( "ui-tooltip-open" );
			this._off( target, "mouseleave focusout keyup" );
			// Remove 'remove' binding only on delegated targets
			if ( target[0] !== this.element[0] ) {
				this._off( target, "remove" );
			}
			this._off( this.document, "mousemove" );

			if ( event && event.type === "mouseleave" ) {
				$.each( this.parents, function( id, parent ) {
					$( parent.element ).attr( "title", parent.title );
					delete that.parents[ id ];
				});
			}

			this.closing = true;
			this._trigger( "close", event, { tooltip: tooltip } );
			this.closing = false;
		},

		_tooltip: function( element ) {
			var id = "ui-tooltip-" + increments++,
				tooltip = $( "<div>" )
					.attr({
						id: id,
						role: "tooltip"
					})
					.addClass( "ui-tooltip ui-widget ui-corner-all ui-widget-content " +
						( this.options.tooltipClass || "" ) );
			$( "<div>" )
				.addClass( "ui-tooltip-content" )
				.appendTo( tooltip );
			tooltip.appendTo( this.document[0].body );
			this.tooltips[ id ] = element;
			return tooltip;
		},

		_find: function( target ) {
			var id = target.data( "ui-tooltip-id" );
			return id ? $( "#" + id ) : $();
		},

		_removeTooltip: function( tooltip ) {
			tooltip.remove();
			delete this.tooltips[ tooltip.attr( "id" ) ];
		},

		_destroy: function() {
			var that = this;

			// close open tooltips
			$.each( this.tooltips, function( id, element ) {
				// Delegate to close method to handle common cleanup
				var event = $.Event( "blur" );
				event.target = event.currentTarget = element[0];
				that.close( event, true );

				// Remove immediately; destroying an open tooltip doesn't use the
				// hide animation
				$( "#" + id ).remove();

				// Restore the title
				if ( element.data( "ui-tooltip-title" ) ) {
					element.attr( "title", element.data( "ui-tooltip-title" ) );
					element.removeData( "ui-tooltip-title" );
				}
			});
		}
	});

	}( jQuery ) );


/***/ },
/* 96 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	var _googleMaps = __webpack_require__(97);

	var _googleMaps2 = _interopRequireDefault(_googleMaps);

	var gmapImpl = {

	    originLatLng: [52.2051, 0.10834], // location of CUDL

	    google: {},
	    map: {},
	    infowindow: {},
	    geocoder: {},
	    marker: {},
	    markers: [],

	    initGMLoader: function initGMLoader() {
	        _googleMaps2['default'].KEY = 'AIzaSyBzPB8FfB31q742f0HcsPx6aycNlWKWAgE'; // cudl api key
	        _googleMaps2['default'].SENSOR = false;
	        _googleMaps2['default'].LANGUAGE = 'en';
	    },

	    init: function init(options) {
	        var _this = this;

	        this.el = options.el;
	        this.input = options.input;

	        this.initGMLoader();

	        _googleMaps2['default'].load(function (google) {
	            _this.google = google;

	            _this.geocoder = new google.maps.Geocoder();
	            _this.infowindow = new google.maps.InfoWindow();

	            var mapOptions = {
	                center: {
	                    lat: _this.originLatLng[0],
	                    lng: _this.originLatLng[1]
	                },
	                zoom: 1,
	                zoomControl: false,
	                disableDefaultUI: true,
	                streetViewControl: false
	            };

	            _this.map = new google.maps.Map(_this.el, mapOptions);

	            // add event hadnler
	            google.maps.event.addListener(_this.map, 'click', function (e) {

	                //
	                // automatically geocode the location where user clicks to city or country
	                // based on current zoom level
	                //

	                _this.geocoder.geocode({ 'latLng': e.latLng }, function (results, status) {
	                    if (status == google.maps.GeocoderStatus.OK) {
	                        if (results[1]) {

	                            var zoom = _this.map.getZoom();

	                            // clear markers
	                            _this.clearMarkers();

	                            // create new marker
	                            _this.map.setZoom(8);
	                            _this.map.setCenter(e.latLng);
	                            _this.marker = new google.maps.Marker({
	                                position: e.latLng,
	                                map: _this.map
	                            });
	                            _this.marker.setMap(_this.map);
	                            _this.markers.push(_this.marker);

	                            // set place
	                            var city = _this.getPlace(results[0].address_components, zoom);
	                            $(_this.input).val(city);
	                        } else {
	                            alert('Geocoder failed due to: ' + status);
	                        }
	                    }
	                });
	            });
	        });
	    },

	    getPlace: function getPlace(address_components, zoom) {
	        var city, country, place;
	        $.each(address_components, function (i, addr_comp) {
	            if (addr_comp.types[0] == 'locality') {
	                city = addr_comp.long_name;
	            } else if (addr_comp.types[0] == 'country') {
	                country = addr_comp.long_name;
	            }
	        });

	        if (zoom <= 4) {
	            // get country
	            place = country;
	        } else {
	            // get country and city
	            place = city + (country.length > 0 ? ', ' + country : country);
	        }

	        return place;
	    },

	    clearMarkers: function clearMarkers() {
	        for (var i = 0; i < this.markers.length; i++) {
	            this.markers[i].setMap(null);
	        }
	        this.markers.length = 0;
	    },

	    refresh: function refresh() {
	        this.google.maps.event.trigger(this.map, 'resize');
	        this.map.setCenter(new this.google.maps.LatLng(this.originLatLng[0], this.originLatLng[1]));
	        this.map.setZoom(0);
	        this.clearMarkers();
	    }

	};
	exports.gmapImpl = gmapImpl;

/***/ },
/* 97 */
/***/ function(module, exports, __webpack_require__) {

	var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_RESULT__;(function(root, factory) {

		if (root === null) {
			throw new Error('Google-maps package can be used only in browser');
		}

		if (true) {
			!(__WEBPACK_AMD_DEFINE_FACTORY__ = (factory), __WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ? (__WEBPACK_AMD_DEFINE_FACTORY__.call(exports, __webpack_require__, exports, module)) : __WEBPACK_AMD_DEFINE_FACTORY__), __WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
		} else if (typeof exports === 'object') {
			module.exports = factory();
		} else {
			root.GoogleMapsLoader = factory();
		}

	})(typeof window !== 'undefined' ? window : null, function() {


		'use strict';


		var googleVersion = '3.18';

		var script = null;

		var google = null;

		var loading = false;

		var callbacks = [];

		var onLoadEvents = [];

		var originalCreateLoaderMethod = null;


		var GoogleMapsLoader = {};


		GoogleMapsLoader.URL = 'https://maps.googleapis.com/maps/api/js';

		GoogleMapsLoader.KEY = null;

		GoogleMapsLoader.LIBRARIES = [];

		GoogleMapsLoader.CLIENT = null;

		GoogleMapsLoader.CHANNEL = null;

		GoogleMapsLoader.SENSOR = null;

		GoogleMapsLoader.LANGUAGE = null;

		GoogleMapsLoader.VERSION = googleVersion;

		GoogleMapsLoader.WINDOW_CALLBACK_NAME = '__google_maps_api_provider_initializator__';


		GoogleMapsLoader._googleMockApiObject = {};


		GoogleMapsLoader.load = function(fn) {
			if (google === null) {
				if (loading === true) {
					if (fn) {
						callbacks.push(fn);
					}
				} else {
					loading = true;

					window[GoogleMapsLoader.WINDOW_CALLBACK_NAME] = function() {
						ready(fn);
					};

					GoogleMapsLoader.createLoader();
				}
			} else if (fn) {
				fn(google);
			}

			var promiseError = function() {
				throw new Error('Using promises is not supported anymore. Please take a look in new documentation and use callback instead.');
			};

			return {
				then: promiseError,
				'catch': promiseError,
				fail: promiseError
			};
		};


		GoogleMapsLoader.createLoader = function() {
			script = document.createElement('script');
			script.type = 'text/javascript';
			script.src = GoogleMapsLoader.createUrl();

			document.body.appendChild(script);
		};


		GoogleMapsLoader.isLoaded = function() {
			return google !== null;
		};


		GoogleMapsLoader.createUrl = function() {
			var url = GoogleMapsLoader.URL;

			url += '?callback=' + GoogleMapsLoader.WINDOW_CALLBACK_NAME;

			url += '&sensor=' + ((GoogleMapsLoader.SENSOR === true || GoogleMapsLoader.SENSOR === 'true') ? 'true' : 'false');

			if (GoogleMapsLoader.KEY) {
				url += '&key=' + GoogleMapsLoader.KEY;
			}

			if (GoogleMapsLoader.LIBRARIES.length > 0) {
				url += '&libraries=' + GoogleMapsLoader.LIBRARIES.join(',');
			}

			if (GoogleMapsLoader.CLIENT) {
				url += '&client=' + GoogleMapsLoader.CLIENT + '&v=' + GoogleMapsLoader.VERSION;
			}

			if (GoogleMapsLoader.CHANNEL) {
				url += '&channel=' + GoogleMapsLoader.CHANNEL;
			}

			if (GoogleMapsLoader.LANGUAGE) {
				url += '&language=' + GoogleMapsLoader.LANGUAGE;
			}

			return url;
		};


		GoogleMapsLoader.release = function(fn) {
			var release = function() {
				GoogleMapsLoader.KEY = null;
				GoogleMapsLoader.LIBRARIES = [];
				GoogleMapsLoader.CLIENT = null;
				GoogleMapsLoader.CHANNEL = null;
				GoogleMapsLoader.LANGUAGE = null;
				GoogleMapsLoader.SENSOR = false;
				GoogleMapsLoader.VERSION = googleVersion;

				google = null;
				loading = false;
				callbacks = [];
				onLoadEvents = [];

				if (typeof window.google !== 'undefined') {
					delete window.google;
				}

				if (typeof window[GoogleMapsLoader.WINDOW_CALLBACK_NAME] !== 'undefined') {
					delete window[GoogleMapsLoader.WINDOW_CALLBACK_NAME];
				}

				if (originalCreateLoaderMethod !== null) {
					GoogleMapsLoader.createLoader = originalCreateLoaderMethod;
					originalCreateLoaderMethod = null;
				}

				if (script !== null) {
					script.parentElement.removeChild(script);
					script = null;
				}

				fn();
			};

			if (loading) {
				GoogleMapsLoader.load(function() {
					release();
				});
			} else {
				release();
			}
		};


		GoogleMapsLoader.onLoad = function(fn) {
			onLoadEvents.push(fn);
		};


		GoogleMapsLoader.makeMock = function() {
			originalCreateLoaderMethod = GoogleMapsLoader.createLoader;

			GoogleMapsLoader.createLoader = function() {
				window.google = GoogleMapsLoader._googleMockApiObject;
				window[GoogleMapsLoader.WINDOW_CALLBACK_NAME]();
			};
		};


		var ready = function(fn) {
			var i;

			loading = false;

			if (google === null) {
				google = window.google;
			}

			for (i = 0; i < onLoadEvents.length; i++) {
				onLoadEvents[i](google);
			}

			if (fn) {
				fn(google);
			}

			for (i = 0; i < callbacks.length; i++) {
				callbacks[i](google);
			}

			callbacks = [];
		};


		return GoogleMapsLoader;

	});


/***/ },
/* 98 */
/***/ function(module, exports) {

	
	/* require d3 */

	"use strict";

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});
	var datepickerImpl = {

	    originYear: [1000, 1100],
	    width: 330, // default
	    height: 125, // default

	    init: function init(options) {
	        this.el = options.el;
	        this.datefrom = options.input[0];
	        this.dateto = options.input[1];

	        // render datepicker
	        this.render(this.el);
	    },

	    render: function render(el) {
	        //
	        // start year, end year. Note: Date is limited to a unix timestamp range.
	        // years between 0 and 99 will be (year + 1900)
	        //
	        var minY = 0,
	            maxY = 2050;

	        var margin = { top: 10, right: 10, bottom: 40, left: 20 },
	            width = this.width - margin.left - margin.right,
	            height = this.height - margin.top - margin.bottom;

	        var minY = new Date();
	        minY.setFullYear(0);
	        var x = d3.time.scale().domain([minY, new Date(maxY, 0, 1)]).range([0, width]);

	        var xAxisg = d3.svg.axis().scale(x).orient("bottom").ticks(d3.time.years, 50).tickSize(-height).tickFormat("");

	        var xAxis = d3.svg.axis().scale(x).orient("bottom").ticks(d3.time.years, 100).tickPadding(0).tickFormat(function (d) {
	            return d.getFullYear();
	        });

	        var brush = d3.svg.brush().x(x).extent([new Date(this.originYear[0], 0, 1), new Date(this.originYear[1], 0, 1)]).on("brush", brushed);

	        var svg = d3.select(el).append("svg").attr("width", width + margin.left + margin.right).attr("height", height + margin.top + margin.bottom).append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	        //.call(zoom);

	        svg.append("rect").attr("class", "grid-background").attr("width", width).attr("height", height);

	        svg.append("g").attr("class", "x grid").attr("transform", "translate(0," + height + ")").call(xAxisg).selectAll(".tick").classed("minor", function (d) {
	            return d.getHours();
	        });

	        svg.append("g").attr("class", "x axis").attr("transform", "translate(0," + height + ")").call(xAxis).selectAll("text").attr("x", 6).attr("transform", "rotate(45)").style("text-anchor", null);

	        var gBrush = svg.append("g").attr("class", "brush").call(brush);

	        gBrush.selectAll("rect").attr("height", height);

	        function brushed() {
	            var extent0 = brush.extent(),
	                extent1;

	            // if dragging, preserve the width of the extent
	            if (d3.event.mode === "move") {
	                var d0 = d3.time.day.round(extent0[0]);
	                var d1 = d3.time.day.offset(d0, Math.round((extent0[1] - extent0[0]) / 864e5));
	                var d00 = Math.ceil(extent0[0].getFullYear() / 50.0) * 50;
	                var d10 = Math.ceil(d1.getFullYear() / 50.0) * 50;
	                var d00y = new Date(),
	                    d10y = new Date();
	                d00y.setFullYear(d00, 0, 1);
	                d10y.setFullYear(d10, 0, 1);
	                extent1 = [d00y, d10y];

	                // set date values in input fields
	                $(datepickerImpl.datefrom).val(d00);
	                $(datepickerImpl.dateto).val(d10);
	            }

	            // otherwise, if resizing then round both dates
	            else {

	                    var yr0 = extent0[0].getFullYear();
	                    var yr0m = Math.floor(yr0 / 50.0) * 50;
	                    var yr1 = extent0[1].getFullYear();
	                    var yr1m = Math.ceil(yr1 / 50.0) * 50;

	                    if (yr0m == yr1m) yr1m = yr1m + 50;

	                    var startY = new Date(),
	                        endY = new Date();
	                    startY.setFullYear(yr0m, 0, 1);
	                    endY.setFullYear(yr1m, 0, 1);
	                    extent1 = [d3.time.year.floor(startY), d3.time.year.ceil(endY)];

	                    // set date values in input fields
	                    $(datepickerImpl.datefrom).val(yr0m);
	                    $(datepickerImpl.dateto).val(yr1m);
	                }

	            d3.select(this).call(brush.extent(extent1));
	        }
	    }

	};
	exports.datepickerImpl = datepickerImpl;

/***/ },
/* 99 */
/***/ function(module, exports, __webpack_require__) {

	"use strict";

	/** controllers */
	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var _commonController = __webpack_require__(89);

	var _commonController2 = _interopRequireDefault(_commonController);

	/** constants */

	var _modelsConsts = __webpack_require__(93);

	var _utilsBase64icons = __webpack_require__(100);

	var OSDController = (function (_Controller) {
	    _inherits(OSDController, _Controller);

	    function OSDController(options) {
	        _classCallCheck(this, OSDController);

	        _get(Object.getPrototypeOf(OSDController.prototype), 'constructor', this).call(this, options);
	        this.osd = options.osd;

	        // constants
	        this.TOOLBAR = _modelsConsts.TOOLBAR;
	        this.PTYPES = _modelsConsts.PTYPES;
	        this.ICONS = _utilsBase64icons.ICONS;
	    }

	    _createClass(OSDController, [{
	        key: 'setControllers',
	        value: function setControllers(dialog_c, toolbar_c) {
	            this.dialog_c = dialog_c;
	            this.toolbar_c = toolbar_c;
	        }
	    }, {
	        key: 'init',
	        value: function init() {
	            this.addEventHandlers();
	            return this;
	        }
	    }, {
	        key: 'addEventHandlers',
	        value: function addEventHandlers() {
	            this.addCanvasHandler();
	            this.addZoomPanHandler();
	            this.addAnimationHandler();
	            this.addMarkerHandler();
	        }
	    }, {
	        key: 'addCanvasHandler',
	        value: function addCanvasHandler() {
	            var _this = this;

	            this.osd.addHandler('canvas-click', function (target, info) {

	                if (target.quick == true) {
	                    // single click
	                    var position = target.position;
	                    _this.actionOnCanvasClick(position);
	                } else {} // drag click

	                // // viewer element coordinates (pixel), origin point is top left corner of the container element
	                // var elCoords = target.position; // e.g., a.Point {x: 150, y: 70}
	                // // viewport coordinates (vector)
	                // var viewportCoords = this.osd.viewport.pointFromPixel(elCoords); // e.g., a.Point {x: 0.014114694965758678, y: 0.10612159548329783}
	                // // image coordinates (pixel)
	                // var imgCoords = this.osd.viewport.viewportToImageCoordinates(viewportCoords.x, viewportCoords.y); //e.g., a.Point {x: 58.378378378377896, y; 438.9189189189198}
	            });
	        }
	    }, {
	        key: 'addZoomPanHandler',
	        value: function addZoomPanHandler() {
	            var _this2 = this;

	            this.osd.addHandler('zoom', function (target) {
	                _this2.actionOnZoomPanAnimation();
	            });

	            this.osd.addHandler('pan', function (target) {
	                _this2.actionOnZoomPanAnimation();
	            });
	        }
	    }, {
	        key: 'addAnimationHandler',
	        value: function addAnimationHandler() {
	            var _this3 = this;

	            this.osd.addHandler('animation', function (target) {
	                _this3.actionOnZoomPanAnimation();
	            });

	            this.osd.addHandler('animation-finish', function (target) {
	                _this3.actionOnZoomPanAnimation();
	            });

	            this.osd.addHandler('animation-start', function (target) {
	                _this3.actionOnZoomPanAnimation();
	            });
	        }
	    }, {
	        key: 'addMarkerHandler',
	        value: function addMarkerHandler() {
	            var _this4 = this;

	            $(this.osd.container).on('click', 'div[id^=osd-anno-marker]', function (e) {
	                _this4.actionOnMarkerClick(e);
	            });
	        }

	        /*************
	         * action bit
	         *************/

	    }, {
	        key: 'actionOnCanvasClick',
	        value: function actionOnCanvasClick(position) {

	            var idx = this.toolbar_c.getActiveTbId();
	            console.log(idx);

	            // clear guides
	            this.clearGuides();

	            var idx = this.toolbar_c.getActiveTbId();

	            if ((idx[0] == this.TOOLBAR.PRIMARY.POINT || idx[0] == this.TOOLBAR.PRIMARY.REGION) && idx[1] >= 0) {
	                // draw guides
	                this.drawGuides(position, idx);

	                // show dialog
	                this.openDialog({
	                    target: idx[0],
	                    type: idx[1],
	                    position: position
	                });
	            }
	        }
	    }, {
	        key: 'actionOnZoomPanAnimation',
	        value: function actionOnZoomPanAnimation() {

	            var idx = this.toolbar_c.getActiveTbId();

	            if (idx[1] >= this.TOOLBAR.SECONDARY.PERSON) {

	                // sync box helper and box overlay
	                if (idx[0] == this.TOOLBAR.PRIMARY.REGION) {
	                    this.moveGuideBox();
	                }
	            }
	        }
	    }, {
	        key: 'actionOnMarkerClick',
	        value: function actionOnMarkerClick(e) {

	            // prevent event from bubbling
	            e.preventDefault();
	            e.stopPropagation();

	            var el = e.target;

	            // show info dialog
	            this.openDialog({
	                of: el,
	                anno: {
	                    uuid: $(el).data('uuid'),
	                    id: $(el).data('id'),
	                    content: $(el).data('content')
	                }
	            });
	        }

	        /*************
	         * dialog bit
	         *************/

	    }, {
	        key: 'openDialog',
	        value: function openDialog(opts) {
	            this.dialog_c.openDialog(opts);
	        }

	        /************
	         * guide bit
	         ************/

	    }, {
	        key: 'drawGuides',
	        value: function drawGuides(position, idx) {
	            var _this5 = this;

	            // var idx = this.toolbar_c.getActiveTbId();

	            if (idx[0] == this.TOOLBAR.PRIMARY.POINT) {
	                var point = this.osd.viewport.pointFromPixel(position);

	                // draw guide icon and line
	                this.drawGuideIcon(point, idx[1]);
	                this.drawGuideLine(point);
	            } else if (idx[0] == this.TOOLBAR.PRIMARY.REGION) {
	                // draw box and box helper
	                this.drawGuideBox(position);
	                setTimeout(function (e) {
	                    _this5.drawGuideBoxHelper();
	                }, 100);
	            }
	        }

	        /** show guide icon for point tagging */
	    }, {
	        key: 'drawGuideIcon',
	        value: function drawGuideIcon(point, type) {
	            var icon = document.createElement("img");
	            icon.id = 'osd-guide-icon';
	            icon.className = 'osd-guide-icon';
	            icon.src = this.getIconSrc(type);

	            this.osd.addOverlay(icon, new OpenSeadragon.Point(point.x, point.y));
	        }

	        /** show guide line for point tagging */
	    }, {
	        key: 'drawGuideLine',
	        value: function drawGuideLine(point) {
	            var line = document.createElement("div");
	            line.id = "osd-guide-line";
	            line.className = 'osd-guide-line';

	            this.osd.addOverlay(line, new OpenSeadragon.Point(point.x, point.y));
	        }

	        /** show guide box (invisible) for region tagging */
	    }, {
	        key: 'drawGuideBox',
	        value: function drawGuideBox(point) {
	            var box = document.createElement("div");
	            box.id = "osd-guide-box";
	            box.className = "osd-guide-box";

	            var rect = this.getRectViewportCoords(point, 60, 60),
	                OSDrect = new OpenSeadragon.Rect(rect[0], rect[1], rect[2], rect[3]);
	            console.log(rect);

	            this.osd.addOverlay(box, OSDrect, OpenSeadragon.OverlayPlacement.CENTER);
	        }

	        /** show guide boxhelper (visible, draggable and resizable) for region tagging */
	    }, {
	        key: 'drawGuideBoxHelper',
	        value: function drawGuideBoxHelper() {
	            var _this6 = this;

	            //
	            // render guide box helper.
	            //
	            // the guide box should be draggable and resizable. converting (osd) box overlay to jquery dialog is possible,
	            // but dragging and resizing action of dialog will conflict with osd overlay. the workaround is to render a
	            // box helper which has the same size and centre position as the guide box overlay.
	            //
	            $(this.osd.element).append('<div id="osd-guide-boxhelper">' + '<div id="osd-guide-boxhelper-centre-handle">' + '<div draggable="false"></div>' + '</div>' + '</div>');

	            // make box helper draggable and resizable
	            $('#osd-guide-boxhelper').draggable({ handle: '#osd-guide-boxhelper-centre-handle' });
	            $('#osd-guide-boxhelper').resizable({ handles: 'n, e, s, w, ne, se, nw, sw' });

	            // add event handler
	            $('#osd-guide-boxhelper').on('drag start stop resize', function (e, ui) {
	                _this6.moveGuideBoxHelper();
	            });

	            this.moveGuideBox();
	        }
	    }, {
	        key: 'moveGuideBox',
	        value: function moveGuideBox() {
	            var box = $('#osd-guide-box');
	            if (box.length > 0) {
	                var p = box.position(),
	                    wo = box.outerWidth(),
	                    ho = box.outerHeight(),
	                    w = box.width(),
	                    h = box.height();

	                // update box helper
	                $('#osd-guide-boxhelper').width(w);
	                $('#osd-guide-boxhelper').height(h);
	                $('#osd-guide-boxhelper').position({
	                    my: 'center center',
	                    at: 'left+' + (p.left + wo / 2) + ' top+' + (p.top + ho / 2),
	                    of: this.osd.element,
	                    collision: 'none'
	                });
	            }
	        }
	    }, {
	        key: 'moveGuideBoxHelper',
	        value: function moveGuideBoxHelper() {
	            var boxhelper = $('#osd-guide-boxhelper');
	            if (boxhelper.length > 0) {
	                var p = boxhelper.position(),
	                    wo = boxhelper.outerWidth(),
	                    ho = boxhelper.outerHeight(),
	                    w = boxhelper.width(),
	                    h = boxhelper.height();

	                // update box overlay
	                var c = this.osd.viewport.viewerElementToImageCoordinates(new OpenSeadragon.Point(p.left + w / 2, p.top + h / 2)),
	                    tl = this.osd.viewport.viewerElementToImageCoordinates(new OpenSeadragon.Point(p.left, p.top)),
	                    tr = this.osd.viewport.viewerElementToImageCoordinates(new OpenSeadragon.Point(p.left + w, p.top)),
	                    bl = this.osd.viewport.viewerElementToImageCoordinates(new OpenSeadragon.Point(p.left, p.top + h));
	                var w = tr.x - tl.x,
	                    h = bl.y - tl.y,
	                    OSDrect = this.osd.viewport.imageToViewportRectangle(c.x - w / 2, c.y - h / 2, w, h);

	                this.osd.updateOverlay('osd-guide-box', OSDrect, OpenSeadragon.OverlayPlacement.CENTER);
	            }
	        }
	    }, {
	        key: 'clearGuides',
	        value: function clearGuides() {
	            var _this7 = this;

	            // remove guide line, icon and box
	            $('[class^=osd-guide]').each(function (i, el) {
	                _this7.osd.removeOverlay(el); // this.osd.removeOverlay($(el).attr('id'));
	            });

	            // remove guide boxhelper
	            $('#osd-guide-boxhelper').remove();
	        }

	        /*************
	         * marker bit
	         *************/

	        /**
	         * @id    annotations count
	         * @anno  annotation class object
	         */

	    }, {
	        key: 'drawMarker',
	        value: function drawMarker(id, anno) {

	            var self = this;

	            var target = anno.getTarget(),
	                type = anno.getType(),
	                posType = anno.getPositionType(),
	                coordinates = anno.getCoordinates(),
	                uuid = anno.getUUID(),
	                name = anno.getName();

	            // create marker element
	            var el = document.createElement('div');
	            el.id = 'osd-anno-marker' + id;
	            el.className = 'osd-anno-marker ' + posType + ' ' + type;

	            $(el).data('uuid', uuid);
	            $(el).data('content', name);
	            $(el).data('id', id);

	            //
	            // draw marker on osd
	            //

	            if (posType == this.PTYPES.POINT) {
	                var viewportCoords = this.osd.viewport.imageToViewportCoordinates(coordinates[0].x, coordinates[0].y);
	                this.osd.addOverlay({
	                    element: $(el)[0],
	                    location: new OpenSeadragon.Rect(viewportCoords.x - 0.02, viewportCoords.y - 0.02, 0.04, 0.04)
	                });
	            } else if (posType == this.PTYPES.POLYGON) {
	                var viewpointCoords = this.getGuideBoxRectViewpointCoords(coordinates);
	                this.osd.addOverlay({
	                    element: $(el)[0],
	                    location: new OpenSeadragon.Rect(viewpointCoords[0], viewpointCoords[1], viewpointCoords[2], viewpointCoords[3])
	                });
	            }
	        }
	    }, {
	        key: 'drawMarkers',
	        value: function drawMarkers(annos) {
	            for (var i = 0; i < annos.length; i++) {
	                var anno = annos[i];
	                this.drawMarker(i, anno);
	            }
	        }
	    }, {
	        key: 'clearMarker',
	        value: function clearMarker(id) {
	            this.osd.removeOverlay('osd-anno-marker' + id);
	        }
	    }, {
	        key: 'clearMarkers',
	        value: function clearMarkers() {
	            var _this8 = this;

	            $('div[id^=osd-anno-marker').each(function (i, el) {
	                _this8.osd.removeOverlay(el);
	            });
	        }

	        /******************
	         * conversion bit
	         ******************/

	        /** get corner and center coordinates of guide boxhelper */
	    }, {
	        key: 'getGuideBoxHelperImageCoords',
	        value: function getGuideBoxHelperImageCoords(elCoordsOriginX, elCoordsOriginY, elCoordsWidth, elCoordsHeight) {
	            return [// Center, NW, NE, SE, SW
	            this.osd.viewport.viewerElementToImageCoordinates(new OpenSeadragon.Point(elCoordsOriginX + elCoordsWidth / 2, elCoordsOriginY + elCoordsHeight / 2)), this.osd.viewport.viewerElementToImageCoordinates(new OpenSeadragon.Point(elCoordsOriginX, elCoordsOriginY)), this.osd.viewport.viewerElementToImageCoordinates(new OpenSeadragon.Point(elCoordsOriginX + elCoordsWidth, elCoordsOriginY)), this.osd.viewport.viewerElementToImageCoordinates(new OpenSeadragon.Point(elCoordsOriginX + elCoordsWidth, elCoordsOriginY + elCoordsHeight)), this.osd.viewport.viewerElementToImageCoordinates(new OpenSeadragon.Point(elCoordsOriginX, elCoordsOriginY + elCoordsHeight))];
	        }

	        /** get center coordinates of guide icon */
	    }, {
	        key: 'getGuideIconImageCoords',
	        value: function getGuideIconImageCoords(elCoordsOriginX, elCoordsOriginY, elCoordsWidth, elCoordsHeight) {
	            return this.osd.viewport.viewerElementToImageCoordinates(new OpenSeadragon.Point(elCoordsOriginX + elCoordsWidth / 2, elCoordsOriginY + elCoordsHeight / 2));
	        }

	        /** get guide box's viewport coordinates */
	    }, {
	        key: 'getGuideBoxRectViewpointCoords',
	        value: function getGuideBoxRectViewpointCoords(imageCoords) {
	            var center = this.osd.viewport.imageToViewportCoordinates(imageCoords[0].x, imageCoords[0].y);
	            var nw = this.osd.viewport.imageToViewportCoordinates(imageCoords[1].x, imageCoords[1].y),
	                ne = this.osd.viewport.imageToViewportCoordinates(imageCoords[2].x, imageCoords[2].y),
	                sw = this.osd.viewport.imageToViewportCoordinates(imageCoords[4].x, imageCoords[4].y);
	            var w = ne.x - nw.x,
	                h = sw.y - nw.y;
	            return [center.x - w / 2, center.y - h / 2, w, h];
	        }

	        /** get guide box's vector coordinates of rect overlay */
	    }, {
	        key: 'getRectViewportCoords',
	        value: function getRectViewportCoords(point, boxWidth, boxHeight) {
	            var c = this.osd.viewport.viewerElementToViewportCoordinates(point),
	                tl = this.osd.viewport.viewerElementToViewportCoordinates(new OpenSeadragon.Point(point.x - boxWidth / 2, point.y - boxHeight / 2)),
	                tr = this.osd.viewport.viewerElementToViewportCoordinates(new OpenSeadragon.Point(point.x + boxWidth / 2, point.y - boxHeight / 2)),
	                bl = this.osd.viewport.viewerElementToViewportCoordinates(new OpenSeadragon.Point(point.x - boxWidth / 2, point.y + boxHeight / 2)),
	                br = this.osd.viewport.viewerElementToViewportCoordinates(new OpenSeadragon.Point(point.x + boxWidth / 2, point.y + boxHeight / 2));
	            var w = tr.x - tl.x,
	                h = bl.y - tl.y;
	            return [tl.x, tl.y, w, h];
	        }

	        /**
	         * get icon src
	         */
	    }, {
	        key: 'getIconSrc',
	        value: function getIconSrc(type) {
	            switch (type) {
	                case this.TOOLBAR.SECONDARY.PERSON:
	                    return this.ICONS.PERSON;
	                case this.TOOLBAR.SECONDARY.ABOUT:
	                    return this.ICONS.ABOUT;
	                case this.TOOLBAR.SECONDARY.DATE:
	                    return this.ICONS.DATE;
	                case this.TOOLBAR.SECONDARY.PLACE:
	                    return this.ICONS.PLACE;
	                default:
	                    return '';
	            }
	        }

	        /**
	         * element getter. osd has no corresponding view
	         */
	    }, {
	        key: 'boxhelper',
	        get: function get() {
	            return $('#osd-guide-boxhelper')[0];
	        }
	    }, {
	        key: 'guideicon',
	        get: function get() {
	            return $('#osd-guide-icon')[0];
	        }
	    }]);

	    return OSDController;
	})(_commonController2['default']);

	exports['default'] = OSDController;
	module.exports = exports['default'];

/***/ },
/* 100 */
/***/ function(module, exports) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});
	var ICONS = Object.freeze({
	    PERSON: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAQAAADa613fAAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQffAQ4MAjIB+JC/AAAJSklEQVR42tVca3BVVxX+9snNi7woCQmp4V1IwFZmgBGKAq12pozjFDstykMnihSZaTtWp1CKtAZkpK1QNUyp2qSMtjKDUxsdOyBQp/KKSISkA5bSUibQFEJCICSXvHM/fwDJzT3rnLPPufeexHX/7bvX2vvbj7XXY++jEHPiSHwO+cjDKOQgE5kwALSjBVfRgM/QiIv4VPVgqBJTmctvcTf16COWcDIzaQwtEA+wijfYQ7fUwQZuYNJQgPAV7mEzo6WTXD94EJK5gucYS9rOKX6DGMbvMz60n/n+wShmN+NHPTzMlPiDmM2PGX9q59Z4ggiwlP7RBc5y0zulDWMSqpGmLbcbbWhDO3rQhR4oJCMBKUhFGlJd9G4NtqpQLOdCcY3WKIZInuPznMIEJtBgAg2qWz+DBg0GmMVHuZchTXkfxHZRvae1rp/nFGZoSjQ4hgv5bw25zZwYGxDDWePY2B94n0fpo7iWtY7yF0UPYwTrHcbrN9HbS3zIURc+Hu3B12Ir/lWqmC3fe9hqu3M2e2yLitlstRH8d46MsXpPZLHtsP3Mk/plDk4jx+LPHixRb9mfOcjGRBRiDPKRAaAVl3ABZ/AJmux8EebgDSyw/PsxVeZ2dAyethyZY06TzAl8nLtZy/YIvVbL3XyCExy4S9hr2fbX3cI4ZqnZX3LgzWExDzFo0ZleBnmIxcyxXdRFNr5Nnhsgr1iKedBhX03mNl50VKcXuY2T7eaVmWyw4L2sD+OLFiK6Oc+Bczp3aVrG3dzF6R5XxVEt/cVMaxj2AjiNb7twd3v4Nqc59OWwBe8yHSBvWTDPcYCRzTJ2urJvO1nObIfeWFkV45xgLLVgdFpUSVxte+rI1MrV9qEHJlpIPewE5IrIttpxHqezRtOiHagDa+x3CsBsC94f2DFtk/1oRxhJ3GSj++2ol5uY7CB9jsh5xe4EkLZqs8a+mmqpYZypilMdjaVXRc4tVgzlYnUNA53Led0zkOtcrtFCjags7uiv0WeCcxwkgb9W/3ReWJjhwgmOpDTM0Ig1LhXKkvCk1J2XRF2frjFaudznYaP3b/h9zNVoZbPIbfZIxWrf1bIExrM6qnhJNcdrtJLFLoF3VcTS4k8E3k/xe63FkYLoAmpa/Oo6nhKK10buEWkNblTU6kgCEqICosmvtqPRVDiWXwoDwiyYVWC9thPThe6ogPSgS7Pmm0LZ0+Ez8qJQYZt2RzoQBD3DIILo0Iwm/lgonNMHhIlYLFR4TbsrQVyKCshFBLVr/9FUksui2zOSgWGmv/erRm3hzTgN74HNEE6jWbv2G0LZhttDskBQa/e7GtSFvOxZ+V7mQ67akmyDwM0Zkc7H/7oa1eM44XlGTrjkfU1Q3+lWGK+6jrmsZNDTfAS5kq6UN2cLVkQhYHC0ULvUHRAVwl4c9LBPQjiIvarXFc95IRa3EADnC+M01kNccplG9MQcTVnmNhBKQ/B8jgLgd0zFbfRwUjOZay1DODI18Fknp0ps6YBZFGBgsqnmdS+ngurE6yjHDU1e4gZeR7nq9KAeKiSTEtxpgvex10QBR3AjG7Vmo5EbOcJjK9MEeXeBR1xHKOwayeAKVjr4772s5Ard3JbQRoogc1YA5uTAJ96BqFbuQDW+iYdRgGQYJi3ViTpU4E+ocamrBhqZnYjcW5kBDDdVrIvGlFW9OM5qVGA+5mEScpCBAIAetKIJH+EgDuBY1JlaM5CUgGBnNUWbslEhHOVxlCEfY5GLFCh04DLO4xJaVHe00kHhxEoJIFGwZmNAqhtNaMIpxJ4kIIlD6+JXFGQI3l36EO+zgnn4uw20mQqz/w+BdATQbFLABa4XbQBpyEQGUuFmqYbQgRa0oM21AkiWgDRiUkShiysTNDAa0zATRRiNbKQh4Oo8uIEm1OFDVuF91GmfLAEBSEsA52+7732UR0NH09NAHh7BN3APMpEMb+n8SSA60YKT+Av/jMtaJ0yhUNYEbjQd95d0bC2mchErIhLQ0V02q+AialyC4lMCd5ZHM54F/CkvRBHxlaPAF1hCxx3KQ5IZD85zn6HjBJZ5SLTpub5l9hcKRMfqXzfH1kwbHWDsiPFcDKQddlCYb5kaFP64ZhsV38K2uN5mbOMWZlm2f68cfADAvwnCcq1ODK5y6dB6oQauooUi5++EzNVNC54P6gfoONUyiR9bOmyVWbQL0FUJ0fB1opBhKMZsX8yQ2SjmMKEH0hWoPbcuTjFRTGXmisGxU77d+z3Fe4UevCnULLwVxFbd2CngfMwcu8JcjIdfNAFfFmJe5lsoDepMf37kGUGQOSI8HLOQ4huQZMyKdMP5S6Hekb78CKBahKB1HldGlqAI/jliBooQecns20K9LWFAAHFxPRcxtQUY7avfMXqgQ8EnhTuWtapyABD1c0FQAb43wNbNh7+PiJKQ32++MgvSC4YX+ifwNv1CykWEXRgwcCf89fAHtrhOCJOEpeL6K74iitoc5mCmQ/kKJKxF3o01Qo31KmgCos5DSkc/EXbG+x9xMcwjH0Zd2A6xc89Ccjb/GjZC/ocZAFDxt/iC8G+puiYCUVfE5ZXBdwc5ajIHK4XSK2r1QEc+fAB+yKWCivsq16oXQJzFu74urxDOgsyBnB1YZ+/7LbawfO4bnMlgkoUnesCZdZcFlLlUgwDkpEVvxjizZlheOJ7vNxRWWvRliR77TEso9/sIIsATFv04oi/kV5ZewgKfYGTxmkUP6t2IUTxqGXt6Oe4gFKfaZCFHuhX2gaWo/zAhrkDsLkN/zb24bJtoSS8XxwlEHvfZuL/LvU3wCNs3b/t1Lrm6PDOW23rxG7yLTnF4vlcWu282cIbD/aJNUSl/3sF62/DodZYzMWoQi3jWIaayKvqRGm6pz/tpJx/wKP1OrmOdo/xHYjXt/9D6okYJ72ampsQEjuPDrNKQe83puZ+7jf+09jPwWpbw87bPwJdwr3bGRPsZuP7D/Ik4jixt7LF5mP8jlMb0YX7fctjq46cSznFmPE/d6TzjA4g2vuiHQbfUw8d13HxO5D0m+eQwMNXhwbZ32uPqLW5MwCRyueMx5o5KWThoAQ7O5zu8GjWE9/kMhgJxLisZdL1zQmxnPZ8bat/YSuFIPsp3NEF8yPW8K5YfCouDdmA2xmBU36fb0pGEXrQh2Pfptjp8FvtPt/0PADF3BBshNbUAAAAASUVORK5CYII=',

	    ABOUT: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wEOCzcZCjq9zAAACvdJREFUeNrtXX2wVkUZ/+3hfiJcPi5wsRI1TBA1h9IUCMVkApNyHCA/+kMzSK2cyilzEB0tG0ZUNMmwkZzGYZyYaWAsJ1BpnMoIg0LHNJEPMcAQ5Jv4vPf++uPsi8vxct9n9909Z9/rfWbunDv3nt3z7P72+dhnd59ViIhIQil17Gn8fSCAjwM4GUALgMEABgBo0j+JfvUggL0AdgLYBmALgO0A3gWwSSnVim6yBqWR5CCSV5P8A/3SWyTvIXkmySaSSXZAdAOgO4HkeJIrSf6PZCvD0yGS20jeS7KuG5i04V8guYTkbhZPr5GcaYLSZYExG0aynuQ0khsYL/2C5FldEghDJfUk+Q1WF71A8uQ8+0yF9pj079cDmA+gpgrHVRuAFQDGAzgE4DgP0Dclgd3Xi0iuBfDrKgUDAHoAGANgF4CHjEEWv4QYQNQAmAPg1i5oEjcBmKqUejk7X4oGkIx6+hSA1QBOCtAZRwEc0D8HAbQCOKKfCkC9HtENABo1D42BgLkdwEMA2kOqsEpAUSRv92hQ2/VzA8m7SZ5Fsof+SYynyvwkxk8NyT4kp5B8zqjTF39vROciG17Ui54aetAAoLdnXhOSQ0heSfJlT/zuJjk0igmlHhl9Sb7ioWFPkRwXarbcUX0kB5O8g+RGD/xPLXSmr8HoT3JrhaPr8WxMKWfJLj2/QnJthaB8uxBQNBg9Se6tgPl5WufHFkk4l+S+CuzNLJIqN1B0JzZrpl1oqQ6n59LBjqDUkry+gsH2kzwlYwDJ7Q5MHiU5JbRXYqigk0gOJ/lJm4BhBpgBOvjpQtOCSYnRyITkvx2Y+3tJjHOwCaNIru6AhwdJNjvG4e4h2ebQ7kkhQUl0x9r66rPzMnQkf1qGn8M2oGTU9HDHdZoWr+02RsljDsxMyMu4kbzFYp7T7GhbmvSilg29560PDDA+52AvLs4RjN6W/D1my1tGbdtqihVeVLbWo00uYIS2GZlOmmkruh7Wd16y/OTXvAxQkr+1/PDovOcXJBc7qNMRHuYsthGK05zWQ4xRcB2AyRb8XqKUWq6UYs4R0FqHMg1O4XG9TUn30QUA9lsUX+AkJQYg71ug/8Oi4jgkn3Dxfjypy2bL797kCspcm7XnIqOdJC+z7JT/+ODXAGW0xbfft/62nqFKfe7dkYT/V1t0ytgA4aR5Ft9/0LZxv7KofFwMawF6t6OEngwYmJQa+cMk+0k/cJoFGI9Etkg2iOTrnfB7R6jBow39CIu+u1vEC8nZwgpbSfaKZekyM1Kv0FL+e5KLSP6Y5OCcBsUsC1B6SyqW0g0xqCqb0HtOUYM+JI8I+/DmcujeKfVS8piJVyuR/JawHzd2OlDK6N98Yv3VD0bpKQ1CjulM1CT03+5uFxn4OcL+XHwiRB8XVjCju8vFwIjD89mCtRZnNAZ2d7VYbS0Q9unwbAX9hZ7B8922wwqYCUJAFh7XryQnCgte2hVGboRqa4/eoH4s/C7dpf56lXS80lI/nOQkvfw8qaBN0U8I3mkA0AvQu9+FK2i7lFL9Q2zB72xEZ45Hl3hOANB4DgZwIdJzHJcAGIkPr/W0KKW2FSCRFwFYjvInDYYrpdbUkDxFWP+jAHIBwwBiqF5lGwHgdP0cgvSIQU98cOSg3FbUjXmDUeorku9AduzjSgCzQfISoZ47tQDVc8DTDvU7C1SfiXBP14qSDRkiqPcggM0FtOeop3qWFuVEKKXaAbwkeP1CID33d6bg5T1aV+dtOy4HcAqATwP4vGa63qHK9UUAYqj3xQAulgQmQfJpgTitzfvIwAnOc9SRXGiprtZG4PWdJ+T1jASAxDa8B6C9oNFlSs0RANMsq5oVgSe+RvhecwJAEgpZX/TBRiNL0D6LYu0AFkYQWWgFcFjwXlMCoK/gxc0RjLISKBdYFNkJ4HAkp2QlgDQk2p8vRzsiCnt8x6LYW3p0Fs6+UOU3JJDt+tsfiXT0AHCZRbG5kUiHFJDaBNVFNQCaLUD8TbVFphPh5KtXJPyOhHxP7it5hXokYwOyvDJHE6RpKsq6Y5HYj5kWxf4WkXRIATmUAJBsBf1EBPajDsAVFsWeKbnKkVC9FJDtgheHRtCw0y1BfC4ilVUjBGRvAuAdwYstCJRby4LOt3h3WWS2epjwvR0JgHVVYtRtQiYLIgNEuvS9PQEgCb71QcB0gAKjngAYZ1FkVRSTjw/UvOgUmlJqj1RlNSINgxfVoC9aFn0zCtcqdSoSAKMFrx9boNogrP/GTCfl0iBNl1t6V20ReVdS+7vIHIkS2lWg6Nucdh0VCxJ6S+koYWahYTCQe1ZQf1+SgwpSWecJixwG8Fo0s8FUwr8usL9HkK45HQPk58JvnF2ADr7Bosg+pOv/MdF0yYQQOoBbAmSlRqkczcjTjugzKNMtiiyNyX6QnCh8dYlSqtXcSlqrtzNKaFCODUosUwieG5O7a7HZelhHhefFtMdJG8RGCzD25O0FCtrgdhxBF5YmmdmaY4NutQDkz5F5Vw8L+V7U2ZG2fwkr+WZOByg32SSfjEFCDI0jTYM4urNKZggr2ZRDyr7+lvuvRnbSrhptj2KT7LfLDiKLDgg6c9eby6SpWtvL1PU7PbmsCS1Flseib5JUKE0c0BYycQDTdONSeqYT6RhlvLc8lGQb37vfgu9ekopPtahwbsCRttmCj1tOYFh7GTvP9wWWaJA8x3ZHfjl1VXra5J+61HdDSfaztB9jTtAOM+H+VSHyBWdSerzqPfmMrtgmPdPeAKNtkmsyMgMMU/U+EDqiQPKXFvw+4PKRn1l8YJlPKSF5l0tCSwOM24x/79BeVki7McaC3e3WfeXgS3tNfSQ8ImFSD6Ps9zIZUvsENuIDLHmdXgkg11h+bJynxr7okBf3uyTfzfz96hCG3OifOtpdSvCnivlxOCAzttI0sSSXeThXOCfwGg2Y3g5qQ0Mq/rhD1uhWfZBUVfDdJysEY35HHejJtS39vtySp2t9SEfpeb4DKM7usIOq/FB2nYBqqobkPy15+qs3fgxGHnHonInZkWXx3W0O31sQQioyYZFdljxtDTIh1XZhhSUz7aYulzCV8V6OCr/RqqVYBZIOpRNcutwhMjAEGCZjbzgwtcp0TS2+V8/OE+2vJzmZZL1vm5GxF/c5gvGloMFMzWSzozppI3mNVIVl/8/0xoVpJKeT/HJ2KdknEMbvLSSfd7Rl+exjMzLtuN7Q9oKvdfmAtqKO5I0VOBb3FrFE2cDKrs2bn+eikQUYnyW5v4J23cc8r83LgNJP7wpxvfNvD9OEx7VFgWA8p5JcV+H85+YQ0msLSl8Hv7wjeprk+DykwPjbx/Sy9WYP/E8uFIwORtofPaVSOsT0mrpzSDZ55rUH09z2V5Fc6YnfXdR3JUZ14lcb+x/QH5XU4EYN0Nl0v777WqbXd9Mzf96v7w5xwf1QAP9AesjHN8V0wf33kWbZ83rBvfKtvoyMC7MB3IauR28D+KpSalWe+Sd92ZXPkFzDrkEHSN4f2nCrkKAYauw6AE9pdVJt1AbgLwAmaJXJqpAKQTyokZVdiV0ELWGFt7lFrcL077U6LLEuYiAeLR0R4EcprbpeWXyW5M4IQHiV5I86ku6PFBkqbaxeFt1Pt2uybecQB3XY5y5mEnyy+7KB4zqjQS/qTNHS45Pe1OsrZzA9DxMdECo2aenIg2F6//kQpDneW/RzAIAmpGk/6rQ3dADp4cmdALYB2II0uc5mAFuUUq2xD8j/AyAz1LkLZrO+AAAAAElFTkSuQmCC',

	    DATE: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAYAAABw4pVUAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wEOCzs3eln/DwAADblJREFUeNrtXXmQnEUV//36m91NYrIbkpANV4Jc4RIKuawECiMgFAUiAnJZhQRQUEGkuA8BBbkENMhVQRBQQCmhBCTcoTiDQQU55JLDgBA2ZHORZLPb/fOPrydMhpmd/ma+bzK7zKv6aiuZPl73r/v169ev+xEDgCStCWAdAGsB6AQwDsAYAO3+Mz7pMgCLAMwH8BGA9wF0AfgfgDkk+9CkxJ0/VNJYSQdJuk/p0uuSzpW0iaR2SabZ4+WB2E3SbEmfSOpT9rRc0keSzpPU2kQgBuFrkmZIWqDVTy9KOuvzCEKbpKMkvaXGpaslbTbYgRgm6UgNLHpI0lr17CfWCYzDAVwPIDcAx5IFMAvAbiSXZ12ZyRiIr0h6A8DvBigYABABmAygW9JlA3KGSMoBuBzAcYNQ+s4BcCDJZwcEIJI2BvBPAF/IgN9eAEv9twxAH4AV/i8BtPkRPQTAUM/D0IyAOQXAZSRdoy7alHRKiguq83/fkvRTSZtJivxnCv6y6DMFX05Sh6QDJD1QUGZa/L3SyFrUzJQauqwAgBEp82gkjZe0r6RnU+J3gaQNGwmIkZKeT6FhN0v6ap15HyfpNEnvpMD/gY0AxihJH9Y4uq5tBJuSpG9IeqNGUH64ujd6i2pg/hpJbEDR+yVJi2tYby6sa7v8wjnaM10N3e/N6Y28HrZIOryGwfbzejI7RlJXFUz2SjpggJl7xnjjZzV0VOb7EC/rXwawacJ6ZgPYkaQCyt9C0l4ANgewmORjAGaQ/KSWznXOjSe5F4DtfIfNNsbcTfLDgHafC+DsKiwb+5C8N6vRYiT9rQpd/ZIEdWwvaYZzzkmStVbOuY+ttedJGl7LZtU5d6NzbqlzTv5b7pybLmndQDG9aZXnNJ1ZAXJVFczskUR9ttZe7JzrlaSCjpO19gNJe1Zr7rfWHmetnV+i3OXW2hMSlNXuD7WS0NzUjYuSdgDwgwTl9gHYheQDSVRoABMB5KRVJRvJsZK2rnIstQPYkmRHid9aAWwlaUiQfCcXIT7Pn52g/rGSZoVqXiZkVAB4NiEYuwJ4ItFiRrb0Y3dyJKu1SUW+41miTvg6TQI+HckdADyVgIcdARya1gy5IWEH7ALgiUoLeD3JGJM6LyR3AvBCgiy/l7R+TYBIOhTA/knAIPl0I4GRMW0PYEkSUColqHRoNC2JOZrk49bavUlOdM65QtFgjIm8Wf4JkisGyD6kw6vgnc45Fsw4I6kLwIMA1gcwL7DIyZK+T/K6xIBIuhLA6MCKHiZ5qe/8AwEcUOyUJqmN5NV+QRwQgPT29nZEUXQMgK3pFxzflhzJFwG8QvI5SZMTrCkXALgukciSNAbAsYEVLCS5e8G/h5EcRrK98JPU5pwbijqd46dlJvLt6ShqzzCSw73CAADPALg2sNjRkn6ZdA25uKCySvTNNPugUbHp7/9IiuSxCRb54yStEQSI1wSmBhb8a2/aSEVCkFxRvAcpaHxPNYX29fXJOdfXD9grUhwIhwama0UZf4NSMyR0A2gBpOnpt0jSf40xrkBcr/zNOfdGNYXmcrnFAN6WtKQE2CuMMa9VC3YJVfgVABcFJj+v1IloKUBODizwKJJL0kLjgw8+mA/gPgAvSHIFM+MTSbcZYx6rspOWGmPuJflEwWwQgB5JswDck7KjwkWInTFC6LB+tSxJZwYWNAfATWkK6bXXXttJesRP5/0kjQfQQ3KWtfbGKIrm1VD8SyTPl/Q2gM3yo9lae3MURS+mvGFcKOkEAFcFJD+tX2VA0su12vqdc3cUG/G8IU/W2mskdYTYtSRN7O3t/WKanumSRkja2Fq7UYj1uKenZ4K1draLaZX2eOfsHfupK9QIObmkyPIdtXlAuz4keX2WKg3J+SRfa2lpeTvNTSTJxSTfiKLozTTFbbW7ck8nlVtDLg4s4Eo0KQT8EwOTTvoMIJJaABwcWMD0ZncH0x8C0oyVtGnxDBkBYFhA5odIdjX7OZhuCVWBiwHZAUBLQMYLm32cSGyFHtDt6R3UV6q9oV7qL6fJ8MKFC4e1t7eXM3CuKHcfw9uYhqC0cVQALMllZfIaLw3KmY16SPak2MzpAI6ukGYIgOEAFuQbtFdAwd0kP0pRBaWkqZJ2KtgIrtwfkXxU0s0kl5bIPsY5dwSAbQC4olFpATyO+IJQKZog6Xgvu1mQD5L6jDF3ALgnRUBuAHAU+jeqtiK+7r0gJ2m9wIKnpT+juZNz7qCi/4QkSFpG8rYyeUeQ3BXA10vZvrzp//oy+6TRJPcBsGHRAAHJPmvtv1MG5F2EWbj3BXCJAbBBYME3pi1jJTmSKPV5W1lpI5q1yM+qEvn6imfNKmqlMZLU5y20pepM+77H3MAyv5Vf1McHJF4G4L0GWizTGAz14tUBeDIg6Y55QDYJWX/RuGcVA4HuChwkHaZYlpahJYOth8rMsqxOM2cGplvTAJgQIgezuEtHsjUvOvxCvvJDGV+qAtW1ZF7EjnZt/VQbAWjzWl5hPvi9WJQBIK8FphudAxByNeA/WchW59yfAbwEQPkR6xdYI+l5ACX3IVEUzXfO3QLgGW8xLVy0rdeUUEbLmmOMuQLAyELAvXbnkNDBL5D6EB+CtVVI157zjFWirBb0u0hGpcRHd3d336hRo8pZeruNMbcDyFlrVQKwshpaV1fX3M7OzukATHG93l0pK4+YEECG5AJtWB9nwaExZnmVs0vlZk8lGjdunPNaYz1JgarvEBNowxp0i3qDAtLSfMCrwcgg7EB+eLOratNhEObY3msQP1NRUR1r9mldAFmeA7AgQPVdNwsuJW0EYGSBX3Ze7SXixyvneOttcb42b/IZ6ZxTkaKg3t7e7tbW1rfK1DnMb4bbCus1xhivCb1LckEGzW0LBaQLwMYVEm6YARjGOXekMWb34usL/re7oyi6rIxCMcY5dzyAycWASbK5XG4GCk7hin6fAOAcAOMLHKgpqQfA/d6BI21AcoGALMohNg9PqpCwU5JJe7dOciNJ2xbvBbwp/OVyu2Z/BW0zktuUKfe1fuoc7pzbhuQGRWXeD+DRmTNnzs1gdkwMTPdxDsCbq3FR762wuy232xbJ3n7y9Veu87+LJCVZSXeSvMAY80JG7ZwSmK4rByDEZ7YDDXSNIA3ze34mArjHGHM+yX9lyPL+gTwtNF5kVaKhANbDICEPBgHcZq09PUswvCF0UkDSWfl9yFuBZU/F4CF6MfWzlpaWVzOuqzNQ5b0TiA1soYbDLN5PVMHfwq/wt6R51V9ea20E4Hbn3JkkX60D+Osj7HDv7rw6BgD3Ati7QoaRksam5XlC0llrbwLwlIrOU706+irKGAGNMfMAXG2t/WuxjchfgX69XL1RFL0O4PWWlpYFdZqNRwSsvysQn72vBOQ3AYAAwBaIow6kQlEUPYj4JmtSMBf5QVTNQKgXEHk6OiDN8vx+Ky/bQm/GntG0giRa0EPfZ5mRv7WcB2Qxws4XdpM0ttnVwfSdwHTnrBS5fhr3Arg1xSnYpJgOC0jzUaFloVAdO3U1aluDUVxdEZh0lQcHTNFCGeJM3Snpe9VsxrwR0Q6UTjXG1GK7CxVXqzwiUOw9fivipx8q0dmSpid9ZEbSJiQP8i+7scHBsADG+Xe8ks6O4xDHyKpE75B8uiwgJH8hKQSQdb1+neTpJpGc5C2tYhoGqWzJIbY2Dy+we4WA0QEgNIrCZ+60l7pfcSnC7qpPl/SnBJcnCWBo/iGyxsfjU1Gb0A/4DIQ5jgAlrryVsrFcFTqr0bxRVTw7tkQcNSGEzio1mE2JEfEuyl92KaYfSZrShKL8iO/HVHJ1uVFeik5PoA39pTkzREnXAdgqMMs0kt3BgJCcl0B0jZD0cMG/o88JDjl8ajScBCB0KzCP5Mn9FVpuMfuxf3MxRH3bVdJpJC8i+Q5iS60drEiQjHwbl/rH3p5MkP2MSppPf1PxYAC3JahsCuL4s58HP66liJ3Q30W4z8HjJHepVT7+MeFLzjs3YhiKjNaOFxP2zfg0Kh2RsNI+SbsMdlAkPZ2wXw5Js/LtqgBlyiAFIifpHwn746ksGPlVFY/x7znIwOiQ1J2wDz7Mihn6R+WThqu4fJDsMzb3TnVJac2sGXulCqaekxQNYEDOrxKMverB3OgqYmjIN+jgAQZEp6QHqwx5NLWe03dUDRHaHmr0c3lJrf5hnGrpvNXB9JAaw+Zd3wjxC0u0a1v/xm+1dP5qU/klreEDS1Yb82+hpN/6JwZXNxAHSnqzxsCSxzTCiBpZhV5eim6VtFudeV9b0hmS3kuB//0bbao/klKw3+WSzpW0pQ+3lCaPkaT1Je0naXZK/HZL2gANKHsp6aQMwne/4wHaoobw3Yf48N1Kmb/Uw3dnEeB+QwB/R3zJJ21qpAD3P0F80OQaGpC8aABwCYATMfjobQDfJvlcFoUzYzH2ZcTnKZsMAiCWAbiS5KlZVlIXfdmfPN6MgXm8axE/2bQH4qAzmb6sV5eNGclbEb+e/d0BBsb9ANYhOYXkikEZDtDHKp+awiYsS5omaSI+b+RPFu+VNL8BQHhB0qlo0kpwdvbHokuqDJOddA+xzJt9zm5Em1ojATNE0pqSDvCzJ016VdJZkjbyobgbDggOEJBGI379Zxzie9/jEPuLtSN2wWn12tBSxJcn5yO+nPo+4sd13gPwfnH00Uak/wNhlU9eJ0Gl7AAAAABJRU5ErkJggg==',

	    PLACE: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAQAAADa613fAAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQffAQ4MATjKACpiAAAJ40lEQVR42s1caZBVxRX+7n2z4iwwwywgW8lOsKTA0QAlixpFUDGAcUlFKkASoqVVpMRYI6GYiiUBHSEYFSUkllpGjIFQIYyICREmhEEisZAdhm3AgRGGQXhvljfvyw8QgXv6dt9+7+Gc969fn3P669t9+pzu0+0g4cQCXItOKEIxOiIHOXABRHAGp3ACR1GHYzjiRNFWiZks5ANcTTPawznswxy6bQvE7fyE5xhlUGrkCZYxrS1AuJUVPM14aRtnfXsQ0jmN1UwkvcL+VxtEO05lcmgtO109GJPZwuRRlJXMSD6I73Ivk08RlicTRAoX8erRYd4cpHWOMYze2IprjOW2IIwwIoiiGVE4SEcIGcjENcgM0LqnUO7EEvktHD5l1IsxktWczf4MMUSXIbp0LvxcunSZwlxO4hrGDOXtSOygWmc0rmezP7MNJbrsxvGsMpB7mj0TA6I9/6dV9iZHWUov5tM8qJV/f/ww8lir6a/F8ftLvFdrCx+Ld+E74yv+VToJG77X8yvfmTPXUhcd5vMrH8EfsCDB5j2Vk3277dd2YjuyTimyhZOStFZ1ZIUPlGnBBbrcqRS3OXEDStQ9h61K3XcHhbFZadnnJ90NctjPJ7YpCiLqZaWYO6+SY5rDE4oWHDd2UXgTqsS6UdzmrDebtijEEPRHH3RHHrLhIIJ61GAvdmMLDjlNJqMCm1Ai/lWFoQ5N+kI1wUeYzA2msoRlrGK9YExjDHMnX+MYtjPqkEpFW35owvy+gnmYHgZDLOF87meTrxltYS3f5FgauKBKr6KHjvFhBeMIA6W5fIJVhkFXjHv4HDsbfF95LavUMX4pss00gNGN5YwEijia+QZLtHLzFdw/82N6SY6jDWAM5FsMBw6eYvyYd2hlDxN5v/RbVSXrfdoARl++57OI+dNG3uo/++jwVZHzBRXDUrG61kFnFl+2hkHGuJL9rCZ9EztIVXuIahYaTMcZbIgrOo9xKYs1WgaInLOlqvPFrZksLZBR3OfbyBY2s5ktPt8sxpOcrtUzV+TN9qzslNbKHztv6LxVlOMR5d+HsRPbcAhRFOMG9EUvpCpqbsGPnF3+xh11AvfPncWXV3tG3JDRL4Hj+YWil3fzeQ5jB6YzhSGmMZsD+CQ3MSaGTxGWMlWj61GB7+CVlbbbeP/MFA1EjFF+xHHehtHhEL7CiAilitdp9UmO5PDL12QvfWFgdkt4QASynIN9bNwLoplv5lStvhcFvhWXVlgsVCg1iFoe4znxkGCkL19PrhSH4xL9ZpKvW89U8YSjwMDwviPYorN8VDe3eJe4N/Mpu2p1vi3w9QMAF0A2vE71WqdOO7Ly0RPeraDP8YE2WqjEh/BuhRbjWq3Ot4Sysq+B3CSYtbkGHn9XFAqlG3BEy3kWFWj2lLZDb+1W9RqhcAxTzgN5XPhzuwGQAuQKTdzhtGibQ+yAN2RNR2cDrUs8JRnIOg9krOeveueEgchMeI8xz+GYUVB+GrWeshBMdo7/gCsHbhqKAFecYIuMGhMSZkgUjUa8zWgS9g9CBpyHhH2G8YALaRn6o+EZSKunLBVZRrwZwllLTJg3XjoumIkJgItunuIIaowaExZ6PxtdjHjz4T30bMUZg5OpGLxh7s2Aiz6e4gbQqDHHUS/Mm+v1R5l0MQj5nuJGA3sHACskl9JFT8HymFENpHBzJLRhEvIwTjAUYewz0rtOsqAuunt72vDcrgG7hFnSB+Oom7RjcZswZWsESybRbmmouvC6IvsNv0gU6xD2lKZhOib6QWEJpoqG9j/CUJX1ei1ejov2Qs8YkUNsFVeNLngCY1T+FnvjFxghmo7NTsSwC71AMlzBzzppvNdcjU1i+XD8Bo+wvRC/3IMF+IHIs0ex4yz0hWCAM1IEP8t0ssMJcwMmiMNkIBbgHq7ARpxEK4gQsjEId+Mu9FAMlw2GPoEMJDUlzv3/SuzBEPGfDpiI7+EIjqIGrShEF3RFgTJBoQGrncZ4GpKCFo8hzArAfxjrMVjZvBwMwADwwiaHX5RShc3GOh3BNWpxBbuTb47DiaACx32VOnDhwvGFcQ6rnVNxAWl0cVqwOsEGV4WhJ6Cm7eIip6Z0CYg3EgyUMuFEsAp1ccEgKrAn0HTwAjnj4pCnsChgLsM6rI8LyA78JVD6bF+h7KQr+DdZwdrh1ONdwzVZjkyWiU6HmkYLZXUu9noKcxH0HH09/mkNZBfed5oDcUwUOrNBGlqZ6Brwm9RZf5NG/MnZGWhCuRjmKdwEuKgWak8J3KBVWAabXLctWBWQo0gwvstVu3cWvcs7uD/wyUgTZzAUUM9QYef4/PTn3wQVhYGBpHF+wDTaVq5k58B6Xhe647x7yjsFJaMtvskgbgwEpJYPWmjxUgPPe4zMEw7411qoSOFMTarA5Xv2vwueqMwxgqR3v/4zVTwDLLSAUsA/Gx+LbuVQCw3SJvY3C6R4/PuMlbdxn2Hif5SlNrmQmmwhMZGm1gpIOp8zmPKtfM8mmZ8LBFnLL6/yuVDlp1ZQ+nOtNj15P8dZyZaSEy9fHlkqVDlil/LHaTyrGVZW1174uCDrgMnoI6dYKczkPDb6WKu/s5eF1Fw2G6XWiAkDrcyygnID/6UEcoz3WcmcJ0rzto/dxYovWcZK9/O4KC/MUpvLYRwoSpMtK5eIlUdbAcniQvEQ+q/BnRIA4GfGSTXKNKczlt/kOlZ4FsddvN1CksPXxC5+Xs3yW5HhI0soE7j7ilzGWVaL4HCxVXXBLTX5tBWQVM68xBA3czHzLKR0VBiNn/izPahgG2UFpRNfvzi8KvX5i2JwICdnfqxnXaaAcovN8siBF3J3D3CiVVdsU7Smm541W7kej7SC8gCr2cQys5TlK3hV8c1DZuw3KqHYhFspnMlyhaH05/tU0Y5/mwtZqFyXx1itKYGvzDCX9aq4Mpjl3qT0lF5EkokOB/iEZwVBhe1QitoSdOcjIJBnfWCMDS4uX3mDg2y12TYw0lrED30CgCl2HzjP987bWpuoXrNmTPGNY8rsRWdoru/9PnFvNnCIJhx7Nq6bXezAWt/AtYFLdWmuRk7/Pk1wPD3+nmqvtOff0Ds2Xi0AsDNLWaOVPzFRn/0fRi9qzOFA5hhKDLEHv89PDOTW6/OBg0z8J42vgR/kHH7H9xr4Q1xjvBtpfA3c/GJ+T/xXyGFUUWIu5s/AooRezL84HMqv4lMJ1bwxmavu4CuivuRQmPOQfOLDFo/rBHlOZB3Tknsb+NItuMlJglER6C5uQsCkcop2GQtGi9gX3xZxJFfxVNwQPuMv0RaIt3AjzwaeOTFGWMtftbU3tjJYwElcZQhiF2exVyIfCkuCdWA+uqH44tNtWUhDK8I4e/HpthocTfzTbf8Hfxw9eK2CD+8AAAAASUVORK5CYII='
	});
	exports.ICONS = ICONS;

/***/ },
/* 101 */
/***/ function(module, exports, __webpack_require__) {

	"use strict";

	/** controllers */
	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ('value' in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== 'function' && superClass !== null) { throw new TypeError('Super expression must either be null or a function, not ' + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var _commonController = __webpack_require__(89);

	var _commonController2 = _interopRequireDefault(_commonController);

	/** models */

	var _modelsTag = __webpack_require__(102);

	var _modelsTag2 = _interopRequireDefault(_modelsTag);

	/** impls */

	var _implsCloudImpl = __webpack_require__(103);

	var TagCloudController = (function (_Controller) {
	    _inherits(TagCloudController, _Controller);

	    function TagCloudController(options) {
	        _classCallCheck(this, TagCloudController);

	        _get(Object.getPrototypeOf(TagCloudController.prototype), 'constructor', this).call(this, options);
	        this.tagcloud = options.tagcloud;
	        this.ajax_c = options.ajax_c;

	        // impl
	        this.cloudImpl = _implsCloudImpl.cloudImpl;
	    }

	    _createClass(TagCloudController, [{
	        key: 'init',
	        value: function init() {
	            this.addEventHandlers();
	            return this;
	        }
	    }, {
	        key: 'addEventHandlers',
	        value: function addEventHandlers() {
	            var _this = this;

	            // refresh cloud button
	            $(this.tagcloud.cloud.refresh).on('click', function (e) {
	                _this.actionOnRefresh();
	            });

	            // text click event
	            $(this.tagcloud.cloud.el).on('click', this.tagcloud.cloud.text, function (e) {
	                _this.actionOnCloudText(e.target);
	            });
	        }

	        /*************
	         * action bit
	         *************/

	    }, {
	        key: 'actionOnRefresh',
	        value: function actionOnRefresh() {
	            this.refreshCloud();
	        }
	    }, {
	        key: 'actionOnCloudText',
	        value: function actionOnCloudText(el) {
	            var style = $(el).css('text-decoration');
	            if (style != 'line-through') {
	                $(el).css('text-decoration', 'line-through');
	                // remove word
	                this.addRemovedTag(el);
	            } else {
	                $(el).css('text-decoration', 'none');
	                // undo removed word
	                this.undoRemovedTag(el);
	            }
	        }

	        /************
	         * cloud bit
	         ************/

	    }, {
	        key: 'refreshCloud',
	        value: function refreshCloud() {
	            var _this2 = this;

	            // re-setup cloud size in case any changes
	            var cloudEl = this.tagcloud.cloud.el;

	            var w = $(cloudEl).width(),
	                h = $(cloudEl).height();

	            // console.log('cloud size: '+w+', '+h);

	            this.cloudImpl.setup({
	                cloudEl: cloudEl,
	                statusEl: this.tagcloud.cloud.status,
	                width: w,
	                height: h
	            });

	            // fetch and show words
	            this.fetchTags(function (words) {
	                // callback
	                // clear cloud
	                _this2.clearCloud();

	                // no tag found
	                if (words.length <= 0) {
	                    $(_this2.tagcloud.cloud.label).show();
	                    return;
	                }

	                $(_this2.tagcloud.cloud.label).hide();

	                // get removed tags
	                _this2.fetchRemovedTags(words, function (rwords) {
	                    // render cloud
	                    _this2.loadCloud(words, rwords);
	                });
	            });
	        }
	    }, {
	        key: 'clearCloud',
	        value: function clearCloud() {
	            this.cloudImpl.clear();
	        }
	    }, {
	        key: 'loadCloud',
	        value: function loadCloud(words, rwords) {
	            this.cloudImpl.load(words, rwords);
	        }

	        /***********
	         * ajax bit
	         ***********/

	    }, {
	        key: 'fetchTags',
	        value: function fetchTags(callback) {
	            //
	            // ajax call to get tags
	            //
	            this.ajax_c.getTags(this.metadata.getItemId(), callback);
	        }
	    }, {
	        key: 'fetchRemovedTags',
	        value: function fetchRemovedTags(words, callback) {
	            //
	            // ajax call to get removed tags
	            //
	            this.ajax_c.getRemovedTags(this.metadata.getItemId(), callback);
	        }
	    }, {
	        key: 'addRemovedTag',
	        value: function addRemovedTag(el) {
	            //
	            // ajax call to remove word
	            //
	            this.ajax_c.addOrUpdateRemovedTag(new _modelsTag2['default']({
	                docId: this.metadata.getItemId(),
	                name: $(el).text(),
	                raw: -1
	            }), function (resp) {
	                console.log('word removed');
	            });
	        }
	    }, {
	        key: 'undoRemovedTag',
	        value: function undoRemovedTag(el) {
	            //
	            // ajax call to undo removed word
	            //
	            this.ajax_c.addOrUpdateRemovedTag(new _modelsTag2['default']({
	                docId: this.metadata.getItemId(),
	                name: $(el).text(),
	                raw: 1
	            }), function (resp) {
	                console.log('removed word reverted');
	            });
	        }

	        /**
	         *
	         */

	    }, {
	        key: 'openCloud',
	        value: function openCloud() {
	            var _this3 = this;

	            //
	            // cloudImpl width or height cannot be zero, or it causes 100% cpu usage.
	            // the width (auto in css) becomes available only if the tagging panel is visible.
	            // there is a racing condition. A workaround is to set timeout to 0 to make sure that
	            // that width() return a valid value. it is only required for the first load of cloud.
	            //
	            setTimeout(function (e) {
	                _this3.refreshCloud();
	            }, 0);
	        }
	    }, {
	        key: 'closeCloud',
	        value: function closeCloud() {
	            this.clearCloud();
	        }
	    }]);

	    return TagCloudController;
	})(_commonController2['default']);

	exports['default'] = TagCloudController;
	module.exports = exports['default'];

/***/ },
/* 102 */
/***/ function(module, exports) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	var Tag = (function () {
	    function Tag(options) {
	        _classCallCheck(this, Tag);

	        // TODO validate values

	        if (options.hasOwnProperty('tags')) {
	            //
	            // tag resource is in json format
	            //
	            var tag = options.tag;

	            this.name = tag.name;
	            this.raw = tag.raw;
	            this.value = tag.value;
	        } else {
	            //
	            // tag resource is from cloud
	            //
	            this.docId = options.docId;

	            this.name = options.name;
	            this.raw = options.raw || 0;
	            this.value = options.value || 0.0;
	        }
	    }

	    _createClass(Tag, [{
	        key: 'getDocumentId',
	        value: function getDocumentId() {
	            return this.docId;
	        }
	    }, {
	        key: 'getName',
	        value: function getName() {
	            return this.name;
	        }
	    }, {
	        key: 'getRaw',
	        value: function getRaw() {
	            return this.raw;
	        }
	    }, {
	        key: 'getValue',
	        value: function getValue() {
	            return this.value;
	        }
	    }]);

	    return Tag;
	})();

	exports['default'] = Tag;
	module.exports = exports['default'];

/***/ },
/* 103 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	var _d3Cloud = __webpack_require__(104);

	var _d3Cloud2 = _interopRequireDefault(_d3Cloud);

	/**
	 * derived from Json Davies's wordcloud.
	 * @see https://www.jasondavies.com/wordcloud/cloud.min.js
	 */

	var cloudImpl = {

	    fill: {},
	    W: 0,
	    H: 0,
	    words: [],
	    scale: 1,

	    tags: {},
	    fontSize: [],

	    layout: {},
	    svg: {},
	    background: {},
	    vis: {},

	    complete: 0,
	    statusText: {},
	    maxZoom: 3,
	    maxLen: 100, // max number of word displayed
	    rwords: [], // removed keywords
	    total: 0,

	    // From Jonathan Feinberg's cue.language, see lib/cue.language/license.txt.
	    // wordSeparators : /[\s\u3031-\u3035\u309b\u309c\u30a0\u30fc\uff70]+/g,

	    setup: function setup(options) {
	        var self = this;

	        this.W = options.width || 500;
	        this.H = options.height || 500;

	        // status element
	        this.statusText = d3.select(options.statusEl);

	        // d3 cloud
	        this.layout = (0, _d3Cloud2['default'])().timeInterval(10).size([this.W, this.H]).rotate(0).fontSize(function (d) {
	            return self.fontSize(+d.value);
	        }).text(function (d) {
	            return d.key;
	        }).on('word', this._progress).on('end', this._draw);

	        // d3
	        if (!d3.select(options.cloudEl).select('svg').empty())
	            // remove existing svg
	            d3.select(options.cloudEl).select('svg').remove();

	        this.svg = d3.select(options.cloudEl).append('svg').attr('width', this.W).attr('height', this.H);

	        this.background = this.svg.append('g');

	        this.vis = this.svg.append('g').attr('transform', 'translate(' + [this.W >> 1, this.H >> 1] + ')');

	        // color fill
	        this.fill = d3.scale.category20b();
	    },

	    clear: function clear() {
	        this.tags = {};
	        this.rwords = [];
	        this.svg.selectAll('text').remove();
	    },

	    load: function load(words, rwords) {
	        this._parseArray(words);
	        this.rwords = rwords;
	        this.total = words.length;
	    },

	    _progress: function _progress() {
	        cloudImpl.statusText.text(++cloudImpl.complete + '/' + cloudImpl.total);
	    },

	    _parseArray: function _parseArray(array) {
	        if (array.length > 0) {
	            this.tags = {};
	            var cases = {};
	            for (var i = 0; i < array.length; i++) {
	                var word = array[i];
	                word.name = word.name.replace(punctuation, "");
	                cases[word.name.toLowerCase()] = word.name;
	                this.tags[word.name.toLowerCase()] = word.raw;
	            }

	            var ar = d3.entries(this.tags);
	            this.tags = d3.shuffle(ar);

	            this.tags.forEach(function (d) {
	                d.key = cases[d.key];
	            });

	            this.tags = this.tags.slice(0, this.maxLen);
	        }

	        this._generate();
	    },

	    _generate: function _generate() {
	        // cloudImpl.layout.font('Impact').spiral('archimedean');
	        this.layout.font('Impact').spiral('archimedean');
	        this.fontSize = d3.scale['log']().range([10, 100]);

	        if (this.tags.length) {
	            var m = this._largest(this.tags);
	            var n = this._smallest(this.tags);
	            this.fontSize.domain([n, m]);
	        }

	        this.complete = 0;
	        this.statusText.style('display', null);

	        this.words = [];

	        // cloudImpl.layout.stop().words( this.tags ).start();
	        this.layout.stop().words(this.tags).start();
	    },

	    _draw: function _draw(data, bounds) {
	        cloudImpl.scale = bounds ? Math.min(cloudImpl.W / Math.abs(bounds[1].x - cloudImpl.W / 2), cloudImpl.W / Math.abs(bounds[0].x - cloudImpl.W / 2), cloudImpl.H / Math.abs(bounds[1].y - cloudImpl.H / 2), cloudImpl.H / Math.abs(bounds[0].y - cloudImpl.H / 2)) / 2 : 1;

	        cloudImpl.words = data;

	        var text = cloudImpl.vis.selectAll('text').data(cloudImpl.words, function (d) {
	            return d.text.toLowerCase();
	        });

	        text.transition().duration(1000).attr('transform', function (d) {
	            return 'translate(' + [d.x, d.y] + ')';
	        }).style('font-size', function (d) {
	            return d.size + 'px';
	        });

	        text.enter().append('text').attr('text-anchor', 'middle').attr('transform', function (d) {
	            return 'translate(' + [d.x, d.y] + ')';
	        }).style('font-size', function (d) {
	            return d.size + 'px';
	        }).on('click', function (d) {

	            ////////////////////////////////////////////////

	            // cache.modWords = [];

	            // var effect = $(this).css('text-decoration');
	            // if (effect != 'line-through') { // strikethrough
	            //     $(this).css('text-decoration', 'line-through');
	            //     // this.setAttribute('style', this.getAttribute('style') + ' text-decoration: line-through;');

	            //     // update removed keywords
	            //     // -1 if removing the word
	            //     //
	            //     cache.addModWords(d.text, -1);
	            // } else { // revoke
	            //     $(this).css('text-decoration', 'none');

	            //     // update removed keywords
	            //     // 1 if changing mind
	            //     //
	            //     cache.addModWords(d.text, 1);
	            // }

	            // ajax.updateRemovedTags();

	            ////////////////////////////////////////////////

	        }).style('opacity', 1e-6).transition().duration(1000).style('opacity', 1);

	        text.style('font-family', function (d) {
	            return d.font;
	        }).style('fill', function (d) {
	            return cloudImpl.fill(d.text.toLowerCase());
	        }).text(function (d) {
	            return d.text;
	        });

	        var exitGroup = cloudImpl.background.append('g').attr('transform', cloudImpl.vis.attr('transform'));

	        var exitGroupNode = exitGroup.node();

	        text.exit().each(function () {
	            exitGroupNode.appendChild(this);
	        });

	        exitGroup.transition().duration(1000).style('opacity', 1e-6).remove();

	        // zoom behavior
	        cloudImpl.svg.call(d3.behavior.zoom().translate([cloudImpl.W >> 1, cloudImpl.H >> 1]).scale(cloudImpl.scale).scaleExtent([cloudImpl.scale, cloudImpl.scale * 3]).on('zoom', cloudImpl._zoom));

	        cloudImpl.vis.transition().delay(1000).duration(750).attr('transform', 'translate(' + [cloudImpl.W >> 1, cloudImpl.H >> 1] + ')scale(' + cloudImpl.scale + ')').call(cloudImpl._endAll, function () {

	            //
	            // mark removed keywords
	            //

	            if (cloudImpl.rwords.length > 0) {
	                cloudImpl.svg.selectAll('text').each(function (d) {
	                    var text = d.text;
	                    for (var j = 0; j < cloudImpl.rwords.length; j++) {
	                        var rword = cloudImpl.rwords[j];
	                        if (rword.name == text) {
	                            $(this).css('text-decoration', 'line-through');
	                        }
	                    }
	                });
	            }
	        });
	    },

	    _zoom: function _zoom() {
	        // this scope changes
	        cloudImpl.vis.attr('transform', 'translate(' + d3.event.translate + ')scale(' + d3.event.scale + ')');
	    },

	    _largest: function _largest(tags) {
	        var tmp = 0;
	        for (var i = 0; i < tags.length; i++) {
	            var tag = tags[i];
	            if (tag.value > tmp) tmp = tag.value;
	        }
	        return tmp;
	    },

	    _smallest: function _smallest(tags) {
	        var tmp = 10000;
	        for (var i = 0; i < tags.length; i++) {
	            var tag = tags[i];
	            if (tag.value < tmp) tmp = tag.value;
	        }
	        return tmp;
	    },

	    _endAll: function _endAll(transition, callback) {
	        var n = 0;
	        if (transition.empty()) {
	            callback();
	        } else {
	            transition.each(function () {
	                ++n;
	            }).each('end', function () {
	                if (! --n) {
	                    callback.apply(this, arguments);
	                }
	            });
	        }
	    }

	};

	exports.cloudImpl = cloudImpl;
	// shuffle : function(o) {
	//     for(var j, x, i = o.length; i; j = Math.floor(Math.random() * i), x = o[--i], o[i] = o[j], o[j] = x);
	//     return o;
	// },

	// isEmpty : function (obj) {
	//     for (var prop in obj) {
	//         if (obj.hasOwnProperty(prop))
	//             return false;
	//     }
	//     return true;
	// },

	var stopWords = /^(i|me|my|myself|we|us|our|ours|ourselves|you|your|yours|yourself|yourselves|he|him|his|himself|she|her|hers|herself|it|its|itself|they|them|their|theirs|themselves|what|which|who|whom|whose|this|that|these|those|am|is|are|was|were|be|been|being|have|has|had|having|do|does|did|doing|will|would|should|can|could|ought|i'm|you're|he's|she's|it's|we're|they're|i've|you've|we've|they've|i'd|you'd|he'd|she'd|we'd|they'd|i'll|you'll|he'll|she'll|we'll|they'll|isn't|aren't|wasn't|weren't|hasn't|haven't|hadn't|doesn't|don't|didn't|won't|wouldn't|shan't|shouldn't|can't|cannot|couldn't|mustn't|let's|that's|who's|what's|here's|there's|when's|where's|why's|how's|a|an|the|and|but|if|or|because|as|until|while|of|at|by|for|with|about|against|between|into|through|during|before|after|above|below|to|from|up|upon|down|in|out|on|off|over|under|again|further|then|once|here|there|when|where|why|how|all|any|both|each|few|more|most|other|some|such|no|nor|not|only|own|same|so|than|too|very|say|says|said|shall)$/,
	    wordSeparators = /[\s\u3031-\u3035\u309b\u309c\u30a0\u30fc\uff70]+/g,
	    unicodePunctuationRe = '!-#%-*,-/:;?@\\[-\\]_{}¡§«¶·»¿;·՚-՟։֊־׀׃׆׳״؉؊،؍؛؞؟٪-٭۔܀-܍߷-߹࠰-࠾࡞।॥॰૰෴๏๚๛༄-༒༔༺-༽྅࿐-࿔࿙࿚၊-၏჻፠-፨᐀᙭᙮᚛᚜᛫-᛭᜵᜶។-៖៘-៚᠀-᠊᥄᥅᨞᨟᪠-᪦᪨-᪭᭚-᭠᯼-᯿᰻-᰿᱾᱿᳀-᳇᳓‐-‧‰-⁃⁅-⁑⁓-⁞⁽⁾₍₎〈〉❨-❵⟅⟆⟦-⟯⦃-⦘⧘-⧛⧼⧽⳹-⳼⳾⳿⵰⸀-⸮⸰-⸻、-〃〈-】〔-〟〰〽゠・꓾꓿꘍-꘏꙳꙾꛲-꛷꡴-꡷꣎꣏꣸-꣺꤮꤯꥟꧁-꧍꧞꧟꩜-꩟꫞꫟꫰꫱꯫﴾﴿︐-︙︰-﹒﹔-﹡﹣﹨﹪﹫！-＃％-＊，-／：；？＠［-］＿｛｝｟-･',
	    punctuation = new RegExp("[" + unicodePunctuationRe + "]", "g");

/***/ },
/* 104 */
/***/ function(module, exports, __webpack_require__) {

	// Word cloud layout by Jason Davies, https://www.jasondavies.com/wordcloud/
	// Algorithm due to Jonathan Feinberg, http://static.mrfeinberg.com/bv_ch03.pdf

	var dispatch = __webpack_require__(105).dispatch;

	var cloudRadians = Math.PI / 180,
	    cw = 1 << 11 >> 5,
	    ch = 1 << 11;

	module.exports = function() {
	  var size = [256, 256],
	      text = cloudText,
	      font = cloudFont,
	      fontSize = cloudFontSize,
	      fontStyle = cloudFontNormal,
	      fontWeight = cloudFontNormal,
	      rotate = cloudRotate,
	      padding = cloudPadding,
	      spiral = archimedeanSpiral,
	      words = [],
	      timeInterval = Infinity,
	      event = dispatch("word", "end"),
	      timer = null,
	      random = Math.random,
	      cloud = {},
	      canvas = cloudCanvas;

	  cloud.canvas = function(_) {
	    return arguments.length ? (canvas = functor(_), cloud) : canvas;
	  };

	  cloud.start = function() {
	    var contextAndRatio = getContext(canvas()),
	        board = zeroArray((size[0] >> 5) * size[1]),
	        bounds = null,
	        n = words.length,
	        i = -1,
	        tags = [],
	        data = words.map(function(d, i) {
	          d.text = text.call(this, d, i);
	          d.font = font.call(this, d, i);
	          d.style = fontStyle.call(this, d, i);
	          d.weight = fontWeight.call(this, d, i);
	          d.rotate = rotate.call(this, d, i);
	          d.size = ~~fontSize.call(this, d, i);
	          d.padding = padding.call(this, d, i);
	          return d;
	        }).sort(function(a, b) { return b.size - a.size; });

	    if (timer) clearInterval(timer);
	    timer = setInterval(step, 0);
	    step();

	    return cloud;

	    function step() {
	      var start = Date.now();
	      while (Date.now() - start < timeInterval && ++i < n && timer) {
	        var d = data[i];
	        d.x = (size[0] * (random() + .5)) >> 1;
	        d.y = (size[1] * (random() + .5)) >> 1;
	        cloudSprite(contextAndRatio, d, data, i);
	        if (d.hasText && place(board, d, bounds)) {
	          tags.push(d);
	          event.word(d);
	          if (bounds) cloudBounds(bounds, d);
	          else bounds = [{x: d.x + d.x0, y: d.y + d.y0}, {x: d.x + d.x1, y: d.y + d.y1}];
	          // Temporary hack
	          d.x -= size[0] >> 1;
	          d.y -= size[1] >> 1;
	        }
	      }
	      if (i >= n) {
	        cloud.stop();
	        event.end(tags, bounds);
	      }
	    }
	  }

	  cloud.stop = function() {
	    if (timer) {
	      clearInterval(timer);
	      timer = null;
	    }
	    return cloud;
	  };

	  function getContext(canvas) {
	    canvas.width = canvas.height = 1;
	    var ratio = Math.sqrt(canvas.getContext("2d").getImageData(0, 0, 1, 1).data.length >> 2);
	    canvas.width = (cw << 5) / ratio;
	    canvas.height = ch / ratio;

	    var context = canvas.getContext("2d");
	    context.fillStyle = context.strokeStyle = "red";
	    context.textAlign = "center";

	    return {context: context, ratio: ratio};
	  }

	  function place(board, tag, bounds) {
	    var perimeter = [{x: 0, y: 0}, {x: size[0], y: size[1]}],
	        startX = tag.x,
	        startY = tag.y,
	        maxDelta = Math.sqrt(size[0] * size[0] + size[1] * size[1]),
	        s = spiral(size),
	        dt = random() < .5 ? 1 : -1,
	        t = -dt,
	        dxdy,
	        dx,
	        dy;

	    while (dxdy = s(t += dt)) {
	      dx = ~~dxdy[0];
	      dy = ~~dxdy[1];

	      if (Math.min(Math.abs(dx), Math.abs(dy)) >= maxDelta) break;

	      tag.x = startX + dx;
	      tag.y = startY + dy;

	      if (tag.x + tag.x0 < 0 || tag.y + tag.y0 < 0 ||
	          tag.x + tag.x1 > size[0] || tag.y + tag.y1 > size[1]) continue;
	      // TODO only check for collisions within current bounds.
	      if (!bounds || !cloudCollide(tag, board, size[0])) {
	        if (!bounds || collideRects(tag, bounds)) {
	          var sprite = tag.sprite,
	              w = tag.width >> 5,
	              sw = size[0] >> 5,
	              lx = tag.x - (w << 4),
	              sx = lx & 0x7f,
	              msx = 32 - sx,
	              h = tag.y1 - tag.y0,
	              x = (tag.y + tag.y0) * sw + (lx >> 5),
	              last;
	          for (var j = 0; j < h; j++) {
	            last = 0;
	            for (var i = 0; i <= w; i++) {
	              board[x + i] |= (last << msx) | (i < w ? (last = sprite[j * w + i]) >>> sx : 0);
	            }
	            x += sw;
	          }
	          delete tag.sprite;
	          return true;
	        }
	      }
	    }
	    return false;
	  }

	  cloud.timeInterval = function(_) {
	    return arguments.length ? (timeInterval = _ == null ? Infinity : _, cloud) : timeInterval;
	  };

	  cloud.words = function(_) {
	    return arguments.length ? (words = _, cloud) : words;
	  };

	  cloud.size = function(_) {
	    return arguments.length ? (size = [+_[0], +_[1]], cloud) : size;
	  };

	  cloud.font = function(_) {
	    return arguments.length ? (font = functor(_), cloud) : font;
	  };

	  cloud.fontStyle = function(_) {
	    return arguments.length ? (fontStyle = functor(_), cloud) : fontStyle;
	  };

	  cloud.fontWeight = function(_) {
	    return arguments.length ? (fontWeight = functor(_), cloud) : fontWeight;
	  };

	  cloud.rotate = function(_) {
	    return arguments.length ? (rotate = functor(_), cloud) : rotate;
	  };

	  cloud.text = function(_) {
	    return arguments.length ? (text = functor(_), cloud) : text;
	  };

	  cloud.spiral = function(_) {
	    return arguments.length ? (spiral = spirals[_] || _, cloud) : spiral;
	  };

	  cloud.fontSize = function(_) {
	    return arguments.length ? (fontSize = functor(_), cloud) : fontSize;
	  };

	  cloud.padding = function(_) {
	    return arguments.length ? (padding = functor(_), cloud) : padding;
	  };

	  cloud.random = function(_) {
	    return arguments.length ? (random = _, cloud) : random;
	  };

	  cloud.on = function() {
	    var value = event.on.apply(event, arguments);
	    return value === event ? cloud : value;
	  };

	  return cloud;
	};

	function cloudText(d) {
	  return d.text;
	}

	function cloudFont() {
	  return "serif";
	}

	function cloudFontNormal() {
	  return "normal";
	}

	function cloudFontSize(d) {
	  return Math.sqrt(d.value);
	}

	function cloudRotate() {
	  return (~~(Math.random() * 6) - 3) * 30;
	}

	function cloudPadding() {
	  return 1;
	}

	// Fetches a monochrome sprite bitmap for the specified text.
	// Load in batches for speed.
	function cloudSprite(contextAndRatio, d, data, di) {
	  if (d.sprite) return;
	  var c = contextAndRatio.context,
	      ratio = contextAndRatio.ratio;

	  c.clearRect(0, 0, (cw << 5) / ratio, ch / ratio);
	  var x = 0,
	      y = 0,
	      maxh = 0,
	      n = data.length;
	  --di;
	  while (++di < n) {
	    d = data[di];
	    c.save();
	    c.font = d.style + " " + d.weight + " " + ~~((d.size + 1) / ratio) + "px " + d.font;
	    var w = c.measureText(d.text + "m").width * ratio,
	        h = d.size << 1;
	    if (d.rotate) {
	      var sr = Math.sin(d.rotate * cloudRadians),
	          cr = Math.cos(d.rotate * cloudRadians),
	          wcr = w * cr,
	          wsr = w * sr,
	          hcr = h * cr,
	          hsr = h * sr;
	      w = (Math.max(Math.abs(wcr + hsr), Math.abs(wcr - hsr)) + 0x1f) >> 5 << 5;
	      h = ~~Math.max(Math.abs(wsr + hcr), Math.abs(wsr - hcr));
	    } else {
	      w = (w + 0x1f) >> 5 << 5;
	    }
	    if (h > maxh) maxh = h;
	    if (x + w >= (cw << 5)) {
	      x = 0;
	      y += maxh;
	      maxh = 0;
	    }
	    if (y + h >= ch) break;
	    c.translate((x + (w >> 1)) / ratio, (y + (h >> 1)) / ratio);
	    if (d.rotate) c.rotate(d.rotate * cloudRadians);
	    c.fillText(d.text, 0, 0);
	    if (d.padding) c.lineWidth = 2 * d.padding, c.strokeText(d.text, 0, 0);
	    c.restore();
	    d.width = w;
	    d.height = h;
	    d.xoff = x;
	    d.yoff = y;
	    d.x1 = w >> 1;
	    d.y1 = h >> 1;
	    d.x0 = -d.x1;
	    d.y0 = -d.y1;
	    d.hasText = true;
	    x += w;
	  }
	  var pixels = c.getImageData(0, 0, (cw << 5) / ratio, ch / ratio).data,
	      sprite = [];
	  while (--di >= 0) {
	    d = data[di];
	    if (!d.hasText) continue;
	    var w = d.width,
	        w32 = w >> 5,
	        h = d.y1 - d.y0;
	    // Zero the buffer
	    for (var i = 0; i < h * w32; i++) sprite[i] = 0;
	    x = d.xoff;
	    if (x == null) return;
	    y = d.yoff;
	    var seen = 0,
	        seenRow = -1;
	    for (var j = 0; j < h; j++) {
	      for (var i = 0; i < w; i++) {
	        var k = w32 * j + (i >> 5),
	            m = pixels[((y + j) * (cw << 5) + (x + i)) << 2] ? 1 << (31 - (i % 32)) : 0;
	        sprite[k] |= m;
	        seen |= m;
	      }
	      if (seen) seenRow = j;
	      else {
	        d.y0++;
	        h--;
	        j--;
	        y++;
	      }
	    }
	    d.y1 = d.y0 + seenRow;
	    d.sprite = sprite.slice(0, (d.y1 - d.y0) * w32);
	  }
	}

	// Use mask-based collision detection.
	function cloudCollide(tag, board, sw) {
	  sw >>= 5;
	  var sprite = tag.sprite,
	      w = tag.width >> 5,
	      lx = tag.x - (w << 4),
	      sx = lx & 0x7f,
	      msx = 32 - sx,
	      h = tag.y1 - tag.y0,
	      x = (tag.y + tag.y0) * sw + (lx >> 5),
	      last;
	  for (var j = 0; j < h; j++) {
	    last = 0;
	    for (var i = 0; i <= w; i++) {
	      if (((last << msx) | (i < w ? (last = sprite[j * w + i]) >>> sx : 0))
	          & board[x + i]) return true;
	    }
	    x += sw;
	  }
	  return false;
	}

	function cloudBounds(bounds, d) {
	  var b0 = bounds[0],
	      b1 = bounds[1];
	  if (d.x + d.x0 < b0.x) b0.x = d.x + d.x0;
	  if (d.y + d.y0 < b0.y) b0.y = d.y + d.y0;
	  if (d.x + d.x1 > b1.x) b1.x = d.x + d.x1;
	  if (d.y + d.y1 > b1.y) b1.y = d.y + d.y1;
	}

	function collideRects(a, b) {
	  return a.x + a.x1 > b[0].x && a.x + a.x0 < b[1].x && a.y + a.y1 > b[0].y && a.y + a.y0 < b[1].y;
	}

	function archimedeanSpiral(size) {
	  var e = size[0] / size[1];
	  return function(t) {
	    return [e * (t *= .1) * Math.cos(t), t * Math.sin(t)];
	  };
	}

	function rectangularSpiral(size) {
	  var dy = 4,
	      dx = dy * size[0] / size[1],
	      x = 0,
	      y = 0;
	  return function(t) {
	    var sign = t < 0 ? -1 : 1;
	    // See triangular numbers: T_n = n * (n + 1) / 2.
	    switch ((Math.sqrt(1 + 4 * sign * t) - sign) & 3) {
	      case 0:  x += dx; break;
	      case 1:  y += dy; break;
	      case 2:  x -= dx; break;
	      default: y -= dy; break;
	    }
	    return [x, y];
	  };
	}

	// TODO reuse arrays?
	function zeroArray(n) {
	  var a = [],
	      i = -1;
	  while (++i < n) a[i] = 0;
	  return a;
	}

	function cloudCanvas() {
	  return document.createElement("canvas");
	}

	function functor(d) {
	  return typeof d === "function" ? d : function() { return d; };
	}

	var spirals = {
	  archimedean: archimedeanSpiral,
	  rectangular: rectangularSpiral
	};


/***/ },
/* 105 */
/***/ function(module, exports, __webpack_require__) {

	(function (global, factory) {
	   true ? factory(exports) :
	  typeof define === 'function' && define.amd ? define('d3-dispatch', ['exports'], factory) :
	  factory((global.d3_dispatch = {}));
	}(this, function (exports) { 'use strict';

	  function dispatch() {
	    return new Dispatch(arguments);
	  }

	  function Dispatch(types) {
	    var i = -1,
	        n = types.length,
	        callbacksByType = {},
	        callbackByName = {},
	        type,
	        that = this;

	    that.on = function(type, callback) {
	      type = parseType(type);

	      // Return the current callback, if any.
	      if (arguments.length < 2) {
	        return (callback = callbackByName[type.name]) && callback.value;
	      }

	      // If a type was specified…
	      if (type.type) {
	        var callbacks = callbacksByType[type.type],
	            callback0 = callbackByName[type.name],
	            i;

	        // Remove the current callback, if any, using copy-on-remove.
	        if (callback0) {
	          callback0.value = null;
	          i = callbacks.indexOf(callback0);
	          callbacksByType[type.type] = callbacks = callbacks.slice(0, i).concat(callbacks.slice(i + 1));
	          delete callbackByName[type.name];
	        }

	        // Add the new callback, if any.
	        if (callback) {
	          callback = {value: callback};
	          callbackByName[type.name] = callback;
	          callbacks.push(callback);
	        }
	      }

	      // Otherwise, if a null callback was specified, remove all callbacks with the given name.
	      else if (callback == null) {
	        for (var otherType in callbacksByType) {
	          if (callback = callbackByName[otherType + type.name]) {
	            callback.value = null;
	            var callbacks = callbacksByType[otherType], i = callbacks.indexOf(callback);
	            callbacksByType[otherType] = callbacks.slice(0, i).concat(callbacks.slice(i + 1));
	            delete callbackByName[callback.name];
	          }
	        }
	      }

	      return that;
	    };

	    while (++i < n) {
	      type = types[i] + "";
	      if (!type || (type in that)) throw new Error("illegal or duplicate type: " + type);
	      callbacksByType[type] = [];
	      that[type] = applier(type);
	    }

	    function parseType(type) {
	      var i = (type += "").indexOf("."), name = type;
	      if (i >= 0) type = type.slice(0, i); else name += ".";
	      if (type && !callbacksByType.hasOwnProperty(type)) throw new Error("unknown type: " + type);
	      return {type: type, name: name};
	    }

	    function applier(type) {
	      return function() {
	        var callbacks = callbacksByType[type], // Defensive reference; copy-on-remove.
	            callback,
	            callbackValue,
	            i = -1,
	            n = callbacks.length;

	        while (++i < n) {
	          if (callbackValue = (callback = callbacks[i]).value) {
	            callbackValue.apply(this, arguments);
	          }
	        }

	        return that;
	      };
	    }
	  }

	  dispatch.prototype = Dispatch.prototype;

	  var version = "0.2.4";

	  exports.version = version;
	  exports.dispatch = dispatch;

	}));

/***/ },
/* 106 */
/***/ function(module, exports, __webpack_require__) {

	
	/**
	 * Author: Hal Blackburn
	 */

	'use strict';

	Object.defineProperty(exports, '__esModule', {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { 'default': obj }; }

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

	var _util = __webpack_require__(81);

	var _util2 = _interopRequireDefault(_util);

	var _lodashLang = __webpack_require__(5);

	var _lodashLang2 = _interopRequireDefault(_lodashLang);

	var _utilsExceptions = __webpack_require__(107);

	/**
	 * Metadata wraps cudl JSON metadata objects to provide accessor methods and
	 * error handling.
	 */

	var Metadata = (function () {
	    function Metadata(metadata, itemId) {
	        _classCallCheck(this, Metadata);

	        if (!_lodashLang2['default'].isObject(metadata)) throw new _utilsExceptions.ValueError(_util2['default'].format('metadata must be an object, got: %s', metadata));
	        this.metadata = metadata;
	        this.itemId = itemId;
	    }

	    /** @return true if the classmark/file ID of this metadata is known. */

	    _createClass(Metadata, [{
	        key: 'hasItemId',
	        value: function hasItemId() {
	            return this.itemId !== undefined;
	        }

	        /** Get the classmark/file ID of this metadata item. */
	    }, {
	        key: 'getItemId',
	        value: function getItemId() {
	            return this.itemId;
	        }

	        /**
	         * Enumerate the logical structures as flat sequence.
	         */
	    }, {
	        key: 'getFlattenedLogicalStructures',
	        value: function getFlattenedLogicalStructures() {
	            // Even though we have es6 generators, not much supports them, so we
	            // kinda have to use arrays....
	            var entries = [];
	            var _iteratorNormalCompletion = true;
	            var _didIteratorError = false;
	            var _iteratorError = undefined;

	            try {
	                for (var _iterator = this._getFlattenedLogicalStructures(this.metadata.logicalStructures, [])[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
	                    var entry = _step.value;

	                    entries.push(entry);
	                }
	            } catch (err) {
	                _didIteratorError = true;
	                _iteratorError = err;
	            } finally {
	                try {
	                    if (!_iteratorNormalCompletion && _iterator['return']) {
	                        _iterator['return']();
	                    }
	                } finally {
	                    if (_didIteratorError) {
	                        throw _iteratorError;
	                    }
	                }
	            }

	            return entries;
	        }
	    }, {
	        key: '_getFlattenedLogicalStructures',
	        value: regeneratorRuntime.mark(function _getFlattenedLogicalStructures(children, parents) {
	            var _iteratorNormalCompletion2, _didIteratorError2, _iteratorError2, _iterator2, _step2, structure, _iteratorNormalCompletion3, _didIteratorError3, _iteratorError3, _iterator3, _step3, subStructure;

	            return regeneratorRuntime.wrap(function _getFlattenedLogicalStructures$(context$2$0) {
	                while (1) switch (context$2$0.prev = context$2$0.next) {
	                    case 0:
	                        _iteratorNormalCompletion2 = true;
	                        _didIteratorError2 = false;
	                        _iteratorError2 = undefined;
	                        context$2$0.prev = 3;
	                        _iterator2 = children[Symbol.iterator]();

	                    case 5:
	                        if (_iteratorNormalCompletion2 = (_step2 = _iterator2.next()).done) {
	                            context$2$0.next = 39;
	                            break;
	                        }

	                        structure = _step2.value;
	                        context$2$0.next = 9;
	                        return [structure, parents];

	                    case 9:
	                        if (!(structure.children && structure.children.length)) {
	                            context$2$0.next = 36;
	                            break;
	                        }

	                        _iteratorNormalCompletion3 = true;
	                        _didIteratorError3 = false;
	                        _iteratorError3 = undefined;
	                        context$2$0.prev = 13;
	                        _iterator3 = this._getFlattenedLogicalStructures(structure.children, (0, _lodashLang2['default'])(parents).concat([structure]).value())[Symbol.iterator]();

	                    case 15:
	                        if (_iteratorNormalCompletion3 = (_step3 = _iterator3.next()).done) {
	                            context$2$0.next = 22;
	                            break;
	                        }

	                        subStructure = _step3.value;
	                        context$2$0.next = 19;
	                        return subStructure;

	                    case 19:
	                        _iteratorNormalCompletion3 = true;
	                        context$2$0.next = 15;
	                        break;

	                    case 22:
	                        context$2$0.next = 28;
	                        break;

	                    case 24:
	                        context$2$0.prev = 24;
	                        context$2$0.t0 = context$2$0['catch'](13);
	                        _didIteratorError3 = true;
	                        _iteratorError3 = context$2$0.t0;

	                    case 28:
	                        context$2$0.prev = 28;
	                        context$2$0.prev = 29;

	                        if (!_iteratorNormalCompletion3 && _iterator3['return']) {
	                            _iterator3['return']();
	                        }

	                    case 31:
	                        context$2$0.prev = 31;

	                        if (!_didIteratorError3) {
	                            context$2$0.next = 34;
	                            break;
	                        }

	                        throw _iteratorError3;

	                    case 34:
	                        return context$2$0.finish(31);

	                    case 35:
	                        return context$2$0.finish(28);

	                    case 36:
	                        _iteratorNormalCompletion2 = true;
	                        context$2$0.next = 5;
	                        break;

	                    case 39:
	                        context$2$0.next = 45;
	                        break;

	                    case 41:
	                        context$2$0.prev = 41;
	                        context$2$0.t1 = context$2$0['catch'](3);
	                        _didIteratorError2 = true;
	                        _iteratorError2 = context$2$0.t1;

	                    case 45:
	                        context$2$0.prev = 45;
	                        context$2$0.prev = 46;

	                        if (!_iteratorNormalCompletion2 && _iterator2['return']) {
	                            _iterator2['return']();
	                        }

	                    case 48:
	                        context$2$0.prev = 48;

	                        if (!_didIteratorError2) {
	                            context$2$0.next = 51;
	                            break;
	                        }

	                        throw _iteratorError2;

	                    case 51:
	                        return context$2$0.finish(48);

	                    case 52:
	                        return context$2$0.finish(45);

	                    case 53:
	                    case 'end':
	                        return context$2$0.stop();
	                }
	            }, _getFlattenedLogicalStructures, this, [[3, 41, 45, 53], [13, 24, 28, 36], [29,, 31, 35], [46,, 48, 52]]);
	        })
	    }, {
	        key: 'pages',
	        get: function get() {
	            return this.metadata.pages;
	        }
	    }]);

	    return Metadata;
	})();

	exports['default'] = Metadata;
	module.exports = exports['default'];

/***/ },
/* 107 */
/***/ function(module, exports) {

	
	/**
	 * Author: Hal Blackburn
	 */

	"use strict";

	Object.defineProperty(exports, "__esModule", {
	    value: true
	});

	var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

	var _get = function get(_x, _x2, _x3) { var _again = true; _function: while (_again) { var object = _x, property = _x2, receiver = _x3; _again = false; if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { _x = parent; _x2 = property; _x3 = receiver; _again = true; desc = parent = undefined; continue _function; } } else if ("value" in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } } };

	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

	function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

	var BaseError = (function (_Error) {
	    _inherits(BaseError, _Error);

	    _createClass(BaseError, [{
	        key: "name",
	        get: function get() {
	            return Object.getPrototypeOf(this).constructor.name;
	        }
	    }]);

	    function BaseError(message) {
	        _classCallCheck(this, BaseError);

	        _get(Object.getPrototypeOf(BaseError.prototype), "constructor", this).call(this, message);
	        this.message = message;

	        if (Error.captureStackTrace) {
	            Error.captureStackTrace(this, BaseError);
	        } else {
	            this.stack = new Error().stack;
	        }
	    }

	    return BaseError;
	})(Error);

	var KeyError = (function (_BaseError) {
	    _inherits(KeyError, _BaseError);

	    function KeyError() {
	        _classCallCheck(this, KeyError);

	        _get(Object.getPrototypeOf(KeyError.prototype), "constructor", this).apply(this, arguments);
	    }

	    return KeyError;
	})(BaseError);

	exports.KeyError = KeyError;

	var NotImplementedError = (function (_BaseError2) {
	    _inherits(NotImplementedError, _BaseError2);

	    function NotImplementedError() {
	        _classCallCheck(this, NotImplementedError);

	        _get(Object.getPrototypeOf(NotImplementedError.prototype), "constructor", this).apply(this, arguments);
	    }

	    return NotImplementedError;
	})(BaseError);

	exports.NotImplementedError = NotImplementedError;

	var ValueError = (function (_BaseError3) {
	    _inherits(ValueError, _BaseError3);

	    function ValueError() {
	        _classCallCheck(this, ValueError);

	        _get(Object.getPrototypeOf(ValueError.prototype), "constructor", this).apply(this, arguments);
	    }

	    return ValueError;
	})(BaseError);

	exports.ValueError = ValueError;

	var IllegalStateException = (function (_BaseError4) {
	    _inherits(IllegalStateException, _BaseError4);

	    function IllegalStateException() {
	        _classCallCheck(this, IllegalStateException);

	        _get(Object.getPrototypeOf(IllegalStateException.prototype), "constructor", this).apply(this, arguments);
	    }

	    return IllegalStateException;
	})(BaseError);

	exports.IllegalStateException = IllegalStateException;

/***/ }
/******/ ]);