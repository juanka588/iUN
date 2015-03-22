package com.unal.iun;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;


public class License extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        initText();
        BitmapDrawable background2 = new BitmapDrawable(
                BitmapFactory.decodeResource(getResources(),
                        R.drawable.fondoinf));
        this.getActionBar().setBackgroundDrawable(background2);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void initText() {
        TextView tx1 = (TextView) findViewById(R.id.textDevelop);
        TextView tx2 = (TextView) findViewById(R.id.textLicense);
        tx1.setText("DESAROLLADORES iUNDev:\n\nmahiguerag\nafgranadosc\njcrodriguezd\n"
                + "mechaconc\ngromerop\nmrinconl\naframosp\n"
                + "gmcabanah\njajaramilloa" + "\nmjangaritar"
                + "\n\n @unal.edu.co");
        tx2.setText("LICENCIA DE USO"
                + "\naplicación móvil iUN"
                + "\n\nA- Reglas de uso:\n\n"
                + "A1- Usted estará autorizado a utilizar la aplicación"
                + " móvil iUN sólo para uso personal, no comercial.\n\n"
                + "A2- Puede utilizar iUN para explorar, buscar y/o navegar "
                + "contenidos en su dispositivo móvil. Tenga en cuenta que la "
                + "disponibilidad del contenido puede variar con base en "
                + "actualizaciones de los datos del directorio o enlaces de los sitios web.\n\n"
                + "B- Limitación de responsabilidad\n\n"
                + "B1-. iUN, suministrará servicios con el cuidado y "
                + "profesionalismo razonable. iUN no asume responsabilidades y garantías"
                + " adicionales en relación con el Servicio, "
                + "en particular, no garantiza que:\n\n"
                + "-  El uso que usted haga del Servicio  se mantenga ininterrumpido o"
                + " libre de errores. Usted acepta que ocasionalmente, iUN pueda "
                + "dejar de funcionar por limitaciones de las redes Wi-Fi, o de datos.\n\n"
                + "- El Servicio estará exento de pérdidas, corrupción, ataques, "
                + "virus, interferencia o cualquier intromisión en la seguridad, "
                + "los cuales serán considerados como eventos de Fuerza Mayor, "
                + "y sobre los cuales iUN no será responsable. \n\n"
                + "B2- El equipo de desarrollo iUNDev hará esfuerzos "
                + "razonables por validar, actualizar y proteger la "
                + "información registrada en la aplicación iUN, pero usted "
                + "reconoce y acepta que dicha información es de referencia"
                + " y debe ser siempre validada por usted.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_license, menu);
        return true;
    }

}
