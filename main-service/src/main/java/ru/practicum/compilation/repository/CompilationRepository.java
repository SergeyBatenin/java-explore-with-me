package ru.practicum.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.exception.DataNotFoundException;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("SELECT c FROM Compilation c WHERE (:pinned IS NULL OR c.pinned = :pinned)")
    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);

    default Compilation checkAndGetCompilation(long compilationId) {
        return this.findById(compilationId)
                .orElseThrow(() -> {
//                    log.debug("GET COMPILATION. Подборка с айди {} не найден.", compilationId);
                    return new DataNotFoundException("Подборка с id=" + compilationId + " не существует.");
                });
    }
}
