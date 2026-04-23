package com.nexuscore.repositories.cleaningandclothing;//package com.expedienteclinico.expedienteclinico.repositories.cleaningandclothing;
//
//import com.expedienteclinico.expedienteclinico.models.cleaningandclothing.Textile_movementsModel;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ITextile_movementsRepository extends JpaRepository<Textile_movementsModel, Long> {
//
//    // Buscar por tipo de movimiento
//    List<Textile_movementsModel> findByMovementType(String movementType);
//
//    // Buscar por artículo
//    List<Textile_movementsModel> findByTextileArticle_ArticlesId(Long articleId);
//
//}