package com.nexuscore.repositories.system;

import com.nexuscore.models.system.HttpStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IHttpStatusCodeRepository extends JpaRepository<HttpStatusCode, Long> {

    Optional<HttpStatusCode> findByCode(int code);
}