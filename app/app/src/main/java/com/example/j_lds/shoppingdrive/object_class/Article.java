package com.example.j_lds.shoppingdrive.object_class;


public class Article {

    private String image; //this will change in the future but it is use for test pruposes
    private String name;
    private double price;
    private String description;

    public Article() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}