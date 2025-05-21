plugins {
    id("java")
    application
    checkstyle
    jacoco
    id("org.sonarqube") version "6.0.1.5171"
    id("com.gradleup.shadow") version "8.3.6"
    id("io.freefair.lombok") version "8.13.1"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass = "hexlet.code.App"
}

sonar {
    properties {
        property("sonar.projectKey", "rom-kavyrshin_java-project-72")
        property("sonar.organization", "rom-kavyrshin")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.6.0")
    implementation("io.javalin:javalin-rendering:6.6.0")
    implementation("gg.jte:jte:3.2.1")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("org.postgresql:postgresql:42.7.5")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}

tasks.shadowJar {
    mergeServiceFiles()
}
