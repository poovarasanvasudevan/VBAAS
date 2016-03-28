package com.sangeetha.vbaas.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Poovarasan on 3/26/2016.
 */

public class Contacts extends RealmObject {

    private String name;

    @PrimaryKey
    private String number;

    private boolean message;

    private String cmessage;

    public String getCmessage() {
        return cmessage;
    }

    public void setCmessage(String cmessage) {
        this.cmessage = cmessage;
    }

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
