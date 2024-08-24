package co.seyco.uber4things.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

	@Provides
	@Singleton
	fun provideFirestore(): FirebaseFirestore {
		return FirebaseFirestore.getInstance()
	}

	@Provides
	@Singleton
	fun provideFirebaseStorage(): FirebaseStorage {
		return FirebaseStorage.getInstance()
	}

	@Provides
	@Singleton
	fun provideFirebaseFunctions(): FirebaseFunctions {
		return FirebaseFunctions.getInstance()
	}
}