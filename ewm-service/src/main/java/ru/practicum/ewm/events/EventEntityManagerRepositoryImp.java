package ru.practicum.ewm.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.events.interfaces.EventEntityManagerRepository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.State;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class EventEntityManagerRepositoryImp implements EventEntityManagerRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    /*
    "SELECT e FROM Event AS e WHERE LOWER(e.annotation) LIKE CONCAT('%', :text, '%') " +
    "OR LOWER(e.description) LIKE CONCAT('%', :text, '%') AND e.category.id IN (:categories) " +
    "AND e.paid = :paid AND e.eventDate >= :rangeStart AND e.eventDate < :rangeEnd AND e.state = :state")
    */

    @Override
    @Transactional
    public Page<Event> findByFilterAndPublished(String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                PageRequest page) {

        Map<String, Object> parameters = new HashMap<>();

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e FROM Event AS e WHERE ");

        if (text != null) {
            builder.append("LOWER(e.annotation) LIKE CONCAT('%', :text, '%')" +
                    "OR LOWER(e.description) LIKE CONCAT('%', :text, '%')");

            parameters.put("text", text);
        }

        if (categories != null) {
            if (text != null) {
                builder.append(" AND ");
            }
            builder.append("e.category.id IN (:categories)");

            parameters.put("categories", categories);
        }

        if (paid != null) {
            if (text != null || categories != null) {
                builder.append(" AND ");
            }
            builder.append("e.paid = :paid");

            parameters.put("paid", paid);
        }

        if (text != null || categories != null || paid != null) {
            builder.append(" AND ");
        }

        builder.append("e.eventDate >= :rangeStart AND e.eventDate < :rangeEnd AND e.state = :state");

        parameters.put("rangeStart", rangeStart);
        parameters.put("rangeEnd", rangeEnd);
        parameters.put("state", State.PUBLISHED);

        System.out.println(builder.toString());
        System.out.println(parameters);

        TypedQuery<Event> query = entityManager.createQuery(builder.toString(), Event.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.setFirstResult(page.getPageNumber() * page.getPageSize());
        query.setMaxResults(page.getPageSize());

        List <Event> events = query.getResultList();

        long totalCount = events.size() < page.getPageSize() ? page.getPageSize() : entityManager
                .createQuery("SELECT COUNT(e) FROM Event AS e", Long.class)
                .getSingleResult();

        return new PageImpl<>(events, page, totalCount);
    }
}
