import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
}

group = "ru.golden.alf"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation ("org.springframework.boot:spring-boot-starter-websocket")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-actuator:3.2.5")
	implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

	// Добавляем зависимости Spring Security
	implementation ("org.springframework.security:spring-security-config")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-messaging:6.3.3")
	implementation("org.springframework:spring-messaging:6.1.13")


	implementation("com.auth0:java-jwt:4.4.0")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.3")


	runtimeOnly("com.h2database:h2:2.3.232")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
