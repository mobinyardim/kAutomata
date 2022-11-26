plugins {
    kotlin("multiplatform") version "1.7.10"
    id("com.jfrog.artifactory") version("4.28.2")
    id("maven-publish")
}

val libraryGroupId = "com.mobinyardim.libs"
val libraryArtifactId = "KAutomata"
val libraryVersion = "0.1.0-alpha01"

group = "com.mobinyardim.libs"
version = "0.1.0-alpha02"

artifactory {
    setContextUrl("https://inbo.jfrog.io/artifactory")
    publish {
        repository {
            setRepoKey("inbo-public-repo")
            setUsername(properties["artifactory_username"] as String)
            setPassword(properties["artifactory_password"] as String)
        }
        defaults {
            publications("aar")
            setPublishArtifacts(true)
            properties.put("qa.level", "basic")
            properties.put("q.os", "android")
            properties.put("dev.team", "core")
            setPublishPom(true)
        }
    }
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation ("com.google.truth:truth:1.1.3")
                implementation ("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
                implementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }

    val publicationsFromMainHost = listOf(jvm(), js()).map { it.name } + "kotlinMultiplatform"
    publishing {
        publications {
            afterEvaluate {
                create<MavenPublication>("kAutomata"){
                    groupId = libraryGroupId
                    artifactId = libraryArtifactId
                    version = libraryVersion
                }
            }
            matching { it.name in publicationsFromMainHost }.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching { it.publication == targetPublication }
                    .configureEach { onlyIf { findProperty("isMainHost") == "true" } }
            }
        }

        repositories {
            maven {
                url = uri("https://inbo.jfrog.io/artifactory/inbo-public-repo/")
                credentials {
                    username = properties["artifactory_username"] as String
                    password = properties["artifactory_password"] as String
                }
            }
        }
    }
}


tasks.register(name = "runNativeApp", type = Exec::class) {
    group = "run"
    dependsOn("runDebugExecutableNative")
    workingDir("$buildDir/bin/native/debugExecutable")
    commandLine(
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            "${project.name}.exe"
        } else{
            "./${project.name}.kexe"
        }
    )
}
