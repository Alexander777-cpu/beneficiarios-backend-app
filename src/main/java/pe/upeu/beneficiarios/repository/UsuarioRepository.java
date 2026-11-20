package pe.upeu.beneficiarios.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upeu.beneficiarios.entity.security.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @EntityGraph(attributePaths = {"roles", "roles.privilegios", "facultad", "proyecto"})
    Optional<Usuario> findByUsernameIgnoreCase(String username);

    @Override
    @EntityGraph(attributePaths = {"roles", "roles.privilegios", "facultad", "proyecto"})
    Page<Usuario> findAll(Pageable pageable);

    boolean existsByUsernameIgnoreCase(String username);
    long countByFacultadId(Long facultadId);
    long countByProyectoId(Long proyectoId);
}
