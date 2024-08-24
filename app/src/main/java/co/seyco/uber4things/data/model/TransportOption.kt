package co.seyco.uber4things.data.model

import com.google.android.gms.maps.model.LatLng

data class TransportOption(
	val origin: LatLng,
	val destination: LatLng,
	val vehicleType: ParcelItem.VehicleType,
	val parcelType: String,
	val parcelDescription: String,
	val parcelMinWeight: Double,
	val parcelMaxWeight: Double,
)