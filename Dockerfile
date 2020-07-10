
FROM tomcat:9.0.30-jdk11-openjdk

COPY ./target/FoundationsViewer.war /usr/local/tomcat/webapps/ROOT.war
COPY ./docker/tomcat-context.xml /usr/local/tomcat/conf/Catalina/localhost/ROOT.xml
COPY ./docker/cudl-global.properties /etc/cudl-viewer/cudl-global.properties

COPY ./docker/startup.sh /usr/local/startup.sh
RUN chmod u+x /usr/local/startup.sh

CMD ["/usr/local/startup.sh", "/srv/cudl-viewer/cudl-data/", "catalina.sh", "run"]
