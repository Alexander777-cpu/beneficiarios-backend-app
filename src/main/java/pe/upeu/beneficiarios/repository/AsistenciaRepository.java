package pe.upeu.beneficiarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.upeu.beneficiarios.entity.Asistencia;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByProyectoTallerIdAndFecha(Long proyectoTallerId, String fecha);

    long countByBeneficiarioId(Long beneficiarioId);

    @Query("SELECT a.beneficiario.id, COUNT(a) FROM Asistencia a WHERE a.asistio = true AND a.beneficiario.id IN :beneficiarioIds GROUP BY a.beneficiario.id")
    List<Object[]> countTotalAsistenciasPorBeneficiarioIds(@Param("beneficiarioIds") List<Long> beneficiarioIds);
}
