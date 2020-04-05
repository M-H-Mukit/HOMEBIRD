package com.example.mukit.homebird_v1.model;

public class RegisteredPerson {

    private String name;
    private String relation;
    private String image_url;

    public RegisteredPerson(){

    }

    public RegisteredPerson(String name, String relation, String image_url) {
        this.name = name;
        this.relation = relation;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public String getRelation() {
        return relation;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
