plugins {
    id("library-conventions")
}

dependencies {
    implementation(project(":components:domain"))
    implementation(project(":libs:javaUtils"))
    implementation(project(":libs:routing"))
    implementation(project(":libs:handlebars"))
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    implementation("org.jboss.resteasy:resteasy-core:6.2.7.Final")
    implementation("org.jboss.resteasy:resteasy-client:6.2.7.Final")
    implementation("org.jboss.resteasy:resteasy-undertow-cdi:6.2.7.Final")
    implementation("org.jboss.resteasy:resteasy-multipart-provider:6.2.7.Final")
    implementation("org.nanohttpd:nanohttpd:2.3.1")

    val undertowVersion = "2.3.10.Final"
    implementation("io.undertow:undertow-core:$undertowVersion")
    implementation("io.undertow:undertow-servlet:$undertowVersion")
}
