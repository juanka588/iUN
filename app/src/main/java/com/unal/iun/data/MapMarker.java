package com.unal.iun.data;

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
    private final LatLng position;
    private final String title;
    private final String description;
    private final int type;
    private final float icon;

    public MapMarker(LatLng position, String title, String description, int type, float icon) {
        this.position = position;
        this.title = title;
        this.description = description;
        this.type = type;
        this.icon = icon;
    }

    private MapMarker(Parcel in) {
        position = in.readParcelable(LatLng.class.getClassLoader());
        title = in.readString();
        description = in.readString();
        type = in.readInt();
        icon = in.readFloat();
    }

    public LatLng getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getIcon() {
        return icon;
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
