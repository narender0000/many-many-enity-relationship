/*
 * You can use the following import statements
 *
 * import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 * 
 * import javax.persistence.*;
 * import java.util.List;
 * 
 */

// Write your code here

package com.example.eventmanagementsystem.model;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.example.eventmanagementsystem.model.Event;

@Entity
@Table(name = "sponsor")
public class Sponsor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int sponsorId;

    @Column(name = "name")
    private String sponsorName;

    @Column(name = "industry")
    private String industry;

    @ManyToMany(mappedBy = "sponsors")
    @JsonIgnoreProperties("sponsors")
    private List<Event> events = new ArrayList<>();

    public Sponsor() {
    }

    public int getSponsorId() {
        return sponsorId;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public String getIndustry() {
        return industry;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}