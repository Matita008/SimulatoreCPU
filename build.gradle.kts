import org.gradle.jvm.tasks.Jar

plugins {
    idea
    application
    id("java")
}

group = "io.matita08"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    // Tell Gradle what the main class is that it should run, this differs per project based on your packages and classes.
    mainClass.set("io.matita08.Main")
}

val normJar = // Same mainclass as the application plugin setting
    tasks.register("normJar", type = Jar::class, configurationAction = fun Jar.() {
        manifest {
            attributes["Implementation-Title"] = "Simulatore"
            attributes["Implementation-Version"] = version
            attributes["Main-Class"] = "io.matita08.Main" // Same mainclass as the application plugin setting
        }
        with(tasks.jar.get() as CopySpec)
        destinationDirectory.set(layout.buildDirectory.dir("dist"))
    })

tasks {
    "build" {
        dependsOn(normJar)
    }
}