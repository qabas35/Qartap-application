package com.example.qartal.School;

import com.google.android.gms.maps.model.LatLng;

public class LocationInfo {

    public LatLng LastKnownLocation;
    public double latitude;
    public double longitude;

    public  LocationInfo ()
    { }

    public LocationInfo(LatLng L , double latitude , double longitude ){
        this.LastKnownLocation = L ;
        latitude = LastKnownLocation.latitude;
        longitude = LastKnownLocation.longitude;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
