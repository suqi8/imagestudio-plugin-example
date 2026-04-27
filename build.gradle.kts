import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.File
import java.util.Properties
import java.util.zip.ZipFile
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

val pluginId = "helloplugin"
val pluginVersion = "1.0.0"
val androidCompileSdk = libs.versions.android.compileSdk.get().toInt()
val androidMinSdk = libs.versions.android.minSdk.get().toInt()
val androidSdkDir = resolveAndroidSdkDir()
val androidBuildToolsDir = File(androidSdkDir, "build-tools")
    .listFiles()
    ?.sortedBy { it.name }
    ?.lastOrNull()
    ?: error("Android build-tools not found. Please install Android SDK build-tools.")
val d8Executable = File(
    androidBuildToolsDir,
    if (System.getProperty("os.name").startsWith("Windows", ignoreCase = true)) "d8.bat" else "d8"
)
val androidPlatformJar = File(androidSdkDir, "platforms/android-$androidCompileSdk/android.jar")

kotlin {
    androidLibrary {
        namespace = "com.example.helloplugin"
        compileSdk = androidCompileSdk
        minSdk = androidMinSdk
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Plugin API source is bundled under src/commonMain/kotlin/com/suqi8/imagestudio/plugin/api/
            // (compileOnly by convention — excluded from the output JAR below so the host provides it at runtime)

            // Compose dependencies — all compileOnly so they're not bundled in the plugin JAR
            // (the host app already provides them at runtime).
            compileOnly(libs.runtime)
            compileOnly(libs.foundation)
            compileOnly(libs.ui)
            compileOnly(libs.material3)

            // Compose Resources: generates Res.string accessors from composeResources/values/strings.xml
            implementation(libs.components.resources)
        }
    }
}

// Exclude plugin-api interfaces from the JAR — the host provides them at runtime.
tasks.named<Jar>("jvmJar") {
    exclude("com/suqi8/imagestudio/plugin/api/**")
}

val jvmJarOutput = tasks.named<Jar>("jvmJar").flatMap { it.archiveFile }
val jvmJarOutputFile = jvmJarOutput.get().asFile

val androidClassesDir = layout.buildDirectory.dir("classes/kotlin/android/main")
val androidClassesJarOutput = layout.buildDirectory.file("plugin/android/$pluginId-android-classes.jar")
val androidDexOutputDir = layout.buildDirectory.dir("plugin/android/d8")
val androidDexJarOutput = layout.buildDirectory.file("plugin/android/$pluginId-android.jar")
val androidClassesJarOutputFile = androidClassesJarOutput.get().asFile
val androidDexOutputDirFile = androidDexOutputDir.get().asFile
val androidDexJarOutputFile = androidDexJarOutput.get().asFile

val androidClassesJar = tasks.register<Jar>("androidClassesJar") {
    dependsOn("compileAndroidMain")
    from(androidClassesDir) {
        exclude("com/suqi8/imagestudio/plugin/api/**")
    }
    archiveFileName.set(androidClassesJarOutputFile.name)
    destinationDirectory.set(androidClassesJarOutputFile.parentFile)
}

val androidDexClasses = tasks.register<Exec>("androidDexClasses") {
    dependsOn(androidClassesJar)
    inputs.file(androidClassesJarOutputFile)
    outputs.dir(androidDexOutputDirFile)
    notCompatibleWithConfigurationCache("uses a script-defined Exec task to package Android dex output")
    doFirst {
        androidDexOutputDirFile.deleteRecursively()
        androidDexOutputDirFile.mkdirs()
    }
    commandLine(
        d8Executable.absolutePath,
        "--release",
        "--min-api", androidMinSdk.toString(),
        "--lib", androidPlatformJar.absolutePath,
        "--output", androidDexOutputDirFile.absolutePath,
        androidClassesJarOutputFile.absolutePath
    )
}

val androidDexJar = tasks.register("androidDexJar") {
    dependsOn(androidDexClasses, "jvmJar")
    inputs.dir(androidDexOutputDirFile)
    inputs.file(jvmJarOutputFile)
    outputs.file(androidDexJarOutputFile)
    doLast {
        val dexFile = File(androidDexOutputDirFile, "classes.dex")
        check(dexFile.exists()) { "D8 output missing classes.dex: ${dexFile.absolutePath}" }
        androidDexJarOutputFile.parentFile.mkdirs()
        ZipOutputStream(androidDexJarOutputFile.outputStream().buffered()).use { zip ->
            zip.putNextEntry(ZipEntry("classes.dex"))
            dexFile.inputStream().buffered().use { input ->
                input.copyTo(zip)
            }
            zip.closeEntry()

            ZipFile(jvmJarOutputFile).use { jvmJar ->
                jvmJar.entries().asSequence()
                    .filter { entry ->
                        !entry.isDirectory && entry.name.startsWith("composeResources/")
                    }
                    .forEach { entry ->
                        zip.putNextEntry(ZipEntry(entry.name))
                        jvmJar.getInputStream(entry).use { input ->
                            input.copyTo(zip)
                        }
                        zip.closeEntry()
                    }
            }
        }
    }
}

compose.resources {
    packageOfResClass = "com.example.helloplugin.generated.resources"
    generateResClass = always
}

/** Package the plugin into a distributable .isp file (ZIP containing JAR + plugin.toml + assets). */
tasks.register<Zip>("packageIsp") {
    notCompatibleWithConfigurationCache("rename lambda captures local variables")
    dependsOn("jvmJar", androidDexJar)

    val desktopJarName = "$pluginId-desktop.jar"
    val androidJarName = "$pluginId-android.jar"
    archiveFileName.set("$pluginId-$pluginVersion.isp")
    destinationDirectory.set(layout.buildDirectory.dir("isp"))

    from(tasks.named<Jar>("jvmJar")) {
        into("libs")
        rename { desktopJarName }
    }

    from(androidDexJarOutput) {
        into("libs")
        rename { androidJarName }
    }

    from(layout.projectDirectory.file("plugin.toml"))

    from(layout.projectDirectory.dir("src/commonMain/pluginAssets")) {
        into("assets")
    }
}

fun resolveAndroidSdkDir(): File {
    val envSdk = System.getenv("ANDROID_HOME")
        ?: System.getenv("ANDROID_SDK_ROOT")
    if (!envSdk.isNullOrBlank()) {
        return File(envSdk)
    }

    val localPropertiesCandidates = listOf(
        rootProject.file("local.properties"),
        rootProject.layout.projectDirectory.asFile.parentFile?.resolve("local.properties")
    ).filterNotNull()

    localPropertiesCandidates.forEach { localPropertiesFile ->
        if (localPropertiesFile.exists()) {
            val properties = Properties()
            localPropertiesFile.inputStream().use(properties::load)
            val sdkDir = properties.getProperty("sdk.dir")
            if (!sdkDir.isNullOrBlank()) {
                return File(sdkDir)
            }
        }
    }

    error("Android SDK not found. Please set ANDROID_HOME, ANDROID_SDK_ROOT, or sdk.dir in local.properties.")
}
