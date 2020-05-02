package com.unal.iun.GUI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.unal.iun.Adapters.InstitutionsExpandableAdapter;
import com.unal.iun.LN.IUNDataBase;
import com.unal.iun.LN.Util;
import com.unal.iun.R;

import java.util.ArrayList;
import java.util.List;


public class InstitucionesActivity extends AppCompatActivity {

    private ExpandableListView lv;
    private boolean mode;
    private String table = "colegios";
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituciones);
        IUNDataBase lb = new IUNDataBase(getApplicationContext());
        db = lb.getWritableDatabase();
        android.support.v7.app.ActionBar barra = this.getSupportActionBar();
        barra.setDisplayHomeAsUpEnabled(true);
        barra.setHomeButtonEnabled(true);
        lv = findViewById(R.id.listViewInstituciones);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        int screenHeight = display.getHeight();
        double factor = screenHeight / 2000.0 + 0.25;
        if (factor > 0.15) {
            factor = 0.15;
        }
        lv.setPadding(0, (int) (screenHeight * (factor)), 0, 10);
        lv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        try {
            Bundle b = getIntent().getExtras();
            if (b == null) {
                return;
            }
            String query;
            mode = b.getBoolean("modo");
            if (mode) {
                table = "colegios";
                query = "select NOMBRE_EDIFICIO,direccion_edificio,latitud,longitud,departamento from "
                        + table;
            } else {
                table = "edificios";
                query = "select distinct nombre_edificio,_id_edificio,latitud,longitud,sede_edificio from "
                        + table;
                barra.setTitle(this.getString(R.string.edificios));
            }
            Log.e("buscado", query);
            Cursor c = db.rawQuery(query, null);
            final String[][] mat = Util.imprimirLista(c);
            c.close();
            ArrayList<String> parentItems = new ArrayList<String>();
            ArrayList<List> childItems = new ArrayList<List>();
            String[] ciudades = Util.getcolumn(mat, 4);
            for (int i = 0; i < ciudades.length; i++) {
                String current = ciudades[i];
                if (!parentItems.contains(current)) {
                    parentItems.add(current);
                }
            }
            for (int i = 0; i < parentItems.size(); i++) {
                ArrayList<String> data = new ArrayList<String>();
                childItems.add(data);
            }
            String[] instituciones = Util.getcolumn(mat, 0);
            for (int i = 0; i < instituciones.length; i++) {
                int index = parentItems.indexOf(ciudades[i]);
                if (index < 0) {
                    continue;
                }
                childItems.get(index).add(instituciones[i]);
            }
            InstitutionsExpandableAdapter adapter = new InstitutionsExpandableAdapter(parentItems, childItems, mat, mode);
            adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
            lv.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_instituciones, menu);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_busqueda_instituciones));

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    recargar(query);
                } else {
                    recargar("");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    recargar(newText);
                } else {
                    recargar("");
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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

    public void home() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        this.finish();
    }

    public void recargar(String query) {
        String cad = query.replaceAll("i", "_");
        cad = cad.replaceAll("a", "_");
        cad = cad.replaceAll("e", "_");
        cad = cad.replaceAll("o", "_");
        cad = cad.replaceAll("u", "_");
        cad = cad.replaceAll(" ", "_");
        String cad2 = "";
        if (mode) {
            table = "colegios";
            cad2 = "select nombre_edificio,direccion_edificio,latitud,longitud,departamento from "
                    + table + " where nombre_edificio like('%" + cad + "%')";
        } else {
            table = "edificios";
            cad2 = "select nombre_edificio,_id_edificio,latitud,longitud,sede_edificio,"
                    + "nombre_edificio||_id_edificio as busqueda from "
                    + table
                    + " where busqueda like('%" + cad + "%')";
        }
        Log.e("busqueda", cad2);
        Cursor c = db.rawQuery(cad2, null);
        final String[][] mat = Util.imprimirLista(c);
        c.close();
        ArrayList<String> parentItems = new ArrayList<String>();
        ArrayList<List> childItems = new ArrayList<List>();
        String[] ciudades = Util.getcolumn(mat, 4);
        for (int i = 0; i < ciudades.length; i++) {
            String current = ciudades[i];
            if (!parentItems.contains(current)) {
                parentItems.add(current);
            }
        }
        for (int i = 0; i < parentItems.size(); i++) {
            ArrayList<String> data = new ArrayList<String>();
            childItems.add(data);
        }
        String[] instituciones = Util.getcolumn(mat, 0);
        for (int i = 0; i < instituciones.length; i++) {
            childItems.get(parentItems.indexOf(ciudades[i])).add(instituciones[i]);
        }
        InstitutionsExpandableAdapter adapter = new InstitutionsExpandableAdapter(parentItems, childItems, mat, mode);
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        lv.setAdapter(adapter);
        lv.expandGroup(0);
    }

}
