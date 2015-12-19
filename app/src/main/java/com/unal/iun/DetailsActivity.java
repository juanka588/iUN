package com.unal.iun;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unal.iun.Adapters.ExpandableDetailsAdapter;
import com.unal.iun.Data.DetailedInformation;

import java.util.ArrayList;


public class DetailsActivity extends AppCompatActivity {

    public static final String ARG_TITLE = "title";
    public static final String ARG_DATA = "datos";
    public static final String ARG_BACKGROUND = "fondo";
    protected RelativeLayout tl;
    protected ArrayList<DetailedInformation> data = new ArrayList<>();
    protected ExpandableListView sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle b = getIntent().getExtras();
        sc = (ExpandableListView) findViewById(R.id.expandableListDestails);
        manejarDisplay();
//        try {
        data = b.getParcelableArrayList(ARG_DATA);
        TextView tx = (TextView) findViewById(R.id.tituloDetallesDtos);
        String title = b.getString(ARG_TITLE);
        tx.setText(title == null ? "" : title.trim());
        int id = R.drawable.ciudad_universitaria;
        int backgroundParameter = b.getInt(ARG_BACKGROUND);
        if (backgroundParameter != 0) {
            id = backgroundParameter;
        }
        Drawable background = new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(), id));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tl.setBackground(background);
        } else {
            tl.setBackgroundDrawable(background);
        }
        sc.setDividerHeight(2);
        // sc.setGroupIndicator(null);
        sc.setClickable(true);

        ExpandableDetailsAdapter adapter = new ExpandableDetailsAdapter(data, this);
        adapter.font = Typeface.createFromAsset(getAssets(),
                "Helvetica.ttf");
        sc.setAdapter(adapter);
        sc.expandGroup(0);
//        } catch (Exception e) {
//            Log.e("error de Detalles ", e.toString());
//            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
//        }

    }

    public void manejarDisplay() {
        BitmapDrawable background2 = new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.fondoinf));
        getSupportActionBar().setBackgroundDrawable(background2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tl = (RelativeLayout) findViewById(R.id.linearLayoutDetalles);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                home();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                this.finish();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            this.finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    public void home() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        this.finish();
    }

}
