package ru.practicum.ewm.requet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.events.model.State;
import ru.practicum.ewm.request.interfaces.RequestRepository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.Status;
import ru.practicum.ewm.users.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RequestRepositoryTests {
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    TestEntityManager testEntityManager;
    private Category category;
    private List<User> users = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Request> requests = new ArrayList<>();

    @BeforeEach
    void init() {

        category = new Category();
        category.setName("Category");
        category = testEntityManager.persist(category);

        users.add(createUser("Alex", "Alex@Alex.ru"));
        users.add(createUser("Ivan", "Ivan@Ivan.ru"));
        users.add(createUser("Grigory", "Grigory@Grigory.ru"));

        events.add(createDefaultEvent("First", "Хочу спать", State.PENDING));
        events.add(createDefaultEvent("Second", "Хочу есть", State.PENDING));
        events.add(createDefaultEvent("3", "Хочу есть", State.PENDING));

        requests.add(createDefaultRequest(events.get(0), Status.PENDING, users.get(0)));
        requests.add(createDefaultRequest(events.get(0), Status.CONFIRMED, users.get(1)));
        requests.add(createDefaultRequest(events.get(0), Status.PENDING, users.get(2)));

        requests.add(createDefaultRequest(events.get(1), Status.CONFIRMED, users.get(0)));
        requests.add(createDefaultRequest(events.get(1), Status.CANCELED, users.get(1)));

        requests.add(createDefaultRequest(events.get(2), Status.CANCELED, users.get(0)));
        requests.add(createDefaultRequest(events.get(2), Status.CONFIRMED, users.get(1)));
        requests.add(createDefaultRequest(events.get(2), Status.CONFIRMED, users.get(2)));

        testEntityManager.flush();
    }

    @Test
    void getCountRequestTest() throws Exception {

        List<Long> ids = List.of(events.get(0).getId(), events.get(1).getId(), events.get(2).getId());
        List<List<Long>> count = requestRepository.getCountRequest(ids, Status.CONFIRMED);

        assertEquals(1, count.get(0).get(0), "Wrong event id");
        assertEquals(1, count.get(0).get(1), "Wrong count requests");

        assertEquals(2, count.get(1).get(0), "Wrong event id");
        assertEquals(1, count.get(1).get(1), "Wrong count requests");

        assertEquals(3, count.get(2).get(0), "Wrong event id");
        assertEquals(2, count.get(2).get(1), "Wrong count requests");
    }

    private Event createDefaultEvent(String title, String annotation, State state) {

        Event event = new Event();
        event.setTitle(title);
        event.setAnnotation(annotation);
        event.setDescription(title.concat(annotation));
        event.setEventDate(LocalDateTime.now());
        event.setLat(30.0f);
        event.setLon(45.0f);
        event.setParticipantLimit(150);
        event.setPaid(true);
        event.setRequestModeration(false);
        event.setState(state);
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(category);
        event.setInitiator(users.get(0));

        return testEntityManager.persist(event);
    }

    private Request createDefaultRequest(Event event, Status status, User user) {

        Request request = new Request();
        request.setEvent(event.getId());
        request.setRequester(user.getId());
        request.setCreated(LocalDateTime.now());
        request.setStatus(status);

        return testEntityManager.persist(request);
    }

    private User createUser(String name, String email) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        return testEntityManager.persist(user);
    }
}
