package com.ulsa.oaxaca.edu.proyecto_banco.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Transaccion;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.TransaccionRepository;

@Service
public class TransaccionServiceImpl {

    @Autowired
    private TransaccionRepository transaccionRepository;

    public Transaccion save(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }
}