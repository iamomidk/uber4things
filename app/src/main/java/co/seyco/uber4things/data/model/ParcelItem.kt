package co.seyco.uber4things.data.model

data class ParcelItem(
	val parcelMinWeight: Double,
	val parcelType: String,
	val parcelImgUrl: String,
	val vehicleType: VehicleType,
	val parcelMaxWeight: Double,
	val parcelDescription: String
) {
	data class VehicleType(
		val driving: Boolean,
		val walking: Boolean,
		val bicycling: Boolean
	)

	companion object {
		fun fromMap(data: Map<String, Any?>): ParcelItem {
			val vehicleTypeMap = data["vehicle_type"] as? Map<*, *>

			val vehicleType = VehicleType(
				driving = vehicleTypeMap?.get("driving")?.toString()?.toBoolean() ?: false,
				walking = vehicleTypeMap?.get("walking")?.toString()?.toBoolean() ?: false,
				bicycling = vehicleTypeMap?.get("bicycling")?.toString()?.toBoolean() ?: false
			)

			return ParcelItem(
				parcelMinWeight = data["parcel_min_weight"]?.toString()?.toDoubleOrNull() ?: 0.0,
				parcelType = data["parcel_type"]?.toString().orEmpty(),
				parcelImgUrl = data["parcel_img_url"]?.toString().orEmpty(),
				vehicleType = vehicleType,
				parcelMaxWeight = data["parcel_max_weight"]?.toString()?.toDoubleOrNull() ?: 0.0,
				parcelDescription = data["parcel_description"]?.toString().orEmpty()
			)
		}
	}
}
