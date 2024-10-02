package com.ulsa.oaxaca.edu.proyecto_banco;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulsa.oaxaca.edu.proyecto_banco.controller.GerenteController;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Gerente;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Sucursal;
import com.ulsa.oaxaca.edu.proyecto_banco.service.GerenteService;

@WebMvcTest(GerenteController.class)
public class GerenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GerenteService gerenteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Gerente gerente;
    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        sucursal = new Sucursal();
        sucursal.setId(1L);
        gerente = new Gerente();
        gerente.setNombre("Juan Pablo");
        gerente.setApellidoPaterno("Sanchez");
        gerente.setApellidoMaterno("Garcia");
        gerente.setFechaNacimiento(LocalDate.of(1977, 5, 15));
        gerente.setGenero("Hombre");
        gerente.setTelefono("9518942309");
        gerente.setEmail("jpsg@correo.com");
        gerente.setDireccion("Calle inventada colonia inventada 789");
        gerente.setRfc("JPSG770515HK9");
        gerente.setDiscapacidad(false);
        gerente.setEstadoCivil("casado");
        gerente.setNivelDeEstudios("Maestria");
        gerente.setFechaContratacion(LocalDate.of(2022, 5, 1));
        gerente.setTurno("Mañana");
        gerente.setAniosDeExperiencia(5);
        gerente.setHorasDeTrabajo(40);
        gerente.setEstado("Activo");
        gerente.setSucursal(sucursal);
        gerente.setResponsabilidades("Gerente de la sucursal");
        gerente.setSueldo(15000.00);
        gerente.setTransacciones(null);
        gerente.setCertificaciones(null);
        gerente.setProyectosActuales(null);
    }

    @Test
    void testGetAllGerentes() throws Exception {
        when(gerenteService.findAll()).thenReturn(List.of(gerente));

        mockMvc.perform(get("/api/gerente/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));

        verify(gerenteService).findAll();
    }

    @Test
    void testGetGerenteById() throws Exception {
        when(gerenteService.findById(1L)).thenReturn(Optional.of(gerente));

        mockMvc.perform(get("/api/gerente/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));

        verify(gerenteService).findById(1L);
    }

    @Test
    void testCreateGerente() throws Exception {
        when(gerenteService.save(any(Gerente.class))).thenReturn(gerente);

        mockMvc.perform(post("/api/gerente/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gerente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan Pablo"))
                .andExpect(jsonPath("$.apellidoPaterno").value("Sanchez"));

        verify(gerenteService).save(any(Gerente.class));
    }

    @Test
    void testUpdateGerente() throws Exception {
        // Configurar el mock para devolver el objeto gerente
        Gerente gerente = new Gerente();
        gerente.setId(1L);
        gerente.setNombre("Juan Pérez");
        when(gerenteService.update(anyLong(), any(Gerente.class))).thenReturn(Optional.of(gerente));

        // Verificar los valores del objeto gerente
        System.out.println("Gerente ID: " + gerente.getId());
        System.out.println("Gerente Nombre: " + gerente.getNombre());

        mockMvc.perform(put("/api/gerente/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gerente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));

        // Verificar que el servicio fue llamado correctamente
        verify(gerenteService).update(anyLong(), any(Gerente.class));
    }

    @Test
    void testPartialUpdateGerente() throws Exception {
        when(gerenteService.findById(anyLong())).thenReturn(Optional.of(gerente));
        when(gerenteService.save(any(Gerente.class))).thenReturn(gerente);

        Map<String, Object> updates = Collections.singletonMap("nombre", "Carlos García");

        mockMvc.perform(patch("/api/gerente/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos García"));

        verify(gerenteService).save(any(Gerente.class));
    }

    @Test
    void testDeleteGerente() throws Exception {
        when(gerenteService.delete(1L)).thenReturn(Optional.of(gerente));

        mockMvc.perform(delete("/api/gerente/delete/1"))
                .andExpect(status().isOk());

        verify(gerenteService).delete(1L);
    }
}
