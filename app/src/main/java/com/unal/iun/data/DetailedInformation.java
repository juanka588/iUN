package com.unal.iun.data;

import android.os.Parcel;
import android.os.Parcelable;

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

    protected DetailedInformation(Parcel in) {
        informationTitle = in.readString();
        Parcelable[] items = in.readParcelableArray(InformationElement.class.getClassLoader());
        mInformationElements = new ArrayList<>();
        for (Parcelable element : items) {
            mInformationElements.add((InformationElement) element);
        }
    }

    public List<InformationElement> getInformationElements() {
        return mInformationElements;
    }

    public void setInformationElements(List<InformationElement> informationElements) {
        mInformationElements = informationElements;
    }

    public String getInformationTitle() {
        return informationTitle;
    }

    public void setInformationTitle(String informationTitle) {
        this.informationTitle = informationTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(informationTitle);
        InformationElement[] elements = new InformationElement[mInformationElements.size()];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = mInformationElements.get(i);
        }
        dest.writeParcelableArray(elements, flags);
    }
}
