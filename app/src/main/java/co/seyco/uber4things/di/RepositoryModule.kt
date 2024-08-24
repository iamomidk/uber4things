package co.seyco.uber4things.di

import co.seyco.uber4things.data.repository.LocationRepository
import co.seyco.uber4things.data.repository.LocationRepositoryImpl
import com.google.android.gms.location.FusedLocationProviderClient
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
object RepositoryModule {

	@Provides
	@Singleton
	fun provideLocationRepository(
		fusedLocationClient: FusedLocationProviderClient,
		firebaseFirestore: FirebaseFirestore,
		firebaseStorage: FirebaseStorage,
		firebaseFunctions: FirebaseFunctions,
	): LocationRepository {
		return LocationRepositoryImpl(
			fusedLocationClient,
			firebaseFirestore,
			firebaseStorage,
			firebaseFunctions
		)
	}
}