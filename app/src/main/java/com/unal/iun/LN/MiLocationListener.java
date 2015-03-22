package com.unal.iun.LN;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.unal.iun.MainActivity;

public class MiLocationListener implements LocationListener {
    public static double lat = 0;
    public static double longi = 0;
    public static Context appcont;
    public static TextView textLat;
    public static TextView textLon;
    public static TextView textSede;
    public static SQLiteDatabase db;
    public static boolean changed = false;

    @Override
    public void onLocationChanged(Location loc) {
        lat = loc.getLatitude();
        longi = loc.getLongitude();
        textLat.setText("Latitud: " + lat);
        textLon.setText("Longitud: " + longi);
        try {
            if (!changed) {
                buscarSede();
                changed = true;
            }
        } catch (Exception e) {
            Log.e("error LocationListener", e.toString());
        }
        /*
         * Toast.makeText(appcont, "posisiones cambiadas a lat: " + lat +
		 * " long: " + longi, 1) .show();
		 */
    }

    public void buscarSede() {
        String query = "SELECT sede_edificio,min((latitud-" + lat
                + ")*(latitud-" + lat + ")+(longitud-" + longi + ")*(longitud-"
                + longi + ")) FROM edificios where nivel=1";
        if (longi < 0) {
            query = "SELECT sede_edificio,min((latitud-" + lat
                    + ")*(latitud-" + lat + ")+(longitud+" + longi * -1
                    + ")*(longitud+" + longi * -1
                    + ")) FROM edificios where nivel=1";
            if (lat < 0) {
                query = "SELECT sede_edificio,min((latitud+" + lat * -1
                        + ")*(latitud+" + lat * -1 + ")+(longitud+" + longi
                        * -1 + ")*(longitud+" + longi * -1
                        + ")) FROM edificios where nivel=1";
            }
        }
        if (lat < 0) {
            query = "SELECT sede_edificio,min((latitud+" + lat * -1
                    + ")*(latitud+" + lat * -1 + ")+(longitud-" + longi
                    + ")*(longitud-" + longi
                    + ")) FROM edificios where nivel=1";
            if (longi < 0) {
                query = "SELECT sede_edificio,min((latitud+" + lat * -1
                        + ")*(latitud+" + lat * -1 + ")+(longitud+" + longi
                        * -1 + ")*(longitud+" + longi * -1
                        + ")) FROM edificios where nivel=1";
            }
        }
        Cursor c = db.rawQuery(query, null);
        String[][] mat = Util.imprimirLista(c);
        Log.e("sede", query);
        Toast.makeText(appcont, Util.getcolumn(mat, 0)[0].trim(),
                1).show();
        String a = Util.getcolumn(mat, 0)[0].trim();
        textSede.setText(a);
        MainActivity.sede = a;
        c.close();
        db.close();

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (!provider.contains("gps")) {
            Toast.makeText(appcont,
                    "Activa el Wi-Fi para Acceder a tu ubicacion", 1).show();
        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (!provider.contains("gps")) {
            Toast.makeText(appcont, "Activado el " + provider, 1).show();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
