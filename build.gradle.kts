plugins {
  java
  id("com.github.johnrengelman.shadow") version "4.0.3"
}

repositories {
  mavenCentral()
  maven {
    url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
  }
}

val vertxVersion = "4.0.0-SNAPSHOT"
val junitVersion = "5.3.2"

dependencies {
  testImplementation("io.vertx:vertx-core:$vertxVersion")
  testImplementation("io.vertx:vertx-rx-java2:$vertxVersion")
  testImplementation("org.apache.commons:commons-lang3:3.9")
  testImplementation("io.vertx:vertx-junit5:$vertxVersion")
  testImplementation("io.vertx:vertx-web-client:$vertxVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
  test {
    useJUnitPlatform()
  }
}
