package com.expedienteclinico.expedienteclinico.services.Patients;

import com.expedienteclinico.expedienteclinico.beans.Patients.PatientsBean;
import com.expedienteclinico.expedienteclinico.models.Patients.PatientsModel;
import com.expedienteclinico.expedienteclinico.repositories.Patients.IPatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientsService {

    @Autowired
    private IPatientsRepository repository;

    // 1. Método para obtener todo el catálogo convertido a DTO (Para GET /patients-list)
    public List<PatientsBean> getAllPatients() {
        List<PatientsModel> entidades = repository.findAll();
        List<PatientsBean> dtos = new ArrayList<>();

        for (PatientsModel entidad : entidades) {
            PatientsBean dto = new PatientsBean();
            dto.setId(entidad.getId());
            dto.setNombre(entidad.getNombre());
            dto.setApellidos(entidad.getApellidos());
            dto.setCurp(entidad.getCurp());
            dto.setFechaNacimiento(entidad.getFechaNacimiento());
            dto.setGenero(entidad.getGenero());
            dto.setTelefono(entidad.getTelefono());
            dto.setEmail(entidad.getEmail());
            dto.setDireccion(entidad.getDireccion());
            dto.setTipoSangre(entidad.getTipoSangre());
            dtos.add(dto);
        }
        return dtos;
    }

    // 2. Método para crear un nuevo paciente (Para POST /new)
    public PatientsModel nuevo(PatientsBean bean) {
        PatientsModel entidad = new PatientsModel();

        entidad.setNombre(bean.getNombre());
        entidad.setApellidos(bean.getApellidos());
        entidad.setCurp(bean.getCurp());
        entidad.setFechaNacimiento(bean.getFechaNacimiento());
        entidad.setGenero(bean.getGenero());
        entidad.setTelefono(bean.getTelefono());
        entidad.setEmail(bean.getEmail());
        entidad.setDireccion(bean.getDireccion());
        entidad.setTipoSangre(bean.getTipoSangre());

        return repository.save(entidad);
    }

    // 3. Mé) //
    public ResponseEntity<?> updateData(PatientsBean bean, BindingResult result, Long id) {
        Map<String, Object> response = new HashMap<>();

        // Revisar si el JSON venía con errores de formato
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errores", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Buscar al paciente en la base de datos
        PatientsModel pacienteActual = repository.findById(id).orElse(null);

        if (pacienteActual == null) {
            response.put("mensaje", "Error: no se pudo editar, el paciente ID: " + id + " no existe.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            // Reemplazar los datos viejos
            pacienteActual.setNombre(bean.getNombre());
            pacienteActual.setApellidos(bean.getApellidos());
            pacienteActual.setCurp(bean.getCurp());
            pacienteActual.setFechaNacimiento(bean.getFechaNacimiento());
            pacienteActual.setGenero(bean.getGenero());
            pacienteActual.setTelefono(bean.getTelefono());
            pacienteActual.setEmail(bean.getEmail());
            pacienteActual.setDireccion(bean.getDireccion());
            pacienteActual.setTipoSangre(bean.getTipoSangre());

            repository.save(pacienteActual);

            response.put("mensaje", "El paciente ha sido actualizado con éxito");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            // Brecha de seguridad 3 cerrada: Mensaje genérico en lugar de e.getMessage()
            System.out.println("Error interno: " + e.getMessage()); // Log para ti como desarrollador
            response.put("mensaje", "Error al actualizar el paciente en la base de datos");
            response.put("error", "Ocurrió un error interno en el servidor."); // Mensaje seguro para el frontend
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}