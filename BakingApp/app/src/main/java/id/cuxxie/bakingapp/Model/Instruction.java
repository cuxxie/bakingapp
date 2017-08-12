package id.cuxxie.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by hendri on 8/4/17.
 */

public class Instruction implements Parcelable {
    int id;
    String name;
    @JsonProperty("ingredients")
    ArrayList<Requirement> requirements;
    ArrayList<Step> steps;
    int servings;
    String image;
    public Instruction() {
    }

    public Instruction(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        in.readTypedList(this.requirements,Requirement.CREATOR);
        in.readTypedList(this.steps,Step.CREATOR);
        this.servings = in.readInt();
        this.image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeTypedList(this.requirements);
        parcel.writeTypedList(this.steps);
        parcel.writeInt(this.servings);
        parcel.writeString(this.image);
    }

    public static final Creator CREATOR
            = new Creator() {

        public Instruction createFromParcel(Parcel in) {
            return new Instruction(in);
        }

        public Instruction[] newArray(int size) {
            return new Instruction[size];
        }
    };

    public Instruction(int id, String name, int servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<Requirement> requirements) {
        this.requirements = requirements;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
