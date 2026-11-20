package pe.upeu.beneficiarios.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.upeu.beneficiarios.dto.BeneficiarioRequestDTO;
import pe.upeu.beneficiarios.dto.BeneficiarioResponseDTO;
import pe.upeu.beneficiarios.entity.*;
import pe.upeu.beneficiarios.exception.RecursoNoEncontradoException;
import pe.upeu.beneficiarios.exception.ReglaNegocioException;
import pe.upeu.beneficiarios.exception.ValidacionException;
import pe.upeu.beneficiarios.mapper.BeneficiarioMapper;
import pe.upeu.beneficiarios.repository.AsistenciaRepository;
import pe.upeu.beneficiarios.repository.BeneficiarioRepository;
import pe.upeu.beneficiarios.repository.ProyectoTallerRepository;
import pe.upeu.beneficiarios.service.impl.BeneficiarioServiceImpl;
import pe.upeu.beneficiarios.service.services.AuditoriaService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficiarioServiceImplTest {

    @Mock
    private BeneficiarioRepository beneficiarioRepository;
    @Mock
    private ProyectoTallerRepository proyectoTallerRepository;
    @Mock
    private AsistenciaRepository asistenciaRepository;
    @Mock
    private AuditoriaService auditoriaService;

    @Spy
    private BeneficiarioMapper mapper = new BeneficiarioMapper();

    @InjectMocks
    private BeneficiarioServiceImpl beneficiarioService;

    private ProyectoTaller proyectoTaller;

    @BeforeEach
    void setUp() {
        Facultad f = Facultad.builder().id(1L).nombre("FIA").build();
        Proyecto p = Proyecto.builder().id(1L).nombre("Alfabetizacion").facultad(f).build();
        Taller t = Taller.builder().id(1L).nombre("Ofimatica").build();
        proyectoTaller = ProyectoTaller.builder().id(1L).proyecto(p).taller(t).build();
    }

    @Test
    void crearBeneficiarioExitoso() {
        BeneficiarioRequestDTO req = BeneficiarioRequestDTO.builder()
                .tipoBeneficiario("NINO")
                .nombres("Lucas Daniel")
                .apellidoPaterno("Perez")
                .apellidoMaterno("Gomez")
                .telefono("987654321")
                .edad(9)
                .proyectoTallerId(1L)
                .build();

        when(beneficiarioRepository.existsByNombresIgnoreCase("Lucas Daniel")).thenReturn(false);
        when(proyectoTallerRepository.findById(1L)).thenReturn(Optional.of(proyectoTaller));
        when(beneficiarioRepository.save(any(Beneficiario.class))).thenAnswer(inv -> {
            Beneficiario b = inv.getArgument(0);
            b.setId(10L);
            return b;
        });

        BeneficiarioResponseDTO res = beneficiarioService.crear(req);

        assertNotNull(res);
        assertEquals(10L, res.getId());
        assertEquals("Lucas Daniel", res.getNombres());
        assertEquals("FIA", res.getFacultadNombre());
        verify(auditoriaService).registrar(eq("CREO"), eq("BENEFICIARIO"), anyString());
    }

    @Test
    void crearBeneficiarioDuplicadoLanzaValidacionException() {
        BeneficiarioRequestDTO req = BeneficiarioRequestDTO.builder()
                .tipoBeneficiario("NINO")
                .nombres("Mateo Gabriel")
                .apellidoPaterno("Quispe")
                .apellidoMaterno("Huaman")
                .proyectoTallerId(1L)
                .build();

        when(beneficiarioRepository.existsByNombresIgnoreCase("Mateo Gabriel")).thenReturn(true);

        ValidacionException ex = assertThrows(ValidacionException.class, () -> beneficiarioService.crear(req));
        assertEquals("nombres", ex.getCampo());
        verify(beneficiarioRepository, never()).save(any());
    }

    @Test
    void eliminarBeneficiarioConAsistenciasBloqueado() {
        Beneficiario b = Beneficiario.builder().id(1L).nombres("Mateo").proyectoTaller(proyectoTaller).build();
        when(beneficiarioRepository.findById(1L)).thenReturn(Optional.of(b));
        when(asistenciaRepository.countByBeneficiarioId(1L)).thenReturn(3L);

        assertThrows(ReglaNegocioException.class, () -> beneficiarioService.eliminar(1L));
        verify(beneficiarioRepository, never()).delete(any());
    }

    @Test
    void buscarBeneficiarioInexistenteLanzaNotFound() {
        when(beneficiarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> beneficiarioService.buscar(99L));
    }
}
