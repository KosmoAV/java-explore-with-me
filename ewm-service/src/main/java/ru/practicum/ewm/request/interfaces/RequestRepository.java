package ru.practicum.ewm.request.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.Status;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT COUNT(r) FROM Request AS r WHERE r.event = :eventId AND r.status = :status")
    public Optional<Long> getCountRequest(Long eventId, Status status);

    @Query("SELECT r.event, COUNT(r) FROM Request AS r WHERE r.event IN (:events)" +
            " AND r.status = :status GROUP BY r.event")
    public List<List<Long>> getCountRequest(List<Long> events, Status status);

    public List<Request> findByRequester(Long requesterId);

    public List<Request> findByEvent(Long eventId);

    @Query("SELECT r.id FROM Request AS r WHERE r.event = :eventId AND r.status = :status")
    public List<Long> getRequestId(Long eventId, Status status);
}
