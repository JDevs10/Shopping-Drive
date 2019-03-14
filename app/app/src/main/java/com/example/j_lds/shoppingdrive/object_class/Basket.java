package com.example.j_lds.shoppingdrive.object_class;

import java.util.ArrayList;

public class Basket {

    private ArrayList<Article> article;

    public Basket() {
    }

    public void setBasketList(ArrayList<Article> basket) {
        this.article = basket;
    }

    public ArrayList<Article> getPrice() {
        return article;
    }


}
