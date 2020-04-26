package com.unal.iun.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unal.iun.LN.Util;
import com.unal.iun.R;

public class DirectoryAdapter extends BaseAdapter {

    private final String[] elements;
    private final LayoutInflater layoutInflater;

    public DirectoryAdapter(LayoutInflater layoutInflater, String[] titulos) {
        super();
        this.layoutInflater = layoutInflater;
        this.elements = titulos;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.simple_text_list_item, null, true);
        }
        TextView textView = view.findViewById(R.id.titulo);
        String element = elements[position];
        if (element != null) {
            textView.setText(Util.toCamelCase(element.toLowerCase()));
            textView.setHint(Util.toCamelCase(element.toLowerCase()));
            if (element.contains("Museo Paleon")) {
                textView.setText("\t Museo Paleontologico");
                textView.setTextSize(15);
            }
            if (element.contains("Estación de Biolo")) {
                textView.setText("\t Estación de Biología Tropical Roberto Franco");
                textView.setTextSize(15);
            }
        }
        return view;
    }

    public int getCount() {
        return elements.length;
    }

    public Object getItem(int arg0) {
        return elements[arg0];
    }

    public long getItemId(int position) {
        return position;
    }
}