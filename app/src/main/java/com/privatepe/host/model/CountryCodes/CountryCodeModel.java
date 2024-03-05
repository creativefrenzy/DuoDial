package com.privatepe.host.model.CountryCodes;

public class CountryCodeModel {


    String country;
    String countryCode;
    String countryImg;
    private boolean isSelected;


    public CountryCodeModel(String country, String countryCode,String countryImg) {
        this.countryImg = countryImg;
        this.country = country;
        this.countryCode = countryCode;
    }


    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getCountryImg() {
        return countryImg;
    }

    public void setCountryImg(String countryImg) {
        this.countryImg = countryImg;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


}
