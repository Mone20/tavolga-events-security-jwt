package com.tavolgaevents.backend.payload.request;

import java.sql.Date;

public class AssessmentRequest {
    public String assessorId;
    public String userId;
    public String nominationId;
    public String criterionId;
    public String contestId;
    public int assessment;
    public String comment;
    public Date approvalDateTime;
}
