
FROM tomcat:9.0.30-jdk11-openjdk

ARG CUDL_GLOBAL_PROPERTIES_PATH
ARG JAVA_OPTS

RUN echo "Using cudl-global.properties at:  ${CUDL_GLOBAL_PROPERTIES_PATH}"

COPY ./target/FoundationsViewer.war /usr/local/tomcat/webapps/ROOT.war
COPY ./docker/tomcat-context.xml /usr/local/tomcat/conf/Catalina/localhost/ROOT.xml
COPY ${CUDL_GLOBAL_PROPERTIES_PATH} /etc/cudl-viewer/cudl-global.properties

COPY ./docker/startup.sh /usr/local/startup.sh
RUN chmod u+x /usr/local/startup.sh

CMD ["/usr/local/startup.sh", "/mnt/cudl-data/", "catalina.sh", "run"]
