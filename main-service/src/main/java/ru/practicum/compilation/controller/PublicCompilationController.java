package ru.practicum.compilation.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.info("GET ALL /compilations request: pinned={}", pinned);
        List<CompilationDto> compilations = compilationService.getAll(pinned, from, size);
        log.info("GET ALL /compilations response: {} elements ", compilations.size());
        return compilations;
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable @Positive long compId) {
        log.info("GET  /compilations/{} request", compId);
        CompilationDto compilationDto = compilationService.getById(compId);
        log.info("GET /compilations/{} response: {} ", compId, compilationDto);
        return compilationDto;
    }
}
