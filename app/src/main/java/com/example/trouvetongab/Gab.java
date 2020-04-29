package com.example.trouvetongab;

public class Gab {
    private String title, location;
    private int posted;

    public Gab(String title, String image, int posted){
        this.title = title;
        this.location = image;
        this.posted = posted;
    }

    public String getTitle(){return title;}
    public String getLocation(){return location;}
    public int getPosted(){return posted;}
}
