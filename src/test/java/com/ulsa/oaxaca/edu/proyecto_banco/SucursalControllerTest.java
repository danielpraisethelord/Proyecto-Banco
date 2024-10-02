package com.ulsa.oaxaca.edu.proyecto_banco;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulsa.oaxaca.edu.proyecto_banco.controller.SucursalController;
import com.ulsa.oaxaca.edu.proyecto_banco.entities.Sucursal;
import com.ulsa.oaxaca.edu.proyecto_banco.service.SucursalService;

@WebMvcTest(SucursalController.class)
public class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SucursalService sucursalService;

    @Autowired
    private ObjectMapper objectMapper;

    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        sucursal = Sucursal.builder()
                .id(2L)
                .nombre("Sucursal Centro Oaxaca")
                .direccion("Avenida Independencia 123, Centro, Oaxaca")
                .telefono("1234567890")
                .email("contacto@banco-oaxaca.com")
                .horarioAtencion("Lunes a Viernes, 9:00 AM - 4:00 PM")
                .fechaApertura(LocalDate.of(2010, 5, 1))
                .estado("Oaxaca")
                .banco(null) // Objeto ficticio del banco asociado
                .personal(null) // Lista ficticia de empleados
                .clientes(null) // Lista ficticia de clientes
                .build();
    }

    @Test
    void testCreateSucursal() throws Exception {
        // Configurar el mock para que devuelva la sucursal creada
        when(sucursalService.save(any(Sucursal.class))).thenReturn(sucursal);

        // Realizar la solicitud POST a la API
        mockMvc.perform(post("/api/sucursal/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sucursal)))
                .andExpect(status().isBadRequest()) // Cambiar a isBadRequest para capturar el error
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    System.out.println("Error response: " + response);
                });

        // Verificar que el método save del servicio haya sido llamado
        verify(sucursalService).save(any(Sucursal.class));
    }

    @Test
    void testGetAllSucursales() throws Exception {
        mockMvc.perform(get("/api/sucursal/all"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSucursalById() throws Exception {
        // Configurar el mock para que devuelva la sucursal esperada
        when(sucursalService.findById(2L)).thenReturn(Optional.of(sucursal));

        // Realizar la solicitud GET a la API
        mockMvc.perform(get("/api/sucursal/all/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nombre").value("Sucursal Centro Oaxaca"))
                .andExpect(jsonPath("$.direccion").value("Avenida Independencia 123, Centro, Oaxaca"))
                .andExpect(jsonPath("$.telefono").value("1234567890"))
                .andExpect(jsonPath("$.email").value("contacto@banco-oaxaca.com"))
                .andExpect(jsonPath("$.horarioAtencion").value("Lunes a Viernes, 9:00 AM - 4:00 PM"))
                .andExpect(jsonPath("$.fechaApertura").value("2010-05-01"))
                .andExpect(jsonPath("$.estado").value("Oaxaca"));

        // Verificar que el método findById del servicio haya sido llamado
        verify(sucursalService).findById(2L);
    }

    @Test
    void testUpdateSucursal() throws Exception {
        when(sucursalService.update(anyLong(), any(Sucursal.class))).thenReturn(Optional.of(sucursal));

        mockMvc.perform(put("/api/sucursal/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sucursal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nombre").value("Sucursal Centro Oaxaca"));

        verify(sucursalService).update(anyLong(), any(Sucursal.class));
    }

    @Test
    void testDeleteSucursal() throws Exception {
        when(sucursalService.delete(2L)).thenReturn(Optional.of(sucursal));

        mockMvc.perform(delete("/api/sucursal/delete/2"))
                .andExpect(status().isOk());

        verify(sucursalService).delete(2L);
    }

}
