package com.unal.iun;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.unal.iun.LN.LinnaeusDatabase;
import com.unal.iun.LN.Util;


public class MenuWEBActivity extends Activity {

    SearchView sv;
    double lat[];
    double lon[];
    String titulos[], descripciones[];
    boolean colegios = false;
    private ActionBar barra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu_web);
        ScrollView sc = (ScrollView) findViewById(R.id.scrollViewMenuWeb);
        Space sp = (Space) findViewById(R.id.spaceMenuWebGeneral);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        int screenWidth, screenHeight, dpi;
        float density;
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        density = metrics.density;
        dpi = metrics.densityDpi;
        double factor = screenHeight / 2000.0 + 0.35;
        double factor2 = 3.0 * screenHeight / 20000.0 + 0.09;
        if (factor > 0.65) {
            factor = 0.65;
        }
        if (factor2 > 0.2) {
            factor2 = 0.2;
        }
        sp.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (screenHeight * (factor2))));
        sc.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (screenHeight * (factor))));
        int ids[] = new int[] { R.id.spaceMenuWeb01, R.id.spaceMenuWeb02,
                R.id.spaceMenuWeb03, R.id.spaceMenuWeb10, R.id.spaceMenuWeb11,
                R.id.spaceMenuWeb12, R.id.spaceMenuWeb20, R.id.spaceMenuWeb21,
                R.id.spaceMenuWeb22, R.id.spaceMenuWeb30, R.id.spaceMenuWeb31,
                R.id.spaceMenuWeb32, R.id.spaceMenuWeb60, R.id.spaceMenuWeb61,
                R.id.spaceMenuWeb62, R.id.spaceMenuWeb70, R.id.spaceMenuWeb71,
                R.id.spaceMenuWeb72, R.id.spaceMenuWeb110,
                R.id.spaceMenuWeb111, R.id.spaceMenuWeb112,
                R.id.spaceMenuWeb120, R.id.spaceMenuWeb121,
                R.id.spaceMenuWeb122, R.id.spaceMenuWeb130,
                R.id.spaceMenuWeb131, R.id.spaceMenuWeb132,
                R.id.spaceMenuWeb140, R.id.spaceMenuWeb141,
                R.id.spaceMenuWeb142, R.id.spaceMenuWeb150,
                R.id.spaceMenuWeb160, R.id.spaceMenuWeb170,
                R.id.spaceMenuWeb171, R.id.spaceMenuWeb172,
                R.id.spaceMenuWeb180, R.id.spaceMenuWeb181,
                R.id.spaceMenuWeb182, R.id.spaceMenuWeb200,
                R.id.spaceMenuWeb201, R.id.spaceMenuWeb202,
                R.id.spaceMenuWeb210, R.id.spaceMenuWeb211,
                R.id.spaceMenuWeb212 };
        Log.e("densidadDPI", density + "");
        for (int i = 0; i < ids.length; i++) {
            Space im = (Space) findViewById(ids[i]);
            ViewGroup.LayoutParams iv_params_b = im.getLayoutParams();
            iv_params_b.height = 0;
            iv_params_b.width = (int) ((screenWidth) * (0.04 + screenWidth
                    / (10000.0 * density)));
            im.setLayoutParams(iv_params_b);
        }
        int ids2[] = new int[] { R.id.textTituloMenuWeb, R.id.textMP,
                R.id.textUNradio, R.id.textPrisma, R.id.textLeon,
                R.id.textLibreria, R.id.textComunidad, R.id.textCorreo,
                R.id.textComedor, R.id.textMedico, R.id.textTituloMenuWeb,
                R.id.textUNIsalud, R.id.textServiciosACOM, R.id.textSIA,
                R.id.textSINAB, R.id.textHermes, R.id.textDRE, R.id.textSARA,
                R.id.textWD,R.id.textIPARM,R.id.textFODUN,R.id.textCooperativa,
                R.id.textLaboratorios,R.id.textComsion};
        Typeface fuente = Typeface
                .createFromAsset(getAssets(), "Helvetica.ttf");
        for (int i = 0; i < ids2.length; i++) {
            TextView tx = (TextView) findViewById(ids2[i]);
            tx.setTypeface(fuente);
        }

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

    public void navegar(String cad) {
        Util.irA(cad, this);
    }

    @Override
    public void onBackPressed() {
        home();
        super.onBackPressed();
    }

    public void home(View v) {
        home();
    }

    public void home() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu_web, menu);
        MenuItem menuItem = menu.getItem(0);
        sv = (SearchView) menuItem.getActionView();
        sv.setQueryHint("Inicia una Busqueda...");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                String cad = "http://unal.edu.co/resultados-de-la-busqueda/?q="
                        + arg0;
                navegar(cad);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        sv.setSoundEffectsEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    public void irA(View v) {

        String cad = "";
        switch (v.getId()) {
            case R.id.imageSIA:
                cad = "http://www.sia.unal.edu.co/";
                break;
            case R.id.imageSINAB:
                cad = "http://www.sinab.unal.edu.co/";
                break;
            case R.id.imageDRE:
                cad = "http://www.ori.unal.edu.co/";
                break;
            case R.id.imageSARA:
                cad = "http://www.sara.unal.edu.co:8082/saraweb/";
                break;
            case R.id.imagePrisma:
                cad = "http://livestream.com/prisma_tv";
                break;
            case R.id.imageUNradio:
			/*
			 * cad = "http://www.unradio.unal.edu.co/nc/en-linea/bogota.html";
			 * break;
			 */

                Intent radio = new Intent(this, RadioActivity.class);
                startActivity(radio);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return;
            case R.id.imageWD:
                cad = "http://www.docentes.unal.edu.co/";
                break;
            case R.id.imageHermes:
                cad = "http://www.hermes.unal.edu.co/";
                break;
            case R.id.imageUNPrincipal:
                cad = "http://www.unal.edu.co/";
                break;
            case R.id.imageCorreo:
                cad = "https://login.unal.edu.co/cloudkey/a/unal.edu.co/user/login?namespace=unal.edu.co";
                break;
            case R.id.imageLeonG:
                cad = "http://www.divulgacion.unal.edu.co/leon_de_greiff/";
                break;
            case R.id.imageLibreria:
                cad = "http://www.libreriaun.unal.edu.co/?q=node/39";
                break;
            case R.id.imageComedor:
                ubicar();
                return;
            // break;
            case R.id.imageMedico:
                cad = "http://www.bienestarbogota.unal.edu.co/salud.php";
                break;
            case R.id.imageUNIsalud:
                cad = "http://www.unisalud.unal.edu.co/";
                break;
            case R.id.imageAdmisiones:
                preguntar(this);
                return;
            case R.id.imageUNperiodico:
                cad = "http://www.unperiodico.unal.edu.co";
                break;
            case R.id.imagePatrimonio:
                cad = "http://www.unal.edu.co/contenido/patrimonio_historico.html";
                break;
            case R.id.imageLaboratorios:
                cad = "http://www.laboratorios.unal.edu.co";
                break;
            case R.id.imageIPARM:
                cad = "http://www.unal.edu.co/iparm/";
                break;
            case R.id.imageComite:
                cad = "http://www.bienestar.unal.edu.co/";
                break;
            case R.id.imageFODUN:
                cad = "http://www.fodun.com.co";
                break;
            case R.id.imageCooperativa:
                cad = "http://www.cooprofesoresun.coop";
                break;
            default:
                break;
        }
        navegar(cad);
    }

    private void preguntar(final Activity act) {
        final String[] items = { "Instituciones", "Información" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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

    public void ubicar() {
        try {
            Intent mapa = new Intent(this, MapaActivity.class);
            LinnaeusDatabase lb = new LinnaeusDatabase(getApplicationContext());
            SQLiteDatabase db = openOrCreateDatabase(MainActivity.dataBaseName,
                    MODE_WORLD_READABLE, null);
            String query;

            query = "select distinct edificio,nombre_edificio,latitud,longitud from edificios "
                    + " where nivel=" + 5;

            Cursor c = db.rawQuery(query, null);
            String[][] mat = Util.imprimirLista(c);
            c.close();
            db.close();
            lat = Util.toDouble(Util.getcolumn(mat, 2));
            lon = Util.toDouble(Util.getcolumn(mat, 3));
            titulos = Util.getcolumn(mat, 1);
            descripciones = Util.getcolumn(mat, 0);
            mapa.putExtra("lat", lat);
            mapa.putExtra("lon", lon);
            mapa.putExtra("titulos", titulos);
            mapa.putExtra("descripciones", descripciones);
            mapa.putExtra("nivel", 3);
            mapa.putExtra("zoom", 14);
            mapa.putExtra("tipo", 1);
            startActivity(mapa);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (Exception ex) {
        }
    }
}
