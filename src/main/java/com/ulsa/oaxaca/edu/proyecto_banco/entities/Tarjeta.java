package com.ulsa.oaxaca.edu.proyecto_banco.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tarjeta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    @JsonIgnoreProperties({ "fechaApertura", "tipoCuenta", "moneda", "saldoActual", "cliente", "tarjetas",
            "transacciones" })
    private Cuenta cuenta;

    @NotBlank(message = "El campo tipo no puede estar vacío")
    private String tipo;

    @NotNull(message = "El campo CVC no puede estar vacío")
    @Min(value = 100, message = "El CVC debe tener al menos 3 dígitos")
    @Max(value = 999, message = "El CVC no puede tener más de 3 dígitos")
    private Integer cvc;

    @Future(message = "La fecha de expiración debe ser en el futuro")
    @NotNull(message = "El campo fecha de expiración no puede estar vacío")
    @Column(name = "fecha_expiracion")
    private LocalDate fechaExpiracion;

    @NotBlank(message = "El campo número de tarjeta no puede estar vacío")
    @Pattern(regexp = "\\d{16}", message = "El número de tarjeta debe tener 16 dígitos")
    @Column(name = "numero_tarjeta")
    private String numeroTarjeta;

    @NotNull(message = "El campo límite de crédito no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El límite de crédito debe ser mayor que 0")
    @Column(name = "limite_credito")
    private Double limiteCredito;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "saldo_actual")
    private Double saldoActual;
}