package com.unal.iun.LN;

import android.provider.BaseColumns;

/**
 * Created by JuanCamilo on 07/04/2015.
 */
public class DirectoryContract {
    public static final class BaseProvider implements BaseColumns {
        public static final String TABLE_NAME = "BaseM";
        // Column with the foreign key into the location table.
        public static final String COLUMN_NIVEL_ADM = "NIVEL_ADMINISTRATIVO";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_SEDE = "SEDE";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_DEPENDENCIA = "DEPENDENCIAS";

        // Short description and long description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_DIVISIONES = "DIVISIONES";
        public static final String COLUMN_DEPARTAMENTOS = "DEPARTEMENTOS";
        public static final String COLUMN_SECCIONES = "SECCIONES";
        public static final String COLUMN_EMAIL = "CORREO_ELECTRONICO";
        public static final String COLUMN_EXTENSION = "EXTENSION";
        public static final String COLUMN_DIRECTO = "DIRECTO";
        public static final String COLUMN_EDIFICIO = "EDIFICIO";
        public static final String COLUMN_ID_ENLACE = "id_enlace";


    }

    public static final class EnlacesProvider implements BaseColumns {

    }

    public static final class EdificiosProvider implements BaseColumns {

    }

    public static final class ColegiosProvider implements BaseColumns {

    }

}
