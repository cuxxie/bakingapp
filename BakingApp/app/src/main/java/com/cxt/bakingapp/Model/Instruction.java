package com.cxt.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hendri on 8/4/17.
 */

public class Instruction implements Parcelable {


    public Instruction() {
    }

    public Instruction(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {

        public Instruction createFromParcel(Parcel in) {
            return new Instruction(in);
        }

        public Instruction[] newArray(int size) {
            return new Instruction[size];
        }
    };
}
