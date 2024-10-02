package com.ulsa.oaxaca.edu.proyecto_banco.entities;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "personal")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Personal extends Persona {

    @PastOrPresent(message = "La fecha de contratación debe ser en el pasado o presente")
    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    @NotBlank(message = "El turno es obligatorio")
    private String turno;

    @Min(value = 0, message = "Los años de experiencia no pueden ser negativos")
    @Column(name = "anios_de_experiencia")
    private Integer aniosDeExperiencia;

    @Min(value = 0, message = "Las horas de trabajo no pueden ser negativas")
    @Column(name = "horas_de_trabajo")
    private Integer horasDeTrabajo;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    @JsonIgnoreProperties({ "nombre", "direccion", "telefono", "email", "horarioAtencion", "fechaApertura", "estado",
            "banco", "personal", "clientes" })
    private Sucursal sucursal;

    @NotBlank(message = "Las responsabilidades son obligatorias")
    private String responsabilidades;

    @NotNull(message = "El sueldo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El sueldo debe ser mayor que 0")
    private Double sueldo;

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaccion> transacciones;
}
