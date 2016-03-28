package com.sangeetha.vbaas.core.model;

/**
 * Created by Poovarasan on 3/26/2016.
 */
public class RelationModel {
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
}
