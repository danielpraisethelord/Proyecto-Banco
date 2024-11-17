package com.ulsa.oaxaca.edu.proyecto_banco.utils;

import com.ulsa.oaxaca.edu.proyecto_banco.dto.ClienteDto;
import com.ulsa.oaxaca.edu.proyecto_banco.dto.CuentaDto;
import com.ulsa.oaxaca.edu.proyecto_banco.dto.TarjetaDto;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cliente;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Cuenta;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Tarjeta;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteMapper {

        public static ClienteDto toDto(Cliente cliente) {
                if (cliente.getCuentas() == null) {
                        return new ClienteDto(
                                        cliente.getId(),
                                        cliente.getNombre(),
                                        cliente.getApellidoPaterno(),
                                        cliente.getApellidoMaterno(),
                                        cliente.getGenero(),
                                        cliente.getFechaNacimiento(),
                                        cliente.getRfc(),
                                        cliente.getEmail(),
                                        cliente.getTelefono(),
                                        cliente.getNivelDeEstudios(),
                                        cliente.getFechaRegistro(),
                                        cliente.getEstado(),
                                        cliente.getSucursal().getId(),
                                        Collections.emptyList());
                }

                List<CuentaDto> cuentasDto = cliente.getCuentas().stream()
                                .map(ClienteMapper::toCuentaDto)
                                .collect(Collectors.toList());

                return new ClienteDto(
                                cliente.getId(),
                                cliente.getNombre(),
                                cliente.getApellidoPaterno(),
                                cliente.getApellidoMaterno(),
                                cliente.getGenero(),
                                cliente.getFechaNacimiento(),
                                cliente.getRfc(),
                                cliente.getEmail(),
                                cliente.getTelefono(),
                                cliente.getNivelDeEstudios(),
                                cliente.getFechaRegistro(),
                                cliente.getEstado(),
                                cliente.getSucursal().getId(),
                                cuentasDto);
        }

        private static CuentaDto toCuentaDto(Cuenta cuenta) {
                List<TarjetaDto> tarjetasDto = cuenta.getTarjetas() != null
                                ? cuenta.getTarjetas().stream()
                                                .map(ClienteMapper::toTarjetaDto)
                                                .collect(Collectors.toList())
                                : Collections.emptyList();

                return new CuentaDto(
                                cuenta.getId(),
                                cuenta.getFechaApertura(),
                                cuenta.getSaldoActual(),
                                cuenta.getTipoCuenta(),
                                cuenta.getMoneda(),
                                tarjetasDto);
        }

        private static TarjetaDto toTarjetaDto(Tarjeta tarjeta) {
                return new TarjetaDto(
                                tarjeta.getId(),
                                tarjeta.getNumeroTarjeta(),
                                tarjeta.getTipo(),
                                tarjeta.getEstado(),
                                tarjeta.getFechaExpiracion(),
                                tarjeta.getLimiteCredito(),
                                tarjeta.getSaldoActual());
        }
}