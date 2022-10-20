package ru.practicum.server.event.repositories;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

List<Event> findEventsByDescriptionContainingOrAnnotationContainingAndCategory_CategoryIdAndPaid(String text,String text2,List<Integer> categoriesIds,Boolean paid);
}