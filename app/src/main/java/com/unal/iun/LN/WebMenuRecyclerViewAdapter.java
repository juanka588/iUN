package com.unal.iun.LN;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unal.iun.R;
import com.unal.iun.WebActivity;
import com.unal.iun.data.WebItem;

import java.util.List;

/**
 * Created by JuanCamilo on 29/06/2015.
 */
public class WebMenuRecyclerViewAdapter extends RecyclerView.Adapter<WebMenuRecyclerViewAdapter.WebItemViewHolder> {

    private List<WebItem> items;
    private Context context;
    private Typeface font;

    public WebMenuRecyclerViewAdapter(List<WebItem> webItems, Context context, Typeface font) {
        this.items = webItems;
        this.context = context;
        this.font = font;
    }

    @Override
    public WebItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        WebItemViewHolder pvh = new WebItemViewHolder(v);
        return pvh;
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
        holder.icon.setImageResource(s.icon);
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
        Intent info = new Intent(context, WebActivity.class);
        info.putExtra(WebActivity.ARG_TAG, items.get(position).url);
        context.startActivity(info);
    }

    public class WebItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout cv;
        TextView name;
        ImageView icon;

        WebItemViewHolder(View itemView) {
            super(itemView);
            cv = (LinearLayout) itemView.findViewById(R.id.card_view);
            name = (TextView) itemView.findViewById(R.id.webItemDescription);
            icon = (ImageView) itemView.findViewById(R.id.webItemImage);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openWeb(getPosition());
        }
    }
}
