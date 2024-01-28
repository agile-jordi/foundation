plugins {
    id("java-conventions")
}

dependencies {
    implementation(project(":libs:javaUtils"))
    implementation("com.github.jknack:handlebars:4.3.1")
}
