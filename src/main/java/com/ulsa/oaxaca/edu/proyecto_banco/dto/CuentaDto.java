package com.ulsa.oaxaca.edu.proyecto_banco.dto;

import java.time.LocalDate;
import java.util.List;

public record CuentaDto(
        Long id,
        LocalDate fechaApertura,
        Double saldoActual,
        String tipoCuenta,
        String moneda,
        List<TarjetaDto> tarjetas) {
}
