package co.seyco.uber4things.di

import android.content.Context
import co.seyco.uber4things.App
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

	@Provides
	@Singleton
	fun provideFusedLocationProviderClient(
		context: Context
	): FusedLocationProviderClient {
		return LocationServices.getFusedLocationProviderClient(context)
	}
}