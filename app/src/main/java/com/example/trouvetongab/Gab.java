package com.example.trouvetongab;

public class Gab {
    private String title, location, commune;
    private int posted;

    public Gab(String title, String image, int posted, String commune){
        this.title = title;
        this.location = image;
        this.posted = posted;
        this.commune = commune;
    }

    public String getTitle(){return title;}
    public String getLocation(){return location;}
    public int getPosted(){return posted;}
    public String getCommune(){return commune;}
}
