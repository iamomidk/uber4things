# Map Parcel Delivery Android App

This Android application allows users to choose parcel types and transportation options by selecting an origin and destination on a map. The app integrates Google Maps, Jetpack Compose, Hilt, and Firebase for a seamless and modern user experience.

## Features

- **Google Maps Integration**: Select origin and destination locations directly on the map.
- **Jetpack Compose UI**: Modern UI components built using Jetpack Compose.
- **Hilt Dependency Injection**: Provides dependency injection for ViewModels and other components.
- **Location Handling**: Fetches current location and allows user-defined locations.
- **Parcel and Transportation Options**: Choose parcel types and transportation options from customizable sheets.
- **Polyline Drawing**: Displays a polyline between the origin and destination on the map.

## Technologies Used

- **Kotlin**: Primary programming language.
- **Jetpack Compose**: UI framework.
- **Google Maps**: Map display and interaction.
- **Hilt**: Dependency injection.
- **Firebase**: Storage, Firestore, and Functions for backend services.
- **ViewModel**: Manages UI-related data lifecycle.

## Architecture

This project follows the MVVM (Model-View-ViewModel) architecture pattern:

- **Model**: Handles data operations. It uses Firebase for data storage and retrieval.
- **ViewModel**: Connects the Model and View. Manages the app's data logic and handles UI state.
- **View (Composable Functions)**: Displays the UI, observes changes from the ViewModel, and updates accordingly.

## Project Structure

```
├── app/
│   ├── data/                        # Data layer including Firebase and Repository implementations.
│   ├── di/                          # Hilt modules for dependency injection.
│   ├── ui/                          # Composables and UI logic.
│   ├── utils/                       # Utility classes and extension functions.
│   └── viewmodel/                   # ViewModels for managing UI state.
├── build.gradle                     # Gradle configuration.
└── README.md                        # Project documentation.
```

## Getting Started

### Prerequisites

- Android Studio Flamingo or later.
- Kotlin 1.8 or higher.
- Firebase account with Firestore and Firebase Functions set up.

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/MapParcelDeliveryApp.git
   ```
2. Open the project in Android Studio.
3. Add your `google-services.json` file in the `app/` directory.
4. Sync the project with Gradle files.

### Configuration

1. Set up Firebase by enabling Firestore, Firebase Storage, and Firebase Functions.
2. Configure the `FusedLocationProviderClient` for location services.
3. Customize map styling or parcel types by editing the corresponding files in the project.

### Building and Running

1. Build the project using Android Studio.
2. Run the app on an emulator or a physical device.

### Usage

1. Open the app and allow location permissions.
2. Choose your origin by centering the map and clicking “Choose Origin.”
3. Select your destination similarly.
4. Choose parcel type and transportation options from the bottom sheets.
5. Confirm and visualize your delivery route on the map.

## Screenshots

Include some relevant screenshots here.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

---
