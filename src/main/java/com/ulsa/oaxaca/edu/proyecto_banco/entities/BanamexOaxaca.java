package com.ulsa.oaxaca.edu.proyecto_banco.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banamex_oaxaca")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BanamexOaxaca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "direccion_oficina_central")
    private String direccionOficinaCentral;

    @Column(name = "numero_sucursales")
    private Integer numeroSucursales;

    private String region;

    @OneToMany(mappedBy = "banco", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sucursal> listaSucursales;

    private String telefono;

    @Column(name = "total_clientes")
    private Integer totalClientes;

    @Column(name = "numero_empleados")
    private Integer numeroEmpleados;

    @Column(name = "horario_atencion")
    private String horarioAtencion;
}
