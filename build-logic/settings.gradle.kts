dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        create("androidlibs") {
            from(files("../gradle/android-libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")