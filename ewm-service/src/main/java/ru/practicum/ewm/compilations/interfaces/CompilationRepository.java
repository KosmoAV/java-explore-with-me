package ru.practicum.ewm.compilations.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    public Page<Compilation> findByPinned(Boolean pinned, PageRequest page);
}
