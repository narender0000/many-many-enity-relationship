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
public class EventJpaService implements EventRepository {
    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Autowired
    private SponsorJpaRepository sponsorJpaRepository;

    @Override
    public ArrayList<Event> getEvents() {
        List<Event> eventsList = eventJpaRepository.findAll();
        ArrayList<Event> events = new ArrayList<>(eventsList);
        return events;
    }

    @Override
    public Event getEventById(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();
            return event;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Event addEvent(Event event) {
        try {
            List<Integer> sponsorIds = new ArrayList<>();
            for (Sponsor sponsor : event.getSponsors()) {
                sponsorIds.add(sponsor.getSponsorId());
            }
            List<Sponsor> sponsors = sponsorJpaRepository.findAllById(sponsorIds);
            if (sponsorIds.size() != sponsors.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            event.setSponsors(sponsors);
            return eventJpaRepository.save(event);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Event updateEvent(int eventId, Event event) {
        try {
            Event newEvent = eventJpaRepository.findById(eventId).get();
            if (event.getEventName() != null)
                newEvent.setEventName(event.getEventName());
            if (event.getDate() != null)
                newEvent.setDate(event.getDate());
            if (event.getSponsors() != null) {
                List<Integer> sponsorIds = new ArrayList<>();
                for (Sponsor sponsor : event.getSponsors()) {
                    sponsorIds.add(sponsor.getSponsorId());
                }
                List<Sponsor> sponsors = sponsorJpaRepository.findAllById(sponsorIds);
                if (sponsorIds.size() != sponsors.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                newEvent.setSponsors(sponsors);
            }
            eventJpaRepository.save(newEvent);
            return newEvent;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteEvent(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();
            List<Sponsor> Sponsors = event.getSponsors();
            for (Sponsor sponsor : Sponsors) {
                sponsor.getEvents().remove(event);
            }
            sponsorJpaRepository.saveAll(Sponsors);
            eventJpaRepository.deleteById(eventId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Sponsor> getEventSponsors(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();
            return event.getSponsors();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}