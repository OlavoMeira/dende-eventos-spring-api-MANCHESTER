package br.com.dendesofthouse.dendeeventos.repository;

import br.com.dendesofthouse.dendeeventos.entity.Usuario;
import br.com.dendesofthouse.dendeeventos.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByAtivoTrue();

    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);

    @Query("SELECT u FROM Usuario u WHERE u.ativo = :ativo AND u.tipoUsuario = :tipo")
    List<Usuario> findByAtivoAndTipoUsuario(@Param("ativo") Boolean ativo,
                                            @Param("tipo") TipoUsuario tipo);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.email = :email AND u.tipoUsuario = :tipo")
    boolean existsByEmailAndTipoUsuario(@Param("email") String email,
                                        @Param("tipo") TipoUsuario tipo);
}
