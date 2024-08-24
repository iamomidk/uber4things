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
		maven { setUrl("https://repo1.maven.org/maven2") }
		maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots/") }
		maven { setUrl("https://jitpack.io") }
		gradlePluginPortal()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
		google()
		maven { setUrl("https://repo1.maven.org/maven2") }
		maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots/") }
		maven { setUrl("https://jitpack.io") }
		mavenCentral()
	}
}

rootProject.name = "uber4things"
include(":app")
 