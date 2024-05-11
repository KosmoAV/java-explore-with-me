package ru.practicum.ewm.compilations.interfaces;

import ru.practicum.ewm.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.dto.CompilationDto;
import ru.practicum.ewm.compilations.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    public void deleteCompilation(Long compId);

    public CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId);

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    public CompilationDto getCompilation(Long compId);
}
