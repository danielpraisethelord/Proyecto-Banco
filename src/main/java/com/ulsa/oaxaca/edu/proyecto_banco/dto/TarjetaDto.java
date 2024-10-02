package com.ulsa.oaxaca.edu.proyecto_banco.dto;

import java.time.LocalDate;

public record TarjetaDto(
                Long id,
                String numeroTarjeta,
                String tipoTarjeta,
                String estado,
                LocalDate fechaExpiracion,
                Double limiteCredito,
                Double saldoActual) {
}
