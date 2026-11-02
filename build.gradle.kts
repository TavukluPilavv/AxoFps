plugins { id("fabric-loom") version "1.8.13" }
version = "1.0.0"
group = "com.axofps"
base { archivesName.set("AxoFPS") }
repositories { mavenCentral() }
dependencies {
    minecraft("com.mojang:minecraft:1.21.1")
    mappings("net.fabricmc:yarn:1.21.1+build.3:v2")
    modImplementation("net.fabricmc:fabric-loader:0.16.10")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.102.1+1.21.1")
}
java { toolchain.languageVersion.set(JavaLanguageVersion.of(21)); withSourcesJar() }
tasks.withType<JavaCompile>().configureEach { options.release.set(21) }
