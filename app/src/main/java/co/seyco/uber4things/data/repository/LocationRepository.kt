package co.seyco.uber4things.data.repository

import co.seyco.uber4things.data.model.ParcelItem
import co.seyco.uber4things.data.model.Pricing
import co.seyco.uber4things.data.model.TransportOption
import com.google.android.gms.maps.model.LatLng

interface LocationRepository {
	/**
	 * Retrieves the last known location of the device.
	 * @return The last known location as a LatLng object.
	 * @throws Exception if location is not available.
	 */
	suspend fun getLastLocation(): LatLng

	/**
	 * Fetches a list of available parcel types from the database.
	 * @return A list of ParcelItem objects.
	 * @throws Exception if there's an error fetching the data.
	 */
	suspend fun getParcelTypes(): List<ParcelItem>

	/**
	 * Retrieves pricing information for a given transport option.
	 * @param transportOption The transport option to get pricing for.
	 * @return Pricing information for the specified transport option.
	 * @throws Exception if there's an error fetching pricing information.
	 */
	suspend fun getTransportOptions(transportOption: TransportOption): Pricing
}
