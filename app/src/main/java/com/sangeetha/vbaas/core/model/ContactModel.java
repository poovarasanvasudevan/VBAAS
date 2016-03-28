package com.sangeetha.vbaas.core.model;

/**
 * Created by Poovarasan on 3/27/2016.
 */
public class ContactModel {
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
}
