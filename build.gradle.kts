plugins {
    id("org.jetbrains.intellij") version "0.4.21"
    java
}

group = "io.github.heldev"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2020.2"
}
