package com.unal.iun.data;

import com.unal.iun.R;

/**
 * Created by JuanCamilo on 05/08/2015.
 */
public class WebItem {
    private final String name;
    private final int icon;
    private final String url;

    public WebItem(String name, int icon, String url) {
        this.name = name;
        this.icon = icon == 0 ? R.drawable.un : icon;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public String getUrl() {
        return url;
    }
}
