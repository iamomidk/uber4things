package co.seyco.uber4things.data.model

data class Pricing(
	val code: Int,
	val status: String,
	val type: List<TransportationType>
) {
	data class TransportationType(
		val price: String,
		val distance: Double,
		val time: String,
		val duration: String,
		val length: String,
		val type: TransportType
	) {
		companion object {
			fun fromMap(data: Map<String, Any?>): TransportationType {
				val types = TransportationType(
					price = data["price"]?.toString().orEmpty(),
					distance = data["distance"]?.toString()?.toDoubleOrNull() ?: 0.0,
					time = data["time"]?.toString().orEmpty(),
					duration = data["duration"]?.toString().orEmpty(),
					length = data["length"]?.toString().orEmpty(),
					type = TransportType.valueOf(data["type"]?.toString().orEmpty().uppercase())
				)
				return types
			}
		}

	}

	enum class TransportType {
		CYCLING,
		RIDING,
		WALKING
	}

}
