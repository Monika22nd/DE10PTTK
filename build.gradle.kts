plugins {
    java
    war
}

group = "supermarket6"
version = "1.0.0"

repositories { mavenCentral() }

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

val devTomcat by configurations.creating

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    compileOnly("org.apache.tomcat.embed:tomcat-embed-core:10.1.61")
    implementation("com.h2database:h2:2.3.232")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.2")
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")
    devTomcat("org.apache.tomcat.embed:tomcat-embed-core:10.1.61")
    devTomcat("org.apache.tomcat.embed:tomcat-embed-jasper:10.1.61")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}

tasks.test { useJUnitPlatform() }

tasks.register<JavaExec>("runLocal") {
    dependsOn(tasks.war)
    mainClass.set("app.DevServer6")
    classpath = sourceSets.main.get().runtimeClasspath + devTomcat
    args(tasks.war.get().archiveFile.get().asFile.absolutePath)
}
