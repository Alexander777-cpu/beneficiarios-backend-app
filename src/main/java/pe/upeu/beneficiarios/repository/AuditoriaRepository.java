package pe.upeu.beneficiarios.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upeu.beneficiarios.entity.Auditoria;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    Page<Auditoria> findAllByOrderByIdDesc(Pageable pageable);
}
