package com.unal.iun.LN;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.unal.iun.GUI.MainActivity;
import com.unal.iun.GUI.WebActivity;
import com.unal.iun.R;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;
import java.util.Date;

/**
 * Created by JuanCamilo on 15/03/2015.
 */
public class Util {

    private static final String TIME_SERVER = "co.pool.ntp.org";

    public static long getCurrentNetworkTime() {
        NTPUDPClient lNTPUDPClient = new NTPUDPClient();
        lNTPUDPClient.setDefaultTimeout(3000);
        long returnTime = new Date().getTime();
        try {
            lNTPUDPClient.open();
            InetAddress lInetAddress = InetAddress.getByName(TIME_SERVER);
            TimeInfo lTimeInfo = lNTPUDPClient.getTime(lInetAddress);
            // returnTime =  lTimeInfo.getReturnTime(); // local time
            String dateString = lTimeInfo.getMessage().getTransmitTimeStamp().toDateString();
            log("server date string", dateString);
            returnTime = lTimeInfo.getMessage().getTransmitTimeStamp().getTime();   //server time
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lNTPUDPClient.close();
        }
        return returnTime;
    }

    public static boolean isOffline(Activity acc) {
        ConnectivityManager cm = (ConnectivityManager) acc.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null) {
            return !netInfo.isConnectedOrConnecting();
        }
        return true;
    }

    public static double[] toDouble(String[] getcolumn) {
        double[] conv = new double[getcolumn.length];
        try {
            for (int i = 0; i < conv.length; i++) {
                conv[i] = Double.parseDouble(getcolumn[i]);
            }
        } catch (Exception e) {
            Log.e("Error Directorio", e.toString());
        }
        return conv;
    }

    public static String[] getcolumn(String[][] mat, int i) {
        String[] cad = new String[mat.length];
        int k = 0;
        for (String[] strings : mat) {
            for (int j2 = 0; j2 < strings.length; j2++) {
                if (j2 == i) {
                    cad[k] = strings[j2];
                    k++;
                }
            }
        }
        return cad;
    }

    public static String[][] imprimirLista(Cursor cursor) {
        String[][] lista = new String[cursor.getCount()][cursor.getColumnCount()];
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                for (int j = 0; j < cursor.getColumnCount(); j++) {
                    lista[i][j] = cursor.getString(j);
                }
                cursor.moveToNext();
            }
        }
        return lista;
    }

    public static String toString(String[][] mat) {
        StringBuilder cad = new StringBuilder();
        for (String[] strings : mat) {
            cad.append(toString(strings)).append("\n");
        }
        return cad.toString();
    }

    public static String toString(String[] getcolumn) {
        StringBuilder cad = new StringBuilder();
        for (int i = 0; i < getcolumn.length - 1; i++) {
            cad.append(getcolumn[i]);
            cad.append(",");
        }
        cad.append(getcolumn[getcolumn.length - 1]);
        return cad.toString();
    }

    public static String toString(double[] doubleArray) {
        StringBuilder cad = new StringBuilder();
        for (double column : doubleArray) {
            cad.append(column).append(" ");
        }
        return cad.toString();
    }

    public static void notificarRed(final Activity act) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setMessage(act.getText(R.string.network_exception))
                .setTitle(act.getText(R.string.expanded_app_name))
                .setPositiveButton(act.getText(R.string.acept_dialog),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.setNegativeButton(act.getText(R.string.cancel_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (act.getClass() != MainActivity.class) {
                    act.finish();
                }

            }
        });
        builder.create().show();
    }

    public static void enviar(Activity act, String[] to, String[] cc,
                              String asunto, String mensaje) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND)
                .setType("message/rfc822")
                .setData(Uri.parse("mailto:"))
                .putExtra(Intent.EXTRA_EMAIL, to)
                .putExtra(Intent.EXTRA_CC, cc)
                .putExtra(Intent.EXTRA_SUBJECT, asunto)
                .putExtra(Intent.EXTRA_TEXT, mensaje);
        act.startActivity(Intent.createChooser(emailIntent, "Email "));
    }

    public static void enviar(Activity act, String to, String cc,
                              String asunto, String mensaje) {
        enviar(act, new String[]{to}, new String[]{cc}, asunto, mensaje);
    }

    public static void irA(String url, Activity act) {
        Intent deta = new Intent(act, WebActivity.class);
        if (url.contains("Sitio Web")) {
            return;
        }
        deta.putExtra("paginaWeb", url);
        act.startActivity(deta);
    }

    public static CharSequence toCamelCase(String lowerCase) {
        StringBuilder cad = new StringBuilder();
        String[] palabras = lowerCase.split(" ");
        palabras[0] = (palabras[0].charAt(0) + "").toUpperCase()
                + palabras[0].substring(1);
        cad.append(palabras[0]).append(" ");
        for (int i = 1; i < palabras.length; i++) {
            if (palabras[i].length() > 3) {
                palabras[i] = (palabras[i].charAt(0) + "").toUpperCase()
                        + palabras[i].substring(1, palabras[i].length());
            }
            if (palabras[i].contains("un")) {
                palabras[i] = "UN";
            }
            cad.append(palabras[i]).append(" ");
        }
        return cad.toString().trim();
    }

    public static void log(String tag, String msg) {
        if (MainActivity.DEBUG) {
            Log.e(tag, msg);
        }
    }
}
