package com.unal.iun.GUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.unal.iun.Adapters.MiAdaptador;
import com.unal.iun.LN.LinnaeusDatabase;
import com.unal.iun.LN.Util;
import com.unal.iun.R;
import com.unal.iun.data.DetailedInformation;
import com.unal.iun.data.InformationElement;
import com.unal.iun.data.MapMarker;

import java.util.ArrayList;
import java.util.List;


public class DirectorioActivity extends AppCompatActivity {
    protected String seleccion = "";
    protected String condicion = "";
    protected String baseTableName = "BaseM";
    protected String buildingsTableName = "Edificios";
    protected String webTableName = "ENLACE";
    protected String sql = "";
    protected String path = "";
    protected String auxCon;
    protected ListView lv;
    protected MenuItem item;
    protected SearchView sv;
    protected int current = 2;
    protected TableRow tr;
    protected int idFondo = R.drawable.fondo,
            idFondoTras = R.drawable.ciudad_universitaria;
    protected double lat[];
    protected double lon[];
    protected String titulos[], descripciones[];
    protected String columnas[] = {"_id", "NIVEL_ADMINISTRATIVO", "SEDE",
            "DEPENDENCIAS", "DIVISIONES", "DEPARTAMENTOS", "SECCIONES",
            "CORREO_ELECTRONICO", "EXTENSION", "DIRECTO",
            "edificios._id", "enlace._id"};
    protected TableLayout tl;
    protected BitmapDrawable background;
    protected boolean buscando = false;
    protected android.support.v7.app.ActionBar barra;
    protected ActionBarDrawerToggle toggle;
    protected Activity act;
    private DrawerLayout cajon;
    private SimpleCursorAdapter simpleCursorAdapter;
    private String[] from = new String[]{"consulta"};
    private int[] to = new int[]{android.R.id.text1};
    private LinnaeusDatabase ln;
    private SQLiteDatabase db;
    private Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        ln = new LinnaeusDatabase(getApplicationContext());
        db = ln.getWritableDatabase();
        font = Typeface.createFromAsset(getAssets(), "Helvetica.ttf");
        setContentView(R.layout.activity_directorio);
        crearBarra();
        manejarDisplay();
        Bundle b = getIntent().getExtras();
        adaptadorInicial(b);
    }

    @Override
    protected void onDestroy() {
        db.close();
        ln.close();
        super.onDestroy();
    }

    private void adaptadorInicial(Bundle b) {
        baseTableName = MainActivity.tbName;
        try {
            if (b.getBoolean("salto")) {
                current = b.getInt("current");
                String sede = b.getString("sede");
                condicion = "sede='" + sede + "'";
                sql = "select  distinct " + columnas[current] + " from "
                        + baseTableName + " where " + condicion
                        + " and NIVEL_ADMINISTRATIVO between 1 and 4";
                path = sede;
                animarFondo(sede, false);
            } else {
                sql = "select  distinct " + columnas[current] + " from "
                        + baseTableName;
            }
            Cursor c;
            if (b.getBoolean("salto")) {
                c = db.rawQuery(sql, null);
            } else {
                c = db.rawQuery(sql
                        + " natural join edificios order by (orden)", null);
            }
            final String[][] mat = Util.imprimirLista(c);
            c.close();
            MiAdaptador adapter = new MiAdaptador(this, Util.getcolumn(mat, 0),
                    Util.getcolumn(mat, 0), MiAdaptador.TYPE_SIMPLE);
            adapter.fuente = font;
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View vista,
                                        int posicion, long arg3) {
                    try {
                        seleccion = mat[posicion][0];
                        if (saltar(seleccion)) {
                            irDirecto(seleccion);
                            return;
                        }
                        if (seleccion.contains("Programas")) {
                            irDirecto();
                            return;
                        }
                        if (path == "") {
                            path = seleccion;
                            // item.setTitleCondensed(path.toUpperCase().trim());
                        } else {
                            path = path + ">" + seleccion.toUpperCase().trim();
                            // item.setTitleCondensed(path);
                        }
                        // item.setTitleCondensed(path);
                        current++;
                        if (current == 3) {
                            animarFondo(mat[posicion][0], true);
                        }
                        if (condicion.equals("")) {
                            condicion = columnas[current - 1] + " = '"
                                    + seleccion + "'";
                        } else {
                            condicion += " and " + columnas[current - 1]
                                    + " = '" + seleccion + "'";
                        }

                        sql = "select  distinct " + columnas[current] + ", "
                                + columnas[2] + " from " + baseTableName
                                + "  where " + condicion;
                        recargar(sql, false, false, 1);

                    } catch (Exception e) {
                        Toast.makeText(getApplication(), e.toString(),
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void manejarDisplay() {
        tl = findViewById(R.id.TableLayoutDirectorio);
        lv = findViewById(R.id.listViewDirectorio);
        tr = findViewById(R.id.tableRowDirectorio);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        int screenHeight = display.getHeight();
        double factor = screenHeight / 2000.0 + 0.25;
        if (factor > 0.35) {
            factor = 0.35;
        }
        lv.setPadding(0, (int) (screenHeight * (factor)), 0, 10);
    }

    private void crearBarra() {
        act = this;
        barra = this.getSupportActionBar();
        BitmapDrawable background2 = new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.fondoinf));
        barra.setBackgroundDrawable(background2);
        String[] valores = getResources().getStringArray(R.array.optionsArray);
        String[] files = new String[]{"edificio.jpg", "enlacep.jpg",
                "mapap.jpg"};
        cajon = findViewById(R.id.drawer_layout);
        final ListView opciones = findViewById(R.id.left_drawer);
        MiAdaptador adapter = new MiAdaptador(act, valores, files, MiAdaptador.TYPE_IMAGE);
        opciones.setAdapter(adapter);
        opciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                switch (arg2) {
                    case 0:
                        if (!buscando) {
                            ubicar(null);
                        }
                        break;
                    case 1:
                        if (buscando) {
                            String cad = "http://unal.edu.co/resultados-de-la-busqueda/?q="
                                    + sv.getQuery();
                            Util.irA(cad, act);
                            break;
                        }
                        if (current == 2) {
                            Util.irA("http://www.unal.edu.co", act);
                        } else {
                            String baseConsult = "select url from enlace natural join "
                                    + baseTableName + " where " + condicion;
                            Cursor c = db.rawQuery(baseConsult, null);
                            String[][] mat = Util.imprimirLista(c);
                            c.close();
                            Util.irA(mat[0][0], act);
                        }
                        break;
                    case 2:
                        Intent ca = new Intent(act, InstitucionesActivity.class);
                        ca.putExtra("modo", false);
                        startActivity(ca);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    default:
                        break;
                }
                cajon.closeDrawer(opciones);
            }
        });
        toggle = new ActionBarDrawerToggle(act, cajon, R.string.drawer_open, R.string.drawer_close);
        barra.setDisplayHomeAsUpEnabled(true);
        barra.setHomeButtonEnabled(true);
    }

    public void home() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        this.finish();
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ubicar(View v) {
        try {
            Intent mapa = new Intent(this, MapaActivity.class);
            String query;
            int nivel = current - 1;
            if (nivel > 2) {
                nivel = 3;
            }
            if (condicion != "") {
                query = "select distinct _id_edificio,nombre_edificio,latitud,longitud from edificios natural join "
                        + baseTableName + " where " + condicion;
                // chambonazo mMap
                if (path.contains("Bogotá")) {
                    query = query + " and nivel=" + nivel;
                }
            } else {
                query = "select distinct _id_edificio,nombre_edificio,latitud,longitud from edificios ";
                query += " where nivel=" + nivel;
            }
            Log.e("query mMap", query);
            Log.e("PATH", path);
            Cursor c = db.rawQuery(query, null);
            String[][] mat = Util.imprimirLista(c);
            c.close();
            if (mat.length == 0) {
                Toast.makeText(getApplicationContext(),
                        this.getText(R.string.alert_dialog1), Toast.LENGTH_SHORT).show();
                return;
            }
            lat = Util.toDouble(Util.getcolumn(mat, 2));
            lon = Util.toDouble(Util.getcolumn(mat, 3));
            titulos = Util.getcolumn(mat, 1);
            descripciones = Util.getcolumn(mat, 0);
            ArrayList<MapMarker> data = new ArrayList<>();
            for (int i = 0; i < titulos.length; i++) {
                data.add(new MapMarker(new LatLng(lat[i], lon[i]),
                        titulos[i],
                        descripciones[i],
                        0,
                        BitmapDescriptorFactory.HUE_VIOLET));
            }
            mapa.putExtra(MapaActivity.ARG_MARKERS, data);
            mapa.putExtra("nivel", current - 1);
            mapa.putExtra("zoom", current <= 2 ? current + 3 : current + 10);
            mapa.putExtra("type", 0);
            startActivity(mapa);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_directorio, menu);
        // item = menu.getItem(0);
        MenuItem menuItem = menu.getItem(0);
        sv = (SearchView) MenuItemCompat.getActionView(menuItem);
        Cursor c = db.rawQuery("select  distinct " + columnas[current] + " consulta, "
                + columnas[2] + ",_id from " + baseTableName, null);
//        simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(),
//                android.R.layout.simple_list_item_1, c, from, to, 0);
//        sv.setSuggestionsAdapter(simpleCursorAdapter);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    recargar(query);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                } else {
                    /*
                     * current = 2; recargar("select  distinct " + columnas[2] +
					 * ", " + columnas[2] + " from " + baseTableName +
					 * " natural join edificios order by (orden)", false,
					 * false);
					 */
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    recargar(newText);
                } else {
                    /*
                     * current = 2; recargar("select  distinct " + columnas[2] +
					 * ", " + columnas[2] + " from " + baseTableName +
					 * " natural join edificios order by (orden)", false,
					 * false);
					 */
                }
                return false;
            }
        });
        /*
         * c.close(); db.close();
		 */
        sv.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                home();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void animarFondo(String cad, boolean cond) {
        int id = R.drawable.fondo;
        Log.e("Seleccionado el fondo", cad);
        if (cad.contains("Bogo")) {
            id = R.drawable.ciudad_universitaria;
            idFondoTras = id;
        }
        if (cad.contains("Amaz")) {
            id = R.drawable.amazonas;
            idFondoTras = id;
        }
        if (cad.contains("Caribe")) {
            id = R.drawable.caribe;
            idFondoTras = id;
        }
        if (cad.contains("Mani")) {
            id = R.drawable.manizales;
            idFondoTras = id;
        }
        if (cad.contains("Mede")) {
            id = R.drawable.medellin;
            idFondoTras = id;
        }
        if (cad.contains("Tumac")) {
            id = R.drawable.tumaco;
            idFondoTras = id;
        }
        if (cad.contains("Palmira")) {
            id = R.drawable.palmira;
            idFondoTras = id;
        }
        if (cad.contains("Orino")) {
            id = R.drawable.orinoquia;
            idFondoTras = id;
        }
        if (cad.contains("Franco")) {
            id = R.drawable.ciudad_universitaria;
            idFondoTras = id;
        }
        if (cad.contains("Museo")) {
            id = R.drawable.ciudad_universitaria;
            idFondoTras = id;
        }
        if (cad.contains("Observatorio")) {
            id = R.drawable.oan_sede_h;
            idFondoTras = id;
        }
        if (cad.contains("Dirección Nacional")) {
            id = R.drawable.uriel;
            idFondoTras = id;
        }
        idFondo = id;
        background = new BitmapDrawable(BitmapFactory.decodeResource(
                getResources(), id));
        Animation aparecer = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.fade_in2);
        aparecer.reset();
        tl.setAnimation(aparecer);
        tl.setBackgroundDrawable(background);
        if (cond) {
            tl.startAnimation(aparecer);
        }
    }

    public void detalles() {
        try {
            Intent deta = new Intent(this, DetailsActivity.class);
            ArrayList<DetailedInformation> datos = new ArrayList<>(getDatos());
            deta.putParcelableArrayListExtra(DetailsFragment.ARG_DATA, datos);
            deta.putExtra(DetailsFragment.ARG_BACKGROUND, idFondoTras);
            deta.putExtra(DetailsFragment.ARG_TITLE, datos.get(0).getInformationTitle());
            startActivity(deta);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            // this.finish();
            if (current == 5) {
                erase(condicion, false);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<DetailedInformation> getDatos() {
        boolean cond = false;
        if (!sql.contains("ASC")) {
            if (current != 5) {
                cond = true;
            }
        }
        return getDatos(condicion, cond);
    }

    @Override
    public void onBackPressed() {
        if (current <= 2) {
            startActivity(new Intent(getApplicationContext(),
                    MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            this.finish();
            super.onBackPressed();
        }
        erase(condicion, true);
        recargar(sql, false, false, 1);
    }

    private void erase(String condicion2, boolean cond) {
        String conds[] = condicion2.split("and");
        String textos[] = path.split(">");

        if (conds.length == 1) {
            // item.setTitleCondensed("");
            path = "";
            condicion = "";
            sql = "select  distinct " + columnas[2] + ", " + columnas[2]
                    + " from " + baseTableName
                    + " natural join " + buildingsTableName + " order by (orden)";
            current = 2;
            animarFondo("", true);
            return;
        }
        String cad = "", cad2 = "";
        for (int i = 0; i < conds.length - 1; i++) {
            if (i != conds.length - 2) {
                cad += conds[i] + "and";
                cad2 += textos[i] + ">";
            } else {
                cad += conds[i];
                cad2 += textos[i];
            }
        }
        // item.setTitleCondensed(cad2);
        path = cad2;
        boolean cont = path.contains("FACULTAD DE");
        if (!cont) {
            tr.setVisibility(View.INVISIBLE);
            CheckedTextView b = findViewById(R.id.buttonDepartamentos);
            b.setChecked(false);
            b = findViewById(R.id.buttonDirectorio);
            b.setChecked(false);
        }
        condicion = cad;
        sql = "select distinct " + columnas[current - 1] + ", " + columnas[2]
                + " from " + baseTableName + " where " + condicion;
        if (cond) {
            current--;
        }
    }

    public void recargar(String cad2) {
        String cad = cad2.replaceAll("i", "_");
        cad = cad.replaceAll("a", "_");
        cad = cad.replaceAll("e", "_");
        cad = cad.replaceAll("o", "_");
        cad = cad.replaceAll("u", "_");
        Log.e("cadena", cad);
        sql = "select secciones, " + columnas[5]
                + ", secciones||extension||departamentos as consulta,_id from "
                + baseTableName + " where consulta like('%" + cad
                + "%') order by NIVEL_ADMINISTRATIVO ASC";
        Log.e("buscado", sql);
        current = 1;
        tr.setVisibility(View.INVISIBLE);
        recargar(sql, true, true, 3);
        buscando = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("current", current);
        outState.putString("sql", sql);
        outState.putString("path", path);
        outState.putInt("idFondo", idFondo);
        outState.putInt("idFondoTras", idFondoTras);
        outState.putString("condicion", condicion);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        try {
            if (savedInstanceState != null) {
                current = savedInstanceState.getInt("current");
                sql = savedInstanceState.getString("sql");
                path = savedInstanceState.getString("path");
                idFondo = savedInstanceState.getInt("idFondo");
                idFondoTras = savedInstanceState.getInt("idFondoTras");
                condicion = savedInstanceState.getString("condicion");
                animarFondo(path, false);
                recargar(sql, current == 5, false, 1);
            }
        } catch (Exception e) {
            Log.e("Error de restauracion", e.toString());
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void recargar(String query, final boolean isLastStep, final boolean isFromSearch,
                         int tipo) {
        try {
            lv.setAdapter(null);
            Cursor c = db.rawQuery(query, null);
            if (current == 3) {
                c = db.rawQuery(query
                        + " and NIVEL_ADMINISTRATIVO between 1 and 4", null);
                if (baseTableName.equals("Base")) {
                    c = db.rawQuery(query, null);
                }
            }
//            simpleCursorAdapter.changeCursorAndColumns(c, from, to);
            final String[][] mat = Util.imprimirLista(c);
            MiAdaptador adapter = new MiAdaptador(this, Util.getcolumn(mat, 0),
                    Util.getcolumn(mat, 1), tipo);
            adapter.fuente = font;
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View vista,
                                        int posicion, long arg3) {
                    seleccion = mat[posicion][0];
                    if (saltar(seleccion)) {
                        irDirecto(seleccion);
                        return;
                    }
                    if (seleccion.contains("Programas")) {
                        irDirecto();
                        return;
                    }
                    if (path.contains("INSTITUTOS ")) {
                        irDirecto(seleccion);
                        return;
                    }
                    if (current == 2
                            || seleccion.contains("Dirección Nacional")) {
                        animarFondo(seleccion, true);
                    }
                    if (path.isEmpty()) {
                        path = seleccion;
                        // item.setTitleCondensed(path.toUpperCase().trim());
                    } else {
                        path = path + ">" + seleccion.toUpperCase().trim();
                        // item.setTitleCondensed(path);
                    }
                    if (isLastStep) {
                        if (isFromSearch) {
                            condicion = "secciones||departamentos like('" +
                                    seleccion + mat[posicion][1] + "')";
                            Log.e("Condicion busqueda", condicion);
                            animarFondo(mat[posicion][1], false);
                            detalles();
                            return;
                        } else {
                            condicion += " and " + columnas[current] + " = '"
                                    + seleccion + "'";
                            detalles();
                            return;
                        }
                    }
                    current++;
                    int resta = 1;
                    if (condicion.equals("")) {
                        condicion = columnas[current - resta] + " = '"
                                + seleccion + "'";

                    } else {
                        condicion += " and " + columnas[current - resta]
                                + " = '" + seleccion + "'";
                    }
                    sql = "select  distinct " + columnas[current] + ", "
                            + columnas[2] + " from " + baseTableName + "  where "
                            + condicion;
                    // Toast.makeText(getApplication(), sql, Toast.LENGTH_LONG)
                    // .show();
                    recargar(sql, current == 5, false, 1);
                    if (path.contains("FACULTAD DE")) {
                        tr.setVisibility(View.VISIBLE);
                        refresh(findViewById(R.id.buttonDirectorio));
                    }
                }
            });
        } catch (Exception e) {
            Log.e("error al recargar ", e.toString());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    protected boolean saltar(String seleccion2) {
        if (seleccion2.contains("Museo")) {
            return true;
        }
        return seleccion2.contains("Roberto");
    }

    public void irDirecto(String seleccion) {
        String query = columnas[current] + " = '" + seleccion + "'";
        if (condicion != "") {
            query = condicion + " and " + query;
        }
        Intent deta = new Intent(this, DetailsActivity.class);
        ArrayList<DetailedInformation> datos = new ArrayList<>(getDatos(query, false));
        deta.putParcelableArrayListExtra(DetailsFragment.ARG_DATA, datos);
        try {
            deta.putExtra("fondo", idFondoTras);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        startActivity(deta);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void irDirecto() {
        String query = columnas[current] + " = '" + seleccion + "' ";
        String baseConsult = "select url from enlace natural join " + baseTableName
                + " where ";
        String consulta = baseConsult + condicion + " and "
                + query;
        Log.e("consulta", consulta);
        Cursor c = db.rawQuery(consulta, null);
        String[][] mat = Util.imprimirLista(c);
        c.close();
        Util.irA(mat[0][0], this);
    }

    public List<DetailedInformation> getDatos(String baseConsult, String criteria,
                                              boolean cond) {
        String consulta = baseConsult + criteria;
        Log.e("SQL ORIGINAL", consulta);
        if (cond) {
            consulta = sql;
        }

        Log.e("consulta", consulta);

        Cursor c = db.rawQuery(consulta, null);
        String[][] mat = Util.imprimirLista(c);
        c.close();
        Log.e("datos", Util.toString(mat));
        List<InformationElement> infos = new ArrayList<>();
        DetailedInformation dt;
        List<DetailedInformation> detailedInformations = new ArrayList<>();
        for (int i = 0; i < mat.length; i++) {
            int size = mat[i].length;
            for (int j = 2; j < size - 4; j++) {
                if (mat[i][j] != null || !mat[i][j].isEmpty()) {
                    infos.add(new InformationElement(mat[i][j]));
                    Log.e("info", mat[i][j]);
                }
            }
            infos.add(new InformationElement(mat[i][size - 4], mat[i][size - 3], mat[i][size - 2], mat[i][size - 1]));
            Log.e("title", mat[i][0]);
            dt = new DetailedInformation(infos, mat[i][0]);
            detailedInformations.add(dt);
            infos = new ArrayList<>();
        }

        return detailedInformations;
    }

    public List<DetailedInformation> getDatos(String criteria, boolean cond) {
        String consulta = "SELECT departamentos,secciones,directo,extension,correo_electronico," +
                "url,NOMBRE_EDIFICIO,piso_oficina, LATITUD,LONGITUD FROM "
                + baseTableName
                + " natural join "
                + buildingsTableName
                + " natural join "
                + webTableName + " where ";
        return getDatos(consulta, criteria, cond);
    }

    public void refresh(View v) {
        CheckedTextView b = null;
        CheckedTextView b2;
        switch (v.getId()) {
            case R.id.buttonDirectorio:
                b = findViewById(v.getId());
                b.setChecked(true);
                b2 = findViewById(R.id.buttonDepartamentos);
                b2.setChecked(false);
                break;
            case R.id.buttonDepartamentos:
                b = findViewById(v.getId());
                b.setChecked(true);
                b2 = findViewById(R.id.buttonDirectorio);
                b2.setChecked(false);
                break;
            default:
                break;
        }
        refresh(v.getId() == R.id.buttonDirectorio);
    }

    private void refresh(boolean cond) {
        String auxCond = "";
        if (cond) {
            auxCond = " and departamentos not like('%Departamento%')";
        } else {
            auxCond = " and departamentos like('%Departamento%')";
        }
        try {
            String query = "select distinct departamentos,sede from "
                    + baseTableName + " where " + condicion;
            Cursor c = db.rawQuery(query + auxCond, null);
            Log.e("consulta recarga", query + auxCond);
            final String[][] mat = Util.imprimirLista(c);
            if (mat.length == 0) {
                Toast.makeText(getApplicationContext(),
                        this.getString(R.string.no_hay) + " " + (cond ? this.getString(R.string.textSwitchON) : this.getString(R.string.textSwitchOFF)), Toast.LENGTH_SHORT)
                        .show();
                c.close();
                return;
            }
            c.close();
            lv.setAdapter(null);
            MiAdaptador adapter = new MiAdaptador(this, Util.getcolumn(mat, 0),
                    Util.getcolumn(mat, 1), 1);
            adapter.fuente = Typeface.createFromAsset(getAssets(),
                    "Helvetica.ttf");
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View vista,
                                        int posicion, long arg3) {
                    seleccion = mat[posicion][0];
                    if (seleccion.contains("Observatorio")) {
                        animarFondo(seleccion, true);
                    }
                    condicion += " and " + columnas[5] + " = '" + seleccion
                            + "'";
                    sql = "select  distinct " + columnas[5] + ", "
                            + columnas[2] + ",_id from " + baseTableName + "  where "
                            + condicion;
                    detalles();
                    return;
                }
            });
        } catch (Exception e) {
            Log.e("error al recargar ", e.toString());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
