package co.seyco.uber4things.presentation.common.transportation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.seyco.uber4things.R
import co.seyco.uber4things.data.model.Pricing

@Composable
fun ChooseTransportOptionBottomSheet(
	transportOptions: List<Pricing.TransportationType>,
	onOptionSelected: (Pricing.TransportationType) -> Unit,
	onBack: () -> Unit,
	onNext: () -> Unit,
	isNextEnabled: Boolean,
	modifier: Modifier = Modifier
) {
	var selectedOption by remember { mutableStateOf<Pricing.TransportationType?>(null) }

	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(8.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "Choose Transport Option",
			style = MaterialTheme.typography.titleMedium,
			modifier = Modifier.padding(bottom = 16.dp)
		)

		LazyRow(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			items(items = transportOptions) { option ->
				TransportOptionItem(
					option = option,
					isSelected = option == selectedOption,
					onOptionSelected = {
						selectedOption = it
						onOptionSelected(it)
					}
				)
			}
		}

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 16.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Button(
				onClick = onBack,
				modifier = Modifier.weight(1f)
			) {
				Text(text = "Back")
			}
			Button(
				onClick = onNext,
				modifier = Modifier.weight(1f),
				enabled = selectedOption != null && isNextEnabled
			) {
				Text(text = "Next")
			}
		}
	}
}

@Composable
fun TransportOptionItem(
	option: Pricing.TransportationType,
	isSelected: Boolean,
	onOptionSelected: (Pricing.TransportationType) -> Unit
) {
	Card(
		modifier = Modifier
			.size(120.dp)
			.clickable { onOptionSelected(option) },
		shape = RoundedCornerShape(12.dp),
		colors = CardDefaults.cardColors(
			containerColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
		),
		elevation = CardDefaults.cardElevation(4.dp),
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center,
			modifier = Modifier.padding(8.dp)
		) {
			Image(
				painter = painterResource(
					id = when (option.type) {
						Pricing.TransportType.CYCLING -> R.drawable.vtr_bike
						Pricing.TransportType.RIDING -> R.drawable.vtr_motor
						Pricing.TransportType.WALKING -> R.drawable.vtr_walking
					}
				),
				contentDescription = option.type.toString(),
				modifier = Modifier
					.size(48.dp)
					.padding(bottom = 8.dp),
				contentScale = ContentScale.Crop
			)
			Text(
				text = option.price.split('~').let {
					buildString {
						append('$')
						append(it[0].toDouble())
						append(" ~ ")
						append('$')
						append(it[1].toDouble())
					}
				},
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onPrimary
			)
			Text(
				text = option.time,
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onPrimary
			)
		}
	}
}
