package com.unal.iun.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.unal.iun.Adapters.WebMenuRecyclerViewAdapter;
import com.unal.iun.DataSource.DirectoryContract;
import com.unal.iun.LN.LinnaeusDatabase;
import com.unal.iun.LN.Util;
import com.unal.iun.R;
import com.unal.iun.data.WebItem;

import java.util.ArrayList;
import java.util.List;


public class MenuWEBActivity extends AppCompatActivity {

    private static String GENERAL_INTEREST = "0";
    private static String UN_COMMUNITY = "1";
    private static String COMMUNITY_SERVICES = "2";
    private SearchView sv;
    private RecyclerView list_general_interest;
    private RecyclerView list_communityUN;
    private RecyclerView list_community_services;
    private double lat[];
    private double lon[];
    private String titulos[], descripciones[];
    private boolean colegios = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu_web);
        list_general_interest = (RecyclerView) findViewById(R.id.list_general_interest);
        list_communityUN = (RecyclerView) findViewById(R.id.list_un_community);
        list_community_services = (RecyclerView) findViewById(R.id.list_community_services);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        int screenWidth, screenHeight, dpi;
        float density;
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        density = metrics.density;
        dpi = metrics.densityDpi;
        int spaces = (int) ((screenWidth) / (300));
        Util.log("spaces ", screenWidth + " " + density + " " + spaces);
        LinnaeusDatabase ln = new LinnaeusDatabase(getApplicationContext());
        SQLiteDatabase db = ln.dataBase;
        Typeface font = Typeface
                .createFromAsset(getAssets(), "Helvetica.ttf");
        WebMenuRecyclerViewAdapter adapter = new WebMenuRecyclerViewAdapter(initData(db,
                GENERAL_INTEREST), this, font);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getApplicationContext(), spaces);
        GridLayoutManager gridLayoutManager2 =
                new GridLayoutManager(getApplicationContext(), spaces);
        GridLayoutManager gridLayoutManager3 =
                new GridLayoutManager(getApplicationContext(), spaces);
        list_general_interest.setLayoutManager(gridLayoutManager);
        list_general_interest.setAdapter(adapter);
        WebMenuRecyclerViewAdapter adapter2 = new WebMenuRecyclerViewAdapter(initData(db,
                UN_COMMUNITY), this, font);
        WebMenuRecyclerViewAdapter adapter3 = new WebMenuRecyclerViewAdapter(initData(db,
                COMMUNITY_SERVICES), this, font);
        list_communityUN.setLayoutManager(gridLayoutManager2);
        list_community_services.setLayoutManager(gridLayoutManager3);
        list_communityUN.setAdapter(adapter2);
        list_community_services.setAdapter(adapter3);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private List<WebItem> initData(SQLiteDatabase db, String filter) {
        List<WebItem> items = new ArrayList<>();
        Cursor cursor = db.query(DirectoryContract.EnlacesProvider.TABLE_NAME,
                DirectoryContract.EnlacesProvider.COLUMN_NAMES,
                DirectoryContract.EnlacesProvider.COLUMN_ICON + " IS NOT NULL AND " +
                        DirectoryContract.EnlacesProvider.COLUMN_COMMUNITY_SERVICE + "=?",
                new String[]{filter},
                null,
                null,
                DirectoryContract.EnlacesProvider.COLUMN_ORDER);
        String mat[][] = Util.imprimirLista(cursor);
        for (int i = 0; i < mat.length; i++) {
            String cad = mat[i][1] + "";
            if (mat[i][1].contains(".")) {
                try {
                    cad = cad.substring(0, cad.length() - 4);
                    Util.log("imagen", cad);
                } catch (Exception e) {
                    Util.log("error data base", e.toString());
                }
            }
            int icon = this.getResources().getIdentifier("drawable/" + cad, null,
                    this.getPackageName());
            items.add(new WebItem(mat[i][0], icon, mat[i][2], false));
        }
        cursor.close();
        Util.log("Cambio", filter);
        return items;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                home();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void navegar(String cad) {
        Util.irA(cad, this);
    }

    @Override
    public void onBackPressed() {
        home();
        super.onBackPressed();
    }

    public void home(View v) {
        home();
    }

    public void home() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu_web, menu);
        MenuItem menuItem = menu.getItem(0);
        sv = (SearchView) MenuItemCompat.getActionView(menuItem);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                String cad = "http://unal.edu.co/resultados-de-la-busqueda/?q="
                        + arg0;
                navegar(cad);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void irA(View v) {
        Intent radio = new Intent(this, RadioActivity.class);
        startActivity(radio);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return;
    }

    private void preguntar(final Activity act) {
        final String[] items = {this.getString(R.string.instituciones), this.getString(R.string.informacion), this.getString(R.string.edificios)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getString(R.string.admisiones)).setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            try {
                                Intent ca = new Intent(act,
                                        InstitucionesActivity.class);
                                ca.putExtra("modo", true);
                                startActivity(ca);
                                overridePendingTransition(R.anim.fade_in,
                                        R.anim.fade_out);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),
                                        act.getText(R.string.disponible), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (item == 1) {
                            String cad = "http://admisiones.unal.edu.co/";
                            Util.irA(cad, act);
                        } else {
                            Intent ca = new Intent(act, InstitucionesActivity.class);
                            ca.putExtra("modo", false);
                            startActivity(ca);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    }
                });

        builder.setNegativeButton(this.getText(R.string.cancel_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void ubicar() {
        try {
            Intent mapa = new Intent(this, MapaActivity.class);
            LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
            SQLiteDatabase db = lb.dataBase;
            String query;

            query = "select distinct edificio._id,nombre_edificio,latitud,longitud from edificios "
                    + " where nivel=" + 5;

            Cursor c = db.rawQuery(query, null);
            String[][] mat = Util.imprimirLista(c);
            c.close();
            db.close();
            lat = Util.toDouble(Util.getcolumn(mat, 2));
            lon = Util.toDouble(Util.getcolumn(mat, 3));
            titulos = Util.getcolumn(mat, 1);
            descripciones = Util.getcolumn(mat, 0);
            mapa.putExtra("lat", lat);
            mapa.putExtra("lon", lon);
            mapa.putExtra("titulos", titulos);
            mapa.putExtra("descripciones", descripciones);
            mapa.putExtra("nivel", 3);
            mapa.putExtra("zoom", 15);
            mapa.putExtra("type", 1);
            startActivity(mapa);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (Exception ex) {
        }
    }
}
