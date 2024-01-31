plugins {
    id("library-conventions")
}

dependencies {
    api("io.javalin:javalin:6.0.0")
    implementation(project(":libs:handlebars"))
}
