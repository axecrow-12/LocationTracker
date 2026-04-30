GPS Location Tracking and Geofencing App
Software Engineering
Group Members
Wilmar T Macheke H240239Z
Rutendo V Mumvuri H240193J
Rachel D Kufa H240026E
Overview
This Android Java app tracks the user’s current GPS location, saves custom locations, registers geofences around saved places, and opens navigation to a selected saved point using Google Maps.
Main Features
•Live GPS tracking with FusedLocationProviderClient
•Current-location marker and coordinate display on Google Maps
•Add location
•Use Current Location button to auto-fill coordinates
•Saved locations list with Navigate and Delete actions
•Geofence registration around saved locations
•Notification support when the user enters a saved location radius
•Bottom navigation: Home, Places and Add
Minimum Requirements
•Android Studio
•Android 8.0 or later (API 26+)
•Java
•Google Play Services / Maps dependencies
How to Run
1.Open the project in Android Studio.
2.Add your Google Maps API key.
3.Build and run the app on a real Android device.
4.Allow location permissions.
5.Open Home to view the map and current coordinates.
Testing Checklist
1.Tap Refresh Current Location and confirm the map marker updates.
2.Open Add and tap Use Current Location.
3.Enter a location name and radius such as 200.
4.Save the location and confirm the geofence registration message.
5.Open Places and confirm the saved location appears.
6.Tap Navigate to open Google Maps directions.
7.Move into the geofence radius or use mock location testing to trigger the notification.
Demo Video Checklist
The video should show:
•Opening the app.
•Viewing the live map and coordinates.
•Adding a saved location.
•Registering the geofence.
•Viewing saved locations.
•Navigating to a selected saved point.
•Receiving or demonstrating the geofence notification.
