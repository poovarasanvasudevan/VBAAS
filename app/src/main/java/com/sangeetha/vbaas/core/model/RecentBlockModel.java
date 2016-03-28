package com.sangeetha.vbaas.core.model;

import java.util.Date;

/**
 * Created by Poovarasan on 3/28/2016.
 */
public class RecentBlockModel {

    String name;
    String number;
    Date date;

    public RecentBlockModel(String name, String number, Date date) {
        this.name = name;
        this.number = number;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
