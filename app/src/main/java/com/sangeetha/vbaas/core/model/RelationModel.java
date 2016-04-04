package com.sangeetha.vbaas.core.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Poovarasan on 3/26/2016.
 */
public class RelationModel implements Parcelable {
    String name;
    String contacts;

    public RelationModel(String name, String contacts) {
        this.name = name;
        this.contacts = contacts;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.contacts);
    }

    protected RelationModel(Parcel in) {
        this.name = in.readString();
        this.contacts = in.readString();
    }

    public static final Parcelable.Creator<RelationModel> CREATOR = new Parcelable.Creator<RelationModel>() {
        @Override
        public RelationModel createFromParcel(Parcel source) {
            return new RelationModel(source);
        }

        @Override
        public RelationModel[] newArray(int size) {
            return new RelationModel[size];
        }
    };
}
