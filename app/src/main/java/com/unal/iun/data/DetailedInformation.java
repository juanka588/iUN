package com.unal.iun.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JuanCamilo on 22/11/2015.
 */
public class DetailedInformation implements Parcelable {
    private List<InformationElement> mInformationElements;
    private String informationTitle;

    public static final Creator<DetailedInformation> CREATOR = new Creator<DetailedInformation>() {
        @Override
        public DetailedInformation createFromParcel(Parcel in) {
            return new DetailedInformation(in);
        }

        @Override
        public DetailedInformation[] newArray(int size) {
            return new DetailedInformation[size];
        }
    };

    public DetailedInformation(List<InformationElement> informationElements, String informationTitle) {
        mInformationElements = informationElements;
        this.informationTitle = informationTitle;
    }

    private DetailedInformation(Parcel in) {
        informationTitle = in.readString();
        Parcelable[] items = in.readParcelableArray(InformationElement.class.getClassLoader());
        if (items == null) {
            Log.e(DetailedInformation.class.getSimpleName(), "details array not found " + informationTitle);
            return;
        }
        mInformationElements = new ArrayList<>();
        for (Parcelable element : items) {
            mInformationElements.add((InformationElement) element);
        }
    }

    public List<InformationElement> getInformationElements() {
        return mInformationElements;
    }

    public String getInformationTitle() {
        return informationTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(informationTitle);
        dest.writeParcelableArray(mInformationElements.toArray(new InformationElement[0]), flags);
    }
}
