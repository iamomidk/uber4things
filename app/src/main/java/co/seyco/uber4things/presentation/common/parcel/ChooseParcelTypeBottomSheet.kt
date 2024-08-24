package co.seyco.uber4things.presentation.common.parcel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import co.seyco.uber4things.data.model.ParcelItem
import coil.compose.rememberAsyncImagePainter

@Composable
fun ChooseParcelTypeBottomSheet(
	parcelItems: List<ParcelItem>,
	onParcelSelected: (ParcelItem) -> Unit,
	onBack: () -> Unit,
	onNext: (ParcelItem) -> Unit,
	modifier: Modifier = Modifier,
) {
	var selectedParcel by remember { mutableStateOf<ParcelItem?>(null) }

	Column(
		modifier = modifier.padding(bottom = 8.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "Choose the parcel's type",
			style = MaterialTheme.typography.titleMedium,
			modifier = Modifier.padding(vertical = 8.dp)
		)

		LazyColumn(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp)
		) {
			items(parcelItems) { item ->
				ParcelItemView(
					parcelItem = item,
					isSelected = item == selectedParcel,
					onItemClicked = {
						selectedParcel = it
						onParcelSelected(it)
					}
				)
			}
		}

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			Button(
				onClick = onBack,
				modifier = Modifier.weight(1f)
			) {
				Text(text = "Back")
			}
			Button(
				onClick = { selectedParcel?.let(onNext) },
				modifier = Modifier.weight(1f),
				enabled = selectedParcel != null
			) {
				Text(text = "Next")
			}
		}
	}
}

@Composable
fun ParcelItemView(
	parcelItem: ParcelItem,
	isSelected: Boolean,
	onItemClicked: (ParcelItem) -> Unit
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp)
			.clickable { onItemClicked(parcelItem) },
		shape = RectangleShape,
		colors = CardDefaults.cardColors(
			containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
		),
		elevation = CardDefaults.cardElevation(4.dp)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(8.dp)
		) {
			Image(
				painter = rememberAsyncImagePainter(model = parcelItem.parcelImgUrl),
				contentDescription = "Parcel Image",
				modifier = Modifier
					.width(64.dp)
					.height(48.dp),
				contentScale = ContentScale.Fit
			)

			Spacer(modifier = Modifier.width(8.dp))

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = parcelItem.parcelType,
					style = MaterialTheme.typography.titleMedium
				)
				Text(
					text = "${parcelItem.parcelMinWeight} Kg - ${parcelItem.parcelMaxWeight} Kg",
					style = MaterialTheme.typography.bodyMedium
				)
				Text(
					text = parcelItem.parcelDescription,
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
	}
}
