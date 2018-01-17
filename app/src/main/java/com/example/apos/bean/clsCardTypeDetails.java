package com.example.apos.bean;

/**
 * Created by Prashant on 9/22/2015.
 */
public class clsCardTypeDetails {

    private String cardTypeCode;
    private String cardTypeDesc;
    private String allowTopUp;
    private String cardExpired;
    private long validityDays;
    private String customerCompulsory;
    private String cashCard;


    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardTypeDesc() {
        return cardTypeDesc;
    }

    public void setCardTypeDesc(String cardTypeDesc) {
        this.cardTypeDesc = cardTypeDesc;
    }

    public String getAllowTopUp() {
        return allowTopUp;
    }

    public void setAllowTopUp(String allowTopUp) {
        this.allowTopUp = allowTopUp;
    }

    public String getCardExpired() {
        return cardExpired;
    }

    public void setCardExpired(String cardExpired) {
        this.cardExpired = cardExpired;
    }

    public long getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(long validityDays) {
        this.validityDays = validityDays;
    }

    public String getCustomerCompulsory() {
        return customerCompulsory;
    }

    public void setCustomerCompulsory(String customerCompulsory) {
        this.customerCompulsory = customerCompulsory;
    }

    public String getCashCard() {
        return cashCard;
    }

    public void setCashCard(String cashCard) {
        this.cashCard = cashCard;
    }
}
