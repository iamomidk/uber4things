plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
	alias(libs.plugins.google.services)
	id("kotlin-kapt")
	alias(libs.plugins.hilt)
}

android {
	namespace = "co.seyco.uber4things"
	compileSdk = 34

	defaultConfig {
		applicationId = "co.seyco.uber4things"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.1"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)

	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.constraintlayout)

	implementation(libs.play.services.maps)
	implementation(libs.play.services.location)
	implementation(libs.android.maps.utils)
	implementation(libs.maps.compose)

	implementation(libs.androidx.cardview)
	implementation(libs.androidx.recyclerview)
	implementation(libs.material)

	implementation(platform(libs.firebase.bom))
	implementation(libs.firebase.firestore.ktx)
	implementation(libs.firebase.storage)
	implementation(libs.firebase.auth.ktx)
	implementation(libs.firebase.analytics.ktx)
	implementation(libs.firebase.functions)

	implementation(libs.hilt.android)
	kapt(libs.hilt.android.compiler)
	implementation(libs.hilt.work)
	kapt(libs.hilt.compiler)
	implementation(libs.androidx.work.runtime.ktx)
	implementation(libs.androidx.hilt.navigation.compose)

	// ViewModel and LiveData
	implementation(libs.androidx.lifecycle.viewmodel.ktx)
	implementation(libs.androidx.lifecycle.livedata.ktx)

	implementation(libs.androidx.runtime.livedata)

	implementation(libs.coil.compose)
}

kapt {
	correctErrorTypes = true
}

secrets {
	propertiesFileName = "secrets.properties"

	defaultPropertiesFileName = "local.defaults.properties"

	ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
	ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}
