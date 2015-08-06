package com.unal.iun.data;

import com.unal.iun.R;

/**
 * Created by JuanCamilo on 05/08/2015.
 */
public class WebItem {
    public String name;
    public int icon;
    public String url;
    public boolean special = false;

    public WebItem(String name, int icon, String url, boolean special) {
        this.name = name;
        this.icon = icon == 0 ? R.drawable.un : icon;
        this.url = url;
        this.special = special;
    }

}
