package ru.practicum.ewm.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilations.dto.CompilationDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationsController {

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam Boolean pinned,
                                                @RequestParam(defaultValue = "0") @Min(0)Long from,
                                                @RequestParam(defaultValue = "10") @Positive Long size) {

        log.info("Call 'getCompilations': {}, {}, {}", pinned, from, size);

        return null;
    }

    @GetMapping(value = "/{compId}")
    public CompilationDto getCompilation(@PathVariable @Positive Long compId) {

        log.info("Call 'getCompilation': {}", compId);

        return null;
    }


}
