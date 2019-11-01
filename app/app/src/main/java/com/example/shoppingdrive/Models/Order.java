package com.example.shoppingdrive.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private String id;
    private Long date;
    private String clientId;
    private String merchantId;
    private float totalPrice;
    private String payment;
    private String ref;
    private ArrayList<Product> products;

    public Order() {
    }

    public Order(String id, Long date, String clientId, String merchantId, float totalPrice, String payment, String ref, ArrayList<Product> products) {
        this.id = id;
        this.date = date;
        this.clientId = clientId;
        this.merchantId = merchantId;
        this.totalPrice = totalPrice;
        this.payment = payment;
        this.ref = ref;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
