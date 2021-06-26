package com.example.scrapwala;

public class ScrapInfo {
    String scrap_name,scarp_image_uri,scrap_weight,user_phn,user_address,user_pin,scrap_latitude,scrap_longitude,scrap_reserve="no",scrap_reserve_ragman_phn="None",status="active";

    public ScrapInfo(String scrap_name, String scarp_image_uri, String scrap_weight, String user_phn, String user_address, String user_pin, String scrap_latitude, String scrap_longitude) {
        this.scrap_name = scrap_name;
        this.scarp_image_uri = scarp_image_uri;
        this.scrap_weight = scrap_weight;
        this.user_phn = user_phn;
        this.user_address = user_address;
        this.user_pin = user_pin;
        this.scrap_latitude = scrap_latitude;
        this.scrap_longitude = scrap_longitude;
    }

    public ScrapInfo() {
    }

    public String getScrap_name() {
        return scrap_name;
    }

    public void setScrap_name(String scrap_name) {
        this.scrap_name = scrap_name;
    }

    public String getScarp_image_uri() {
        return scarp_image_uri;
    }

    public void setScarp_image_uri(String scarp_image_uri) {
        this.scarp_image_uri = scarp_image_uri;
    }

    public String getScrap_weight() {
        return scrap_weight;
    }

    public void setScrap_weight(String scrap_weight) {
        this.scrap_weight = scrap_weight;
    }

    public String getUser_phn() {
        return user_phn;
    }

    public void setUser_phn(String user_phn) {
        this.user_phn = user_phn;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_pin() {
        return user_pin;
    }

    public void setUser_pin(String user_pin) {
        this.user_pin = user_pin;
    }

    public String getScrap_latitude() {
        return scrap_latitude;
    }

    public void setScrap_latitude(String scrap_latitude) {
        this.scrap_latitude = scrap_latitude;
    }

    public String getScrap_longitude() {
        return scrap_longitude;
    }

    public void setScrap_longitude(String scrap_longitude) {
        this.scrap_longitude = scrap_longitude;
    }

    public String getScrap_reserve() {
        return scrap_reserve;
    }

    public void setScrap_reserve(String scrap_reserve) {
        this.scrap_reserve = scrap_reserve;
    }

    public String getScrap_reserve_ragman_phn() {
        return scrap_reserve_ragman_phn;
    }

    public void setScrap_reserve_ragman_phn(String scrap_reserve_ragman_phn) {
        this.scrap_reserve_ragman_phn = scrap_reserve_ragman_phn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
