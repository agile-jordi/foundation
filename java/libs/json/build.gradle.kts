plugins {
    id("library-conventions")
}

dependencies {
    // TODO: Try to avoid exposing jackson apis
    api("com.fasterxml.jackson.jr:jackson-jr-objects:2.13.0")
    implementation(projects.libs.javaUtils)
}
