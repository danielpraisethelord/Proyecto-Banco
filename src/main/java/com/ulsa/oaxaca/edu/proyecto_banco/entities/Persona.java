package com.ulsa.oaxaca.edu.proyecto_banco.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El genero es obligatorio")
    private String genero;

    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos")
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    private String email;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotNull(message = "El RFC es obligatorio")
    @Pattern(regexp = "[A-Z]{4}\\d{6}[A-Z0-9]{3}", message = "El RFC debe tener un formato válido")
    private String rfc;

    private Boolean discapacidad;

    @NotBlank(message = "El estado civil es obligatorio")
    @Column(name = "estado_civil")
    private String estadoCivil;

    @NotBlank(message = "El nivel de estudios es obligatorio")
    @Column(name = "nivel_de_estudios")
    private String nivelDeEstudios;
}
