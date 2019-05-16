package com.example.j_lds.shoppingdrive.object_class_Client;


public class Article {

    private String id;
    private String image; //this will change in the future but it is use for test pruposes
    private String name;
    private double price;
    private int quantity;
    private String description;

    public Article() {
        this.quantity = 1;
    }

    public void setId(String id){  this.id = id;}

    public String getId(){ return id;}

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
