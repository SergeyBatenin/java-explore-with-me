package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.StatInfoDto;
import ru.practicum.ewm.model.ParamHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<ParamHit, Long> {
    @Query("SELECT new ru.practicum.ewm.dto.StatInfoDto(ph.app, ph.uri, COUNT(DISTINCT ph.ip)) " +
            "FROM ParamHit ph " +
            "WHERE ph.timestamp between :start AND :end " +
            "GROUP BY ph.app, ph.uri " +
            "ORDER BY COUNT(DISTINCT ph.ip) DESC")
    List<StatInfoDto> getAllWithUniqueIpAndWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.StatInfoDto(ph.app, ph.uri, COUNT(ph.ip)) " +
            "FROM ParamHit ph " +
            "WHERE ph.timestamp between :start AND :end " +
            "GROUP BY ph.app, ph.uri " +
            "ORDER BY COUNT(ph.ip) DESC")
    List<StatInfoDto> getAllNotUniqueIpAndWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.StatInfoDto(ph.app, ph.uri, COUNT(DISTINCT ph.ip)) " +
            "FROM ParamHit ph " +
            "WHERE ph.timestamp between :start AND :end AND ph.uri IN :uris " +
            "GROUP BY ph.app, ph.uri " +
            "ORDER BY COUNT(DISTINCT ph.ip) DESC")
    List<StatInfoDto> getAllWithUniqueIpAndWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.dto.StatInfoDto(ph.app, ph.uri, COUNT(ph.ip)) " +
            "FROM ParamHit ph " +
            "WHERE ph.timestamp between :start AND :end AND ph.uri IN :uris " +
            "GROUP BY ph.app, ph.uri " +
            "ORDER BY COUNT(ph.ip) DESC")
    List<StatInfoDto> getAllNotUniqueIpAndWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
