package ru.practicum.server.participationRequest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.participationRequest.models.ParticipationRequest;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {
    @Query("select p from ParticipationRequest p where p.user = ?1")
    List<ParticipationRequest> getAllByUser(Long userId);
}