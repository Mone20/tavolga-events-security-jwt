package com.tavolgaevents.backend.models;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Entity
@Table(name = "contest_parts")
public class ContestPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    public Long id;

    @JsonView(Views.Public.class)
    @NotBlank
    public Date startDate;

    public String accessRole;

    @JsonView(Views.Public.class)
    @NotBlank
    public Date endDate;

    @JsonView(Views.Private.class)
    @ManyToOne(fetch = FetchType.LAZY)
    Contest contest;

    @JsonView(Views.Public.class)
    @ManyToOne(fetch = FetchType.LAZY)
    ContestPartType contestPartType;

    public ContestPart() {
    }

    public String getAccessRole() {
        return accessRole;
    }

    public void setAccessRole(String accessRole) {
        this.accessRole = accessRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public ContestPartType getContestPartType() {
        return contestPartType;
    }

    public void setContestPartType(ContestPartType contestPartType) {
        this.contestPartType = contestPartType;
    }
}
