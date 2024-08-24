package co.seyco.uber4things.domain.usecase

import co.seyco.uber4things.data.model.ParcelItem
import co.seyco.uber4things.data.model.Pricing
import co.seyco.uber4things.data.model.TransportOption
import co.seyco.uber4things.data.repository.LocationRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
	private val locationRepository: LocationRepository
) {
	suspend operator fun invoke(): LatLng = locationRepository.getLastLocation()
	suspend fun fetch(): List<ParcelItem> = locationRepository.getParcelTypes()
	suspend fun fetchTransportOption(transportOption: TransportOption): Pricing =
		locationRepository.getTransportOptions(transportOption)

}
