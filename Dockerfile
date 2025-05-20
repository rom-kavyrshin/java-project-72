FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY app/gradle gradle
COPY app/build.gradle.kts .
COPY app/settings.gradle.kts .
COPY app/gradlew .

COPY app/src src
COPY app/config config

RUN ./gradlew --no-daemon dependencies

RUN ./gradlew --no-daemon build

ENV JAVA_OPTS="-Xmx512M -Xms512M"

CMD ["java", "-jar", "build/libs/app-1.0-SNAPSHOT-all.jar"]