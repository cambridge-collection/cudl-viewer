# CUDL Viewer

The CUDL viewer provides a frontend display for the [Cambridge Digital Library](https://cudl.lib.cam.ac.uk/),
allowing you to view books, manuscripts and other content with a full zoomable viewer and detailed metadata
display.

It relies on data converted from [standard TEI](https://github.com/tei-for-special-collections/consolidated-schema) into a JSON format for display. This is done by Cambridge using [XSLT
transformations](https://github.com/cambridge-collection/xslt-transformation-engine).  We have some [example data](https://github.com/cambridge-collection/dl-data-samples)
for you to have a look and get started.

The viewer uses Java Spring and maven, you can see more instructions on getting the dependencies setup in our
github documentation. **To build and run this
you will need to be able to access public packages on GitHub using maven - full instructions to do this are on this page.**
- https://cambridge-collection.github.io/setup-local-viewer.html.

![CUDLViewer.png](src/main/docs/images/CUDLViewer.png)

## Building and Running

### Using sample data

Once you have setup the required  [dependencies](https://cambridge-collection.github.io/setup-local-viewer.html) you are
ready to build and run the CUDL viewer.

First make sure you have a copy of this repository using git:

    git clone git@github.com:cambridge-collection/cudl-viewer.git

The sample data is linked as a git submodule so we need to initalise
it and download the data.  Do this with the following commands:

    git submodule init
    git submodule update

Check the git data submodule are present: dl-data-samples should be at the directory:

    data/dl-data-samples


### Building the application

To build the application into a WAR packaged file, to run locally run:

    mvn clean package

The war file will be created under `target/`.

To run the viewer:

    docker-compose --env-file sample-data.env up

When running you can then access the Viewer at
[http://localhost:8888/](http://localhost:8888/).


### Configuration

The Viewer is configured in the `cudl-global.properties` file. A template is
available at `docker/cudl-global.properties`.

Go through the file, updating defaults as desired. Most of the defaults are
acceptable to get the Viewer running, but some must be changed:

* `cudl-viewer-content.html.path`
* `cudl-viewer-content.images.path`

These must point to the `html/` and `images/` directories in a local checkout of
the data you're using (see above for details).

## Development

It uses the separate repository cudl-viewer-ui for all the javascript and css dependencies,
so this is a good thing to look at if you want to start customising your viewer instance.

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


# Cambridge Development

## Using Cambridge CUDL data

Download a sample of the cudl data from s3 at e.g. s3://staging-cul-cudl-data-releases or from Bitbucket.
This should be placed in a separate directory at the same level as the cudl-data-viewer.

    cd ..
    git clone git@bitbucket.org:CUDL/cudl-data-releases.git cudl-data-releases

To run the viewer:

    docker-compose --env-file cudl-data.env up

## Deployment
When deployed, the Viewer requires `cudl-global.properties` to exist on the
classpath.

This file will be excluded from any WAR file generated as it contains the properties
that vary between systems (DEV, BETA, LIVE etc). This file should be copied into the
classpath for your web container (e.g. `lib` directory in Tomcat).

## Making a release

Releasing is using the Maven release plugin.

### Tag format

Releases are tagged using the old CARET Ops tag format, which is
`<server-class>-<date-yyyymmdd><release-in-day>`. `<release-in-day>` is a two
digit number, starting from `00` which increments after each release in one day.

For example, for the 1st release on `21/06/2024` for a production deployment
we'd use `production-2024062100`.

Note that the last two digits make tags a pain to auto-generate, so you'll have
to manually specify the tag value each time you tag.

### Step 1: Prepare

After committing and testing all changes, switch to the `main` branch and
run:

```
$ mvn release:prepare
```

You'll be prompted for the version and tag to use for the release, and the
version to use for the next version. Use a tag in the above format for the first
two. The third must be
`1.0-SNAPSHOT`.

After this finishes, you'll have two new commits on your `main` branch and
a new tag in your local repo. You need to push all of these to the CUDL repo.
Assuming your CUDL remote is `cudl`, and your created tag was
`production-2024062100`, you'd run:

```
$ git push cudl main production-2024062100
```

### Step 2: Perform

Once the release has been tagged you can finish off by deploying the artifacts
to the CUDL packages repository in GitHub using the command:

```
$ mvn release:perform
```

Once you have performed the release, the latest version of your application will be automatically built into a Docker
image using AWS CodeDeploy and is then available in the [sandbox ECR](https://eu-west-1.console.aws.amazon.com/ecr/repositories/private/563181399728/sandbox-cudl-viewer?region=eu-west-1).

## More information

For more information, see the [Documentation](https://cambridge-collection.github.io).
