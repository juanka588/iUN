package com.unal.iun.LN;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.unal.iun.GUI.MainActivity;
import com.unal.iun.R;

public class MainScreenLocationListener implements LocationListener {
    private final Context context;
    private final TextView textLocation;
    private final TextView textSede;
    private final SQLiteDatabase db;

    private boolean changed = false;
    private double latitude = 0;
    private double longitude = 0;

    public MainScreenLocationListener(Context context, TextView textLocation, TextView textSede, SQLiteDatabase db) {
        this.context = context;
        this.textLocation = textLocation;
        this.textSede = textSede;
        this.db = db;
    }

    @Override
    public void onLocationChanged(Location loc) {
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        String locationString = context.getString(R.string.location, latitude, longitude);
        textLocation.setText(locationString);
        if (!changed) {
            buscarSede();
            changed = true;
        }
    }

    private void buscarSede() {
        String query = "SELECT sede_edificio,min((latitud-" + latitude
                + ")*(latitud-" + latitude + ")+(longitud-" + longitude + ")*(longitud-"
                + longitude + ")) FROM edificios where nivel=1";
        if (longitude < 0) {
            query = "SELECT sede_edificio,min((latitud-" + latitude
                    + ")*(latitud-" + latitude + ")+(longitud+" + longitude * -1
                    + ")*(longitud+" + longitude * -1
                    + ")) FROM edificios where nivel=1";
            if (latitude < 0) {
                query = "SELECT sede_edificio,min((latitud+" + latitude * -1
                        + ")*(latitud+" + latitude * -1 + ")+(longitud+" + longitude
                        * -1 + ")*(longitud+" + longitude * -1
                        + ")) FROM edificios where nivel=1";
            }
        }
        if (latitude < 0) {
            query = "SELECT sede_edificio,min((latitud+" + latitude * -1
                    + ")*(latitud+" + latitude * -1 + ")+(longitud-" + longitude
                    + ")*(longitud-" + longitude
                    + ")) FROM edificios where nivel=1";
            if (longitude < 0) {
                query = "SELECT sede_edificio,min((latitud+" + latitude * -1
                        + ")*(latitud+" + latitude * -1 + ")+(longitud+" + longitude
                        * -1 + ")*(longitud+" + longitude * -1
                        + ")) FROM edificios where nivel=1";
            }
        }
        Cursor c = db.rawQuery(query, null);
        String[][] mat = Util.imprimirLista(c);
        Util.log(context.getString(R.string.main_activity_sede), query);
        Toast.makeText(context, Util.getcolumn(mat, 0)[0].trim(),
                Toast.LENGTH_SHORT).show();
        String a = Util.getcolumn(mat, 0)[0].trim();
        textSede.setText(a);
        MainActivity.CLOSEST_LOCATION_PARAM = a;
        c.close();
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (!provider.contains("gps")) {
            Toast.makeText(context, "Activa el Wi-Fi para Acceder a tu ubicacion", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (!provider.contains("gps")) {
            Toast.makeText(context, "Activado el " + provider, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
