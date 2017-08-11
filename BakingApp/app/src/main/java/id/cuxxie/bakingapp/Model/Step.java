package id.cuxxie.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hendri on 8/4/17.
 */

public class Step implements Parcelable {


    public Step() {
    }

    public Step(Parcel in) {
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

        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
