# GPS Location Tracking and Geofencing App

## Software Engineering Project

### Group Members

| Name | Student ID |
|---|---|
| Wilmar T. Macheke | H240239Z |
| Rutendo V. Mumvuri | H240193J |
| Rachel D. Kufa | H240026E |

---

## Overview

The **GPS Location Tracking and Geofencing App** is an Android Java application designed to track a user's current GPS location, save custom locations, register geofences around saved places, and open navigation to a selected saved point using Google Maps.

This project demonstrates the use of location-based services in Android, including live GPS tracking, geofence registration, saved location management, and navigation support.

---

## Main Features

- Live GPS tracking using `FusedLocationProviderClient`
- Current-location marker and coordinate display on Google Maps
- Add and save custom locations
- Use Current Location button to auto-fill coordinates
- Saved locations list with Navigate and Delete actions
- Geofence registration around saved locations
- Notification support when the user enters a saved location radius
- Bottom navigation with Home, Places, and Add sections

---

## Minimum Requirements

Before running the application, ensure you have:

- Android Studio installed
- Android 8.0 or later, API 26+
- Java installed and configured
- Google Play Services dependencies
- Google Maps API key

---

## How to Run

1. Open the project in **Android Studio**.
2. Add your **Google Maps API key** to the project.
3. Build and run the app on a real Android device.
4. Allow location permissions when prompted.
5. Open the **Home** screen to view the map and current coordinates.

---

## Testing Checklist

Use the checklist below to test the main functionality of the application:

1. Tap **Refresh Current Location** and confirm that the map marker updates.
2. Open the **Add** screen and tap **Use Current Location**.
3. Enter a location name and radius, for example `200`.
4. Save the location and confirm that the geofence registration message appears.
5. Open the **Places** screen and confirm that the saved location appears.
6. Tap **Navigate** to open Google Maps directions.
7. Move into the geofence radius or use mock location testing to trigger the notification.

---

## Demo Video Checklist

The demo video should clearly show the following:

- Opening the application
- Viewing the live map and current coordinates
- Adding a saved location
- Registering the geofence
- Viewing saved locations
- Navigating to a selected saved point
- Receiving or demonstrating the geofence notification

---

## Technologies Used

- Java
- Android Studio
- Google Maps API
- Google Play Services Location API
- Geofencing API
- Android Notifications
- Local Storage

---

## Project Purpose

The purpose of this project is to demonstrate how Android applications can use GPS, maps, geofencing, and notifications to provide location-aware services. It also shows how saved places can be managed and used for navigation and proximity-based alerts.
