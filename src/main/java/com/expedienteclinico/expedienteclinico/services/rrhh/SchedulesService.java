package com.expedienteclinico.expedienteclinico.services.rrhh;

import com.expedienteclinico.expedienteclinico.beans.rrhh.SchedulesObject;
import com.expedienteclinico.expedienteclinico.models.rrhh.SchedulesModel;
import com.expedienteclinico.expedienteclinico.repositories.rrhh.ISchedulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulesService {
    @Autowired
    ISchedulesRepository schedulesRepository;

    public SchedulesObject convertToDTO(SchedulesModel model) {
        SchedulesObject dto = new SchedulesObject();
        dto.setId(model.getId());
        dto.setUuid(model.getUuid());

        if (model.getEmployee() != null) {
            dto.setEmployeeId(model.getEmployee().getId());
            dto.setEmployeeFullName(String.format("%s %s %s",
                    model.getEmployee().getName(),
                    model.getEmployee().getPatname(),
                    model.getEmployee().getMatname()));
        }

        dto.setDayOfWeek(model.getDayOfWeek());
        dto.setStartTime(model.getStartTime());
        dto.setEndTime(model.getEndTime());

        if (model.getId_status() != null) {
            dto.setStatusName(model.getId_status().getStatusName());
        }

        return dto;
    }

    public List<SchedulesObject> getAll() {
        return schedulesRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }
}
