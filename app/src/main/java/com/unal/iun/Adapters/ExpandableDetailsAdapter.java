package com.unal.iun.Adapters;

import android.Manifest;
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

import com.unal.iun.Data.DetailedInformation;
import com.unal.iun.Data.InformationElement;
import com.unal.iun.LN.Util;
import com.unal.iun.MapaActivity;
import com.unal.iun.R;

import java.util.ArrayList;

public class ExpandableDetailsAdapter extends BaseExpandableListAdapter {

    public Typeface font;
    private Activity activity;
    private ArrayList<DetailedInformation> items;


    public ExpandableDetailsAdapter(ArrayList<DetailedInformation> children, Activity activity) {
        this.items = children;
        this.activity = activity;
    }


    @SuppressWarnings("unchecked")
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        DetailedInformation detail = items.get(groupPosition);
        InformationElement child = detail.getInformationElements().get(childPosition);
        TextView textView = (TextView) convertView.findViewById(R.id.textDetalle);
        ImageView im = (ImageView) convertView.findViewById(R.id.imageDetalle);
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.detalles, null);
        }
        textView.setTypeface(font);
        textView.setText(child.getInformationDescription());
        int type = child.getType();
        int draw = 0;
        final String text = child.getInformationDescription();
        switch (type) {
            case 0:
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llamar(text);
                    }
                });
                break;
            case 1:
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llamar("3165000;" + text);
                    }
                });
                break;
            case 2:
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        correo(text);
                    }
                });
                break;
            case 3:
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ubicar(text);
                    }
                });
                break;
            case 4:
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
        im.setImageResource(draw);
        return convertView;
    }


    public void llamar(String number) {
        Uri numero = Uri.parse("tel: +571" + number);
        Intent intent = new Intent(Intent.ACTION_CALL, numero);
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

    public void ubicar(String cad) {
        try {
            if (cad.equals("Edificio")) {
                return;
            }
            Intent mapa = new Intent(activity, MapaActivity.class);
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
            ((CheckedTextView) convertView).setText(items.get(groupPosition).getInformationTitle());
            ((CheckedTextView) convertView).setChecked(isExpanded);
            ((CheckedTextView) convertView).setGravity(Gravity.CENTER);
        }
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return (InformationElement) (items.get(groupPosition).getInformationElements()
                .get(childPosition));
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
        return (DetailedInformation) items.get(groupPosition);
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
