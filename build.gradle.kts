plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("maven-publish")
}

group = "com.dalmeng"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Web
    api("org.springframework.boot:spring-boot-starter-web:3.2.0")
    
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    // Validation
    api("org.springframework.boot:spring-boot-starter-validation:3.2.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            
            pom {
                name.set("Kotlin Common Web Library")
                description.set("Common web utilities for Spring Boot Kotlin applications including BaseResponse, BaseException, and GlobalExceptionHandler")
                url.set("https://github.com/dalmengs/kotlin-common-web-lib")
                
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                
                developers {
                    developer {
                        id.set("dalmeng")
                        name.set("dalmeng")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/dalmengs/kotlin-common-web-lib.git")
                    developerConnection.set("scm:git:ssh://github.com/dalmengs/kotlin-common-web-lib.git")
                    url.set("https://github.com/dalmengs/kotlin-common-web-lib")
                }
            }
        }
    }
    
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/dalmengs/kotlin-common-web-lib")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
