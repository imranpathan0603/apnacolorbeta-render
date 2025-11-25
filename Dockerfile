#FROM maven:3.8.5-openjdk-17 AS build
#COPY . .
#RUN mvn clean package -DskipTests
#
#FROM openjdk:17.0.1-jdk-slim
##COPY --from=build /*
#COPY --from=build /app/target/ApnaColor-0.0.1-SNAPSHOT.jar app.jar
#EXPOSE 8080
#ENTRYPOINT [ "java","-jar","demo.jar" ]



# === Stage 1: Build the JAR ===
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# === Stage 2: Run Application ===
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/ApnaColor-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
