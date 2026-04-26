package com.nexuscoreserver.repositories.system;

import com.nexuscoreserver.models.system.SystemUsersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ISystemUsersRepository extends JpaRepository<SystemUsersModel, Long> { // Cambiado de Integer a Long

    // Metodo esencial para que Spring Security busque usuarios por su username
    Optional<SystemUsersModel> findByUserName(String userName);
}