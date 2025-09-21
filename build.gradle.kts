import org.gradle.jvm.tasks.Jar

plugins {
    idea
    application
    id("java")
}

group = "io.matita08"

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
            attributes["Implementation-Title"] = "CPUSim"
            attributes["Implementation-Version"] = "1.13.7" //Manifest
            attributes["Main-Class"] = "io.matita08.Main" // Same mainclass as the application plugin setting
        }
        with(tasks.jar.get() as CopySpec)
        destinationDirectory.set(layout.buildDirectory.dir("dist"))
    })

tasks {
    "build" {
        dependsOn(normJar)
        dependsOn(javadoc)
    }
}

// ===== JAVADOC CONFIGURATION =====
// Enhanced Javadoc generation with professional settings

tasks.javadoc {
    options {
        (this as StandardJavadocDocletOptions).apply {
            windowTitle = "CPU Simulator API Documentation"
            docTitle = "CPU Simulator v1.5 - API Documentation"
            header = "<b>CPU Simulator</b>"
            bottom = "Copyright © 2025 - 2026 Matita008. All rights reserved."
            links("https://docs.oracle.com/en/java/javase/17/docs/api/")
            addBooleanOption("Xdoclint:none", true)
            addStringOption("Xmaxwarns", "1")
            encoding = "UTF-8"
            charSet = "UTF-8"
        }
    }
    include("**/io/matita08/**")
    setDestinationDir(file("${buildDir}/docs/javadoc"))

    doFirst {
        println("Generating comprehensive API documentation...")
    }

    doLast {
        println("Documentation generated successfully at: ${buildDir}/docs/javadoc/index.html")
    }
}

// Task to generate documentation and open it in browser
tasks.register("generateAndOpenDocs") {
    dependsOn("javadoc")
    group = "documentation"
    description = "Generate Javadoc and open it in browser"

    doLast {
        val docFile = file("${buildDir}/docs/javadoc/index.html")
        if (docFile.exists()) {
            val os = System.getProperty("os.name").lowercase()
            val command = when {
                os.contains("windows") -> "cmd /c start"
                os.contains("mac") -> "open"
                else -> "xdg-open"
            }

            try {
                ProcessBuilder(command.split(" ") + docFile.absolutePath).start()
                println("Opening documentation in browser...")
            } catch (e: Exception) {
                println("Documentation available at: ${docFile.absolutePath}")
            }
        } else {
            println("Documentation not found. Run './gradlew javadoc' first.")
        }
    }
}

// Task for checking documentation coverage
tasks.register("checkDocCoverage") {
    group = "verification"
    description = "Check Javadoc documentation coverage"

    doLast {
        val srcDir = file("src/main/java")
        if (srcDir.exists()) {
            val javaFiles = fileTree(srcDir).matching { include("**/*.java") }.files
            val documentedFiles = javaFiles.filter { file ->
                file.readText().contains("/**")
            }

            val totalFiles = javaFiles.size
            val documentedCount = documentedFiles.size
            val coverage = if (totalFiles > 0) (documentedCount * 100.0 / totalFiles) else 0.0

            println("=== Documentation Coverage Report ===")
            println("Total Java files: $totalFiles")
            println("Documented files: $documentedCount")
            println("Coverage: ${"%.2f".format(coverage)}%")

            if (coverage >= 80.0) {
                println("✅ Documentation coverage meets threshold (80%)")
            } else {
                println("⚠️  Documentation coverage below threshold (80%)")
            }
        }
    }
}
