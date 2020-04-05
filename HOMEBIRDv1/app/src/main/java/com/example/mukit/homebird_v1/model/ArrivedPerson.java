package com.example.mukit.homebird_v1.model;

public class ArrivedPerson {

    private String name;
    private String date;
    private String time;
    private String image_url;
    private String view;

    private  String timestamp;

    public  ArrivedPerson(){

    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ArrivedPerson(String name, String date, String time, String image_url, String view, String timestamp) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.image_url = image_url;
        this.view = view;
        this.timestamp= timestamp;

    }




    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getImage_url() {
        return image_url;
    }




    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
