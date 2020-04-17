package com.example.trouvetongab;

public class Bank {
    private int id;
    private String title, image;

    public Bank(int id, String title, String image){
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public int getId(){return id;}
    public String getTitle(){return title;}
    public String getImage(){return image;}
}
