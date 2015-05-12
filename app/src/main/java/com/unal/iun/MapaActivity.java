package com.unal.iun;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unal.iun.LN.LinnaeusDatabase;
import com.unal.iun.LN.MiLocationListener;
import com.unal.iun.LN.Util;

import java.util.ArrayList;

public class MapaActivity extends FragmentActivity {

    static int tipo = 1, count = 0, zoom = 19;
    static String cond = "";
    GoogleMap mapa;
    double lat[], lon[];
    ArrayList<LatLng> marcadores = new ArrayList<LatLng>();
    String titulos[];
    String descripciones[];
    boolean traffic = true;
    String urlRutas = "http://maps.googleapis.com/maps/api/directions/json?origin=4.6382023,-74.0840434&destination=6.26261,-75.57775&sensor=true";
    String urlClima = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=4.53&lon=-74.07&units=metric&mode=JSON&cnt=7";
    Intent deta;
    MarkerOptions focus;
    String tableName = "BaseM";
    int nivel = 1;
    MenuItem item;
    int idFondoTras = R.drawable.ciudad_universitaria;
    private ActionBar bar;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        act = this;
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());
        if (status == ConnectionResult.SUCCESS) {
            mapa = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
        } else {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();
        }
        try {
            deta = new Intent(this, DetailsActivity.class);
            bar = this.getActionBar();
            BitmapDrawable background2 = new BitmapDrawable(
                    BitmapFactory.decodeResource(getResources(),
                            R.drawable.fondoinf));
            bar.setBackgroundDrawable(background2);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setTitle(this.getText(R.string.cobertura_nacional));
            Bundle b = getIntent().getExtras();
            lat = b.getDoubleArray("lat");
            lon = b.getDoubleArray("lon");
            titulos = b.getStringArray("titulos");
            descripciones = b.getStringArray("descripciones");
            zoom = b.getInt("zoom");
            tipo = b.getInt("tipo");
            nivel = b.getInt("nivel");
            cambiar();
            animarCamara(lat[0], lon[0], zoom);
            addNuevos(false);
            tableName = MainActivity.tbName;
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(false);
            mapa.getUiSettings().setCompassEnabled(true);
            MapsInitializer.initialize(MapaActivity.this);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    private void animarCamara(double d, double e, int zoom2) {
        LatLng position = new LatLng(d, e);
        CameraPosition camPos = new CameraPosition.Builder().target(position)
                .zoom(zoom2) // Establecemos el zoom en 19
                .bearing(0) // Establecemos la orientación con el noreste arriba
                .tilt(0) // Bajamos el punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);

        mapa.animateCamera(camUpd3);

    }

    public void ruta(double lat, double lon) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("dir:"
                    + lat + "," + lon));
            startActivity(intent);
        } catch (Exception e) {
            Intent navigation = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?" + "saddr="
                            + MiLocationListener.lat + ","
                            + MiLocationListener.longi + "&daddr=" + lat + ","
                            + lon));
            startActivity(navigation);
        }
        /*
         * Intent deta = new Intent(this, WebActivity.class); double lat2 =
		 * 4.637275;// (float) MiLocationListener.lat; double lon2 = -74.082776;
		 * // (float) MiLocationListener.longi; if (lat2 != 0 && lon2 != 0) {
		 * lat2 = (double) MiLocationListener.lat; lon2 = (double)
		 * MiLocationListener.longi; } deta.putExtra("paginaWeb",
		 * "https://www.google.es/maps/dir/'" + lat2 + "," + lon2 + "'/'" + lat
		 * + "," + lon + "'"); startActivity(deta);
		 */
    }

    private void mostrarMarcador(double lat, double lng, String title,
                                 String desc, int tipo) {
        /*
         * float a = 0; switch (count) { case 0: a =
		 * BitmapDescriptorFactory.HUE_CYAN; break; case 1: a =
		 * BitmapDescriptorFactory.HUE_ORANGE; break; case 2: a =
		 * BitmapDescriptorFactory.HUE_VIOLET; break; case 3: a =
		 * BitmapDescriptorFactory.HUE_YELLOW; break; case 4: a =
		 * BitmapDescriptorFactory.HUE_ORANGE; break;
		 *
		 * default: break; }
		 */
        if (!marcadores.contains(new LatLng(lat, lng))) {
            marcadores.add(new LatLng(lat, lng));
            MarkerOptions k = null;
            if (tipo == 0) {
                k = new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(title)
                        .snippet(desc)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

            } else {
                k = new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(title)
                        .snippet(desc)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                // .fromResource(R.drawable.edificiop2));
            }
            mapa.addMarker(k);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putDoubleArray("lat", lat);
        outState.putDoubleArray("lon", lon);
        outState.putStringArray("titulos", titulos);
        outState.putStringArray("descripciones", descripciones);
        outState.putInt("zoom", zoom);
        outState.putInt("tipo", tipo);
        outState.putInt("nivel", nivel);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle b) {
        lat = b.getDoubleArray("lat");
        lon = b.getDoubleArray("lon");
        titulos = b.getStringArray("titulos");
        descripciones = b.getStringArray("descripciones");
        zoom = b.getInt("zoom");
        tipo = b.getInt("tipo");
        nivel = b.getInt("nivel");
        super.onRestoreInstanceState(b);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                home();
                break;
            case R.id.menu_tipo_mapa:
                cambiar();
                return true;
            case R.id.menu_trafico:
                mapa.setTrafficEnabled(traffic);
                if (traffic) {
                    traffic = false;
                } else {
                    traffic = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void home() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        this.finish();
    }

    private void cambiar() {
        if (tipo == 0) {
            mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            tipo++;
        } else {
            mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            tipo = 0;
        }
    }

    public void acercar(Marker arg0) {
        LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
        SQLiteDatabase db = openOrCreateDatabase(MainActivity.dataBaseName,
                MODE_WORLD_READABLE, null);
        double lat = arg0.getPosition().latitude;
        double lon = arg0.getPosition().longitude;
        String query = "select sede_edificio from edificios where latitud between "
                + (lat - 0.0001)
                + " and "
                + (lat + 0.0001)
                + " and longitud between"
                + (lon - 0.0001)
                + " and "
                + (lon + 0.0001);
        Cursor c = db.rawQuery(query, null);
        String[][] mat = Util.imprimirLista(c);
        cond = Util.getcolumn(mat, 0)[0].trim();
        Log.e("sede", cond);
        nivel++;
        String query2 = "select latitud, longitud,nombre_edificio, url from edificios "
                + "natural join "
                + tableName
                + " natural join enlace where sede_edificio='" + cond + "'";
        // chambonazo mapa
        if (cond.equals("Bogotá")) {
            query2 += " and nivel =" + nivel + " group by nombre_edificio";
        }
        c = db.rawQuery(query2, null);
        mat = Util.imprimirLista(c);
        this.lat = Util.toDouble(Util.getcolumn(mat, 0));
        this.lon = Util.toDouble(Util.getcolumn(mat, 1));
        this.titulos = Util.getcolumn(mat, 2);
        this.descripciones = Util.getcolumn(mat, 3);
        addNuevos(false);
        c.close();
        db.close();
    }

    public void acercar() {
        LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
        SQLiteDatabase db = openOrCreateDatabase(MainActivity.dataBaseName,
                MODE_WORLD_READABLE, null);
        double lat = focus.getPosition().latitude;
        double lon = focus.getPosition().longitude;
        String query = "select latitud, longitud,nombre_edificio, edificio  from edificios where latitud between "
                + (lat - 0.001)
                + " and "
                + (lat + 0.001)
                + " and longitud between"
                + (lon - 0.001)
                + " and "
                + (lon + 0.001);// + " and nivel=4";
        Cursor c = db.rawQuery(query, null);
        String[][] mat = Util.imprimirLista(c);
        this.lat = Util.toDouble(Util.getcolumn(mat, 0));
        this.lon = Util.toDouble(Util.getcolumn(mat, 1));
        this.titulos = Util.getcolumn(mat, 2);
        this.descripciones = Util.getcolumn(mat, 3);
        addNuevos(true);
        c.close();
        db.close();
    }

    public void addNuevos(final boolean b) {
        if (nivel < 3 && !b) {
            mapa.clear();
            marcadores.removeAll(marcadores);
        }
        int type = 0;
        if (b) {
            type = 1;
        }
        count++;
        count = count % 5;
        for (int i = 0; i < lat.length; i++) {
            mostrarMarcador(lat[i], lon[i], titulos[i], descripciones[i], type);
        }
        mapa.setMyLocationEnabled(true);
        mapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng arg0) {
                focus = new MarkerOptions()
                        .position(arg0)
                        .title(act.getString(R.string.markerEti))
                        .snippet(
                                ("lat: " + arg0.latitude).substring(0, 15)
                                        + ("\nlong: " + arg0.longitude)
                                        .substring(0, 15))
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mapa.addMarker(focus);
                acercar();

            }
        });
        mapa.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(final Marker arg0) {

                final String[] items = {act.getText(R.string.indications) + "", act.getText(R.string.informacion) + ""};
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MapaActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

                builder.setTitle(arg0.getTitle()).setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    ruta(arg0.getPosition().latitude,
                                            arg0.getPosition().longitude);
                                } else {
                                    if (tipo == 0) {
                                        tipo = 1;
                                    }
                                    switch (nivel) {
                                        case 1:
                                            zoom = 12;
                                            break;
                                        case 2:
                                            zoom = 17;
                                            break;
                                        case 3:
                                            zoom = 19;
                                            break;

                                        default:
                                            break;
                                    }
                                    animarCamara(arg0.getPosition().latitude,
                                            arg0.getPosition().longitude, zoom);
                                    try {
                                        String consulta = "SELECT departamentos,secciones,"
                                                + "directo,extension,correo_electronico,NOMBRE_EDIFICIO,"
                                                + "url,piso_oficina, LATITUD,LONGITUD FROM "
                                                + tableName
                                                + " natural join edificios"
                                                + " natural join enlace"
                                                + " where ";

                                        ArrayList<String[]> datos = getDatos(
                                                consulta, "NOMBRE_EDIFICIO='"
                                                        + arg0.getTitle() + "'");
                                        if (datos.isEmpty() || nivel < 3) {
                                            acercar(arg0);
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    act.getText(R.string.data_exception),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            deta.putExtra("datos", datos);
                                            animarFondo(cond);
                                            deta.putExtra("fondo", idFondoTras);
                                            startActivity(deta);
                                            cambiar();
                                        }
                                    } catch (Exception e) {
                                        try {
                                            acercar(arg0);
                                        } catch (Exception ex2) {
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    act.getText(R.string.data_exception),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });

                builder.setNegativeButton(act.getText(R.string.cancel_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });
                builder.show();

            }
        });

        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                int len = marker.getTitle().length();
                bar.setTitle(marker.getTitle()
                        .substring(0, len > 20 ? 20 : len));
                return false;
            }
        });

    }

    public ArrayList<String[]> getDatos(String baseConsult, String criteria) {
        String consulta = baseConsult + criteria;
        Log.e("consulta mapa", consulta);
        ArrayList<String[]> datos = new ArrayList<String[]>();
        SQLiteDatabase db = openOrCreateDatabase(MainActivity.dataBaseName,
                MODE_WORLD_READABLE, null);
        Cursor c = db.rawQuery(consulta, null);
        String[][] mat = Util.imprimirLista(c);
        c.close();
        db.close();
        Log.e("datos", Util.toString(mat));
        try {
            for (int i = 0; i < mat.length; i++) {
                String arr[];
                if (mat[i].length == 1) {
                    arr = new String[]{mat[i][0]};
                } else {
                    arr = new String[mat[i].length - 1];
                    for (int j = 0; j < mat[i].length - 1; j++) {
                        if (j == mat[i].length - 2) {
                            arr[j] = mat[i][j] + " " + mat[i][j + 1];
                        } else {
                            arr[j] = mat[i][j];
                        }
                    }
                }
                datos.add(arr);
                Log.e("los datos " + i, Util.toString(arr));
            }
        } catch (Exception e) {
            Log.e("Error Datos", e.toString());
            Toast.makeText(getApplication(), this.getText(R.string.data_exception),
                    Toast.LENGTH_LONG).show();
        }
        return datos;
    }

    public void animarFondo(String cad) {
        int id = R.drawable.fondo;
        Log.e("Seleccionado el fondo", cad);
        if (cad.contains("Bogo")) {
            id = R.drawable.ciudad_universitaria;
            idFondoTras = id;
        }
        if (cad.contains("Amaz")) {
            id = R.drawable.amazonas;
            idFondoTras = id;
        }
        if (cad.contains("Caribe")) {
            id = R.drawable.caribe;
            idFondoTras = id;
        }
        if (cad.contains("Mani")) {
            id = R.drawable.manizales;
            idFondoTras = id;
        }
        if (cad.contains("Mede")) {
            id = R.drawable.medellin;
            idFondoTras = id;
        }
        if (cad.contains("Tumac")) {
            id = R.drawable.tumaco;
            idFondoTras = id;
        }
        if (cad.contains("Palmira")) {
            id = R.drawable.palmira;
            idFondoTras = id;
        }
        if (cad.contains("Orino")) {
            id = R.drawable.orinoquia;
            idFondoTras = id;
        }
        if (cad.contains("Franco")) {
            id = R.drawable.ciudad_universitaria;
            idFondoTras = id;
        }
        if (cad.contains("Museo")) {
            id = R.drawable.ciudad_universitaria;
            idFondoTras = id;
        }
        if (cad.contains("Observatorio")) {
            id = R.drawable.oan_sede_h;
            idFondoTras = id;
        }
    }

}
