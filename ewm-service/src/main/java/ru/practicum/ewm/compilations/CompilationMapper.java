package ru.practicum.ewm.compilations;

import ru.practicum.ewm.admin.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.dto.CompilationDto;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.events.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {

        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());

        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(new ArrayList<>(newCompilationDto.getEvents()));
        } else {
            compilation.setEvents(new ArrayList<>());
        }

        return compilation;
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> eventShortDtoList) {

        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setEvents(eventShortDtoList);

        return compilationDto;
    }
}
