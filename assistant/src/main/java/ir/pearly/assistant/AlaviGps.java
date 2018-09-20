package ir.pearly.assistant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

//<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

public class AlaviGps implements LocationListener {


    private final Context context;

    private LocationManager locationManager;
    public boolean GpsEnable = false;
    private Criteria criteria;
    private String Holder;
    private Location location;

    private static String subname = "";

    public AlaviGps(Context context, String subname) {

        this.context = context;
        this.subname = subname;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        Holder = locationManager.getBestProvider(criteria, false);
        CheckGpsStatus();

    }

    public void stop() {
        if (locationManager != null) {
            locationManager.removeUpdates(AlaviGps.this);
        }
    }

    @SuppressLint("MissingPermission")
    public void start() {
        CheckGpsStatus();
        if (GpsEnable == true) {
            if (Holder != null) {

                location = locationManager.getLastKnownLocation(Holder);
                locationManager.requestLocationUpdates(Holder, 1000, 1, AlaviGps.this);
            }
        } else {

            showSettingsAlert();

        }
    }

    private void CheckGpsStatus() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        GpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting DialogHelp Title
        alertDialog.setTitle("GPS is settings");

        // Setting DialogHelp Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {


        AlaviUtill.callsub(context, subname, 0, location);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


}