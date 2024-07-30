/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * 
 * import java.util.*;
 *
 */

// Write your code here
package com.example.eventmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.eventmanagementsystem.model.*;
import com.example.eventmanagementsystem.repository.*;

@Service
public class SponsorJpaService implements SponsorRepository {
    @Autowired
    private EventJpaRepository eventJpaRepository;
    @Autowired
    private SponsorJpaRepository sponsorJpaRepository;

    @Override
    public ArrayList<Sponsor> getSponsors() {
        List<Sponsor> sponsorList = sponsorJpaRepository.findAll();
        ArrayList<Sponsor> sponsors = new ArrayList<>(sponsorList);
        return sponsors;
    }

    @Override
    public Sponsor getSponsorById(int sponsorId) {
        try {
            Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
            return sponsor;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Sponsor addSponsor(Sponsor sponsor) {
        List<Integer> eventIds = new ArrayList<>();
        for (Event event : sponsor.getEvents()) {
            eventIds.add(event.getEventId());
        }

        List<Event> events = eventJpaRepository.findAllById(eventIds);
        if (events.size() != eventIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        sponsor.setEvents(events);

        for (Event event : events) {
            event.getSponsors().add(sponsor);
        }
        Sponsor savedSponsor = sponsorJpaRepository.save(sponsor);
        eventJpaRepository.saveAll(events);
        return savedSponsor;
    }

    @Override
    public Sponsor updateSponsor(int sponsorId, Sponsor sponsor) {
        try {
            Sponsor newSponsor = sponsorJpaRepository.findById(sponsorId).get();
            if (sponsor.getSponsorName() != null)
                newSponsor.setSponsorName(sponsor.getSponsorName());
            if (sponsor.getIndustry() != null)
                newSponsor.setIndustry(sponsor.getIndustry());
            if (sponsor.getEvents() != null) {
                List<Integer> newEventIds = new ArrayList<>();
                for (Event event : sponsor.getEvents()) {
                    newEventIds.add(event.getEventId());
                }
                List<Event> newEvents = eventJpaRepository.findAllById(newEventIds);

                if (newEventIds.size() != newEvents.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }

                List<Event> events = newSponsor.getEvents();
                for (Event event : events) {
                    event.getSponsors().remove(newSponsor);
                }
                eventJpaRepository.saveAll(events);

                for (Event event : newEvents) {
                    event.getSponsors().add(newSponsor);
                }
                eventJpaRepository.saveAll(newEvents);
                newSponsor.setEvents(newEvents);

            }
            return newSponsor;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteSponsor(int sponsorId) {
        try {
            Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
            List<Event> events = sponsor.getEvents();
            for (Event event : events) {
                event.getSponsors().remove(sponsor);
            }
            eventJpaRepository.saveAll(events);
            sponsorJpaRepository.deleteById(sponsorId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Event> getSponsorEvents(int sponsorId) {
        try {
            Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
            return sponsor.getEvents();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}