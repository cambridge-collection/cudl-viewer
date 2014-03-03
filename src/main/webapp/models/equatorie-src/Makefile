# Variables
BIN = ./node_modules/.bin
COFFEE = ${BIN}/coffee
BROWSERIFY = ${BIN}/browserify
UGLIFY = ${BIN}/uglifyjs
DAEMON = ./watch.sh

# Targets
default: web

deps: 
	if test -d "node_modules"; then echo "dependencies installed"; else npm install; fi
  
clean:
	if [ -e "build/equatorie.js" ]; then rm build/equatorie.js; fi
	rm -rf build

# compile the NPM library version to JavaScript
build: clean
	${COFFEE} -o build -c src/

# Watch a directory then hit web build
watch: clean
	${DAEMON} src make
  
# compiles the NPM version files into a combined minified web .js library
web: build
	${BROWSERIFY} build/equatorie.js > build/equatorie_full.js
	${UGLIFY} build/equatorie_full.js > build/equatorie.min.js
	cp build/equatorie_full.js html/js/equatorie.js
	cp build/equatorie.min.js html/js/equatorie.min.js
	cp build/physics.js html/js/physics.js

docs:
	docco src/*.coffee

test: build
	mocha --compilers coffee:coffee-script

dist: deps web

publish: dist
	npm publish
