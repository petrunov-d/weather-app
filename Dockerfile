FROM eclipse-temurin:17
VOLUME /tmp
ADD target/weatherapp-0.0.1-SNAPSHOT.jar weatherapp.jar
RUN sh -c 'touch /weatherapp.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/weatherapp.jar"]