package com.unal.iun.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unal.iun.LN.Util;
import com.unal.iun.R;

import java.util.ArrayList;

public class MiAdaptador extends BaseAdapter {
    /*TYPE_COMPLETE con imagen titulo y subtitulo*/
    public static final int TYPE_COMPLETE = 0;
    /*TYPE_SIMPLE solo el titulo*/
    public static final int TYPE_SIMPLE = 1;
    /*TYPE_IMAGE con imagen sin subtitulo*/
    public static final int TYPE_IMAGE = 2;
    private final Activity actividad;
    private final String[] lista;
    private final String[] lista2;
    public Typeface fuente;
    public int tipo;

    public MiAdaptador(Activity actividad, String[] titulos,
                       String[] subtitulos, int tipo) {
        super();
        this.actividad = actividad;
        this.lista = titulos;
        this.lista2 = subtitulos;
        this.tipo = tipo;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {

            LayoutInflater inflater = actividad.getLayoutInflater();
            view = inflater.inflate(R.layout.elemento_lista, null, true);
        }
        TextView textView = view.findViewById(R.id.titulo);
        TextView textView2 = view.findViewById(R.id.subtitulo);
        ImageView imageView = view.findViewById(R.id.icono);

        String element = lista[position];
        if (element != null) {
            textView.setText(Util.toCamelCase(element.toLowerCase()));
            textView.setHint(Util.toCamelCase(element.toLowerCase()));
            if (tipo != 0) {
                if (element.contains("Museo Paleon")) {
                    textView.setText("\t Museo Paleontologico");
                    textView.setTextSize(15);
                }
                if (element.contains("Estación de Biolo")) {
                    textView.setText("\t Estación de Biología Tropical Roberto Franco");
                    textView.setTextSize(15);
                }
            }
        }
        imageView.setImageResource(R.drawable.icono_app);
        int[] ids = {R.id.titulo, R.id.subtitulo};
        for (int value : ids) {
            TextView prueba = view.findViewById(value);
            prueba.setTypeface(fuente);
        }
        String element2 = lista2[position];
        if (element2 != null) {
            textView2
                    .setText(Util.toCamelCase(element2.toLowerCase()));
            if (tipo > 0 && tipo < 3) {
                textView2.setText("");
            }
            if (element2.contains("Bogo")) {
                imageView.setImageResource(R.drawable.ic_bogota);
            }
            if (element2.contains("Amaz")) {
                imageView.setImageResource(R.drawable.ic_amazonia);
            }
            if (element2.contains("Caribe")) {
                imageView.setImageResource(R.drawable.ic_caribe);
            }
            if (element2.contains("Mani")) {
                imageView.setImageResource(R.drawable.ic_manizales);
            }
            if (element2.contains("Mede")) {
                imageView.setImageResource(R.drawable.ic_medellin);
            }
            if (element2.contains("Tumac")) {
                imageView.setImageResource(R.drawable.ic_tumaco);
            }
            if (element2.contains("Palmira")) {
                imageView.setImageResource(R.drawable.ic_palmira);
            }
            if (element2.contains("Orino")) {
                imageView.setImageResource(R.drawable.ic_oriniquia);
            }
            if (tipo == 2) {
                String cad = element2;
                cad = cad.substring(0, cad.length() - 4);
                int id = actividad.getResources().getIdentifier(
                        "com.unal.iun:drawable/" + cad, null, null);
                imageView.setImageResource(id);
            }
            if (tipo == 1 || tipo == 3) {
                imageView.setVisibility(View.GONE);
            }
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