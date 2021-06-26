package com.example.scrapwala.normal_user;

public class model {
    String scrap_name,status,scarp_image_uri;

    model()
    {

    }
    public model(String scrap_name, String status, String scarp_image_uri) {
        this.scrap_name = scrap_name;
        this.status = status;
        this.scarp_image_uri = scarp_image_uri;
    }

    public String getScrap_name() {
        return scrap_name;
    }

    public String getScarp_image_uri() {
        return scarp_image_uri;
    }

    public void setScarp_image_uri(String scarp_image_uri) {
        this.scarp_image_uri = scarp_image_uri;
    }

    public void setScrap_name(String scrap_name) {
        this.scrap_name = scrap_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
