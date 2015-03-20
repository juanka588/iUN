package com.unal.iun;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unal.iun.LN.LinnaeusDatabase;
import com.unal.iun.LN.MiLocationListener;
import com.unal.iun.LN.Timer;
import com.unal.iun.LN.Util;


public class MainActivity extends Activity {
    public static String dataBaseName = "datastore.sqlite", tbName = "BaseM";
    public Timer tim;
    public static String sede = "Bogotá";
    ImageView im;
    int screenWidth;
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            sede = savedInstanceState.getString("sede");
        }
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        Typeface fuente = Typeface
                .createFromAsset(getAssets(), "Helvetica.ttf");
        int ids[] = { R.id.SOnlineButton, R.id.admisionesButton,
                R.id.sedesButton, R.id.textLatitud, R.id.textLongitud,
                R.id.textLugar, R.id.eventosButton };
        for (int i = 0; i < ids.length; i++) {
            TextView prueba = (TextView) findViewById(ids[i]);
            prueba.setTypeface(fuente);
            if (ids[i] == R.id.textSede) {
                prueba.setText(sede);
            }
        }
        iniciarLocalService();
        if (!Util.isOnline(this)) {
            Util.notificarRed(this);
        }
        // addShortcut();
    }


    protected void cambiarBD() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Que Base de Datos Desea usar?")
                .setTitle("Confirme:")
                .setPositiveButton("Modo Básico",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                tbName = "BaseM";
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Modo Experto",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                tbName = "Base";
                                dialog.cancel();
                            }
                        });
        builder.create().show();
    }

    public void licencia(View v) {
        Intent ca = new Intent(this, License.class);
        startActivity(ca);
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
        TextView tx = (TextView) findViewById(R.id.textSede);
        tx.setText(sede);
        im = (ImageView) findViewById(R.id.imageUNPrincipal);
        im.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                cambiarBD();
                return false;
            }
        });
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void eventos(View v) {
        Util.irA("http://circular.unal.edu.co/nc/eventos-3.html", this);
    }

    public void admisiones(View v) {
        final String[] items = { "Instituciones", "Información" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Activity act = this;
        builder.setTitle("Admisiones").setItems(items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            try {
                                Intent ca = new Intent(act,
                                        InstitucionesActivity.class);
                                ca.putExtra("modo", true);
                                startActivity(ca);
                                overridePendingTransition(R.anim.fade_in,
                                        R.anim.fade_out);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),
                                        "Disponible proximamente", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String cad = "http://admisiones.unal.edu.co/";
                            Util.irA(cad, act);
                        }
                    }
                });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void iniciarLocalService() {
        LocationManager milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener milocListener = new MiLocationListener();
        MiLocationListener.appcont = this.getApplicationContext();
        MiLocationListener.textLat = (TextView) findViewById(R.id.textLatitud);
        MiLocationListener.textLon = (TextView) findViewById(R.id.textLongitud);
        MiLocationListener.textSede = (TextView) findViewById(R.id.textSede);
        LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
        MiLocationListener.db = openOrCreateDatabase(dataBaseName,
                MODE_WORLD_READABLE, null);
        try {

            milocManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, milocListener);
        } catch (Exception e) {

        } finally {
            milocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, milocListener);
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("sede", sede);
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
            Intent directorio = new Intent(getApplicationContext(),
                    DirectorioActivity.class);
            if (cond) {
                directorio.putExtra("current", 3);
                directorio.putExtra("sede", sede);
            }
            directorio.putExtra("salto", cond);
            startActivity(directorio);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            // this.finish();
        } catch (Exception e) {
            Log.e("error", e.toString());
        }

    }

    public void servicios(View v) {
        try {
            Class cls;
            if (screenWidth > 600 && false) {
                cls = null;
            } else {
                cls = MenuWEBActivity.class;
            }
            startActivity(new Intent(getApplicationContext(), cls));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            // this.finish();
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

}
