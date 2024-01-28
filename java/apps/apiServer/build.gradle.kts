plugins {
    id("library-conventions")
}

dependencies {
    implementation(project(":libs:javaUtils"))
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    implementation("org.jboss.resteasy:resteasy-core:6.2.7.Final")
    implementation("org.jboss.resteasy:resteasy-client:6.2.7.Final")
    implementation("org.jboss.resteasy:resteasy-undertow-cdi:6.2.7.Final")
    implementation("org.jboss.resteasy:resteasy-multipart-provider:6.2.7.Final")
    implementation("org.nanohttpd:nanohttpd:2.3.1")
    implementation("com.github.jknack:handlebars:4.3.1")
}
