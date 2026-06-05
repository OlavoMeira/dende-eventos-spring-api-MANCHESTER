package br.com.dendesofthouse.dendeeventos.repository;

import br.com.dendesofthouse.dendeeventos.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByCnpj(String cnpj);

    Optional<Empresa> findByOrganizadorId(Long organizadorId);

    boolean existsByCnpj(String cnpj);

    boolean existsByOrganizadorId(Long organizadorId);
}
