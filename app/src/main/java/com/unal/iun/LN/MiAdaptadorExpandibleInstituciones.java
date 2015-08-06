package com.unal.iun.LN;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.unal.iun.MapaActivity;
import com.unal.iun.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MiAdaptadorExpandibleInstituciones extends
        BaseExpandableListAdapter {

    public Typeface fuente;
    public String mat[][];
    public int parentsSize;
    public boolean mode;
    double lat[];
    double lon[];
    String titulos[], descripciones[];
    private Activity activity;
    private ArrayList<List> childtems;
    private LayoutInflater inflater;
    private ArrayList<String> parentItems, child;

    public MiAdaptadorExpandibleInstituciones(ArrayList<String> parents,
                                              ArrayList<List> childern, String data[][], boolean mode) {

        this.parentItems = parents;

        this.childtems = childern;

        this.mat = data;

        this.parentsSize = parents.size() - 1;

        this.mode = mode;

    }

    public void setInflater(LayoutInflater inflater, Activity activity) {

        this.inflater = inflater;

        this.activity = activity;

    }

    @SuppressWarnings("unchecked")
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        child = (ArrayList<String>) childtems.get(groupPosition);

        TextView textView = null;
        TextView textView2 = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.itemsub, null);
        }
        final int posicion = (int) getChildId(groupPosition, childPosition);
        textView = (TextView) convertView.findViewById(R.id.textItem);
        textView.setText(Util.toCammelCase(mat[posicion][0].trim()
                .toLowerCase()));
        textView2 = (TextView) convertView.findViewById(R.id.textItemSub);
        textView2.setText(mat[posicion][1].trim());
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                double lat = Double.parseDouble(mat[posicion][2]);
                double lon = Double.parseDouble(mat[posicion][3]);
                ubicar(lat, lon);
            }

        });
        return convertView;

    }

    public void ubicar(double lt, double lg) {
        try {
            Intent mapa = new Intent(activity, MapaActivity.class);
            String query;
            LinnaeusDatabase lb = new LinnaeusDatabase(
                    activity.getApplicationContext());
            SQLiteDatabase db = activity.openOrCreateDatabase(
                    LinnaeusDatabase.DATABASE_NAME, activity.MODE_WORLD_READABLE,
                    null);
            String condicion = "latitud between " + (lt - 0.0001) + " and "
                    + (lt + 0.0001) + " and longitud between" + (lg - 0.0001)
                    + " and " + (lg + 0.0001);
            if (mode) {
                query = "select distinct direccion_edificio,nombre_edificio,latitud,longitud,departamento from"
                        + " colegios" + " where " + condicion;
            } else {
                query = "select distinct _id_edificio,nombre_edificio,latitud,longitud from edificios "
                        + "where " + condicion;
            }
            Log.e("buscado", query);
            Cursor c = db.rawQuery(query, null);
            String[][] mat = Util.imprimirLista(c);
            c.close();
            db.close();
            if (mat.length == 0) {
                Toast.makeText(activity.getApplicationContext(),
                        activity.getText(R.string.data_exception), Toast.LENGTH_SHORT).show();
                return;
            }
            lat = Util.toDouble(Util.getcolumn(mat, 2));
            lon = Util.toDouble(Util.getcolumn(mat, 3));
            titulos = Util.getcolumn(mat, 1);
            descripciones = Util.getcolumn(mat, 0);
            mapa.putExtra("lat", lat);
            mapa.putExtra("lon", lon);
            mapa.putExtra("titulos", titulos);
            mapa.putExtra("descripciones", descripciones);
            mapa.putExtra("nivel", 3);
            mapa.putExtra("zoom", 18);
            mapa.putExtra("tipo", 1);
            activity.startActivity(mapa);
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (Exception ex) {
            Toast.makeText(activity.getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grupo, null);

        }
        String cad = parentItems.get(groupPosition).toUpperCase(
                Locale.getDefault());
        CheckedTextView ctv = ((CheckedTextView) convertView);
        ctv.setText("\t\t" + cad + "\n");
        ctv.setChecked(isExpanded);
        return convertView;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        int totalGroupPosition = 0;
        for (int i = 0; i < groupPosition; i++) {
            totalGroupPosition += childtems.get(i).size();
        }
        return totalGroupPosition + childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((ArrayList<String>) childtems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return (ArrayList<String>) child;
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
