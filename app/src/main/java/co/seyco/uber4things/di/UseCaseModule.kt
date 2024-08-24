package co.seyco.uber4things.di

import co.seyco.uber4things.data.repository.LocationRepository
import co.seyco.uber4things.domain.usecase.GetCurrentLocationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

	@Provides
	@Singleton
	fun provideGetLocationUseCase(
		locationRepository: LocationRepository
	): GetCurrentLocationUseCase {
		return GetCurrentLocationUseCase(locationRepository)
	}
}