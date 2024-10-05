package com.ulsa.oaxaca.edu.proyecto_banco.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ulsa.oaxaca.edu.proyecto_banco.entities.User;
import com.ulsa.oaxaca.edu.proyecto_banco.repositories.UserRepository;

@Service
public class JpaUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("No se encontro el usuario %s", user));
        }

        User userDb = user.orElseThrow();

        GrantedAuthority authoritie = new SimpleGrantedAuthority(userDb.getRole().getName());

        return new org.springframework.security.core.userdetails.User(
                userDb.getUsername(),
                userDb.getPassword(),
                true,
                true,
                true,
                true,
                List.of(authoritie));
    }

}
