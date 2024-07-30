/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.web.bind.annotation.*;
 * 
 * import java.util.ArrayList;
 * import java.util.List;
 * 
 */

// Write your code here
package com.example.eventmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import com.example.eventmanagementsystem.model.*;
import com.example.eventmanagementsystem.service.SponsorJpaService;

@RestController
public class SponsorController {
    @Autowired
    public SponsorJpaService sponsorService;

    @GetMapping("/events/sponsors")
    public ArrayList<Sponsor> getSponsors() {
        return sponsorService.getSponsors();
    }

    @GetMapping("/events/sponsors/{sponsorId}")
    public Sponsor getSponsorById(@PathVariable("sponsorId") int sponsorId) {
        return sponsorService.getSponsorById(sponsorId);
    }

    @PostMapping("/events/sponsors")
    public Sponsor addSponsor(@RequestBody Sponsor sponsor) {
        return sponsorService.addSponsor(sponsor);
    }

    @PutMapping("/events/sponsors/{sponsorId}")
    public Sponsor updateSponsor(@PathVariable("sponsorId") int sponsorId, @RequestBody Sponsor sponsor) {
        return sponsorService.updateSponsor(sponsorId, sponsor);
    }

    @DeleteMapping("/events/sponsors/{sponsorId}")
    public void deleteSponsor(@PathVariable("sponsorId") int sponsorId) {
        sponsorService.deleteSponsor(sponsorId);
    }

    @GetMapping("/sponsors/{sponsorId}/events")
    public List<Event> getSponsorEvents(@PathVariable("sponsorId") int sponsorId) {
        return sponsorService.getSponsorEvents(sponsorId);
    }
}