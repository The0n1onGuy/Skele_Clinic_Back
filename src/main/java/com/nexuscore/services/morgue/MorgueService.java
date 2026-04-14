package com.nexuscore.services.morgue;

import com.nexuscore.beans.morgue.MorgueObject;
import com.nexuscore.models.morgue.Morgue;
import com.nexuscore.repositories.morgue.IMorgueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MorgueService {

    @Autowired
    IMorgueRepository iMorgueRepository;

    public ArrayList<Object> getMorgueList() {
        ArrayList<Object> listaPrueba = new ArrayList<>();
        // Datos de ejemplo para pruebas locales
        listaPrueba.add(new MorgueObject("Juan Pérez", 45, "Infarto", "2026-02-18", 1));
        listaPrueba.add(new MorgueObject("María López", 70, "Causas Naturales", "2026-02-18", 2));
        return listaPrueba;
    }

    public List<Morgue> getAll() {
        return iMorgueRepository.findAll();
    }
    // este metodo recibe el id de coffe y usa el repo para encontrar el registro
    public Morgue buscarPorPaciente(Long idPaciente) {
        return iMorgueRepository.findByPacienteId(idPaciente);
    }
    //prueba mck
    public Morgue buscarPorPacienteLocal(Long idRecibido) {
        // Simulamos que buscamos en una lista y encontramos uno
        Morgue prueba = new Morgue();
        prueba.setId(1L);
        prueba.setIdPacienteLocal(idRecibido); // El ID que tú le des en la URL
        prueba.setNombrePacienteSimulado("Paciente de Prueba " + idRecibido);
        prueba.setCausa_defuncion("Causa Simulada");
        return prueba;
    }

}
