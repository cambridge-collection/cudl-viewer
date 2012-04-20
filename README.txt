Just want to see the application (using the default properties)?  Use the command

mvn jetty:run 

Will start a jetty web container on port 1111 which you can access
from the URL:

http://localhost:1111

--------------------------------------------------------------

Want to specify your own properties, for example your own instance of XTF or 
place to put the cached files:

cp /src/test/resources/cudl-global.properties /src/main/resources/cudl-global.properties

edit the cudl-global.properties in main resources to reflect your 
local system and run the command:

mvn jetty:run 

Will start a jetty web container on port 1111 which you can access
from the URL:

http://localhost:1111

--------------------------------------------------------------

Want to build a WAR file for deployment?

This project requires the following file on the classpath:

	cudl-global.properties
	
This file will be excluded from any WAR file generated as it contains the properties
that vary between systems (DEV, BETA, LIVE etc). This file should be copied into the 
classpath for your web container (e.g. lib directory in tomcat).

Then you can run the command:

mvn clean package

To generate the war file, which will be placed in the target directory.  
	
--------------------------------------------------------------

For more information see src/main/docs/building-foundations-viewer.txt.

This application requires jdk 1.6+, maven 2.2.1+. 

