package co.seyco.uber4things.di

import android.content.Context
import co.seyco.uber4things.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	@Singleton
	@Provides
	fun provideApplication(@ApplicationContext app: Context): App = app as App

	@Provides
	@Singleton
	fun provideContext(application: App): Context = application.applicationContext
}