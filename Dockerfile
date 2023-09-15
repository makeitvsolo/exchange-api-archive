FROM maven:3.9.4-eclipse-temurin-17-alpine as build
WORKDIR .
COPY pom.xml ./pom.xml
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn package

FROM tomcat:10.1.13-jre17-temurin-jammy as deploy
COPY --from=build ./target/*.war $CATALINA_HOME/webapps/exchange-api.war
CMD ["catalina.sh", "run"]
