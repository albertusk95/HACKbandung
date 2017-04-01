package com.sai.hackbandung.DatabaseClass;

public class CitizensInfo {

    private String username;
    private String email;
    private String password;
    private String full_name;

    public CitizensInfo(String username, String email, String password, String full_name) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.full_name = full_name;

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

    public String FullName() {
        return full_name;
    }

}