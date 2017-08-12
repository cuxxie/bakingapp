package id.cuxxie.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by hendri on 8/4/17.
 */

public class Requirement implements Parcelable {
    int id = 0;
    int quantity;
    String measure;
    @JsonProperty("ingredient")
    String name;
    @JsonIgnore
    boolean status;
    @JsonIgnore
    int instructionid;
    public Requirement() {
    }

    public Requirement(Parcel in) {
        this.quantity = in.readInt();
        this.measure = in.readString();
        this.name = in.readString();
    }

    public Requirement(int id,int quantity, String measure, String name, int status, int instructionid) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
        this.status = (status==1);
        this.instructionid = instructionid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.quantity);
        parcel.writeString(this.measure);
        parcel.writeString(this.name);
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getInstructionid() {
        return instructionid;
    }

    public void setInstructionid(int instructionid) {
        this.instructionid = instructionid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
