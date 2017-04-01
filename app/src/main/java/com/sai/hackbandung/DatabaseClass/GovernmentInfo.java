package com.sai.hackbandung.DatabaseClass;

/**
 * Created by AlbertusK95 on 4/1/2017.
 */

public class GovernmentInfo {

    private String username;
    private String email;
    private String password;
    private String full_name;
    private String agency_type;

    public GovernmentInfo(String username, String email, String password, String full_name, String agency_type) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.full_name = full_name;
        this.agency_type = agency_type;

    }

    // SETTER
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public  void setFullName(String full_name) {
        this.full_name = full_name;
    }

    public void setAgencyType(String agency_type) {
        this.agency_type = agency_type;
    }

    // GETTER
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return full_name;
    }

    public String getAgencyTpe() {
        return agency_type;
    }

}
