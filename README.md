# CUDL Viewer

The CUDL Viewer is a [Spring](https://spring.io/)-powered java web application.

## Developing

The Viewer uses [Maven](https://maven.apache.org/) for dependency management
of 3rd party Java libraries, and build automation. Non-java internal
dependencies are not managed as Maven dependencies and need to be setup
manually.

Ensure you have a recent JDK and version of Maven installed before continuing.

### Setup

Before running the Viewer, some setup and configuration is needed to obtain
dependencies not manged by Maven.

#### Required Configuration

The Viewer is configured in the `cudl-global.properties` file. A template is
available at `src/test/resources/cudl-global.properties`. Copy this to
`src/main/resources`:

```
$ cp src/test/resources/cudl-global.properties src/main/resources/
```

Go through the file, updating defaults as desired. Most of the defaults are
acceptable to get the Viewer running, but some must be changed:

* `cudl-viewer-content.html.path`
* `cudl-viewer-content.images.path`

These must point to the `html/` and `images/` directories in a local checkout of
the [cudl-viewer-content](https://bitbucket.org/CUDL/cudl-viewer-content)
repository.

```
$ git clone git@bitbucket.org:CUDL/cudl-viewer-content.git
[...]
$ cd cudl-viewer-content/
$ ls
html  images
$ pwd
/home/hal/projects/cudl-viewer/cudl-viewer-content
```

In the above example, the following settings would be used:

```
cudl-viewer-content.html.path=/home/hal/projects/cudl-viewer/cudl-viewer-content/html
cudl-viewer-content.images.path=/home/hal/projects/cudl-viewer/cudl-viewer-content/images
```

### Semi-optional configuration

The above settings are sufficient to get the Viewer running, but you'll see
numerous stack traces in the console if you don't configure the admin settings,
even if `admin.enabled=false`.

1. Clone [cudl-data](https://bitbucket.org/CUDL/cudl-data), and set
   `admin.git.json.localpath` to the clone's location
2. Set `admin.git.content.localpath` to the location of the cudl-viewer-content
   clone you made in the previous section
3. Clone [snapshots](https://bitbucket.org/CUDL/snapshots), and set
   `admin.git.db.localpath` to the clone's location

### Running

Once configured, the Viewer can be run using an embedded Tomcat 7 server by
executing:

```
$ mvn tomcat7:run
```

You can then access the Viewer at
[http://localhost:1111/](http://localhost:1111/).


## Building and tagging

To build a WAR file, run:

```
$ mvn clean package
```

The war file will be created under `target/`.

When deployed, the Viewer requires `cudl-global.properties` to exist on on the
classpath.
	
This file will be excluded from any WAR file generated as it contains the properties
that vary between systems (DEV, BETA, LIVE etc). This file should be copied into the 
classpath for your web container (e.g. `lib` directory in Tomcat).
	
## More information

For more information, see the [CUDL Wiki](https://wiki.cam.ac.uk/cudl-docs/).
