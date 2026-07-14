FROM eclipse-temurin:22-jdk AS buildstage

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src /app/src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk

COPY --from=buildstage /app/target/SumativaCloudConsumidor-1.0.0.jar /app/app.jar

EXPOSE 8081

RUN mkdir -p /app/data

CMD ["java", "-jar", "/app/app.jar"]