package com.example.j_lds.shoppingdrive.object_class;

import java.util.ArrayList;

public class Basket {

    private ArrayList<String> names;
    private ArrayList<Double> price;

    public Basket() {
    }

    public void addNames(String names) {
        this.names.add(names);
    }

    public void getNames(int i) {
        this.names.get(i);
    }

    public ArrayList<Double> getPrice() {
        return price;
    }

    public void setPrice(ArrayList<Double> price) {
        this.price = price;
    }


}
