FROM tomcat:9.0.30-jdk11-openjdk

COPY target/FoundationsViewer.war /usr/local/tomcat/webapps/ROOT.war
COPY docker/tomcat-context.xml /usr/local/tomcat/conf/Catalina/localhost/ROOT.xml
