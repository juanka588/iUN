package com.unal.iun.GUI;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unal.iun.Interfaces.OnTimeUpdate;
import com.unal.iun.LN.IUNDataBase;
import com.unal.iun.LN.MainScreenLocationListener;
import com.unal.iun.LN.Util;
import com.unal.iun.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements OnTimeUpdate {
    public static final boolean DEBUG = true;
    public static String tbName = "BaseM";
    public static String CLOSEST_LOCATION_PARAM = "Bogotá";
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            CLOSEST_LOCATION_PARAM = savedInstanceState.getString(getString(R.string.main_activity_sede));
        }
        iniciarLocalService();
        if (Util.isOffline(this)) {
            Util.notificarRed(this);
        }
        dateTextView = findViewById(R.id.textTimestamp);
        final UINetworkClock action = new UINetworkClock();
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(action);
            }
        }, 1_000, 1_000);
    }

    public void licencia(View v) {
        Intent intent = new Intent(this, License.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void comentar(View v) {
        Util.enviar(
                this,
                "mahiguerag@unal.edu.co",
                getString(R.string.android_app_feedback_subject),
                getString(R.string.android_app_feedback_content));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        CLOSEST_LOCATION_PARAM = savedInstanceState.getString("sede");
        TextView tx = findViewById(R.id.textSede);
        tx.setText(CLOSEST_LOCATION_PARAM);
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void eventos(View v) {
        Util.irA("http://circular.unal.edu.co/museumlist/", MainActivity.this);
    }

    public void admisiones(View v) {
        final String[] items = {this.getString(R.string.instituciones), this.getString(R.string.informacion), this.getString(R.string.edificios)};
        AlertDialog.Builder builder = getBuilderApi();
        final Activity act = this;
        builder.setTitle(this.getString(R.string.admisiones)).setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                try {
                                    Intent ca = new Intent(act,
                                            InstitucionesActivity.class);
                                    ca.putExtra("modo", true);
                                    startActivity(ca);
                                    overridePendingTransition(R.anim.fade_in,
                                            R.anim.fade_out);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(),
                                            act.getText(R.string.disponible), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                String cad = "http://admisiones.unal.edu.co/";
                                Util.irA(cad, act);
                                break;
                            default:
                                Intent ca = new Intent(act, InstitucionesActivity.class);
                                ca.putExtra("modo", false);
                                startActivity(ca);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                break;
                        }
                    }
                });

        builder.setNegativeButton(this.getText(R.string.cancel_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private AlertDialog.Builder getBuilderApi() {
        int currentVersion = Build.VERSION.SDK_INT;
        Log.e("version", String.valueOf(currentVersion));
        if (currentVersion <= Build.VERSION_CODES.HONEYCOMB_MR2) {
            return new AlertDialog.Builder(this);
        }
        return new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
    }

    private void iniciarLocalService() {
        LocationManager milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener milocListener = new MainScreenLocationListener(getApplicationContext(),
                (TextView) findViewById(R.id.textLocation),
                (TextView) findViewById(R.id.textSede),
                new IUNDataBase(getApplicationContext()).dataBase
        );
        milocManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 1, milocListener);
        milocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 1, milocListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.main_activity_sede), CLOSEST_LOCATION_PARAM);
        super.onSaveInstanceState(outState);
    }

    public void directorio(View v) {
        directorio(false);
    }

    public void irSede(View v) {
        directorio(true);
    }

    private void directorio(boolean cond) {
        try {
            Intent directorio = new Intent(getApplicationContext(), DirectorioActivity.class);
            if (cond) {
                directorio.putExtra("current", 3);
                directorio.putExtra("sede", CLOSEST_LOCATION_PARAM);
            }
            directorio.putExtra("salto", cond);
            startActivity(directorio);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }

    }

    public void servicios(View v) {
        try {
            startActivity(new Intent(getApplicationContext(), MenuWebTabActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    @Override
    public void onTimeUpdated(String newDate) {
        dateTextView.setText(newDate);
    }

    private class UINetworkClock implements Runnable {
        private final SimpleDateFormat dateFormat;

        private boolean hasSyncedWithServer;
        private int counter;
        private long lastNetworkDate;


        private UINetworkClock() {
            hasSyncedWithServer = false;
            counter = 0;
            dateFormat = new SimpleDateFormat(
                    "EEEE, dd MMMM yyyy HH:mm:ss",
                    Locale.getDefault()
            );
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
        }

        @Override
        public void run() {
            if (!hasSyncedWithServer) {
                lastNetworkDate = Util.getCurrentNetworkTime();
                hasSyncedWithServer = true;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(lastNetworkDate));
            cal.add(Calendar.SECOND, counter);
            MainActivity.this.onTimeUpdated(dateFormat.format(cal.getTime()));
            counter++;
        }
    }
}
