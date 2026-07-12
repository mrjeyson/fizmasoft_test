pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "f_test"
include(":app")
include(":domain")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:ui")
include(":core:location")
include(":core:notification")
include(":data")
include(":feature:splash")
include(":feature:login")
include(":feature:face")
include(":feature:movies")
include(":feature:map")
include(":feature:prayer")
include(":feature:weather")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")