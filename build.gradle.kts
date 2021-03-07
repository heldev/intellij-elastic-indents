import org.jetbrains.intellij.tasks.PatchPluginXmlTask

plugins {
    id("org.jetbrains.intellij") version "0.7.2"
    java
}

group = "io.github.heldev"
version = "2.0.0"

repositories {
    mavenCentral()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2019.3"
}

tasks.getByName<PatchPluginXmlTask>("patchPluginXml") {
    untilBuild("")
}
