package org.phoegasus.springai.part8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.function.Function;

@Configuration
public class FunctionsConfig {
  @Bean
  @Description("Check if overlapping event exists")
  public Function<Event, Boolean> overlappingEventExists(EventService eventService) {
    return eventService::overlappingEventExists;
  }

  @Bean
  @Description("Create new event")
  public Function<Event, Boolean> createEvent(EventService eventService) {
    return eventService::addEvent;
  }

  @Bean
  @Description("Delete existing event")
  public Function<Event, Boolean> deleteEvent(EventService eventService) {
    return event -> eventService.deleteEvent(event.getId());
  }

  @Bean
  @Description("List all events")
  public Function<Void, List<Event>> listEvents(EventService eventService) {
    return (v) -> eventService.getAllEvents();
  }
}
