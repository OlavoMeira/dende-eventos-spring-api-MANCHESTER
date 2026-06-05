package br.com.dendesofthouse.dendeeventos.repository;

import br.com.dendesofthouse.dendeeventos.entity.Evento;
import br.com.dendesofthouse.dendeeventos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByOrganizador(Usuario organizador);

    List<Evento> findByOrganizadorId(Long organizadorId);

    List<Evento> findByAtivoTrue();

    List<Evento> findByDataFimAfter(LocalDateTime data);

    @Query("SELECT e FROM Evento e WHERE e.ativo = true AND e.dataFim > CURRENT_TIMESTAMP " +
            "AND (e.capacidadeMaxima - SIZE(e.ingressos)) > 0 ORDER BY e.dataInicio, e.nome")
    List<Evento> findEventosAtivosComVagas();

    @Query("SELECT e FROM Evento e WHERE e.organizador.id = :organizadorId AND e.ativo = true AND e.dataFim > CURRENT_TIMESTAMP")
    List<Evento> findEventosAtivosByOrganizadorId(@Param("organizadorId") Long organizadorId);

    @Modifying
    @Transactional
    @Query("UPDATE Evento e SET e.ativo = :ativo WHERE e.id = :eventoId")
    void updateAtivoStatus(@Param("eventoId") Long eventoId, @Param("ativo") Boolean ativo);

    boolean existsByOrganizadorIdAndAtivoTrueAndDataFimAfter(Long organizadorId, LocalDateTime now);
}
