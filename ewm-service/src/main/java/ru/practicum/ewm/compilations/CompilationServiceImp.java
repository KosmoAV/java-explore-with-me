package ru.practicum.ewm.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.admin.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.dto.CompilationDto;
import ru.practicum.ewm.compilations.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilations.interfaces.CompilationRepository;
import ru.practicum.ewm.compilations.interfaces.CompilationService;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.events.EventMapper;
import ru.practicum.ewm.events.dto.EventShortDto;
import ru.practicum.ewm.events.interfaces.EventRepository;
import ru.practicum.ewm.events.model.Event;
import ru.practicum.ewm.exception.DataNotFoundRequestException;
import ru.practicum.ewm.request.interfaces.RequestRepository;
import ru.practicum.ewm.request.model.Status;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImp implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {

        List<Event> events = new ArrayList<>();

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {

            events = eventRepository.findAllById(newCompilationDto.getEvents());

            if (newCompilationDto.getEvents().size() != events.size()) {
                throw new DataNotFoundRequestException("Not all events were found");
            }
        }

        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);

        compilation = compilationRepository.save(compilation);

        if (events.isEmpty()) {

            return CompilationMapper.toCompilationDto(compilation, new ArrayList<>());
        }

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);

        List<EventShortDto> eventShortDtoList = events.stream()
                .map(event -> EventMapper.toEventShortDto(event, confirmedRequests.get(event.getId())))
                .collect(Collectors.toList());

        return CompilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }

    @Override
    public void deleteCompilation(Long compId) {

        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundRequestException("Compilation with id = " + compId + " not found"));

        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(new ArrayList<>(updateCompilationRequest.getEvents()));
        }

        List<Event> events = new ArrayList<>();

        if (!compilation.getEvents().isEmpty()) {

            events = eventRepository.findAllById(compilation.getEvents());

            if (compilation.getEvents().size() != events.size()) {
                throw new DataNotFoundRequestException("Not all events were found");
            }
        }

        compilation = compilationRepository.save(compilation);

        if (events.isEmpty()) {

            return CompilationMapper.toCompilationDto(compilation, new ArrayList<>());
        }

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);

        List<EventShortDto> eventShortDtoList = events.stream()
                .map(event -> EventMapper.toEventShortDto(event, confirmedRequests.get(event.getId())))
                .collect(Collectors.toList());

        return CompilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        List<Compilation> compilations;

        if  (pinned != null) {
            compilations = compilationRepository.findByPinned(pinned, page).getContent();
        } else {
            compilations = compilationRepository.findAll(page).getContent();
        }

        Set<Long> eventsIds = compilations.stream()
                .flatMap(compilation -> compilation.getEvents().stream()).collect(Collectors.toSet());

        Map<Long, Event> eventMap = eventRepository.findAllById(eventsIds).stream()
                .collect(Collectors.toMap(Event::getId, event -> event));

        if (eventMap.values().size() != eventsIds.size()) {
            throw new DataNotFoundRequestException("Not all events were found");
        }

        Map<Long, Long> confirmedRequestsMap = getConfirmedRequests(eventMap.values());

        return compilations.stream()
            .map(compilation -> {
                List<EventShortDto> eventShortDtoList = compilation.getEvents().stream()
                    .map(eventMap::get)
                    .map(event -> EventMapper.toEventShortDto(event, confirmedRequestsMap.get(event.getId())))
                    .collect(Collectors.toList());
                return CompilationMapper.toCompilationDto(compilation, eventShortDtoList);})
            .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundRequestException("Compilation with id = " + compId + " not found"));

        List<Event> events = new ArrayList<>();

        if (!compilation.getEvents().isEmpty()) {
            events = eventRepository.findAllById(compilation.getEvents());

            if (compilation.getEvents().size() != events.size()) {
                throw new DataNotFoundRequestException("Not all events were found");
            }
        }

        Map<Long, Long> confirmedRequests = getConfirmedRequests(events);

        List<EventShortDto> eventShortDtoList = events.stream()
                .map(event -> EventMapper.toEventShortDto(event, confirmedRequests.get(event.getId())))
                .collect(Collectors.toList());

        return CompilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }

    private Map<Long, Long> getConfirmedRequests(Collection<Event> events) {

        List<Long> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Map<Long, Long> map = requestRepository.getCountRequest(ids, Status.CONFIRMED).stream()
                .collect(Collectors.toMap(list -> list.get(0), list -> list.get(1)));

        for (Long id : ids) {
            if (!map.containsKey(id)) {
                map.put(id, 0L);
            }
        }

        return map;
    }
}
