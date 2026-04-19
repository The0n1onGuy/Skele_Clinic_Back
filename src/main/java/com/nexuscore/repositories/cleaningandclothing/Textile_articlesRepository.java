package com.nexuscore.repositories.cleaningandclothing;//package com.expedienteclinico.expedienteclinico.repositories.cleaningandclothing;
//
//import com.expedienteclinico.expedienteclinico.models.cleaningandclothing.Textile_articlesModel;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface Textile_articlesRepository extends JpaRepository<Textile_articlesModel, Long> {
//
//    // Buscar por nombre
//    Optional<Textile_articlesModel> findByName(String name);
//
//    // Validar existencia
//    boolean existsByName(String name);
//
//    // Buscar por estado
//    List<Textile_articlesModel> findByStatus_Id(Long statusId);
//
//}