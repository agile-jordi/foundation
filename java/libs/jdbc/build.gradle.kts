plugins {
    id("library-conventions")
}

dependencies {
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.7.2")

    testFixturesImplementation("org.testcontainers:postgresql:1.19.7")
    testFixturesImplementation(projects.libs.javaUtils)
}
