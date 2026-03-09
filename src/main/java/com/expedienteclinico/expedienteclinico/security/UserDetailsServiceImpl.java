package com.expedienteclinico.expedienteclinico.security;

import com.expedienteclinico.expedienteclinico.models.system.SystemUsersModel;
import com.expedienteclinico.expedienteclinico.repositories.system.ISystemUsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ISystemUsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUsersModel userEntity = usersRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado en el sistema"));

        // Validar si el usuario está inactivo basándonos en el catálogo (ID 1 es Activo)
        boolean isEnabled = userEntity.getStatus().getId().equals(1L);

        return new User(
                userEntity.getUserName(),
                userEntity.getUserPassword(),
                isEnabled, // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().getRoleName()))
        );
    }
}