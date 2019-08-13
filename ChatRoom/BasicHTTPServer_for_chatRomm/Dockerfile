FROM java:8 
WORKDIR /
ADD target/BasicHTTPServer-0.0.1-SNAPSHOT.jar ChatServer.jar 
ADD resource/ resource/
EXPOSE 8080 
CMD java -jar ChatServer.jar