plugins {
    id("library-conventions")
}

dependencies {
    implementation(projects.libs.javaUtils)
    implementation(projects.components.domain)

    testImplementation(testFixtures(projects.libs.jdbc))
}
