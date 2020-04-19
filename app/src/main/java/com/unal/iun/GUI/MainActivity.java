package com.unal.iun.GUI;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.unal.iun.Interfaces.OnTimeUpdate;
import com.unal.iun.LN.IUNDataBase;
import com.unal.iun.LN.MainScreenLocationListener;
import com.unal.iun.LN.Util;
import com.unal.iun.R;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements OnTimeUpdate {
    public static final boolean DEBUG = true;
    public static String tbName = "BaseM";
    public static String sede = "Bogotá";
    int screenWidth;
    int screenHeight;
    TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            sede = savedInstanceState.getString(getString(R.string.main_activity_sede));
        }
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        Typeface fuente = Typeface.createFromAsset(getAssets(), "Helvetica.ttf");
        int[] ids = {R.id.SOnlineButton, R.id.admisionesButton,
                R.id.sedesButton, R.id.textLocation,
                R.id.textTimestamp, R.id.timestamp_label,
                R.id.textLugar, R.id.eventosButton};
        for (int id : ids) {
            TextView textView = findViewById(id);
            textView.setTypeface(fuente);
        }
        iniciarLocalService();
        if (!Util.isOnline(this)) {
            Util.notificarRed(this);
        }
        dateTextView = findViewById(R.id.textTimestamp);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String currentNetworkTime = Util.getCurrentNetworkTime();
                        MainActivity.this.onTimeUpdated(currentNetworkTime);
                    }
                });
            }
        },0,10_000);
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
                "",
                "Comentarios Aplicacion iUN Android",
                "iUN es una aplicacción de apoyo, "
                        + "en tal razón la información contenida "
                        + "en ella es solo de referencia. \n\n\n "
                        + "Envía tus comentarios acerca de la aplicación iUN"
                        + " respondiendo las siguientes preguntas.\n\n\n "
                        + "A. Nombres y Apellidos. \n\n\n B. Estudiante, Docente, "
                        + "Administrativo de la Universidad Nacional de Colombia o "
                        + "persona externa? \n\n\n 1 "
                        + "¿Cual fue la primera acción que ejecutó? \n\n\n 2 "
                        + "¿Identifica claramente las acciones de los botones e iconos?"
                        + " \n\n\n 3. Si hubo un bloqueo, ¿Cual fue, si recuerda, "
                        + "la acción que causo esto?  \n\n\n 4 "
                        + "¿ Impresión general sobre la aplicación? "
                        + "\n\n\n Gracias por su colaboración.\n "
                        + "Equipo de Desarrollo iUN.");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        sede = savedInstanceState.getString("sede");
        TextView tx = findViewById(R.id.textSede);
        tx.setText(sede);
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
        outState.putString(getString(R.string.main_activity_sede), sede);
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
                directorio.putExtra("sede", sede);
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

}
