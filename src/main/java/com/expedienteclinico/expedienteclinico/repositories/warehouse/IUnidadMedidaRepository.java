package com.expedienteclinico.expedienteclinico.repositories.warehouse;

import com.expedienteclinico.expedienteclinico.models.warehouse.UnidadMedidaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUnidadMedidaRepository extends JpaRepository<UnidadMedidaModel, Long> {
}