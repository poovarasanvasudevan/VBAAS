package com.sangeetha.vbaas.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Poovarasan on 3/26/2016.
 */


public class Relation extends RealmObject {

    @PrimaryKey
    private String name;
    private String message;

    private RealmList<Contacts> contacts;

    public RealmList<Contacts> getContacts() {
        return contacts;
    }

    public void setContacts(RealmList<Contacts> contacts) {
        this.contacts = contacts;
    }

    public Relation() {
    }

    public Relation(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
