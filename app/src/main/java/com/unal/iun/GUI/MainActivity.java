package com.unal.iun.GUI;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.unal.iun.LN.LinnaeusDatabase;
import com.unal.iun.LN.MiLocationListener;
import com.unal.iun.LN.Timer;
import com.unal.iun.LN.Util;
import com.unal.iun.R;


public class MainActivity extends Activity {
    public static final boolean DEBUG = true;
    public static String tbName = "BaseM";
    public static String sede = "Bogotá";
    public Timer tim;
    ImageView im;
    int screenWidth;
    int screenHeight;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

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
        Typeface fuente = Typeface
                .createFromAsset(getAssets(), "Helvetica.ttf");
        int ids[] = {R.id.SOnlineButton, R.id.admisionesButton,
                R.id.sedesButton, R.id.textLatitud, R.id.textLongitud,
                R.id.textLugar, R.id.eventosButton};
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    protected void cambiarBD() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(this);
        }
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
        final String[] items = {this.getString(R.string.instituciones), this.getString(R.string.informacion), this.getString(R.string.edificios)};
        AlertDialog.Builder builder = getBuilderApi();
        final Activity act = this;
        builder.setTitle(this.getString(R.string.admisiones)).setItems(items,
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
                                        act.getText(R.string.disponible), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (item == 1) {
                            String cad = "http://admisiones.unal.edu.co/";
                            Util.irA(cad, act);
                        } else {
                            Intent ca = new Intent(act, InstitucionesActivity.class);
                            ca.putExtra("modo", false);
                            startActivity(ca);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
        Log.e("version", currentVersion + "");
        if (currentVersion <= Build.VERSION_CODES.HONEYCOMB_MR2) {
            return new AlertDialog.Builder(this);
        }
        return new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
    }

    private void iniciarLocalService() {
        LocationManager milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener milocListener = new MiLocationListener();
        MiLocationListener.appcont = this.getApplicationContext();
        MiLocationListener.textLat = (TextView) findViewById(R.id.textLatitud);
        MiLocationListener.textLon = (TextView) findViewById(R.id.textLongitud);
        MiLocationListener.textSede = (TextView) findViewById(R.id.textSede);
        LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
        MiLocationListener.db = lb.dataBase;
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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
            Intent directorio = new Intent(getApplicationContext(),
                    DirectoryActivity.class);
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
            startActivity(new Intent(getApplicationContext(), MenuWebTabActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            // this.finish();
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.unal.iun.GUI/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.unal.iun.GUI/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }
}
