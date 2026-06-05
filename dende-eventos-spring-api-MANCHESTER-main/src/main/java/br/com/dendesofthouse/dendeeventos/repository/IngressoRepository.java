package br.com.dendesofthouse.dendeeventos.repository;

import br.com.dendesofthouse.dendeeventos.entity.Ingresso;
import br.com.dendesofthouse.dendeeventos.entity.Usuario;
import br.com.dendesofthouse.dendeeventos.enums.StatusIngresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Long> {

    List<Ingresso> findByUsuario(Usuario usuario);

    List<Ingresso> findByUsuarioId(Long usuarioId);

    List<Ingresso> findByEventoId(Long eventoId);

    List<Ingresso> findByEventoIdAndStatus(Long eventoId, StatusIngresso status);

    @Query("SELECT i FROM Ingresso i WHERE i.usuario.id = :usuarioId " +
            "ORDER BY CASE WHEN i.status = 'ATIVO' AND i.evento.ativo = true AND i.evento.dataFim > CURRENT_TIMESTAMP " +
            "THEN 0 ELSE 1 END, i.evento.dataInicio, i.evento.nome")
    List<Ingresso> findIngressosOrdenadosPorUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COUNT(i) FROM Ingresso i WHERE i.evento.id = :eventoId AND i.status = 'ATIVO'")
    int countIngressosAtivosByEventoId(@Param("eventoId") Long eventoId);

    @Modifying
    @Transactional
    @Query("UPDATE Ingresso i SET i.status = 'CANCELADO', i.dataCancelamento = CURRENT_TIMESTAMP, " +
            "i.valorReembolsado = i.valorPago * (1 - :taxaCancelamento) " +
            "WHERE i.evento.id = :eventoId AND i.status = 'ATIVO'")
    void cancelarIngressosDoEvento(@Param("eventoId") Long eventoId,
                                   @Param("taxaCancelamento") Double taxaCancelamento);
}
