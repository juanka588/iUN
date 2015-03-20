package com.unal.iun.LN;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unal.iun.DetailsActivity;
import com.unal.iun.MapaActivity;
import com.unal.iun.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class MiAdaptadorExpandibleDetalles extends BaseExpandableListAdapter {

	private Activity activity;

	private ArrayList<Object> childtems;

	private LayoutInflater inflater;

	private ArrayList<String> parentItems, child;

	public Typeface fuente;

	public MiAdaptadorExpandibleDetalles(ArrayList<String> parents,
			ArrayList<Object> childern) {

		this.parentItems = parents;

		this.childtems = childern;

	}

	public void setInflater(LayoutInflater inflater, Activity activity) {

		this.inflater = inflater;

		this.activity = activity;

	}

	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		child = (ArrayList<String>) childtems.get(groupPosition);

		TextView textView = null;
		ImageView im = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.detalles, null);
		}
		String text = child.get(childPosition);
		if (text != null) {
			if (!text.equals("")) {
				int type = comprobar(text);
				textView = (TextView) convertView
						.findViewById(R.id.textDetalle);
				textView.setTypeface(fuente);
				textView.setText(text);
				im = (ImageView) convertView.findViewById(R.id.imageDetalle);
				int draw = 0;
				switch (type) {
				case 0:
					draw = R.drawable.llamar;
					break;
				case 1:
					draw = R.drawable.llamar;
					break;
				case 2:
					draw = R.drawable.correo;
					break;
				case 3:
					draw = R.drawable.edificio;
					break;
				case 4:
					draw = R.drawable.un;
					textView.setText("Sitio Web");
					break;

				default:
					break;
				}
				BitmapDrawable icono = new BitmapDrawable(
						BitmapFactory.decodeResource(activity.getResources(),
								draw));
				im.setImageDrawable(icono);
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						int type = comprobar(child.get(childPosition));
						switch (type) {
						case 0:
							llamar(child.get(childPosition));
							break;
						case 1:
							llamar("3165000;" + child.get(childPosition));
							break;
						case 2:
							correo(child.get(childPosition));
							break;
						case 3:
							ubicar(child.get(childPosition));
							break;
						case 4:
							Util.irA(child.get(childPosition), activity);
							break;

						default:
							break;
						}

					}

				});
			}
		}
		return convertView;

	}

	private int comprobar(String cad) {
		int ret = 3;
		Pattern p = Pattern.compile("[0-9]{0,}");
		Matcher m = p.matcher(cad);
		if (m.matches()) {
			ret = 0;
		}
		p = Pattern.compile("[0-9]{0,5}");
		m = p.matcher(cad);
		if (m.matches()) {
			ret = 1;
		}
		if (cad.contains("@")) {
			ret = 2;
		}
		p = Pattern.compile(".*[w-y]{3}.*");
		m = p.matcher(cad);
		if (m.matches()) {
			ret = 4;
		}
		if (cad.contains("http")) {
			ret = 4;
		}
		return ret;
	}

	public void llamar(String number) {
		Uri numero = Uri.parse("tel: +571" + number);
		Intent intent = new Intent(Intent.ACTION_CALL, numero);
		activity.startActivity(intent);
	}

	public void ubicar(String cad) {
		try {
			if (cad.equals("Edificio")) {
				return;
			}
			Intent mapa = new Intent(activity, MapaActivity.class);
			mapa.putExtra("lat", DetailsActivity.lat);
			mapa.putExtra("lon", DetailsActivity.lon);
			mapa.putExtra("titulos", DetailsActivity.titulos);
			mapa.putExtra("descripciones", DetailsActivity.descripciones);
			mapa.putExtra("zoom", 18);
			mapa.putExtra("tipo", 1);
			mapa.putExtra("nivel", 3);
			activity.startActivity(mapa);
			activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		} catch (Exception ex) {
		}
	}

	public void correo(String email) {
		if (email.contains("Correo")) {
			return;
		}
		Util.enviar(activity, email.split(" "), null, "", "");
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.grupo, null);
		}

		((CheckedTextView) convertView).setText(parentItems.get(groupPosition));

		((CheckedTextView) convertView).setChecked(isExpanded);
		((CheckedTextView) convertView).setGravity(Gravity.CENTER);

		return convertView;

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;

	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return 0;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) childtems.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
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
