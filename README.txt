This project requires the following file in  src/main/resources:

cudl-global.properties

This can be copied from the src/main/test folder and edited to match the machine you are running on
It defines properties that vary between deployments, so that the exact
same war file is deployed to DEV and LIVE. This reduces the possibility of introducing bugs 
between build environments. For more information see the documentation for deploying to DEV and 
deploying to LIVE. 

Once this file is in place and edited the application can be tested locally by running 
the command:

mvn jetty:run 

Which will start up a jetty server on port 1111 so you should be able to access the 
site from the URL: 

http://localhost:1111

This application requires jdk 1.6+, maven 2.2.1+. 

Further documentation can be found at: src/main/docs 
