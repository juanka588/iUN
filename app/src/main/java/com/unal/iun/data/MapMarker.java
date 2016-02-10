package com.unal.iun.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by JuanCamilo on 22/11/2015.
 */
public class MapMarker implements Parcelable {
    public static final Creator<MapMarker> CREATOR = new Creator<MapMarker>() {
        @Override
        public MapMarker createFromParcel(Parcel in) {
            return new MapMarker(in);
        }

        @Override
        public MapMarker[] newArray(int size) {
            return new MapMarker[size];
        }
    };
    private LatLng position;
    private String title;
    private String description;
    private int type;
    private float icon;

    public MapMarker(LatLng position, String title, String description, int type, float icon) {
        this.position = position;
        this.title = title;
        this.description = description;
        this.type = type;
        this.icon = icon;
    }

    protected MapMarker(Parcel in) {
        position = in.readParcelable(LatLng.class.getClassLoader());
        title = in.readString();
        description = in.readString();
        type = in.readInt();
        icon = in.readFloat();
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getIcon() {
        return icon;
    }

    public void setIcon(float icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(position, flags);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(type);
        dest.writeFloat(icon);
    }
}
