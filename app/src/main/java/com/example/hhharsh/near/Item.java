package com.example.hhharsh.near;

/**
 * Created by hhharsh on 3/1/18.
 */

public class Item {

    private String loc_name;
    private String address;
    private Double rating;
    private String url;




    public Item(String loc, String address, Double rating, String url){
        this.loc_name=loc;
        this.address=address;
        this.rating=rating;
        this.url=url;

    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
