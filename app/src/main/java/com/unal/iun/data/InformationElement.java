package com.unal.iun.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.unal.iun.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JuanCamilo on 22/11/2015.
 */
public class InformationElement implements Parcelable {

    public static final Creator<InformationElement> CREATOR = new Creator<InformationElement>() {
        @Override
        public InformationElement createFromParcel(Parcel in) {
            return new InformationElement(in);
        }

        @Override
        public InformationElement[] newArray(int size) {
            return new InformationElement[size];
        }
    };
    private static final String DIVIDER = "-";
    private final int TYPE_EXTENSION = 0;
    private final int TYPE_PHONE = 1;
    private final int TYPE_EMAIL = 2;
    private final int TYPE_MAP_POINT = 3;
    private final int TYPE_URL = 4;

    private String informationDescription;
    private int informationIcon;
    private int type;
    private LatLng coordinates;

    public InformationElement(Parcel in) {
        informationDescription = in.readString();
        informationIcon = in.readInt();
        type = in.readInt();
        setType(check(informationDescription));
    }

    public InformationElement(String informationDescription) {
        this.informationDescription = informationDescription;
        setType(check(informationDescription));
    }

    public InformationElement(String buildName, String buildNumber, String lat, String lon) {
        this.informationDescription = buildName + DIVIDER + buildNumber;
        setType(TYPE_MAP_POINT);
        coordinates = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInformationDescription() {
        return informationDescription;
    }

    public void setInformationDescription(String informationDescription) {
        this.informationDescription = informationDescription;
    }

    public int getInformationIcon() {
        return informationIcon;
    }

    public void setInformationIcon(int informationIcon) {
        this.informationIcon = informationIcon;
    }

    private int check(String cad) {
        int ret = TYPE_MAP_POINT;
        setInformationIcon(R.drawable.edificio);
        Pattern p = Pattern.compile("[0-9]{0,}");
        Matcher m = p.matcher(cad);
        if (m.matches()) {
            setInformationIcon(R.drawable.llamar);
            ret = TYPE_EXTENSION;
        } else {
            p = Pattern.compile("[0-9]{0,5}");
            m = p.matcher(cad);
            if (m.matches()) {
                setInformationIcon(R.drawable.llamar);
                ret = TYPE_PHONE;
            } else if (cad.contains("@")) {
                setInformationIcon(R.drawable.email);
                ret = TYPE_EMAIL;
            } else {
                p = Pattern.compile(".*[w-y]{3}.*");
                m = p.matcher(cad);
                if (m.matches()) {
                    setInformationIcon(R.drawable.un);
                    ret = TYPE_URL;
                } else if (cad.contains("http")) {
                    setInformationIcon(R.drawable.un);
                    ret = TYPE_URL;
                }
            }
        }
        return ret;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(informationDescription);
        dest.writeInt(informationIcon);
        dest.writeInt(type);
    }

    public MapMarker getMapMarker() {
        return new MapMarker(getCoordinates(), getInformationTitle(), getInformationSubTitle(), 0, BitmapDescriptorFactory.HUE_VIOLET);
    }

    @Override
    public String toString() {
        return informationDescription;
    }

    public String getInformationTitle() {
        return informationDescription.split(DIVIDER)[0];
    }

    public String getInformationSubTitle() {
        return informationDescription.split(DIVIDER)[1];
    }
}
