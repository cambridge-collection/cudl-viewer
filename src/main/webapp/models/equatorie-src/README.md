Equatorie
=========

The Model of the Equatorie

This project is made up of a set of node.js libraries and a bunch of coffeescript source files.

Inside the main directory is a 'make' Makefile file. Have a look at this. If you have all the node dependencies (found in the package.json file) you can just run 'make' and it will build the Equatorie files inside the html directory.

The final site can be found inside the html directory and is all self contained. Inside here there is:

css - all the css including some twitter bootstrap libraries and custom css
fonts - some basic web fonts
js - all the js including the minimised versions of the final source code
models - all the model data for the equatorie including the json files for the models
shaders - the GLSL shaders we are using
index.html - where everything is pulled together

outside this directory are all the things we need to create the project:

src - the files in coffeescript that make up the project
lib - lib files included in the build - basically just the coffeegl js library
useful - basic source, including Seb's notes
node_modules - modules we need to build everything 