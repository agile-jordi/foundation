plugins {
    id("java-conventions")
}

dependencies {
    implementation(projects.components.domain)
    implementation(projects.libs.javaUtils)
    implementation(projects.libs.handlebars)
    implementation(projects.libs.javalin)
    implementation(projects.libs.json)
    runtimeOnly("org.slf4j:slf4j-simple:2.0.10")
    implementation("org.apache.commons:commons-csv:1.10.0")

    testImplementation(testFixtures(projects.libs.json))
}
