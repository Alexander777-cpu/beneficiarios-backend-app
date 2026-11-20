package pe.upeu.beneficiarios.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.upeu.beneficiarios.dto.BeneficiarioCertificadoDTO;
import pe.upeu.beneficiarios.dto.GuardarAsistenciaRequestDTO;
import pe.upeu.beneficiarios.entity.*;
import pe.upeu.beneficiarios.exception.ReglaNegocioException;
import pe.upeu.beneficiarios.mapper.BeneficiarioMapper;
import pe.upeu.beneficiarios.repository.AsistenciaRepository;
import pe.upeu.beneficiarios.repository.BeneficiarioRepository;
import pe.upeu.beneficiarios.repository.ProyectoTallerRepository;
import pe.upeu.beneficiarios.service.impl.AsistenciaServiceImpl;
import pe.upeu.beneficiarios.service.impl.CertificadoServiceImpl;
import pe.upeu.beneficiarios.service.services.AuditoriaService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaYCertificadoServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;
    @Mock
    private BeneficiarioRepository beneficiarioRepository;
    @Mock
    private ProyectoTallerRepository proyectoTallerRepository;
    @Mock
    private AuditoriaService auditoriaService;

    @Spy
    private BeneficiarioMapper mapper = new BeneficiarioMapper();

    @InjectMocks
    private AsistenciaServiceImpl asistenciaService;

    @InjectMocks
    private CertificadoServiceImpl certificadoService;

    @Test
    void guardarAsistenciasEnLoteExitoso() {
        Facultad f = Facultad.builder().id(1L).nombre("FIA").build();
        Proyecto p = Proyecto.builder().id(1L).nombre("Proy 1").facultad(f).build();
        Taller t = Taller.builder().id(1L).nombre("Taller 1").build();
        ProyectoTaller pt = ProyectoTaller.builder().id(1L).proyecto(p).taller(t).build();
        Beneficiario b = Beneficiario.builder().id(1L).nombres("Juan").proyectoTaller(pt).build();

        GuardarAsistenciaRequestDTO req = GuardarAsistenciaRequestDTO.builder()
                .proyectoTallerId(1L)
                .fecha("2026-11-17")
                .detalles(List.of(new GuardarAsistenciaRequestDTO.DetalleAsistenciaDTO(1L, true)))
                .build();

        when(proyectoTallerRepository.findById(1L)).thenReturn(Optional.of(pt));
        when(asistenciaRepository.findByProyectoTallerIdAndFecha(1L, "2026-11-17")).thenReturn(List.of());
        when(beneficiarioRepository.findAllById(List.of(1L))).thenReturn(List.of(b));

        asistenciaService.guardar(req);

        verify(asistenciaRepository).saveAll(anyList());
        verify(auditoriaService).registrar(eq("REGISTRO"), eq("ASISTENCIA"), anyString());
    }

    @Test
    void emitirCertificadoConMenosDe5AsistenciasFalla() {
        Beneficiario b = Beneficiario.builder().id(2L).nombres("Valeria").build();
        when(beneficiarioRepository.findById(2L)).thenReturn(Optional.of(b));
        when(beneficiarioRepository.contarAsistenciasValidas(2L)).thenReturn(2L);

        assertThrows(ReglaNegocioException.class, () -> certificadoService.validarAptoCertificado(2L));
    }

    @Test
    void emitirCertificadoCon5AsistenciasEsApto() {
        Facultad f = Facultad.builder().id(1L).nombre("FIA").build();
        Proyecto p = Proyecto.builder().id(1L).nombre("Proy 1").facultad(f).build();
        Taller t = Taller.builder().id(1L).nombre("Taller 1").build();
        ProyectoTaller pt = ProyectoTaller.builder().id(1L).proyecto(p).taller(t).build();
        Beneficiario b = Beneficiario.builder()
                .id(1L).nombres("Mateo").apellidoPaterno("Quispe").apellidoMaterno("Huaman").proyectoTaller(pt).build();

        when(beneficiarioRepository.findById(1L)).thenReturn(Optional.of(b));
        when(beneficiarioRepository.contarAsistenciasValidas(1L)).thenReturn(5L);

        BeneficiarioCertificadoDTO res = certificadoService.validarAptoCertificado(1L);
        assertTrue(res.isAptoCertificado());
        assertEquals(5L, res.getTotalAsistencias());
    }
}
