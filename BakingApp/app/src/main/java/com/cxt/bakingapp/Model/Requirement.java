package com.cxt.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hendri on 8/4/17.
 */

public class Requirement implements Parcelable {


    public Requirement() {
    }

    public Requirement(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static final Creator CREATOR
            = new Creator() {

        public Requirement createFromParcel(Parcel in) {
            return new Requirement(in);
        }

        public Requirement[] newArray(int size) {
            return new Requirement[size];
        }
    };
}
