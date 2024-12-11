###################
### build stage ###
###################
FROM maven:3.8.6-amazoncorretto-17 AS build
WORKDIR /usr/app/
COPY pom.xml /usr/app
COPY src /usr/app/src
RUN mvn clean install -DskipTests -Dmaven.javadoc.skip=true

########################
### production stage ###
########################
FROM amazoncorretto:17-alpine3.13 AS production
WORKDIR /usr/app/
COPY --from=build /usr/app/target/springboot-mysql-docker.jar /usr/app/target/
CMD [ "sh", "-c", "java -jar /usr/app/target/springboot-mysql-docker.jar" ]