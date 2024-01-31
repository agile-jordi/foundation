plugins {
    id("library-conventions")
}

dependencies {
    implementation(project(":components:domain"))
    implementation(project(":libs:javaUtils"))
    implementation(project(":libs:handlebars"))
    implementation(project(":libs:javalin"))
    runtimeOnly("org.slf4j:slf4j-simple:2.0.10")
}
