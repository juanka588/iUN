package com.unal.iun.DataSource;

import android.provider.BaseColumns;

/**
 * Created by JuanCamilo on 07/04/2015.
 */
public class DirectoryContract {
    public static final class BaseProvider implements BaseColumns {
        public static final String TABLE_NAME_BASE = "BaseM";
        public static final String TABLE_NAME_LINKS = "BaseM";
        // Column with the foreign key into the location table.
        public static final String COLUMN_NIVEL_ADM = "NIVEL_ADMINISTRATIVO";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_SEDE = "SEDE";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_DEPENDENCIA = "DEPENDENCIAS";

        public static final String COLUMN_DIVISIONES = "DIVISIONES";
        public static final String COLUMN_DEPARTAMENTOS = "DEPARTEMENTOS";
        public static final String COLUMN_SECCIONES = "SECCIONES";
        public static final String COLUMN_EMAIL = "CORREO_ELECTRONICO";
        public static final String COLUMN_EXTENSION = "EXTENSION";
        public static final String COLUMN_DIRECTO = "DIRECTO";
        public static final String COLUMN_EDIFICIO = "_id_edificio";
        public static final String COLUMN_ID_ENLACE = TABLE_NAME_LINKS + "._id";


    }

    public static final class EnlacesProvider implements BaseColumns {
        public static final String TABLE_NAME = "ENLACE";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_DECRIPTION = "descripcionEnlace";
        public static final String COLUMN_ICON = "nombre_icono";
        public static final String COLUMN_COMMUNITY_SERVICE = "serv_comunidad";
        public static final String COLUMN_ORDER = "orden_enlace";
        public static final String[] COLUMN_NAMES = new String[]{COLUMN_DECRIPTION, COLUMN_ICON,
                COLUMN_URL, COLUMN_COMMUNITY_SERVICE, COLUMN_ORDER};

    }

    public static final class EdificiosProvider implements BaseColumns {

    }

    public static final class ColegiosProvider implements BaseColumns {

    }

}
