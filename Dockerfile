# 🔧 Étape 1 : Build avec Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copie des fichiers Maven
COPY pom.xml .

# Téléchargement des dépendances pour cache Docker
RUN mvn dependency:go-offline

# Copie du code source après le cache
COPY src ./src

# Build sans les tests
RUN mvn clean package -DskipTests

# 🚀 Étape 2 : Exécution
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copie du JAR compilé
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
