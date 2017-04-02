package com.sai.hackbandung.DatabaseClass;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by AlbertusK95 on 4/2/2017.
 */

@IgnoreExtraProperties
public class ReportInfo {

	public Long imgREF;
    public Long imgREF_AFTER_COMPLETED;
	public String topic;
	public String postingDate;
	public String responsibleAgency;
	public String address;
	public String userRole;
	public String username;
	public String status;
    public String userMessage;
	
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public ReportInfo() {
    }

    public ReportInfo(Long imgREF, Long imgREF_AFTER_COMPLETED, String topic, String postingDate, String responsibleAgency, String address,
                      String userRole, String username, String status, String userMessage) {

        this.imgREF = imgREF;
        this.imgREF_AFTER_COMPLETED = imgREF_AFTER_COMPLETED;
        this.topic = topic;
        this.postingDate = postingDate;
        this.responsibleAgency = responsibleAgency;
        this.address = address;
        this.userRole = userRole;
        this.username = username;
        this.status = status;
        this.userMessage = userMessage;

	}

}
