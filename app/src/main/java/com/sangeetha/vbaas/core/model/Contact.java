package com.sangeetha.vbaas.core.model;

/**
 * Created by Poovarasan on 3/26/2016.
 */
public class Contact {
    long id;
    String name;
    String phno;

    public Contact(long id, String name, String phno) {
        this.id = id;
        this.name = name;
        this.phno = phno;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }
}
