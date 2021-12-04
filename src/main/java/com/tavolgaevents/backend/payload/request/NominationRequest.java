package com.tavolgaevents.backend.payload.request;

import java.util.List;

public class NominationRequest {
    public String id;
    public String name;
    public String description;
    public String contestId;
    public List<String> criteriaList;
}
