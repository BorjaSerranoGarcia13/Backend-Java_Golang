# Imagen base
FROM openjdk:17-jdk as build

# Directorio de trabajo
WORKDIR /workspace/app

# Copiar el archivo pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Descargar dependencias
RUN ./mvnw dependency:go-offline -B

# Copiar el resto del código fuente
COPY src src

# Construir la aplicación
RUN ./mvnw package -DskipTests

# Etapa de ejecución
FROM openjdk:17-jdk
VOLUME /tmp
COPY --from=build /workspace/app/target/spring-ecommerce-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]