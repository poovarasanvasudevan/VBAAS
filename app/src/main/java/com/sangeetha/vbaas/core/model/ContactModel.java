package com.sangeetha.vbaas.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Poovarasan on 3/27/2016.
 */
public class ContactModel implements Parcelable {
    String name;
    String relation;



    public ContactModel(String name, String relation) {
        this.name = name;
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.relation);
    }

    protected ContactModel(Parcel in) {
        this.name = in.readString();
        this.relation = in.readString();
    }

    public static final Parcelable.Creator<ContactModel> CREATOR = new Parcelable.Creator<ContactModel>() {
        @Override
        public ContactModel createFromParcel(Parcel source) {
            return new ContactModel(source);
        }

        @Override
        public ContactModel[] newArray(int size) {
            return new ContactModel[size];
        }
    };
}
