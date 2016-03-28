package com.sangeetha.vbaas.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Poovarasan on 3/28/2016.
 */
public class RecentBlocked extends RealmObject {
    private String name;
    private String number;
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
