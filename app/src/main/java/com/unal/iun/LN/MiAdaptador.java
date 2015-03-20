package com.unal.iun.LN;

import java.util.ArrayList;

import com.unal.iun.R;
import com.unal.iun.R.drawable;
import com.unal.iun.R.id;
import com.unal.iun.R.layout;

import android.app.Activity;
import android.content.ContextWrapper;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MiAdaptador extends BaseAdapter {
	private final Activity actividad;
	private final String[] lista;
	private final String[] lista2;
	public Typeface fuente;
	// 0 con todo 1 sin imagen ni subtitulo 2 con imagen y sin subtitulo
	// 3 con titulo y subtitulo sin imagen
	public int tipo = 0;

	public MiAdaptador(Activity actividad, ArrayList<String> titulos,
			ArrayList<String> subtitulos, int tipo) {
		super();
		this.actividad = actividad;
		lista = new String[titulos.size()];
		titulos.toArray(lista);
		lista2 = new String[subtitulos.size()];
		subtitulos.toArray(lista2);
		this.tipo = tipo;
	}

	public MiAdaptador(Activity actividad, String[] titulos, String[] subtitulos) {
		super();
		this.actividad = actividad;
		this.lista = titulos;
		this.lista2 = subtitulos;
		this.tipo = 1;
	}

	public MiAdaptador(Activity actividad, String[] titulos,
			String[] subtitulos, int tipo) {
		super();
		this.actividad = actividad;
		this.lista = titulos;
		this.lista2 = subtitulos;
		this.tipo = tipo;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = actividad.getLayoutInflater();
		View view = inflater.inflate(layout.elemento_lista, null, true);
		TextView textView = (TextView) view.findViewById(id.titulo);
		TextView textView2 = (TextView) view.findViewById(id.subtitulo);
		ImageView imageView = (ImageView) view.findViewById(id.icono);

		if (lista[position] != null) {
			textView.setText(Util.toCammelCase(lista[position].toLowerCase()));
			textView.setHint(Util.toCammelCase(lista[position].toLowerCase()));
			if (tipo != 0) {
				if (lista[position].contains("Museo Paleon")) {
					textView.setText("\t Museo Paleontologico");
					textView.setTextSize(15);
				}
				if (lista[position].contains("Estaci�n de Biolo")) {
					textView.setText("\t Estaci�n de Biolog�a Tropical Roberto Franco");
					textView.setTextSize(15);
				}
			}
		}
		imageView.setImageResource(drawable.icono_app);
		int ids[] = { id.titulo, id.subtitulo };
		for (int i = 0; i < ids.length; i++) {
			TextView prueba = (TextView) view.findViewById(ids[i]);
			prueba.setTypeface(fuente);
		}
		if (lista2[position] != null) {
			textView2
					.setText(Util.toCammelCase(lista2[position].toLowerCase()));
			if (tipo > 0 && tipo < 3) {
				textView2.setText("");
			}
			if (lista2[position].contains("Bogo")) {
				imageView.setImageResource(drawable.ic_bogota);
			}
			if (lista2[position].contains("Amaz")) {
				imageView.setImageResource(drawable.ic_amazonia);
			}
			if (lista2[position].contains("Caribe")) {
				imageView.setImageResource(drawable.ic_caribe);
			}
			if (lista2[position].contains("Mani")) {
				imageView.setImageResource(drawable.ic_manizales);
			}
			if (lista2[position].contains("Mede")) {
				imageView.setImageResource(drawable.ic_medellin);
			}
			if (lista2[position].contains("Tumac")) {
				imageView.setImageResource(drawable.ic_tumaco);
			}
			if (lista2[position].contains("Palmira")) {
				imageView.setImageResource(drawable.ic_palmira);
			}
			if (lista2[position].contains("Orino")) {
				imageView.setImageResource(drawable.ic_oriniquia);
			}
			if (tipo == 2) {
				// Log.e("iconos en servicios",lista2[position]);
				String cad = lista2[position];
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