@file:OptIn(ExperimentalMaterial3Api::class)

package co.seyco.uber4things.presentation.map

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import co.seyco.uber4things.R
import co.seyco.uber4things.data.model.TransportOption
import co.seyco.uber4things.domain.model.SheetType.PARCEL_TYPE
import co.seyco.uber4things.domain.model.SheetType.TRANSPORT_OPTION
import co.seyco.uber4things.presentation.common.LoadingDialog
import co.seyco.uber4things.presentation.common.parcel.ChooseParcelTypeBottomSheet
import co.seyco.uber4things.presentation.common.transportation.ChooseTransportOptionBottomSheet
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
	val uiState by viewModel.uiState.collectAsState()
	val cameraPositionState = rememberCameraPositionState()
	val context = LocalContext.current
	var centerLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
	var originMarker by remember { mutableStateOf<LatLng?>(null) }
	var destinationMarker by remember { mutableStateOf<LatLng?>(null) }
	var polylinePoints by remember { mutableStateOf<List<LatLng>?>(null) }
	var isMapFrozen by remember { mutableStateOf(false) }
	var isCenterMarkerVisible by remember { mutableStateOf(true) }
	var centerMarkerDrawable by remember { mutableIntStateOf(R.drawable.origin_pin) }
	val parcelItems = remember { mutableStateOf(uiState.parcelItems) }
	val transportationType = remember { mutableStateOf(uiState.transportationType) }
	val currentSheet = remember { mutableStateOf(PARCEL_TYPE) }
	var isLoading by remember { mutableStateOf(true) }

	val sheetState = rememberModalBottomSheetState(
		skipPartiallyExpanded = true,
		confirmValueChange = { it != SheetValue.Hidden }
	)
	val scope = rememberCoroutineScope()
	var showBottomSheet by remember { mutableStateOf(false) }

	LaunchedEffect(cameraPositionState.isMoving) {
		if (!cameraPositionState.isMoving) {
			centerLatLng = cameraPositionState.position.target
			Log.d("MapScreen", "Camera stopped moving at: $centerLatLng")
		}
	}
	if (showBottomSheet) ModalBottomSheet(
		modifier = Modifier.wrapContentHeight(),
		onDismissRequest = { showBottomSheet = false },
		sheetState = sheetState,
		properties = ModalBottomSheetDefaults.properties(shouldDismissOnBackPress = false),
	) {
		when (currentSheet.value) {
			PARCEL_TYPE -> ChooseParcelTypeBottomSheet(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentHeight(),
				parcelItems = parcelItems.value,
				onParcelSelected = { },
				onBack = { },
				onNext = {
					scope.launch {
						isLoading = true
						transportationType.value = viewModel.getPricing(
							TransportOption(
								origin = originMarker ?: LatLng(0.0, 0.0),
								destination = destinationMarker ?: LatLng(0.0, 0.0),
								vehicleType = it.vehicleType,
								parcelType = it.parcelType,
								parcelDescription = it.parcelDescription,
								parcelMinWeight = it.parcelMinWeight,
								parcelMaxWeight = it.parcelMaxWeight,
							)
						).type
					}.invokeOnCompletion {
						isLoading = false
						currentSheet.value = TRANSPORT_OPTION
					}
				},
			)

			TRANSPORT_OPTION -> ChooseTransportOptionBottomSheet(
				transportOptions = transportationType.value,
				onOptionSelected = { },
				onBack = { },
				onNext = { },
				isNextEnabled = false
			)
		}
	}

	Box(modifier = Modifier.fillMaxSize()) {
		GoogleMap(
			modifier = Modifier.fillMaxSize(),
			cameraPositionState = cameraPositionState,
			onMapLoaded = {
				isLoading = false
				viewModel.getCurrentLocation(cameraPositionState)
			},
			properties = MapProperties(
				isMyLocationEnabled = true,
				mapType = MapType.NORMAL
			),
			uiSettings = MapUiSettings(
				zoomControlsEnabled = false,
				compassEnabled = !isMapFrozen,
				rotationGesturesEnabled = !isMapFrozen,
				tiltGesturesEnabled = !isMapFrozen,
				scrollGesturesEnabled = !isMapFrozen,
				zoomGesturesEnabled = !isMapFrozen,
				indoorLevelPickerEnabled = !isMapFrozen
			)
		) {
			originMarker?.let {
				Marker(
					state = MarkerState(position = it),
					title = "Origin",
					icon = ContextCompat
						.getDrawable(context, R.drawable.origin_pin)
						?.let { drawable ->
							val width = drawable.intrinsicWidth
							val height = drawable.intrinsicHeight
							drawable.toBitmap(
								width.div(3), height.div(3)
							)
						}
						?.let { bitmap -> BitmapDescriptorFactory.fromBitmap(bitmap) }
				)
			}

			destinationMarker?.let {
				Marker(
					state = MarkerState(position = it),
					title = "Destination",
					icon = ContextCompat
						.getDrawable(context, R.drawable.destination_pin)
						?.let { drawable ->
							val width = drawable.intrinsicWidth
							val height = drawable.intrinsicHeight
							drawable.toBitmap(
								width.div(3), height.div(3)
							)
						}
						?.let { bitmap -> BitmapDescriptorFactory.fromBitmap(bitmap) }
				)
			}
			polylinePoints?.let {
				Polyline(
					points = it,
					color = Color.DarkGray,
					width = 10f,
					pattern = listOf(Dash(20f), Gap(20f), Dash(20f))
				)
			}
		}


		if (isCenterMarkerVisible) {
			Image(
				painter = painterResource(id = centerMarkerDrawable),
				contentDescription = "Center Marker",
				modifier = Modifier
					.size(48.dp)
					.align(Alignment.Center)
					.zIndex(1f)
			)
		}

		if (originMarker == null) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.align(Alignment.BottomCenter)
					.background(Color.Transparent)
					.padding(16.dp)
			) {
				Button(
					onClick = {
						originMarker = centerLatLng
						centerMarkerDrawable = R.drawable.destination_pin
					},
					modifier = Modifier.weight(4f)
				) { Text("Choose Origin") }
				Button(
					onClick = { viewModel.getCurrentLocation(cameraPositionState) },
					modifier = Modifier
						.clip(CircleShape)
						.padding(horizontal = 8.dp)
				) {
					Image(
						modifier = Modifier.size(24.dp),
						painter = painterResource(id = R.drawable.gps),
						contentDescription = "Center Button"
					)
				}
			}
		} else {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.align(Alignment.BottomCenter)
					.background(Color.Transparent)
					.padding(16.dp)
			) {
				Button(
					onClick = {
						originMarker = null
						centerMarkerDrawable = R.drawable.origin_pin
					},
					modifier = Modifier.weight(1f)
				) { Text("Back") }
				Button(
					onClick = {
						isCenterMarkerVisible = false
						destinationMarker = centerLatLng
						polylinePoints = originMarker?.let { origin ->
							destinationMarker?.let { destination ->
								viewModel.calculateArcPoints(origin, destination)
							}
						}
						isMapFrozen = true
						scope.launch {
							isLoading = true
							parcelItems.value = viewModel.getParcelItems()
						}.invokeOnCompletion {
							scope.launch {
								sheetState.show()
							}.invokeOnCompletion {
								isLoading = false
								showBottomSheet = sheetState.isVisible
							}
						}
						viewModel.moveCameraToBounds(cameraPositionState, originMarker!!, destinationMarker!!)
					},
					modifier = Modifier.weight(2f)
				) { Text("Choose Destination") }
				Button(
					onClick = { viewModel.getCurrentLocation(cameraPositionState) },
					modifier = Modifier
						.clip(CircleShape)
						.padding(horizontal = 8.dp)
				) {
					Image(
						modifier = Modifier.size(24.dp),
						painter = painterResource(id = R.drawable.gps),
						contentDescription = "Center Button"
					)
				}
			}
		}

		LoadingDialog(isLoading = isLoading)
	}
}