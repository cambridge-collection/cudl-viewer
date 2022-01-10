# CUDL Viewer

The CUDL Viewer is a [Spring](https://spring.io/)-powered java web application.

## Building

To build a WAR file, run:

```
$ mvn clean package
```

The war file will be created under `target/`.

When deployed, the Viewer requires `cudl-global.properties` to exist on the
classpath.

This file will be excluded from any WAR file generated as it contains the properties
that vary between systems (DEV, BETA, LIVE etc). This file should be copied into the
classpath for your web container (e.g. `lib` directory in Tomcat).

## Run locally

### Using sample data

The sample data is linked as a git submodule so we need to initalise
it and download the data.  Do this with the following commands:

    git submodule init
    git submodule update

Check the git submodules are present: dl-data-samples should be at:

    docker/db/dl-data-samples

To run the viewer:

    docker-compose --env-file sample-data.env up

### Using cudl data

Check the cudl database snapshots are available here:
(NOTE: These are held in Bitbucket and are available to Cambridge developers)

    docker/db/snapshots

To run the viewer:

    docker-compose --env-file cudl-data.env up

Note: if switching between data sets you may have to remove the old cudl-db images.
e.g.

    docker image rm cudl-viewer-master_cudl-db:latest


# Below sections require reviewing!, they may be out of date

## Developing

The Viewer uses [Maven](https://maven.apache.org/) for dependency management
of 3rd party Java libraries, and build automation. Non-java internal
dependencies are not managed as Maven dependencies and need to be setup
manually.

Ensure you have a recent JDK and version of Maven installed before continuing.

You'll need to
[add credentials for the CUDL Maven repository](https://wiki.cam.ac.uk/cudl-docs/CUDL_Maven_Repository#Credentials)
in order to resolve internal dependencies such as
[cudl-viewer-ui](https://github.com/cambridge-collection/cudl-viewer-ui).

### Database Setup

See the [database setup](src/main/docs/database-setup.md) document for
instructions on creating a Postgres database for use with CUDL Viewer. It also
covers importing data and creating admin users.


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
the [cudl-viewer-content](https://github.com/cambridge-collection/cudl-viewer-content)
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

1. Clone [cudl-data](https://github.com/cambridge-collection/cudl-data), and set
   `admin.git.json.localpath` to the clone's location
2. Set `admin.git.content.localpath` to the location of the cudl-viewer-content
   clone you made in the previous section
3. Clone [snapshots](https://github.com/cambridge-collection/snapshots), and set
   `admin.git.db.localpath` to the clone's location

### Running

Once configured, there are two embedded Tomcat servers that can be run. One will allow live editing of static resources (e.g. jsps) and can be run using the command:

```
$ mvn tomcat7:run
```

Alternatively you can run the alternative embedded tomcat through that Cargo container that is setup to use the same version tomcat as the live/staging/dev servers for final testing.  This does not allow live editing. To run this you use the command:

```
$ mvn clean verify cargo:run
```

For both servers, when running you can then access the Viewer at
[http://localhost:1111/](http://localhost:1111/).

### Live updates from `cudl-viewer-ui`

All the Javascript, CSS etc used by the viewer comes from `cudl-viewer-ui`
via a Maven dependency. You can make live changes to the UI Javascript etc
without rebuilding each time by using the UI's devserver.

This requires setting the `cudl.ui.dev` property to `true` in
`cudl-global.properties`. You can also set `cudl.ui.dev.baseUrl` if the default
of `http://localhost:8080/` is not right for you.

Once enabled, the viewer will link to the UI's devserver instead of serving
Javascript/CSS from the dependency JARs.

See the [cudl-viewer-ui's README](https://github.com/cambridge-collection/cudl-viewer-ui) for
instructions on running the UI devserver.



## Making a release

Releasing is using the Maven release plugin.

### Tag format

Releases are tagged using the old CARET Ops tag format, which is
`<server-class>-<date-yyyymmdd><release-in-day>`. `<release-in-day>` is a two
digit number, starting from `00` which increments after each release in one day.

For example, for the 1st release on `21/06/2015` for a production deployment
we'd use `production-2015062100`.

Note that the last two digits make tags a pain to auto-generate, so you'll have
to manually specify the tag value each time you tag.

### Step 1: Prepare

After committing and testing all changes, switch to the `master` branch and
run:

```
$ mvn release:prepare
```

You'll be prompted for the version and tag to use for the release, and the
version to use for the next version. Use a tag in the above format for the first
two, and accept the default for the next version, which should be
`1.0-SNAPSHOT`.

After this finishes, you'll have two new commits on your `master` branch and
a new tag in your local repo. You need to push all of these to the CUDL repo.
Assuming your CUDL remote is `cudl`, and your created tag was
`production-2015062100`, you'd run:

```
$ git push cudl master production-2015062100
```

### Step 2: CAMBRIDGE ONLY: deploy to Maven repo on S3

Once the release has been tagged you can upload this version to our internal s3 repo
which is where the code is deployed from using Puppet.

First ensure that you have the aws credentials in your ~/.m2/settings.xml

```
  <servers>
	<server>
	<id>github</id>
	<username>YOUR_GITHUB_USERNAME</username>
	<password>YOUR_GITHUB_ACCESS_TOKEN></password>
	</server>
	<server>
	<id>cudl-aws-release</id>
	<username>YOUR_AWS_ACCESS_TOKEN_NAME</username>
	<password>YOUR_AWS_ACCESS_TOKEN_VALUE</password>
	</server>
  </servers>
```

Then run the following command to upload to the s3 repository for release
so that puppet can find and deploy the code.

```
$mvn release:stage -DstagingRepository=s3://mvn.cudl.lib.cam.ac.uk/release
```

You then need to **repeat the release:prepare (stage 1)** with the same info for releasing
to the github repo.

### Step 3: Perform
Once the release has been tagged you can finish off by deploying the artifacts
to the CUDL packages repository in github using the command:

```
$ mvn release:perform
```

## More information

For more information, see the [Documentation](https://cambridge-collection.github.io).
