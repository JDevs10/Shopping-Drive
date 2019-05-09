package com.example.j_lds.shoppingdrive.databaseOffline.model;

public class Settings {
    private int id;
    private String selectedMerchantUid;
    private String selectedMerchantCompanyName;

    public Settings(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSelectedMerchantUid() {
        return selectedMerchantUid;
    }

    public void setSelectedMerchantUid(String selectedMerchantUid) {
        this.selectedMerchantUid = selectedMerchantUid;
    }

    public String getSelectedMerchantCompanyName() {
        return selectedMerchantCompanyName;
    }

    public void setSelectedMerchantCompanyName(String selectedMerchantCompanyName) {
        this.selectedMerchantCompanyName = selectedMerchantCompanyName;
    }
}
