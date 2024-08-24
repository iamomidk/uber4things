package co.seyco.uber4things.data.repository

import android.annotation.SuppressLint
import android.util.Log
import co.seyco.uber4things.data.model.ParcelItem
import co.seyco.uber4things.data.model.Pricing
import co.seyco.uber4things.data.model.TransportOption
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepositoryImpl @Inject constructor(
	private val fusedLocationClient: FusedLocationProviderClient,
	private val firebaseFirestore: FirebaseFirestore,
	private val firebaseStorage: FirebaseStorage,
	private val firebaseFunctions: FirebaseFunctions
) : LocationRepository {

	@SuppressLint("MissingPermission")
	override suspend fun getLastLocation(): LatLng = suspendCancellableCoroutine { continuation ->
		fusedLocationClient.lastLocation.addOnSuccessListener { location ->
			location?.let {
				continuation.resume(LatLng(it.latitude, it.longitude))
			} ?: continuation.resumeWithException(Exception("Location not available"))
		}.addOnFailureListener {
			continuation.resumeWithException(it)
		}
	}

	override suspend fun getParcelTypes(): List<ParcelItem> = try {
		val result = firebaseFirestore.collection("bearerParcels").get().await()
		result.documents.map { document ->
			val data = document.data ?: emptyMap()
			val vehicleType = (data["vehicle_type"] as? Map<*, *>) ?: emptyMap<String, Boolean>()
			val imageUrl = data["parcel_img_url"].toString()
			val downloadUrl = getImageDownloadUrl(imageUrl)

			ParcelItem(
				parcelMinWeight = data["parcel_min_weight"].toString().toDoubleOrNull() ?: 0.0,
				parcelType = data["parcel_type"].toString(),
				parcelImgUrl = downloadUrl,
				vehicleType = ParcelItem.VehicleType(
					driving = vehicleType["driving"].toString().toBoolean(),
					walking = vehicleType["walking"].toString().toBoolean(),
					bicycling = vehicleType["bicycling"].toString().toBoolean()
				),
				parcelMaxWeight = data["parcel_max_weight"].toString().toDoubleOrNull() ?: 0.0,
				parcelDescription = data["parcel_description"].toString()
			)
		}
	} catch (e: Exception) {
		Log.e("getParcelTypes_TAG", e.localizedMessage, e)
		emptyList()
	}

	override suspend fun getTransportOptions(transportOption: TransportOption): Pricing = try {
		val req = firebaseFunctions
			.getHttpsCallable("pricing")
			.call(
				mapOf(
					"origin" to mapOf(
						"lat" to transportOption.origin.latitude,
						"lng" to transportOption.origin.longitude
					),
					"destination" to mapOf(
						"lat" to transportOption.destination.latitude,
						"lng" to transportOption.destination.longitude
					),
					"vehicle_type" to mapOf(
						"driving" to transportOption.vehicleType.driving,
						"walking" to transportOption.vehicleType.walking,
						"bicycling" to transportOption.vehicleType.bicycling
					),
					"parcel_type" to transportOption.parcelType,
					"parcel_description" to transportOption.parcelDescription,
					"parcel_min_weight" to transportOption.parcelMinWeight,
					"parcel_max_weight" to transportOption.parcelMaxWeight
				)
			)
			.await()

		val res = req.data as? Map<*, *>
		Pricing(
			code = res?.get("code").toString().toInt(),
			status = res?.get("status").toString(),
			type = listOfNotNull(
				res?.get("cycling")?.let { map ->
					Pricing.TransportationType.fromMap(map as Map<String, Any?>)
				},
				res?.get("riding")?.let { map ->
					Pricing.TransportationType.fromMap(map as Map<String, Any?>)
				},
				res?.get("walking")?.let { map ->
					Pricing.TransportationType.fromMap(map as Map<String, Any?>)
				}
			)
		).also { Log.i("_TAG", it.toString()) }
	} catch (e: Exception) {
		Log.e("getTransportOptions_TAG", e.localizedMessage, e)
		throw e
	}

	private suspend fun getImageDownloadUrl(imagePath: String): String = try {
		val storageRef = firebaseStorage.reference.child(imagePath)
		storageRef.downloadUrl.await().toString()
	} catch (e: Exception) {
		Log.e("getImageDownloadUrl_TAG", "Failed to get download URL for $imagePath", e)
		""
	}
}
