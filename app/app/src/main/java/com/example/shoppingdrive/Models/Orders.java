package com.example.shoppingdrive.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Orders implements Serializable {
    private String id;
    private String ref;
    private Long date;
    private String id_client;
    private String id_merchant;
    private String payment;
    private ArrayList<Product> products;
    private double total_price;

    public Orders(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getClientId() {
        return id_client;
    }

    public void setClientId(String clientId) {
        this.id_client = clientId;
    }

    public String getMerchantId() {
        return id_merchant;
    }

    public void setMerchantId(String merchantId) {
        this.id_merchant = merchantId;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return total_price;
    }

    public void setTotalPrice(double totalPrice) {
        this.total_price = totalPrice;
    }
}
