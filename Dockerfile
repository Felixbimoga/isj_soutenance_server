# 🔧 Étape 1 : Build de l'application avec Maven/Gradle
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

# Copie des fichiers sources et du pom.xml
COPY pom.xml .
COPY src ./src

# Build de l'application (en ignorant les tests pour aller plus vite)
RUN ./mvnw clean package -DskipTests  # Pour Maven
# OU si vous utilisez Gradle :
# RUN ./gradlew build -x test

# 🚀 Étape 2 : Création de l'image finale (minimisée)
FROM eclipse-temurin:17-jre-jammy  # JRE seulement (plus léger que JDK)

WORKDIR /app

# Copie du .jar depuis l'étape de build
COPY --from=builder /app/target/*.jar ./app.jar  # Pour Maven
# COPY --from=builder /app/build/libs/*.jar ./app.jar  # Pour Gradle

# Exposition du port (remplacez 8081 par votre port Spring Boot)
EXPOSE 8081

# Commande de démarrage (avec paramètres JVM optimisés)
ENTRYPOINT ["java", "-jar", "-Dserver.port=${PORT:-8081}", "app.jar"]