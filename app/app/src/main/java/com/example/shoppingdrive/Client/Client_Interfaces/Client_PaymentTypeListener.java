package com.example.shoppingdrive.Client.Client_Interfaces;

public interface Client_PaymentTypeListener {
    void CreditCardChecked(boolean isChecked);
    void DiscoverCardChecked(boolean isChecked);
    void PaypalCardChecked(boolean isChecked);
}
