#!/bin/sh

set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPO_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/.." && pwd)
UI_ROOT=$(CDPATH= cd -- "$REPO_ROOT/../cudl-viewer-ui" 2>/dev/null && pwd || true)
UI_POM="$UI_ROOT/pom.xml"
COMPOSE_FILE="$REPO_ROOT/docker-compose-hot.yml"
UI_ENDPOINT="${CUDL_UI_DEV_ENDPOINT:-http://localhost:8080/js/page-standard.js}"

fail() {
    printf 'Error: %s\n' "$1" >&2
    exit 1
}

if [ "$#" -ne 1 ] || [ -z "$1" ]; then
    fail "ENV_FILE is required. Run 'make dev ENV_FILE=sample-data.env' or supply another readable environment file."
fi

case "$1" in
    /*) ENV_FILE=$1 ;;
    *) ENV_FILE="$REPO_ROOT/$1" ;;
esac

[ -r "$ENV_FILE" ] || fail \
    "Environment file '$1' is not readable. Pass a readable path with 'make dev ENV_FILE=<path>'."

[ -n "$UI_ROOT" ] && [ -r "$UI_POM" ] || fail \
    "The sibling cudl-viewer-ui repository was not found at '$REPO_ROOT/../cudl-viewer-ui'. Clone it there and run 'make dev' in it first."

extract_ui_version() {
    if command -v xmllint >/dev/null 2>&1; then
        xmllint --xpath \
            'string(/*[local-name()="project"]/*[local-name()="version"][1])' \
            "$UI_POM"
    elif command -v python3 >/dev/null 2>&1; then
        python3 - "$UI_POM" <<'PY'
import sys
import xml.etree.ElementTree as ET

root = ET.parse(sys.argv[1]).getroot()
namespace = root.tag.partition("}")[0] + "}" if root.tag.startswith("{") else ""
version = root.find(namespace + "version")
if version is not None and version.text:
    print(version.text.strip(), end="")
PY
    else
        fail "The UI Maven version cannot be read because neither xmllint nor python3 is available. Install an XML parser and retry."
    fi
}

UI_VERSION=$(extract_ui_version)
[ -n "$UI_VERSION" ] || fail \
    "No project version was found in '$UI_POM'. Check that the UI pom.xml is valid."

command -v java >/dev/null 2>&1 || fail \
    "Java was not found. JDK 11 is required; verify the active runtime with 'java -version'."

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2; exit}')
JAVA_MAJOR=$(printf '%s\n' "$JAVA_VERSION" | awk -F. '{if ($1 == "1") print $2; else print $1}')
[ "$JAVA_MAJOR" = "11" ] || fail \
    "Java ${JAVA_VERSION:-could not be detected} is active, but JDK 11 is required. Activate JDK 11 and verify it with 'java -version'."

command -v javac >/dev/null 2>&1 || fail \
    "javac was not found. A full JDK 11 is required; verify the active compiler with 'javac -version'."

JAVAC_VERSION=$(javac -version 2>&1 | awk '/^javac / {print $2; exit}')
JAVAC_MAJOR=$(printf '%s\n' "$JAVAC_VERSION" | awk -F. '{if ($1 == "1") print $2; else print $1}')
[ "$JAVAC_MAJOR" = "11" ] || fail \
    "javac ${JAVAC_VERSION:-could not be detected} is active, but javac 11 is required. Activate JDK 11 and verify it with 'javac -version'."

command -v mvn >/dev/null 2>&1 || fail \
    "Maven was not found. Maven 3.6.3 or newer is required; verify it with 'mvn --version'."

MAVEN_OUTPUT=$(mvn --version 2>&1)
MAVEN_VERSION=$(printf '%s\n' "$MAVEN_OUTPUT" | awk '/Apache Maven/ {print $3; exit}')
MAVEN_JAVA_VERSION=$(printf '%s\n' "$MAVEN_OUTPUT" | awk -F '[:, ]+' '/Java version:/ {print $3; exit}')
MAVEN_JAVA_MAJOR=$(printf '%s\n' "$MAVEN_JAVA_VERSION" | awk -F. '{if ($1 == "1") print $2; else print $1}')

printf '%s\n' "$MAVEN_VERSION" | awk -F. '
    {
        if ($1 > 3 ||
            ($1 == 3 && $2 > 6) ||
            ($1 == 3 && $2 == 6 && $3 >= 3)) exit 0
        exit 1
    }
' || fail \
    "Maven ${MAVEN_VERSION:-could not be detected} is active, but Maven 3.6.3 or newer is required. Verify it with 'mvn --version'."

[ "$MAVEN_JAVA_MAJOR" = "11" ] || fail \
    "Maven is using Java ${MAVEN_JAVA_VERSION:-could not be detected}, but Java 11 is required. Activate JDK 11 and verify Maven's runtime with 'mvn --version'."

command -v docker >/dev/null 2>&1 || fail \
    "Docker was not found. Docker with Compose v2 is required; verify it with 'docker --version'."

docker info >/dev/null 2>&1 || fail \
    "Docker is installed but its daemon is unavailable. Start Docker and verify it with 'docker info'."

docker compose version >/dev/null 2>&1 || fail \
    "Docker Compose v2 is unavailable. Verify it with 'docker compose version'."

COMPOSE_CONFIG=$(mktemp "${TMPDIR:-/tmp}/cudl-viewer-compose.XXXXXX")
trap 'rm -f "$COMPOSE_CONFIG"' EXIT HUP INT TERM

if ! docker compose --file "$COMPOSE_FILE" --env-file "$ENV_FILE" \
        config >"$COMPOSE_CONFIG"; then
    fail "The environment file '$1' is not valid for docker-compose-hot.yml. Check it with 'docker compose --file docker-compose-hot.yml --env-file \"$1\" config'."
fi

mount_source() {
    awk -v wanted="$1" '
        $1 == "source:" {
            source = substr($0, index($0, "source:") + 8)
            gsub(/^ +| +$/, "", source)
            gsub(/^"|"$/, "", source)
        }
        $1 == "target:" {
            target = substr($0, index($0, "target:") + 8)
            gsub(/^ +| +$/, "", target)
            gsub(/^"|"$/, "", target)
            if (target == wanted) {
                print source
                exit
            }
        }
    ' "$COMPOSE_CONFIG"
}

check_file_mount() {
    source_path=$(mount_source "$1")
    [ -n "$source_path" ] || fail \
        "The selected Compose configuration does not define the required '$1' mount."
    [ -f "$source_path" ] || fail \
        "The required file for '$1' does not exist at '$source_path'. Check '$ENV_FILE'."
}

check_directory_mount() {
    source_path=$(mount_source "$1")
    [ -n "$source_path" ] || fail \
        "The selected Compose configuration does not define the required '$1' mount."
    [ -d "$source_path" ] || fail \
        "The required directory for '$1' does not exist at '$source_path'. Check '$ENV_FILE'."
}

check_file_mount "/etc/cudl-viewer/cudl-global.properties"
check_directory_mount "/srv/cudl-viewer/cudl-data"
check_file_mount "/srv/cudl-viewer/cudl-data/cudl.ui.json5"
check_file_mount "/srv/cudl-viewer/cudl-data/cudl.dl-dataset.json"

MAVEN_REPO=$(mvn -q help:evaluate -Dexpression=settings.localRepository \
    -DforceStdout 2>/dev/null || true)
[ -n "$MAVEN_REPO" ] || fail \
    "Maven's local repository could not be determined. Check 'mvn --version' and your Maven settings."

UI_ARTIFACT_DIR="$MAVEN_REPO/ulcambridge/foundations/viewer/viewer-ui/$UI_VERSION"
UI_JAR="$UI_ARTIFACT_DIR/viewer-ui-$UI_VERSION.jar"
UI_RESOURCES_JAR="$UI_ARTIFACT_DIR/viewer-ui-$UI_VERSION-resources.jar"

[ -f "$UI_JAR" ] && [ -f "$UI_RESOURCES_JAR" ] || fail \
    "The cudl-viewer-ui $UI_VERSION artefacts are not installed in '$MAVEN_REPO'. Run 'make dev' in '$UI_ROOT' first."

command -v curl >/dev/null 2>&1 || fail \
    "curl was not found. It is required to check the UI development endpoint '$UI_ENDPOINT'."

curl --fail --silent --show-error --max-time 3 --output /dev/null \
    "$UI_ENDPOINT" || fail \
    "The UI development endpoint '$UI_ENDPOINT' is unavailable. Run 'make dev' in '$UI_ROOT' first."

VIEWER_PORT=$(awk '
    $1 == "target:" && $2 == "8080" {viewer_port = 1; next}
    viewer_port && $1 == "published:" {
        gsub(/"/, "", $2)
        print $2
        exit
    }
' "$COMPOSE_CONFIG")

if [ -n "$VIEWER_PORT" ] && command -v lsof >/dev/null 2>&1 && \
        lsof -nP -iTCP:"$VIEWER_PORT" -sTCP:LISTEN >/dev/null 2>&1; then
    fail "Viewer port $VIEWER_PORT is already in use. Stop the process using it and verify the port with 'lsof -nP -iTCP:$VIEWER_PORT -sTCP:LISTEN'."
fi

MAVEN_CHECK=$(mktemp "${TMPDIR:-/tmp}/cudl-viewer-maven.XXXXXX")
if ! (cd "$REPO_ROOT" && mvn -q \
        -Dcudl-viewer-ui.version="$UI_VERSION" validate) \
        >"$MAVEN_CHECK" 2>&1; then
    tail -20 "$MAVEN_CHECK" >&2
    rm -f "$MAVEN_CHECK"
    fail "Maven validation failed. Check that Maven can use the configured openjdk 11 toolchain and access required packages; verify with 'mvn -Dcudl-viewer-ui.version=$UI_VERSION validate'."
fi
rm -f "$MAVEN_CHECK"

printf '%s\n' "$UI_VERSION"
