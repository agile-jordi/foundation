plugins {
    `java-library`
    id("java-conventions")
    id("java-test-fixtures")
}

dependencies {
    testFixturesImplementation("org.jetbrains:annotations:24.0.0")
    testFixturesImplementation(platform("org.junit:junit-bom:5.10.1"))
    testFixturesImplementation("org.junit.jupiter:junit-jupiter")
}
