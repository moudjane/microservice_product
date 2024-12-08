FROM maven:4.0.0-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk21
WORKDIR /usr/local/tomcat/webapps/

COPY --from=build /app/target/*.war .

EXPOSE 8080

# Lancer Tomcat
CMD ["catalina.sh", "run"]
