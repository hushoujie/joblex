package com.berkerol.joblex.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Blacklist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "candidate_id")
    private long candidateId;

    @Column(name = "recruiter_id")
    private long recruiterId;

    private String reason;

    public Blacklist() {
    }

    public Blacklist(long candidateId, long recruiterId, String reason) {
        this.candidateId = candidateId;
        this.recruiterId = recruiterId;
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(long candidateId) {
        this.candidateId = candidateId;
    }

    public long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(long recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
