package com.ulsa.oaxaca.edu.proyecto_banco.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.Persona;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.PersonaRepository;
import com.ulsa.oaxaca.edu.proyecto_banco.service.PersonaService;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Transactional
    @Override
    public Persona save(Persona persona) {
        return personaRepository.save(persona);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Persona> findAll() {
        return (List<Persona>) personaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Persona> findById(Long id) {
        return personaRepository.findById(id);
    }

    @Transactional
    @Modifying
    @Override
    public Optional<Persona> update(Long id, Persona persona) {
        Optional<Persona> personaOptional = personaRepository.findById(id);
        if (personaOptional.isPresent()) {
            Persona personaDb = personaOptional.orElseThrow();
            personaDb.setNombre(persona.getNombre());
            personaDb.setApellidoPaterno(persona.getApellidoPaterno());
            personaDb.setApellidoMaterno(persona.getApellidoMaterno());
            personaDb.setFechaNacimiento(persona.getFechaNacimiento());
            personaDb.setGenero(persona.getGenero());
            personaDb.setTelefono(persona.getTelefono());
            personaDb.setEmail(persona.getEmail());
            personaDb.setDireccion(persona.getDireccion());
            personaDb.setRfc(persona.getRfc());
            personaDb.setDiscapacidad(persona.getDiscapacidad());
            personaDb.setEstadoCivil(persona.getEstadoCivil());
            personaDb.setNivelDeEstudios(persona.getNivelDeEstudios());
            return Optional.of(personaRepository.save(personaDb));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    @Modifying
    public Optional<Persona> delete(Long id) {
        Optional<Persona> persona = personaRepository.findById(id);
        persona.ifPresent(personaRepository::delete);
        return persona;
    }
}