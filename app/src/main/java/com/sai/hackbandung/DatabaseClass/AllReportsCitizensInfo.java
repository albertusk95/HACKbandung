package com.sai.hackbandung.DatabaseClass;

import com.sai.hackbandung.Fragments.AllReportsCitizens;

/**
 * Created by AlbertusK95 on 4/1/2017.
 */

public class AllReportsCitizensInfo {

    private String imageForVerification;
    private String userProfileImage;
    private String topic;
    private String postingDate;
    private String responsibleAgency;
    private String address;
    private String status;

    public AllReportsCitizensInfo() {

    }

    public AllReportsCitizensInfo(String imageForVerification, String userProfileImage, String topic, String postingDate,
                              String responsibleAgency, String address, String status) {

        this.imageForVerification = imageForVerification;
        this.userProfileImage = userProfileImage;
        this.topic = topic;
        this.postingDate = postingDate;
        this.responsibleAgency = responsibleAgency;
        this.address = address;
        this.status = status;

    }

    // SETTER
    public void setImageForVerification(String imageForVerification) {
        this.imageForVerification = imageForVerification;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public void setResponsibleAgency(String responsibleAgency) {
        this.responsibleAgency = responsibleAgency;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // GETTER
    public String getImageForVerification() {
        return imageForVerification;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public String getTopic() {
        return topic;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public String getResponsibleAgency() {
        return responsibleAgency;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

}
