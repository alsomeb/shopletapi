plugins {
    java
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.alsomeb"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.5") // Validator
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.0.5") // Actuator
    implementation("org.springframework.boot:spring-boot-starter-logging:3.0.5") // Logging
    implementation("org.springframework.boot:spring-boot-starter-hateoas:3.0.5") // Hateoas
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2") // För att Serialisera LocalDateTime


}

tasks.withType<Test> {
    useJUnitPlatform()
}
