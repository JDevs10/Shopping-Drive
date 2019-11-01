package com.example.shoppingdrive.Models;

import com.example.shoppingdrive.Models.Product;

import java.util.ArrayList;

public class Basket {

    private ArrayList<Product> article;

    public Basket() {
    }

    public void setBasketList(ArrayList<Product> basket) {
        this.article = basket;
    }

    public ArrayList<Product> getPrice() {
        return article;
    }


}
