plugins {
    id("java-conventions")
}

dependencies {
    implementation("com.fasterxml.jackson.jr:jackson-jr-objects:2.13.0")
    implementation("com.fasterxml.jackson.jr:jackson-jr-stree:2.13.0")
    implementation(projects.libs.javaUtils)
}
