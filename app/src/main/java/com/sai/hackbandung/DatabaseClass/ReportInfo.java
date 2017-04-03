package com.sai.hackbandung.DatabaseClass;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by AlbertusK95 on 4/2/2017.
 */

@IgnoreExtraProperties
public class ReportInfo {

    public String REPORT_ID;
	public Long imgREF;
    public Long imgREF_AFTER_COMPLETED;
	public String topic;
	public String postingDate;
    public String finishDate;
	public String responsibleAgency;
	public String address;
	public String userRole;
	public String username;
    public String fullname;
	public String status;
    public String userMessage;
	
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public ReportInfo() {
    }

    public ReportInfo(String REPORT_ID, Long imgREF, Long imgREF_AFTER_COMPLETED, String topic, String postingDate, String finishDate, String responsibleAgency, String address,
                      String userRole, String username, String fullname, String status, String userMessage) {

        this.REPORT_ID = REPORT_ID;
        this.imgREF = imgREF;
        this.imgREF_AFTER_COMPLETED = imgREF_AFTER_COMPLETED;
        this.topic = topic;
        this.postingDate = postingDate;
        this.finishDate = finishDate;
        this.responsibleAgency = responsibleAgency;
        this.address = address;
        this.userRole = userRole;
        this.username = username;
        this.fullname = fullname;
        this.status = status;
        this.userMessage = userMessage;

	}

}
