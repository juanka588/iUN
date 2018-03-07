package com.unal.iun.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unal.iun.LN.LinnaeusDatabase;
import com.unal.iun.LN.MiLocationListener;
import com.unal.iun.LN.Util;
import com.unal.iun.R;
import com.unal.iun.data.DetailedInformation;
import com.unal.iun.data.InformationElement;
import com.unal.iun.data.MapMarker;

import java.util.ArrayList;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String ARG_MARKERS = "markers";
    public static final String ARG_LEVEL = "nivel";
    public static final String ARG_ZOOM = "zoom";
    public static final String ARG_TYPE = "tipo";
    private static final String TAG = MapaActivity.class.getSimpleName();
    private static int type = 1, zoom = 19;
    private static String cond = "";
    private ArrayList<MapMarker> markers = new ArrayList<>();
    private GoogleMap mMap;
    private boolean traffic = true;
    private Intent deta;
    private MapMarker focus;
    private String tableName = "BaseM";
    private int nivel = 1;
    private int idFondoTras = R.drawable.ciudad_universitaria;
    private android.support.v7.app.ActionBar bar;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        setUpMapIfNeeded();
        handleToolBar();
    }

    private void handleToolBar() {
        bar = this.getSupportActionBar();
        BitmapDrawable background2 = new BitmapDrawable(getApplicationContext().getResources(),
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.fondoinf));
        bar.setBackgroundDrawable(background2);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle(this.getText(R.string.cobertura_nacional));
    }

    private void handleBundle() {
        deta = new Intent(this, DetailsActivity.class);
        Bundle b = getIntent().getExtras();
        markers = b.getParcelableArrayList(ARG_MARKERS);
        zoom = b.getInt(ARG_ZOOM);
        type = b.getInt(ARG_TYPE);
        nivel = b.getInt(ARG_LEVEL);
        changeMapType();
        animarCamara(markers.get(0).getPosition(), zoom);
        tableName = MainActivity.tbName;
        addNewMarkers(false, new ArrayList<MapMarker>());
    }


    private void setUpMapIfNeeded() {
        act = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void animarCamara(LatLng position, int zoom2) {
        CameraPosition camPos = new CameraPosition.Builder().target(position)
                .zoom(zoom2) // Establecemos el zoom en 19
                .bearing(0) // Establecemos la orientación con el noreste arriba
                .tilt(0) // Bajamos el punto de vista de la cámara 70 grados
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARG_MARKERS, markers);
        outState.putInt(ARG_ZOOM, zoom);
        outState.putInt(ARG_TYPE, type);
        outState.putInt(ARG_LEVEL, nivel);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle b) {
        markers = b.getParcelableArrayList(ARG_MARKERS);
        showMarkers();
        zoom = b.getInt(ARG_ZOOM);
        type = b.getInt(ARG_TYPE);
        nivel = b.getInt(ARG_LEVEL);
        animarCamara(markers.get(0).getPosition(), zoom);
        tableName = MainActivity.tbName;
        addNewMarkers(false, new ArrayList<MapMarker>());
        super.onRestoreInstanceState(b);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                home();
                break;
            case R.id.menu_tipo_mapa:
                changeMapType();
                return true;
            case R.id.menu_trafico:
                if (mMap == null) {
                    return true;
                }
                mMap.setTrafficEnabled(traffic);
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

    private void changeMapType() {
        if (mMap == null) {
            return;
        }
        if (type == 0) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            type++;
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            type = 0;
        }
    }

    public void zoomIn(Marker arg0) {
        LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
        SQLiteDatabase db = lb.getReadableDatabase();
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
        // chambonazo mMap
        if (cond.equals("Bogotá")) {
            query2 += " and nivel =" + nivel + " group by nombre_edificio";
        }
        c = db.rawQuery(query2, null);
        mat = Util.imprimirLista(c);
        for (int i = 0; i < mat.length; i++) {
            lat = Double.parseDouble(mat[i][0]);
            lon = Double.parseDouble(mat[i][1]);
            markers.add(new MapMarker(new LatLng(lat, lon)
                    , mat[i][2]
                    , mat[i][3]
                    , 0
                    , BitmapDescriptorFactory.HUE_VIOLET));
        }
        addNewMarkers(false, new ArrayList<MapMarker>());
        c.close();
        db.close();
    }

    public void zoomIn() {
        LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
        SQLiteDatabase db = lb.getReadableDatabase();
        double lat = focus.getPosition().latitude;
        double lon = focus.getPosition().longitude;
        String query = "select latitud, longitud,nombre_edificio, edificios._id  from edificios where latitud between "
                + (lat - 0.001)
                + " and "
                + (lat + 0.001)
                + " and longitud between"
                + (lon - 0.001)
                + " and "
                + (lon + 0.001);// + " and nivel=4";
        Cursor c = db.rawQuery(query, null);
        String[][] mat = Util.imprimirLista(c);
        ArrayList<MapMarker> newMarks = new ArrayList<>();
        for (int i = 0; i < mat.length; i++) {
            lat = Double.parseDouble(mat[i][0]);
            lon = Double.parseDouble(mat[i][1]);
            newMarks.add(new MapMarker(new LatLng(lat, lon)
                    , mat[i][2]
                    , mat[i][3]
                    , 0
                    , BitmapDescriptorFactory.HUE_VIOLET));
        }
        addNewMarkers(true, newMarks);
        c.close();
        db.close();
    }

    /**
     * @param mustClear defines if the map must clear in case of zoom in
     * @param newMarks  list new MapMarks to add to current list
     */
    public void addNewMarkers(boolean mustClear, ArrayList<MapMarker> newMarks) {
        if (nivel < 3 && !mustClear) {
            mMap.clear();
            markers.removeAll(markers);
        }
        int type = 0;
        if (mustClear) {
            type = 1;
        }
        manageEventsOnMap();
        for (MapMarker marker : newMarks) {
            if (!markers.contains(marker)) {
                markers.add(marker);
            }
        }
        showMarkers();
    }

    private void showMarkers() {
        for (MapMarker marker : markers) {
            Log.e(TAG, marker.getTitle());
            mMap.addMarker(getMarkerOption(marker));
        }
    }

    private void manageEventsOnMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng arg0) {
                Log.e(TAG, "long pressed");
                String title = getApplicationContext().getString(R.string.markerEti);
                String description = ("lat: " + arg0.latitude).substring(0, 15)
                        + ("\nlong: " + arg0.longitude)
                        .substring(0, 15);
                focus = new MapMarker(arg0, title, description, 1, (int) BitmapDescriptorFactory.HUE_RED);
                mMap.addMarker(getMarkerOption(focus));
                zoomIn();
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(final Marker arg0) {

                final String[] items = {act.getText(R.string.indications) + "", act.getText(R.string.informacion) + ""};
                AlertDialog.Builder builder;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    builder = new AlertDialog.Builder(
                            MapaActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                } else {
                    builder = new AlertDialog.Builder(MapaActivity.this);
                }

                builder.setTitle(arg0.getTitle()).setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    ruta(arg0.getPosition().latitude,
                                            arg0.getPosition().longitude);
                                } else {
                                    if (MapaActivity.type == 0) {
                                        MapaActivity.type = 1;
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
                                    animarCamara(arg0.getPosition(), zoom);

                                    String consulta = "SELECT secciones,"
                                            + "directo,extension,correo_electronico,"
                                            + "url,piso_oficina,NOMBRE_EDIFICIO,edificios._id, LATITUD,LONGITUD FROM "
                                            + tableName
                                            + " inner join edificios"
                                            + " on BaseM._id_edificio=edificios._id"
                                            + " inner join enlace"
                                            + " on " + tableName + "._id_enlace=enlace._id where ";

                                    ArrayList<DetailedInformation> data = getData(
                                            consulta, "NOMBRE_EDIFICIO='"
                                                    + arg0.getTitle() + "'");
                                    if (data.isEmpty() || nivel < 3) {
                                        zoomIn(arg0);
                                        Toast.makeText(
                                                getApplicationContext(),
                                                act.getText(R.string.data_exception),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        deta.putExtra(DetailsFragment.ARG_TITLE, getString(R.string.build) + arg0.getTitle());
                                        deta.putExtra(DetailsFragment.ARG_DATA, data);
                                        animarFondo(cond);
                                        deta.putExtra(DetailsFragment.ARG_BACKGROUND, idFondoTras);
                                        startActivity(deta);
                                        changeMapType();
                                    }
                                }
                            }
                        });

                builder.setNegativeButton(act.getText(R.string.cancel_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                int len = marker.getTitle().length();
                bar.setTitle(marker.getTitle()
                        .substring(0, len > 20 ? 20 : len));
                return false;
            }
        });
    }

    private MarkerOptions getMarkerOption(MapMarker focus) {
        MarkerOptions options = new MarkerOptions()
                .position(focus.getPosition())
                .title(focus.getTitle())
                .snippet(focus.getDescription())
                .icon(BitmapDescriptorFactory.defaultMarker(focus.getIcon()));
        return options;
    }

    public ArrayList<DetailedInformation> getData(String baseConsult, String criteria) {
        String consulta = baseConsult + criteria;
        Log.e("consulta mMap", consulta);
        LinnaeusDatabase lin = new LinnaeusDatabase(getApplicationContext());
        SQLiteDatabase db = lin.getReadableDatabase();
        Cursor c = db.rawQuery(consulta, null);
        String[][] mat = Util.imprimirLista(c);
        c.close();
        db.close();
        Log.e("datos", Util.toString(mat));
        ArrayList<InformationElement> infos = new ArrayList<>();
        DetailedInformation dt;
        ArrayList<DetailedInformation> detailedInformations = new ArrayList<>();
        for (int i = 0; i < mat.length; i++) {
            int size = mat[i].length;
            for (int j = 1; j < size - 4; j++) {
                if (mat[i][j] != null) {
                    infos.add(new InformationElement(mat[i][j]));
                    Log.e("info", mat[i][j]);
                }
            }
            infos.add(new InformationElement(mat[i][size - 4], mat[i][size - 3], mat[i][size - 2], mat[i][size - 1]));
            Log.e("title", mat[i][0]);
            dt = new DetailedInformation(infos, mat[i][0]);
            detailedInformations.add(dt);
            infos = new ArrayList<>();
        }

        return detailedInformations;
    }

    public void animarFondo(String cad) {
        int id;
        Log.e("Seleccionado el fondo", cad);
        if (cad.contains("Bogo")) {
            id = R.drawable.ciudad_universitaria;
            idFondoTras = id;
        } else if (cad.contains("Amaz")) {
            id = R.drawable.amazonas;
            idFondoTras = id;
        } else if (cad.contains("Caribe")) {
            id = R.drawable.caribe;
            idFondoTras = id;
        } else if (cad.contains("Mani")) {
            id = R.drawable.manizales;
            idFondoTras = id;
        } else if (cad.contains("Mede")) {
            id = R.drawable.medellin;
            idFondoTras = id;
        } else if (cad.contains("Tumac")) {
            id = R.drawable.tumaco;
            idFondoTras = id;
        } else if (cad.contains("Palmira")) {
            id = R.drawable.palmira;
            idFondoTras = id;
        } else if (cad.contains("Orino")) {
            id = R.drawable.orinoquia;
            idFondoTras = id;
        } else if (cad.contains("Franco")) {
            id = R.drawable.ciudad_universitaria;
            idFondoTras = id;
        } else if (cad.contains("Museo")) {
            id = R.drawable.ciudad_universitaria;
            idFondoTras = id;
        } else if (cad.contains("Observatorio")) {
            id = R.drawable.oan_sede_h;
            idFondoTras = id;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        MapsInitializer.initialize(MapaActivity.this);
        handleBundle();
    }
}
