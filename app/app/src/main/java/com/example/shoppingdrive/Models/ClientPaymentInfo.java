package com.example.shoppingdrive.Models;

public class ClientPaymentInfo {
    private String type;
    private Long number;
    private Long crypto;
    private String expireDate;

    public ClientPaymentInfo() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getCrypto() {
        return crypto;
    }

    public void setCrypto(Long crypto) {
        this.crypto = crypto;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
