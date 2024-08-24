package co.seyco.uber4things.data.model

import co.seyco.uber4things.domain.model.SheetType

data class MapsUiState(
	val parcelItems: List<ParcelItem> = emptyList(),
	val transportationType: List<Pricing.TransportationType> = emptyList(),
	val isParcelSelected: Boolean = false,
	val isTransportSelected: Boolean = false,
	val currentSheet: SheetType = SheetType.PARCEL_TYPE
)