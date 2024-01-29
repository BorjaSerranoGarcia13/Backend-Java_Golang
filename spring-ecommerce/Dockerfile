FROM openjdk:17-jdk-buster as build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY wait-for-db.sh .
COPY src/main/resources/data/ src/main/resources/data/

RUN chmod +x wait-for-db.sh

RUN ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw package -DskipTests

FROM openjdk:17-jdk-buster

RUN apt-get update && apt-get install -y default-mysql-client

VOLUME /tmp
COPY --from=build /workspace/app/target/spring-ecommerce-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /workspace/app/wait-for-db.sh wait-for-db.sh
COPY --from=build /workspace/app/src/main/resources/data/ /app/src/main/resources/data/

RUN chmod +x wait-for-db.sh

EXPOSE 8080
ENTRYPOINT ["./wait-for-db.sh", "db", "java","-jar","/app.jar"]