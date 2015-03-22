package com.unal.iun.LN;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unal.iun.R.id;
import com.unal.iun.R.layout;

import java.util.ArrayList;

public class MiAdaptadorEventos extends BaseAdapter {
    private final Activity actividad;
    private final String[][] lista;
    public Typeface fuente;

    public MiAdaptadorEventos(Activity actividad, String[][] titulos) {
        super();
        this.actividad = actividad;
        this.lista = titulos;
    }

    public MiAdaptadorEventos(Activity actividad, ArrayList<String[]> titulos) {
        super();
        this.actividad = actividad;
        lista = new String[titulos.size()][titulos.get(0).length];
        for (int i = 0; i < lista.length; i++) {
            for (int j = 0; j < lista[i].length; j++) {
                lista[i][j] = titulos.get(i)[j];
            }
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = actividad.getLayoutInflater();
        View view = inflater.inflate(layout.elemento_lista_eventos, null,
                true);
        TextView textView = (TextView) view.findViewById(id.tituloEvento);
        if (lista[position][0] != null) {
            textView.setText(Util.toCammelCase(lista[position][0].toLowerCase()));
            textView.setHint(Util.toCammelCase(lista[position][0].toLowerCase()));
        }
        TextView textView2 = (TextView) view.findViewById(id.descripcionEvento);
        if (lista[position][1] != null) {
            textView2.setText(Util.toCammelCase(lista[position][1].toLowerCase()));
            textView2.setHint(Util.toCammelCase(lista[position][1].toLowerCase()));
        }
        TextView textView3 = (TextView) view.findViewById(id.lugarEvento);
        if (lista[position][2] != null) {
            textView3.setText(Util.toCammelCase(lista[position][2].toLowerCase()));
            textView3.setHint(Util.toCammelCase(lista[position][2].toLowerCase()));
        }
        TextView textView4 = (TextView) view.findViewById(id.fechaEvento);
        if (lista[position][3] != null) {
            textView4.setText(Util.toCammelCase(lista[position][3].toLowerCase()));
            textView4.setHint(Util.toCammelCase(lista[position][3].toLowerCase()));
        }
        TextView textView5 = (TextView) view.findViewById(id.emailEvento);
        if (lista[position][4] != null) {
            textView5.setText(Util.toCammelCase(lista[position][4].toLowerCase()));
            textView5.setHint(Util.toCammelCase(lista[position][4].toLowerCase()));
        }
        TextView textView6 = (TextView) view.findViewById(id.sitioWEBEvento);
        if (lista[position][5] != null) {
            textView6.setText(Util.toCammelCase(lista[position][5].toLowerCase()));
            textView6.setHint(Util.toCammelCase(lista[position][5].toLowerCase()));
        }
        TextView textView7 = (TextView) view.findViewById(id.telefonoEvento);
        if (lista[position][6] != null) {
            textView7.setText(Util.toCammelCase(lista[position][6].toLowerCase()));
            textView7.setHint(Util.toCammelCase(lista[position][6].toLowerCase()));
        }

        int ids[] = {id.tituloEvento, id.lugarEvento, id.fechaEvento,
                id.descripcionEvento, id.emailEvento, id.sitioWEBEvento,
                id.telefonoEvento};
        for (int i = 0; i < ids.length; i++) {
            TextView prueba = (TextView) view.findViewById(ids[i]);
            prueba.setTypeface(fuente);
        }
        return view;
    }


    public int getCount() {
        return lista.length;
    }

    public Object getItem(int arg0) {
        return lista[arg0];
    }

    public long getItemId(int position) {
        return position;
    }
}