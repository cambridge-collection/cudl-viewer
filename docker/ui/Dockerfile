FROM tomcat:9.0.30-jdk11-openjdk

ENV JAVA_OPTS="-server -Djava.awt.headless=true -Xms512m -Xmx2560m" \
    VIEWER_USER="digilib" \
    VIEWER_UID="1729" \
    VIEWER_GROUP="digilib" \
    VIEWER_GID="1729"

COPY target/FoundationsViewer.war $CATALINA_HOME/webapps/ROOT.war
COPY docker/ui/server.xml $CATALINA_HOME/conf/server.xml
COPY docker/ui/context.xml $CATALINA_HOME/conf/Catalina/localhost/ROOT.xml

RUN set -ex; \
    groupadd -r --gid "$VIEWER_GID" "$VIEWER_GROUP"; \
    useradd -r --uid "$VIEWER_UID" --gid "$VIEWER_GID" "$VIEWER_USER"; \
    chown -R $VIEWER_USER:$VIEWER_GROUP $CATALINA_HOME;

USER digilib
