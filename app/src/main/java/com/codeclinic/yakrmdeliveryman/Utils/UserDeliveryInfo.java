package com.codeclinic.yakrmdeliveryman.Utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class UserDeliveryInfo {
    public static String item_delivery_id = "";
    public static String userPickUpLocation = "";
    public static String userDestinationLocation = "";
    public static String userItemQuantity = "";
    public static String userDate = "";
    public static String userMainDate = "";
    public static String userHourTime = "";
    public static String userMinTime = "";
    public static String userAMPM = "";
    public static double latitude = 0.00;
    public static double longitude = 0.00;
    public static double totalKm = 0;
    public static double perKmPrice = 1;
    public static LatLng pickUpLatLong;
    public static LatLng destinationLatLong;
    public static Location location = null;

    public static void clearInfo() {
        userPickUpLocation = "";
        userDestinationLocation = "";
        userItemQuantity = "";
        userDate = "";
        userMainDate = "";
        userHourTime = "";
        userMinTime = "";
        userAMPM = "";
        totalKm = 0;
    }
}
