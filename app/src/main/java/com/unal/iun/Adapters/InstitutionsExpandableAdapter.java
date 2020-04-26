package com.unal.iun.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.unal.iun.GUI.MapaActivity;
import com.unal.iun.LN.IUNDataBase;
import com.unal.iun.LN.Util;
import com.unal.iun.R;
import com.unal.iun.data.MapMarker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InstitutionsExpandableAdapter extends BaseExpandableListAdapter {

    private String[][] mat;
    private boolean mode;
    private Activity activity;
    private ArrayList<List> childtems;
    private LayoutInflater inflater;
    private ArrayList<String> parentItems, child;

    public InstitutionsExpandableAdapter(ArrayList<String> parents,
                                         ArrayList<List> children, String[][] data, boolean mode) {

        this.parentItems = parents;
        this.childtems = children;
        this.mat = data;
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

        TextView textView;
        TextView textView2;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.itemsub, null);
        }
        final int posicion = (int) getChildId(groupPosition, childPosition);
        textView = convertView.findViewById(R.id.textItem);
        textView.setText(Util.toCamelCase(mat[posicion][0].trim().toLowerCase()));
        textView2 = convertView.findViewById(R.id.textItemSub);
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
            IUNDataBase lb = new IUNDataBase(activity.getApplicationContext());
            SQLiteDatabase db = lb.getReadableDatabase();
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
            double lat, lon;
            ArrayList<MapMarker> markers = new ArrayList<>();
            for (String[] strings : mat) {
                lat = Double.parseDouble(strings[2]);
                lon = Double.parseDouble(strings[3]);
                markers.add(new MapMarker(new LatLng(lat, lon)
                        , strings[1]
                        , strings[0]
                        , 0
                        , BitmapDescriptorFactory.HUE_VIOLET));
            }
            mapa.putExtra(MapaActivity.ARG_MARKERS, markers);
            mapa.putExtra(MapaActivity.ARG_LEVEL, 3);
            mapa.putExtra(MapaActivity.ARG_ZOOM, 18);
            mapa.putExtra(MapaActivity.ARG_TYPE, 1);
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
        String cad = parentItems.get(groupPosition).toUpperCase(Locale.getDefault());
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
        return child;
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