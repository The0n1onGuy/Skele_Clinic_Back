package com.nexuscore.services.cleaningandclothing;//package com.expedienteclinico.expedienteclinico.services.cleaningandclothing;
//
//
//import com.expedienteclinico.expedienteclinico.beans.cleaningandclothing.Textile_movementsObject;
//import com.expedienteclinico.expedienteclinico.models.cleaningandclothing.Textile_movementsModel;
//import com.expedienteclinico.expedienteclinico.repositories.cleaningandclothing.ITextile_movementsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//@Service
//public class Textile_movementsService {
//
//    @Autowired
//    private ITextile_movementsRepository iTextile_movementsRepository;
//
//    public List<Textile_movementsObject> getAll() {
//
//
//        List<Textile_movementsModel> textiles= iTextile_movementsRepository.findAll();
//        List<Textile_movementsObject> dts = new ArrayList<>();
//
//        for (Textile_movementsModel textile : textiles) {
//
//            Textile_movementsObject dtos = new Textile_movementsObject();
//
//            dtos.setId_motion(textile.getMovementId());
//            dtos.setType_motion(textile.getMovementType());
//            dtos.setAmount(textile.getAmount());
//            dtos.setMotion_date(String.valueOf(textile.getMovementDate()));
//            dtos.setObservations(textile.getObservations());
//            if (textile.getTextileArticle() != null) {
//                dtos.setArticuloId(textile.getTextileArticle().getArticlesId());
//            }
//            if (textile.getEmployees() != null) {
//                dtos.setEmployeesNombre(textile.getEmployees().getNombre());
//            }
//            dts.add(dtos);
//        }
//        return dts;
//    }
//}
