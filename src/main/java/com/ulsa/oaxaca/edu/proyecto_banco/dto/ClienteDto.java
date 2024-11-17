package com.ulsa.oaxaca.edu.proyecto_banco.dto;

import java.time.LocalDate;
import java.util.List;

public record ClienteDto(
        Long id,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String genero,
        LocalDate fechaNacimiento,
        String rfc,
        String email,
        String telefono,
        String nivelDeEstudios,
        LocalDate fechaRegistro,
        String estado,
        Long idSucursal,
        List<CuentaDto> cuentas) {

}
