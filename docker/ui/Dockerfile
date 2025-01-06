FROM maven:3.9.9-amazoncorretto-11 AS maven

ARG GITHUB_USER GITHUB_TOKEN

WORKDIR /opt/build

COPY pom.xml toolchains.xml settings.xml .
COPY src ./src/

RUN mvn -s settings.xml -t toolchains.xml clean package;

FROM tomcat:9.0.30-jdk11-openjdk AS tomcat

ENV JAVA_OPTS="-server -Djava.awt.headless=true -Xms512m -Xmx2560m" \
    VIEWER_USER="digilib" \
    VIEWER_UID="1729" \
    VIEWER_GROUP="digilib" \
    VIEWER_GID="1729"

COPY --from=maven /opt/build/target/FoundationsViewer.war $CATALINA_HOME/webapps/ROOT.war
COPY docker/ui/server.xml $CATALINA_HOME/conf/server.xml
COPY docker/ui/context.xml $CATALINA_HOME/conf/Catalina/localhost/ROOT.xml
COPY docker/ui/entrypoint.sh /usr/local/bin/entrypoint.sh

RUN set -ex; \
    curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"; \
    unzip awscliv2.zip; \
    ./aws/install; \
    aws --version;

RUN set -ex; \
    chmod 0755 /usr/local/bin/entrypoint.sh; \
    groupadd -r --gid "$VIEWER_GID" "$VIEWER_GROUP"; \
    useradd -r --uid "$VIEWER_UID" --gid "$VIEWER_GID" "$VIEWER_USER"; \
    chown -R $VIEWER_USER:$VIEWER_GROUP $CATALINA_HOME; \
    mkdir -p /etc/cudl-viewer; \
    chown -R $VIEWER_USER:$VIEWER_GROUP /etc/cudl-viewer;

USER digilib

ENTRYPOINT [ "entrypoint.sh" ]
CMD [ "catalina.sh", "run" ]
