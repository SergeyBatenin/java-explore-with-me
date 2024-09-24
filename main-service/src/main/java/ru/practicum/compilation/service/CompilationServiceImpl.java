package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.dto.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());

        if (events.size() != newCompilationDto.getEvents().size()) {
            throw new IllegalArgumentException("Подборка содержит несуществующие события");
        }

        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);

        return compilationMapper.toDto(compilation, eventMapper.toShortDto(compilation.getEvents()));
    }

    @Transactional
    @Override
    public CompilationDto update(long compId, UpdateCompilationRequest updateCompilationDto) {
        Compilation compilation = compilationRepository.checkAndGetCompilation(compId);

        if (updateCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateCompilationDto.getEvents());
            if (events.size() != updateCompilationDto.getEvents().size()) {
                throw new IllegalArgumentException("Подборка содержит несуществующие события");
            }
            compilation.setEvents(events);
        }
        if (updateCompilationDto.getTitle() != null && !updateCompilationDto.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }

        return compilationMapper.toDto(compilation, eventMapper.toShortDto(compilation.getEvents()));
    }

    @Transactional
    @Override
    public void delete(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, page);

        return compilations.stream()
                .map(compilation -> compilationMapper.toDto(
                        compilation,
                        eventMapper.toShortDto(compilation.getEvents())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getById(long compId) {
        Compilation compilation = compilationRepository.checkAndGetCompilation(compId);
        return compilationMapper.toDto(compilation, eventMapper.toShortDto(compilation.getEvents()));
    }
}
