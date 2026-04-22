package com.nexuscore.repositories.warehouse;

import com.nexuscore.models.warehouse.UnidadMedidaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUnidadMedidaRepository extends JpaRepository<UnidadMedidaModel, Long> {
}