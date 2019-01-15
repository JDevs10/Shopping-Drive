package com.example.j_lds.shoppingdrive.object_class;


import java.util.ArrayList;

public class Merchants {

    private ArrayList<String> imageView_merchants;
    private ArrayList<String> merchant_names;

    public Merchants() {
    }

    public String getImageView_merchants_index(int i) {
        return imageView_merchants.get(i);
    }

    public void setImageView_merchants_index(String img_name) {
        this.imageView_merchants.add(img_name);
    }

    public String getMerchant_names(int i) {
        return merchant_names.get(i);
    }

    public void setMerchant_namess(String merchant_name) {
        this.merchant_names.add(merchant_name);
    }
}
