package com.unal.iun.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.unal.iun.GUI.InstitucionesActivity;
import com.unal.iun.GUI.RadioActivity;
import com.unal.iun.GUI.WebActivity;
import com.unal.iun.LN.Util;
import com.unal.iun.R;
import com.unal.iun.data.WebItem;

import java.util.List;

/**
 * Created by JuanCamilo on 29/06/2015.
 */
public class WebMenuRecyclerViewAdapter extends RecyclerView.Adapter<WebMenuRecyclerViewAdapter.WebItemViewHolder> {

    private List<WebItem> items;
    private Activity activity;
    private Context context;
    private Typeface font;

    public WebMenuRecyclerViewAdapter(List<WebItem> webItems, Activity activity, Typeface font) {
        this.items = webItems;
        this.context = activity.getApplicationContext();
        this.font = font;
        this.activity = activity;
    }

    @Override
    public WebItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new WebItemViewHolder(v);
    }

    @Override
    public void onViewRecycled(WebItemViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(WebItemViewHolder holder, int i) {
        WebItem s = items.get(i);
        holder.name.setText(s.name);
        holder.name.setTypeface(font);
        Picasso.with(holder.cv.getContext()).load(s.icon).fit().into(holder.icon);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void openWeb(int position) {
        Intent info = new Intent(activity, WebActivity.class);
        info.putExtra(WebActivity.ARG_TAG, items.get(position).url);
        activity.startActivity(info);
    }

    public void openRadioActivity() {
        Intent intent = new Intent(activity, RadioActivity.class);
        activity.startActivity(intent);
    }

    private void admissions() {
        final String[] items = {context.getString(R.string.instituciones),
                context.getString(R.string.informacion), context.getString(R.string.edificios)};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(context.getString(R.string.admisiones)).setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            try {
                                Intent ca = new Intent(activity,
                                        InstitucionesActivity.class);
                                ca.putExtra("modo", true);
                                activity.startActivity(ca);
                                activity.overridePendingTransition(R.anim.fade_in,
                                        R.anim.fade_out);
                            } catch (Exception e) {
                                Toast.makeText(context, context.getText(R.string.disponible), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (item == 1) {
                            String cad = "http://admisiones.unal.edu.co/";
                            Util.irA(cad, activity);
                        } else {
                            Intent ca = new Intent(activity, InstitucionesActivity.class);
                            ca.putExtra("modo", false);
                            activity.startActivity(ca);
                            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    }
                });

        builder.setNegativeButton(context.getText(R.string.cancel_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public class WebItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout cv;
        TextView name;
        ImageView icon;

        WebItemViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_view);
            name = itemView.findViewById(R.id.webItemDescription);
            icon = itemView.findViewById(R.id.webItemImage);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TextView desc = v.findViewById(R.id.webItemDescription);
            String cad = desc.getText().toString();
            Log.e("desc", cad);
            if (cad.contains("Radio")) {
                openRadioActivity();
            } else if (cad.contains("Admisio")) {
                admissions();
            } else {
                openWeb(getAdapterPosition());
            }
        }
    }
}
