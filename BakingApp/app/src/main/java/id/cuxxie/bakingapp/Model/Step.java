package id.cuxxie.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by hendri on 8/4/17.
 */

public class Step implements Parcelable {

    int id;
    @JsonIgnore
    int instructionId;
    String shortDescription;
    String description;
    String videoURL;
    @JsonProperty("thumbnailURL")
    String thumbnailURL;
    @JsonIgnore
    int rowid;
    public Step() {
    }

    public Step(Parcel in) {
        this.rowid = in.readInt();
        this.id = in.readInt();
        this.instructionId = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.rowid);
        parcel.writeInt(this.id);
        parcel.writeInt(this.instructionId);
        parcel.writeString(this.shortDescription);
        parcel.writeString(this.description);
        parcel.writeString(this.videoURL);
        parcel.writeString(this.thumbnailURL);
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

    public Step(int rowid,int id, int instructionId ,String shortDescription, String description, String videoURL, String thubmnailURL) {
        this.rowid = rowid;
        this.id = id;
        this.instructionId = instructionId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thubmnailURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThubmnailURL() {
        return thumbnailURL;
    }

    public void setThubmnailURL(String thubmnailURL) {
        this.thumbnailURL = thubmnailURL;
    }

    public int getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(int instructionId) {
        this.instructionId = instructionId;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }
}
