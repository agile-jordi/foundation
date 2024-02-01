plugins {
    id("library-conventions")
}

dependencies {
    implementation(projects.libs.javaUtils)
    implementation("com.github.jknack:handlebars:4.3.1")
}
