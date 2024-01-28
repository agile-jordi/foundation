plugins {
    java
    // Versions specified in the buildSrc/build.gradle.kts
    id("com.github.spotbugs")
}

group = "com.agilogy"

repositories {
    mavenCentral()
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Enable deprecation messages when compiling Java code
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:deprecation")
}
