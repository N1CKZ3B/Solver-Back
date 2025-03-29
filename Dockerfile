# Etapa 1: construir el proyecto con Maven
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagen liviana para ejecutar el .jar
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto por defecto de Spring Boot (puedes cambiarlo si usas otro)
EXPOSE 8080

# Comando para correr la app
CMD ["java", "-jar", "app.jar"]
