package com.unal.iun.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unal.iun.GUI.MapaActivity;
import com.unal.iun.LN.Util;
import com.unal.iun.R;
import com.unal.iun.data.DetailedInformation;
import com.unal.iun.data.InformationElement;
import com.unal.iun.data.MapMarker;

import java.util.ArrayList;
import java.util.List;

public class ExpandableDetailsAdapter extends BaseExpandableListAdapter {

    public Typeface font;
    private Activity activity;
    private List<DetailedInformation> items;


    public ExpandableDetailsAdapter(List<DetailedInformation> children, Activity activity) {
        this.items = children;
        this.activity = activity;
    }


    @SuppressLint("InflateParams")
    @SuppressWarnings("unchecked")
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.detalles, null);
        }
        DetailedInformation detail = items.get(groupPosition);
        final InformationElement child = detail.getInformationElements().get(childPosition);
        TextView textView = convertView.findViewById(R.id.textDetalle);
        ImageView im = convertView.findViewById(R.id.imageDetalle);
        textView.setTypeface(font);
        textView.setText(child.getInformationDescription());
        int type = child.getType();
        int draw = R.drawable.llamar;
        final String text = child.getInformationDescription();
        switch (type) {
            case 0:
                draw = R.drawable.llamar;
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call(text);
                    }
                });
                break;
            case 1:
                draw = R.drawable.llamar_ext;
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call("3165000;" + text);
                    }
                });
                break;
            case 2:
                draw = R.drawable.email;
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        correo(text);
                    }
                });
                break;
            case 3:
                draw = R.drawable.edificio;
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ubicar(child);
                    }
                });
                break;
            case 4:
                draw = R.drawable.un;
                textView.setText(activity.getText(R.string.web_site));
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.irA(text, activity);
                    }
                });
                break;

            default:
                break;
        }
        Picasso.with(activity).load(draw).fit().into(im);
        return convertView;
    }


    public void call(String numberSTR) {
        Uri number = Uri.parse("tel: +571" + numberSTR);
        Intent intent = new Intent(Intent.ACTION_CALL, number);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        activity.startActivity(intent);
    }

    public void ubicar(InformationElement element) {
        Intent mapa = new Intent(activity, MapaActivity.class);
        mapa.putExtra(MapaActivity.ARG_ZOOM, 18);
        mapa.putExtra(MapaActivity.ARG_TYPE, 1);
        mapa.putExtra(MapaActivity.ARG_LEVEL, 3);
        ArrayList<MapMarker> data = new ArrayList<>();
        data.add(element.getMapMarker());
        mapa.putExtra(MapaActivity.ARG_MARKERS, data);
        activity.startActivity(mapa);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    public void correo(String email) {
        if (email.contains("Correo")) {
            return;
        }
        Util.enviar(activity, email.split(" "), null, "", "");
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.grupo, null);
        }
        ((CheckedTextView) convertView).setText(items.get(groupPosition).getInformationTitle());
        ((CheckedTextView) convertView).setChecked(isExpanded);
        ((CheckedTextView) convertView).setGravity(Gravity.CENTER);
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return (items.get(groupPosition).getInformationElements().get(childPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return items.get(groupPosition).getInformationElements().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
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
        return groupPosition;
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
