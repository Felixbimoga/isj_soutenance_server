# 🔧 Étape 1 : Build de l'application avec Maven
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

# Copie des fichiers nécessaires
COPY pom.xml .
COPY src ./src

# Build de l'application (ignore les tests)
RUN ./mvnw clean package -DskipTests

# 🚀 Étape 2 : Image d'exécution finale
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copie du JAR depuis l'étape builder
COPY --from=builder /app/target/*.jar app.jar

# Exposition du port
EXPOSE 8081

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]