package com.ulsa.oaxaca.edu.proyecto_banco.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ejecutivo")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Ejecutivo extends Personal {

    @NotBlank(message = "El estilo de clientes asignados es obligatorio")
    @Column(name = "estilo_de_clientes_asignados")
    private String estiloDeClientesAsignados;

    @NotBlank(message = "Los objetivos de ventas son obligatorios")
    @Column(name = "objetivos_de_ventas")
    private String objetivosDeVentas;

    @NotBlank(message = "El departamento es obligatorio")
    private String departamento;
}
