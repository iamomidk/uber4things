package co.seyco.uber4things.presentation.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.seyco.uber4things.data.model.MapsUiState
import co.seyco.uber4things.data.model.ParcelItem
import co.seyco.uber4things.data.model.Pricing
import co.seyco.uber4things.data.model.TransportOption
import co.seyco.uber4things.domain.usecase.GetCurrentLocationUseCase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class MapViewModel @Inject constructor(
	private val getCurrentLocationUseCase: GetCurrentLocationUseCase
) : ViewModel() {

	private val _uiState = MutableStateFlow(MapsUiState())
	val uiState: StateFlow<MapsUiState> = _uiState

	fun getCurrentLocation(
		cameraPositionState: CameraPositionState,
	) {
		viewModelScope.launch {
			try {
				val location = getCurrentLocationUseCase()
				cameraPositionState.animate(
					CameraUpdateFactory.newLatLngZoom(location, 15f)
				)
			} catch (e: Exception) {
				Log.e("_TAG", e.localizedMessage, e)
			}
		}
	}

	suspend fun getParcelItems(): List<ParcelItem> {
		try {
			return getCurrentLocationUseCase.fetch()
		} catch (e: Exception) {
			Log.e("getParcelItems", e.localizedMessage, e)
			throw e
		}
	}

	suspend fun getPricing(transportOption: TransportOption): Pricing {
		try {
			return getCurrentLocationUseCase.fetchTransportOption(transportOption)
		} catch (e: Exception) {
			Log.e("_TAG", e.localizedMessage, e)
			throw e
		}
	}

	fun moveCameraToBounds(
		cameraPositionState: CameraPositionState,
		startPoint: LatLng,
		endPoint: LatLng
	) {
		val boundsBuilder = LatLngBounds.Builder()
		boundsBuilder.include(startPoint)
		boundsBuilder.include(endPoint)
		val bounds = boundsBuilder.build()
		viewModelScope.launch {
			cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 100))
		}
	}

	fun calculateArcPoints(
		startPoint: LatLng,
		endPoint: LatLng,
		numberOfPoints: Int = 250
	): List<LatLng> {
		val points = mutableListOf<LatLng>()
		val midPoint = LatLng(
			(startPoint.latitude + endPoint.latitude) / 2,
			(startPoint.longitude + endPoint.longitude) / 2
		)
		val controlPoint = calculateControlPoint(startPoint, endPoint, midPoint)

		for (i in 0..numberOfPoints) {
			val t = i.toDouble() / numberOfPoints
			val lat =
				(1 - t) * (1 - t) * startPoint.latitude + 2 * (1 - t) * t * controlPoint.latitude + t * t * endPoint.latitude
			val lng =
				(1 - t) * (1 - t) * startPoint.longitude + 2 * (1 - t) * t * controlPoint.longitude + t * t * endPoint.longitude
			points.add(LatLng(lat, lng))
		}

		return points
	}

	private fun calculateControlPoint(start: LatLng, end: LatLng, mid: LatLng): LatLng {
		val d = sqrt(
			(start.latitude - end.latitude) * (start.latitude - end.latitude) +
					(start.longitude - end.longitude) * (start.longitude - end.longitude)
		)
		val r = d / 2
		val h = r * sqrt(3.0) / 2
		val bearing = atan2(end.latitude - start.latitude, end.longitude - start.longitude)
		val angle = bearing + Math.PI / 2
		val controlLat = mid.latitude + h * sin(angle)
		val controlLng = mid.longitude + h * cos(angle)

		return LatLng(controlLat, controlLng)
	}

	fun selectParcel(parcelItem: ParcelItem) {}
	fun onBackPressed() {}
	fun onParcelConfirmed() {}

}
