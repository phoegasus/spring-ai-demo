package org.phoegasus.springai.part8;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;

  @Transactional
  public boolean addEvent(Event event) {
    if (overlappingEventExists(event)) {
      return false;
    }

    eventRepository.save(event);
    return true;
  }

  public boolean overlappingEventExists(Event event) {
    List<Event> overlappingEvents = eventRepository.findOverlappingEvents(event.getStartDateTime(), event.getEndDateTime());
    return !overlappingEvents.isEmpty();
  }

  public List<Event> getAllEvents() {
    return eventRepository.findAll();
  }

  public boolean deleteEvent(Long eventId) {
    if (eventRepository.existsById(eventId)) {
      eventRepository.deleteById(eventId);
      return true;
    }
    return false;
  }
}