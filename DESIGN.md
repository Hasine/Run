# Design document

## Minimum Viable Product
* Map ingezoomd op huidige locatie
* Tijdens het plaatsen van de markers, de totale afstand vanuit huidige positie in km laten zien
* Wanneer uiteindelijk gewenste aantal km behaald is met markers, route vastleggen.
* Interacten met muziekspeler
* Afstandstracker implementeren

## Optioneel
* Timer om duur van de run bij te houden
* Wanneer goal bereikt is, gemiddelde snelheid tonen

## Classes and public methods
Class RunMainActivity
* ~ public void search
* ~ public void start

Class SavedRouteActivity
Nog niks

Class SearchActivity
* private GoogleMap mMap
* private GoogleApiClient client
* private LocationRequest mLocationRequest
* private LatLng myLocation
* private PolylineOptions rectOptions
* private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000
* ~ public void onConnected
* ~ private void handleNewLocation
* ~ public void onMapReady
* ~ public void onMapLongClick
* ~ public void onMarkerDragEnd

Class StartActivity
* private float goal
* private float runned

## Advenced sketches of UIs
Zie README voor meest actuele UI screenshots
## APIs and Frameworks
* Google Maps Android API

## Data source, database tables and fields
Geen
