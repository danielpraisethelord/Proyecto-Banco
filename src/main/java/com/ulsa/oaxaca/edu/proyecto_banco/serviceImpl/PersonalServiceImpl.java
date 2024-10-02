package com.ulsa.oaxaca.edu.proyecto_banco.serviceImpl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Personal;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.PersonalRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.PersonalService;

@Service
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    private PersonalRepository personalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Personal> findAll() {
        return (List<Personal>) personalRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Personal> findById(Long id) {
        return personalRepository.findById(id);
    }
}
