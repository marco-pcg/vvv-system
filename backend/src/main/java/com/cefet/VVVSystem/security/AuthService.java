package com.cefet.VVVSystem.security;

import com.cefet.VVVSystem.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(MainUser::new) // Instancia o adaptador usando o seu MainUser.java
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o username: " + username));
    }
}