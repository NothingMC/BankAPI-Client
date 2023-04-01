import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    `java-library`
    id("com.google.protobuf") version "0.9.2"
    `maven-publish`
}

group = "io.github.nothingmc.bankapi"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    api(group = "io.grpc", name = "grpc-protobuf", version = "1.54.0")
    implementation(group = "io.grpc", name = "grpc-stub", version = "1.54.0")

    if (JavaVersion.current().isJava9Compatible) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        api(group = "javax.annotation", name = "javax.annotation-api", version = "1.3.2")
    }
}

sourceSets{
    getByName("main"){
        java {
            srcDirs(
                "build/generated/source/proto/main/grpc",
                "build/generated/source/proto/main/java"
            )
        }
    }
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:3.22.2"
    }

    plugins {
        // Optional: an artifact spec for a protoc plugin, with "grpc" as
        // the identifier, which can be referred to in the "plugins"
        // container of the "generateProtoTasks" closure.
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.54.0"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                // Apply the "grpc" plugin whose spec is defined above, without options.
                id("grpc")
            }
        }
    }
}


tasks.withType<JavaCompile>() {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = sourceCompatibility
}


val sourcesJar by tasks.registering(Jar::class) {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}

publishing {
    publications {
        register<MavenPublication>("BankAPI-Client") {
            artifactId = "BankAPI-Client"
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}
