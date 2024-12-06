package org.phoegasus.springai.part8;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;

  public Event(String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    this.title = title;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
  }
}
