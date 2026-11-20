package pe.upeu.beneficiarios.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pe.upeu.beneficiarios.exception.ReglaNegocioException;

import java.util.Set;

public final class PaginacionUtil {

    public static final int TAMANIO_MAXIMO = 100;

    private PaginacionUtil() {
    }

    public static Pageable construir(
            int pagina,
            int tamanio,
            String ordenarPor,
            String direccion,
            Set<String> camposOrdenables,
            String ordenPorDefecto) {

        if (pagina < 0) {
            throw new ReglaNegocioException("La pagina no puede ser negativa");
        }

        if (tamanio < 1 || tamanio > TAMANIO_MAXIMO) {
            throw new ReglaNegocioException("El tamano de pagina debe estar entre 1 y " + TAMANIO_MAXIMO);
        }

        return PageRequest.of(
                pagina,
                tamanio,
                construirSort(ordenarPor, direccion, camposOrdenables, ordenPorDefecto)
        );
    }

    public static Sort construirSort(
            String ordenarPor,
            String direccion,
            Set<String> camposOrdenables,
            String ordenPorDefecto) {

        String campo = (ordenarPor == null || ordenarPor.isBlank())
                ? ordenPorDefecto
                : ordenarPor.trim();

        if (!camposOrdenables.contains(campo)) {
            throw new ReglaNegocioException("El campo de ordenamiento '" + campo + "' no esta permitido. Campos validos: " + camposOrdenables);
        }

        String sentido = (direccion == null || direccion.isBlank())
                ? "desc"
                : direccion.trim();

        if (!sentido.equalsIgnoreCase("asc") && !sentido.equalsIgnoreCase("desc")) {
            throw new ReglaNegocioException("La direccion de ordenamiento '" + sentido + "' no esta permitida. Valores validos: asc, desc");
        }

        return sentido.equalsIgnoreCase("asc")
                ? Sort.by(campo).ascending()
                : Sort.by(campo).descending();
    }
}
