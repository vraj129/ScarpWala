package com.example.scrapwala;

public class UserHelperClass {

    String full_name,user_email,user_phn,user_pass,user_type;

    public UserHelperClass() {
    }

    public UserHelperClass(String full_name, String user_email, String user_phn, String user_pass, String user_type) {
        this.full_name = full_name;
        this.user_email = user_email;
        this.user_phn = user_phn;
        this.user_pass = user_pass;
        this.user_type = user_type;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phn() {
        return user_phn;
    }

    public void setUser_phn(String user_phn) {
        this.user_phn = user_phn;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
