package ru.practicum.compilation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST /admin/compilations request: {}", newCompilationDto);
        CompilationDto compilationDto = compilationService.create(newCompilationDto);
        log.info("POST /admin/compilations response: {}", compilationDto);
        return compilationDto;
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable @Positive long compId,
                                 @Valid @RequestBody UpdateCompilationRequest updateCompilationDto) {
        log.info("PATCH /admin/compilations/{} request: {}", compId, updateCompilationDto);
        CompilationDto compilationDto = compilationService.update(compId, updateCompilationDto);
        log.info("PATCH /admin/compilations/{} response: {}", compId, compilationDto);
        return compilationDto;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive long compId) {
        log.info("DELETE /admin/compilations/{} request", compId);
        compilationService.delete(compId);
        log.info("DELETE /admin/compilations/{} response: success ", compId);
    }
}
